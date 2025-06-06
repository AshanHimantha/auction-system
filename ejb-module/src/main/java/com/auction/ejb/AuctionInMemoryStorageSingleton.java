package com.auction.ejb;

import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.time.LocalDateTime; // NEW IMPORT
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Singleton
@Startup
public class AuctionInMemoryStorageSingleton {

    private Map<Long, Auction> auctions;
    private AtomicLong currentMaxId = new AtomicLong(0);

    @PostConstruct
    public void init() {
        System.out.println("AuctionInMemoryStorageSingleton initialized. Populating initial auctions...");
        auctions = new ConcurrentHashMap<>();

        // MODIFIED: Create auctions with new fields
        Auction a1 = new Auction("Vintage Watch", "A classic timepiece from the 1950s.", new String[]{"https://via.placeholder.com/150/0000FF/FFFFFF?text=Watch1", "https://via.placeholder.com/150/0000AA/FFFFFF?text=Watch2"}, 100.00, 5.00, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(5));
        a1.setWinningBidder("system_user");
        auctions.put(a1.getId(), a1);

        Auction a2 = new Auction("Rare Comic Book", "First edition of a highly sought-after superhero comic.", new String[]{"https://via.placeholder.com/150/FF0000/000000?text=Comic1"}, 50.00, 2.00, LocalDateTime.now().minusHours(12), LocalDateTime.now().plusDays(2));
        a2.setWinningBidder("system_user");
        auctions.put(a2.getId(), a2);

        Auction a3 = new Auction("Old Painting", "Abstract art by a renowned local artist.", new String[]{"https://via.placeholder.com/150/00FF00/000000?text=Painting1", "https://via.placeholder.com/150/00AA00/000000?text=Painting2", "https://via.placeholder.com/150/005500/000000?text=Painting3"}, 200.00, 10.00, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusDays(7)); // Auction starts in future
        a3.setWinningBidder("system_user");
        auctions.put(a3.getId(), a3);

        auctions.keySet().forEach(id -> {
            while (currentMaxId.get() < id) {
                currentMaxId.compareAndSet(currentMaxId.get(), id);
            }
        });

        System.out.println("Populated " + auctions.size() + " initial auctions.");
    }

    @Lock(LockType.READ)
    public Auction getAuction(Long id) {
        // In a real system, you might filter by active status or time here for UI.
        return auctions.get(id);
    }

    @Lock(LockType.READ)
    public Collection<Auction> getAllActiveAuctions() {
        // MODIFIED: Filter by active status and within start/end times
        LocalDateTime now = LocalDateTime.now();
        return auctions.values().stream()
                .filter(a -> a.getStatus() == AuctionStatus.ACTIVE &&
                        !now.isBefore(a.getStartTime()) && // Auction has started or is starting now
                        !now.isAfter(a.getEndTime()))      // Auction has not ended or is ending now
                .collect(java.util.stream.Collectors.toList()); // Convert to list
    }

    @Lock(LockType.WRITE)
    public void addOrUpdateAuction(Auction auction) {
        Auction existing = auctions.get(auction.getId());
        if (existing != null && existing.getVersion() != auction.getVersion()) {
            throw new RuntimeException("Concurrent modification detected for auction " + auction.getId() + ". Expected version " + auction.getVersion() + ", but found " + existing.getVersion());
        }
        auction.incrementVersion();
        auctions.put(auction.getId(), auction);
        System.out.println("Added/Updated auction " + auction.getId() + " in in-memory storage. New version: " + auction.getVersion());
    }

    @Lock(LockType.WRITE)
    public boolean removeAuction(Long id) {
        return auctions.remove(id) != null;
    }
}