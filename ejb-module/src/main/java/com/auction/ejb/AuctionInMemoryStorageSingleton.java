package com.auction.ejb;

import com.auction.entity.Auction;
import com.auction.entity.AuctionStatus;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

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

        Auction a1 = new Auction("Vintage Watch", 100.00, 5.00);
        a1.setWinningBidder("system_user");
        auctions.put(a1.getId(), a1);

        Auction a2 = new Auction("Rare Comic Book", 50.00, 2.00);
        a2.setWinningBidder("system_user");
        auctions.put(a2.getId(), a2);

        Auction a3 = new Auction("Old Painting", 200.00, 10.00);
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
        return auctions.get(id);
    }

    @Lock(LockType.READ)
    public Collection<Auction> getAllActiveAuctions() {
        return Collections.unmodifiableCollection(auctions.values());
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