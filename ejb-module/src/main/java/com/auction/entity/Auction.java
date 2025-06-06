package com.auction.entity;

import java.io.Serializable;
import java.time.LocalDateTime; // NEW IMPORT
import java.util.Arrays; // NEW IMPORT
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Auction implements Serializable {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(100);

    private Long id;
    private String title;
    private String description; // NEW FIELD
    private String[] imageUrls; // NEW FIELD: Array of image URLs (up to 3)
    private double startPrice; // NEW FIELD: Original starting price
    private double currentBid;
    private String winningBidder;
    private double minIncrement;
    private LocalDateTime startTime; // NEW FIELD
    private LocalDateTime endTime; // NEW FIELD
    private AuctionStatus status;
    private long version;

    public Auction() {
        this.id = ID_GENERATOR.incrementAndGet();
        this.status = AuctionStatus.ACTIVE;
        this.version = 0;
    }

    // MODIFIED CONSTRUCTOR: Add new fields
    public Auction(String title, String description, String[] imageUrls, double startPrice, double minIncrement, LocalDateTime startTime, LocalDateTime endTime) {
        this(); // Call default constructor for ID generation
        this.title = title;
        this.description = description;
        this.imageUrls = imageUrls;
        this.startPrice = startPrice;
        this.currentBid = startPrice; // Current bid starts at start price
        this.minIncrement = minIncrement;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters for all fields (new and existing)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; } // NEW GETTER
    public void setDescription(String description) { this.description = description; } // NEW SETTER

    public String[] getImageUrls() { return imageUrls; } // NEW GETTER
    public void setImageUrls(String[] imageUrls) { this.imageUrls = imageUrls; } // NEW SETTER

    public double getStartPrice() { return startPrice; } // NEW GETTER
    public void setStartPrice(double startPrice) { this.startPrice = startPrice; } // NEW SETTER

    public double getCurrentBid() { return currentBid; }
    public void setCurrentBid(double currentBid) { this.currentBid = currentBid; }

    public String getWinningBidder() { return winningBidder; }
    public void setWinningBidder(String winningBidder) { this.winningBidder = winningBidder; }

    public double getMinIncrement() { return minIncrement; }
    public void setMinIncrement(double minIncrement) { this.minIncrement = minIncrement; }

    public LocalDateTime getStartTime() { return startTime; } // NEW GETTER
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; } // NEW SETTER

    public LocalDateTime getEndTime() { return endTime; } // NEW GETTER
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; } // NEW SETTER

    public AuctionStatus getStatus() { return status; }
    public void setStatus(AuctionStatus status) { this.status = status; }

    public long getVersion() { return version; }
    public void setVersion(long version) { this.version = version; }
    public void incrementVersion() { this.version++; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auction auction = (Auction) o;
        return Objects.equals(id, auction.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Auction{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", imageUrls=" + Arrays.toString(imageUrls) +
                ", startPrice=" + startPrice +
                ", currentBid=" + currentBid +
                ", winningBidder='" + winningBidder + '\'' +
                ", minIncrement=" + minIncrement +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", status=" + status +
                ", version=" + version +
                '}';
    }
}