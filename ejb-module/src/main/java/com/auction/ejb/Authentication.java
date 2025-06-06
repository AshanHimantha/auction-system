package com.auction.ejb;

import jakarta.ejb.Local;

@Local
public interface Authentication {
    String login(String username, String password);
    boolean validateToken(String token);
    String getUsernameFromToken(String token);
    String getRoleFromToken(String token); // NEW METHOD
    void logout(String token);
    boolean registerUser(String username, String password);
}