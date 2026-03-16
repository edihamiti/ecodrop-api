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
     * - Google     : email, name
     * - Discord    : email, username
     * - GitLab     : email, username
     * - GitHub     : email, login
     * - Microsoft  : mail/userPrincipalName, displayName
     * - Facebook   : email, name
     * - LinkedIn   : email, name
     * - Twitter    : data.username (nested)
     * - Default    : essaie email/name, email/username, email/login
     */
    private static UserInfo extractUserInfo(JsonNode json, OAuthProvider provider) {
        return switch (provider) {
            case GOOGLE, SALESFORCE, ADOBE, PAYPAL, AUTH0, OKTA, KEYCLOAK ->
                new UserInfo(getField(json, "email"), getField(json, "name"));

            case DISCORD, GITLAB, GITLAB_COM, GITLAB_FRAMAGIT, GITLAB_SELF ->
                new UserInfo(getField(json, "email"), getField(json, "username"));

            case GITHUB, GITEA ->
                new UserInfo(getField(json, "email"), getField(json, "login"));

            case LINKEDIN ->
                new UserInfo(getField(json, "email"), getField(json, "name"));


            case SNAPCHAT -> {
                JsonNode me = json.has("me") ? json.get("me") : json;
                yield new UserInfo(null, getField(me, "displayName"));
            }

            case NOTION ->
                new UserInfo(getField(json, "email"), getField(json, "name"));

            case FIGMA ->
                new UserInfo(getField(json, "email"), getField(json, "handle"));

            case ZOOM ->
                new UserInfo(getField(json, "email"), getField(json, "display_name"));

            case LINE ->
                new UserInfo(null, getField(json, "displayName"));

            case KAKAO -> {
                JsonNode account = json.has("kakao_account") ? json.get("kakao_account") : json;
                JsonNode profile = account.has("profile") ? account.get("profile") : account;
                yield new UserInfo(getField(account, "email"), getField(profile, "nickname"));
            }

            case YANDEX ->
                new UserInfo(getField(json, "default_email"), getField(json, "login"));

            case VK -> {
                JsonNode response = json.has("response") && json.get("response").isArray() && !json.get("response").isEmpty()
                        ? json.get("response").get(0) : json;
                String name = getField(response, "first_name") + " " + getField(response, "last_name");
                yield new UserInfo(getField(json, "email"), name.trim());
            }

            case BAIDU ->
                new UserInfo(null, getField(json, "uname"));

            case ATLASSIAN ->
                new UserInfo(getField(json, "email"), getField(json, "name"));

            case EPIC_GAMES ->
                new UserInfo(getField(json, "email"), getField(json, "displayName"));

            case STEAM -> {
                JsonNode players = json.path("response").path("players");
                JsonNode player = players.isArray() && !players.isEmpty() ? players.get(0) : json;
                yield new UserInfo(null, getField(player, "personaname"));
            }

            case HUBSPOT ->
                new UserInfo(getField(json, "user"), getField(json, "user"));

            case MAILCHIMP ->
                new UserInfo(getField(json, "login_email"), getField(json, "accountname"));

            case SHOPIFY ->
                new UserInfo(getField(json, "email"), getField(json, "name"));

            case STRAVA ->
                new UserInfo(getField(json, "email"),
                        (getField(json, "firstname") + " " + getField(json, "lastname")).trim());

            case INSTAGRAM ->
                new UserInfo(null, getField(json, "username"));

            case WORDPRESS ->
                new UserInfo(getField(json, "email"), getField(json, "display_name"));

            case HEROKU ->
                new UserInfo(getField(json, "email"), getField(json, "name"));

            case DIGITALOCEAN -> {
                JsonNode account = json.has("account") ? json.get("account") : json;
                yield new UserInfo(getField(account, "email"), getField(account, "name"));
            }

            case BOX ->
                new UserInfo(getField(json, "login"), getField(json, "name"));

            case DAILYMOTION ->
                new UserInfo(getField(json, "email"), getField(json, "screenname"));

            case MEETUP ->
                new UserInfo(getField(json, "email"), getField(json, "name"));

            case COINBASE -> {
                JsonNode data = json.has("data") ? json.get("data") : json;
                yield new UserInfo(getField(data, "email"), getField(data, "name"));
            }

            case EBAY ->
                new UserInfo(getField(json, "email"), getField(json, "username"));

            case UBER ->
                new UserInfo(getField(json, "email"),
                        (getField(json, "first_name") + " " + getField(json, "last_name")).trim());

            case LYFT ->
                new UserInfo(null, getField(json, "display_name"));

            case FOURSQUARE -> {
                JsonNode response = json.has("response") && json.has("response") ? json.path("response").path("user") : json;
                yield new UserInfo(getField(response, "contact") != null ? getField(response, "email") : null,
                        (getField(response, "firstName") + " " + getField(response, "lastName")).trim());
            }

            case TUMBLR -> {
                JsonNode user = json.path("response").path("user");
                yield new UserInfo(null, getField(user, "name"));
            }

            case VIMEO ->
                new UserInfo(getField(json, "email"), getField(json, "name"));

            case SOUNDCLOUD ->
                new UserInfo(null, getField(json, "username"));

            case BATTLE_NET ->
                new UserInfo(null, getField(json, "battletag"));

            case PATREON -> {
                JsonNode data = json.has("data") ? json.get("data") : json;
                JsonNode attrs = data.has("attributes") ? data.get("attributes") : data;
                yield new UserInfo(getField(attrs, "email"), getField(attrs, "full_name"));
            }

            case MASTODON ->
                new UserInfo(null, getField(json, "username"));

            case WECHAT ->
                new UserInfo(null, getField(json, "nickname"));

            case NAVER -> {
                JsonNode response = json.has("response") ? json.get("response") : json;
                yield new UserInfo(getField(response, "email"), getField(response, "name"));
            }

            case FLICKR ->
                new UserInfo(null, getField(json, "username"));

            case DEVIANTART ->
                new UserInfo(null, getField(json, "username"));

            case STACKEXCHANGE -> {
                JsonNode items = json.has("items") && json.get("items").isArray() && !json.get("items").isEmpty()
                        ? json.get("items").get(0) : json;
                yield new UserInfo(null, getField(items, "display_name"));
            }

            case BASECAMP ->
                new UserInfo(getField(json, "email_address"), getField(json, "name"));

            case SPOTIFY_PODCASTERS ->
                new UserInfo(getField(json, "email"), getField(json, "display_name"));

            case ORCID ->
                new UserInfo(getField(json, "email"), getField(json, "name"));
        };
    }

    /**
     * Extrait un champ texte d'un JsonNode, retourne null si absent.
     */
    private static String getField(JsonNode json, String field) {
        return json.has(field) && !json.get(field).isNull() ? json.get(field).asText() : null;
    }
}
