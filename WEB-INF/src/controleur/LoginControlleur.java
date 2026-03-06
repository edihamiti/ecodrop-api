package controleur;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.TokenUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/login")
public class LoginControlleur extends HttpServlet {

    // On réutilise l'ObjectMapper (statique pour la performance)
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String from = req.getParameter("from");
        String code = req.getParameter("code");

        // 1. Headers standards pour une API REST
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (from == null || code == null) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Paramètres 'from' ou 'code' manquants");
            return;
        }

        try {
            // 2. Détermination de l'URL du provider
            String tokenUrl = switch (from.toLowerCase()) {
                case "gitlab" -> "https://gitlab.univ-lille.fr/oauth/token";
                case "discord" -> "https://discord.com/api/oauth2/token";
                case "google" -> "https://oauth2.googleapis.com/token";
                default -> null;
            };

            if (tokenUrl == null) {
                sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Provider inconnu : " + from);
                return;
            }

            String internalToken = TokenUtils.createAppTokenFromProvider(code, from, tokenUrl);

            // 4. Envoi de la réponse JSON réussie
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", internalToken);

            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), responseData);

        } catch (Exception e) {
            e.printStackTrace();
            // 5. Envoi d'une erreur propre en JSON
            sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Authentification échouée : " + e.getMessage());
        }
    }

    /**
     * Méthode utilitaire pour renvoyer des erreurs formatées en JSON
     */
    private void sendError(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        Map<String, Object> errorData = new HashMap<>();
        errorData.put("success", false);
        errorData.put("error", message);
        mapper.writeValue(resp.getWriter(), errorData);
    }
}