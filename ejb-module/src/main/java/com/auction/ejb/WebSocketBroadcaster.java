package com.auction.ejb;

import jakarta.ejb.Local;

@Local
public interface WebSocketBroadcaster {
    void broadcast(String message);
}