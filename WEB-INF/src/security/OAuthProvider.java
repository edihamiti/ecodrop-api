package security;

import utils.Config;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Enum centralisant la configuration des providers OAuth supportés.
 * Chaque provider expose ses URLs, ses scopes et son label d'affichage.
 */
public enum OAuthProvider {

    GITLAB("gitlab",
            "GitLab",
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
           "user:email"),

    LINKEDIN("linkedin",
             "LinkedIn",
             "bi-linkedin", "#0a66c2",
             "https://www.linkedin.com/oauth/v2/authorization",
             "https://www.linkedin.com/oauth/v2/accessToken",
             "https://api.linkedin.com/v2/userinfo",
             "openid profile email"),

    SNAPCHAT("snapchat",
             "Snapchat",
             "bi-snapchat", "#fffc00",
             "https://accounts.snapchat.com/login/oauth2/authorize",
             "https://accounts.snapchat.com/login/oauth2/access_token",
             "https://kit.snapchat.com/v1/me",
             "https://auth.snapchat.com/oauth2/api/user.display_name https://auth.snapchat.com/oauth2/api/user.bitmoji.avatar"),

    NOTION("notion",
           "Notion",
           "bi-journal-text", "#000000",
           "https://api.notion.com/v1/oauth/authorize",
           "https://api.notion.com/v1/oauth/token",
           "https://api.notion.com/v1/users/me",
           ""),

    FIGMA("figma",
          "Figma",
          "bi-vector-pen", "#f24e1e",
          "https://www.figma.com/oauth",
          "https://www.figma.com/api/oauth/token",
          "https://api.figma.com/v1/me",
          "file_read"),

    ZOOM("zoom",
         "Zoom",
         "bi-camera-video-fill", "#2d8cff",
         "https://zoom.us/oauth/authorize",
         "https://zoom.us/oauth/token",
         "https://api.zoom.us/v2/users/me",
         "user:read:user"),

    SALESFORCE("salesforce",
               "Salesforce",
               "bi-cloud-fill", "#00a1e0",
               "https://login.salesforce.com/services/oauth2/authorize",
               "https://login.salesforce.com/services/oauth2/token",
               "https://login.salesforce.com/services/oauth2/userinfo",
               "openid email profile"),

    LINE("line",
         "LINE",
         "bi-chat-fill", "#00c300",
         "https://access.line.me/oauth2/v2.1/authorize",
         "https://api.line.me/oauth2/v2.1/token",
         "https://api.line.me/v2/profile",
         "profile openid email"),

    KAKAO("kakao",
          "Kakao",
          "bi-chat-dots-fill", "#fee500",
          "https://kauth.kakao.com/oauth/authorize",
          "https://kauth.kakao.com/oauth/token",
          "https://kapi.kakao.com/v2/user/me",
          "profile_nickname account_email"),

    YANDEX("yandex",
           "Yandex",
           "bi-search", "#ff0000",
           "https://oauth.yandex.com/authorize",
           "https://oauth.yandex.com/token",
           "https://login.yandex.ru/info",
           "login:email login:info"),

    VK("vk",
       "VK",
       "bi-chat-square-fill", "#4680c2",
       "https://oauth.vk.com/authorize",
       "https://oauth.vk.com/access_token",
       "https://api.vk.com/method/users.get?fields=photo_200,screen_name&v=5.131",
       "email"),

    BAIDU("baidu",
          "Baidu",
          "bi-globe2", "#2529d8",
          "https://openapi.baidu.com/oauth/2.0/authorize",
          "https://openapi.baidu.com/oauth/2.0/token",
          "https://openapi.baidu.com/rest/2.0/passport/users/getInfo",
          "basic"),

    ATLASSIAN("atlassian",
              "Atlassian",
              "bi-kanban-fill", "#0052cc",
              "https://auth.atlassian.com/authorize",
              "https://auth.atlassian.com/oauth/token",
              "https://api.atlassian.com/me",
              "read:me read:account"),

    ADOBE("adobe",
          "Adobe",
          "bi-brush-fill", "#ff0000",
          "https://ims-na1.adobelogin.com/ims/authorize/v2",
          "https://ims-na1.adobelogin.com/ims/token/v3",
          "https://ims-na1.adobelogin.com/ims/userinfo/v2",
          "openid email profile"),

    EPIC_GAMES("epicgames",
               "Epic Games",
               "bi-controller", "#2f2d2e",
               "https://www.epicgames.com/id/authorize",
               "https://account-public-service-prod.ol.epicgames.com/account/api/oauth/token",
               "https://account-public-service-prod.ol.epicgames.com/account/api/public/account",
               "basic_profile"),

    STEAM("steam",
          "Steam",
          "bi-steam", "#171a21",
          "https://steamcommunity.com/openid/login",
          "https://steamcommunity.com/openid/login",
          "https://api.steampowered.com/ISteamUser/GetPlayerSummaries/v0002/",
          ""),

    GITLAB_COM("gitlabcom",
               "GitLab.com",
               "bi-gitlab", "#fc6d26",
               "https://gitlab.com/oauth/authorize",
               "https://gitlab.com/oauth/token",
               "https://gitlab.com/api/v4/user",
               "read_user"),

    HUBSPOT("hubspot",
            "HubSpot",
            "bi-bullseye", "#ff7a59",
            "https://app.hubspot.com/oauth/authorize",
            "https://api.hubapi.com/oauth/v1/token",
            "https://api.hubapi.com/oauth/v1/access-tokens/",
            "oauth"),

    MAILCHIMP("mailchimp",
              "Mailchimp",
              "bi-envelope-at-fill", "#ffe01b",
              "https://login.mailchimp.com/oauth2/authorize",
              "https://login.mailchimp.com/oauth2/token",
              "https://login.mailchimp.com/oauth2/metadata",
              ""),

    SHOPIFY("shopify",
            "Shopify",
            "bi-shop", "#96bf48",
            "https://{shop}.myshopify.com/admin/oauth/authorize",
            "https://{shop}.myshopify.com/admin/oauth/access_token",
            "https://{shop}.myshopify.com/admin/api/2024-01/shop.json",
            "read_products"),

    STRAVA("strava",
           "Strava",
           "bi-bicycle", "#fc4c02",
           "https://www.strava.com/oauth/authorize",
           "https://www.strava.com/oauth/token",
           "https://www.strava.com/api/v3/athlete",
           "read"),

    INSTAGRAM("instagram",
              "Instagram",
              "bi-instagram", "#e4405f",
              "https://api.instagram.com/oauth/authorize",
              "https://api.instagram.com/oauth/access_token",
              "https://graph.instagram.com/me?fields=id,username",
              "user_profile"),

    WORDPRESS("wordpress",
              "WordPress",
              "bi-wordpress", "#21759b",
              "https://public-api.wordpress.com/oauth2/authorize",
              "https://public-api.wordpress.com/oauth2/token",
              "https://public-api.wordpress.com/rest/v1/me",
              "auth"),

    HEROKU("heroku",
           "Heroku",
           "bi-cloud-arrow-up-fill", "#430098",
           "https://id.heroku.com/oauth/authorize",
           "https://id.heroku.com/oauth/token",
           "https://api.heroku.com/account",
           "identity"),

    DIGITALOCEAN("digitalocean",
                 "DigitalOcean",
                 "bi-water", "#0080ff",
                 "https://cloud.digitalocean.com/v1/oauth/authorize",
                 "https://cloud.digitalocean.com/v1/oauth/token",
                 "https://api.digitalocean.com/v2/account",
                 "read"),

    BOX("box",
        "Box",
        "bi-box-fill", "#0061d5",
        "https://account.box.com/api/oauth2/authorize",
        "https://api.box.com/oauth2/token",
        "https://api.box.com/2.0/users/me",
        "root_readwrite"),

    DAILYMOTION("dailymotion",
                "Dailymotion",
                "bi-play-btn-fill", "#0066dc",
                "https://www.dailymotion.com/oauth/authorize",
                "https://api.dailymotion.com/oauth/token",
                "https://api.dailymotion.com/me?fields=id,screenname,email",
                "email userinfo"),

    MEETUP("meetup",
           "Meetup",
           "bi-people-fill", "#ed1c40",
           "https://secure.meetup.com/oauth2/authorize",
           "https://secure.meetup.com/oauth2/access",
           "https://api.meetup.com/members/self",
           "basic"),

    GITLAB_SELF("gitlabself",
                "GitLab Self-Hosted",
                "bi-gitlab", "#fc6d26",
                "https://gitlab.example.com/oauth/authorize",
                "https://gitlab.example.com/oauth/token",
                "https://gitlab.example.com/api/v4/user",
                "read_user"),

    OKTA("okta",
         "Okta",
         "bi-shield-lock-fill", "#007dc1",
         "https://{domain}.okta.com/oauth2/v1/authorize",
         "https://{domain}.okta.com/oauth2/v1/token",
         "https://{domain}.okta.com/oauth2/v1/userinfo",
         "openid email profile"),

    AUTH0("auth0",
          "Auth0",
          "bi-shield-check", "#eb5424",
          "https://{domain}.auth0.com/authorize",
          "https://{domain}.auth0.com/oauth/token",
          "https://{domain}.auth0.com/userinfo",
          "openid email profile"),

    KEYCLOAK("keycloak",
             "Keycloak",
             "bi-key-fill", "#4d4d4d",
             "https://{domain}/realms/{realm}/protocol/openid-connect/auth",
             "https://{domain}/realms/{realm}/protocol/openid-connect/token",
             "https://{domain}/realms/{realm}/protocol/openid-connect/userinfo",
             "openid email profile"),

    COINBASE("coinbase",
             "Coinbase",
             "bi-currency-bitcoin", "#0052ff",
             "https://www.coinbase.com/oauth/authorize",
             "https://api.coinbase.com/oauth/token",
             "https://api.coinbase.com/v2/user",
             "wallet:user:read wallet:user:email"),

    EBAY("ebay",
         "eBay",
         "bi-cart-fill", "#e53238",
         "https://auth.ebay.com/oauth2/authorize",
         "https://api.ebay.com/identity/v1/oauth2/token",
         "https://apiz.ebay.com/commerce/identity/v1/user/",
         "https://api.ebay.com/oauth/api_scope/commerce.identity.readonly"),

    UBER("uber",
         "Uber",
         "bi-car-front-fill", "#000000",
         "https://login.uber.com/oauth/v2/authorize",
         "https://login.uber.com/oauth/v2/token",
         "https://api.uber.com/v1.2/me",
         "profile email"),

    LYFT("lyft",
         "Lyft",
         "bi-car-front", "#ff00bf",
         "https://api.lyft.com/oauth/authorize",
         "https://api.lyft.com/oauth/token",
         "https://api.lyft.com/v1/profile",
         "public profile"),

    FOURSQUARE("foursquare",
               "Foursquare",
               "bi-geo-alt-fill", "#f94877",
               "https://foursquare.com/oauth2/authenticate",
               "https://foursquare.com/oauth2/access_token",
               "https://api.foursquare.com/v2/users/self",
               ""),

    TUMBLR("tumblr",
           "Tumblr",
           "bi-file-text-fill", "#35465c",
           "https://www.tumblr.com/oauth2/authorize",
           "https://api.tumblr.com/v2/oauth2/token",
           "https://api.tumblr.com/v2/user/info",
           "basic"),

    VIMEO("vimeo",
          "Vimeo",
          "bi-camera-reels-fill", "#1ab7ea",
          "https://api.vimeo.com/oauth/authorize",
          "https://api.vimeo.com/oauth/access_token",
          "https://api.vimeo.com/me",
          "public private"),

    SOUNDCLOUD("soundcloud",
               "SoundCloud",
               "bi-soundwave", "#ff5500",
               "https://api.soundcloud.com/connect",
               "https://api.soundcloud.com/oauth2/token",
               "https://api.soundcloud.com/me",
               "non-expiring"),

    BATTLE_NET("battlenet",
               "Battle.net",
               "bi-joystick", "#148eff",
               "https://oauth.battle.net/authorize",
               "https://oauth.battle.net/token",
               "https://oauth.battle.net/userinfo",
               "openid"),

    PATREON("patreon",
            "Patreon",
            "bi-heart-fill", "#ff424d",
            "https://www.patreon.com/oauth2/authorize",
            "https://www.patreon.com/api/oauth2/token",
            "https://www.patreon.com/api/oauth2/v2/identity?fields%5Buser%5D=email,full_name",
            "identity identity[email]"),

    MASTODON("mastodon",
             "Mastodon",
             "bi-mastodon", "#6364ff",
             "https://mastodon.social/oauth/authorize",
             "https://mastodon.social/oauth/token",
             "https://mastodon.social/api/v1/accounts/verify_credentials",
             "read:accounts"),

    GITLAB_FRAMAGIT("framagit",
                    "FramaGit",
                    "bi-gitlab", "#fc6d26",
                    "https://framagit.org/oauth/authorize",
                    "https://framagit.org/oauth/token",
                    "https://framagit.org/api/v4/user",
                    "read_user"),

    GITEA("gitea",
          "Gitea",
          "bi-git", "#609926",
          "https://gitea.com/login/oauth/authorize",
          "https://gitea.com/login/oauth/access_token",
          "https://gitea.com/api/v1/user",
          ""),

    WECHAT("wechat",
           "WeChat",
           "bi-wechat", "#07c160",
           "https://open.weixin.qq.com/connect/qrconnect",
           "https://api.weixin.qq.com/sns/oauth2/access_token",
           "https://api.weixin.qq.com/sns/userinfo",
           "snsapi_login"),

    NAVER("naver",
          "Naver",
          "bi-n-square-fill", "#03c75a",
          "https://nid.naver.com/oauth2.0/authorize",
          "https://nid.naver.com/oauth2.0/token",
          "https://openapi.naver.com/v1/nid/me",
          ""),

    FLICKR("flickr",
           "Flickr",
           "bi-image-fill", "#0063dc",
           "https://www.flickr.com/services/oauth/authorize",
           "https://www.flickr.com/services/oauth/access_token",
           "https://www.flickr.com/services/rest/?method=flickr.people.getInfo&format=json",
           "read"),

    DEVIANTART("deviantart",
               "DeviantArt",
               "bi-palette-fill", "#05cc47",
               "https://www.deviantart.com/oauth2/authorize",
               "https://www.deviantart.com/oauth2/token",
               "https://www.deviantart.com/api/v1/oauth2/user/whoami",
               "user"),

    STACKEXCHANGE("stackexchange",
                  "Stack Overflow",
                  "bi-stack-overflow", "#f48024",
                  "https://stackoverflow.com/oauth",
                  "https://stackoverflow.com/oauth/access_token/json",
                  "https://api.stackexchange.com/2.3/me?site=stackoverflow",
                  ""),

    BASECAMP("basecamp",
             "Basecamp",
             "bi-building-fill", "#1d2d35",
             "https://launchpad.37signals.com/authorization/new",
             "https://launchpad.37signals.com/authorization/token",
             "https://launchpad.37signals.com/authorization.json",
             ""),

    SPOTIFY_PODCASTERS("spotifypodcasters",
                       "Spotify for Podcasters",
                       "bi-mic-fill", "#1db954",
                       "https://accounts.spotify.com/authorize",
                       "https://accounts.spotify.com/api/token",
                       "https://api.spotify.com/v1/me",
                       "user-read-email"),

    ORCID("orcid",
          "ORCID",
          "bi-person-badge-fill", "#a6ce39",
          "https://orcid.org/oauth/authorize",
          "https://orcid.org/oauth/token",
          "https://pub.orcid.org/v3.0/me",
          "openid /authenticate");

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
     * Construit l'URL complète d'autorisation OAuth pour ce provider.
     * @return l'URL prête à être utilisée dans un lien href
     */
    public String buildAuthorizeUrl() {
        String redirectUri = URLEncoder.encode(Config.get("redirect_uri"), StandardCharsets.UTF_8) + "%3Ffrom%3D" + name;

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

