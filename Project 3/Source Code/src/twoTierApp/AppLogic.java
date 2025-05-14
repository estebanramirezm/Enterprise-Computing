/*
* CNT 4714 Enterprise Computing - Project 3: Two-Tier app
* Description: Implements the logic for the app's functionality (database connection, SQL commands)
* Author: Esteban Ramirez
* Date: November 3rd, 2024
* Class: AppLogic
*/

package twoTierApp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.util.Vector;

public class AppLogic 
{
    private Connection databaseConnection, loggingConnection;
    private Statement databaseStatement, loggingStatement;
    public static final String BASE_URL = "jdbc:mysql://localhost:3306/";

 // Connects to the selected database
    public boolean connectToDatabase(String database, String user, String password) 
    {
    	// Try to connect
        try 
        {
            // Append the selected database to local URL
            String fullURL = BASE_URL + database;
            
            // Establish the connection
            databaseConnection = DriverManager.getConnection(fullURL, user, password);
            databaseStatement = databaseConnection.createStatement();
            
            // Successful connection
            System.out.println("Connected to the database: " + fullURL);
            
            // If it's the accountant, no need to double log into the operationsLog database
            if (!user.equals("theaccountant")) 
        	{
                connectToLoggingDatabase(user, password);
        	}
            
            // Indicate successful connection
            return true;
            
        } 
        // Connection failure
        catch (SQLException e) 
        {
            System.err.println("Connection failed: " + e.getMessage());
            return false;
        }
    }

    // Execute SELECT queries and display in a JTable
    public JTable executeSelectQuery(String query) throws SQLException 
    {
    	// Variables for the query
        ResultSet resultSet = databaseStatement.executeQuery(query);
        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        // Define column names
        Vector<String> columnNames = new Vector<>();
        for (int col = 1; col <= columnCount; col++) 
        {
            columnNames.add(metaData.getColumnName(col));
        }

        // Add rows to the table
        Vector<Vector<Object>> data = new Vector<>();
        while (resultSet.next()) 
        {
            Vector<Object> row = new Vector<>();
            for (int col = 1; col <= columnCount; col++) 
            {
                row.add(resultSet.getObject(col));
            }
            data.add(row);
        }
        
        // Close set
        resultSet.close();

        // Create the table model with data
        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        JTable table = new JTable(tableModel);

        // Disable direct user-edits for accuracy (command-only edits)
        table.setEnabled(false);
        
        // Set column borders and grid lines for clarity
        table.setShowGrid(true);
        table.setGridColor(Color.GRAY);

        // Return the JTable
        return table;
    }

    // Display the JTable in a scroll pane
    public void displayTableInScrollPane(String query) throws SQLException 
    {
        JTable table = executeSelectQuery(query);

        // Set up JFrame for display
        JFrame frame = new JFrame("Database Table Result");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Add JTable to JScrollPane for scrollability and header visibility
        JScrollPane scrollPane = new JScrollPane(table);
        frame.add(scrollPane, BorderLayout.CENTER);
        
        // Layout
        frame.setSize(800, 600);
        frame.setVisible(true);
    }

    // Connects to the operations log database
    public boolean connectToLoggingDatabase(String user, String password) 
    {
    	// Try to connect
        try 
        {
            // Append the logs database to local URL
            String fullURL = BASE_URL + "operationslog"; 
            
            // Connect to database with edit permits
            loggingConnection = DriverManager.getConnection(fullURL, "root", "root"); 
            loggingStatement = loggingConnection.createStatement();
            
            // Print successful connection to the console
            System.out.println("Connected to the logging database: " + fullURL);
            
            // Return successful connection status
            return true;
        }
        // Connection failure
        catch (SQLException e) 
        {
            System.err.println("Connection to logging database failed: " + e.getMessage());
            return false;
        }
    }

 // Executes UPDATE, INSERT, DELETE commands
    public int executeUpdate(String query, String username) throws SQLException 
    {
        int result = databaseStatement.executeUpdate(query);
        return result;
    }

    // Logs operation
    public void logOperation(String username, boolean isQuery) throws SQLException 
    {
        String query;
        
        // Define which kind of operation
        // If query, add 1 to num_queries 
        if (isQuery) 
        {
            query = "INSERT INTO operationscount (login_username, num_queries, num_updates) VALUES (?, 1, 0) " +
                    "ON DUPLICATE KEY UPDATE num_queries = num_queries + 1";
        } 
        // Else, add 1 to num_updates
        else 
        {
            query = "INSERT INTO operationscount (login_username, num_queries, num_updates) VALUES (?, 0, 1) " +
                    "ON DUPLICATE KEY UPDATE num_updates = num_updates + 1";
        }
   
        // Execute update
        try (PreparedStatement preparedStatement = loggingConnection.prepareStatement(query)) 
        {
            preparedStatement.setString(1, username + "@localhost");
            int rowsInserted = preparedStatement.executeUpdate();
            // Print success/failure to console
            if (rowsInserted > 0) 
            {
                System.out.println("Operation logged successfully for user: " + username + "@localhost");
            } 
            else 
            {
                System.err.println("No rows inserted in operationslog for user: " + username + "@localhost");
            }
        } 
        // Print default failure to console
        catch (SQLException e) 
        {
            System.err.println("Error logging operation: " + e.getMessage());
        }
    }

    // Disconnects from operatiosnLog database
    public void disconnectFromLoggingDatabase() 
    {
    	// Try to disconnect
        try
        {
            if (loggingStatement != null) 
            {
            	loggingStatement.close();
            }
            if (loggingConnection != null) 
            {
            	loggingConnection.close();
                System.out.println("Disconnected from the logging database.");
            }
        } 
        // Error message - debugging
        catch (SQLException e) 
        {
            System.err.println("Error while disconnecting from logging database: " + e.getMessage());
        } 
        // Set to null to indicate no current connection
        finally 
        {
        	loggingConnection = null;
            loggingStatement = null;
        }
    }

    // Disconnect from the selected database
    public void disconnectFromDatabase() 
    {
    	// Try to disconnect
        try 
        {
            if (databaseConnection != null) 
            {
            	if(loggingConnection != null)
            	{
	                disconnectFromLoggingDatabase();
            	}
                databaseConnection.close();
                System.out.println("Disconnected from the database.");
            }
        } 
        // Error message - debugging
        catch (SQLException e) 
        {
            System.err.println("Error while disconnecting: " + e.getMessage());
        } 
        // Set to null to indicate no current connection
        finally 
        {
        	databaseConnection = null;
        	databaseStatement = null;
        }
    }
}

