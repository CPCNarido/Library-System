import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class UserMenu extends JPanel {
    private Image backgroundImage;
    private JButton BOOKLIST_Button;
    private JButton LOGOUT_BUTTON;
    private JButton BOOKBORROWING_Button;
    private JButton BOOKRETURNING_Button;

    public UserMenu() {
        // Load the background image
        backgroundImage = new ImageIcon("Menu-Admin.png").getImage();

        // Set panel properties
        setLayout(null); // Use absolute positioning

        // Create BOOKLIST button
        BOOKLIST_Button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isArmed()) {
                    g2d.setColor(new Color(0xB09E92).darker());
                } else {
                    g2d.setColor(new Color(0xB09E92));
                }
                int arc = 5; // Set the corner radius to 5 pixels
                int width = getWidth();
                int height = getHeight();
                g2d.fillRoundRect(0, 0, width, height, arc, arc);
                g2d.dispose();

                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // No border painting needed
            }
        };

        BOOKLIST_Button.setBounds(102, 307, 675, 174); // Set button bounds (x, y, width, height)
        BOOKLIST_Button.setContentAreaFilled(false); // Ensure the background is painted in paintComponent
        BOOKLIST_Button.setOpaque(false);
        BOOKLIST_Button.setForeground(Color.WHITE); // Set button text color

        // Create a panel to hold the icon and text vertically
        JPanel booklistContentPanel = new JPanel();
        booklistContentPanel.setLayout(new BoxLayout(booklistContentPanel, BoxLayout.Y_AXIS));
        booklistContentPanel.setOpaque(false); // Make the panel transparent

        // Load the icon image and create a label to display it
        ImageIcon booklistIcon = new ImageIcon("Booklist_Icon.png");
        JLabel booklistIconLabel = new JLabel(booklistIcon);
        booklistIconLabel.setAlignmentX(CENTER_ALIGNMENT); // Center align the icon
        booklistContentPanel.add(booklistIconLabel);

        // Add a vertical gap of 5 pixels
        booklistContentPanel.add(Box.createVerticalStrut(5));

        // Create a label for the text "BOOK LIST"
        JLabel booklistTextLabel = new JLabel("BOOK LIST");
        booklistTextLabel.setFont(new Font("Raleway", Font.BOLD, 18)); // Set the font to bold and size 18
        booklistTextLabel.setForeground(Color.WHITE); // Set the text color
        booklistTextLabel.setAlignmentX(CENTER_ALIGNMENT); // Center align the text
        booklistContentPanel.add(booklistTextLabel);

        BOOKLIST_Button.add(booklistContentPanel); // Add content panel to the BOOKLIST button

        BOOKLIST_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle button click action here
                System.out.println("BOOKLIST button clicked!");
            }
        });

        add(BOOKLIST_Button); // Add BOOKLIST button to the panel

        // Create LOGOUT button
        LOGOUT_BUTTON = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isArmed()) {
                    g2d.setColor(new Color(0xCAA88D).darker());
                } else {
                    g2d.setColor(new Color(0xCAA88D));
                }
                int arc = 5; // Set the corner radius to 5 pixels
                int width = getWidth();
                int height = getHeight();
                g2d.fillRoundRect(0, 0, width, height, arc, arc);
                g2d.dispose();

                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // No border painting needed
            }
        };

        LOGOUT_BUTTON.setBounds(793, 307, 134, 174); // Set button bounds (x, y, width, height)
        LOGOUT_BUTTON.setContentAreaFilled(false); // Ensure the background is painted in paintComponent
        LOGOUT_BUTTON.setOpaque(false);
        LOGOUT_BUTTON.setForeground(Color.WHITE); // Set button text color

        // Create a panel to hold the icon and text vertically
        JPanel logoutContentPanel = new JPanel();
        logoutContentPanel.setLayout(new BoxLayout(logoutContentPanel, BoxLayout.Y_AXIS));
        logoutContentPanel.setOpaque(false); // Make the panel transparent

        // Load the icon image and create a label to display it
        ImageIcon logoutIcon = new ImageIcon("LOGOUT.png");
        JLabel logoutIconLabel = new JLabel(logoutIcon);
        logoutIconLabel.setAlignmentX(CENTER_ALIGNMENT); // Center align the icon
        logoutContentPanel.add(logoutIconLabel);

        // Add a vertical gap of 5 pixels
        logoutContentPanel.add(Box.createVerticalStrut(5));

        // Create a label for the text "LOGOUT"
        JLabel logoutTextLabel = new JLabel("LOGOUT");
        logoutTextLabel.setFont(new Font("Raleway", Font.BOLD, 18)); // Set the font to bold and size 18
        logoutTextLabel.setForeground(Color.WHITE); // Set the text color
        logoutTextLabel.setAlignmentX(CENTER_ALIGNMENT); // Center align the text
        logoutContentPanel.add(logoutTextLabel);

        LOGOUT_BUTTON.add(logoutContentPanel); // Add content panel to the LOGOUT button

        LOGOUT_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle button click action here
                System.out.println("LOGOUT button clicked!");
            }
        });

        add(LOGOUT_BUTTON); // Add LOGOUT button to the panel
        
        // Create BOOKBORROWING button
        BOOKBORROWING_Button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isArmed()) {
                    g2d.setColor(new Color(0x9E6351).darker());
                } else {
                    g2d.setColor(new Color(0x9E6351));
                }
                int arc = 5; // Set the corner radius to 5 pixels
                int width = getWidth();
                int height = getHeight();
                g2d.fillRoundRect(0, 0, width, height, arc, arc);
                g2d.dispose();

                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // No border painting needed
            }
        };

        BOOKBORROWING_Button.setBounds(102, 496, 375, 174); // Set button bounds (x, y, width, height)
        BOOKBORROWING_Button.setContentAreaFilled(false); // Ensure the background is painted in paintComponent
        BOOKBORROWING_Button.setOpaque(false);
        BOOKBORROWING_Button.setForeground(Color.WHITE); // Set button text color

        // Create a panel to hold the icon and text in a row
        JPanel bookborrowingContentPanel = new JPanel();
        bookborrowingContentPanel.setLayout(new BoxLayout(bookborrowingContentPanel, BoxLayout.Y_AXIS));
        bookborrowingContentPanel.setOpaque(false); // Make the panel transparent

        // Load the icon image and create a label to display it
        ImageIcon bookborrowingIcon = new ImageIcon("BOOKBORROWING.png");
        JLabel bookborrowingIconLabel = new JLabel(bookborrowingIcon);
        bookborrowingIconLabel.setAlignmentX(CENTER_ALIGNMENT); // Center align the icon
        bookborrowingIconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, -1, 0)); // Add margin to the top
        bookborrowingContentPanel.add(bookborrowingIconLabel);

        // Add a vertical gap of 5 pixels
        bookborrowingContentPanel.add(Box.createVerticalStrut(5));

        // Create a label for the text "BOOK BORROWING"
        JLabel bookborrowingTextLabel = new JLabel("BOOK BORROWING");
        bookborrowingTextLabel.setFont(new Font("Raleway", Font.BOLD, 18)); // Set the font to bold and size 18
        bookborrowingTextLabel.setForeground(Color.WHITE); // Set the text color
        bookborrowingTextLabel.setAlignmentX(CENTER_ALIGNMENT); // Center align the text
        bookborrowingTextLabel.setBorder(BorderFactory.createEmptyBorder(-5, 0, 0, 0)); // Add margin to the top
        bookborrowingContentPanel.add(bookborrowingTextLabel);

        BOOKBORROWING_Button.add(bookborrowingContentPanel); // Add content panel to the BOOKBORROWING button

        BOOKBORROWING_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle button click action here
                System.out.println("BOOK BORROWING button clicked!");
            }
        });

        add(BOOKBORROWING_Button); // Add BOOKBORROWING button to the panel


        //BOOK RETURNING
        BOOKRETURNING_Button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                if (getModel().isArmed()) {
                    g2d.setColor(new Color(0xB09E92).darker());
                } else {
                    g2d.setColor(new Color(0xB09E92));
                }
                int arc = 5; // Set the corner radius to 5 pixels
                int width = getWidth();
                int height = getHeight();
                g2d.fillRoundRect(0, 0, width, height, arc, arc);
                g2d.dispose();

                super.paintComponent(g);
            }

            @Override
            protected void paintBorder(Graphics g) {
                // No border painting needed
            }
        };

        BOOKRETURNING_Button.setBounds(493, 496, 434, 174); // Set button bounds (x, y, width, height)
        BOOKRETURNING_Button.setContentAreaFilled(false); // Ensure the background is painted in paintComponent
        BOOKRETURNING_Button.setOpaque(false);
        BOOKRETURNING_Button.setForeground(Color.WHITE); // Set button text color

        // Create a panel to hold the icon and text in a row
        JPanel bookreturningContentPanel = new JPanel();
        bookreturningContentPanel.setLayout(new BoxLayout(bookreturningContentPanel, BoxLayout.Y_AXIS));
        bookreturningContentPanel.setOpaque(false); // Make the panel transparent

        // Load the icon image and create a label to display it
        ImageIcon bookreturningIcon = new ImageIcon("BOOKRETURNING.png");
        JLabel bookreturningIconLabel = new JLabel(bookreturningIcon);
        bookreturningIconLabel.setAlignmentX(CENTER_ALIGNMENT); // Center align the icon
        bookreturningIconLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // Add margin to the top
        bookreturningContentPanel.add(bookreturningIconLabel);

        // Add a vertical gap of 5 pixels
        bookreturningContentPanel.add(Box.createVerticalStrut(5));

        // Create a label for the text "BOOK BORROWING"
        JLabel bookreturningTextLabel = new JLabel("BOOK RETURNING");
        bookreturningTextLabel.setFont(new Font("Raleway", Font.BOLD, 18)); // Set the font to bold and size 18
        bookreturningTextLabel.setForeground(Color.WHITE); // Set the text color
        bookreturningTextLabel.setAlignmentX(CENTER_ALIGNMENT); // Center align the text
        bookreturningTextLabel.setBorder(BorderFactory.createEmptyBorder(-4, 0, 0, 0)); // Add margin to the top
        bookreturningContentPanel.add(bookreturningTextLabel);

        BOOKRETURNING_Button.add(bookreturningContentPanel); // Add content panel to the BOOKBORROWING button

        BOOKRETURNING_Button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Handle button click action here
                System.out.println("BOOK RETURNING button clicked!");
            }
        });

        add(BOOKRETURNING_Button); // Add BOOKBORROWING button to the panel

        JFrame frame = new JFrame("Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        UserMenu panel = new UserMenu();
        panel.setPreferredSize(new Dimension(1024, 768));

        frame.getContentPane().add(panel);
        frame.pack();
        frame.setVisible(true);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw the background image
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
    }
}