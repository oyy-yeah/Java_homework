package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtil {
    private static final String DB_URL = "jdbc:sqlite:library.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
            initializeDatabase();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }

    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void initializeDatabase() {
        Connection conn = null;
        Statement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.createStatement();

            String createBooksTable = "CREATE TABLE IF NOT EXISTS Books ("
                    + "ISBN TEXT PRIMARY KEY, "
                    + "title TEXT NOT NULL, "
                    + "author TEXT NOT NULL, "
                    + "publisher TEXT, "
                    + "price REAL, "
                    + "totalQuantity INTEGER DEFAULT 0, "
                    + "availableQuantity INTEGER DEFAULT 0, "
                    + "borrowable BOOLEAN DEFAULT FALSE)";
            stmt.execute(createBooksTable);

            String createReadersTable = "CREATE TABLE IF NOT EXISTS Readers ("
                    + "id TEXT PRIMARY KEY, "
                    + "name TEXT NOT NULL, "
                    + "type TEXT NOT NULL, "
                    + "currentBorrowedCount INTEGER DEFAULT 0, "
                    + "maxBorrowCount INTEGER DEFAULT 0)";
            stmt.execute(createReadersTable);

            String createBorrowRecordsTable = "CREATE TABLE IF NOT EXISTS BorrowRecords ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "readerId TEXT NOT NULL, "
                    + "bookISBN TEXT NOT NULL, "
                    + "borrowDate DATETIME NOT NULL, "
                    + "dueDate DATETIME NOT NULL, "
                    + "returnDate DATETIME, "
                    + "status TEXT NOT NULL, "
                    + "FOREIGN KEY (readerId) REFERENCES Readers(id), "
                    + "FOREIGN KEY (bookISBN) REFERENCES Books(ISBN))";
            stmt.execute(createBorrowRecordsTable);

            System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeStatement(stmt);
            closeConnection(conn);
        }
    }
}