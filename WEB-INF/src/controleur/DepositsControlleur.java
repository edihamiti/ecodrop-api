package controleur;

import dao.DepositDAO;
import dao.JDBCDepositDAO;
import dto.CollectionPoint;
import dto.Deposit;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.MergeUtils;
import utils.PathUtils;
import utils.SerializationUtils;

import java.io.IOException;
import java.util.Collection;

@WebServlet("/deposits/*")
public class DepositsControlleur extends PatchServlet {
    private static final DepositDAO depositDAO = new JDBCDepositDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            Integer id = PathUtils.parseId(req.getPathInfo());

            // Cas "/"
            if (id == null) {
                int limit = utils.ParamUtils.getLimit(req);
                int offset = utils.ParamUtils.getOffset(req);
                Collection<Deposit> l;

                if (limit > 0 && offset >= 0) {
                    l = depositDAO.findAll(limit, offset);
                } else if (limit > 0) {
                    l = depositDAO.findAll(limit);
                } else {
                    l = depositDAO.findAll();
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

            Deposit deposit = depositDAO.findById(id);
            if (deposit == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            SerializationUtils.sendResponse(resp, req, deposit, HttpServletResponse.SC_OK);
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Deposit deposit;
        try {
            deposit = SerializationUtils.parseRequest(req, Deposit.class);
        } catch (IOException e) {
            System.out.printf("[DEBUG] IOException: %s", e.getMessage());
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Corps de la requête invalide");
            return;
        }

        if (deposit == null || deposit.getPoint() == null || deposit.getWasteType() == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Données du dépôt invalides");
            return;
        }

        // 400 si le poids est négatif ou nul
        if (deposit.getWeight() <= 0) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Le poids doit être un nombre positif");
            return;
        }

        System.out.println("[DEBUG] Deposit: " + deposit);

        try {
            Deposit saved = depositDAO.save(deposit);
            if (saved == null) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            SerializationUtils.sendResponse(resp, req, saved, HttpServletResponse.SC_CREATED);
        } catch (IllegalStateException e) {
            // 403 Forbidden si le point de collecte est saturé ou si le type de déchets n'est pas accepté
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
        }
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

        Deposit patchData;
        try {
            patchData = SerializationUtils.parseRequest(req, Deposit.class);
        } catch (Exception e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (patchData == null) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Deposit existingData = depositDAO.findById(id);
        if (existingData == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        if (existingData != null) {
            MergeUtils.merge(existingData, patchData);
            if (depositDAO.update(existingData) == null) {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            SerializationUtils.sendResponse(resp, req, existingData, HttpServletResponse.SC_OK);
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

        Deposit putData;
        try {
            putData = SerializationUtils.parseRequest(req, Deposit.class);
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Corps de la requête invalide");
            return;
        }

        if (putData == null || putData.getPoint() == null || putData.getWasteType() == null || putData.getWeight() <= 0) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Données du dépôt invalides ou champs manquants");
            return;
        }

        Deposit existing = depositDAO.findById(id);
        if (existing == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        putData.setId(id);

        Deposit updated;
        try {

            updated = depositDAO.update(putData);
        } catch (NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (updated == null) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        SerializationUtils.sendResponse(resp, req, updated, HttpServletResponse.SC_OK);
    }
}
