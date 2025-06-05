package com.auction.entity;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

public class Auction implements Serializable {
    private static final AtomicLong ID_GENERATOR = new AtomicLong(100);

    private Long id;
    private String title;
    private double currentBid;
    private String winningBidder;
    private double minIncrement;
    private AuctionStatus status;
    private long version;

    public Auction() {
        this.id = ID_GENERATOR.incrementAndGet();
        this.status = AuctionStatus.ACTIVE;
        this.version = 0;
    }

    public Auction(String title, double currentBid, double minIncrement) {
        this();
        this.title = title;
        this.currentBid = currentBid;
        this.minIncrement = minIncrement;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public double getCurrentBid() { return currentBid; }
    public void setCurrentBid(double currentBid) { this.currentBid = currentBid; }

    public String getWinningBidder() { return winningBidder; }
    public void setWinningBidder(String winningBidder) { this.winningBidder = winningBidder; }

    public double getMinIncrement() { return minIncrement; }
    public void setMinIncrement(double minIncrement) { this.minIncrement = minIncrement; }

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
                ", currentBid=" + currentBid +
                ", winningBidder='" + winningBidder + '\'' +
                ", status=" + status +
                ", version=" + version +
                '}';
    }
}