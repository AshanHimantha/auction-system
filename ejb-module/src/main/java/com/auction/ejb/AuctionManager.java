package com.auction.ejb;

import com.auction.entity.Auction;
import jakarta.ejb.Local;

import java.time.LocalDateTime; // NEW IMPORT
import java.util.Collection;

@Local
public interface AuctionManager {
    // MODIFIED: Constructor signature matches new Auction fields
    Auction createAuction(String title, String description, String[] imageUrls, double startPrice, double minIncrement, LocalDateTime startTime, LocalDateTime endTime);
    Auction getAuctionDetails(Long id);
    Collection<Auction> getAllActiveAuctions();
    boolean closeAuction(Long id);
}