package security;

import io.jsonwebtoken.Claims;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.JwtUtils;

import java.io.IOException;

/**
 * Filtre de sécurité pour protéger les endpoints de l'application.
 * 
 * Règles :
 * - Tous les GET sont publics (sauf /points/overloaded)
 * - Modification (POST, PUT, PATCH, DELETE) nécessite un Token
 * - DELETE et GET /points/overloaded : ADMIN uniquement
 * - POST /deposits : USER ou ADMIN
 * - Autres modifications : ADMIN par défaut
 */
@WebFilter("/*")
public class SecurityFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        String servletPath = req.getServletPath();
        String pathInfo = req.getPathInfo();
        String path = servletPath + (pathInfo == null ? "" : pathInfo);
        String method = req.getMethod();

        // 1. Accès Public : Tous les endpoints en GET sont publics ne nécessitent aucune protection (consultation libre)
        // Exception : GET /points/overloaded nécessite d'être ADMIN
        if ("GET".equalsIgnoreCase(method) && !"/points/overloaded".equals(path)) {
            chain.doFilter(request, response);
            return;
        }

        // 2. Accès Protégé (Token) : Toutes les opérations de modification (POST, PUT, PATCH, DELETE)
        // ainsi que GET /points/overloaded nécessitent obligatoirement la présence du token valide
        String authHeader = req.getHeader("Authorization");
        String token = JwtUtils.extractToken(authHeader);
        Claims claims = JwtUtils.decodeJWT(token);

        if (claims == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token invalide ou absent");
            return;
        }

        String role = (String) claims.get("role");
        if (role == null) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Rôle non trouvé dans le token");
            return;
        }

        // 3. Droits d'administration (Rôles)

        // L'ADMIN est le seul autorisé à utiliser les méthodes DELETE 
        // ainsi que l'accès aux statistiques de surcharge (GET /points/overloaded)
        if ("DELETE".equalsIgnoreCase(method) || ("/points/overloaded".equals(path) && "GET".equalsIgnoreCase(method))) {
            if (!"ADMIN".equals(role)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé aux administrateurs");
                return;
            }
        }
        
        // Le rôle USER peut effectuer des dépôts (POST /deposits)
        // ADMIN est également autorisé (héritage implicite des droits USER)
        else if ("POST".equalsIgnoreCase(method) && "/deposits".equals(path)) {
            if (!"USER".equals(role) && !"ADMIN".equals(role)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé aux utilisateurs");
                return;
            }
        }

        // Autres opérations de modification (PUT, PATCH, POST sur d'autres routes)
        // Nécessitent ADMIN par défaut selon l'énoncé
        else if (!"GET".equalsIgnoreCase(method)) {
            if (!"ADMIN".equals(role)) {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Accès réservé aux administrateurs");
                return;
            }
        }

        // Si on arrive ici, le token est valide et l'utilisateur a les droits
        chain.doFilter(request, response);
    }
}
