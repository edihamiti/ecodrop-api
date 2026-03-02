package utils;

public class PathUtils {

    /**
     * Extrait l'ID de la ressource à partir du pathInfo.
     * @param pathInfo le pathInfo de la requête
     * @return l'ID sous forme d'Integer, ou null si le chemin est la racine ("/" ou null)
     * @throws IllegalArgumentException si le chemin est mal formé ou si l'ID n'est pas un nombre
     */
    public static Integer parseId(String pathInfo) throws IllegalArgumentException {
        if (pathInfo == null || pathInfo.equals("/")) {
            return null;
        }

        String[] splits = pathInfo.split("/");

        // On attend "/{id}", donc length doit être 2 (le premier élément est vide avant le /)
        if (splits.length != 2 || splits[1].isEmpty()) {
            throw new IllegalArgumentException("Format du chemin invalide");
        }

        try {
            return Integer.parseInt(splits[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("L'ID fourni n'est pas un entier valide", e);
        }
    }
}
