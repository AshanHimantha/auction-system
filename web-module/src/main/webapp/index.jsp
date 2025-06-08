
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
                    <a href="auctions.jsp" class="shadcn-button">
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
<section id="how-it-works" class="py-16 relative bg-gradient-to-b from-background to-card">
    <div class="container mx-auto px-4">
        <h2 class="text-3xl font-bold text-center text-foreground mb-12">How It Works</h2>

        <style>
            .feature-card {
                position: relative;
                transition: all 0.4s ease;
                overflow: hidden;
                z-index: 1;
                border-bottom: 3px solid transparent;
                background-clip: padding-box;
            }

            /* Gradient bottom border animation */
            .feature-card::before {
                content: '';
                position: absolute;
                left: 0;
                right: 0;
                bottom: 0;
                height: 3px;
                background: linear-gradient(
                        90deg,
                        hsl(240 5.9% 90%),
                        hsl(0, 0%, 21%),
                        hsl(0, 0%, 38%),
                        hsl(240 5% 64.9%),
                        hsl(240 5.9% 90%)
                );
                background-size: 300% 100%;
                z-index: 1;
                animation: gradientBorder 6s ease infinite;
            }

            /* Shine effect only on hover */
            .feature-card::after {
                content: '';
                position: absolute;
                top: 0;
                left: -100%;
                width: 80%;
                height: 100%;
                background: linear-gradient(
                        90deg,
                        transparent,
                        rgba(255, 255, 255, 0.2),
                        transparent
                );
                z-index: 0;
                opacity: 0;
                transition: opacity 0.3s ease;
            }

            .feature-card:hover::after {
                opacity: 1;
                animation: shine 0.8s forwards;
            }

            .feature-card:hover {
                transform: translateY(-10px);
                box-shadow: 0 20px 30px -15px rgba(0, 0, 0, 0.3);
            }

            .feature-card:hover .feature-icon {
                transform: scale(1.1) rotate(5deg);
            }

            .feature-icon {
                transition: all 0.4s ease;
                z-index: 2;
                position: relative;
            }

            @keyframes gradientBorder {
                0% { background-position: 0% 50% }
                50% { background-position: 100% 50% }
                100% { background-position: 0% 50% }
            }

            @keyframes shine {
                0% { left: -100%; }
                100% { left: 100%; }
            }
        </style>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-8 mt-16">
            <!-- Card 1 -->
            <div class="feature-card text-center p-8 bg-background rounded-lg border border-border relative">
                <div class="w-20 h-20 bg-muted rounded-full flex items-center justify-center mx-auto mb-6 feature-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10 text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                    </svg>
                </div>
                <h3 class="text-xl font-semibold mb-3">Create an Account</h3>
                <p class="text-muted-foreground">Sign up for free and set up your profile to start participating in exciting auctions today.</p>
            </div>

            <!-- Card 2 -->
            <div class="feature-card text-center p-8 bg-background rounded-lg border border-border relative">
                <div class="w-20 h-20 bg-muted rounded-full flex items-center justify-center mx-auto mb-6 feature-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10 text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 15l-2 5L9 9l11 4-5 2zm0 0l5 5M7.188 2.239l.777 2.897M5.136 7.965l-2.898-.777M13.95 4.05l-2.122 2.122m-5.657 5.656l-2.12 2.122" />
                    </svg>
                </div>
                <h3 class="text-xl font-semibold mb-3">Find & Bid on Items</h3>
                <p class="text-muted-foreground">Browse our extensive collection and place your bids on items you're interested in winning.</p>
            </div>

            <!-- Card 3 -->
            <div class="feature-card text-center p-8 bg-background rounded-lg border border-border relative">
                <div class="w-20 h-20 bg-muted rounded-full flex items-center justify-center mx-auto mb-6 feature-icon">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10 text-primary" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 8v13m0-13V6a2 2 0 112 2h-2zm0 0V5.5A2.5 2.5 0 109.5 8H12zm-7 4h14M5 12a2 2 0 110-4h14a2 2 0 110 4M5 12v7a2 2 0 002 2h10a2 2 0 002-2v-7" />
                    </svg>
                </div>
                <h3 class="text-xl font-semibold mb-3">Win & Receive</h3>
                <p class="text-muted-foreground">Pay securely for your winning bids and receive your items directly from trusted sellers.</p>
            </div>
        </div>
    </div>
</section>

<!-- Featured Auctions Section -->
<section class="py-16 bg-background">
    <div class="container mx-auto px-4">
        <div class="flex justify-between items-center mb-8">
            <h2 class="text-3xl font-bold text-foreground">Featured Auctions</h2>
            <a href="auctions.jsp" class="text-primary hover:underline font-medium flex items-center">
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

<!-- Categories Section -->
<section class="py-16 bg-card border-y border-border">
    <div class="container mx-auto px-4">
        <h2 class="text-3xl font-bold text-center text-foreground mb-12">Popular Categories</h2>

        <div class="grid grid-cols-2 md:grid-cols-4 gap-4">
            <a href="#" class="group">
                <div class="bg-background rounded-lg overflow-hidden border border-border transition-all duration-300 group-hover:border-primary">
                    <div class="h-32 bg-muted/20 flex items-center justify-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-primary/80" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 18h.01M8 21h8a2 2 0 002-2V5a2 2 0 00-2-2H8a2 2 0 00-2 2v14a2 2 0 002 2z" />
                        </svg>
                    </div>
                    <div class="p-4 text-center">
                        <h3 class="font-medium text-foreground">Electronics</h3>
                        <p class="text-sm text-muted-foreground mt-1">142 items</p>
                    </div>
                </div>
            </a>

            <a href="#" class="group">
                <div class="bg-background rounded-lg overflow-hidden border border-border transition-all duration-300 group-hover:border-primary">
                    <div class="h-32 bg-muted/20 flex items-center justify-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-primary/80" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 3h2l.4 2M7 13h10l4-8H5.4M7 13L5.4 5M7 13l-2.293 2.293c-.63.63-.184 1.707.707 1.707H17m0 0a2 2 0 100 4 2 2 0 000-4zm-8 2a2 2 0 11-4 0 2 2 0 014 0z" />
                        </svg>
                    </div>
                    <div class="p-4 text-center">
                        <h3 class="font-medium text-foreground">Collectibles</h3>
                        <p class="text-sm text-muted-foreground mt-1">89 items</p>
                    </div>
                </div>
            </a>

            <a href="#" class="group">
                <div class="bg-background rounded-lg overflow-hidden border border-border transition-all duration-300 group-hover:border-primary">
                    <div class="h-32 bg-muted/20 flex items-center justify-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-primary/80" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
                        </svg>
                    </div>
                    <div class="p-4 text-center">
                        <h3 class="font-medium text-foreground">Real Estate</h3>
                        <p class="text-sm text-muted-foreground mt-1">28 items</p>
                    </div>
                </div>
            </a>

            <a href="#" class="group">
                <div class="bg-background rounded-lg overflow-hidden border border-border transition-all duration-300 group-hover:border-primary">
                    <div class="h-32 bg-muted/20 flex items-center justify-center">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-12 w-12 text-primary/80" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M5 3v4M3 5h4M6 17v4m-2-2h4m5-16l2.286 6.857L21 12l-5.714 2.143L13 21l-2.286-6.857L5 12l5.714-2.143L13 3z" />
                        </svg>
                    </div>
                    <div class="p-4 text-center">
                        <h3 class="font-medium text-foreground">Jewelry</h3>
                        <p class="text-sm text-muted-foreground mt-1">56 items</p>
                    </div>
                </div>
            </a>
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
            <a href="auctions.jsp" class="shadcn-button shadcn-button-secondary">
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

</style>
<!-- Footer -->
<jsp:include page="includes/footer.jsp" />
<!-- Replace the existing script block with these script tags -->
<script src="js/header.js"></script>
<script src="js/homepage.js"></script>
</body>
</html>