package controleur;

import com.fasterxml.jackson.databind.ObjectMapper;
import dto.UserInfo;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import security.OAuthProvider;
import utils.JwtUtils;
import utils.OAuthClient;

import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/auth/token")
public class tokenControlleur extends HttpServlet {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String from = req.getParameter("from");
        String code = req.getParameter("code");

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        if (from == null || code == null) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Paramètres 'from' ou 'code' manquants");
            return;
        }

        try {
            // 1. Résolution du provider OAuth
            OAuthProvider provider = OAuthProvider.fromName(from);

            // 2. Échange du code d'autorisation contre un access token
            String accessToken = OAuthClient.exchangeCode(req, code, provider);

            // 3. Récupération des informations utilisateur (normalisées)
            UserInfo userInfo = OAuthClient.getUserInfo(accessToken, provider);

            // 4. Création du token JWT interne (contient email, pseudo)
            String internalToken = JwtUtils.createToken(userInfo);

            // 5. Envoi de la réponse
            Map<String, Object> responseData = new HashMap<>();
            responseData.put("token", internalToken);

            resp.setStatus(HttpServletResponse.SC_OK);
            mapper.writeValue(resp.getWriter(), responseData);

        } catch (IllegalArgumentException e) {
            sendError(resp, HttpServletResponse.SC_BAD_REQUEST, "Provider inconnu : " + from);
        } catch (Exception e) {
            e.printStackTrace();
            sendError(resp, HttpServletResponse.SC_UNAUTHORIZED, "Authentification échouée : " + e.getMessage());
        }
    }

    private void sendError(HttpServletResponse resp, int status, String message) throws IOException {
        resp.setStatus(status);
        Map<String, Object> errorData = new HashMap<>();
        errorData.put("success", false);
        errorData.put("error", message);
        mapper.writeValue(resp.getWriter(), errorData);
    }
}
