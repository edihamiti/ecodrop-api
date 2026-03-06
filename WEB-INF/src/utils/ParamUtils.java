package utils;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Map;

public class ParamUtils {
    /**
     * Extrait la limite de pagination à partir des paramètres de la requête.
     * @param request la requête HTTP
     * @return la limite de pagination, ou -1 si le paramètre n'est pas présent ou mal formé
     */
    public static int getLimit(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        if (params.containsKey("limit")) {
            try {
                return Integer.parseInt(params.get("limit")[0]);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }

    /**
     * Extrait l'offset de pagination à partir des paramètres de la requête.
     * @param request la requête HTTP
     * @return l'offset de pagination, ou -1 si le paramètre n'est pas présent ou mal formé
     */
    public static int getOffset(HttpServletRequest request) {
        Map<String, String[]> params = request.getParameterMap();
        if (params.containsKey("offset")) {
            try {
                return Integer.parseInt(params.get("offset")[0]);
            } catch (NumberFormatException e) {
                return -1;
            }
        }
        return -1;
    }
}
