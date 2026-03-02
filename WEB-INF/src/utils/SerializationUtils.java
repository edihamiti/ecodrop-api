package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class SerializationUtils {

    /**
     * Lit le corps de la requête et le convertit en objet selon le Content-Type
     */
    public static <T> T parseRequest(HttpServletRequest req, Class<T> clazz) throws IOException {
        String contentType = req.getContentType();
        ObjectMapper mapper = ContentMapperFactory.getMapper(contentType);

        // Utiliser getInputStream() est plus robuste pour les flux binaires/XML
        return mapper.readValue(req.getInputStream(), clazz);
    }

    /**
     * Configure la réponse et convertit l'objet en String selon le header Accept
     */
    public static void sendResponse(HttpServletResponse resp, Object data, int status) throws IOException {
        String accept = resp.getHeader("Accept"); // On peut aussi le repasser en paramètre
        if (accept == null || accept.equals("*/*")) accept = "application/json";

        resp.setContentType(accept);
        resp.setStatus(status);

        ObjectMapper mapper = ContentMapperFactory.getMapper(accept);
        resp.getWriter().println(mapper.writeValueAsString(data));
    }
}
