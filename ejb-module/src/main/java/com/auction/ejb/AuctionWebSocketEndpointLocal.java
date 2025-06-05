package com.auction.ejb;

import jakarta.ejb.Local;

@Local
public interface AuctionWebSocketEndpointLocal extends WebSocketBroadcaster {
    // No methods needed here, as it inherits 'broadcast' from WebSocketBroadcaster
}