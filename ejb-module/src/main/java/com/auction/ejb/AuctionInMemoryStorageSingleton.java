package com.auction.ejb;

import com.auction.entity.Auction;
import com.auction.entity.AuctionBidHistory;
import com.auction.entity.AuctionCategory;
import com.auction.entity.AuctionStatus;
import jakarta.annotation.PostConstruct;
import jakarta.ejb.Lock;
import jakarta.ejb.LockType;
import jakarta.ejb.Singleton;
import jakarta.ejb.Startup;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList; // NEW IMPORT for thread-safe list
import java.util.concurrent.atomic.AtomicLong;
// No longer need Collectors.toList() for initialization if using CopyOnWriteArrayList

@Singleton
@Startup
public class AuctionInMemoryStorageSingleton {

    private Map<Long, Auction> auctions;
    private Map<Long, AuctionCategory> categories;
    private Map<Long, List<AuctionBidHistory>> bidHistories;
    private AtomicLong currentMaxId = new AtomicLong(0);

    @PostConstruct
    public void init() {
        System.out.println("AuctionInMemoryStorageSingleton initialized. Populating initial data...");
        auctions = new ConcurrentHashMap<>();
        categories = new ConcurrentHashMap<>();
        bidHistories = new ConcurrentHashMap<>();

        // --- Populate Categories ---
        AuctionCategory catElectronics = new AuctionCategory("Electronics", "Gadgets, devices, and tech items.");
        AuctionCategory catCollectibles = new AuctionCategory("Collectibles", "Rare items, coins, stamps, and memorabilia.");
        AuctionCategory catArt = new AuctionCategory("Art", "Paintings, sculptures, and creative works.");
        categories.put(catElectronics.getId(), catElectronics);
        categories.put(catCollectibles.getId(), catCollectibles);
        categories.put(catArt.getId(), catArt);
        System.out.println("Populated " + categories.size() + " categories.");

        // --- Populate Auctions with Categories ---
        // Ensure category IDs are correct for initial auctions
        Auction a1 = new Auction("Vintage Watch", "A classic timepiece from the 1950s.", new String[]{"https://via.placeholder.com/150/0000FF/FFFFFF?text=Watch1", "https://via.placeholder.com/150/0000AA/FFFFFF?text=Watch2"}, 100.00, 5.00, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusHours(1), catCollectibles.getId());
        a1.setWinningBidder("system_user");
        auctions.put(a1.getId(), a1);
        bidHistories.put(a1.getId(), new CopyOnWriteArrayList<>()); // Initialize with CopyOnWriteArrayList

        Auction a2 = new Auction("Rare Comic Book", "First edition of a highly sought-after superhero comic.", new String[]{"https://via.placeholder.com/150/FF0000/000000?text=Comic1"}, 50.00, 2.00, LocalDateTime.now().minusHours(12), LocalDateTime.now().minusHours(1), catCollectibles.getId());
        a2.setStatus(AuctionStatus.CLOSED);
        a2.setWinningBidder("UserEnded");
        auctions.put(a2.getId(), a2);
        bidHistories.put(a2.getId(), new CopyOnWriteArrayList<>(List.of(new AuctionBidHistory(a2.getId(), "InitialBidder", 50.00), new AuctionBidHistory(a2.getId(), "UserEnded", 52.00)))); // Example history with CopyOnWriteArrayList

        Auction a3 = new Auction("Old Painting", "Abstract art by a renowned local artist.", new String[]{"https://via.placeholder.com/150/00FF00/000000?text=Painting1", "https://via.placeholder.com/150/00AA00/000000?text=Painting2", "https://via.placeholder.com/150/005500/000000?text=Painting3"}, 200.00, 10.00, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusDays(7), catArt.getId());
        auctions.put(a3.getId(), a3);
        bidHistories.put(a3.getId(), new CopyOnWriteArrayList<>());

        Auction a4 = new Auction("Rare Stamp Collection", "Complete set of vintage stamps.", new String[]{"https://via.placeholder.com/150/FFFF00/000000?text=Stamp1"}, 150.00, 5.00, LocalDateTime.now().minusDays(3), LocalDateTime.now().plusDays(10), catCollectibles.getId());
        auctions.put(a4.getId(), a4);
        bidHistories.put(a4.getId(), new CopyOnWriteArrayList<>());


        auctions.keySet().forEach(id -> {
            while (currentMaxId.get() < id) {
                currentMaxId.compareAndSet(currentMaxId.get(), id);
            }
        });

        System.out.println("Populated " + auctions.size() + " auctions.");
    }

    @Lock(LockType.READ)
    public Auction getAuction(Long id) {
        return auctions.get(id);
    }

    // Renamed from getAllActiveAuctions to getAllAuctions to match AuctionManager interface
    @Lock(LockType.READ)
    public Collection<Auction> getAllAuctions() {
        return Collections.unmodifiableCollection(auctions.values());
    }

    @Lock(LockType.READ)
    public Collection<AuctionCategory> getAllCategories() {
        return Collections.unmodifiableCollection(categories.values());
    }

    @Lock(LockType.READ)
    public AuctionCategory getCategoryById(Long id) {
        return categories.get(id);
    }

    @Lock(LockType.WRITE)
    public void addOrUpdateAuction(Auction auction) {
        Auction existing = auctions.get(auction.getId());
        if (existing != null && existing.getVersion() != auction.getVersion()) {
            throw new RuntimeException("Concurrent modification detected for auction " + auction.getId() + ". Expected version " + auction.getVersion() + ", but found " + existing.getVersion());
        }
        auction.incrementVersion();
        auctions.put(auction.getId(), auction);
        // Initialize history list if it's a new auction with a thread-safe list
        bidHistories.computeIfAbsent(auction.getId(), k -> new CopyOnWriteArrayList<>());
        System.out.println("Added/Updated auction " + auction.getId() + " in in-memory storage. New version: " + auction.getVersion());
    }

    @Lock(LockType.WRITE)
    public void addBidToHistory(Long auctionId, AuctionBidHistory bid) {
        // Ensure the list is a CopyOnWriteArrayList or similar thread-safe list
        bidHistories.computeIfAbsent(auctionId, k -> new CopyOnWriteArrayList<>())
                .add(bid);
        System.out.println("Added bid to history for auction " + auctionId + ": " + bid.getBidAmount());
    }

    @Lock(LockType.READ)
    public List<AuctionBidHistory> getBidHistory(Long auctionId) {
        return Collections.unmodifiableList(bidHistories.getOrDefault(auctionId, List.of()));
    }

    @Lock(LockType.WRITE)
    public boolean removeAuction(Long id) {
        bidHistories.remove(id);
        return auctions.remove(id) != null;
    }
}