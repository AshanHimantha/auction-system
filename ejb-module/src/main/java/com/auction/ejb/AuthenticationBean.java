package com.auction.ejb;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

@Stateless
public class AuthenticationBean implements Authentication {

    @EJB
    private AuthenticationServiceSingleton authService;

    @Override
    public String login(String username, String password) {
        if (authService.authenticate(username, password)) {
            return authService.generateToken(username);
        }
        return null;
    }

    @Override
    public boolean validateToken(String token) {
        return authService.validateToken(token);
    }

    @Override
    public String getUsernameFromToken(String token) {
        return authService.getUsernameFromToken(token);
    }

    @Override
    public void logout(String token) {
        authService.invalidateToken(token);
    }

    @Override // NEW METHOD IMPLEMENTATION
    public boolean registerUser(String username, String password) {
        return authService.registerUser(username, password);
    }
}