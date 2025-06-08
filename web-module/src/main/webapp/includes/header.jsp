<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- Header/Navigation -->
<header class="bg-card shadow-md border-b border-border fixed top-0 w-full z-[1000]">
    <div class="container mx-auto px-4 py-4">
        <div class="flex justify-between items-center">
            <div class="flex items-center">
                <div class="w-10 h-10 bg-primary rounded-full flex items-center justify-center mr-3">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6 text-primary-foreground" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                    </svg>
                </div>
                <a href="index.jsp" class="font-bold text-2xl text-foreground">AuctionHub</a>
            </div>

            <nav class="hidden md:flex space-x-8">
                <a href="index.jsp" class="text-primary font-medium">Home</a>
                <a href="auctions.jsp" class="text-muted-foreground hover:text-primary transition duration-300">Auctions</a>
                <a href="#" class="text-muted-foreground hover:text-primary transition duration-300">Categories</a>
                <a href="#how-it-works" class="text-muted-foreground hover:text-primary transition duration-300">How It Works</a>
                <a href="#contact" class="text-muted-foreground hover:text-primary transition duration-300">Contact</a>
            </nav>

            <div class="flex items-center space-x-4">
                <a href="login-register.jsp" class="md:block shadcn-button" id="login-button">Login</a>
                <a href="create-auction.jsp" id="create-auction-link-nav" class="bg-muted hover:bg-accent text-foreground font-medium py-2 px-4 rounded transition duration-300 hidden">Create Auction</a>
                <div id="user-profile" class="hidden">
                    <span id="username-display" class="text-muted-foreground mr-2"></span>
                    <button id="logout-button" class="text-red-400 hover:text-red-300">Logout</button>
                </div>
                <button class="md:hidden" id="mobile-menu-button">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-6 w-6" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 6h16M4 12h16M4 18h16" />
                    </svg>
                </button>
            </div>
        </div>

        <!-- Mobile Menu (hidden by default) -->
        <div class="md:hidden hidden mt-4 pb-4" id="mobile-menu">
            <a href="index.jsp" class="block py-2 text-primary font-medium">Home</a>
            <a href="auctions.jsp" class="block py-2 text-muted-foreground">Auctions</a>
            <a href="#" class="block py-2 text-muted-foreground">Categories</a>
            <a href="#how-it-works" class="block py-2 text-muted-foreground">How It Works</a>
            <a href="#contact" class="block py-2 text-muted-foreground">Contact</a>
            <a href="login-register.jsp" class="block py-2 text-muted-foreground" id="mobile-login-button">Login</a>
        </div>
    </div>
</header>