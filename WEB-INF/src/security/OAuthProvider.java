package security;

import jakarta.servlet.http.HttpServletRequest;
import utils.Config;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Enum centralisant la configuration des providers OAuth supportés.
 * Chaque provider expose ses URLs, ses scopes et son label d'affichage.
 */
public enum OAuthProvider {

    GITLAB("gitlab",
            "GitLab de l'université",
            "bi-gitlab", "#fc6d26",
            "https://gitlab.univ-lille.fr/oauth/authorize",
            "https://gitlab.univ-lille.fr/oauth/token",
            "https://gitlab.univ-lille.fr/api/v4/user",
            "read_user"),

    DISCORD("discord",
            "Discord",
            "bi-discord", "#5865F2",
            "https://discord.com/oauth2/authorize",
            "https://discord.com/api/oauth2/token",
            "https://discord.com/api/users/@me",
            "identify+email"),

    GOOGLE("google",
            "Google",
            "bi-google", "#ea4335",
            "https://accounts.google.com/o/oauth2/v2/auth",
            "https://oauth2.googleapis.com/token",
            "https://www.googleapis.com/oauth2/v3/userinfo",
            "openid email profile"),

    GITHUB("github",
           "GitHub",
           "bi-github", "#181717",
           "https://github.com/login/oauth/authorize",
           "https://github.com/login/oauth/access_token",
           "https://api.github.com/user",
           "user:email");

    private final String name;
    private final String displayName;
    private final String icon;
    private final String color;
    private final String authorizeUrl;
    private final String tokenUrl;
    private final String userInfoUrl;
    private final String scopes;

    OAuthProvider(String name, String displayName, String icon, String color, String authorizeUrl, String tokenUrl, String userInfoUrl, String scopes) {
        this.name = name;
        this.displayName = displayName;
        this.icon = icon;
        this.color = color;
        this.authorizeUrl = authorizeUrl;
        this.tokenUrl = tokenUrl;
        this.userInfoUrl = userInfoUrl;
        this.scopes = scopes;
    }

    public String getName() {
        return name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getIcon() {
        return icon;
    }

    public String getColor() {
        return color;
    }

    public String getAuthorizeUrl() {
        return authorizeUrl;
    }

    public String getTokenUrl() {
        return tokenUrl;
    }

    public String getUserInfoUrl() {
        return userInfoUrl;
    }

    public String getScopes() {
        return scopes;
    }

    /**
     * Construit l'URI de redirection pour ce provider.
     */
    public static String getRedirectUri(HttpServletRequest request, String providerName) {
        int port = request.getServerPort();
        String domain = request.getScheme() + "://" + request.getServerName()
                + ((port == 80 || port == 443) ? "" : ":" + port);
        System.out.println(domain);
        System.out.println(request.getScheme().equals("https")? domain : domain + "/ecodrop");
        return URLEncoder.encode(((port == 80 || port == 443)? domain.replace("http", "https") : domain + "/ecodrop") + Config.get("redirect_uri"), StandardCharsets.UTF_8) + "?from=" + providerName;
    }

    /**
     * Construit l'URL complète d'autorisation OAuth pour ce provider.
     * @return l'URL prête à être utilisée dans un lien href
     */
    public String buildAuthorizeUrl(HttpServletRequest request) {
        String redirectUri = getRedirectUri(request, name);

        return authorizeUrl
                + "?client_id=" + Config.get(name + ".client_id")
                + "&redirect_uri=" + redirectUri
                + "&scope=" + URLEncoder.encode(scopes, StandardCharsets.UTF_8)
                + "&response_type=code";
    }

    /**
     * Résout un provider à partir de son nom (case-insensitive).
     * @param name le nom du provider (ex: "google", "discord", "gitlab")
     * @return le provider correspondant
     * @throws IllegalArgumentException si le provider est inconnu
     */
    public static OAuthProvider fromName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Le nom du provider ne peut pas être null");
        }
        for (OAuthProvider provider : values()) {
            if (provider.name.equalsIgnoreCase(name)) {
                return provider;
            }
        }
        throw new IllegalArgumentException("Provider OAuth inconnu : " + name);
    }
}

