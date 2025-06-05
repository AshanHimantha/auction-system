package com.auction.web;

import com.auction.ejb.Authentication;
import jakarta.annotation.Priority; // UPDATED
import jakarta.ejb.EJB; // UPDATED
import jakarta.ws.rs.Priorities; // UPDATED
import jakarta.ws.rs.container.ContainerRequestContext; // UPDATED
import jakarta.ws.rs.container.ContainerRequestFilter; // UPDATED
import jakarta.ws.rs.core.HttpHeaders; // UPDATED
import jakarta.ws.rs.core.Response; // UPDATED
import jakarta.ws.rs.core.SecurityContext; // UPDATED
import jakarta.ws.rs.ext.Provider; // UPDATED

import java.io.IOException;
import java.security.Principal;
import java.util.logging.Level;
import java.util.logging.Logger;

@Authenticated
@Provider
@Priority(Priorities.AUTHENTICATION)
public class AuthenticationFilter implements ContainerRequestFilter {

    private static final Logger LOGGER = Logger.getLogger(AuthenticationFilter.class.getName());
    private static final String AUTH_SCHEME = "Bearer";

    @EJB
    private Authentication authenticationBean;

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
            if (username == null) {
                LOGGER.log(Level.WARNING, "AuthenticationFilter: Token not associated with a user: {0}", token);
                abortWithUnauthorized(requestContext);
                return;
            }

            LOGGER.log(Level.INFO, "AuthenticationFilter: User {0} authenticated successfully.", username);

            final SecurityContext currentSecurityContext = requestContext.getSecurityContext();
            requestContext.setSecurityContext(new SecurityContext() {
                @Override
                public Principal getUserPrincipal() {
                    return () -> username;
                }

                @Override
                public boolean isUserInRole(String role) {
                    return "user".equals(role);
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