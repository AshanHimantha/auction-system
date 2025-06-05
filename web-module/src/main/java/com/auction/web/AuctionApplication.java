package com.auction.web;

import jakarta.ws.rs.ApplicationPath; // UPDATED
import jakarta.ws.rs.core.Application; // UPDATED

@ApplicationPath("api")
public class AuctionApplication extends Application {
}