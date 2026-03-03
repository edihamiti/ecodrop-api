package utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;

public class TokenUtils {

    private static final String SECRET = Config.get("secret");
    private static final String PREFIX = "Bearer ";

    public static String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith(PREFIX)) {
            return authHeader.substring(PREFIX.length()).trim();
        }
        return null;
    }

    public static Claims decodeJWT(String token) {
        if (token == null) return null;
        try {
            SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

            return Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token) // On vérifie la signature
                    .getPayload();            // On récupère les données
        } catch (Exception e) {
            return null;
        }
    }
}