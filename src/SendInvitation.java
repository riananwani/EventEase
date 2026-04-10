import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import java.awt.*;

public class SendInvitation {

    int userId;
    String username;

    public SendInvitation(int userId, String username) {
        this.userId = userId;
        this.username = username;

        JFrame frame = new JFrame("Send Invitation");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UITheme.applyFrameTheming(frame);
        frame.setLayout(new GridBagLayout());

        JPanel card = UITheme.createCardPanel();
        card.setLayout(new BorderLayout(30, 0));

        // LEFT PANEL (Controls)
        JPanel leftPanel = new JPanel(new GridBagLayout());
        leftPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 5, 10, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel title = new JLabel("Select Event");
        UITheme.styleHeader(title);
        leftPanel.add(title, gbc);

        gbc.gridy++;
        JComboBox<String> eventBox = new JComboBox<>();
        UITheme.styleComboBox(eventBox);
        leftPanel.add(eventBox, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(25, 5, 10, 5);
        JButton inviteBtn = new JButton("Invite All!");
        UITheme.styleButton(inviteBtn);
        leftPanel.add(inviteBtn, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(5, 5, 10, 5);
        JButton backBtn = new JButton("Back");
        UITheme.styleSecondaryButton(backBtn);
        leftPanel.add(backBtn, gbc);

        // RIGHT PANEL (Table)
        JTable table = new JTable();
        UITheme.styleTable(table);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(UITheme.COLOR_BORDER, 2));
        scrollPane.getViewport().setBackground(UITheme.COLOR_PANEL_BG);
        scrollPane.setPreferredSize(new Dimension(500, 450));

        card.add(leftPanel, BorderLayout.WEST);
        card.add(scrollPane, BorderLayout.CENTER);

        frame.add(card);

        // Load events
        try {
            Connection conn = DBConnection.connect();
            String sql = "SELECT id, name FROM events WHERE user_id=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                eventBox.addItem(rs.getInt("id") + " - " + rs.getString("name"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        backBtn.addActionListener(e -> {
            frame.dispose();
            new Dashboard(userId, username);
        });

        Runnable loadGuests = () -> {
            try {
                String selected = (String) eventBox.getSelectedItem();
                if (selected == null) return;
                int eventId = Integer.parseInt(selected.split(" - ")[0]);

                Connection conn = DBConnection.connect();
                String sql = "SELECT id, name, email, phone FROM guests WHERE event_id=?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setInt(1, eventId);
                ResultSet rs = stmt.executeQuery();

                Vector<String> columns = new Vector<>();
                columns.add("ID");
                columns.add("Name");
                columns.add("Email");
                columns.add("Phone");

                Vector<Vector<Object>> data = new Vector<>();
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt("id"));
                    row.add(rs.getString("name"));
                    row.add(rs.getString("email"));
                    row.add(rs.getString("phone"));
                    data.add(row);
                }

                table.setModel(new javax.swing.table.DefaultTableModel(data, columns) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        };

        eventBox.addActionListener(e -> loadGuests.run());

        inviteBtn.addActionListener(e -> {
            String selected = (String) eventBox.getSelectedItem();
            if (selected == null) return;
            int eventId = Integer.parseInt(selected.split(" - ")[0]);

            try {
                Connection conn = DBConnection.connect();
                for (int i = 0; i < table.getRowCount(); i++) {
                    int guestId = (int) table.getValueAt(i, 0);
                    String sql = "INSERT INTO invitations(event_id, guest_id, message, status) VALUES(?,?,?,?)";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setInt(1, eventId);
                    stmt.setInt(2, guestId);
                    stmt.setString(3, "You are invited to the event!");
                    stmt.setString(4, "Sent");
                    stmt.executeUpdate();
                }
                JOptionPane.showMessageDialog(frame, "Invitation sent successfully!");
                loadGuests.run();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        if (eventBox.getItemCount() > 0) {
            loadGuests.run();
        }

        frame.setVisible(true);
    }
}
