package controleur;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import dao.JDBCWasteTypeDAO;
import dao.WasteTypeDAO;
import dto.WasteType;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.SerializationUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

@WebServlet("/waste-types/*")
public class WasteTypesControlleur extends HttpServlet {
    private static WasteTypeDAO wasteTypesDAO = new JDBCWasteTypeDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();

        String accept = req.getHeader("Accept");
        if (accept == null) accept = "application/json";
        resp.setContentType(accept);

        String contentType = req.getContentType();
        if (contentType == null) contentType = "application/json";

        WasteType wasteType = null;
        ObjectMapper objectMapper = new ObjectMapper();
        if (contentType.equals("application/xml")) {
            objectMapper = new XmlMapper();
            wasteType = objectMapper.readValue(req.getInputStream(), WasteType.class);
        } else if (contentType.equals("application/json")) {
            objectMapper = new ObjectMapper();
            wasteType = objectMapper.readValue(req.getReader(), WasteType.class);
        }

        if (accept.equals("application/json")) {
            objectMapper = new ObjectMapper();
        } else if (accept.equals("application/xml")) {
            objectMapper = new XmlMapper();
        }

        if (wasteType == null) resp.sendError(HttpServletResponse.SC_BAD_REQUEST);

        WasteType created = wasteTypesDAO.save(wasteType);

        if (created != null) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            out.println(objectMapper.writeValueAsString(created));
            return;
        }
        resp.sendError(409);
    }
}
