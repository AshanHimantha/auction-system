package com.auction.ejb;

import jakarta.ejb.Singleton;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
public class WebSocketRegistryEJB implements AuctionWebSocketEndpointLocal {

    private static final Logger LOGGER = Logger.getLogger(WebSocketRegistryEJB.class.getName());

    @Inject
    private Event<String> messageEvent;

    @Override
    public void broadcast(String message) {
        LOGGER.log(Level.INFO, "Broadcasting message via EJB: {0}", message);
        messageEvent.fire(message);
    }
}