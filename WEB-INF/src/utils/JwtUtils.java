package utils;

import dao.JDBCUserDAO;
import dao.UserDAO;
import dto.User;
import dto.UserInfo;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 * Utilitaire focalisé sur la gestion des tokens JWT internes à l'application.
 */
public class JwtUtils {
    private static final String SECRET = Config.get("api.client_secret");
    private static final String PREFIX = "Bearer ";
    private static final long EXPIRATION_MS = 2_592_000_000L * 12; // 1 an en millisecondes (Pour que bruno fonctionne le jour du rendu)
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));
    private  static final UserDAO userDAO = new JDBCUserDAO();

    public static String extractToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith(PREFIX)) {
            return authHeader.substring(PREFIX.length()).trim();
        }
        return null;
    }

    public static Claims decodeJWT(String token) {
        if (token == null) return null;
        try {
            return Jwts.parser()
                    .verifyWith(KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Crée un token JWT interne contenant les infos utilisateur normalisées.
     *
     * @param userInfo les infos utilisateur (email, pseudo)
     * @return le token JWT signé
     */
    public static String createToken(UserInfo userInfo) {
        String login = userInfo.email();
        User user = userDAO.findByLogin(login);
        if (user == null){
            user = userDAO.save(login);
        }

        if (user == null) {
            throw new RuntimeException("Impossible de créer ou récupérer l'utilisateur dans la base de données");
        }

        return Jwts.builder()
                .subject(userInfo.email())
                .claim("id", user.getId())
                .claim("email", login)
                .claim("pseudo", userInfo.pseudo())
                .claim("role", user.getRole())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_MS))
                .signWith(KEY)
                .compact();
    }
}
