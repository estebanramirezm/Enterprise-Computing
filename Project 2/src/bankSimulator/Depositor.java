package bankSimulator;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

// Implements Depositor agent
public class Depositor implements Runnable
{
	// Define constant values
    private static final int MAX_DEPOSIT = 600;
    private static final int MAX_SLEEP = 180;
    
   // Members
    private final BankAccount[] accounts;
    private final Random random;
    private final int id;
    private final AtomicInteger transactionCounter;

    // Initializes depositor agent
    public Depositor(int id, BankAccount[] accounts, AtomicInteger transactionCounter) {
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
        	// Set variables for the deposit
            int accountIndex = random.nextInt(accounts.length);
            int amount = random.nextInt(MAX_DEPOSIT) + 1;
            BankAccount account = accounts[accountIndex];
            account.deposit(amount);
            
            // Print updated balance and update transaction counter
            int transactionNumber = transactionCounter.incrementAndGet();
            System.out.printf("Agent DT%d deposits $%d into: JA-%d \t\t\t\t        (+) JA-%d balance is: $%d \t\t\t %d\n\n",
                              id, amount, accountIndex, accountIndex, account.getBalance(), transactionNumber);
            
            // Flag deposit if it's bigger than 450
            if (amount > 450) 
            {
                System.out.printf("* * *Flagged Transaction * ** Agent DT%d made a deposit in excess of $450.00 USD - See Flagged Transaction Log. \n\n", id);
                TransactionLog.logTransaction("DT", String.valueOf(id), "deposit", amount, transactionNumber, accountIndex);
            }
            // Queue next deposit
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