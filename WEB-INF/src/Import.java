import bdd.DS;
import org.postgresql.copy.CopyManager;
import org.postgresql.core.BaseConnection;

import java.io.InputStream;
import java.sql.Connection;

public class Import {
    public static void main(String[] args) {
        DS bdd = new DS();
        try (Connection con = bdd.getConnection();
             InputStream csv = Import.class.getClassLoader().getResourceAsStream("Data.csv")) {

            if (csv == null) throw new RuntimeException("Data.csv introuvable dans le classpath");

            CopyManager cm = new CopyManager((BaseConnection) con);
            long rows = cm.copyIn(
                    "COPY Deposit (userid, pointid, wasteTypeId, poids) FROM STDIN DELIMITER ',' CSV HEADER",
                    csv
            );
            System.out.println(rows + " lignes insérées avec succès.");
        } catch (Exception e) {
            System.err.println("Import échoué : " + e.getMessage());
            e.printStackTrace();
        }
    }
}
