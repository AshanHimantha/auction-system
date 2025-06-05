package com.auction.ejb;

import jakarta.ejb.Stateless;
import jakarta.annotation.Resource;
import jakarta.jms.JMSContext;
import jakarta.jms.Queue;
import jakarta.jms.JMSException;
import jakarta.jms.MapMessage;

@Stateless
public class BidManagerBean implements BidManager {

    @jakarta.inject.Inject
    private JMSContext jmsContext;

    @Resource(lookup = "jms/BidQueue")
    private Queue bidQueue;

    @Override
    public String submitBid(Long auctionId, double bidAmount, String bidderName) {
        if (auctionId == null || bidAmount <= 0 || bidderName == null || bidderName.trim().isEmpty()) {
            return "Invalid bid data provided.";
        }

        try {
            MapMessage message = jmsContext.createMapMessage();
            message.setLong("auctionId", auctionId);
            message.setDouble("bidAmount", bidAmount);
            message.setString("bidderName", bidderName);

            jmsContext.createProducer().send(bidQueue, message);
            System.out.println("Bid for auction " + auctionId + " by " + bidderName + " submitted to JMS queue.");
            return "Bid received for processing.";
        } catch (JMSException e) {
            System.err.println("Error sending bid message to JMS: " + e.getMessage());
            e.printStackTrace();
            return "Error submitting bid. Please try again.";
        }
    }
}