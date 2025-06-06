package com.auction.jms;

import com.auction.ejb.AuctionInMemoryStorageSingleton;
import com.auction.entity.Auction;
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
                long auctionId = mapMessage.getLong("auctionId");
                double bidAmount = mapMessage.getDouble("bidAmount");
                String bidderName = mapMessage.getString("bidderName"); // This is the bidder name from the *incoming* message

                LOGGER.log(Level.INFO, "DEBUG (BidProcessorMDB): Bidder Name extracted from incoming JMS message: " + bidderName);

                Auction currentAuction = auctionStorage.getAuction(auctionId);
                if (currentAuction == null) {
                    System.err.println("MDB: Auction with ID " + auctionId + " not found. Discarding bid.");
                    return;
                }

                // Create a mutable copy to update
                Auction auctionToUpdate = new Auction();
                auctionToUpdate.setId(currentAuction.getId());
                auctionToUpdate.setTitle(currentAuction.getTitle());
                auctionToUpdate.setDescription(currentAuction.getDescription());
                auctionToUpdate.setImageUrls(currentAuction.getImageUrls());
                auctionToUpdate.setStartPrice(currentAuction.getStartPrice());
                auctionToUpdate.setCurrentBid(currentAuction.getCurrentBid());
                auctionToUpdate.setWinningBidder(currentAuction.getWinningBidder()); // Copy existing bidder first
                auctionToUpdate.setMinIncrement(currentAuction.getMinIncrement());
                auctionToUpdate.setStartTime(currentAuction.getStartTime());
                auctionToUpdate.setEndTime(currentAuction.getEndTime());
                auctionToUpdate.setStatus(currentAuction.getStatus());
                auctionToUpdate.setVersion(currentAuction.getVersion());

                if (auctionToUpdate.getStatus() != AuctionStatus.ACTIVE) {
                    System.err.println("MDB: Auction " + auctionId + " is not active. Status: " + auctionToUpdate.getStatus() + ". Discarding bid.");
                    return;
                }

                if (bidAmount <= auctionToUpdate.getCurrentBid() + auctionToUpdate.getMinIncrement()) {
                    System.out.println("MDB: Bid amount " + bidAmount + " for auction " + auctionId + " is too low (current: " + auctionToUpdate.getCurrentBid() + ", min increment: " + auctionToUpdate.getMinIncrement() + "). Discarding bid.");
                    return;
                }

                // Update auction in in-memory storage (this is where winningBidder is set on the object)
                auctionToUpdate.setCurrentBid(bidAmount);
                auctionToUpdate.setWinningBidder(bidderName); // The bidderName is set on the object here
                auctionStorage.addOrUpdateAuction(auctionToUpdate); // This commits the change to the map

                LOGGER.log(Level.INFO, "MDB: Successfully updated auction " + auctionId + " with new bid " + bidAmount + " by " + bidderName);

                // Publish update to WebSocket clients via JMS Topic
                MapMessage updateMessage = jmsContext.createMapMessage();
                updateMessage.setLong("auctionId", auctionId);
                updateMessage.setDouble("currentBid", auctionToUpdate.getCurrentBid());

                // **** CRITICAL CHANGE HERE: Use the original 'bidderName' directly, ensuring it's non-null ****
                String finalBidderForTopic = (bidderName != null && !bidderName.isEmpty()) ? bidderName : "None";
                updateMessage.setString("winningBidder", finalBidderForTopic); // Use the direct, non-null string

                updateMessage.setString("title", auctionToUpdate.getTitle());

                jmsContext.createProducer().send(auctionUpdatesTopic, updateMessage);
                LOGGER.log(Level.INFO, "MDB: Sent auction update to topic for auction " + auctionId + " with bidder: " + finalBidderForTopic); // Log the sent value

            } catch (RuntimeException e) {
                LOGGER.log(Level.SEVERE, "MDB: Error processing bid message (runtime exception): " + e.getMessage(), e);
                throw new jakarta.ejb.EJBException("Error during bid processing", e);
            } catch (JMSException e) {
                LOGGER.log(Level.SEVERE, "MDB: JMS error during message processing: " + e.getMessage(), e);
                throw new jakarta.ejb.EJBException("JMS Exception", e);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "MDB: Unexpected error during bid processing: " + e.getMessage(), e);

                throw new jakarta.ejb.EJBException("Unexpected error", e);
            }
        } else {
            LOGGER.log(Level.WARNING, "MDB: Received non-MapMessage: " + message.getClass().getName());
        }
    }
}