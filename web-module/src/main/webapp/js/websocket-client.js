// websocket-client.js
// This file will be included on all pages that need real-time updates.

(function() {
    const CONTEXT_ROOT = '/auction-system'; // Private to this module
    let sharedWebSocket = null;
    let notificationLogElement = null;

    // --- Utility for logging to the current page's log element ---
    function logToPage(message, className = '', logElementId = 'auction-log') {
        const logElement = document.getElementById(logElementId) || document.getElementById('app-log');
        if (logElement) {
            const p = document.createElement('p');
            p.className = `mb-1 ${className}`;
            p.textContent = `[${new Date().toLocaleTimeString()}] ${message}`;
            logElement.appendChild(p);
            logElement.scrollTop = logElement.scrollHeight; // Scroll to bottom
        } else {
            console.log(`[${new Date().toLocaleTimeString()}] ${message}`);
        }
    }

    // --- Enhanced Notification System ---
    function setupNotificationContainer() {
        // Create notification container if it doesn't exist
        if (!document.getElementById('notification-container')) {
            const container = document.createElement('div');
            container.id = 'notification-container';
            container.className = 'notification-container';
            document.body.appendChild(container);
        }
        return document.getElementById('notification-container');
    }

    // Enhanced notification display function
    function showNotification(message, type = 'info') {
        const container = setupNotificationContainer();
        
        // Create notification element
        const notification = document.createElement('div');
        notification.className = `notification notification-${type}`;
        
        // Create icon based on type
        const iconMap = {
            'info': '<svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>',
            'success': '<svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>',
            'error': '<svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" /></svg>',
            'warning': '<svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor"><path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" /></svg>'
        };
        
        notification.innerHTML = `
            <div class="notification-icon">${iconMap[type] || iconMap.info}</div>
            <div class="notification-content">
                <div class="notification-message">${message}</div>
                <div class="notification-timestamp">${new Date().toLocaleTimeString()}</div>
            </div>
            <button class="notification-close">&times;</button>
        `;
        
        // Add to container
        container.appendChild(notification);
        
        // Add entry animation
        setTimeout(() => notification.classList.add('show'), 10);
        
        // Add dismiss button functionality
        const closeButton = notification.querySelector('.notification-close');
        closeButton.addEventListener('click', () => {
            notification.classList.remove('show');
            setTimeout(() => notification.remove(), 300);
        });
        
        // Auto dismiss after 5 seconds
        setTimeout(() => {
            if (notification.isConnected) {
                notification.classList.remove('show');
                setTimeout(() => notification.remove(), 300);
            }
        }, 5000);
        
        // Also log to the page if log element exists
        logToPage(`Notification: ${message}`, `notification-text-${type}`, 'app-log');
    }

    // --- WebSocket Connection Logic ---
    function connectSharedWebSocket() {
        // Only connect if not already connected or connecting
        if (sharedWebSocket && (sharedWebSocket.readyState === WebSocket.OPEN || sharedWebSocket.readyState === WebSocket.CONNECTING)) {
            return;
        }

        const wsUrl = `ws://${window.location.host}${CONTEXT_ROOT}/auctionUpdates`;
        sharedWebSocket = new WebSocket(wsUrl);

        sharedWebSocket.onopen = function(event) {
            logToPage('WebSocket connected for real-time updates.', 'text-green-600', 'app-log');
        };

        sharedWebSocket.onmessage = function(event) {
            try {
                const data = JSON.parse(event.data);
                handleBidUpdateNotification(data);
            } catch (e) {
                logToPage(`WebSocket message parsing error: ${e.message}. Raw: ${event.data}`, 'text-red-500', 'app-log');
            }
        };

        sharedWebSocket.onclose = function(event) {
            logToPage('WebSocket disconnected. Reconnecting in 5s...', 'text-red-600', 'app-log');
            const token = localStorage.getItem('authToken');
            if (token || window.location.pathname.includes('index.html')) {
                setTimeout(connectSharedWebSocket, 5000);
            }
        };

        sharedWebSocket.onerror = function(error) {
            logToPage('WebSocket error: ' + error.message, 'text-red-700', 'app-log');
        };
    }

    // --- Handler for incoming bid update messages ---
    function handleBidUpdateNotification(data) {
        const auctionId = data.auctionId;
        const currentBid = data.currentBid;
        const winningBidder = data.winningBidder;
        const title = data.title;

        // Check if the current page is the specific auction details page
        const currentPageIsAuctionDetails = window.location.pathname.includes('auction-details.html') &&
            new URLSearchParams(window.location.search).get('id') == auctionId;

        if (currentPageIsAuctionDetails) {
            // If on the specific auction page, call its local update function
            if (typeof window.updateAuctionDetails === 'function') {
                window.updateAuctionDetails(data);
            }
            logToPage(`Update for THIS AUCTION: ${title} (#${auctionId}) - New bid $${currentBid.toFixed(2)} by ${winningBidder}`, 'text-blue-600', 'auction-log');
        } else {
            // For other pages, show a general notification
            showNotification(`New bid: $${currentBid.toFixed(2)} on "${title}" by ${winningBidder}!`, 'info');
        }
    }

    // --- Expose public methods ---
    window.auctionSocket = {
        connect: connectSharedWebSocket,
        showNotification: showNotification
    };

    // --- Initialization ---
    document.addEventListener('DOMContentLoaded', () => {
        connectSharedWebSocket();
    });
})();
