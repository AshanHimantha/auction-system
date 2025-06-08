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

                // Extract all relevant data from the JMS MapMessage
                Long auctionId = mapMessage.getLong("auctionId");
                double currentBid = mapMessage.getDouble("currentBid");
                String winningBidder = mapMessage.getString("winningBidder"); // This is where we get the bidder string
                String title = mapMessage.getString("title");

                // NEW FIELDS from Auction model (if you want them in real-time updates)
                String description = mapMessage.getString("description");
                String imageUrls = mapMessage.getStringProperty("imageUrls"); // As a comma-separated string
                double startPrice = mapMessage.getDouble("startPrice");
                double minIncrement = mapMessage.getDouble("minIncrement");
                String startTime = mapMessage.getString("startTime"); // As ISO string
                String endTime = mapMessage.getString("endTime");     // As ISO string
                Long categoryId = mapMessage.getLong("categoryId");
                String status = mapMessage.getString("status");

                // Extract bid history data
                String lastBidderName = null;
                double lastBidAmount = 0;
                String lastBidTime = null;

                // Try to get the bid history data, but don't fail if it's not there
                try {
                    lastBidderName = mapMessage.getString("lastBidderName");
                    lastBidAmount = mapMessage.getDouble("lastBidAmount");
                    lastBidTime = mapMessage.getString("lastBidTime");
                } catch (JMSException e) {
                    LOGGER.log(Level.INFO, "No bid history data in message, may be an initialization update");
                }

                // DEBUG LOG: Check bidder value immediately after extraction
                LOGGER.log(Level.INFO, "DEBUG (AuctionUpdateMDB): Extracted winningBidder: '" + winningBidder + "' for Auction ID " + auctionId);

                // IMPORTANT: Ensure winningBidder is not null or empty before including in JSON
                // If it's null or empty, use "None" for display.
                String bidderForJson = (winningBidder != null && !winningBidder.isEmpty()) ? winningBidder : "None";

                // Build the main auction data JSON
                StringBuilder jsonBuilder = new StringBuilder();
                jsonBuilder.append("{")
                    .append("\"auctionId\": ").append(auctionId).append(", ")
                    .append("\"currentBid\": ").append(currentBid).append(", ")
                    .append("\"winningBidder\": \"").append(bidderForJson).append("\", ")
                    .append("\"title\": \"").append(title).append("\", ")
                    .append("\"description\": \"").append(description).append("\", ")
                    .append("\"imageUrls\": [\"").append(imageUrls != null ? imageUrls.replace(",", "\",\"") : "").append("\"], ")
                    .append("\"startPrice\": ").append(startPrice).append(", ")
                    .append("\"minIncrement\": ").append(minIncrement).append(", ")
                    .append("\"startTime\": \"").append(startTime).append("\", ")
                    .append("\"endTime\": \"").append(endTime).append("\", ")
                    .append("\"categoryId\": ").append(categoryId).append(", ")
                    .append("\"status\": \"").append(status).append("\"");

                // Add bid history data if available
                if (lastBidderName != null && lastBidTime != null) {
                    jsonBuilder.append(", ")
                        .append("\"lastBid\": {")
                        .append("\"bidderName\": \"").append(lastBidderName).append("\", ")
                        .append("\"bidAmount\": ").append(lastBidAmount).append(", ")
                        .append("\"bidTime\": \"").append(lastBidTime).append("\"")
                        .append("}");
                }

                // Close the JSON object
                jsonBuilder.append("}");

                String updateJson = jsonBuilder.toString();

                LOGGER.log(Level.INFO, "AuctionUpdateMDB received JMS message. Processing and broadcasting to WebSockets.");

                // Check if the EJB was successfully injected before calling its method
                if (webSocketBroadcaster != null) {
                    webSocketBroadcaster.broadcast(updateJson); // Call method on injected interface
                } else {
                    // This indicates a critical deployment issue if it happens at runtime
                    LOGGER.log(Level.SEVERE, "WebSocketBroadcaster EJB was not injected! Real-time updates will not work.");
                }

            } catch (JMSException e) {
                LOGGER.log(Level.SEVERE, "AuctionUpdateMDB: JMS error while reading message from topic. " + e.getMessage(), e);
            } catch (Exception e) { // Catch any other unexpected runtime exceptions
                LOGGER.log(Level.SEVERE, "AuctionUpdateMDB: Unexpected error during message processing: " + e.getMessage(), e);
                e.printStackTrace();
            }
        } else {
            // Handle unexpected message types
            LOGGER.log(Level.WARNING, "AuctionUpdateMDB: Received unexpected JMS message type: " + message.getClass().getName() + ". Expected MapMessage.");
        }
    }

  private String buildJsonUpdate(MapMessage mapMessage) throws JMSException {
      Long auctionId = mapMessage.getLong("auctionId");
      double currentBid = mapMessage.getDouble("currentBid");
      String winningBidder = mapMessage.getString("winningBidder");
      String title = mapMessage.getString("title");

      // Ensure winning bidder is never null in the JSON
      if (winningBidder == null || winningBidder.isEmpty()) {
          winningBidder = "None";
      }

      return String.format(
              "{\"auctionId\": %d, \"currentBid\": %.2f, \"winningBidder\": \"%s\", \"title\": \"%s\"}",
              auctionId, currentBid, winningBidder, title
      );
  }
}