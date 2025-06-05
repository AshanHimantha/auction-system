package com.auction.ejb;

import jakarta.ejb.Local;

@Local
public interface BidManager {
    String submitBid(Long auctionId, double bidAmount, String bidderName);
}