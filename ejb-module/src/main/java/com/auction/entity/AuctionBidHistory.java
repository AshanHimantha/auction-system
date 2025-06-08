package com.auction.entity;

import java.io.Serializable;
import java.time.LocalDateTime; // NEW IMPORT
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class AuctionBidHistory implements Serializable {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1000);

    private Long id;
    private Long auctionId; // The ID of the auction this bid is for
    private String bidderName;
    private double bidAmount;
    private LocalDateTime bidTime;

    public AuctionBidHistory() {
        this.id = ID_GENERATOR.incrementAndGet();
        this.bidTime = LocalDateTime.now(); // Set bid time on creation
    }

    public AuctionBidHistory(Long auctionId, String bidderName, double bidAmount) {
        this();
        this.auctionId = auctionId;
        this.bidderName = bidderName;
        this.bidAmount = bidAmount;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getAuctionId() { return auctionId; }
    public void setAuctionId(Long auctionId) { this.auctionId = auctionId; }

    public String getBidderName() { return bidderName; }
    public void setBidderName(String bidderName) { this.bidderName = bidderName; }

    public double getBidAmount() { return bidAmount; }
    public void setBidAmount(double bidAmount) { this.bidAmount = bidAmount; }

    public LocalDateTime getBidTime() { return bidTime; }
    public void setBidTime(LocalDateTime bidTime) { this.bidTime = bidTime; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuctionBidHistory that = (AuctionBidHistory) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "AuctionBidHistory{" +
                "id=" + id +
                ", auctionId=" + auctionId +
                ", bidderName='" + bidderName + '\'' +
                ", bidAmount=" + bidAmount +
                ", bidTime=" + bidTime +
                '}';
    }
}