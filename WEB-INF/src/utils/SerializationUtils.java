package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class SerializationUtils {

    /**
     * Lit le corps de la requête et le convertit en objet selon le Content-Type
     */
    public static <T> T parseRequest(HttpServletRequest req, Class<T> clazz) throws IOException {
        String contentType = req.getContentType();
        ObjectMapper mapper = ContentMapperFactory.getMapper(contentType);
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);

        // Utiliser getInputStream() est plus robuste pour les flux binaires/XML
        return mapper.readValue(req.getInputStream(), clazz);
    }

    /**
     * Configure la réponse et convertit l'objet en String selon le header Accept
     */
    public static void sendResponse(HttpServletResponse resp, HttpServletRequest req, Object data, int status) throws IOException {
        String accept = req.getHeader("Accept"); // On peut aussi le repasser en paramètre
        if (accept == null || accept.equals("*/*")) accept = "application/json";

        resp.setContentType(accept);
        resp.setStatus(status);

        ObjectMapper mapper = ContentMapperFactory.getMapper(accept);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);
        resp.getWriter().println(mapper.writeValueAsString(data));
    }
}
