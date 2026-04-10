import java.sql.*;

public class TestApp {
    public static void main(String[] args) {
        try {
            Connection conn = DBConnection.connect();
            ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM events");
            int count = 0;
            while(rs.next()) {
                System.out.println("Event ID: " + rs.getInt("id") + " Name: " + rs.getString("name"));
                count++;
            }
            if (count == 0) {
                System.out.println("NO EVENTS FOUND!");
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
