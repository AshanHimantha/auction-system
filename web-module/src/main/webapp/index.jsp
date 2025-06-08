<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en" class="dark">
<head>
    <title>AuctionHub - Online Auction System</title>
    <jsp:include page="includes/head.jsp" />

</head>
<body class="bg-background text-foreground font-sans leading-normal tracking-normal">
<!-- Header/Navigation -->
<jsp:include page="includes/header.jsp" />

<!-- Hero Section -->
<section class="hero-pattern py-20 md:py-32 relative overflow-hidden mt-20">
    <div class="absolute top-0 right-0 w-1/3 h-full bg-secondary opacity-10 transform -skew-x-12"></div>
    <div class="container mx-auto px-4 relative z-10">
        <div class="flex flex-col md:flex-row items-center">
            <div class="md:w-1/2 text-center md:text-left mb-10 md:mb-0">
                <h1 class="text-4xl md:text-5xl lg:text-6xl font-bold text-foreground mb-6 relative group">
                  <span class="bg-clip-text text-transparent bg-gradient-to-b from-foreground via-primary to-foreground
                  bg-size-200 animate-gradient relative inline-block">Discover, Bid, <span class="text-primary font-extrabold">Win!</span></span>
                    <span class="absolute inset-0 w-full bg-gradient-to-r from-transparent via-white/20 to-transparent
                  opacity-0 group-hover:animate-shine"></span>
                </h1>
                <p class="text-lg md:text-xl text-muted-foreground mb-8 max-w-lg">
                    Find unique items and great deals in our online auction marketplace. Join thousands of satisfied bidders today!
                </p>
                <div class="flex flex-col sm:flex-row justify-center md:justify-start space-y-4 sm:space-y-0 sm:space-x-4">
                    <a href="auctions.html" class="shadcn-button">
                        Browse Auctions
                    </a>
                    <a href="login-register.html" class="shadcn-button shadcn-button-secondary">
                        Join Now
                    </a>
                </div>
            </div>
            <div class="md:w-1/2 relative">
                <div class="bg-card p-8 rounded-xl shadow-xl relative z-10 transform md:scale-110 border border-border">
                    <div class="flex justify-between items-start mb-4">
                        <span class="text-sm font-medium text-primary">Featured Auction</span>
                        <div class="status-active px-3 py-1 flex items-center space-x-1.5 rounded-full border border-emerald-500/30 shadow-lg shadow-emerald-500/10">
                            <span class="h-2 w-2 rounded-full bg-emerald-500 animate-pulse"></span>
                            <span class="text-xs font-medium tracking-wider uppercase">Active</span>
                        </div>
                    </div>

                    <div class="mb-4 relative">

                        <img src="https://th.bing.com/th/id/R.ad76f044acd53da43c459dc1af958f9c?rik=4UoSDv9wFjaJ3A&pid=ImgRaw&r=0" alt="Featured Auction" class="rounded-md w-full h-48 object-cover">
                        <div class="absolute bottom-2 right-2 bg-background/80 rounded-full px-3 py-1 text-xs backdrop-blur-sm">
                            14h 23m 11s
                        </div>
                    </div>

                    <div class="flex justify-between items-center">
                        <div>
                            <h3 class="font-medium">Premium Collectible</h3>
                            <p class="text-muted-foreground text-sm">Current Bid: $1,250</p>
                        </div>
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-primary gavel" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15.172 7l-6.586 6.586a2 2 0 102.828 2.828l6.414-6.586a4 4 0 00-5.656-5.656l-6.415 6.585a6 6 0 108.486 8.486L20.5 13" />
                        </svg>
                    </div>
                </div>

                <!-- Decorative elements -->
                <div class="absolute top-1/2 left-0 transform -translate-y-1/2 -translate-x-1/4 w-24 h-24 bg-primary/5 rounded-full"></div>
                <div class="absolute bottom-0 right-0 transform translate-y-1/4 w-40 h-40 bg-primary/5 rounded-full"></div>
            </div>
        </div>
    </div>
    <div class="absolute bottom-0 left-0 w-full h-[30%] bg-gradient-to-t from-background to-transparent"></div>
</section>

<!-- How It Works Section -->
<section id="how-it-works" class="py-5 relative bg-gradient-to-b from-background to-gray-950">
    <div class="container mx-auto px-4 mb-5">
        <h2 class="text-3xl font-bold text-center text-foreground mb-2">How It Works</h2>
        <p class="text-center text-muted-foreground max-w-3xl mx-auto mb-12">
            Getting started is simple. Follow these four easy steps to begin your auction journey.</p>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-8 mt-16">
            <!-- Step 1: Sign Up -->
            <div class="feature-card text-center p-8 bg-background rounded-lg border border-border relative">
                <div class="w-20 h-20 bg-muted rounded-full flex items-center justify-center mx-auto mb-6 feature-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10 text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                    </svg>
                </div>
                <h3 class="text-xl font-semibold mb-3">Sign Up</h3>
                <p class="text-muted-foreground">Create your account in seconds and verify your identity for secure bidding.</p>
            </div>

            <!-- Step 2: Browse Auctions -->
            <div class="feature-card text-center p-8 bg-background rounded-lg border border-border relative">
                <div class="w-20 h-20 bg-muted rounded-full flex items-center justify-center mx-auto mb-6 feature-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10 text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
                    </svg>
                </div>
                <h3 class="text-xl font-semibold mb-3">Browse Auctions</h3>
                <p class="text-muted-foreground">Explore thousands of verified items across various categories and collections.</p>
            </div>

            <!-- Step 3: Place Bids -->
            <div class="feature-card text-center p-8 bg-background rounded-lg border border-border relative">
                <div class="w-20 h-20 bg-muted rounded-full flex items-center justify-center mx-auto mb-6 feature-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10 text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                    </svg>
                </div>
                <h3 class="text-xl font-semibold mb-3">Place Bids</h3>
                <p class="text-muted-foreground">Bid in real-time with our intuitive interface and automated bid management.</p>
            </div>

            <!-- Step 4: Win & Collect -->
            <div class="feature-card text-center p-8 bg-background rounded-lg border border-border relative">
                <div class="w-20 h-20 bg-muted rounded-full flex items-center justify-center mx-auto mb-6 feature-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10 text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m6 2a9 9 0 11-18 0 9 9 0 0118 0z" />
                    </svg>
                </div>
                <h3 class="text-xl font-semibold mb-3">Win & Collect</h3>
                <p class="text-muted-foreground">Secure your items with our trusted payment system and global shipping.</p>
            </div>
        </div>
    </div>
    <div class="absolute bottom-0 left-0 w-full h-[30%] bg-gradient-to-t from-background to-transparent mt-10"></div>
</section>

<!-- Featured Auctions Section -->
<section class="py-16 bg-background from-background to-card">
    <div class="container mx-auto px-4">
        <div class="flex justify-between items-center mb-8">
            <h2 class="text-3xl font-bold text-foreground">Featured Auctions</h2>
            <a href="auctions.html" class="text-primary hover:underline font-medium flex items-center">
                View All Auctions
                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 ml-1" viewBox="0 0 20 20" fill="currentColor">
                    <path fill-rule="evenodd" d="M12.293 5.293a1 1 0 011.414 0l4 4a1 1 0 010 1.414l-4 4a1 1 0 01-1.414-1.414L14.586 11H3a1 1 0 110-2h11.586l-2.293-2.293a1 1 0 010-1.414z" clip-rule="evenodd" />
                </svg>
            </a>
        </div>

        <!-- Auction List -->
        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6" id="featured-auction-list">
            <p class="col-span-full text-center text-muted-foreground">Loading auctions...</p>
        </div>
    </div>
</section>

<!-- Why Choose AuctionHouse Section -->
<section class="py-16 bg-card border-y border-border">
    <div class="container mx-auto px-4">
        <h2 class="text-3xl font-bold text-center text-foreground mb-8">Why Choose Auction Hub?</h2>
        <p class="text-center text-muted-foreground max-w-3xl mx-auto mb-12">
            Our platform combines cutting-edge technology with user-friendly design to deliver the ultimate auction experience.
        </p>

        <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-8">
            <!-- Real-Time Bidding -->
            <div class="feature-card relative rounded-lg overflow-hidden group">
                <div class="gradient-border absolute inset-0 bg-gradient-to-br from-primary via-secondary to-primary opacity-70 animate-gradient-shift rounded-lg"></div>
                <div class="relative bg-background m-[2px] rounded-lg p-8 h-full flex flex-col justify-between group-hover:bg-background/90 transition-all duration-300 overflow-hidden">
                    <div class="shine-effect absolute inset-0 opacity-0 group-hover:opacity-100"></div>
                    <div class="relative z-10">
                        <div class="flex justify-center mb-6">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-primary icon-pulse" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M13 10V3L4 14h7v7l9-11h-7z" />
                            </svg>
                        </div>
                        <h3 class="text-xl font-semibold mb-3 text-center">Real-Time Bidding</h3>
                        <p class="text-muted-foreground text-center">Experience lightning-fast bidding with instant updates and zero lag.</p>
                    </div>
                </div>
            </div>

            <!-- Secure Transactions -->
            <div class="feature-card relative rounded-lg overflow-hidden group">
                <div class="gradient-border absolute inset-0 bg-gradient-to-br from-primary via-secondary to-primary opacity-70 animate-gradient-shift rounded-lg"></div>
                <div class="relative bg-background m-[2px] rounded-lg p-8 h-full flex flex-col justify-between group-hover:bg-background/90 transition-all duration-300 overflow-hidden">
                    <div class="shine-effect absolute inset-0 opacity-0 group-hover:opacity-100"></div>
                    <div class="relative z-10">
                        <div class="flex justify-center mb-6">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-primary icon-float" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 15v2m-6 4h12a2 2 0 002-2v-6a2 2 0 00-2-2H6a2 2 0 00-2 2v6a2 2 0 002 2zm10-10V7a4 4 0 00-8 0v4h8z" />
                            </svg>
                        </div>
                        <h3 class="text-xl font-semibold mb-3 text-center">Secure Transactions</h3>
                        <p class="text-muted-foreground text-center">Bank-level security ensures your transactions are safe and protected.</p>
                    </div>
                </div>
            </div>

            <!-- Global Community -->
            <div class="feature-card relative rounded-lg overflow-hidden group">
                <div class="gradient-border absolute inset-0 bg-gradient-to-br from-primary via-secondary to-primary opacity-70 animate-gradient-shift rounded-lg"></div>
                <div class="relative bg-background m-[2px] rounded-lg p-8 h-full flex flex-col justify-between group-hover:bg-background/90 transition-all duration-300 overflow-hidden">
                    <div class="shine-effect absolute inset-0 opacity-0 group-hover:opacity-100"></div>
                    <div class="relative z-10">
                        <div class="flex justify-center mb-6">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-primary icon-spin" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3.055 11H5a2 2 0 012 2v1a2 2 0 002 2 2 2 0 012 2v2.945M8 3.935V5.5A2.5 2.5 0 0010.5 8h.5a2 2 0 012 2 2 2 0 104 0 2 2 0 012-2h1.064M15 20.488V18a2 2 0 012-2h3.064M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                            </svg>
                        </div>
                        <h3 class="text-xl font-semibold mb-3 text-center">Global Community</h3>
                        <p class="text-muted-foreground text-center">Connect with collectors and enthusiasts from around the world.</p>
                    </div>
                </div>
            </div>

            <!-- Market Analytics -->
            <div class="feature-card relative rounded-lg overflow-hidden group">
                <div class="gradient-border absolute inset-0 bg-gradient-to-br from-primary via-secondary to-primary opacity-70 animate-gradient-shift rounded-lg"></div>
                <div class="relative bg-background m-[2px] rounded-lg p-8 h-full flex flex-col justify-between group-hover:bg-background/90 transition-all duration-300 overflow-hidden">
                    <div class="shine-effect absolute inset-0 opacity-0 group-hover:opacity-100"></div>
                    <div class="relative z-10">
                        <div class="flex justify-center mb-6">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-primary icon-bounce" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 19v-6a2 2 0 00-2-2H5a2 2 0 00-2 2v6a2 2 0 002 2h2a2 2 0 002-2zm0 0V9a2 2 0 012-2h2a2 2 0 012 2v10m-6 0a2 2 0 002 2h2a2 2 0 002-2m0 0V5a2 2 0 012-2h2a2 2 0 012 2v14a2 2 0 01-2 2h-2a2 2 0 01-2-2z" />
                            </svg>
                        </div>
                        <h3 class="text-xl font-semibold mb-3 text-center">Market Analytics</h3>
                        <p class="text-muted-foreground text-center">Access detailed market insights and price history for informed bidding.</p>
                    </div>
                </div>
            </div>

            <!-- 24/7 Auctions -->
            <div class="feature-card relative rounded-lg overflow-hidden group">
                <div class="gradient-border absolute inset-0 bg-gradient-to-br from-primary via-secondary to-primary opacity-70 animate-gradient-shift rounded-lg"></div>
                <div class="relative bg-background m-[2px] rounded-lg p-8 h-full flex flex-col justify-between group-hover:bg-background/90 transition-all duration-300 overflow-hidden">
                    <div class="shine-effect absolute inset-0 opacity-0 group-hover:opacity-100"></div>
                    <div class="relative z-10">
                        <div class="flex justify-center mb-6">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-primary icon-glow" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                            </svg>
                        </div>
                        <h3 class="text-xl font-semibold mb-3 text-center">24/7 Auctions</h3>
                        <p class="text-muted-foreground text-center">Bid anytime, anywhere with our round-the-clock auction platform.</p>
                    </div>
                </div>
            </div>

            <!-- Verified Items -->
            <div class="feature-card relative rounded-lg overflow-hidden group">
                <div class="gradient-border absolute inset-0 bg-gradient-to-br from-primary via-secondary to-primary opacity-70 animate-gradient-shift rounded-lg"></div>
                <div class="relative bg-background m-[2px] rounded-lg p-8 h-full flex flex-col justify-between group-hover:bg-background/90 transition-all duration-300 overflow-hidden">
                    <div class="shine-effect absolute inset-0 opacity-0 group-hover:opacity-100"></div>
                    <div class="relative z-10">
                        <div class="flex justify-center mb-6">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-primary icon-shake" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M9 12l2 2 4-4m5.618-4.016A11.955 11.955 0 0112 2.944a11.955 11.955 0 01-8.618 3.04A12.02 12.02 0 003 9c0 5.591 3.824 10.29 9 11.622 5.176-1.332 9-6.03 9-11.622 0-1.042-.133-2.052-.382-3.016z" />
                            </svg>
                        </div>
                        <h3 class="text-xl font-semibold mb-3 text-center">Verified Items</h3>
                        <p class="text-muted-foreground text-center">All items are authenticated and verified by our expert team.</p>
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Contact Section -->
<section id="contact" class="py-16 bg-background">
    <div class="container mx-auto px-4">
        <h2 class="text-3xl font-bold text-center text-foreground mb-12">Contact Us</h2>

        <div class="max-w-4xl mx-auto bg-card border border-border rounded-lg shadow-lg p-8">
            <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
                <div>
                    <h3 class="text-xl font-semibold mb-4">Get In Touch</h3>
                    <p class="text-muted-foreground mb-6">Have questions about our auction platform? Send us a message and our team will get back to you as soon as possible.</p>

                    <div class="space-y-4">
                        <div class="flex items-start">
                            <div class="flex-shrink-0 bg-muted rounded-full p-2 mr-3">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                                </svg>
                            </div>
                            <div>
                                <h4 class="text-sm font-medium text-foreground">Email</h4>
                                <p class="text-muted-foreground">ashanhimantha321@gmail.com</p>
                            </div>
                        </div>

                        <div class="flex items-start">
                            <div class="flex-shrink-0 bg-muted rounded-full p-2 mr-3">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
                                </svg>
                            </div>
                            <div>
                                <h4 class="text-sm font-medium text-foreground">Phone</h4>
                                <p class="text-muted-foreground">+94 (70) 170-5553</p>
                            </div>
                        </div>

                        <div class="flex items-start">
                            <div class="flex-shrink-0 bg-muted rounded-full p-2 mr-3">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                                </svg>
                            </div>
                            <div>
                                <h4 class="text-sm font-medium text-foreground">Address</h4>
                                <p class="text-muted-foreground">26, Wagollawaththa<br/>Mawathagama</p>
                            </div>
                        </div>
                    </div>
                </div>

                <div>
                    <form class="space-y-4">
                        <div>
                            <label for="name" class="block text-sm font-medium text-foreground mb-1">Name</label>
                            <input type="text" id="name" name="name" class="shadcn-input" placeholder="Your name" required>
                        </div>

                        <div>
                            <label for="email" class="block text-sm font-medium text-foreground mb-1">Email</label>
                            <input type="email" id="email" name="email" class="shadcn-input" placeholder="your.email@example.com" required>
                        </div>

                        <div>
                            <label for="subject" class="block text-sm font-medium text-foreground mb-1">Subject</label>
                            <input type="text" id="subject" name="subject" class="shadcn-input" placeholder="How can we help you?">
                        </div>

                        <div>
                            <label for="message" class="block text-sm font-medium text-foreground mb-1">Message</label>
                            <textarea id="message" name="message" rows="4" class="shadcn-input" placeholder="Your message here..." required></textarea>
                        </div>

                        <button type="submit" class="shadcn-button w-full">Send Message</button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- CTA Section -->
<section class="py-16 bg-card border-y border-border text-foreground">
    <div class="container mx-auto px-4 text-center">
        <h2 class="text-3xl md:text-4xl font-bold mb-6">Ready to start bidding?</h2>
        <p class="text-lg md:text-xl max-w-2xl mx-auto mb-8 text-muted-foreground">Join our auction platform today and discover amazing deals on unique items. Create your account in minutes.</p>
        <div class="flex flex-col sm:flex-row justify-center space-y-4 sm:space-y-0 sm:space-x-4">
            <a href="login-register.html" class="shadcn-button">
                Sign Up Now
            </a>
            <a href="auctions.html" class="shadcn-button shadcn-button-secondary">
                Browse Auctions
            </a>
        </div>
    </div>
</section>

<style>
    .status-active { background-color: hsl(160, 84%, 39%, 0.5); color: hsl(167, 78%, 96%); }
    .status-waiting { background-color: hsl(50, 100%, 50%, 0.5); color: hsl(37, 95%, 50%); }
    .status-ended { background-color: hsl(0, 84%, 60%, 0.5); color: hsl(0, 100%, 64%); }

    .hero-pattern {
        background-color: hsl(240 10% 3.9%);
        background-image: url("data:image/svg+xml,%3Csvg width='100' height='100' viewBox='0 0 100 100' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M11 18c3.866 0 7-3.134 7-7s-3.134-7-7-7-7 3.134-7 7 3.134 7 7 7zm48 25c3.866 0 7-3.134 7-7s-3.134-7-7-7-7 3.134-7 7 3.134 7 7 7zm-43-7c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zm63 31c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zM34 90c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zm56-76c1.657 0 3-1.343 3-3s-1.343-3-3-3-3 1.343-3 3 1.343 3 3 3zM12 86c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm28-65c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm23-11c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm-6 60c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm29 22c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zM32 63c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm57-13c2.76 0 5-2.24 5-5s-2.24-5-5-5-5 2.24-5 5 2.24 5 5 5zm-9-21c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM60 91c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM35 41c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2zM12 60c1.105 0 2-.895 2-2s-.895-2-2-2-2 .895-2 2 .895 2 2 2z' fill='%232A2A3C' fill-opacity='0.2' fill-rule='evenodd'/%3E%3C/svg%3E");
        background-size: 100% 100%;
        animation: heroPatternAnim 15s ease infinite alternate;
        position: relative;
    }

    @keyframes heroPatternAnim {
        0% {
            background-position: 0% 0%;
            background-size: 100% 100%;
        }
        50% {
            background-position: 5% 5%;
            background-size: 110% 110%;
        }
        100% {
            background-position: 0% 0%;
            background-size: 100% 100%;
        }
    }
    .gavel {
        animation: gavel 2s ease-in-out infinite;
        transform-origin: bottom right;
    }

    @keyframes gavel {
        0%, 100% { transform: rotate(0deg); }
        50% { transform: rotate(-20deg); }
    }

    .shadcn-input {
        background-color: hsl(240 3.7% 15.9%);
        border: 1px solid hsl(240 5.9% 20%);
        border-radius: 0.375rem;
        color: hsl(0 0% 98%);
        padding: 0.75rem 1rem;
        width: 100%;
        transition: all 0.2s ease;
    }

    .shadcn-input:focus {
        outline: none;
        border-color: hsl(240 5% 64.9%);
        box-shadow: 0 0 0 2px hsla(240, 5%, 64.9%, 0.2);
    }

    .shadcn-button {
        background-color: hsl(240 5.9% 90%);
        color: hsl(240 5.9% 10%);
        font-weight: 500;
        border-radius: 0.375rem;
        padding: 0.75rem 1.5rem;
        transition: all 0.2s ease;
    }

    .shadcn-button:hover {
        background-color: hsl(240 5.9% 95%);
    }

    .shadcn-button-secondary {
        background-color: hsl(240 3.7% 15.9%);
        color: hsl(0 0% 98%);
    }

    .shadcn-button-secondary:hover {
        background-color: hsl(240 3.7% 20%);
    }

    .auction-status {
        font-weight: bold;
        padding: 0.25rem 0.5rem;
        border-radius: 0.25rem;
        display: inline-block;
        font-size: 0.75rem;
    }

    .animate-gradient {
        background-size: 100% 200%;
        animation: gradientFlow 4s ease infinite alternate;
    }

    @keyframes gradientFlow {
        0% { background-position: 0% 0%; }
        100% { background-position: 0% 100%; }
    }

    .bg-size-200 {
        background-size: 200% 200%;
    }

    @keyframes shine {
        from { transform: translateX(-100%); }
        to { transform: translateX(100%); }
    }

    .animate-shine {
        animation: shine 1.5s ease-in-out;
    }

    /* Styles for Why Choose AuctionHouse section */
    .gradient-border {
        background-size: 300% 300%;
        animation: moveGradient 4s alternate infinite;
        border-bottom: 3px solid transparent;
        background-origin: border-box;
        background-clip: padding-box, border-box;
    }

    @keyframes moveGradient {
        0% { background-position: 0% 50%; }
        50% { background-position: 100% 50%; }
        100% { background-position: 0% 50%; }
    }

    .animate-gradient-shift {
        animation: gradientShift 6s ease infinite;
    }

    @keyframes gradientShift {
        0% { background-position: 0% 50%; }
        50% { background-position: 100% 50%; }
        100% { background-position: 0% 50%; }
    }

    .shine-effect {
        position: absolute;
        top: 0;
        left: -150%;
        width: 150%;
        height: 100%;
        transform: rotate(45deg);
        background: linear-gradient(
                to right,
                rgba(255, 255, 255, 0) 0%,
                rgba(255, 255, 255, 0.05) 50%,
                rgba(255, 255, 255, 0) 100%
        );
        animation: shine 1.5s ease-in-out infinite;
    }

    @keyframes shine {
        0% { left: -150%; }
        100% { left: 150%; }
    }

    .feature-card:hover .shine-effect {
        animation: shine 1.5s ease-in-out;
    }

    /* New styles for feature icons and border treatment */
    .feature-card {
        position: relative;
        border-bottom: 4px solid transparent;
    }

    .feature-card::after {
        content: '';
        position: absolute;
        bottom: 0;
        left: 0;
        right: 0;
        height: 4px;
        background: linear-gradient(to right, var(--primary), var(--secondary), var(--primary));
        background-size: 200% auto;
        animation: gradient 3s ease infinite;
    }

    @keyframes gradient {
        0% {
            background-position: 0% 50%;
        }
        50% {
            background-position: 100% 50%;
        }
        100% {
            background-position: 0% 50%;
        }
    }

    /* Icon animations */
    .icon-pulse {
        animation: iconPulse 2s ease-in-out infinite;
    }

    .icon-spin {
        animation: iconSpin 6s linear infinite;
    }

    .icon-bounce {
        animation: iconBounce 2s ease-in-out infinite;
    }

    .icon-float {
        animation: iconFloat 3s ease-in-out infinite;
    }

    .icon-shake {
        animation: iconShake 2s ease-in-out infinite;
    }

    .icon-glow {
        animation: iconGlow 2s ease-in-out infinite;
        filter: drop-shadow(0 0 2px var(--primary));
    }

    @keyframes iconPulse {
        0%, 100% { transform: scale(1); }
        50% { transform: scale(1.1); }
    }

    @keyframes iconSpin {
        0% { transform: rotate(0deg); }
        100% { transform: rotate(360deg); }
    }

    @keyframes iconBounce {
        0%, 100% { transform: translateY(0); }
        50% { transform: translateY(-10px); }
    }

    @keyframes iconFloat {
        0%, 100% { transform: translateY(0) rotate(0); }
        50% { transform: translateY(-5px) rotate(2deg); }
    }

    @keyframes iconShake {
        0%, 100% { transform: translateX(0); }
        10%, 30%, 50%, 70%, 90% { transform: translateX(-2px); }
        20%, 40%, 60%, 80% { transform: translateX(2px); }
    }

    @keyframes iconGlow {
        0%, 100% { filter: drop-shadow(0 0 2px var(--primary)); }
        50% { filter: drop-shadow(0 0 8px var(--primary)); }
    }
</style>
<!-- Footer -->
<jsp:include page="includes/footer.jsp" />
<!-- Replace the existing script block with these script tags -->
<script src="js/header.js"></script>
<script src="js/homepage.js"></script>
</body>
</html>