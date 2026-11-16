import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Database {
    private static final String URL = "jdbc:sqlite:smartweb.db";

    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL);
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
                String sql = "CREATE TABLE IF NOT EXISTS users (" +
                             "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                             "username TEXT UNIQUE NOT NULL, " +
                             "email TEXT UNIQUE NOT NULL, " +
                             "password_hash TEXT NOT NULL, " +
                             "salt TEXT NOT NULL, " +
                             "role TEXT DEFAULT 'USER')";
                stmt.execute(sql);
            }
            return conn;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao conectar ao banco de dados: " + e.getMessage());
        }
    }
}
