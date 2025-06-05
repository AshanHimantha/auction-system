package com.auction.web;

import com.auction.ejb.AuctionManager;
import com.auction.ejb.BidManager;
import com.auction.entity.Auction;
import jakarta.ejb.EJB; // UPDATED
import jakarta.json.Json; // UPDATED
import jakarta.json.JsonObject; // UPDATED
import jakarta.ws.rs.*; // UPDATED
import jakarta.ws.rs.core.Context; // UPDATED
import jakarta.ws.rs.core.MediaType; // UPDATED
import jakarta.ws.rs.core.Response; // UPDATED
import jakarta.ws.rs.core.SecurityContext; // UPDATED

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
    @Authenticated // Only authenticated users can bid
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response placeBid(
            @PathParam("id") Long auctionId,
            @FormParam("bidAmount") double bidAmount,
            @Context SecurityContext securityContext) {

        String bidderName = securityContext.getUserPrincipal().getName();
        System.out.println("Received bid: Auction ID " + auctionId + ", Amount " + bidAmount + ", Bidder " + bidderName);
        String result = bidManager.submitBid(auctionId, bidAmount, bidderName);

        return Response.accepted(result).build();
    }

    @POST
    @Authenticated
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewAuction(JsonObject auctionData) {
        try {
            String title = auctionData.getString("title");
            double startingBid = auctionData.getJsonNumber("startingBid").doubleValue();
            double minIncrement = auctionData.getJsonNumber("minIncrement").doubleValue();

            Auction newAuction = auctionManager.createAuction(title, startingBid, minIncrement);
            return Response.status(Response.Status.CREATED).entity(newAuction).build();
        } catch (Exception e) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Error creating auction: " + e.getMessage()).build();
        }
    }
}