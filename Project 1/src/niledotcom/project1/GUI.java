// Using WindowsBuilder and Swing

package niledotcom.project1;

import java.awt.EventQueue;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class GUI extends JFrame 
{

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField inputQuantity;
	private JTextField inputID;
	private JTextField displayDetails;
	private JTextField displaySubtotal;
	private JTextField infoItem1;
	private JTextField infoItem2;
	private JTextField infoItem5;
	private JTextField infoItem4;
	private JTextField infoItem3;
	private JPanel userControls_1;
	private JButton buttonExit;
	private JButton buttonEmpty;
	private JButton buttonCheckOut;
	private JButton buttonView;
	private JButton buttonAdd;
	private JButton buttonSearch;

	private Logic logic; // Instance of the Logic class
	private JLabel promptID; // Define JLabel globally
	private JLabel promptQuantity; // Define JLabel globally
	private JLabel labelSC; // Define JLabel globally
	private JLabel labelSubtotal; // Define JLabel globally
	private JLabel labelDetails; // Define JLabel globally

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI frame = new GUI();
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
	public GUI() {
		// Initialize logic for GUI functionality
		logic = new Logic();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 740, 480);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);

		JPanel userInput = new JPanel();
		JPanel shoppingCart = new JPanel();
		contentPane.setLayout(new GridLayout(0, 1, 0, 0));
		contentPane.add(userInput);
		userInput.setLayout(new GridLayout(4, 2, 0, 0));
		shoppingCart.setLayout(new GridLayout(6, 1, 0, 0));

		// Initialize labels and input fields for ID and Quantity
		promptID = new JLabel("Enter ID for item #1:");
		promptID.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		promptID.setHorizontalAlignment(JTextField.CENTER);
		userInput.add(promptID);

		// Field to input the ID
		inputID = new JTextField();
		promptID.setLabelFor(inputID);
		userInput.add(inputID);
		inputID.setColumns(10);

		// Label to ask for quantity
		promptQuantity = new JLabel("Enter quantity for item #1:");
		promptQuantity.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		promptQuantity.setHorizontalAlignment(JTextField.CENTER);
		userInput.add(promptQuantity);

		// Field to input the quantity
		inputQuantity = new JTextField();
		userInput.add(inputQuantity);
		inputQuantity.setColumns(10);

		// Label to display details
		labelDetails = new JLabel("Details for item #1:");
		labelDetails.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		labelDetails.setHorizontalAlignment(JTextField.CENTER);
		userInput.add(labelDetails);

		// Field to dynamically display details
		displayDetails = new JTextField();
		displayDetails.setEditable(false);
		userInput.add(displayDetails);
		displayDetails.setColumns(10);

		// Label to display subtotal
		labelSubtotal = new JLabel("Current subtotal for 0 item(s):");
		labelSubtotal.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		labelSubtotal.setHorizontalAlignment(JTextField.CENTER);
		userInput.add(labelSubtotal);

		// Label to dynamically display subtotal
		displaySubtotal = new JTextField();
		displaySubtotal.setEditable(false);
		userInput.add(displaySubtotal);
		displaySubtotal.setColumns(10);
		contentPane.add(shoppingCart);

		labelSC = new JLabel("Your shopping cart is currently empty :(");
		labelSC.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		labelSC.setHorizontalAlignment(JTextField.CENTER);
		shoppingCart.add(labelSC);

		infoItem1 = new JTextField();
		shoppingCart.add(infoItem1);
		infoItem1.setColumns(10);
		infoItem1.setEditable(false);

		infoItem2 = new JTextField();
		infoItem2.setColumns(10);
		shoppingCart.add(infoItem2);
		infoItem2.setEditable(false);

		infoItem3 = new JTextField();
		infoItem3.setColumns(10);
		shoppingCart.add(infoItem3);
		infoItem3.setEditable(false);

		infoItem4 = new JTextField();
		infoItem4.setColumns(10);
		shoppingCart.add(infoItem4);
		infoItem4.setEditable(false);

		infoItem5 = new JTextField();
		infoItem5.setColumns(10);
		shoppingCart.add(infoItem5);
		infoItem5.setEditable(false);

		JPanel userControls = new JPanel();
		contentPane.add(userControls);
		userControls.setLayout(new GridLayout(2, 1, 0, 0));

		JLabel labelUC = new JLabel("USER CONTROLS");
		labelUC.setHorizontalAlignment(SwingConstants.CENTER);
		labelUC.setFont(new Font("Lucida Grande", Font.PLAIN, 12));
		userControls.add(labelUC);

		userControls_1 = new JPanel();
		userControls.add(userControls_1);
		userControls_1.setLayout(new GridLayout(3, 2, 0, 0));

		buttonSearch = new JButton("Search for item #1");
		userControls_1.add(buttonSearch);

		buttonAdd = new JButton("Add item #1 to cart\n");
		userControls_1.add(buttonAdd);
		buttonAdd.setEnabled(false);

		buttonView = new JButton("View cart");
		userControls_1.add(buttonView);
		buttonView.setEnabled(false);

		buttonCheckOut = new JButton("Check out");
		userControls_1.add(buttonCheckOut);
		buttonCheckOut.setEnabled(false);

		buttonEmpty = new JButton("Empty cart - Start a new order");
		userControls_1.add(buttonEmpty);

		buttonExit = new JButton("Exit (Close App)");
		userControls_1.add(buttonExit);

		// Add the action listeners
		addActionListeners();
	}

	// Method that adds action listeners to the buttons
	private void addActionListeners() 
	{
		// Action: search for an item
		buttonSearch.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				// Check if the item ID input is empty
				String itemId = inputID.getText().trim();
				if (itemId.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please input the item ID");
					return; // Exit the method if input is invalid
				}

				// Check if the quantity input is empty before parsing
				String quantityInput = inputQuantity.getText().trim();
				if (quantityInput.isEmpty()) {
					JOptionPane.showMessageDialog(null, "Please input the quantity");
					return; // Exit the method if input is invalid
				}

				// Now it's safe to parse the quantity
				int quantity = Integer.parseInt(quantityInput);

				String result = logic.searchItem(itemId, quantity);

				// If there's no stock at all
				if (result == "Out of stock")
				{
					JOptionPane.showMessageDialog(null, "Sorry... that item is out of stock, please try another item");
					inputID.setText(""); // Display item details in the GUI
					inputQuantity.setText(""); // Display item details in the GUI
					return;
				} 
				// If there's partial stock available
				else if (result == "Insufficient stock") 
				{
					JOptionPane.showMessageDialog(null, "Insufficient stock. Only " + logic.getStock(itemId)
							+ " in hand. Please reduce the quantity");
					inputQuantity.setText("");
					return;
				} 
				// If item was not found at all
				else if (result == "Item not found") 
				{
					JOptionPane.showMessageDialog(null, "Item ID " + itemId + " not in file");
					return;
				}

				// Enable the user to add the item to the cart
				buttonAdd.setEnabled(true);
				displayDetails.setText(result);
			}
		});

		// Action: add item to cart
		buttonAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String itemId = inputID.getText();
				int quantity = Integer.parseInt(inputQuantity.getText());
				logic.addItemToCart(itemId, quantity);
				updateGUI();
				buttonAdd.setEnabled(false);
				inputID.setText("");
				inputQuantity.setText("");
			}
		});

		// Action: view current cart
		buttonView.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String currentCart = logic.viewCart();
				JOptionPane.showMessageDialog(null, currentCart); // Display the invoice
			}
		});

		// Action: check out
		buttonCheckOut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String invoice = logic.checkOut();
				JOptionPane.showMessageDialog(null, invoice); // Display the invoice
				buttonCheckOut.setEnabled(false);
				disableInputs();
			}
		});

		// Action: empty cart and start a new order
		buttonEmpty.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logic = new Logic(); // Reset the logic (clear cart)
				clearCartDisplay(); // Clear the cart display
			}
		});

		// Action: exit the application
		buttonExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
	}

	// Method that updates the GUI as the customer adds item to the order. Wrapper for several helper methods
	private void updateGUI() 
	{
		// Get item count and cart size
		int itemCount = logic.getCartSize();
		int totalItems = logic.getTotalItems();
		
		// Call the different helper methods to update the GUI
		updateText(itemCount);
		updateInputs(itemCount);
		updateCartDisplay();
		updateCartLabel(itemCount, totalItems);
		updateSubtotal(itemCount, totalItems);
	}

	// Method that updates the counter for items (wheverer there's an item counter)
	private void updateText(int itemCount) {
		promptID.setText("Enter ID for item #" + (itemCount + 1) + ":");
		promptQuantity.setText("Enter quantity for item #" + (itemCount + 1) + ":");
		labelDetails.setText("Details for item #" + (itemCount + 1));
		buttonSearch.setText("Search for item #" + (itemCount + 1));
		buttonAdd.setText("Add item #" + (itemCount + 1) + " to cart");
	}

	// Method that updates the inputs based on the item count (max 5)
	private void updateInputs(int itemCount) 
	{
		// Make sure there's something in the cart before checking out and viewing cart is possible
		if (itemCount >= 1) {
			buttonCheckOut.setEnabled(true);
			buttonView.setEnabled(true);
		}
		// Cart is full at this point
		if (itemCount >= 5) 
		{
			disableInputs();
		}
	}

	// Method that disables the inputs (when the cart can't hold more items)
	private void disableInputs() {
		inputID.setEnabled(false);
		inputQuantity.setEnabled(false);
		buttonAdd.setEnabled(false); 
		buttonSearch.setEnabled(false);
	}

	// Method that updates the subtotal displayed
	private void updateSubtotal(int itemCount, int totalItems) {
		labelSubtotal.setText("Current subtotal for " + totalItems + " item(s):");
		displaySubtotal.setText("$" + logic.getTotalPrice()); // Value is only used here, no need to pass it as an argument
	}

	// Method that updates the cart's display
	private void updateCartDisplay() 
	{
		// Load cart's information
		JTextField[] itemFields = { infoItem1, infoItem2, infoItem3, infoItem4, infoItem5 };
		String[] cartItems = logic.currentOrder().split("\n");

		// Loop through cart
		for (int i = 0; i < itemFields.length; i++) 
		{
			// Fill in each item's information
			if (i < cartItems.length) 
			{
				itemFields[i].setText(cartItems[i]);
			} 
			// Leave unocuppied spaces empty
			else 
			{
				itemFields[i].setText("");
			}
		}
	}

	// Method that updates the cart label (that indicates how many items its holding)
	private void updateCartLabel(int itemCount, int totalItems) 
	{
		// Empty
		if (itemCount == 0) 
		{
			labelSC.setText("Your shopping cart is currently empty :(");
		} 
		// Has items but still not full
		else if (itemCount < 5) 
		{
			labelSC.setText("Your shopping cart currently contains " + totalItems + " item(s).");
		} 
		// Has items and is full
		else if (itemCount == 5) {
			labelSC.setText("Your shopping cart currently contains " + totalItems + " item(s). Cart is now full.");
		}
	}

	// Method that clears the current cart and starts a new order
	private void clearCartDisplay()
	{
		// Dispose of the current frame
		this.dispose();

		// Create a new instance of the GUI (to start a new order)
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				GUI frame = new GUI();
				frame.setVisible(true);
			}
		});
	}
}
