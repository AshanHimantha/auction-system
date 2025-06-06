package com.auction.web;

import com.auction.ejb.AuctionManager;
import com.auction.ejb.BidManager;
import com.auction.entity.Auction;
import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;

import java.time.LocalDateTime; // NEW IMPORT
import java.util.Collection;

@Path("/auctions")
public class AuctionResource {

    @EJB
    private AuctionManager auctionManager;

    @EJB
    private BidManager bidManager;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllActiveAuctions() {
        Collection<Auction> auctions = auctionManager.getAllActiveAuctions();
        if (auctions.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No active auctions found.").build();
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

    @POST
    @Path("/{id}/bid")
    @Authenticated // Requires authentication
    @RolesAllowed({"ADMIN", "USER"}) // Both admin and regular users can bid
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response placeBid(
            @PathParam("id") Long auctionId,
            @FormParam("bidAmount") double bidAmount,
            @Context SecurityContext securityContext) {

        String bidderName = securityContext.getUserPrincipal().getName();
        System.out.println("DEBUG (AuthResource): Bidder Name from SecurityContext: " + bidderName); // Keep debug if needed
        System.out.println("Received bid: Auction ID " + auctionId + ", Amount " + bidAmount + ", Bidder " + bidderName);
        String result = bidManager.submitBid(auctionId, bidAmount, bidderName);

        return Response.accepted(result).build();
    }

    // NEW ENDPOINT: Create New Auction (Admin Only)
    @POST
    @Authenticated // Requires authentication
    @RolesAllowed("ADMIN") // Only ADMIN role can create auctions
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/create") // Adding a sub-path for clarity
    public Response createNewAuction(JsonObject auctionData) {
        try {
            String title = auctionData.getString("title");
            String description = auctionData.getString("description");
            String[] imageUrls = auctionData.getJsonArray("imageUrls").stream()
                    .map(v -> v.toString().replaceAll("\"", "")) // Clean up quotes
                    .toArray(String[]::new);
            double startPrice = auctionData.getJsonNumber("startPrice").doubleValue();
            double minIncrement = auctionData.getJsonNumber("minIncrement").doubleValue();
            LocalDateTime startTime = LocalDateTime.parse(auctionData.getString("startTime")); // Assuming ISO-8601 string
            LocalDateTime endTime = LocalDateTime.parse(auctionData.getString("endTime")); // Assuming ISO-8601 string

            Auction newAuction = auctionManager.createAuction(title, description, imageUrls, startPrice, minIncrement, startTime, endTime);
            return Response.status(Response.Status.CREATED).entity(newAuction).build();
        } catch (Exception e) {
            e.printStackTrace(); // Log the full stack trace for debugging
            return Response.status(Response.Status.BAD_REQUEST).entity("Error creating auction: " + e.getMessage()).build();
        }
    }
}