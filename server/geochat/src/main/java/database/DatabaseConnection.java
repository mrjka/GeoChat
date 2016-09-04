package database;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by paulosk on 05/04/16.
 */
public class DatabaseConnection {

    private final static String CONNECTION_URL =
            "jdbc:postgresql://127.0.0.1:5432/geochat";

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch(ClassNotFoundException e) {
            System.err.println("Could not find PostgreSQL driver. Exiting.");
            System.exit(-1);
        }
    }

    public static Connection connect() {
        Connection con = null;

        try {
            con = DriverManager.getConnection(CONNECTION_URL, "paulosk", "");
        } catch(SQLException e) {
            System.err.println("Failed to get database connection. Exiting.");
        }

        return con;
    }
}
