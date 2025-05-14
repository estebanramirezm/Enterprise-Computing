/*
* CNT 4714 Enterprise Computing - Project 2: Bank Simulator
* Description: Simulates a bank's operations in a random order. Automatized, with no user input. Writes a transaction log to an external file.
* Author: Esteban Ramirez
* Date: September 22nd, 2024
*/

package bankSimulator;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.lang.Thread;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class Main 
{
	// Defined constant number of agents 
    private static final int NUM_ACCOUNTS = 2;
    private static final int NUM_DEPOSITORS = 5;
    private static final int NUM_WITHDRAWALS = 10;
    private static final int NUM_TRANSFERS = 2;
    
    
    // Simulation time. Can be adjusted. In minutes.
    private static final int SIMULATION_MINUTES = 10;

    // Simulates a bank's operations. Writes a transaction log to an external file.
    public static void main(String[] args) 
    {
    	
    	// Redirects console output to an external file. Commented out for grading purposes
    	
        /*PrintStream out;
		try {
			out = new PrintStream("consoleOutput.txt");
	        System.setOut(out);
		} catch (FileNotFoundException e) 
		{
			e.printStackTrace();
		}*/
    	
    	// Print initial messages to describe & start the program
        System.out.printf("* * * BANK SIMULATION BEGINS * * *\n\n");
        System.out.printf("Deposit Agents\t\t\t\t Withdrawal Agents\t\t\t Balances\t\t\t Transaction Number\n");
        System.out.printf("--------------\t\t\t\t ------------------\t\t\t --------\t\t\t ------------------\n");

        // Initialize bank accounts
        BankAccount[] accounts = new BankAccount[NUM_ACCOUNTS];
        for (int i = 0; i < NUM_ACCOUNTS; i++) 
        {
            accounts[i] = new BankAccount();
        }

        // Initialize transaction counter
        AtomicInteger transactionCounter = new AtomicInteger(0);

        // Start threads for agents
        ExecutorService executor = Executors.newFixedThreadPool(
            NUM_DEPOSITORS + NUM_WITHDRAWALS + NUM_TRANSFERS + 2);

        // Start depositor and withdrawal agents interchangeably (so it randomizes the process and doesn't unbalance the deposit/withdrawal ratio)
        int maxAgents = Math.max(NUM_DEPOSITORS, NUM_WITHDRAWALS);
        for (int i = 0; i < maxAgents; i++) {
            if (i < NUM_DEPOSITORS) 
            {
                executor.submit(new Depositor(i, accounts, transactionCounter));
            }
            if (i < NUM_WITHDRAWALS) 
            {
                executor.submit(new Withdrawal(i, accounts, transactionCounter));
            }
        }
        
        // Start transfer agents
        for (int i = 0; i < NUM_TRANSFERS; i++) 
        {
            executor.submit(new Transfer(i, accounts, transactionCounter));
        }

        // Start auditor agents
        executor.submit(new InternalAudit(accounts, transactionCounter)); // Internal 
        executor.submit(new USAudit(accounts, transactionCounter)); // Treasury
        
        // Run the simulation for a set amount of minutes
        try 
        {
            Thread.sleep(TimeUnit.MINUTES.toMillis(SIMULATION_MINUTES));
        } catch (InterruptedException e) 
        {
            Thread.currentThread().interrupt();
        }

        // Shutdown the executor after simulation time runs out
        executor.shutdownNow();
        try 
        {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) 
            {
                System.err.println("Simulation has run out of time\n\n"); // Let the user know the simulation has finished
            }
        } 
        catch (InterruptedException e) 
        {
            Thread.currentThread().interrupt();
        }

        // End of program message
        System.out.println("Thanks for running this simulator!!!");
    }
}