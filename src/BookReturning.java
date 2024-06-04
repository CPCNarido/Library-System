import java.awt.AlphaComposite;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Font;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JComboBox; 

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import javax.swing.JLabel;

class TransparentImageButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Image image;

	public TransparentImageButton(ImageIcon icon) {
		super(icon);
		this.image = icon.getImage();
		setOpaque(false);
		setContentAreaFilled(false);
		setBorderPainted(false);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, getAlpha()));
		super.paintComponent(g2);
		g2.dispose();
	}

	private float getAlpha() {
		return isEnabled() ? 1.0f : 0.5f; // Adjust the alpha value based on button state (enabled/disabled)
	}
}

class BorrowedBook {
	private String isbn;
	private Date dueDate;
	private boolean isPaid;

	public BorrowedBook(Date dueDate, boolean isPaid, String isbn) {
		this.dueDate = dueDate;
		this.isPaid = isPaid;
		this.isbn = isbn;
	} 

	public boolean isPaid() {
		return isPaid;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public String getBookISBN() {
		return isbn;
	}
}

public class BookReturning {
	private Connection connection;

	private JFrame frame;

	/**
	 * Launch the application.
	 * 
	 * 
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					BookReturning window = new BookReturning();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private Image backgroundImage;
	private JTextField name;
	private JTextField dueDate;
	private JTextField dateReturned;
	private JButton payPenaltyBtn;
	private JComboBox<String> bookSelector;
	private JLabel payPenaltylbl;

	/**
	 * Returns the names of the books borrowed by the user
	 */
	public String[] booksBorrowed(int user_id) {
		String[] books = {};
		try {
			String query = "SELECT title FROM books_borrowed BB JOIN books B ON B.ISBN = BB.book_isbn WHERE borrower_id = ?";
			PreparedStatement statement = connection.prepareStatement(query);
			statement.setInt(1, user_id);
			ResultSet result = statement.executeQuery();

			List<String> bookList = new ArrayList<>();
			while(result.next()) {
				String bookName = result.getString("title");
				bookList.add(bookName);
			}
			books = bookList.toArray(new String[0]);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return books;
	}

	public BorrowedBook getBook(String bookName) {
		String query = "SELECT due_date, is_paid, book_isbn FROM books_borrowed BB JOIN books B ON B.ISBN = BB.book_isbn WHERE borrower_id = ? AND title = ?";
		try (PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, User.id);
			statement.setString(2, bookName);
			try (ResultSet result = statement.executeQuery()) {
				if (result.next()) {
					Date dueDate = result.getDate("due_date");
					boolean isPaid = result.getBoolean("is_paid");
	
					// IF THERE IS NO DUE DATE THEN THERE IS NO PAYMENT
					if (dueDate == null && !isPaid || dueDate.compareTo(new Date(System.currentTimeMillis())) >= 0) {
						isPaid = true;
					}
	
					return new BorrowedBook(dueDate, isPaid, bookName);
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error getting book", e);
		}
		return null;
	}

	public void returnBook(String title) {
		String isbnQuery = "SELECT isbn FROM books WHERE title = ?";
		try (PreparedStatement isbnStatement = connection.prepareStatement(isbnQuery)) {
			isbnStatement.setString(1, title);
			try (ResultSet resultSet = isbnStatement.executeQuery()) {
				if (resultSet.next()) {
					String isbn = resultSet.getString("isbn");

					String deleteQuery = "DELETE FROM books_borrowed WHERE borrower_id = ? AND book_isbn = ?";
					try (PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery)) {
						deleteStatement.setInt(1, User.id);
						deleteStatement.setString(2, isbn);

						int rowsDeleted = deleteStatement.executeUpdate();
						if (rowsDeleted > 0) {
							JOptionPane.showMessageDialog(frame, "You have successfully returned this book!");
						}
					}
				} else {
					JOptionPane.showMessageDialog(frame, "Book not found.");
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException("Error returning book", e);
		}
	}

	public JPanel setBackgroundPanel(String imageFilePath) {
		try {
			File imageFile = new File(imageFilePath);
			if (imageFile.exists()) {
				backgroundImage = ImageIO.read(imageFile);
			} else {
				System.err.println("Background image file not found!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		final Image finalBackgroundImage = backgroundImage;
		JPanel backgroundPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.drawImage(finalBackgroundImage, 0, 0, null);
			}
		};

		
		name = new JTextField(User.name);
		name.setBounds(377, 419, 503, 30);
		backgroundPanel.add(name);
		name.setColumns(10);
		
		dueDate = new JTextField();
		dueDate.setColumns(10);
		dueDate.setBounds(377, 503, 503, 30);
		backgroundPanel.add(dueDate);
		
		dateReturned = new JTextField();
		dateReturned.setColumns(10);
		dateReturned.setBounds(377, 545, 503, 30);
		backgroundPanel.add(dateReturned);
		ImageIcon buttonIcon = null;
        try {
            buttonIcon = new ImageIcon(ImageIO.read(new File("./assets/back_button.png")));
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Button image file not found!");
        }
		
		// Create a button with the image icon
        TransparentImageButton backBtn = new TransparentImageButton(buttonIcon);

        // Set button bounds manually for precise placement
        backBtn.setBounds((1024 - buttonIcon.getIconWidth()), 0, buttonIcon.getIconWidth(), buttonIcon.getIconHeight());
		backBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // Add an action listener to handle the button click
        backBtn.addActionListener(e -> {
			// OPEN MAIN MENU
			// if(User.user_type.equals("admin")) {

			// } else {

			// }
		});

		// Add the button to the background panel
		backgroundPanel.add(backBtn);
		backgroundPanel.setLayout(null);
		
		JButton returnBookBtn = new JButton("Return");
		returnBookBtn.setForeground(new Color(255, 255, 255));
		returnBookBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (bookSelector.getSelectedItem() != null) { 
					String bookSelected = (String) bookSelector.getSelectedItem();
					returnBook(bookSelected); 
					bookSelector.removeItem(bookSelected);
					dueDate.setText("");
					dateReturned.setText("");
				} else {
					JOptionPane.showMessageDialog(frame, "Please select a book to return.");
				}
			}
		});
		returnBookBtn.setBackground(new Color(92, 56, 44));
		returnBookBtn.setFont(new Font("Tahoma", Font.BOLD, 24));
		returnBookBtn.setBounds(744, 620, 166, 38);
		returnBookBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		// returnBookBtn.setEnabled(false);
		backgroundPanel.add(returnBookBtn);
		
		payPenaltyBtn = new JButton("Pay Penalty");
		payPenaltyBtn.setForeground(Color.WHITE);
		payPenaltyBtn.setFont(new Font("Tahoma", Font.BOLD, 24));
		payPenaltyBtn.setBackground(new Color(158, 99, 81));
		payPenaltyBtn.setBounds(546, 620, 177, 38);
		payPenaltyBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
		payPenaltyBtn.setEnabled(false);
		backgroundPanel.add(payPenaltyBtn);
		
		String[] bookNames = booksBorrowed(User.id);
		 bookSelector = new JComboBox<>(bookNames);
		bookSelector.setSelectedIndex(-1);
		bookSelector.setBounds(377, 461, 503, 30);

		bookSelector.addItemListener(e -> {
			String selectedBook = (String) bookSelector.getSelectedItem();
			BorrowedBook book = getBook(selectedBook);
			if (selectedBook != null) {
				if (book.isPaid()) {
					returnBookBtn.setEnabled(true);
					payPenaltyBtn.setEnabled(false);
				} else {
					payPenaltyBtn.setEnabled(true);
					returnBookBtn.setEnabled(false);
					payPenaltylbl.setVisible(true);
				}

				String dueDateStr = book.getDueDate() == null ? "No Due Date" : book.getDueDate().toString();

				dueDate.setText(dueDateStr);
				dateReturned.setText(new Date(System.currentTimeMillis()).toString());
			}
		});

		backgroundPanel.add(bookSelector);
		

		payPenaltylbl = new JLabel("<html>You have unpaid dues please pay <br>them to be able to return the book</html>");
		payPenaltylbl.setForeground(new Color(59, 28, 17));
		payPenaltylbl.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 16));
		payPenaltylbl.setBounds(135, 620, 351, 38);
		payPenaltylbl.setVisible(false);
		backgroundPanel.add(payPenaltylbl);
		
		return backgroundPanel;
	}

	/**
	 * Create the application.
	 */
	public BookReturning() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
        frame = new JFrame("Alfombros Archives");
        frame.setResizable(false);
        frame.setSize(1024, 768);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Connect to the database
		try {
			String url = "jdbc:mysql://localhost/alfombroarchives";
			String user = "root";
			String password = "";
			connection = DriverManager.getConnection(url, user, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		frame.setContentPane(setBackgroundPanel("./assets/book_returning.png"));
	}
}
