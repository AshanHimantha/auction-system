// homepage.js - for index.jsp

document.addEventListener('DOMContentLoaded', () => {
    // --- Mobile menu functionality ---
    const mobileMenuButton = document.getElementById('mobile-menu-button');
    const mobileMenu = document.getElementById('mobile-menu');

    if (mobileMenuButton && mobileMenu) {
        mobileMenuButton.addEventListener('click', () => {
            mobileMenu.classList.toggle('hidden');
        });
    }

    // --- Featured auctions section ---
    const fetchFeaturedAuctions = async () => {
        const featuredAuctionList = document.getElementById('featured-auction-list');
        if (!featuredAuctionList) return;

        try {
            // Define a CONTEXT_ROOT variable if needed
            const CONTEXT_ROOT = window.CONTEXT_ROOT || '';

            const response = await fetch(`/auction-system/api/auctions`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const auctions = await response.json();

            if (auctions.length === 0) {
                featuredAuctionList.innerHTML = '<p class="col-span-full text-center text-muted-foreground">No featured auctions available right now.</p>';
                return;
            }

            featuredAuctionList.innerHTML = '';

            // Display up to 3 featured auctions
            auctions.slice(0, 3).forEach(auction => {
                featuredAuctionList.appendChild(createFeaturedAuctionCard(auction));
            });

        } catch (error) {
            console.error('Failed to load featured auctions:', error);
            featuredAuctionList.innerHTML = '<p class="col-span-full text-center text-red-400">Error loading auctions. Please try again later.</p>';
        }
    };

    // Helper functions for featured auctions
    function formatTimeRemaining(endTimeString) {
        const now = new Date().getTime();
        const endTime = new Date(endTimeString).getTime();
        const distance = endTime - now;

        if (distance < 0) return 'ENDED';

        const days = Math.floor(distance / (1000 * 60 * 60 * 24));
        const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));

        return `${days}d ${hours}h ${minutes}m`;
    }

    function getAuctionStatus(auction) {
        const now = new Date();
        const startTime = new Date(auction.startTime);
        const endTime = new Date(auction.endTime);

        if (now < startTime) {
            return {
                text: 'WAITING',
                tailwindClass: 'bg-opacity-60 backdrop-blur-sm',
                textColor: 'text-blue-400',
                emoji: '⏳',
                dotColor: 'bg-blue-400',
                animation: 'animate-rotate'
            };
        } else if (now > endTime) {
            return {
                text: 'ENDED',
                tailwindClass: 'bg-opacity-60 backdrop-blur-sm',
                textColor: 'text-gray-400',
                emoji: '✓',
                dotColor: 'bg-gray-400',
                animation: ''
            };
        } else {
            return {
                text: 'ACTIVE',
                tailwindClass: 'bg-opacity-60 backdrop-blur-sm',
                textColor: 'text-green-400',
                emoji: '🔥',
                dotColor: 'bg-green-400',
                animation: 'animate-smooth-bounce'
            };
        }
    }

    function createFeaturedAuctionCard(auction) {
        const CONTEXT_ROOT = "/auction-system" || '';
        const card = document.createElement('div');
        card.className = 'bg-card border border-border rounded-lg overflow-hidden flex flex-col transform transition-all duration-300 hover:-translate-y-1 hover:shadow-lg';

        const status = getAuctionStatus(auction);

        const imageUrl = auction.imageUrls && auction.imageUrls.length > 0
            ? auction.imageUrls[0]
            : 'https://placehold.co/600x400/1f1f23/e2e2e2.png?text=No+Image';

        card.innerHTML = `
            <div class="relative">
                <img src="${imageUrl}" alt="${auction.title}" class="w-full h-48 object-cover auction-card-image">
                <div class="auction-status absolute top-2 right-2 flex items-center gap-1 px-3 py-1 rounded-full bg-black ${status.tailwindClass}">
    <span class="animate-bounce">${status.emoji}</span>
    <span class="pulse-dot h-2 w-2 rounded-full ${status.dotColor}"></span>
    <span class="text-xs font-semibold ${status.textColor}">${status.text}</span>
</div>
            </div>
            <div class="p-5 flex-grow">
                <h3 class="font-bold text-lg text-foreground mb-2 hover:text-primary">
                    <a href="${CONTEXT_ROOT}/auction-details.html?id=${auction.id}">${auction.title}</a>
                </h3>
                <p class="text-muted-foreground text-sm mb-4">${auction.description.substring(0, 80)}...</p>
                <div class="flex justify-between items-end">
                    <div>
                        <p class="text-muted-foreground text-xs">Current Bid</p>
                        <p class="text-primary font-bold">$${auction.currentBid.toFixed(2)}</p>
                    </div>
                    <div class="text-right">
                        <p class="text-muted-foreground text-xs">Time Left</p>
                        <p class="countdown text-sm">${formatTimeRemaining(auction.endTime)}</p>
                    </div>
                </div>
            </div>
            <div class="bg-secondary/30 px-5 py-3 border-t border-border">
                <a href="${CONTEXT_ROOT}/auction-details.html?id=${auction.id}" class="text-primary hover:text-primary hover:underline font-medium text-sm">View Details</a>
            </div>
        `;
        return card;
    }

    async function fetchRecentBids() {
        const recentBidsTable = document.getElementById('recent-bids-table');

        try {
            const response = await fetch(`${CONTEXT_ROOT}/api/auctions/recent-bids`);
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            const bids = await response.json();

            if (bids.length === 0) {
                recentBidsTable.innerHTML = `
                <tr class="text-center">
                    <td colspan="5" class="px-4 py-8 text-muted-foreground">
                        No recent bids found
                    </td>
                </tr>
            `;
                return;
            }

            recentBidsTable.innerHTML = bids.map(bid => `
            <tr class="hover:bg-secondary/30">
                <td class="px-4 py-3">
                    <a href="${CONTEXT_ROOT}/auction-details.html?id=${bid.auctionId}" class="hover:text-primary">
                        ${bid.auctionTitle}
                    </a>
                </td>
                <td class="px-4 py-3">${bid.bidderUsername}</td>
                <td class="px-4 py-3 text-right font-medium">$${bid.amount.toFixed(2)}</td>
                <td class="px-4 py-3 text-right text-muted-foreground">${new Date(bid.timestamp).toLocaleTimeString()}</td>
                <td class="px-4 py-3 text-center">
                    ${bid.winning ?
                '<span class="bg-green-500/20 text-green-400 text-xs px-2 py-1 rounded">Winning</span>' :
                '<span class="bg-gray-500/20 text-gray-400 text-xs px-2 py-1 rounded">Outbid</span>'
            }
                </td>
            </tr>
        `).join('');
        } catch (error) {
            console.error('Error fetching recent bids:', error);
            recentBidsTable.innerHTML = `
            <tr class="text-center">
                <td colspan="5" class="px-4 py-3 text-red-500">
                    Failed to load recent bids: ${error.message}
                </td>
            </tr>
        `;
        }
    }

    document.addEventListener('DOMContentLoaded', () => {
        // Other initialization code...
        fetchRecentBids();

        // Refresh bid history periodically
        setInterval(fetchRecentBids, 60000); // Update every minute
    });

    // Initialize the featured auctions
    fetchFeaturedAuctions();

    // Update countdown timers periodically
    setInterval(() => {
        document.querySelectorAll('.countdown').forEach(element => {
            const auctionCard = element.closest('[data-auction-end]');
            if (auctionCard) {
                const endTime = auctionCard.dataset.auctionEnd;
                element.textContent = formatTimeRemaining(endTime);
            }
        });
    }, 60000); // Update every minute
});