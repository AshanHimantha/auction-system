package com.auction.ejb;

import jakarta.ejb.Local;

@Local
public interface AuctionWebSocketEndpointLocal {
    // Method moved from WebSocketBroadcaster interface
    void broadcast(String message);
}