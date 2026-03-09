package controleur;

import dao.DepositDAO;
import dao.JDBCDepositDAO;
import dao.JDBCWasteTypeDAO;
import dao.WasteTypeDAO;
import dto.Deposit;
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

@WebServlet("/deposits/*")
public class DepositsControlleur extends HttpServlet {
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
}
