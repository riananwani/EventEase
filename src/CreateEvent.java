import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.swing.*;

public class CreateEvent {

    int userId;
    String username;

    public CreateEvent(int userId, String username) {
        this.userId = userId;
        this.username = username;

        JFrame frame = new JFrame("Create Event");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UITheme.applyFrameTheming(frame);
        frame.setLayout(new GridBagLayout());

        JPanel card = UITheme.createCardPanel();
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        JLabel title = new JLabel("Create New Event", SwingConstants.CENTER);
        UITheme.styleTitle(title);
        card.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        JLabel nameLabel = new JLabel("Event Name");
        UITheme.styleLabel(nameLabel);
        card.add(nameLabel, gbc);

        gbc.gridx = 1;
        JTextField nameField = new JTextField(20);
        UITheme.styleTextField(nameField);
        card.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel dateLabel = new JLabel("Date (DD/MM/YYYY)");
        UITheme.styleLabel(dateLabel);
        card.add(dateLabel, gbc);

        gbc.gridx = 1;
        JTextField dateField = new JTextField(20);
        UITheme.styleTextField(dateField);
        card.add(dateField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel locationLabel = new JLabel("Location");
        UITheme.styleLabel(locationLabel);
        card.add(locationLabel, gbc);

        gbc.gridx = 1;
        JTextField locationField = new JTextField(20);
        UITheme.styleTextField(locationField);
        card.add(locationField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel descLabel = new JLabel("Description");
        UITheme.styleLabel(descLabel);
        card.add(descLabel, gbc);

        gbc.gridx = 1;
        JTextArea descArea = new JTextArea(4, 20);
        descArea.setFont(UITheme.FONT_REGULAR);
        descArea.setForeground(UITheme.COLOR_TEXT_LIGHT);
        descArea.setBackground(UITheme.COLOR_BACKGROUND);
        descArea.setCaretColor(UITheme.COLOR_WHITE);
        descArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UITheme.COLOR_BORDER, 2, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        descArea.setLineWrap(true);
        JScrollPane scrollDesc = new JScrollPane(descArea);
        scrollDesc.setBorder(BorderFactory.createEmptyBorder());
        card.add(scrollDesc, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JLabel categoryLabel = new JLabel("Category");
        UITheme.styleLabel(categoryLabel);
        card.add(categoryLabel, gbc);
        
        gbc.gridx = 1;
        String[] categories = {"Birthday", "Anniversary", "Baby Shower", "Family Event", "Celebration", "Seminar", "Meeting", "Workshop"};
        JComboBox<String> categoryBox = new JComboBox<>(categories);
        UITheme.styleComboBox(categoryBox);
        card.add(categoryBox, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        JButton backBtn = new JButton("Back");
        UITheme.styleSecondaryButton(backBtn);
        card.add(backBtn, gbc);

        gbc.gridx = 1;
        JButton saveBtn = new JButton("Save Event");
        UITheme.styleButton(saveBtn);
        card.add(saveBtn, gbc);

        backBtn.addActionListener(e -> {
            frame.dispose();
            new Dashboard(userId, username);
        });

        saveBtn.addActionListener(e -> {
            String name = nameField.getText();
            String date = dateField.getText();
            String location = locationField.getText();
            String description = descArea.getText();
            String category = (String) categoryBox.getSelectedItem();
            
            try {
                Connection conn = DBConnection.connect();
                String sql = "INSERT INTO events(name,date,location,description,category,user_id) VALUES(?,?,?,?,?,?)";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, name);
                stmt.setString(2, date);
                stmt.setString(3, location);
                stmt.setString(4, description);
                stmt.setString(5, category);
                stmt.setInt(6, userId);
                stmt.executeUpdate();
                
                JOptionPane.showMessageDialog(frame, "Event created successfully!");
                frame.dispose();
                new Dashboard(userId, username);
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(frame, "Error creating event: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        frame.add(card);
        frame.setVisible(true);
    }
}