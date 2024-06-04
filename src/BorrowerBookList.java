import javax.swing.*;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BorrowerBookList {
	private static DefaultTableModel tableModel;
    public static void main(String[] args) {
    	// Register the MySQL database driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Database driver not found", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SwingUtilities.invokeLater(BorrowerBookList::createAndShowGUI);
    }

    static void createAndShowGUI() {
        // Load the background image
        ImageIcon backgroundImage = new ImageIcon("./assets/book_list_borrower.png");

        // Create and configure the frame
        JFrame frame = new JFrame("Book List For Borrower");
        frame.setSize(1024, 768);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Create a layered pane to hold components
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(1024, 768));

        // Background label for the image
        JLabel backgroundLabel = new JLabel(backgroundImage);
        backgroundLabel.setBounds(0, 0, backgroundImage.getIconWidth(), backgroundImage.getIconHeight());
        layeredPane.add(backgroundLabel, JLayeredPane.DEFAULT_LAYER);

        // Create a panel to hold other components
        JPanel panel = new JPanel();
        panel.setOpaque(false); // Ensure panel is transparent
        panel.setLayout(null); // Use null layout for precise positioning
        panel.setBounds(0, 0, 1024, 768);

        // Create a custom JComboBox with the renderer
        JComboBox<String> customComboBox = new JComboBox<>();
        customComboBox.setUI(new CustomComboBoxUI());
        customComboBox.setRenderer(new CustomComboBoxRenderer());
        customComboBox.addItem("Category"); // Initial item to display "CATEGORY"
        customComboBox.addItem("Non-Fictional");
        customComboBox.addItem("Fictional");
        customComboBox.addItem("Academic");
        customComboBox.setBounds(77, 212, 164, 36); // Adjust position and size as needed
        // Set font for JComboBox
        customComboBox.setFont(new Font("Raleway", Font.PLAIN, 18));
        customComboBox.addActionListener(e -> {
            String selectedCategory = (String) customComboBox.getSelectedItem();
            if (selectedCategory != null) {
                // Reload the table data based on the selected category
                List<Book> filteredBooks = filterBooksByCategory(selectedCategory);
                refreshTable(tableModel, filteredBooks);
            }
        });
        // Add panel to layered pane
        layeredPane.add(panel, JLayeredPane.PALETTE_LAYER);
        panel.add(customComboBox);

        // Add BackButton image
        ImageIcon backButtonImage = new ImageIcon("./assets/back_button.png");
        JLabel backButtonLabel = new JLabel(backButtonImage);
        backButtonLabel.setBounds(804, 0, 220, 141);
        backButtonLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                frame.dispose();
                new UserMenu();
            }
        });
        panel.add(backButtonLabel);

        // Set the layered pane as the content pane
        frame.setContentPane(layeredPane);

        // Create the table with empty data
        String[] columnNames = {"Title", "ISBN", "Category", "Author", "Year Published", "Publisher", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                Component component = super.prepareRenderer(renderer, row, column);
                component.setBackground(new Color(204, 169, 141)); // Set background color for all cells
                return component;
            }
        };
        table.setCellSelectionEnabled(true);
        table.setShowGrid(true);
        table.setOpaque(false); // Make table transparent
        table.setGridColor(new Color(204, 169, 141));

        // Set custom header renderer for the table
        CustomTableHeaderRenderer customHeaderRenderer = new CustomTableHeaderRenderer();
        table.getTableHeader().setDefaultRenderer(customHeaderRenderer);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBounds(97, 303, 826, 367);
        panel.add(scrollPane);

        // Load book data and populate the table
        List<Book> books = loadBooksFromDatabase();
        populateTable(tableModel, books);

        // Make frame visible
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
    }

    static class CustomComboBoxUI extends BasicComboBoxUI {
        private static final Color BACKGROUND_COLOR = new Color(148, 116, 94);

        @Override
        protected JButton createArrowButton() {
            JButton button = new BasicArrowButton(BasicArrowButton.SOUTH) {
                private boolean clicked = false;

                @Override
                protected void fireActionPerformed(ActionEvent e) {
                    if (!clicked) {
                        JComboBox<?> comboBox = (JComboBox<?>) getParent();
                        // if (comboBox.getItemCount() > 0 && comboBox.getItemAt(0).equals("Category")) {
                        //     comboBox.removeItemAt(0); // Remove "Category" if it's the first item
                        // }
                        clicked = true;
                    }
                    super.fireActionPerformed(e);
                }
            };
            button.setBackground(BACKGROUND_COLOR);
            return button;
        }

        @Override
        public void paintCurrentValue(Graphics g, Rectangle bounds, boolean hasFocus) {
            g.setColor(BACKGROUND_COLOR); // Set the custom background color for the selected item
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
            super.paintCurrentValue(g, bounds, hasFocus);
        }

        @Override
        public void paintCurrentValueBackground(Graphics g, Rectangle bounds, boolean hasFocus) {
            g.setColor(BACKGROUND_COLOR); // Set the custom background color
            g.fillRect(bounds.x, bounds.y, bounds.width, bounds.height);
        }
    }

    static class CustomComboBoxRenderer extends DefaultListCellRenderer {
        private static final Color BACKGROUND_COLOR = new Color(148, 116, 94);
        private static final Color TEXT_COLOR = Color.WHITE;

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value, int index,
                                                      boolean isSelected, boolean cellHasFocus) {
            if (value == null) {
                return new JLabel(); // Return empty JLabel for "Category"
            }

            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            label.setOpaque(true);
            label.setForeground(TEXT_COLOR);
            label.setBackground(isSelected ? BACKGROUND_COLOR.darker() : BACKGROUND_COLOR); // Set the custom background color
            label.setHorizontalAlignment(CENTER); // Center the text
            label.setText(value.toString());

            return label;
        }
    }

    static class CustomTableHeaderRenderer extends DefaultTableCellRenderer {
        private static final Color BACKGROUND_COLOR = new Color(148, 116, 94);
        private static final Font TABLE_FONT = new Font("Raleway", Font.BOLD, 18);

        public CustomTableHeaderRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER); // Center align text
            setFont(TABLE_FONT); // Set font
            setBackground(BACKGROUND_COLOR); // Set background color
            setForeground(Color.WHITE); // Set text color
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            setText(value.toString()); // Set the column name text
            return this;
        }
    }

    static class CustomTableCellRenderer extends DefaultTableCellRenderer {
        private static final Color FIRST_ROW_COLOR = new Color(204, 169, 141);
        private static final Color OTHER_ROWS_COLOR = new Color(204, 169, 141);
        private static final Color BORDER_COLOR = FIRST_ROW_COLOR;
        private static final Font TABLE_FONT = new Font("Raleway", Font.BOLD, 18);

        public CustomTableCellRenderer() {
            setHorizontalAlignment(SwingConstants.CENTER); // Center align text
            setFont(TABLE_FONT); // Set font
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Set background color based on the row index
            setBackground(row % 2 == 0 ? FIRST_ROW_COLOR : OTHER_ROWS_COLOR);
            setForeground(Color.WHITE); // Set text color for all rows
            setBackground(FIRST_ROW_COLOR);
            setBorder(BorderFactory.createLineBorder(BORDER_COLOR)); // Set border color
            return this;
        }
    }

    static class Book {
        private String title;
        private String isbn;
        private String category;
        private String author;
        private String yearPublished;
        private String publisher;
        private String status;

        public Book(String title, String isbn, String category, String author, String yearPublished,
                    String publisher, String status) {
            this.title = title;
            this.isbn = isbn;
            this.category = category;
            this.author = author;
            this.yearPublished = yearPublished;
            this.publisher = publisher;
            this.status = status;
        }

        public String getTitle() { return title; }
        public String getIsbn() { return isbn; }
        public String getCategory() { return category; }
        public String getAuthor() { return author; }
        public String getYearPublished() { return yearPublished; }
        public String getPublisher() { return publisher; }
        public String getStatus() { return status; }
    }

    private static List<Book> loadBooksFromDatabase() {
        List<Book> books = new ArrayList<>();
        String query = "SELECT title, ISBN, category, author, year_published, publisher, status FROM books";

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/alfombroarchives", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                String title = resultSet.getString("title");
                String isbn = resultSet.getString("ISBN");  // Change to "ISBN" (case-sensitive)
                String category = resultSet.getString("category");
                String author = resultSet.getString("author");
                String yearPublished = resultSet.getString("year_published");  // Change to "year_published"
                String publisher = resultSet.getString("publisher");
                String status = resultSet.getString("status");

                books.add(new Book(title, isbn, category, author, yearPublished, publisher, status));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }


    private static void populateTable(DefaultTableModel tableModel, List<Book> books) {
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                    book.getTitle(),
                    book.getIsbn(),
                    book.getCategory(),
                    book.getAuthor(),
                    book.getYearPublished(),
                    book.getPublisher(),
                    book.getStatus()
            });
        }
    }
 // Define a method to filter books by category
    private static List<Book> filterBooksByCategory(String category) {
        List<Book> filteredBooks = new ArrayList<>();
        // Query the database to fetch books of the selected category
        String query = "";
        if(category != "Category") {
            query = "SELECT title, ISBN, category, author, year_published, publisher, status FROM books WHERE category=?";
        } else {
            query = "SELECT title, ISBN, category, author, year_published, publisher, status FROM books";
        }

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/alfombroarchives", "root", "");
            PreparedStatement statement = connection.prepareStatement(query)) {
            if(category != "Category") {
                statement.setString(1, category);
            }
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    String title = resultSet.getString("title");
                    String isbn = resultSet.getString("ISBN");
                    String author = resultSet.getString("author");
                    String yearPublished = resultSet.getString("year_published");
                    String publisher = resultSet.getString("publisher");
                    String status = resultSet.getString("status");
                    filteredBooks.add(new Book(title, isbn, category, author, yearPublished, publisher, status));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filteredBooks;
    }
 // Define a method to refresh the table with new data
    private static void refreshTable(DefaultTableModel tableModel, List<Book> books) {
        tableModel.setRowCount(0); // Clear existing data
        for (Book book : books) {
            tableModel.addRow(new Object[]{
                    book.getTitle(),
                    book.getIsbn(),
                    book.getCategory(),
                    book.getAuthor(),
                    book.getYearPublished(),
                    book.getPublisher(),
                    book.getStatus()
            });
        }
    }
}
