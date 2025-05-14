package bankSimulator;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

// Implements withdrawal agent
public class Withdrawal implements Runnable 
{
	// Define constant values
    private static final int MAX_WITHDRAWAL = 99;
    private static final int MAX_SLEEP = 70;
    
    // Members
    private final BankAccount[] accounts;
    private final Random random;
    private final int id;
    private final AtomicInteger transactionCounter;

    // Initialize withdrawal agent
    public Withdrawal(int id, BankAccount[] accounts, AtomicInteger transactionCounter) 
    {
        this.id = id;
        this.accounts = accounts;
        this.random = new Random();
        this.transactionCounter = transactionCounter;
    }

    @Override
    public void run() 
    {
        while (!Thread.currentThread().isInterrupted()) 
        {
        	// Variables
            int accountIndex = random.nextInt(accounts.length);
            int amount = random.nextInt(MAX_WITHDRAWAL) + 1; 
            BankAccount account = accounts[accountIndex];
            
            // Try to withdraw
            boolean success = account.withdraw(amount);
            // Successful withdrawal, enough funds
            if (success) 
            {
                // Print updated balance and update transaction counter
                int transactionNumber = transactionCounter.incrementAndGet();                
                System.out.printf("\t\t\t\tAgent WT%d withdraws $%d from JA-%d \t(-) JA-%d balance is: $%d \t\t\t %d\n\n",
                        id, amount, accountIndex, accountIndex, account.getBalance(), transactionNumber);
                
                // Flag withdrawal if it's bigger than 90
                if (amount > 90) {
                    System.out.printf("* * * Flagged Transaction * * * Agent WT%d made a withdrawal in excess of $90.00 USD - See Flagged Transaction Log. \n\n", id);
                    TransactionLog.logTransaction("WT", String.valueOf(id), "withdrawal", amount, transactionNumber, accountIndex);
                }
            } 
            // Failed withdrawal, insufficient funds
            else 
            {
                System.out.printf("Agent WT%d attempts to withdraw $%d from JA-%d. \t\t (*****) WITHDRAWAL BLOCKED -- INSUFFICIENT FUNDS!!!\n\n",
                        id, amount, accountIndex, accountIndex);
            }
            // Queue next withdrawal
            try 
            {
                Thread.sleep(random.nextInt(MAX_SLEEP));
            } 
            catch (InterruptedException e) 
            {
                Thread.currentThread().interrupt();
            }
        }
    }
}