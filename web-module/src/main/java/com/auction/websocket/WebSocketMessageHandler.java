package com.auction.websocket;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.websocket.Session;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class WebSocketMessageHandler {
    private static final Logger LOGGER = Logger.getLogger(WebSocketMessageHandler.class.getName());
    private final Set<Session> sessions = Collections.synchronizedSet(new HashSet<>());

    public void addSession(Session session) {
        sessions.add(session);
        LOGGER.log(Level.INFO, "Session added: {0}, total: {1}",
                new Object[]{session.getId(), sessions.size()});
    }

    public void removeSession(Session session) {
        sessions.remove(session);
        LOGGER.log(Level.INFO, "Session removed: {0}, total: {1}",
                new Object[]{session.getId(), sessions.size()});
    }

    public void onMessage(@Observes String message) {
        LOGGER.log(Level.INFO, "Broadcasting to {0} sessions", sessions.size());
        for (Session session : sessions) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Error sending to session {0}: {1}",
                            new Object[]{session.getId(), e.getMessage()});
                }
            }
        }
    }
}