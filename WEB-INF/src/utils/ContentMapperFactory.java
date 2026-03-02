package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class ContentMapperFactory {
    private static final ObjectMapper jsonMapper = new ObjectMapper();
    private static final XmlMapper xmlMapper = new XmlMapper();

    public static ObjectMapper getMapper(String contentType) {
        if (contentType != null && contentType.contains("application/xml")) {
            return xmlMapper;
        }
        // Par défaut, on retourne JSON
        return jsonMapper;
    }
}
