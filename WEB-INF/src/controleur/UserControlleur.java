package controleur;

import dao.JDBCUserDAO;
import dao.UserDAO;
import dto.CollectionPoint;
import dto.LeaderBoardUser;
import dto.User;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.MergeUtils;
import utils.PathUtils;
import utils.SerializationUtils;

import java.io.IOException;
import java.util.Collection;

@WebServlet("/users/*")
public class UserControlleur extends PatchServlet{
    private final UserDAO usersDAO = new JDBCUserDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String info = req.getPathInfo();
        if (info == null || info.equals("/")) {
            int limit = utils.ParamUtils.getLimit(req);
            int offset = utils.ParamUtils.getOffset(req);
            Collection<LeaderBoardUser> l;

            if (limit > 0 && offset >= 0) {
                l = usersDAO.leaderboard(limit, offset);
            } else if (limit > 0) {
                l = usersDAO.leaderboard(limit);
            } else if (offset > 0) {
                l = usersDAO.leaderboard(0, offset);
            } else {
                l = usersDAO.leaderboard();
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
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
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
        User patchData;
        try {
            patchData = SerializationUtils.parseRequest(req, User.class);
        }catch (Exception e){
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        User existingRecord = usersDAO.findById(id);

        if (existingRecord != null) {
            MergeUtils.merge(existingRecord, patchData);
            if (usersDAO.update(existingRecord) != null){
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            };
            SerializationUtils.sendResponse(resp, req, existingRecord, HttpServletResponse.SC_OK);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
