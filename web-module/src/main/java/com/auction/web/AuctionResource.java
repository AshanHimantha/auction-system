package com.auction.web;

import com.auction.ejb.AuctionManager;
import com.auction.ejb.BidManager;
import com.auction.entity.Auction;
import com.auction.entity.AuctionCategory; // NEW IMPORT: To return categories
import com.auction.entity.AuctionBidHistory; // NEW IMPORT: To return bid history
import com.auction.ejb.AuctionInMemoryStorageSingleton; // Add this import for direct access to bid history
import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Path("/auctions")
public class AuctionResource {

    @EJB
    private AuctionManager auctionManager;

    @EJB
    private BidManager bidManager;

    @EJB
    private AuctionInMemoryStorageSingleton auctionStorage; // Add direct reference to storage singleton

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    // IMPORTANT: Renamed to getAllAuctions() to match AuctionManager interface
    public Response getAllAuctions() {
        Collection<Auction> auctions = auctionManager.getAllAuctions(); // Call the renamed method
        if (auctions.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No auctions found.").build();
        }
        return Response.ok(auctions).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuctionDetails(@PathParam("id") Long id) {
        Auction auction = auctionManager.getAuctionDetails(id);
        if (auction == null) {
            return Response.status(Response.Status.NOT_FOUND).entity("Auction with ID " + id + " not found.").build();
        }
        return Response.ok(auction).build();
    }

    // NEW ENDPOINT: Get All Categories
    @GET
    @Path("/categories")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCategories() {
        Collection<AuctionCategory> categories = auctionManager.getAllCategories();
        if (categories.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No categories found.").build();
        }
        return Response.ok(categories).build();
    }

    // FIXED ENDPOINT: Get Bid History for an Auction
    @GET
    @Path("/{id}/history")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuctionHistory(@PathParam("id") Long auctionId) {
        List<AuctionBidHistory> history = auctionStorage.getBidHistory(auctionId);
        if (history == null || history.isEmpty()) {
            // Return an empty array instead of 404 - this is more RESTful for collections
            return Response.ok("[]").build();
        }
        return Response.ok(history).build();
    }


    @POST
    @Path("/{id}/bid")
    @Authenticated
    @RolesAllowed({"ADMIN", "USER"})
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response placeBid(
            @PathParam("id") Long auctionId,
            @FormParam("bidAmount") double bidAmount,
            @Context SecurityContext securityContext) {

        String bidderName = securityContext.getUserPrincipal().getName();
        System.out.println("DEBUG (AuthResource): Bidder Name from SecurityContext: " + bidderName);
        System.out.println("Received bid: Auction ID " + auctionId + ", Amount " + bidAmount + ", Bidder " + bidderName);
        String result = bidManager.submitBid(auctionId, bidAmount, bidderName);

        return Response.accepted(result).build();
    }

    @POST
    @Authenticated
    @RolesAllowed("ADMIN")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create")
    public Response createNewAuction(JsonObject auctionData) {
        try {
            String title = auctionData.getString("title");
            String description = auctionData.getString("description");
            String[] imageUrls = auctionData.getJsonArray("imageUrls").stream()
                    .map(v -> v.toString().replaceAll("\"", ""))
                    .toArray(String[]::new);
            double startPrice = auctionData.getJsonNumber("startPrice").doubleValue();
            double minIncrement = auctionData.getJsonNumber("minIncrement").doubleValue();
            LocalDateTime startTime = LocalDateTime.parse(auctionData.getString("startTime"));
            LocalDateTime endTime = LocalDateTime.parse(auctionData.getString("endTime"));
            Long categoryId = auctionData.getJsonNumber("categoryId").longValue(); // NEW: Get category ID

            // MODIFIED: Pass categoryId to createAuction
            Auction newAuction = auctionManager.createAuction(title, description, imageUrls, startPrice, minIncrement, startTime, endTime, categoryId);
            return Response.status(Response.Status.CREATED).entity(newAuction).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.BAD_REQUEST).entity("Error creating auction: " + e.getMessage()).build();
        }
    }
}