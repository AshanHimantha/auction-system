package com.auction.ejb;

import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.time.LocalDateTime; // NEW IMPORT
import java.util.Collection;

import com.auction.ejb.AuctionInMemoryStorageSingleton;

@Stateless
public class AuctionManagerBean implements AuctionManager {

    @EJB
    private AuctionInMemoryStorageSingleton auctionStorage;

    @Override // MODIFIED: Method signature matches new Auction fields
    public Auction createAuction(String title, String description, String[] imageUrls, double startPrice, double minIncrement, LocalDateTime startTime, LocalDateTime endTime) {
        Auction auction = new Auction(title, description, imageUrls, startPrice, minIncrement, startTime, endTime);
        auctionStorage.addOrUpdateAuction(auction);
        System.out.println("Created auction: " + auction.getId() + " - " + auction.getTitle());
        return auction;
    }

    @Override
    public Auction getAuctionDetails(Long id) {
        return auctionStorage.getAuction(id);
    }

    @Override
    public Collection<Auction> getAllActiveAuctions() {
        return auctionStorage.getAllActiveAuctions();
    }

    @Override
    public boolean closeAuction(Long id) {
        Auction auction = auctionStorage.getAuction(id);
        if (auction != null && auction.getStatus() == AuctionStatus.ACTIVE) {
            auction.setStatus(AuctionStatus.CLOSED);
            auctionStorage.addOrUpdateAuction(auction);
            System.out.println("Closed auction: " + auction.getId() + " - " + auction.getTitle());
            return true;
        }
        return false;
    }
}