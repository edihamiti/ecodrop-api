package controleur;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Base64;

@WebServlet("/auth/token")
public class tokenControlleur extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String auth = req.getHeader("Authorization");
        if (auth.startsWith("Basic")){
            auth = auth.substring(5).trim();
            auth = new String(Base64.getDecoder().decode(auth));
            String login = auth.split(":")[0];
            String password = auth.split(":")[1];
            // requeste bdd
        }
    }
}
