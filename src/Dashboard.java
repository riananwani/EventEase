import java.awt.*;
import javax.swing.*;

public class Dashboard {

    int userId;
    String username;

    public Dashboard(int userId, String username) {
        this.userId = userId;
        this.username = username;

        JFrame frame = new JFrame("EventEase Dashboard");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        UITheme.applyFrameTheming(frame);
        frame.setLayout(new GridBagLayout());

        JPanel card = UITheme.createCardPanel();
        card.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel title = new JLabel("Welcome back, " + username + "!", SwingConstants.CENTER);
        UITheme.styleTitle(title);
        card.add(title, gbc);

        gbc.gridy++;
        JButton createEvent = new JButton("Create New Event");
        UITheme.styleButton(createEvent);
        card.add(createEvent, gbc);

        createEvent.addActionListener(e -> {
            frame.dispose();
            new CreateEvent(userId, username);
        });

        gbc.gridy++;
        JButton manageGuests = new JButton("Manage Guests");
        UITheme.styleButton(manageGuests);
        card.add(manageGuests, gbc);

        manageGuests.addActionListener(e -> {
            frame.dispose();
            new ManageGuests(userId, username);
        });

        gbc.gridy++;
        JButton sendInvite = new JButton("Send Invitations");
        UITheme.styleButton(sendInvite);
        card.add(sendInvite, gbc);

        sendInvite.addActionListener(e -> {
            frame.dispose();
            new SendInvitation(userId, username);
        });

        gbc.gridy++;
        JButton logout = new JButton("Logout");
        UITheme.styleSecondaryButton(logout);
        card.add(logout, gbc);

        logout.addActionListener(e -> {
            frame.dispose();
            LoginPage.main(new String[]{});
        });

        frame.add(card);
        frame.setVisible(true);
    }
}