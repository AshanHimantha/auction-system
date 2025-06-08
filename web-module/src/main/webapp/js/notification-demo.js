document.addEventListener('DOMContentLoaded', function() {
    // Add test button to the page
    const testButton = document.createElement('button');
    testButton.className = 'notification-test-button';
    testButton.textContent = 'Test Notifications';
    document.body.appendChild(testButton);
    
    // Demo notifications when button is clicked
    testButton.addEventListener('click', function() {
        // Show different types of notifications with staggered timing
        setTimeout(() => window.auctionSocket.showNotification('Welcome to Auction Hub!', 'info'), 0);
        setTimeout(() => window.auctionSocket.showNotification('Your bid was successful!', 'success'), 1000);
        setTimeout(() => window.auctionSocket.showNotification('Auction ending soon!', 'warning'), 2000);
        setTimeout(() => window.auctionSocket.showNotification('Connection lost, reconnecting...', 'error'), 3000);
    });

    // Show a welcome notification after a slight delay
    setTimeout(() => {
        window.auctionSocket.showNotification('Welcome to Auction Hub! Browse our latest auctions.', 'info');
    }, 1000);
});
