package controleur;

import dao.JDBCWasteTypeDAO;
import dao.WasteTypeDAO;
import dto.WasteType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.PathUtils;
import utils.SerializationUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;

@WebServlet("/waste-types/*")
public class WasteTypesControlleur extends HttpServlet {
    private static final WasteTypeDAO wasteTypesDAO = new JDBCWasteTypeDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Integer id = PathUtils.parseId(req.getPathInfo());

            // Cas "/"
            if (id == null) {
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

            WasteType wasteType = wasteTypesDAO.findById(id);
            if (wasteType == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            SerializationUtils.sendResponse(resp, req, wasteType, HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
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
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            Integer id = PathUtils.parseId(req.getPathInfo());

            if (id == null || id <= 0) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            WasteType wasteType = wasteTypesDAO.findById(id);
            if (wasteType == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            boolean success = wasteTypesDAO.delete(id);
            if (!success) {
                resp.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }

            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);

        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Integer id = PathUtils.parseId(req.getPathInfo());

            if (id == null || id <= 0) {
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
            WasteType newType;
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

        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
