package com.auction.ejb;

import com.auction.entity.Auction;
import jakarta.ejb.Local;

import java.util.Collection;

@Local
public interface AuctionManager {
    Auction createAuction(String title, double startingBid, double minIncrement);
    Auction getAuctionDetails(Long id);
    Collection<Auction> getAllActiveAuctions();
    boolean closeAuction(Long id);
}