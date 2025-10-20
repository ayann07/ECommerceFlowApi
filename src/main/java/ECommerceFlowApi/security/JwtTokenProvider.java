package ECommerceFlowApi.security; // Or your package name

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * A service for handling all operations related to JSON Web Tokens (JWTs).
 * This includes generating, validating, and extracting information from the
 * tokens.
 */
@Service
public class JwtTokenProvider {

    // Injects the secret key from the application.properties file. This key is used
    // to sign the JWT.
    @Value("${jwt.secret}")
    private String secretKey;

    // Injects the token expiration time in milliseconds from the
    // application.properties file.
    @Value("${jwt.expiration-ms}")
    private long jwtExpiration;

    /**
     * Extracts the username (which is the 'subject' claim in a JWT) from a given
     * token.
     * 
     * @param token The JWT to parse.
     * @return The username (email) contained within the token.
     */
    public String extractUsername(String token) {
        // It uses the generic extractClaim method, passing a function to get the
        // subject.
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * A convenience method to generate a token for a user without any extra claims.
     * 
     * @param userDetails The user details object from Spring Security.
     * @return A signed JWT string.
     */
    public String generateToken(UserDetails userDetails) {
        // It calls the more detailed generateToken method with an empty map for extra
        // claims.
        return generateTokenHelper(new HashMap<>(), userDetails);
    }

    /**
     * The main method for generating a new JWT.
     * 
     * @param extraClaims A map of any custom data you want to include in the token.
     * @param userDetails The user for whom the token is being generated.
     * @return A signed, URL-safe JWT string.
     */
    public String generateTokenHelper(Map<String, Object> extraClaims, UserDetails userDetails) {
        // Uses the JJWT library's builder pattern to construct the token.
        return Jwts.builder()
                // Sets the custom claims.
                .setClaims(extraClaims)
                // Sets the 'subject' of the token, which is conventionally the user's unique
                // identifier.
                .setSubject(userDetails.getUsername())
                // Sets the 'issued at' time to the current moment.
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // Sets the 'expiration' time based on the current time plus the configured
                // expiration duration.
                .setExpiration(new Date(System.currentTimeMillis() + jwtExpiration))
                // Signs the token with the specified secret key and signing algorithm
                // (HMAC-SHA256).
                // This signature proves the token's authenticity and integrity.
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                // Builds the token and serializes it to a compact, URL-safe string.
                .compact();
    }

    /**
     * Validates a given JWT.
     * 
     * @param token       The JWT to validate.
     * @param userDetails The user details to validate against.
     * @return True if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        // Extracts the username from the token.
        final String username = extractUsername(token);
        // Checks if the username in the token matches the user's actual username AND if
        // the token is not expired.
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // --- Private Helper Methods ---

    /**
     * Checks if a token has expired by comparing its expiration date with the
     * current date.
     * 
     * @param token The JWT to check.
     * @return True if the token is expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * A helper method to extract the expiration date from a token's claims.
     * 
     * @param token The JWT to parse.
     * @return The expiration date.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * A generic, reusable method to extract a specific claim from the JWT payload.
     * 
     * @param token          The JWT to parse.
     * @param claimsResolver A function that specifies which claim to extract.
     * @return The extracted claim.
     */
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        // First, extract all claims from the token.
        final Claims claims = extractAllClaims(token);
        // Then, apply the provided function to get the desired claim.
        return claimsResolver.apply(claims);
    }

    /**
     * Parses the JWT to extract its payload (the "claims").
     * This method implicitly verifies the token's signature. If the signature is
     * invalid,
     * the JJWT library will throw an exception.
     * 
     * @param token The JWT to parse.
     * @return The Claims object containing the token's data.
     */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                // Sets the secret key used to verify the token's signature.
                .setSigningKey(getSigningKey())
                // Builds the parser.
                .build()
                // Parses the token string.
                .parseClaimsJws(token)
                // Retrieves the payload (body) of the token.
                .getBody();
    }

    /**
     * Prepares the secret key for use in the signing process.
     * It decodes the Base64-encoded secret from the properties file into a
     * cryptographic Key object.
     * 
     * @return The Key object used for signing and verifying tokens.
     */
    private Key getSigningKey() {
        // Decodes the Base64 string into a byte array.
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        // Creates a Key suitable for the HMAC-SHA algorithm.
        return Keys.hmacShaKeyFor(keyBytes);
    }
}