package com.auction.web;

import com.auction.ejb.Authentication;
import jakarta.ejb.EJB;
import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;

import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/auth")
public class AuthResource {

    private static final Logger LOGGER = Logger.getLogger(AuthResource.class.getName());

    @EJB
    private Authentication authenticationBean;

    @POST
    @Path("/login")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(JsonObject loginData) {
        String username = loginData.getString("username");
        String password = loginData.getString("password");

        LOGGER.log(Level.INFO, "Login attempt for user: {0}", username);

        String token = authenticationBean.login(username, password);
        if (token != null) {
            LOGGER.log(Level.INFO, "Login successful for user: {0}", username);
            return Response.ok(Json.createObjectBuilder()
                            .add("message", "Login successful")
                            .add("token", token)
                            .add("username", username)
                            .build())
                    .build();
        } else {
            LOGGER.log(Level.WARNING, "Login failed for user: {0}", username);
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(Json.createObjectBuilder().add("message", "Invalid credentials").build())
                    .build();
        }
    }

    @POST
    @Path("/logout")
    @Authenticated
    @Produces(MediaType.TEXT_PLAIN)
    public Response logout(@Context SecurityContext securityContext,
                           @HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        String username = securityContext.getUserPrincipal().getName();
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return Response.status(Response.Status.BAD_REQUEST).entity("Invalid authorization header.").build();
        }
        String token = authHeader.substring("Bearer ".length()).trim();

        authenticationBean.logout(token);
        LOGGER.log(Level.INFO, "User {0} logged out successfully.", username);
        return Response.ok("Logged out successfully.").build();
    }

    @GET
    @Path("/me")
    @Authenticated
    @Produces(MediaType.APPLICATION_JSON)
    public Response whoami(@Context SecurityContext securityContext) {
        String username = securityContext.getUserPrincipal().getName();
        return Response.ok(Json.createObjectBuilder().add("username", username).build()).build();
    }

    // --- NEW ENDPOINT FOR USER REGISTRATION ---
    @POST
    @Path("/register")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response register(JsonObject registrationData) {
        String username = registrationData.getString("username");
        String password = registrationData.getString("password");

        LOGGER.log(Level.INFO, "Registration attempt for user: {0}", username);

        if (authenticationBean.registerUser(username, password)) {
            LOGGER.log(Level.INFO, "User registered successfully: {0}", username);
            return Response.status(Response.Status.CREATED)
                    .entity(Json.createObjectBuilder().add("message", "Registration successful. You can now log in.").build())
                    .build();
        } else {
            LOGGER.log(Level.WARNING, "Registration failed for user: {0} (possibly username already exists or invalid data).", username);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Json.createObjectBuilder().add("message", "Registration failed. Username might already exist or invalid data provided.").build())
                    .build();
        }
    }
}