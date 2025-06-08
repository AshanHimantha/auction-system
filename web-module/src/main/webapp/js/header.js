// header.js

// Constants
const CONTEXT_ROOT = '/auction-system';

// --- Auth Utility Functions ---
function getAuthToken() { return localStorage.getItem('authToken'); }
function getCurrentUsername() { return localStorage.getItem('currentUsername'); }
function getCurrentUserRole() { return localStorage.getItem('currentUserRole'); }

// --- Authentication UI Update ---
function updateUIBasedOnAuth() {
    const token = getAuthToken();
    const username = getCurrentUsername();
    const userRole = getCurrentUserRole();

    // DOM Elements for auth UI
    const createAuctionLinkNav = document.getElementById('create-auction-link-nav');
    const loginButton = document.getElementById('login-button');
    const mobileLoginButton = document.getElementById('mobile-login-button');
    const userProfileDiv = document.getElementById('user-profile');
    const usernameDisplay = document.getElementById('username-display');

    if (token && username) {
        // User is logged in
        loginButton.classList='hidden'; // Hide desktop login button
        mobileLoginButton.classList.add('hidden'); // Hide mobile login button

        userProfileDiv.classList.remove('hidden'); // Show user profile section
        usernameDisplay.textContent = username;

        // Show create auction link for admins, hide for others
        if (userRole === 'ADMIN') {
            createAuctionLinkNav.classList.remove('hidden');
        } else {
            createAuctionLinkNav.classList.add('hidden');
        }
    } else {
        // User is not logged in
        loginButton.classList.remove('hidden'); // Show desktop login button
        mobileLoginButton.classList.remove('hidden'); // Show mobile login button

        userProfileDiv.classList.add('hidden'); // Hide user profile section
        createAuctionLinkNav.classList.add('hidden'); // Hide create auction link
    }
}

// --- Logout function ---
function logout() {
    localStorage.removeItem('authToken');
    localStorage.removeItem('currentUsername');
    localStorage.removeItem('currentUserRole');
    updateUIBasedOnAuth(); // Update UI after logout
    window.location.href = 'index.jsp'; // Redirect to home page
}

// --- Mobile menu toggle ---
function toggleMobileMenu() {
    const mobileMenu = document.getElementById('mobile-menu');
    mobileMenu.classList.toggle('hidden');
}

// --- Initialization ---
document.addEventListener('DOMContentLoaded', () => {
    // Initialize auth UI
    updateUIBasedOnAuth();

    // Set up event listeners
    const logoutButton = document.getElementById('logout-button');
    if (logoutButton) {
        logoutButton.addEventListener('click', logout);
    }

    const mobileMenuButton = document.getElementById('mobile-menu-button');
    if (mobileMenuButton) {
        mobileMenuButton.addEventListener('click', toggleMobileMenu);
    }
});