package com.auction.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Auction implements Serializable {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(100);

    private Long id;
    private String title;
    private String description;
    private String[] imageUrls;
    private double startPrice;
    private double currentBid;
    private String winningBidder;
    private double minIncrement;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private AuctionStatus status;
    private Long categoryId; // NEW FIELD: Link to category by ID
    private long version;

    public Auction() {
        this.id = ID_GENERATOR.incrementAndGet();
        this.status = AuctionStatus.ACTIVE;
        this.version = 0;
    }

    // MODIFIED CONSTRUCTOR: Add categoryId
    public Auction(String title, String description, String[] imageUrls, double startPrice, double minIncrement, LocalDateTime startTime, LocalDateTime endTime, Long categoryId) {
        this();
        this.title = title;
        this.description = description;
        this.imageUrls = imageUrls;
        this.startPrice = startPrice;
        this.currentBid = startPrice;
        this.minIncrement = minIncrement;
        this.startTime = startTime;
        this.endTime = endTime;
        this.categoryId = categoryId; // NEW
    }

    // Getters and Setters for all fields
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String[] getImageUrls() { return imageUrls; }
    public void setImageUrls(String[] imageUrls) { this.imageUrls = imageUrls; }

    public double getStartPrice() { return startPrice; }
    public void setStartPrice(double startPrice) { this.startPrice = startPrice; }

    public double getCurrentBid() { return currentBid; }
    public void setCurrentBid(double currentBid) { this.currentBid = currentBid; }

    public String getWinningBidder() { return winningBidder; }
    public void setWinningBidder(String winningBidder) { this.winningBidder = winningBidder; }

    public double getMinIncrement() { return minIncrement; }
    public void setMinIncrement(double minIncrement) { this.minIncrement = minIncrement; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public AuctionStatus getStatus() { return status; }
    public void setStatus(AuctionStatus status) { this.status = status; }

    public Long getCategoryId() { return categoryId; } // NEW GETTER
    public void setCategoryId(Long categoryId) { this.categoryId = categoryId; } // NEW SETTER

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
                ", categoryId=" + categoryId +
                ", version=" + version +
                '}';
    }
}