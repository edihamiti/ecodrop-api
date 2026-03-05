package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

public class TokenUtils {

    private static final String SECRET = null;
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

    public static String exchangeCode(String code, String tokenUrl, String provider) throws Exception {
        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();

        // 1. Préparation des paramètres
        Map<String, String> params = Map.of(
                "grant_type", "authorization_code",
                "code", code,
                "redirect_uri", Config.get("redirect_uri") + "?from=" + provider,
                "client_id", Config.get(provider + ".client_id"),
                "client_secret", Config.get(provider + ".client_secret")
        );

        // 2. ENCODAGE CRUCIAL des paramètres (C'est ça qui corrige tes erreurs)
        String form = params.entrySet().stream()
                .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "=" +
                        URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        // 3. Envoi de la requête
        java.net.http.HttpRequest request = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(tokenUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(java.net.http.HttpRequest.BodyPublishers.ofString(form))
                .build();

        System.out.println(request);

        java.net.http.HttpResponse<String> response = client.send(request,
                java.net.http.HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            // --- PARSING JACKSON ICI ---
            ObjectMapper mapper = new ObjectMapper();

            // On lit le corps de la réponse comme un arbre JSON
            JsonNode rootNode = mapper.readTree(response.body());

            // On récupère le champ "access_token"
            String accessTokenNode = rootNode.get("access_token").asText();

            if (accessTokenNode != null) {
                if (verifyTokenWithProvider(accessTokenNode, provider) != null) {
                    return accessTokenNode;
                } else {
                    System.err.println("Tentative de connexion avec un faux token (" + provider + ")");
                    return null;
                }
            } else {
                System.err.println("Le champ access_token est absent de la réponse : " + response.body());
                return null;
            }
        } else {
            System.err.println("Erreur OAuth : " + response.body());
            return null;
        }
    }

    public static JsonNode verifyTokenWithProvider(String token, String provider) throws Exception {
        // 1. On choisit l'URL de vérification selon le provider
        String verifyUrl = switch (provider) {
            case "google" -> "https://oauth2.googleapis.com/tokeninfo?id_token=" + token;
            case "discord" -> "https://discord.com/api/users/@me";
            case "gitlab" -> "https://gitlab.univ-lille.fr/api/v4/user";
            default -> throw new IllegalArgumentException("Provider inconnu");
        };

        java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
        java.net.http.HttpRequest.Builder requestBuilder = java.net.http.HttpRequest.newBuilder()
                .uri(java.net.URI.create(verifyUrl));

        // Pour Discord et GitLab, le token se passe dans le Header
        if (!provider.equals("google")) {
            requestBuilder.header("Authorization", "Bearer " + token);
        }

        java.net.http.HttpResponse<String> response = client.send(
                requestBuilder.GET().build(),
                java.net.http.HttpResponse.BodyHandlers.ofString()
        );

        // 2. Si le statut est 200, le token est AUTHENTIQUE et VALIDE
        if (response.statusCode() == 200) {
            return new ObjectMapper().readTree(response.body());
        } else {
            return null;
        }
    }

}