import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminMenu extends JFrame {

    public AdminMenu() {
        setTitle("Alfombros Archives");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        add(backgroundPanel);
        setVisible(true);
        System.out.println("Admin Menu created");
    }

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            String backgroundImagePath = "./assets/menu_admin.png";
            backgroundImage = loadImage(backgroundImagePath);
            if (backgroundImage == null) {
                System.err.println("Background image not found: " + backgroundImagePath);
            }
            setLayout(null);
            createStyledButton("BOOKS", 100, 290, 462, 174, "./assets/books_icon.png", new Color(158, 99, 81), e -> openBooksWindow());
            createStyledButton("PENALTY", 575, 290, 195, 174, "./assets/penalty_icon.png", new Color(202, 168, 141), e -> openPenaltyWindow());
            createStyledButton("LOGOUT", 785, 290, 134, 174, "./assets/logout_icon.png", new Color(176, 158, 146), e -> logout());
            createStyledButton("BORROWER LIST", 100, 470, 375, 174, "./assets/borrowers_list_icon.png", new Color(202, 168, 141), e -> openBorrowerListWindow());
            createStyledButton("BOOKS BORROWED", 485, 470, 434, 174, "./assets/books_borrowed_icon.png", new Color(158, 99, 81), e -> openBooksBorrowedWindow());
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        private void createStyledButton(String label, int x, int y, int width, int height, String imagePath, Color bgColor, ActionListener actionListener) {
            JButton button = new JButton();
            button.setFont(new Font("Raleway", Font.PLAIN, 18));
            button.setForeground(Color.WHITE);
            button.setBackground(bgColor);
            button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
            button.setBounds(x, y, width, height);
            button.setIcon(new ImageIcon(imagePath));
            button.addActionListener(actionListener);
            
            JPanel textPanel = new JPanel(new BorderLayout());
            textPanel.setOpaque(false);
            JLabel textLabel = new JLabel(label);
            textLabel.setFont(new Font("Raleway", Font.BOLD, 16));
            textLabel.setForeground(Color.WHITE);
            textLabel.setHorizontalAlignment(SwingConstants.CENTER);
            textPanel.add(textLabel, BorderLayout.SOUTH);
            button.add(textPanel);

            add(button);
        }

        private Image loadImage(String imagePath) {
            try {
                return new ImageIcon(imagePath).getImage();
            } catch (Exception e) {
                System.err.println("Error loading image: " + e.getMessage());
                return null;
            }
        }

        //Books window
        private void openBooksWindow() {
            dispose();
            SwingUtilities.invokeLater(AdminBookList::createAndShowGUI);
        }
        //penalty window
        private void openPenaltyWindow() {

        }
        //logout window
        private void logout() {
            new LoginFrame();
            dispose();
        }
        //borrower window
        private void openBorrowerListWindow() {
            //    new BookBorrowerList(); 
        }
        //borrowed books window
        private void openBooksBorrowedWindow() {
            // new BookList();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            AdminMenu frame = new AdminMenu();
            frame.setVisible(true);
        });
    }
}
