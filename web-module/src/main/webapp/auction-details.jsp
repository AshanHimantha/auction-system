<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title id="page-title">Auction Details</title>
  <script src="https://cdn.tailwindcss.com"></script>
  <style>
    .log-container { max-height: 150px; overflow-y: auto; }
    .log-container::-webkit-scrollbar { width: 8px; }
    .log-container::-webkit-scrollbar-track { background: #f1f1f1; border-radius: 10px; }
    .log-container::-webkit-scrollbar-thumb { background: #888; border-radius: 10px; }
    .log-container::-webkit-scrollbar-thumb:hover { background: #555; }
    .image-gallery { display: grid; grid-template-columns: repeat(auto-fit, minmax(150px, 1fr)); gap: 1rem; }
    .image-gallery img { max-width: 100%; height: auto; border-radius: 0.5rem; object-fit: cover; }
    .auction-status {
      font-weight: bold;
      padding: 0.25rem 0.5rem;
      border-radius: 0.25rem;
      display: inline-block;
      font-size: 0.9rem;
      margin-top: 0.5rem;
    }
    .status-active { background-color: #d1fae5; color: #065f46; } /* green-100 / green-800 */
    .status-waiting { background-color: #fffbeb; color: #b45309; } /* amber-100 / amber-800 */
    .status-ended { background-color: #fee2e2; color: #991b1b; } /* red-100 / red-800 */
    .countdown { font-family: monospace; font-weight: bold; }
  </style>
</head>
<body class="bg-gray-100 font-sans leading-normal tracking-normal">

<div class="container mx-auto p-4">
  <div class="flex justify-between items-center mb-8">
    <h1 class="text-4xl font-bold text-gray-800" id="auction-title-header">Auction Details</h1>
    <div class="flex items-center space-x-4">
                <span id="user-info-display" class="text-gray-700 hidden">
                    Logged in as <span id="current-username" class="font-bold"></span> (<span id="current-user-role" class="font-bold"></span>)
                </span>
      <a href="index.jsp" class="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded transition duration-300">All Auctions</a>
      <a href="login-register.html" id="login-link" class="bg-blue-500 hover:bg-blue-600 text-white font-bold py-2 px-4 rounded transition duration-300 hidden">Login / Register</a>
      <button id="logout-btn" class="bg-red-500 hover:bg-red-600 text-white font-bold py-2 px-4 rounded transition duration-300 hidden">Logout</button>
    </div>
  </div>

  <div id="auction-details-container" class="bg-white rounded-lg shadow-lg p-6 mb-8 hidden">
    <!-- Auction details will be loaded here by JavaScript -->
    <h2 class="text-3xl font-semibold text-gray-800 mb-4" id="auction-details-title"></h2>
    <span class="auction-status" id="auction-details-status"></span>
    <p class="text-gray-700 mb-4" id="auction-details-description"></p>

    <div class="image-gallery mb-6" id="auction-image-gallery">
      <!-- Images will be loaded here -->
    </div>

    <div class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
      <div>
        <p class="text-gray-600 mb-1">Starting Price: <span class="font-bold text-green-600" id="auction-details-start-price"></span></p>
        <p class="text-gray-600 mb-2">Current Bid: <span class="font-bold text-blue-600 text-2xl" id="auction-details-current-bid"></span></p>
        <p class="text-gray-600 mb-4">Winning Bidder: <span class="font-medium text-gray-700" id="auction-details-winning-bidder"></span></p>
        <p class="text-gray-500 text-sm">Min Increment: <span id="auction-details-min-increment"></span></p>
      </div>
      <div>
        <p class="text-gray-500 text-sm">Starts: <span id="auction-details-start-time"></span></p>
        <p class="text-gray-500 text-sm">Ends: <span id="auction-details-end-time"></span></p>
        <p class="text-gray-700 mt-2">Time Left: <span class="countdown text-xl" id="auction-details-countdown"></span></p>
      </div>
    </div>

    <!-- Bid Form (conditionally shown) -->
    <form id="bid-form" class="mt-4 hidden">
      <input type="number" step="0.01" placeholder="Your Bid" class="w-full p-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 mb-2" id="bid-amount-input" required>
      <button type="submit" class="w-full bg-blue-500 text-white p-2 rounded-md hover:bg-blue-600 transition duration-300">Place Bid</button>
      <p id="bid-message" class="text-red-500 text-xs italic mt-2"></p>
    </form>
    <p id="login-to-bid-message" class="text-gray-600 text-center mt-4 hidden">Please <a href="login-register.html" class="text-blue-500 hover:underline">login</a> to place a bid.</p>
    <p id="bid-disabled-message" class="text-gray-600 text-center mt-4 hidden">Bidding is not possible for this auction (ended, not started, or closed).</p>
  </div>

  <div class="bg-white rounded-lg shadow-md p-6">
    <h2 class="text-2xl font-semibold text-gray-700 mb-4">Real-time Auction Log</h2>
    <div id="auction-log" class="bg-gray-50 p-4 rounded-md border border-gray-200 log-container text-sm text-gray-700">
      <p class="text-gray-500 italic">Connecting to real-time updates...</p>
    </div>
  </div>
</div>

<script>
  const logoutBtn = document.getElementById('logout-btn');
  const loginLink = document.getElementById('login-link');
  const userInfoDisplay = document.getElementById('user-info-display');
  const currentUsernameSpan = document.getElementById('current-username');
  const currentUserRoleSpan = document.getElementById('current-user-role');
  const auctionTitleHeader = document.getElementById('auction-title-header');

  const auctionDetailsContainer = document.getElementById('auction-details-container');
  const auctionDetailsTitle = document.getElementById('auction-details-title');
  const auctionDetailsStatus = document.getElementById('auction-details-status');
  const auctionDetailsDescription = document.getElementById('auction-details-description');
  const auctionImageGallery = document.getElementById('auction-image-gallery');
  const auctionDetailsStartPrice = document.getElementById('auction-details-start-price');
  const auctionDetailsCurrentBid = document.getElementById('auction-details-current-bid');
  const auctionDetailsWinningBidder = document.getElementById('auction-details-winning-bidder');
  const auctionDetailsMinIncrement = document.getElementById('auction-details-min-increment');
  const auctionDetailsStartTime = document.getElementById('auction-details-start-time');
  const auctionDetailsEndTime = document.getElementById('auction-details-end-time');
  const auctionDetailsCountdown = document.getElementById('auction-details-countdown');

  const bidForm = document.getElementById('bid-form');
  const bidAmountInput = document.getElementById('bid-amount-input');
  const bidMessage = document.getElementById('bid-message');
  const loginToBidMessage = document.getElementById('login-to-bid-message');
  const bidDisabledMessage = document.getElementById('bid-disabled-message');

  const auctionLog = document.getElementById('auction-log');

  let websocket;
  let auctionId = null; // Store the current auction ID from URL
  let currentAuctionDetails = null; // Store fetched auction details
  let countdownInterval = null; // Store countdown interval ID

  const CONTEXT_ROOT = '/auction-system';

  // --- Utility Functions ---
  function getAuthToken() { return localStorage.getItem('authToken'); }
  function getCurrentUsername() { return localStorage.getItem('currentUsername'); }
  function getCurrentUserRole() { return localStorage.getItem('currentUserRole'); }

  function logMessage(message, className = '') {
    const p = document.createElement('p');
    p.className = `mb-1 ${className}`;
    p.textContent = `[${new Date().toLocaleTimeString()}] ${message}`;
    auctionLog.appendChild(p);
    auctionLog.scrollTop = auctionLog.scrollHeight;
  }

  function getUrlParameter(name) {
    name = name.replace(/[\[]/, '\\[').replace(/[\]]/, '\\]');
    const regex = new RegExp('[\\?&]' + name + '=([^&#]*)');
    const results = regex.exec(location.search);
    return results === null ? '' : decodeURIComponent(results[1].replace(/\+/g, ' '));
  }

  // --- Authentication/Authorization Functions ---
  function updateAuthUI() {
    const token = getAuthToken();
    const username = getCurrentUsername();
    const role = getCurrentUserRole();

    if (token && username && role) {
      userInfoDisplay.classList.remove('hidden');
      currentUsernameSpan.textContent = username;
      currentUserRoleSpan.textContent = role;
      loginLink.classList.add('hidden');
      logoutBtn.classList.remove('hidden');
    } else {
      userInfoDisplay.classList.add('hidden');
      loginLink.classList.remove('hidden');
      logoutBtn.classList.add('hidden');
    }
  }

  async function handleLogout() {
    const token = getAuthToken();
    if (!token) return;

    try {
      const response = await fetch(`${CONTEXT_ROOT}/api/auth/logout`, {
        method: 'POST',
        headers: { 'Authorization': `Bearer ${token}` }
      });

      if (response.ok) {
        logMessage('Logged out successfully.', 'text-green-600');
      } else {
        logMessage('Logout failed. Token might be invalid or expired on server.', 'text-red-500');
      }
    } catch (error) {
      logMessage('Network error during logout.', 'text-red-700');
      console.error('Logout error:', error);
    } finally {
      localStorage.removeItem('authToken');
      localStorage.removeItem('currentUsername');
      localStorage.removeItem('currentUserRole');
      // Redirect to login page after logout
      window.location.href = `${CONTEXT_ROOT}/login-register.html`;
    }
  }

  // --- WebSocket Functions ---
  function connectWebSocket() {
    if (websocket && (websocket.readyState === WebSocket.OPEN || websocket.readyState === WebSocket.CONNECTING)) {
      return;
    }

    const wsUrl = `ws://${window.location.host}${CONTEXT_ROOT}/auctionUpdates`;
    websocket = new WebSocket(wsUrl);

    websocket.onopen = function(event) {
      logMessage('WebSocket connected.', 'text-green-600');
    };

    websocket.onmessage = function(event) {
      try {
        const data = JSON.parse(event.data);
        if (data.auctionId == auctionId && data.currentBid !== undefined && data.winningBidder) { // Only update if it's THIS auction
          updateAuctionDetails(data);
          logMessage(`Auction ${data.title} (#${data.auctionId}): New bid $${data.currentBid.toFixed(2)} by ${data.winningBidder}`, 'text-blue-600');
        }
      } catch (e) {
        logMessage(`WebSocket Error: ${e.message}. Raw message: ${event.data}`, 'text-red-500');
      }
    };

    websocket.onclose = function(event) {
      logMessage('WebSocket disconnected. Trying to reconnect in 5 seconds...', 'text-red-600');
      if (getAuthToken()) {
        setTimeout(connectWebSocket, 5000);
      }
    };

    websocket.onerror = function(error) {
      logMessage('WebSocket error: ' + error.message, 'text-red-700');
    };
  }

  // --- Auction Details & Bidding Logic ---
  function getAuctionStatus(auction) {
    const now = new Date();
    const startTime = new Date(auction.startTime);
    const endTime = new Date(auction.endTime);

    if (now < startTime) {
      return { text: 'WAITING', class: 'status-waiting', canBid: false };
    } else if (now > endTime) {
      return { text: 'ENDED', class: 'status-ended', canBid: false };
    } else {
      return { text: 'ACTIVE', class: 'status-active', canBid: true };
    }
  }

  function setupCountdown(endTimeString) {
    if (countdownInterval) clearInterval(countdownInterval); // Clear any existing interval

    const endTime = new Date(endTimeString).getTime();

    const updateTimer = () => {
      const now = new Date().getTime();
      const distance = endTime - now;

      if (distance < 0) {
        clearInterval(countdownInterval);
        auctionDetailsCountdown.textContent = 'ENDED';
        auctionDetailsCountdown.classList.remove('text-red-600', 'text-orange-500');
        auctionDetailsCountdown.classList.add('text-gray-500');
        // Update bid form status to disabled
        updateBidFormVisibility();
        return;
      }

      const days = Math.floor(distance / (1000 * 60 * 60 * 24));
      const hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
      const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
      const seconds = Math.floor((distance % (1000 * 60)) / 1000);

      auctionDetailsCountdown.textContent = `${days}d ${hours}h ${minutes}m ${seconds}s`;

      if (distance < 1000 * 60 * 60) { // Less than 1 hour
        auctionDetailsCountdown.classList.remove('text-orange-500');
        auctionDetailsCountdown.classList.add('text-red-600');
      } else if (distance < 1000 * 60 * 60 * 24) { // Less than 24 hours
        auctionDetailsCountdown.classList.remove('text-red-600');
        auctionDetailsCountdown.classList.add('text-orange-500');
      }
    };

    updateTimer(); // Call immediately to avoid 1-second delay
    countdownInterval = setInterval(updateTimer, 1000);
  }

  async function fetchAuctionDetails() {
    const token = getAuthToken(); // Token is optional for viewing, but required for setting headers if present
    try {
      let headers = {};
      if (token) { // Only send Authorization header if token exists
        headers['Authorization'] = `Bearer ${token}`;
      }

      const response = await fetch(`${CONTEXT_ROOT}/api/auctions/${auctionId}`, { headers });
      if (!response.ok) {
        auctionDetailsContainer.classList.add('hidden');
        auctionTitleHeader.textContent = 'Auction Not Found';
        logMessage(`Error loading auction ${auctionId}: ${response.statusText}`, 'text-red-700');
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      currentAuctionDetails = await response.json(); // Store fetched details
      auctionDetailsContainer.classList.remove('hidden'); // Show container

      updateAuctionDisplay(currentAuctionDetails);
      setupCountdown(currentAuctionDetails.endTime);
      updateBidFormVisibility(); // Update bid form based on status/login
    } catch (error) {
      logMessage(`Failed to load auction details: ${error.message}`, 'text-red-700');
      console.error('Fetch auction details error:', error);
    }
  }

  function updateAuctionDisplay(auction) {
    document.title = `${auction.title} - Auction Details`;
    auctionTitleHeader.textContent = auction.title;
    auctionDetailsTitle.textContent = auction.title;
    auctionDetailsDescription.textContent = auction.description;

    auctionImageGallery.innerHTML = auction.imageUrls && auction.imageUrls.length > 0
            ? auction.imageUrls.map(url => `<img src="${url}" alt="${auction.title} Image">`).join('')
            : '<p class="text-gray-400">No images available for this auction.</p>';

    auctionDetailsStartPrice.textContent = `$${auction.startPrice.toFixed(2)}`;
    auctionDetailsCurrentBid.textContent = `$${auction.currentBid.toFixed(2)}`;
    auctionDetailsWinningBidder.textContent = auction.winningBidder || 'None';
    auctionDetailsMinIncrement.textContent = `$${auction.minIncrement.toFixed(2)}`;
    auctionDetailsStartTime.textContent = new Date(auction.startTime).toLocaleString();
    auctionDetailsEndTime.textContent = new Date(auction.endTime).toLocaleString();

    const status = getAuctionStatus(auction);
    auctionDetailsStatus.textContent = status.text;
    auctionDetailsStatus.className = `auction-status ${status.class}`; // Apply class for styling

    // Set min bid for input field
    bidAmountInput.min = (auction.currentBid + auction.minIncrement).toFixed(2);
  }

  // Called by WebSocket on real-time update
  function updateAuctionDetails(data) {
    // Update fields that can change
    auctionDetailsCurrentBid.textContent = `$${data.currentBid.toFixed(2)}`;
    auctionDetailsWinningBidder.textContent = data.winningBidder || 'None';
    bidAmountInput.min = (data.currentBid + (currentAuctionDetails ? currentAuctionDetails.minIncrement : 0)).toFixed(2);
    currentAuctionDetails.currentBid = data.currentBid; // Update stored data
    currentAuctionDetails.winningBidder = data.winningBidder; // Update stored data
  }

  function updateBidFormVisibility() {
    const token = getAuthToken();
    const status = currentAuctionDetails ? getAuctionStatus(currentAuctionDetails) : null;

    bidForm.classList.add('hidden');
    loginToBidMessage.classList.add('hidden');
    bidDisabledMessage.classList.add('hidden');

    if (!status) return; // No auction details yet

    if (!token) {
      loginToBidMessage.classList.remove('hidden');
    } else if (!status.canBid) {
      bidDisabledMessage.classList.remove('hidden');
    } else {
      bidForm.classList.remove('hidden');
    }
  }

  async function handleBidSubmission(event) {
    event.preventDefault();
    const token = getAuthToken();
    const bidAmount = bidAmountInput.value;

    if (!token) {
      bidMessage.textContent = 'You must be logged in to place a bid.';
      bidMessage.className = 'text-red-500 text-xs italic mt-2';
      return;
    }
    if (bidAmount === '' || isNaN(parseFloat(bidAmount))) {
      bidMessage.textContent = 'Please enter a valid bid amount.';
      bidMessage.className = 'text-red-500 text-xs italic mt-2';
      return;
    }
    if (!currentAuctionDetails || !getAuctionStatus(currentAuctionDetails).canBid) {
      bidMessage.textContent = 'Bidding is not allowed for this auction right now.';
      bidMessage.className = 'text-red-500 text-xs italic mt-2';
      return;
    }

    const apiUrl = `${CONTEXT_ROOT}/api/auctions/${auctionId}/bid`;

    try {
      const response = await fetch(apiUrl, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded',
          'Authorization': `Bearer ${token}`
        },
        body: new URLSearchParams({
          bidAmount: parseFloat(bidAmount)
        })
      });

      const message = await response.text();
      if (response.ok || response.status === 202) {
        bidMessage.textContent = `Bid received: ${message}`;
        bidMessage.className = 'text-green-600 text-xs italic mt-2';
        bidAmountInput.value = ''; // Clear input
      } else if (response.status === 401 || response.status === 403) {
        bidMessage.textContent = `Authorization failed: ${message}. Please re-login.`;
        bidMessage.className = 'text-red-500 text-xs italic mt-2';
        handleLogout(); // Redirect to login
      } else {
        bidMessage.textContent = `Error bidding: ${message}`;
        bidMessage.className = 'text-red-500 text-xs italic mt-2';
      }
    } catch (error) {
      bidMessage.textContent = `Network error: ${error.message}`;
      bidMessage.className = 'text-red-700 text-xs italic mt-2';
      console.error('Bid submission error:', error);
    }
  }


  // --- Initialization ---
  document.addEventListener('DOMContentLoaded', () => {
    logoutBtn.addEventListener('click', handleLogout);
    bidForm.addEventListener('submit', handleBidSubmission);

    auctionId = getUrlParameter('id'); // Get auction ID from URL
    if (!auctionId) {
      // If no ID in URL, redirect to main auction list
      window.location.href = `index.jsp`;
      return;
    }

    updateAuthUI(); // Update login/logout links
    fetchAuctionDetails(); // Load details for this specific auction
    connectWebSocket(); // Connect WebSocket for real-time updates for this auction
  });

  // Clean up countdown interval when navigating away
  window.addEventListener('beforeunload', () => {
    if (countdownInterval) {
      clearInterval(countdownInterval);
    }
  });
</script>
</body>
</html>