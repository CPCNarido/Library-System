import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JCheckBox showPasswordCheckbox;

    public LoginFrame() {
        setTitle("Login");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);
        setResizable(false);

        // Background image
        JLabel background = new JLabel(new ImageIcon("./assets/login.png"));
        setContentPane(background);
        background.setLayout(null);

        // Username Field
        usernameField = new JTextField();
        usernameField.setBounds(425, 320, 310, 35);
        usernameField.setBackground(Color.decode("#CAA88D"));
        usernameField.setForeground(Color.decode("#5C382C"));
        usernameField.setFont(new Font("Arial", Font.BOLD, 19));
        usernameField.setBorder(null);
        background.add(usernameField);

        // Password Field
        passwordField = new JPasswordField();
        passwordField.setBounds(425, 380, 310, 35);
        passwordField.setBackground(Color.decode("#CAA88D"));
        passwordField.setForeground(Color.decode("#5C382C"));
        passwordField.setFont(new Font("Arial", Font.BOLD, 19));
        passwordField.setBorder(null);
        background.add(passwordField);

        // Show Password Checkbox
        showPasswordCheckbox = new JCheckBox("Show Password");
        showPasswordCheckbox.setBounds(420, 435, 150, 25);
        showPasswordCheckbox.setBackground(Color.decode("#F8E8D1"));
        background.add(showPasswordCheckbox);
        showPasswordCheckbox.setFont(new Font("Arial", Font.BOLD, 15));
        showPasswordCheckbox.setForeground(Color.decode("#5C382C"));
        showPasswordCheckbox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (showPasswordCheckbox.isSelected()) {
                    passwordField.setEchoChar((char) 0);
                } else {
                    passwordField.setEchoChar('•');
                }
            }
        });

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setBounds(633, 440, 100, 35);
        loginButton.setBackground(Color.decode("#5C382C"));
        loginButton.setForeground(Color.decode("#FFFFFF"));
        loginButton.setFont(new Font("Arial", Font.BOLD, 18));
        background.add(loginButton);
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                authenticateUser();
            }
        });

        setVisible(true);
    }

    private void authenticateUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        Connection connection = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/alfombroarchives", "root", "");
            pst = connection.prepareStatement("SELECT id, name, type FROM users WHERE username=? AND password=?");
            pst.setString(1, username);
            pst.setString(2, password);
            rs = pst.executeQuery();

            if (rs.next()) {
                User.id = rs.getInt("id");
                User.name = rs.getString("name");
                User.type = rs.getString("type");

                System.out.println("User ID: " + User.id);
                System.out.println("Name: " + User.name);
                System.out.println("User Type: " + User.type);

                if (User.type.equals("Admin")) {
                    JOptionPane.showMessageDialog(this, "Welcome Admin!");
                    // Go to Admin Panel
                    new AdminMenu();
                } else {
                    JOptionPane.showMessageDialog(this, "Welcome User!");
                    // Go to Borrowers Panel
                    new UserMenu();
                }
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Username or Password", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database driver not found", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database connection error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pst != null) pst.close();
                if (connection != null) connection.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}