import javax.swing.*;
import java.awt.*;

public class GuestDashboard {

    String guestEmail;

    public GuestDashboard(String email) {
        this.guestEmail = email;

        JFrame frame = new JFrame("EventEase - Guest Dashboard");
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

        JLabel title = new JLabel("Welcome, Guest!", SwingConstants.CENTER);
        UITheme.styleTitle(title);
        card.add(title, gbc);

        gbc.gridy++;
        JLabel emailLabel = new JLabel("(" + guestEmail + ")", SwingConstants.CENTER);
        UITheme.styleSubtitle(emailLabel);
        card.add(emailLabel, gbc);

        gbc.gridy++;
        gbc.insets = new Insets(30, 15, 15, 15);
        JButton viewInvitesBtn = new JButton("View Invites");
        UITheme.styleButton(viewInvitesBtn);
        card.add(viewInvitesBtn, gbc);

        viewInvitesBtn.addActionListener(e -> {
            frame.dispose();
            new GuestInvitesPage(guestEmail);
        });

        gbc.gridy++;
        gbc.insets = new Insets(15, 15, 15, 15);
        JButton logoutBtn = new JButton("Logout");
        UITheme.styleSecondaryButton(logoutBtn);
        card.add(logoutBtn, gbc);

        logoutBtn.addActionListener(e -> {
            frame.dispose();
            LoginPage.main(new String[]{});
        });

        frame.add(card);
        frame.setVisible(true);
    }
}
