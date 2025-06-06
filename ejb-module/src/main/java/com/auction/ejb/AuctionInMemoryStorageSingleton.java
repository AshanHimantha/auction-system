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
        Auction a1 = new Auction("Vintage Watch", "A classic timepiece from the 1950s.", new String[]{"https://th.bing.com/th/id/OIP.YyVBrBigKQMSbIQUZBaMSgHaJQ?rs=1&pid=ImgDetMain", "https://th.bing.com/th/id/OIP.v4U30tNNeO39lKkQeKGUqAHaJQ?pid=ImgDet&w=474&h=592&rs=1"}, 100.00, 5.00, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(5));
        a1.setWinningBidder("NoBidder"); // No bids yet
        auctions.put(a1.getId(), a1);

        Auction a2 = new Auction("Rare Comic Book", "First edition of a highly sought-after superhero comic.", new String[]{"https://images-geeknative-com.exactdn.com/wp-content/uploads/2020/02/18002335/Comics-Incredible-Hulk-181.jpg?strip=all&lossy=1&w=1920&ssl=1","https://images-geeknative-com.exactdn.com/wp-content/uploads/2020/02/18002335/Comics-Incredible-Hulk-181.jpg?strip=all&lossy=1&w=1920&ssl=1"}, 50.00, 2.00, LocalDateTime.now().minusHours(12), LocalDateTime.now().plusDays(2));
        a2.setWinningBidder("NoBidder");
        auctions.put(a2.getId(), a2);

        Auction a3 = new Auction("Old Painting", "Abstract art by a renowned local artist.", new String[]{"https://th.bing.com/th/id/R.ad76f044acd53da43c459dc1af958f9c?rik=4UoSDv9wFjaJ3A&pid=ImgRaw&r=0", "https://cdn.pixabay.com/photo/2016/08/07/09/17/old-1575887_960_720.jpg", "https://th.bing.com/th/id/OIP.DA9XmE8NmZhTFpItnTT1XgHaKU?w=646&h=900&rs=1&pid=ImgDetMain"}, 200.00, 10.00, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusDays(7)); // Auction starts in future
        a3.setWinningBidder("NoBidder");
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