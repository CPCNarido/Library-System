import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LoadingScreen extends JFrame {

    public LoadingScreen() {
        setTitle("Alfombros Archives");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel backgroundPanel = new BackgroundPanel();
        add(backgroundPanel);
    }

    private class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {

            backgroundImage = new ImageIcon("./assets/enter_archive.png").getImage();

            
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    new LoginFrame();
                    dispose(); 
                }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);

            
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Serif", Font.PLAIN, 20));
            FontMetrics fm = g2d.getFontMetrics();
            String footer = "Click Anywhere to Continue";
            int x = (getWidth() - fm.stringWidth(footer)) / 2;
            int y = getHeight() - fm.getHeight() - 20;
            g2d.drawString(footer, x, y);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoadingScreen frame = new LoadingScreen();
            frame.setVisible(true);
        });
    }
}
