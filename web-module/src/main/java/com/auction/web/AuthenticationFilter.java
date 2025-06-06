package com.auction.web;

import com.auction.ejb.Authentication;
import jakarta.annotation.Priority;
import jakarta.ejb.EJB;
import jakarta.ws.rs.Priorities;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.SecurityContext;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;

@Authenticated // This filter applies to methods/classes annotated with @Authenticated
@Provider // Makes it discoverable by JAX-RS runtime
@Priority(Priorities.AUTHENTICATION) // Runs before authorization filters
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(AuthenticationFilter.class.getName());
    private static final String AUTH_SCHEME = "Bearer";

    @EJB
    private Authentication authenticationBean; // Inject the Authentication EJB

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        String authorizationHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authorizationHeader == null || !authorizationHeader.startsWith(AUTH_SCHEME + " ")) {
            LOGGER.log(Level.WARNING, "AuthenticationFilter: No authorization header or invalid scheme for path: {0}", requestContext.getUriInfo().getAbsolutePath());
            abortWithUnauthorized(requestContext);
            return;
        }

        String token = authorizationHeader.substring(AUTH_SCHEME.length()).trim();

        try {
            if (!authenticationBean.validateToken(token)) {
                LOGGER.log(Level.WARNING, "AuthenticationFilter: Invalid or expired token: {0}", token);
                abortWithUnauthorized(requestContext);
                return;
            }

            final String username = authenticationBean.getUsernameFromToken(token);
            final String role = authenticationBean.getRoleFromToken(token); // NEW: Get user's role from token

            if (username == null || role == null) { // Ensure both username and role are present
                LOGGER.log(Level.WARNING, "AuthenticationFilter: Token not associated with a user or role: {0}", token);
                abortWithUnauthorized(requestContext);
                return;
            }

            LOGGER.log(Level.INFO, "AuthenticationFilter: User {0} (Role: {1}) authenticated successfully for path: {2}",
                    new Object[]{username, role, requestContext.getUriInfo().getAbsolutePath()});

            final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> username;
                }

                @Override
                public boolean isUserInRole(String roleName) { // NEW: Check if the user's role matches the requested roleName
                    return role != null && role.equalsIgnoreCase(roleName);
                }

                @Override
                public boolean isSecure() {
                    return currentSecurityContext.isSecure();
                }

                @Override
                public String getAuthenticationScheme() {
                    return AUTH_SCHEME;
                }
            });

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "AuthenticationFilter: Error during token validation: " + e.getMessage(), e);
            requestContext.abortWith(Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Authentication error: " + e.getMessage())
                    .build());
        }
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED)
                .header(HttpHeaders.WWW_AUTHENTICATE, AUTH_SCHEME + " realm=\"AuctionSystem\"")
                .entity("Authentication required or token invalid/expired.")
                .build());
    }
}