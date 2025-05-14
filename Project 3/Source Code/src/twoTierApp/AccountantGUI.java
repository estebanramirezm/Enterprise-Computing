/*
* CNT 4714 Enterprise Computing - Project 3: A Specialized Accountant Application
* Description: Implements the accountant GUI version of the application (for operations log)
* Author: Esteban Ramirez
* Date: November 3rd , 2024
* Class: AccountantGUI
*/

package twoTierApp;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;

public class AccountantGUI extends JFrame 
{
    private JPanel contentPane;
    private AppLogic appLogic;

    private JButton connectButton, disconnectButton, executeSQL, clearSQL, clearResults, closeApp;
    private JLabel connectionStatusLabel;
    private JTextField usernameInput;
    private JPasswordField passwordInput;
    private JTextArea sqlCommandTextArea;
    private JTable resultTable;
    private JScrollPane scrollPane;
    private JComboBox<String> urlDropdown, userDropdown;
    
    public AccountantGUI() 
    {
    	// Initialize JFrame
        setTitle("Accountant Application");
        appLogic = new AppLogic();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setBounds(100, 100, 850, 700);  // Adjust size as needed
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        // Layout
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[]{0, 0};  // Two columns
        gbl_contentPane.rowHeights = new int[]{0, 0, 0, 0, 0};  // Adjust for multiple rows
        gbl_contentPane.columnWeights = new double[]{0.2, 0.5};  // Equal column widths
        gbl_contentPane.rowWeights = new double[]{0.1, 0.3, 0.02, 0.02, 0.4};  // Adjust row proportions to reduce button panel height
        contentPane.setLayout(gbl_contentPane);

        // Row 1 - Header labels
        
	    	// Label
	        JLabel connectionDetailsHeader = new JLabel("Connection Details", JLabel.CENTER);
	       
		        // Layout
		        GridBagConstraints gbc_connectionDetailsHeader = new GridBagConstraints();
		        gbc_connectionDetailsHeader.gridx = 0;
		        gbc_connectionDetailsHeader.gridy = 0;
		        gbc_connectionDetailsHeader.fill = GridBagConstraints.BOTH;
		        contentPane.add(connectionDetailsHeader, gbc_connectionDetailsHeader);

		// Label
        JLabel sqlCommandHeader = new JLabel("Enter an SQL Command", JLabel.CENTER);
        
	        // Layout
	        GridBagConstraints gbc_sqlCommandHeader = new GridBagConstraints();
	        gbc_sqlCommandHeader.gridx = 1;
	        gbc_sqlCommandHeader.gridy = 0;
	        gbc_sqlCommandHeader.fill = GridBagConstraints.BOTH;
	        contentPane.add(sqlCommandHeader, gbc_sqlCommandHeader);

	    // Row 2 - Connection Details (labels and inputs)
        JPanel connectionPanel = new JPanel();
        connectionPanel.setLayout(new GridLayout(4, 2, 5, 5));  // 4 rows for labels and inputs
        
        // Column 1 - Database and User Selection / Command Input
        
	        // Label
	        connectionPanel.add(new JLabel("DB URL Properties:"));

		        // Non-editable URL
			    JTextField urlTextArea = new JTextField("operationslog.properties");
		        urlTextArea.setEditable(false);
		        urlTextArea.setBackground(new Color(220, 220, 220));
		        connectionPanel.add(urlTextArea);

			// Label
	        connectionPanel.add(new JLabel("User Properties:"));

			    // Non-editable user properties
		        JTextField userTextArea = new JTextField("theaccountant.properties");
		        userTextArea.setEditable(false);
		        userTextArea.setBackground(new Color(220, 220, 220));
		        connectionPanel.add(userTextArea);
      
			// Label
		    connectionPanel.add(new JLabel("Username:"));
		        
		        	// Username input
			        usernameInput = new JTextField();
			        connectionPanel.add(usernameInput);
		        
			// Label 
			connectionPanel.add(new JLabel("Password:"));
		        
			        // Password input
			        passwordInput = new JPasswordField();
			        connectionPanel.add(passwordInput);
        
		    // Layout
	        GridBagConstraints gbc_connectionPanel = new GridBagConstraints();
	        gbc_connectionPanel.gridx = 0;
	        gbc_connectionPanel.gridy = 1;
	        gbc_connectionPanel.fill = GridBagConstraints.BOTH;
	        contentPane.add(connectionPanel, gbc_connectionPanel);

	    // Column 2 - SQL Command Input
	        
	        // Panel
	        JPanel sqlCommandPanel = new JPanel();
	        sqlCommandPanel.setLayout(new BorderLayout());
	      
			    // Command input
		        sqlCommandTextArea = new JTextArea(5, 20);
		        sqlCommandPanel.add(sqlCommandTextArea, BorderLayout.CENTER);
		        sqlCommandPanel.setBorder(new EmptyBorder(10, 10, 5, 10));
		        sqlCommandTextArea.setEditable(false);
		        sqlCommandTextArea.setBackground(new Color(220, 220, 220)); 
		        sqlCommandTextArea.setLineWrap(true);
		        sqlCommandTextArea.setWrapStyleWord(true);
       
			// Layout
	        GridBagConstraints gbc_sqlCommandPanel = new GridBagConstraints();
	        gbc_sqlCommandPanel.gridx = 1;
	        gbc_sqlCommandPanel.gridy = 1;
	        gbc_sqlCommandPanel.fill = GridBagConstraints.BOTH;
	        contentPane.add(sqlCommandPanel, gbc_sqlCommandPanel);

	    // Row 3
	        
	        // Subrow 1 - Buttons
	        JPanel buttonPanel = new JPanel();
	        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 3, 5));
        
		        // Connect button
		        connectButton = new JButton("Connect to Database");
		        buttonPanel.add(connectButton);
		        
		        // Disconnect button
		        disconnectButton = new JButton("Disconnect from Database");
		        buttonPanel.add(disconnectButton);
		        disconnectButton.setEnabled(false);

	        // Spacing
	        Component rigidArea = Box.createRigidArea(new Dimension(20, 1)); // w, h
	        buttonPanel.add(rigidArea);

	        // Clear SQL button
	        clearSQL = new JButton("Clear SQL Command");
	        buttonPanel.add(clearSQL);
	        clearSQL.setEnabled(false);
	
	        // Execute SQL button
	        executeSQL = new JButton("Execute SQL Command");
	        buttonPanel.add(executeSQL);
	        executeSQL.setEnabled(false);

	        // Layout
	        GridBagConstraints gbc_buttonPanel = new GridBagConstraints();
	        gbc_buttonPanel.gridx = 0;
	        gbc_buttonPanel.gridy = 2;
	        gbc_buttonPanel.gridwidth = 2;  // Span across both columns
	        gbc_buttonPanel.fill = GridBagConstraints.BOTH;
	        contentPane.add(buttonPanel, gbc_buttonPanel);

	    // Subrow 2 - Connection status

	        // Label
	        connectionStatusLabel = new JLabel("NO CONNECTION ESTABLISHED", JLabel.CENTER);
	        connectionStatusLabel.setForeground(Color.RED);
	        connectionStatusLabel.setOpaque(true);
	        connectionStatusLabel.setBackground(Color.BLACK);
        
	        // Layout
	        GridBagConstraints gbc_connectionStatusLabel = new GridBagConstraints();
	        gbc_connectionStatusLabel.gridx = 0;
	        gbc_connectionStatusLabel.gridy = 3;
	        gbc_connectionStatusLabel.gridwidth = 2;  // Span across both columns
	        gbc_connectionStatusLabel.fill = GridBagConstraints.HORIZONTAL;
	        contentPane.add(connectionStatusLabel, gbc_connectionStatusLabel);

	    // Row 4 - SQL Result panel
        JPanel resultPanel = new JPanel();
        resultPanel.setLayout(new BorderLayout());
        
	        // Subrow 1 - SQL Result Window
        
	        	// Label
		        resultPanel.add(new JLabel("SQL Execution Result Window", JLabel.CENTER), BorderLayout.NORTH);
		        
			        // Result panel (table)
			        resultTable = new JTable();
			        scrollPane = new JScrollPane(resultTable);
			        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
			        scrollPane.setPreferredSize(new Dimension(580, 350));
			        resultPanel.add(scrollPane, BorderLayout.CENTER);

        // Subrow 2 - SQL Result Buttons
			        
			// Buttons
	        JPanel resultButtonPanel = new JPanel();
	        resultButtonPanel.setLayout(new GridLayout(1, 2, 5, 5));  // 1 row, 2 buttons
        
		        // Clear result button
		        clearResults = new JButton("Clear Result Window");
		        resultButtonPanel.add(clearResults);
		        
		        // Close app button
		        closeApp = new JButton("Close Application");
		        resultButtonPanel.add(closeApp);
                
	        resultPanel.add(resultButtonPanel, BorderLayout.SOUTH);
        
	    // Layout
        GridBagConstraints gbc_resultPanel = new GridBagConstraints();
        gbc_resultPanel.gridx = 0;
        gbc_resultPanel.gridy = 4;
        gbc_resultPanel.gridwidth = 2;  // Span across both columns
        gbc_resultPanel.fill = GridBagConstraints.BOTH;
        contentPane.add(resultPanel, gbc_resultPanel);
        
        // Add action listeners
        addDynamic();    
    }
    
    // Function that adds actionListeners to the buttons for app functionality
    public void addDynamic()
    {
    	// Action listener for connect button
    	connectButton.addActionListener(new ActionListener() 
    	{
    	    public void actionPerformed(ActionEvent e) 
    	    {
    	        // Get input data
    	        String expectedUser = "theaccountant.properties";
    	        String enteredUsername = usernameInput.getText().trim();
    	        String enteredPassword = new String(passwordInput.getPassword());
    	        String databaseName = "operationslog"; // Fixed database name
    	        
    	        // Check credentials
    	        if (!expectedUser.equals(enteredUsername + ".properties")) 
    	        {
    	        	// Update connection label - mismatch failure
    	            connectionStatusLabel.setText("FAILED TO CONNECT: USER CREDENTIALS DO NOT MATCH PROPERTIES FILE");
    	            connectionStatusLabel.setForeground(Color.RED);
    	            return;
    	        }

    	        // Try to connect to the operationslog database
    	        if (appLogic.connectToDatabase(databaseName, enteredUsername, enteredPassword)) 
    	        {
    	        	// Update connection label
    	            connectionStatusLabel.setText("CONNECTED TO DATABASE -> " + AppLogic.BASE_URL + databaseName);
    	            connectionStatusLabel.setForeground(Color.GREEN);

    	            // Disable login fields and enable disconnect button
    	            usernameInput.setEnabled(false);
    	            passwordInput.setEnabled(false);
    	            disconnectButton.setEnabled(true);
    	            connectButton.setEnabled(false);

    	            // Enable the SQL command console
    	            sqlCommandTextArea.setEditable(true);
    	            sqlCommandTextArea.setBackground(Color.WHITE);    
    	        } 
    	        else 
    	        {
    	        	// Update connection label - default failure
    	            connectionStatusLabel.setText("FAILED TO CONNECT");
    	            connectionStatusLabel.setForeground(Color.RED);
    	        }
    	    }
    	});
        
    	// Disconnect button 
        disconnectButton.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
            	// Disconnect & update connection status
                appLogic.disconnectFromDatabase();
                connectionStatusLabel.setText("NO CONNECTION ESTABLISHED");
                
                // Toggle buttons/menus/console availability
                disconnectButton.setEnabled(false);
                connectButton.setEnabled(true);
                sqlCommandTextArea.setEditable(false);
                sqlCommandTextArea.setText("");
                sqlCommandTextArea.setBackground(new Color(220, 220, 220)); 
	            usernameInput.setEnabled(true);
	            passwordInput.setEnabled(true);
                
                // Clear the result table
                resultTable.setModel(new DefaultTableModel());
                scrollPane.setViewportView(resultTable);
                
                // Udpate connection label
                connectionStatusLabel.setForeground(Color.RED);
            }
        });
        
        // Clear button
        clearSQL.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
            	// Clear the text area
                sqlCommandTextArea.setText("");
            }
        });
        
        // Automatic toggles for command buttons
        sqlCommandTextArea.getDocument().addDocumentListener((DocumentListener) new DocumentListener() 
        {
            public void insertUpdate(DocumentEvent e) 
            {
                toggleButtons();
            }

            public void removeUpdate(DocumentEvent e) 
            {
                toggleButtons();
            }

            public void changedUpdate(DocumentEvent e) 
            {
                toggleButtons();
            }

            // Enable buttons if SQL command area has content, else disable
            private void toggleButtons() {
                boolean hasText = !sqlCommandTextArea.getText().trim().isEmpty();
                clearSQL.setEnabled(hasText);
                executeSQL.setEnabled(hasText);
            }
        });
        
     // Execute button
        executeSQL.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) 
            {
            	// Get input for execution
    	        String enteredUsername = usernameInput.getText().trim();
                String sqlCommand = sqlCommandTextArea.getText().trim();
                sqlCommandTextArea.setText("");

                try 
                {
                    // Check if the command is a SELECT query
                    if (sqlCommand.toLowerCase().startsWith("select")) 
                    {
                        // Populate resultTable with data from executeSelectQuery
                        resultTable = appLogic.executeSelectQuery(sqlCommand);

                        // Display the new table in the scroll pane
                        scrollPane.setViewportView(resultTable);
                    } 
                    else 
                    {
                        // For non-SELECT queries, update table and display affected rows
                        int affectedRows = appLogic.executeUpdate(sqlCommand, enteredUsername);
                        JOptionPane.showMessageDialog(null, "Command executed successfully. Rows affected: " + affectedRows, "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                } 
                // Error handler -> debugging
                catch (SQLException ex) 
                {
                    JOptionPane.showMessageDialog(null, "Error executing command: " + ex.getMessage(), "SQL Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        
        // Clear results button
        clearResults.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) {
                // Clear the result table by setting an empty table
                resultTable.setModel(new DefaultTableModel());

                // Display the new, empty table
                scrollPane.setViewportView(resultTable);
            }
        });

        // Close button
        closeApp.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
            	// Disconnect from database before exiting
                appLogic.disconnectFromDatabase();
                
                // Close app
                System.exit(0);
            }
        });
    }
    
    
    // Launch GUI independently - testing
    public static void main(String[] args) 
    {
        EventQueue.invokeLater(() -> {
            try {
                AccountantGUI frame = new AccountantGUI();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
}



