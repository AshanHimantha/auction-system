package com.auction.jms;

import com.auction.ejb.AuctionInMemoryStorageSingleton;
import com.auction.entity.Auction;
import com.auction.entity.AuctionBidHistory; // NEW IMPORT
import com.auction.entity.AuctionStatus;
import jakarta.ejb.MessageDriven;
import jakarta.ejb.ActivationConfigProperty;
import jakarta.jms.MessageListener;
import jakarta.jms.Message;
import jakarta.jms.MapMessage;
import jakarta.jms.JMSException;
import jakarta.annotation.Resource;
import jakarta.jms.JMSContext;
import jakarta.jms.Topic;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.ejb.EJB;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/BidQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BidProcessorMDB implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(BidProcessorMDB.class.getName());

    @EJB
    private AuctionInMemoryStorageSingleton auctionStorage;

    @jakarta.inject.Inject
    private JMSContext jmsContext;

    @Resource(lookup = "jms/AuctionUpdatesTopic")
    private Topic auctionUpdatesTopic;

    @Override
    public void onMessage(Message message) {
        if (message instanceof MapMessage) {
            try {
                MapMessage mapMessage = (MapMessage) message;
                Long auctionId = mapMessage.getLong("auctionId");
                double bidAmount = mapMessage.getDouble("bidAmount");
                String bidderName = mapMessage.getString("bidderName");

                LOGGER.log(Level.INFO, "DEBUG (BidProcessorMDB): Bidder Name extracted from incoming JMS message: " + bidderName);

                Auction currentAuction = auctionStorage.getAuction(auctionId);
                if (currentAuction == null) {
                    System.err.println("MDB: Auction with ID " + auctionId + " not found. Discarding bid.");
                    return;
                }

                Auction auctionToUpdate = new Auction();
                auctionToUpdate.setId(currentAuction.getId());
                auctionToUpdate.setTitle(currentAuction.getTitle());
                auctionToUpdate.setDescription(currentAuction.getDescription()); // Include new fields
                auctionToUpdate.setImageUrls(currentAuction.getImageUrls());   // Include new fields
                auctionToUpdate.setStartPrice(currentAuction.getStartPrice()); // Include new fields
                auctionToUpdate.setCurrentBid(currentAuction.getCurrentBid());
                auctionToUpdate.setWinningBidder(currentAuction.getWinningBidder());
                auctionToUpdate.setMinIncrement(currentAuction.getMinIncrement());
                auctionToUpdate.setStartTime(currentAuction.getStartTime());   // Include new fields
                auctionToUpdate.setEndTime(currentAuction.getEndTime());       // Include new fields
                auctionToUpdate.setStatus(currentAuction.getStatus());
                auctionToUpdate.setCategoryId(currentAuction.getCategoryId()); // NEW: Include categoryId
                auctionToUpdate.setVersion(currentAuction.getVersion());

                if (auctionToUpdate.getStatus() != AuctionStatus.ACTIVE) {
                    System.err.println("MDB: Auction " + auctionId + " is not active. Status: " + auctionToUpdate.getStatus() + ". Discarding bid.");
                    return;
                }

                if (bidAmount <= auctionToUpdate.getCurrentBid() + auctionToUpdate.getMinIncrement()) {
                    System.out.println("MDB: Bid amount " + bidAmount + " for auction " + auctionId + " is too low (current: " + auctionToUpdate.getCurrentBid() + ", min increment: " + auctionToUpdate.getMinIncrement() + "). Discarding bid.");
                    return;
                }

                auctionToUpdate.setCurrentBid(bidAmount);
                auctionToUpdate.setWinningBidder(bidderName);
                auctionStorage.addOrUpdateAuction(auctionToUpdate);

                // Create the bid history entry with the current timestamp
                AuctionBidHistory bidHistoryEntry = new AuctionBidHistory(auctionId, bidderName, bidAmount);
                LocalDateTime bidTime = LocalDateTime.now();
                bidHistoryEntry.setBidTime(bidTime);
                auctionStorage.addBidToHistory(auctionId, bidHistoryEntry);


                LOGGER.log(Level.INFO, "MDB: Successfully updated auction " + auctionId + " with new bid " + bidAmount + " by " + bidderName);

                MapMessage updateMessage = jmsContext.createMapMessage();
                updateMessage.setLong("auctionId", auctionId);
                updateMessage.setDouble("currentBid", auctionToUpdate.getCurrentBid());

                String finalWinningBidder = (auctionToUpdate.getWinningBidder() != null && !auctionToUpdate.getWinningBidder().isEmpty())
                        ? auctionToUpdate.getWinningBidder()
                        : "None";
                updateMessage.setString("winningBidder", finalWinningBidder);
                updateMessage.setString("title", auctionToUpdate.getTitle());
                updateMessage.setString("description", auctionToUpdate.getDescription()); // Include for updates if needed
                updateMessage.setStringProperty("imageUrls", String.join(",", auctionToUpdate.getImageUrls())); // Send image URLs as comma-separated string
                updateMessage.setDouble("startPrice", auctionToUpdate.getStartPrice());
                updateMessage.setDouble("minIncrement", auctionToUpdate.getMinIncrement());
                updateMessage.setString("startTime", auctionToUpdate.getStartTime().toString()); // Send as ISO string
                updateMessage.setString("endTime", auctionToUpdate.getEndTime().toString()); // Send as ISO string
                updateMessage.setLong("categoryId", auctionToUpdate.getCategoryId()); // NEW: Include category ID
                updateMessage.setString("status", auctionToUpdate.getStatus().name()); // NEW: Include status

                // Add bid history info to message
                updateMessage.setString("lastBidderName", bidderName);
                updateMessage.setDouble("lastBidAmount", bidAmount);
                updateMessage.setString("lastBidTime", bidTime.toString());

                jmsContext.createProducer().send(auctionUpdatesTopic, updateMessage);
                LOGGER.log(Level.INFO, "MDB: Sent auction update to topic for auction " + auctionId + " with bidder: " + finalWinningBidder);

            } catch (RuntimeException e) {
                LOGGER.log(Level.SEVERE, "MDB: Error processing bid message (runtime exception): " + e.getMessage(), e);
                throw new jakarta.ejb.EJBException("Error during bid processing", e);
            } catch (JMSException e) {
                LOGGER.log(Level.SEVERE, "MDB: JMS error during message processing: " + e.getMessage(), e);
                throw new jakarta.ejb.EJBException("JMS Exception", e);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "MDB: Unexpected error during bid processing: " + e.getMessage(), e);
                e.printStackTrace();
                throw new jakarta.ejb.EJBException("Unexpected error", e);
            }
        } else {
            LOGGER.log(Level.WARNING, "MDB: Received non-MapMessage: " + message.getClass().getName());
        }
    }
}