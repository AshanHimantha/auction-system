package com.auction.ejb;

import com.auction.entity.Auction;
import com.auction.entity.AuctionCategory; // NEW IMPORT
import jakarta.ejb.Local;

import java.time.LocalDateTime;
import java.util.Collection;

@Local
public interface AuctionManager {
    // MODIFIED: Constructor signature matches new Auction fields + categoryId
    Auction createAuction(String title, String description, String[] imageUrls, double startPrice, double minIncrement, LocalDateTime startTime, LocalDateTime endTime, Long categoryId);
    Collection<Auction> getAllAuctions(); // Renamed from getAllActiveAuctions()
    Auction getAuctionDetails(Long id);
    Collection<AuctionCategory> getAllCategories(); // NEW
    boolean closeAuction(Long id);
}