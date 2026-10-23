package dao;

import util.PasswordUtil;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/** Opens the SQLite connection and creates the tables on first run. */
public final class DatabaseConnection {

    // Database name: Student Tuition Payment Reservation System (SQLite file in the project folder).
    private static final String URL = "jdbc:sqlite:studentpayment.db";

    private DatabaseConnection() {
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    /** Creates the users table (and a default admin) if they do not exist yet. */
    public static void initialize() {
        String createUsers = """
                CREATE TABLE IF NOT EXISTS users (
                    id       INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    roles    TEXT NOT NULL,
                    status   TEXT NOT NULL DEFAULT 'PENDING'
                )
                """;

        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.execute(createUsers);
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize database: " + e.getMessage(), e);
        }

        seedAdmin();
    }

    // Default administrator so someone can approve new accounts: admin / admin123
    private static void seedAdmin() {
        String check = "SELECT 1 FROM users WHERE username = ?";
        String insert = "INSERT INTO users (username, password, roles, status) VALUES (?, ?, 'ADMIN', 'ACTIVE')";
        try (Connection conn = getConnection();
             PreparedStatement c = conn.prepareStatement(check)) {
            c.setString(1, "admin");
            if (!c.executeQuery().next()) {
                try (PreparedStatement i = conn.prepareStatement(insert)) {
                    i.setString(1, "admin");
                    i.setString(2, PasswordUtil.hash("admin123"));
                    i.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to seed admin: " + e.getMessage(), e);
        }
    }
}
