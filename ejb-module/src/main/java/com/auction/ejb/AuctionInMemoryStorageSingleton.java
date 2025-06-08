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
        Auction a1 = new Auction("Vintage Watch", "A classic timepiece from the 1950s.",
                new String[]{"https://vintagewatchspecialist.com/cdn/shop/files/record-wwii-british-military-issued-dirty-dozen-wristwatch-1944-dirty-dirty-dozen-dozen-collection-watch-dirty-dozen-watch-watches-vintage-watch-800_2400x.jpg?v=1724341237",
                        "https://vintagewatchspecialist.com/cdn/shop/files/record-wwii-british-military-issued-dirty-dozen-wristwatch-1944-dirty-dirty-dozen-dozen-collection-watch-dirty-dozen-watch-watches-vintage-watch-496_2400x.jpg?v=1724341251",
                "https://vintagewatchspecialist.com/cdn/shop/files/record-wwii-british-military-issued-dirty-dozen-wristwatch-1944-dirty-dirty-dozen-dozen-collection-watch-dirty-dozen-watch-watches-vintage-watch-738_2400x.jpg?v=1724341258"
                }, 100.00, 5.00, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusHours(1), catCollectibles.getId());
        a1.setWinningBidder("system_user");
        auctions.put(a1.getId(), a1);
        bidHistories.put(a1.getId(), new CopyOnWriteArrayList<>()); // Initialize with CopyOnWriteArrayList

        Auction a2 = new Auction("Rare Comic Book", "First edition of a highly sought-after superhero comic.",
                new String[]{"https://th.bing.com/th/id/OIP.eCb11UP2KrhO2AxSFTYP-wAAAA?rs=1&pid=ImgDetMain",
                "https://www.comicbookdaily.com/wp-content/uploads/2020/11/tec-27-480x734.jpg",
                        "https://comicvine.gamespot.com/a/uploads/scale_small/0/9116/1206901-detectivecomics27.jpg"}, 50.00, 2.00, LocalDateTime.now().minusHours(12), LocalDateTime.now().minusHours(1), catCollectibles.getId());
        a2.setStatus(AuctionStatus.CLOSED);
        a2.setWinningBidder("UserEnded");
        auctions.put(a2.getId(), a2);
        bidHistories.put(a2.getId(), new CopyOnWriteArrayList<>(List.of(new AuctionBidHistory(a2.getId(), "InitialBidder", 50.00), new AuctionBidHistory(a2.getId(), "UserEnded", 52.00)))); // Example history with CopyOnWriteArrayList

        Auction a3 = new Auction("Old Painting", "Abstract art by a renowned local artist.", new String[]{
                "https://1.bp.blogspot.com/-sm737j8m4x4/UUEwnz2tcYI/AAAAAAAABPc/1m2sz7LJBt0/s1600/4830_1.jpg",
                "https://a.1stdibscdn.com/alfred-banner-paintings-the-ramblers-english-landscape-with-figures-early-20c-victorian-style-for-sale-picture-2/a_5073/a_109675721664724929793/IMG_8572_master.jpg",
                "https://th.bing.com/th/id/OIP.ZOSEnWXG0i8ZL6G8s6UkrgHaIX?w=1588&h=1795&rs=1&pid=ImgDetMain"}, 200.00, 10.00, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusDays(7), catArt.getId());
        auctions.put(a3.getId(), a3);
        bidHistories.put(a3.getId(), new CopyOnWriteArrayList<>());

        Auction a4 = new Auction("Rare Stamp Collection", "Complete set of vintage stamps.", new String[]{
                "https://th.bing.com/th/id/OIP.-G7DziTLRgnFV77bNXZEZgHaE8?rs=1&pid=ImgDetMain",
                "https://th.bing.com/th/id/OIP.1uwSnegn693jmiS7Ty1jRAHaHa?w=626&h=626&rs=1&pid=ImgDetMain",
                "https://th.bing.com/th/id/OIP._vv2koW5QUgAZxNeEUyXfAHaKe?w=724&h=1024&rs=1&pid=ImgDetMain"
        }, 150.00, 5.00, LocalDateTime.now().minusDays(3), LocalDateTime.now().plusDays(10), catCollectibles.getId());
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