package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

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
        String accept = req.getHeader("Accept");
        String responseContentType = resolveResponseContentType(accept);

        resp.setContentType(responseContentType);
        resp.setStatus(status);

        ObjectMapper mapper = ContentMapperFactory.getMapper(responseContentType);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        mapper.setDateFormat(df);
        resp.getWriter().println(mapper.writeValueAsString(data));
    }

    private static String resolveResponseContentType(String acceptHeader) {
        if (acceptHeader == null || acceptHeader.isBlank()) {
            return "application/json";
        }

        // Accept peut contenir plusieurs types + paramètres (q=...)
        for (String rawType : acceptHeader.split(",")) {
            String mediaType = rawType.trim();
            int separatorIndex = mediaType.indexOf(';');
            if (separatorIndex >= 0) {
                mediaType = mediaType.substring(0, separatorIndex).trim();
            }

            mediaType = mediaType.toLowerCase(Locale.ROOT);
            if ("application/xml".equals(mediaType) || "text/xml".equals(mediaType)) {
                return "application/xml";
            }
            if ("application/json".equals(mediaType) || "*/*".equals(mediaType)) {
                return "application/json";
            }
        }

        return "application/json";
    }
}
