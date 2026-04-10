import java.sql.*;
import java.util.Vector;
import javax.swing.*;
import java.awt.*;

public class ManageGuests {

    int userId;
    String username;

    public ManageGuests(int userId, String username) {
        this.userId = userId;
        this.username = username;

        JFrame frame = new JFrame("Manage Guests");
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

        JLabel title = new JLabel("Manage Guests");
        UITheme.styleHeader(title);
        leftPanel.add(title, gbc);

        gbc.gridy++;
        JComboBox<String> eventBox = new JComboBox<>();
        UITheme.styleComboBox(eventBox);
        leftPanel.add(eventBox, gbc);

        gbc.gridy++;
        JLabel nameLabel = new JLabel("Guest Name");
        UITheme.styleLabel(nameLabel);
        leftPanel.add(nameLabel, gbc);

        gbc.gridy++;
        JTextField nameField = new JTextField(15);
        UITheme.styleTextField(nameField);
        leftPanel.add(nameField, gbc);

        gbc.gridy++;
        JLabel emailLabel = new JLabel("Email");
        UITheme.styleLabel(emailLabel);
        leftPanel.add(emailLabel, gbc);

        gbc.gridy++;
        JTextField emailField = new JTextField(15);
        UITheme.styleTextField(emailField);
        leftPanel.add(emailField, gbc);

        gbc.gridy++;
        JLabel phoneLabel = new JLabel("Phone Number");
        UITheme.styleLabel(phoneLabel);
        leftPanel.add(phoneLabel, gbc);

        gbc.gridy++;
        JTextField phoneField = new JTextField(15);
        UITheme.styleTextField(phoneField);
        leftPanel.add(phoneField, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(25, 5, 10, 5);
        JButton addBtn = new JButton("Add Guest");
        UITheme.styleButton(addBtn);
        leftPanel.add(addBtn, gbc);

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
                columns.add("Action");

                Vector<Vector<Object>> data = new Vector<>();
                while (rs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(rs.getInt("id"));
                    row.add(rs.getString("name"));
                    row.add(rs.getString("email"));
                    row.add(rs.getString("phone"));
                    row.add("🗑️ Delete");
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

        table.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                int col = table.columnAtPoint(e.getPoint());
                if (row >= 0 && col == 4) { // Action column
                    int guestId = (int) table.getValueAt(row, 0);
                    int confirm = JOptionPane.showConfirmDialog(frame, "Remove this guest?", "Confirm", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        try {
                            Connection conn = DBConnection.connect();
                            PreparedStatement stmt = conn.prepareStatement("DELETE FROM guests WHERE id=?");
                            stmt.setInt(1, guestId);
                            stmt.executeUpdate();
                            loadGuests.run();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                }
            }
        });

        addBtn.addActionListener(e -> {
            try {
                String selected = (String) eventBox.getSelectedItem();
                if (selected == null) {
                    JOptionPane.showMessageDialog(frame, "Please create an event first!");
                    return;
                }
                int eventId = Integer.parseInt(selected.split(" - ")[0]);
                Connection conn = DBConnection.connect();
                String sql = "INSERT INTO guests(name,email,phone,event_id) VALUES(?,?,?,?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, nameField.getText());
                stmt.setString(2, emailField.getText());
                stmt.setString(3, phoneField.getText());
                stmt.setInt(4, eventId);
                stmt.executeUpdate();

                JOptionPane.showMessageDialog(frame, "Guest Added!");
                nameField.setText("");
                emailField.setText("");
                phoneField.setText("");
                loadGuests.run();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        eventBox.addActionListener(e -> loadGuests.run());

        if (eventBox.getItemCount() > 0) {
            loadGuests.run();
        }

        frame.setVisible(true);
    }
}