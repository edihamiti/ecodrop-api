package controleur;

import dao.CollectionPointDAO;
import dao.JDBCCollectionPointDAO;
import dto.CollectionPoint;
import dto.CollectionPointStatus;
import dto.CollectionPointWithWasteTypes;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.MergeUtils;
import utils.PathUtils;
import utils.SerializationUtils;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

@WebServlet("/points/*")
public class CollectionPointControlleur extends PatchServlet {
    private final CollectionPointDAO collectionPointDAO = new JDBCCollectionPointDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String info = req.getPathInfo();

        // GET /points/overloaded
        if ("/overloaded".equals(info)) {
            List<CollectionPointStatus> overloaded = collectionPointDAO.findOverloaded();
            if (overloaded == null) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            if (overloaded.isEmpty()) {
                resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
                return;
            }
            SerializationUtils.sendResponse(resp, req, overloaded, HttpServletResponse.SC_OK);
            return;
        }

        // GET /points ou GET /points/
        if (info == null || info.equals("/")) {
            int limit = utils.ParamUtils.getLimit(req);
            int offset = utils.ParamUtils.getOffset(req);
            Collection<CollectionPoint> l;

            if (limit > 0 && offset >= 0) {
                l = collectionPointDAO.findAll(limit, offset);
            } else if (limit > 0) {
                l = collectionPointDAO.findAll(limit);
            } else {
                l = collectionPointDAO.findAll();
            }

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

        // Routes avec ID : /{id}, /{id}/status
        String[] segments = info.split("/");
        // segments[0] = "" (avant le premier /), segments[1] = id, segments[2] optionnel = "status" ou "clear"

        if (segments.length < 2 || segments[1].isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        int id;
        try {
            id = Integer.parseInt(segments[1]);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // GET /points/{id}/status
        if (segments.length == 3 && "status".equals(segments[2])) {
            CollectionPointStatus status = collectionPointDAO.getStatus(id);
            if (status == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            SerializationUtils.sendResponse(resp, req, status, HttpServletResponse.SC_OK);
            return;
        }

        // GET /points/{id}  — avec WasteTypes imbriqués
        if (segments.length == 2) {
            CollectionPointWithWasteTypes cp = collectionPointDAO.findByIdWithWasteTypes(id);
            if (cp == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
            SerializationUtils.sendResponse(resp, req, cp, HttpServletResponse.SC_OK);
            return;
        }

        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

    @Override
    public void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String info = req.getPathInfo();
        if (info == null || info.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Integer id;
        try {
            id = PathUtils.parseId(info);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        CollectionPoint patchData;
        try {
            patchData = SerializationUtils.parseRequest(req, CollectionPoint.class);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (patchData == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        CollectionPoint existingRecord = collectionPointDAO.findById(id);
        if (existingRecord == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        MergeUtils.merge(existingRecord, patchData);
        // Correction du bug : on renvoie 500 si update retourne null (échec), pas si ça réussit
        if (collectionPointDAO.update(existingRecord) == null) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        SerializationUtils.sendResponse(resp, req, existingRecord, HttpServletResponse.SC_OK);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String info = req.getPathInfo();
        if (info == null || info.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Integer id;
        try {
            id = PathUtils.parseId(info);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        CollectionPoint existing = collectionPointDAO.findById(id);
        if (existing == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        CollectionPoint putData;
        try {
            putData = SerializationUtils.parseRequest(req, CollectionPoint.class);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Corps de la requête invalide");
            return;
        }

        if (putData == null || putData.getAdresse() == null || putData.getCapaciteMax() == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Champs adresse et capaciteMax obligatoires");
            return;
        }

        putData.setId(id);
        CollectionPoint updated = collectionPointDAO.update(putData);
        if (updated == null) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        SerializationUtils.sendResponse(resp, req, updated, HttpServletResponse.SC_OK);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String info = req.getPathInfo();
        if (info == null || info.equals("/")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        // DELETE /points/{id}/clear
        String[] segments = info.split("/");
        if (segments.length == 3 && "clear".equals(segments[2])) {
            int id;
            try {
                id = Integer.parseInt(segments[1]);
            } catch (NumberFormatException e) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            CollectionPoint cp = collectionPointDAO.findById(id);
            if (cp == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            boolean ok = collectionPointDAO.clearDeposits(id);
            if (!ok) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            resp.setStatus(HttpServletResponse.SC_NO_CONTENT);
            return;
        }

        resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
}
