package epaw.lab4.util;

import java.io.FileWriter;
import java.io.PrintWriter;

public class DBLogger {

    private static final String DB_FILE = "DB.txt";

    public static synchronized void append(String sql) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DB_FILE, true))) {
            pw.println(sql + ";");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** Wrap a value in single quotes with internal quotes escaped, or return NULL. */
    public static String q(String s) {
        if (s == null) return "NULL";
        return "'" + s.replace("'", "''") + "'";
    }
}
