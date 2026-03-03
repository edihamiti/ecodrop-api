package utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class TokenUtils {

    /**
     * Extrait le token brut de la chaîne du Header Authorization
     * @param authHeader Le contenu du header "Authorization"
     * @return Le token string ou null s'il est mal formé
     */
    public static String extractToken(String authHeader) {
        return null;
    }

    /**
     * Décode le JWT et vérifie sa signature
     * @param token Le token extrait
     * @return Les claims (données) ou null si invalide/expiré
     */
    public static Claims decodeJWT(String token) {
            return null;
    }
}