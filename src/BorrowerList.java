import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Font;
import java.awt.Color;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JComboBox;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.UIManager;

public class BorrowerList extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Image backgroundImage;
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> comboBox;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    BorrowerList frame = new BorrowerList();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public BorrowerList() {
        // Load the background image
        backgroundImage = new ImageIcon("C:/Users/cj/Downloads/Borrowers List.png").getImage();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1024, 768); // Set the size of the JFrame to 1024x768

        // Create a custom JPanel to draw the background image
        contentPane = new JPanel() {
            private static final long serialVersionUID = 1L;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);

        setContentPane(contentPane);

        // Add values to the JComboBox
        comboBox = new JComboBox<>();
        comboBox.setBounds(79, 212, 211, 36);
        comboBox.setFont(new Font("Raleway", Font.PLAIN, 18));
        comboBox.setBackground(Color.decode("#94745E"));
        comboBox.setForeground(Color.WHITE); // Set the font color
        comboBox.addItem("STUDENT'S LIST");
        comboBox.addItem("TEACHER'S LIST");
        comboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String selectedItem = (String) comboBox.getSelectedItem();
                updateTableContent(selectedItem);
            }
        });
        contentPane.add(comboBox);

        // Create a button with an image and make it transparent
        JButton btnNewButton = new JButton();
        btnNewButton.setIcon(new ImageIcon("C:/Users/cj/Downloads/Back Button.png"));
        btnNewButton.setBounds(804, 0, 220, 141);
        btnNewButton.setContentAreaFilled(false);
        btnNewButton.setBorderPainted(false);
        btnNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Action to be performed when button is clicked
                System.out.println("Button clicked");
            }
        });
        contentPane.add(btnNewButton);

        // Initialize the table model
        tableModel = new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };
        table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                c.setBackground(Color.decode("#CCA98D"));
                c.setForeground(Color.WHITE);
                return c;
            }
        };
        table.setFont(new Font("Raleway", Font.PLAIN, 18));
        table.setRowHeight(25);
        table.setGridColor(Color.WHITE); // Set grid color to white
        table.setShowGrid(true);
        table.setForeground(Color.BLACK);

        // Style the table header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Raleway", Font.BOLD, 18));
        header.setBackground(Color.decode("#94745E"));
        header.setForeground(Color.WHITE);

        // Add the table to a JScrollPane
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(99, 280, 815, 366);
        scrollPane.getViewport().setBackground(Color.decode("#CCA98D")); // Ensure scrollPane background matches table
        contentPane.add(scrollPane);

        // Add initial data to the table
        updateTableContent("STUDENT'S LIST");
    }

    /**
     * Update table content based on the selected item.
     */
    private void updateTableContent(String selectedItem) {
        // Reset the table model
        tableModel.setRowCount(0); // Clear existing rows
        tableModel.setColumnCount(0); // Clear existing columns

        // Add columns and rows based on the selected item
        if (selectedItem.equals("STUDENT'S LIST")) {
            tableModel.addColumn("NAME");
            tableModel.addColumn("STUDENT ID");
            tableModel.addColumn("YEAR LEVEL");
            tableModel.addColumn("SECTION");

            // Fetch data from the database
            fetchDataFromDatabase("students");
        } else if (selectedItem.equals("TEACHER'S LIST")) {
            tableModel.addColumn("NAME");
            tableModel.addColumn("EMPLOYEE ID");
            tableModel.addColumn("DEPARTMENT");

            // Fetch data from the database
            fetchDataFromDatabase("teachers");
        }
    }

    /**
     * Fetch data from the database and populate the table.
     */
    private void fetchDataFromDatabase(String tableName) {
        // Dummy data for demonstration purposes
        String dbUrl = "jdbc:mysql://localhost:3306/alfombroarchives";
        String user = "root";
        String password = "";
        String query = "";

        if (tableName.equals("students")) {
            query = "SELECT u.name, s.student_id, s.year_level, s.section \r\n"
            		+ "FROM students s\r\n"
            		+ "LEFT JOIN users u ON s.user_id = u.id;\r\n"
            		+ "";
        } else if (tableName.equals("teachers")) {
            query = "SELECT u.name, t.employeeId, t.department FROM teacher t LEFT JOIN users u ON t.user_id = u.id";
        }

        try (Connection conn = DriverManager.getConnection(dbUrl, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                if (tableName.equals("students")) {
                    tableModel.addRow(new Object[]{
                            rs.getString("name"),
                            rs.getString("student_id"),
                            rs.getString("year_level"),
                            rs.getString("section")
                    });
                } else if (tableName.equals("teachers")) {
                    tableModel.addRow(new Object[]{
                            rs.getString("name"),
                            rs.getString("employeeId"),
                            rs.getString("department")
                    });
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
