package security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.JwtUtils;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/*")
public class SecurityFilter implements Filter {

    // On définit les routes qui ne demandent PAS de token
    private static final List<String> PUBLIC_URLS = Arrays.asList(
            "/index.jsp",
            "/auth/token",
            "/res/style.css"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        // L'URL est /EcoDrop/login, getServletPath() renverra juste "/login"
        String servletPath = req.getServletPath();

        // Vérification : est-ce que le chemin actuel commence par une URL publique ?
        boolean isPublic = PUBLIC_URLS.stream().anyMatch(servletPath::startsWith);

        if (isPublic) {
            // C'est public, on laisse passer sans regarder le header !
            chain.doFilter(request, response);
            return;
        }

        // --- Logique de sécurité pour tout le reste ---
        String authHeader = req.getHeader("Authorization");
        String token = JwtUtils.extractToken(authHeader);
        Claims claims = JwtUtils.decodeJWT(token);
        if (claims == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalide ou absent");
            return;
        }

        // Si on arrive ici, le token est là, on peut continuer
        chain.doFilter(request, response);
    }
}