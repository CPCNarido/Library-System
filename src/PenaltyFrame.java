import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PenaltyPaying extends JPanel {

    private Image backgroundImage;

    private JTextField nameField;
    private JTextField amountDueField;
    private JTextField amountPaidField;
    private JTextField changeField;
    private JButton payPenaltyButton;
    private JLabel infoLabel;

    private Connection connection;

    public PenaltyPaying() {
        try {
            backgroundImage = ImageIO.read(new File("PenaltyPaying.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        setPreferredSize(new Dimension(1024, 768));
        setLayout(null); // Use absolute positioning

        // Create and configure the text fields
        nameField = createTextField(382, 395);
        amountDueField = createTextField(382, 440);
        amountPaidField = createTextField(382, 488);
        changeField = createTextField(382, 537);

        amountDueField.setEditable(false);
        changeField.setEditable(false);

        // Add text fields to the panel
        add(nameField);
        add(amountDueField);
        add(amountPaidField);
        add(changeField);

        // Create and configure the submit button
        payPenaltyButton = new JButton("Pay Penalty");
        payPenaltyButton.setBounds(713, 606, 196, 38);
        payPenaltyButton.setBackground(new Color(158, 99, 81)); // Set background color
        payPenaltyButton.setFont(new Font("Raleway", Font.BOLD, 24)); // Set font
        payPenaltyButton.setForeground(Color.WHITE); // Set text color to white
        add(payPenaltyButton);

        // Create and configure the information label
        infoLabel = new JLabel("<html>To reduce the penalty you owe, simply return<br>some borrowed books</html>");
        infoLabel.setBounds(130, 606, 351, 38);
        infoLabel.setFont(new Font("Raleway", Font.BOLD | Font.ITALIC, 16));
        infoLabel.setForeground(new Color(59, 28, 17)); // Set text color
        add(infoLabel);

        // Add listeners
        nameField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateAmountDue();
            }
        });

        amountPaidField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateChange();
            }
        });

        payPenaltyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                processPayment();
            }
        });

        // Set up the database connection
        String jdbcURL = "jdbc:mysql://localhost:3306/alfombroarchives";
        String dbUser = "root";
        String dbPassword = "";

        try {
            connection = DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection failed!");
            System.exit(1);
        }
    }

    private JTextField createTextField(int x, int y) {
        JTextField textField = new JTextField();
        textField.setBounds(x, y, 503, 30);
        textField.setBackground(Color.WHITE);
        textField.setFont(new Font("Raleway", Font.BOLD, 20));
        textField.setBorder(BorderFactory.createEmptyBorder());
        return textField;
    }

    private void updateAmountDue() {
        String username = nameField.getText();
        String selectSql = "SELECT b.borrower_id, " +
                            "CASE WHEN DATEDIFF(CURDATE(), b.due_date) > 0 THEN DATEDIFF(CURDATE(), b.due_date) * 20 ELSE 0 END AS Amount_due " +
                            "FROM users u " +
                            "JOIN books_borrowed b ON u.id = b.borrower_id " +
                            "WHERE u.username = ? AND b.is_paid = 0";

        try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
            statement.setString(1, username);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int amountDue = resultSet.getInt("Amount_due");
                amountDueField.setText(String.valueOf(amountDue));
            } else {
                amountDueField.setText("");
                JOptionPane.showMessageDialog(this, "No unpaid books found for this user!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateChange() {
        try {
            int amountDue = Integer.parseInt(amountDueField.getText());
            int amountPaid = Integer.parseInt(amountPaidField.getText());
            int change = amountPaid - amountDue;
            changeField.setText(String.valueOf(change));
        } catch (NumberFormatException e) {
            changeField.setText("");
        }
    }

    private void processPayment() {
        String username = nameField.getText();
        int amountDue = Integer.parseInt(amountDueField.getText());
        int amountPaid = Integer.parseInt(amountPaidField.getText());

        if (amountPaid >= amountDue) {
            String updateSql = "UPDATE books_borrowed b JOIN users u ON u.id = b.borrower_id " +
                               "SET b.is_paid = 1 WHERE u.username = ? AND b.is_paid = 0";

            try (PreparedStatement statement = connection.prepareStatement(updateSql)) {
                statement.setString(1, username);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Payment successful!");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Insufficient payment!");
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Penalty Paying");
        PenaltyPaying panel = new PenaltyPaying();
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setResizable(false); // Make the frame non-resizable
        frame.setLocationRelativeTo(null); // Center the frame
        frame.setVisible(true);
    }
}
