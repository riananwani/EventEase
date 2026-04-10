import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBConnection {

    private static Connection connection = null;

    public static Connection connect() {

        if (connection != null) {
            return connection;
        }

        try {

            String url = "jdbc:sqlite:database/eventease.db";
            Connection conn = DriverManager.getConnection(url);

            Statement stmt = conn.createStatement();

            // USERS TABLE (Admin)
            String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "username TEXT," +
                    "password TEXT" +
                    ");";

            stmt.execute(usersTable);

            // GUEST USERS TABLE (For Guest Login)
            String guestUsersTable = "CREATE TABLE IF NOT EXISTS guest_users (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "email TEXT UNIQUE," +
                    "password TEXT" +
                    ");";
            stmt.execute(guestUsersTable);



            // EVENTS TABLE (Updated)
            String eventsTable = "CREATE TABLE IF NOT EXISTS events (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "date TEXT," +
                    "location TEXT," +
                    "description TEXT," +
                    "category TEXT," +
                    "user_id INTEGER" +
                    ");";

            stmt.execute(eventsTable);

            // GUESTS TABLE
            String guestsTable = "CREATE TABLE IF NOT EXISTS guests (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "email TEXT," +
                    "phone TEXT," +
                    "event_id INTEGER" +
                    ");";

            stmt.execute(guestsTable);

            // INVITATIONS TABLE
            String inviteTable = "CREATE TABLE IF NOT EXISTS invitations (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "event_id INTEGER," +
                    "guest_id INTEGER," +
                    "message TEXT," +
                    "status TEXT" +
                    ");";

            stmt.execute(inviteTable);

            // CATEGORY TABLE
            String categoryTable = "CREATE TABLE IF NOT EXISTS categories (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "category_name TEXT" +
                    ");";

            stmt.execute(categoryTable);

            stmt.close();
            connection = conn;
            return connection;

        } catch (Exception e) {
            System.out.println("Database connection failed");
            return null;
        }
    }
}