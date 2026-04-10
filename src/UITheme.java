import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.JTableHeader;

public class UITheme {

    // Premium Dark Mode Colors
    public static final Color COLOR_BACKGROUND = new Color(15, 23, 42);     // Slate 900
    public static final Color COLOR_PANEL_BG = new Color(30, 41, 59);       // Slate 800
    public static final Color COLOR_PRIMARY = new Color(139, 92, 246);      // Neon Purple (Violet 500)
    public static final Color COLOR_PRIMARY_HOVER = new Color(124, 58, 237); // Violet 600
    public static final Color COLOR_TEXT_LIGHT = new Color(248, 250, 252);  // Slate 50
    public static final Color COLOR_TEXT_MUTED = new Color(148, 163, 184);  // Slate 400
    public static final Color COLOR_BORDER = new Color(51, 65, 85);         // Slate 700
    public static final Color COLOR_WHITE = Color.WHITE;

    // Modern Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 46); // Increased size
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.ITALIC, 16);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 15);
    public static final Font FONT_BUTTON = new Font("Segoe UI", Font.BOLD, 15);

    public static void applyFrameTheming(JFrame frame) {
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Always Full Screen
        
        BackgroundPanel bgPanel = new BackgroundPanel("resources/bg.png");
        bgPanel.setLayout(new GridBagLayout());
        frame.setContentPane(bgPanel);
    }

    public static class BackgroundPanel extends JPanel {
        private Image bgImage;

        public BackgroundPanel(String imagePath) {
            try {
                bgImage = new ImageIcon(imagePath).getImage();
            } catch (Exception e) {
                // Ignore, will fallback to solid color
            }
            setBackground(COLOR_BACKGROUND);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (bgImage != null && bgImage.getWidth(null) > 0) { // Ensure image loaded
                g.drawImage(bgImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    public static JPanel createCardPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(COLOR_PANEL_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 1),
            new EmptyBorder(40, 50, 40, 50)
        ));
        return panel;
    }

    public static void styleTitle(JLabel label) {
        label.setFont(FONT_TITLE);
        label.setForeground(COLOR_PRIMARY);
    }

    public static void styleSubtitle(JLabel label) {
        label.setFont(FONT_SUBTITLE);
        label.setForeground(COLOR_TEXT_MUTED);
    }

    public static void styleHeader(JLabel label) {
        label.setFont(FONT_HEADER);
        label.setForeground(COLOR_TEXT_LIGHT);
    }

    public static void styleLabel(JLabel label) {
        label.setFont(FONT_REGULAR);
        label.setForeground(COLOR_TEXT_MUTED);
    }

    public static void styleButton(JButton button) {
        button.setBackground(COLOR_PRIMARY);
        button.setForeground(COLOR_WHITE);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_PRIMARY_HOVER, 1, true),
                new EmptyBorder(12, 20, 12, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(COLOR_PRIMARY_HOVER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(COLOR_PRIMARY);
            }
        });
    }

    public static void styleSecondaryButton(JButton button) {
        button.setBackground(COLOR_PANEL_BG);
        button.setForeground(COLOR_TEXT_LIGHT);
        button.setFont(FONT_BUTTON);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLOR_BORDER, 2, true),
                new EmptyBorder(12, 20, 12, 20)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(COLOR_BORDER);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(COLOR_PANEL_BG);
            }
        });
    }

    public static void styleTextField(JTextField textField) {
        textField.setFont(FONT_REGULAR);
        textField.setForeground(COLOR_TEXT_LIGHT);
        textField.setBackground(COLOR_BACKGROUND);
        textField.setCaretColor(COLOR_WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(COLOR_BORDER, 2, true),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
    }
    
    public static void styleComboBox(JComboBox<?> box) {
        box.setFont(FONT_REGULAR);
        box.setForeground(COLOR_TEXT_LIGHT);
        box.setBackground(COLOR_BACKGROUND);
        box.setBorder(BorderFactory.createLineBorder(COLOR_BORDER, 1));
    }

    public static void styleTable(JTable table) {
        table.setFont(FONT_REGULAR);
        table.setForeground(COLOR_TEXT_LIGHT);
        table.setBackground(COLOR_PANEL_BG);
        table.setRowHeight(40);
        table.setSelectionBackground(COLOR_PRIMARY);
        table.setSelectionForeground(COLOR_WHITE);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_BUTTON);
        header.setBackground(COLOR_BORDER);
        header.setForeground(COLOR_TEXT_LIGHT);
        header.setPreferredSize(new Dimension(100, 45));
        header.setBorder(BorderFactory.createEmptyBorder());
    }
}
