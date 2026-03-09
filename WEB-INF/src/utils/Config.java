package utils;

import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final Properties props = new Properties();

    static {
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream("config.prop")) {
            if (in == null) {
                throw new RuntimeException("Fichier config.prop introuvable dans le classpath");
            }
            props.load(in);
        } catch (Exception e) {
            throw new ExceptionInInitializerError("Erreur fatale lors du chargement de config.prop : " + e.getMessage());
        }
    }

    public static String get(String key) {
        return props.getProperty(key);
    }
}