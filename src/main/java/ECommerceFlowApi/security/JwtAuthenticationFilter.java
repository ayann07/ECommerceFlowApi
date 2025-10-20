package ECommerceFlowApi.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // Get the Authorization header from the request.
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Check if the header is null or doesn't start with "Bearer ".
        // If so, we pass the request to the next filter and stop processing.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract the JWT from the "Bearer " prefix.
        jwt = authHeader.substring(7);

        // Extract the user's email from the token using our JwtTokenProvider.
        userEmail = jwtTokenProvider.extractUsername(jwt);

        // Check if we have a user email and that the user is not already
        // authenticated.
        // SecurityContextHolder.getContext().getAuthentication() == null ensures we
        // only authenticate once per request.
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load the user's details from the database via UserDetailsService.
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            // Check if the token is valid for the loaded user.
            if (jwtTokenProvider.isTokenValid(jwt, userDetails)) {

                // If the token is valid, create an authentication object.
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // We don't need credentials for token-based auth
                        userDetails.getAuthorities());

                // Set extra details for the authentication object from the HTTP request.
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                // Update the SecurityContextHolder with the new authentication token.
                // This is the step that marks the user as authenticated for this request.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Pass the request and response to the next filter in the chain.
        filterChain.doFilter(request, response);
    }

}
