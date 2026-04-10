import javax.swing.*;
import java.awt.*;

public class LoginPage {

    static boolean isAdminLogin = true; // start with admin login

    public static void main(String[] args) {

        DBConnection.connect();

        JFrame frame = new JFrame("EventEase - Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UITheme.applyFrameTheming(frame);
        frame.setLayout(new GridBagLayout());

        buildUI(frame);
    }

    private static void buildUI(JFrame frame) {
        frame.getContentPane().removeAll();

        JPanel card = UITheme.createCardPanel();
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;

        JLabel title = new JLabel(isAdminLogin ? "Admin Login" : "Guest Login", SwingConstants.CENTER);
        UITheme.styleTitle(title);
        card.add(title, gbc);

        gbc.gridy++;
        JLabel headline = new JLabel(isAdminLogin ? "Your Ultimate Event Command Center" : "View Your Invitations",
                SwingConstants.CENTER);
        headline.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headline.setForeground(UITheme.COLOR_TEXT_LIGHT);
        card.add(headline, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(0, 15, 25, 15);
        JLabel subHeadline = new JLabel(isAdminLogin ? "Seamlessly Design, Manage, and Experience Unforgettable Events."
                : "Log in to see events you have been invited to.", SwingConstants.CENTER);
        UITheme.styleSubtitle(subHeadline);
        card.add(subHeadline, gbc);

        gbc.insets = new Insets(10, 15, 10, 15);

        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel userLabel = new JLabel(isAdminLogin ? "Username" : "Email");
        UITheme.styleLabel(userLabel);
        card.add(userLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        JTextField usernameField = new JTextField(20);
        UITheme.styleTextField(usernameField);
        card.add(usernameField, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        JLabel passLabel = new JLabel("Password");
        UITheme.styleLabel(passLabel);
        card.add(passLabel, gbc);

        gbc.gridy++;
        gbc.gridwidth = 2;
        JPasswordField passwordField = new JPasswordField(20);
        UITheme.styleTextField(passwordField);
        card.add(passwordField, gbc);

        gbc.gridy++;
        gbc.gridwidth = 1;
        gbc.weightx = 0.5;
        JButton loginBtn = new JButton("Login");
        UITheme.styleButton(loginBtn);
        card.add(loginBtn, gbc);

        gbc.gridx = 1;
        JButton registerBtn = new JButton("Register");
        UITheme.styleSecondaryButton(registerBtn);
        card.add(registerBtn, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 15, 10, 15);
        JButton toggleRoleBtn = new JButton(isAdminLogin ? "Switch to Guest Login" : "Switch to Admin Login");
        UITheme.styleSecondaryButton(toggleRoleBtn);
        card.add(toggleRoleBtn, gbc);

        toggleRoleBtn.addActionListener(e -> {
            isAdminLogin = !isAdminLogin;
            buildUI(frame);
        });

        loginBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            try {
                java.sql.Connection conn = DBConnection.connect();
                if (isAdminLogin) {
                    String sql = "SELECT * FROM users WHERE username=? AND password=?";
                    java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, username);
                    stmt.setString(2, password);
                    java.sql.ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        int userId = rs.getInt("id");
                        frame.dispose();
                        new Dashboard(userId, username);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid Username or Password", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    String sql = "SELECT * FROM guest_users WHERE email=? AND password=?";
                    java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, username); // it's email
                    stmt.setString(2, password);
                    java.sql.ResultSet rs = stmt.executeQuery();
                    if (rs.next()) {
                        String email = rs.getString("email");
                        frame.dispose();
                        new GuestDashboard(email);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Invalid Email or Password", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        registerBtn.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            if (username.trim().isEmpty() || password.trim().isEmpty()) {
                JOptionPane.showMessageDialog(frame,
                        (isAdminLogin ? "Username" : "Email") + " and Password cannot be empty");
                return;
            }
            try {
                java.sql.Connection conn = DBConnection.connect();
                if (isAdminLogin) {
                    String checkSql = "SELECT * FROM users WHERE username=?";
                    java.sql.PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                    checkStmt.setString(1, username);
                    java.sql.ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(frame, "User already exists! Please Login.");
                    } else {
                        String insertSql = "INSERT INTO users(username, password) VALUES(?, ?)";
                        java.sql.PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                        insertStmt.setString(1, username);
                        insertStmt.setString(2, password);
                        insertStmt.executeUpdate();
                        JOptionPane.showMessageDialog(frame, "Admin Registration Successful! You can now Login.");
                    }
                } else {
                    String checkSql = "SELECT * FROM guest_users WHERE email=?";
                    java.sql.PreparedStatement checkStmt = conn.prepareStatement(checkSql);
                    checkStmt.setString(1, username);
                    java.sql.ResultSet rs = checkStmt.executeQuery();
                    if (rs.next()) {
                        JOptionPane.showMessageDialog(frame, "Guest User already exists! Please Login.");
                    } else {
                        String insertSql = "INSERT INTO guest_users(email, password) VALUES(?, ?)";
                        java.sql.PreparedStatement insertStmt = conn.prepareStatement(insertSql);
                        insertStmt.setString(1, username);
                        insertStmt.setString(2, password);
                        insertStmt.executeUpdate();
                        JOptionPane.showMessageDialog(frame, "Guest Registration Successful! You can now Login.");
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        frame.add(card);
        frame.revalidate();
        frame.repaint();
        if (!frame.isVisible()) {
            frame.setVisible(true);
        }
    }
}