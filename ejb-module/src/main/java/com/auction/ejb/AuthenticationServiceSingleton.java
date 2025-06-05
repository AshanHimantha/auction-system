package com.auction.ejb;

import com.auction.entity.User;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

@Singleton
@Startup
public class AuthenticationServiceSingleton {

    private static final Logger LOGGER = Logger.getLogger(AuthenticationServiceSingleton.class.getName());

    private final Map<String, User> users = new ConcurrentHashMap<>();
    private final Map<String, String> activeTokens = new ConcurrentHashMap<>();
    private final Map<String, LocalDateTime> tokenExpirations = new ConcurrentHashMap<>();

    private static final long TOKEN_VALIDITY_MINUTES = 60;

    @PostConstruct
    public void init() {
        LOGGER.log(Level.INFO, "AuthenticationServiceSingleton initialized. Populating initial users.");
        users.put("user1", new User("user1", "pass1"));
        users.put("user2", new User("user2", "pass2"));
        LOGGER.log(Level.INFO, "Populated " + users.size() + " initial users.");
    }

    @Lock(LockType.READ)
    public boolean authenticate(String username, String password) {
        User user = users.get(username);
        return user != null && user.getPasswordHash().equals(password);
    }

    @Lock(LockType.WRITE)
    public String generateToken(String username) {
        activeTokens.entrySet().stream()
                .filter(entry -> entry.getValue().equals(username))
                .map(Map.Entry::getKey)
                .forEach(this::invalidateToken);

        String token = UUID.randomUUID().toString();
        activeTokens.put(token, username);
        tokenExpirations.put(token, LocalDateTime.now().plusMinutes(TOKEN_VALIDITY_MINUTES));
        LOGGER.log(Level.INFO, "Generated token for user: " + username + " - Token: " + token);
        return token;
    }

    @Lock(LockType.READ)
    public boolean validateToken(String token) {
        String username = activeTokens.get(token);
        if (username == null) {
            LOGGER.log(Level.WARNING, "Invalid token: " + token + " (not found)");
            return false;
        }
        LocalDateTime expiration = tokenExpirations.get(token);
        if (expiration == null || LocalDateTime.now().isAfter(expiration)) {
            LOGGER.log(Level.WARNING, "Invalid token: " + token + " (expired)");
            invalidateToken(token);
            return false;
        }
        return true;
    }

    @Lock(LockType.READ)
    public String getUsernameFromToken(String token) {
        return activeTokens.get(token);
    }

    @Lock(LockType.WRITE)
    public void invalidateToken(String token) {
        String username = activeTokens.remove(token);
        if (username != null) {
            tokenExpirations.remove(token);
            LOGGER.log(Level.INFO, "Invalidated token for user: " + username + " - Token: " + token);
        } else {
            LOGGER.log(Level.WARNING, "Attempted to invalidate non-existent token: " + token);
        }
    }
}