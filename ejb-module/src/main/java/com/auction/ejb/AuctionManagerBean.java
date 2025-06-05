package com.auction.ejb;

import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;
import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;

import java.util.Collection;

// THIS IMPORT IS CRUCIAL AND ADDRESSES "Unresolved symbol 'AuctionInMemoryStorageSingleton'"
import com.auction.ejb.AuctionInMemoryStorageSingleton; // Ensure this is present and correct

@Stateless
public class AuctionManagerBean implements AuctionManager {

    @EJB
    private AuctionInMemoryStorageSingleton auctionStorage; // This should now resolve correctly

    @Override
    public Auction createAuction(String title, double startingBid, double minIncrement) {
        Auction auction = new Auction(title, startingBid, minIncrement);
        // These methods are defined in AuctionInMemoryStorageSingleton.java
        auctionStorage.addOrUpdateAuction(auction); // Should resolve
        System.out.println("Created auction: " + auction.getId() + " - " + auction.getTitle());
        return auction;
    }

    @Override
    public Auction getAuctionDetails(Long id) {
        // This method is defined in AuctionInMemoryStorageSingleton.java
        return auctionStorage.getAuction(id); // Should resolve
    }

    @Override
    public Collection<Auction> getAllActiveAuctions() {
        // This method is defined in AuctionInMemoryStorageSingleton.java
        return auctionStorage.getAllActiveAuctions(); // Should resolve
    }

    @Override
    public boolean closeAuction(Long id) {
        // This method is defined in AuctionInMemoryStorageSingleton.java
        Auction auction = auctionStorage.getAuction(id);
        if (auction != null && auction.getStatus() == AuctionStatus.ACTIVE) { // AuctionStatus.ACTIVE is defined in enum
            auction.setStatus(AuctionStatus.CLOSED);
            auctionStorage.addOrUpdateAuction(auction); // Should resolve
            System.out.println("Closed auction: " + auction.getId() + " - " + auction.getTitle());
            return true;
        }
        return false;
    }
}