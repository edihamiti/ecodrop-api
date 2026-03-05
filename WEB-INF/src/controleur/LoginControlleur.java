package controleur;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.TokenUtils;

import java.io.IOException;

@WebServlet("/login")
public class LoginControlleur extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String from = req.getParameter("from");
        String code = req.getParameter("code");

        if (from == null || code == null) {
            resp.sendRedirect(req.getContextPath() + "/index.html");
            return;
        }

        String accessToken = null;

        try {
            switch (from) {
                case "gitlab" -> accessToken = TokenUtils.exchangeCode(code,
                        "https://gitlab.univ-lille.fr/oauth/token", "gitlab");
                case "discord" -> accessToken = TokenUtils.exchangeCode(code,
                        "https://discord.com/api/oauth2/token", "discord");
                case "google" -> accessToken = TokenUtils.exchangeCode(code,
                        "https://oauth2.googleapis.com/token", "google");
                default -> {
                    resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Provider inconnu");
                    return;
                }
            }

            if (accessToken != null) {
                resp.getWriter().write("Access Token récupéré : " + accessToken);
            }

        } catch (Exception e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erreur lors de l'échange du token");
        }
    }
}
