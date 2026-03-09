package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.UserInfo;
import security.OAuthProvider;

import javax.swing.*;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Client HTTP pour les interactions OAuth avec les providers externes.
 * Responsabilités : échange du code d'autorisation et récupération des infos utilisateur.
 */
public class OAuthClient {
    // Instances partagées (thread-safe)
    private static final HttpClient HTTP_CLIENT = HttpClient.newHttpClient();
    private static final ObjectMapper MAPPER = new ObjectMapper();


    /**
     * Échange un code d'autorisation contre un access token auprès du provider.
     *
     * @param code     le code d'autorisation reçu du callback OAuth
     * @param provider le provider OAuth
     * @return l'access token du provider
     * @throws Exception si l'échange échoue
     */
    public static String exchangeCode(String code, OAuthProvider provider) throws Exception {
        Map<String, String> params = Map.of(
                "grant_type", "authorization_code",
                "code", code,
                "redirect_uri", Config.get("redirect_uri") + "?from=" + provider.getName(),
                "client_id", Config.get(provider.getName() + ".client_id"),
                "client_secret", Config.get(provider.getName() + ".client_secret")
        );

        String form = params.entrySet().stream()
                .map(e -> URLEncoder.encode(e.getKey(), StandardCharsets.UTF_8) + "="
                        + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(provider.getTokenUrl()))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(form))
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Erreur OAuth (" + response.statusCode() + ") : " + response.body());
        }

        JsonNode root = MAPPER.readTree(response.body());
        JsonNode accessTokenNode = root.get("access_token");

        if (accessTokenNode == null || accessTokenNode.isNull()) {
            throw new RuntimeException("Le champ access_token est absent de la réponse : " + response.body());
        }

        return accessTokenNode.asText();
    }

    /**
     * Récupère les informations de l'utilisateur auprès du provider et les normalise.
     *
     * @param accessToken l'access token obtenu via {@link #exchangeCode}
     * @param provider    le provider OAuth
     * @return un {@link UserInfo} normalisé (email, pseudo, picture)
     * @throws Exception si la requête échoue ou si le token est invalide
     */
    public static UserInfo getUserInfo(String accessToken, OAuthProvider provider) throws Exception {
        if (accessToken == null || accessToken.isEmpty()) {
            throw new IllegalArgumentException("Access token null ou vide pour " + provider.getName());
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(provider.getUserInfoUrl()))
                .header("Authorization", "Bearer " + accessToken)
                .GET()
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new RuntimeException("Erreur UserInfo (" + response.statusCode() + ") : " + response.body());
        }

        JsonNode json = MAPPER.readTree(response.body());
        return extractUserInfo(json, provider);
    }

    /**
     * Normalise le JSON brut d'un provider en un {@link UserInfo} commun.
     *
     * Mapping par provider :
     * - Google  : email, name
     * - Discord : email, username
     * - GitLab  : email, username
     */
    private static UserInfo extractUserInfo(JsonNode json, OAuthProvider provider) {
        return switch (provider) {
            case GOOGLE -> new UserInfo(
                    getField(json, "email"),
                    getField(json, "name")
            );
            case DISCORD -> new UserInfo(
                    getField(json, "email"),
                    getField(json, "username")
            );
            case GITLAB -> new UserInfo(
                    getField(json, "email"),
                    getField(json, "username")
            );
            case GITHUB -> new UserInfo(
                    getField(json, "email"),
                    getField(json, "username")
            );
        };
    }

    /**
     * Extrait un champ texte d'un JsonNode, retourne null si absent.
     */
    private static String getField(JsonNode json, String field) {
        return json.has(field) && !json.get(field).isNull() ? json.get(field).asText() : null;
    }
}
