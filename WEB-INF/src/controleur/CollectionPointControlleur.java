package controleur;

import dao.CollectionPointDAO;
import dao.JDBCCollectionPointDAO;
import dto.CollectionPoint;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.SerializationUtils;

import java.io.IOException;
import java.util.Collection;

@WebServlet("/points/*")
public class CollectionPointControlleur extends HttpServlet {
    private final CollectionPointDAO collectionPointDAO = new JDBCCollectionPointDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String info = req.getPathInfo();
        if (info == null || info.equals("/")) {
            Collection<CollectionPoint> l = collectionPointDAO.findAll();
            if (l == null) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            if (l.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            }
            SerializationUtils.sendResponse(resp, req, l, HttpServletResponse.SC_OK);
            return;
        }

        String[] splits = info.split("/");

        if (splits.length != 2 || splits[1].isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String idString = splits[1];
        int id;
        try {
            id = Integer.parseInt(idString);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        CollectionPoint collectionPoint = collectionPointDAO.findById(id);
        if (collectionPoint == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        SerializationUtils.sendResponse(resp, req, collectionPoint, HttpServletResponse.SC_OK);
    }
}
