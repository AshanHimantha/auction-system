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

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/BidQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Queue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BidProcessorMDB implements MessageListener {

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

                System.out.println("MDB processing bid for auction " + auctionId + " by " + bidderName + " amount " + bidAmount);

                Auction currentAuction = auctionStorage.getAuction(auctionId);
                if (currentAuction == null) {
                    System.err.println("MDB: Auction with ID " + auctionId + " not found. Discarding bid.");
                    return;
                }

                Auction auctionToUpdate = new Auction();
                auctionToUpdate.setId(currentAuction.getId());
                auctionToUpdate.setTitle(currentAuction.getTitle());
                auctionToUpdate.setCurrentBid(currentAuction.getCurrentBid());
                auctionToUpdate.setWinningBidder(currentAuction.getWinningBidder());
                auctionToUpdate.setMinIncrement(currentAuction.getMinIncrement());
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
                auctionToUpdate.setWinningBidder(bidderName); // <-- THIS LINE SETS THE BIDDER ON THE OBJECT
                auctionStorage.addOrUpdateAuction(auctionToUpdate); // This commits the change to the map

                System.out.println("MDB: Successfully updated auction " + auctionId + " with new bid " + bidAmount + " by " + bidderName);

                // Publish update to WebSocket clients via JMS Topic
                MapMessage updateMessage = jmsContext.createMapMessage();
                updateMessage.setLong("auctionId", auctionId);
                updateMessage.setDouble("currentBid", auctionToUpdate.getCurrentBid()); // Use the updated value
                updateMessage.setString("winningBidder", auctionToUpdate.getWinningBidder()); // <-- CRITICAL: Use the bidder from the UPDATED object
                updateMessage.setString("title", auctionToUpdate.getTitle());
                jmsContext.createProducer().send(auctionUpdatesTopic, updateMessage);
                System.out.println("MDB: Sent auction update to topic for auction " + auctionId);

            } catch (RuntimeException e) {
                System.err.println("MDB: Error processing bid message (runtime exception): " + e.getMessage());
                throw new jakarta.ejb.EJBException("Error during bid processing", e);
            } catch (JMSException e) {
                System.err.println("MDB: JMS error during message processing: " + e.getMessage());
                throw new jakarta.ejb.EJBException("JMS Exception", e);
            } catch (Exception e) {
                System.err.println("MDB: Unexpected error during bid processing: " + e.getMessage());
                e.printStackTrace();
                throw new jakarta.ejb.EJBException("Unexpected error", e);
            }
        } else {
            System.err.println("MDB: Received non-MapMessage: " + message.getClass().getName());
        }
    }
}