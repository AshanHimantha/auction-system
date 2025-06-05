package com.auction.jms;

import jakarta.ejb.*;
import jakarta.jms.*;
import com.auction.ejb.AuctionWebSocketEndpointLocal;
import java.util.logging.Level;
import java.util.logging.Logger;

@MessageDriven(activationConfig = {
        @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "jms/AuctionUpdatesTopic"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "jakarta.jms.Topic"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
})
@TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
public class AuctionUpdateMDB implements MessageListener {

    private static final Logger LOGGER = Logger.getLogger(AuctionUpdateMDB.class.getName());

    @EJB
    private AuctionWebSocketEndpointLocal webSocketBroadcaster;

    @Override
    public void onMessage(Message message) {
        if (message instanceof MapMessage) {
            try {
                MapMessage mapMessage = (MapMessage) message;
                String updateJson = buildJsonUpdate(mapMessage);
                LOGGER.log(Level.INFO, "Broadcasting update: {0}", updateJson);
                webSocketBroadcaster.broadcast(updateJson);
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error processing message: {0}", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private String buildJsonUpdate(MapMessage mapMessage) throws JMSException {
        Long auctionId = mapMessage.getLong("auctionId");
        double currentBid = mapMessage.getDouble("currentBid");
        String winningBidder = mapMessage.getString("bidderName");
        String title = mapMessage.getString("title");

        return String.format(
                "{\"auctionId\": %d, \"currentBid\": %.2f, \"winningBidder\": \"%s\", \"title\": \"%s\"}",
                auctionId, currentBid, winningBidder, title
        );
    }
}