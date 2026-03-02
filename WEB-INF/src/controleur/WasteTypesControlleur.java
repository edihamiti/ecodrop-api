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

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

@WebServlet("/waste-types/*")
public class WasteTypesControlleur extends HttpServlet {
    private static WasteTypeDAO wasteTypesDAO = new JDBCWasteTypeDAO();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        XmlMapper xmlMapper = new XmlMapper();

        String accept = req.getHeader("Accept");
        if (accept == null) accept = "application/json";
        resp.setContentType(accept);

        String info = req.getPathInfo();

        // Cas "/"
        if (info == null || info.equals("/")) {
            Collection<WasteType> l = wasteTypesDAO.findAll();
            if (l.isEmpty()) {
                resp.sendError(HttpServletResponse.SC_NO_CONTENT);}
            if (accept.equals("application/json")) {
                out.println(objectMapper.writeValueAsString(l));
            } else if (accept.equals("application/xml")) {
                out.println(xmlMapper.writeValueAsString(l));
            }
            return;
        }

        String[] splits = info.split("/");

        if (splits.length != 2) {
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

        if (accept.equals("application/json")) {
            out.println(objectMapper.writeValueAsString(wasteType));
        } else if (accept.equals("application/xml")) {
            out.println(xmlMapper.writeValueAsString(wasteType));
        }
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

        boolean success = wasteTypesDAO.save(wasteType);

        if (success) {
            resp.setStatus(HttpServletResponse.SC_CREATED);
            out.println(objectMapper.writeValueAsString(wasteType));
            return;
        }
        resp.sendError(409);
    }
}
