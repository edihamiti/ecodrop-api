package controleur;

import dao.JDBCWasteTypeDAO;
import dao.WasteTypeDAO;
import dto.WasteType;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.SerializationUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

@WebServlet("/waste-types/*")
public class WasteTypesControlleur extends HttpServlet {
    private static final WasteTypeDAO wasteTypesDAO = new JDBCWasteTypeDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String info = req.getPathInfo();
        // Cas "/"
        if (info == null || info.equals("/")) {
            Collection<WasteType> l = wasteTypesDAO.findAll();
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

        WasteType wasteType = wasteTypesDAO.findById(id);
        if (wasteType == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        SerializationUtils.sendResponse(resp, req, wasteType, HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        WasteType wasteType = SerializationUtils.parseRequest(req, WasteType.class);
        if (wasteType == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        WasteType saved = wasteTypesDAO.save(wasteType);
        if (saved == null) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        SerializationUtils.sendResponse(resp, req, saved, HttpServletResponse.SC_CREATED);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int id;
        try {
            String[] splits = pathInfo.split("/");
            if (splits.length != 2) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            id = Integer.parseInt(splits[1]);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (id <= 0) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Vérifier que le wasteType à mettre à jour existe
        WasteType oldType = wasteTypesDAO.findById(id);
        if (oldType == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // Récupération du wasteType à mettre à jour depuis le body de la requête
        WasteType newType = null;
        try {
            newType = SerializationUtils.parseRequest(req, WasteType.class);
        } catch (IOException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (newType == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // On n'autorise pas la modification des IDs pour éviter les incohérences
        if (!Objects.equals(newType.id(), oldType.id())) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // Mettre à jour
        WasteType updated = wasteTypesDAO.update(newType);

        if (updated == null) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        SerializationUtils.sendResponse(resp, req, updated, HttpServletResponse.SC_OK);
    }
}
