import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GuestInvitesPage {

    String guestEmail;

    public GuestInvitesPage(String guestEmail) {
        this.guestEmail = guestEmail;

        JFrame frame = new JFrame("Your Invitations - EventEase");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UITheme.applyFrameTheming(frame);
        frame.setLayout(new GridBagLayout());

        JPanel mainCard = UITheme.createCardPanel();
        mainCard.setLayout(new BorderLayout(20, 20));
        mainCard.setPreferredSize(new Dimension(800, 600));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        JLabel title = new JLabel("Your Invitations", SwingConstants.CENTER);
        UITheme.styleTitle(title);
        headerPanel.add(title, BorderLayout.CENTER);

        JButton backBtn = new JButton("Back to Dashboard");
        UITheme.styleSecondaryButton(backBtn);
        backBtn.addActionListener(e -> {
            frame.dispose();
            new GuestDashboard(guestEmail);
        });
        headerPanel.add(backBtn, BorderLayout.WEST);

        mainCard.add(headerPanel, BorderLayout.NORTH);

        // Invites List Panel
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(UITheme.COLOR_PANEL_BG);

        try {
            Connection conn = DBConnection.connect();
            String sql = "SELECT e.name AS event_name, e.date AS event_date, e.location AS event_location, e.description AS event_description, u.username AS admin_name " +
                         "FROM invitations i " +
                         "JOIN guests g ON i.guest_id = g.id " +
                         "JOIN events e ON g.event_id = e.id " +
                         "JOIN users u ON e.user_id = u.id " +
                         "WHERE g.email = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, guestEmail);
            ResultSet rs = stmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                String eventName = rs.getString("event_name");
                String adminName = rs.getString("admin_name");
                String eventDate = rs.getString("event_date");
                String eventLocation = rs.getString("event_location");
                String eventDescription = rs.getString("event_description");

                JPanel itemPanel = new JPanel(new BorderLayout(15, 0));
                itemPanel.setBackground(UITheme.COLOR_BACKGROUND);
                itemPanel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(UITheme.COLOR_PRIMARY, 1, true),
                        BorderFactory.createEmptyBorder(20, 20, 20, 20)
                ));
                itemPanel.setMaximumSize(new Dimension(750, 100));

                JLabel messageLabel = new JLabel("You have an invitation");
                messageLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
                messageLabel.setForeground(UITheme.COLOR_TEXT_LIGHT);
                
                itemPanel.add(messageLabel, BorderLayout.CENTER);

                JButton openBtn = new JButton("Open");
                UITheme.styleButton(openBtn);
                openBtn.addActionListener(e -> {
                    String fullDetails = "Event: " + eventName + "\n" +
                                         "Location: " + eventLocation + "\n" +
                                         "Date: " + eventDate + "\n\n" +
                                         "Description:\n" + eventDescription + "\n\n- By " + adminName;
                    JOptionPane.showMessageDialog(frame, fullDetails, "Event Details", JOptionPane.INFORMATION_MESSAGE);
                });
                itemPanel.add(openBtn, BorderLayout.EAST);
                
                listPanel.add(itemPanel);
                listPanel.add(Box.createRigidArea(new Dimension(0, 15)));
                count++;
            }

            if (count == 0) {
                JLabel emptyLabel = new JLabel("You have no pending invitations.", SwingConstants.CENTER);
                UITheme.styleHeader(emptyLabel);
                emptyLabel.setForeground(UITheme.COLOR_TEXT_MUTED);
                listPanel.add(emptyLabel);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        JScrollPane scrollPane = new JScrollPane(listPanel);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(UITheme.COLOR_PANEL_BG);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        mainCard.add(scrollPane, BorderLayout.CENTER);

        frame.add(mainCard);
        frame.setVisible(true);
    }
}
