package com.auction.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class AuctionCategory implements Serializable {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    private Long id;
    private String name; // e.g., "Electronics", "Collectibles", "Art"
    private String description;

    public AuctionCategory() {
        this.id = ID_GENERATOR.incrementAndGet();
    }

    public AuctionCategory(String name, String description) {
        this();
        this.name = name;
        this.description = description;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuctionCategory that = (AuctionCategory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AuctionCategory{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}