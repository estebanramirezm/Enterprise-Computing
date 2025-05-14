package bankSimulator;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

// Writes a transaction log to an external file
public class TransactionLog 
{
	// Define output file and date format
    private static final String LOG_FILE = "transactions.csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss z");

    // Creates a log containing all flagged transactions
    public static void logTransaction(String agentType, String agentId, String transactionType, double amount, int transactionNumber, int accountIndex) 
    {        
        // Creates string for the flagged transaction
        String timestamp = DATE_FORMAT.format(new Date());
        String logEntry = String.format("Agent %s%s issued %s of $%.2f on Account %d at: %s Transaction Number: %d%n%n",
                agentType, agentId, transactionType, amount, accountIndex, timestamp, transactionNumber);

        // Prints the transaction to the log file
        try (PrintWriter out = new PrintWriter(new FileWriter(LOG_FILE, true))) 
        {
            out.print(logEntry);
        } 
        catch (IOException e) 
        {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }
}