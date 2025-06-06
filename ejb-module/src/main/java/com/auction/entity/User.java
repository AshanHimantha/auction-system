package com.auction.entity;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
    private String username;
    private String passwordHash;
    private UserRole role; // NEW FIELD: Role of the user

    public User() {}

    public User(String username, String passwordHash, UserRole role) { // MODIFIED CONSTRUCTOR
        this.username = username;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public UserRole getRole() { return role; } // NEW GETTER
    public void setRole(UserRole role) { this.role = role; } // NEW SETTER

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username);
    }
}