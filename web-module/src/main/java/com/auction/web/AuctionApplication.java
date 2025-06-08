package com.auction.web;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("api")
public class AuctionApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> classes = new HashSet<>();
        // Register resource classes
        classes.add(AuctionResource.class);
        classes.add(CategoryResource.class);
        classes.add(AuthResource.class);
        return classes;
    }
}