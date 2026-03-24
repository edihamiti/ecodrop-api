package security;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Filtre limitant le nombre de requêtes par IP pour prévenir les abus (DDoS, Spam).
 * Utilise uniquement les classes standards du JDK (ConcurrentHashMap, AtomicInteger).
 */
@WebFilter("/*")
public class RateLimitFilter implements Filter {

    // Stockage en mémoire : IP -> Infos (Timestamp de début de fenêtre, Compteur)
    private static final Map<String, RateInfo> clients = new ConcurrentHashMap<>();

    // CONFIGURATION : 100 requêtes par minute (ajustable selon les besoins)
    private static final int MAX_REQUESTS = 100;
    private static final long WINDOW_MS = 60_000; // Fenêtre de 1 minute

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {}

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        String ip = request.getRemoteAddr();
        long now = System.currentTimeMillis();

        // Calcul atomique du compteur pour l'IP donnée
        RateInfo info = clients.compute(ip, (k, v) -> {
            if (v == null || (now - v.startTime) > WINDOW_MS) {
                // Nouvelle fenêtre si l'IP est inconnue ou si le temps est écoulé
                return new RateInfo(now, new AtomicInteger(1));
            }
            // Sinon on incrémente le compteur existant
            v.count.incrementAndGet();
            return v;
        });

        // Vérification de la limite de débit
        if (info.count.get() > MAX_REQUESTS) {
            HttpServletResponse res = (HttpServletResponse) response;
            res.setStatus(429); // Code HTTP 429 "Too Many Requests"
            res.setContentType("application/json");
            res.setCharacterEncoding("UTF-8");
            res.getWriter().write("{error: Trop de requêtes. Limite de " + MAX_REQUESTS + " par minute atteinte.}");
            return; // Bloque la requête ici
        }

        // Si la limite n'est pas atteinte, on passe au filtre suivant (SecurityFilter)
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        clients.clear();
    }

    /**
     * Classe interne pour stocker l'état du débit d'un client.
     */
    private static class RateInfo {
        final long startTime;
        final AtomicInteger count;

        RateInfo(long startTime, AtomicInteger count) {
            this.startTime = startTime;
            this.count = count;
        }
    }
}
