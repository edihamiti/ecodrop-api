package utils;

import java.lang.reflect.Field;

public class MergeUtils {

    /**
     * Fusionne deux objets du même type.
     * Les champs de 'existing' sont mis à jour par ceux de 'patch'
     * UNIQUEMENT si le champ dans 'patch' n'est pas null.
     */
    public static <T> T merge(T existing, T patch) {
        if (existing == null || patch == null) return existing;

        // On récupère la classe des objets
        Class<?> clazz = existing.getClass();
        Field[] fields = clazz.getDeclaredFields();

        for (Field field : fields) {
            // Permet d'accéder aux champs privés (private)
            field.setAccessible(true);
            try {
                Object newValue = field.get(patch);

                // On ne remplace que si la nouvelle valeur n'est pas nulle
                if (newValue != null) {
                    field.set(existing, newValue);
                }
            } catch (IllegalAccessException e) {
                // Gestion d'erreur si on ne peut pas accéder au champ
                e.printStackTrace();
            }
        }
        return existing;
    }
}
