package controleur;

import dao.CollectionPointDAO;
import dao.JDBCCollectionPointDAO;
import dto.CollectionPoint;
import dto.WasteType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.MergeUtils;
import utils.PathUtils;
import utils.SerializationUtils;

import java.io.IOException;
import java.util.Collection;

@WebServlet("/points/*")
public class CollectionPointControlleur extends PatchServlet {
    private final CollectionPointDAO collectionPointDAO = new JDBCCollectionPointDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String info = req.getPathInfo();
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

        Integer id;
        try {
            id = PathUtils.parseId(info);
        } catch (IllegalArgumentException e) {
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

    @Override
    public void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String info = req.getPathInfo();
        if (info == null || info.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
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
        }catch (Exception e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        CollectionPoint existingRecord = collectionPointDAO.findById(id);

        if (existingRecord != null) {
            MergeUtils.merge(existingRecord, patchData);
            if (collectionPointDAO.update(existingRecord) != null){
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            };
            SerializationUtils.sendResponse(resp, req, existingRecord, HttpServletResponse.SC_OK);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
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

        CollectionPoint putData;
        try {
            putData = SerializationUtils.parseRequest(req, CollectionPoint.class);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        if (putData == null || putData.getAdresse() == null || putData.getCapaciteMax() == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        CollectionPoint existingRecord = collectionPointDAO.findById(id);
        if (existingRecord == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // L'ID ne peut pas être mis à jour, on force celui de l'URL
        putData.setId(id);

        CollectionPoint updated = collectionPointDAO.update(putData);
        if (updated == null) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        SerializationUtils.sendResponse(resp, req, updated, HttpServletResponse.SC_OK);
    }
}
