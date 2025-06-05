package com.auction.websocket;

import jakarta.inject.Inject;
import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@ServerEndpoint("/auctionUpdates")
public class AuctionWebSocketEndpoint {

    private static final Logger LOGGER = Logger.getLogger(AuctionWebSocketEndpoint.class.getName());

    @Inject
    private WebSocketMessageHandler messageHandler;

    @OnOpen
    public void onOpen(Session session) {
        messageHandler.addSession(session);
        try {
            session.getBasicRemote().sendText("Welcome to the Auction! Real-time updates enabled.");
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error sending welcome message: {0}", e.getMessage());
        }
    }

    @OnClose
    public void onClose(Session session, CloseReason closeReason) {
        messageHandler.removeSession(session);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        LOGGER.log(Level.SEVERE, "WebSocket error: {0}", throwable.getMessage());
        messageHandler.removeSession(session);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        LOGGER.log(Level.INFO, "Message from {0}: {1}", new Object[]{session.getId(), message});
        try {
            session.getBasicRemote().sendText("Server received: " + message);
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error sending response: {0}", e.getMessage());
        }
    }
}