package bankSimulator;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

public class Transfer implements Runnable 
{
	// Define constant values
    private static final int MAX_TRANSFER = 1000;
    private static final int MAX_SLEEP = 500; 
    
    // Members
    private final BankAccount[] accounts;
    private final Random random;
    private final int id;
    private final AtomicInteger transactionCounter;

    // Initializes transfer agent
    public Transfer(int id, BankAccount[] accounts, AtomicInteger transactionCounter) {
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
        	// Decide origin and destination
            int fromIndex = random.nextInt(accounts.length);
            int toIndex;
            do 
            {
                toIndex = random.nextInt(accounts.length);
            } while (toIndex == fromIndex);

            // Set amount to transfer and variables for origin/destination
            int amount = random.nextInt(MAX_TRANSFER) + 1;
            BankAccount fromAccount = accounts[fromIndex];
            BankAccount toAccount = accounts[toIndex];

            // Try to transfer
            boolean success = transfer(fromAccount, toAccount, amount);
            // Successful transfer
            if (success) 
            {
                int transactionNumber = transactionCounter.incrementAndGet();
                System.out.printf("\t TRANSFER --> Agent TR%d transferring $%d from JA-%d to JA-%d \t    JA-%d balance is: $%d"
                		+ "\t\t\t %d\n" + "\t TRANSFER COMPLETE --> Account JA-%d balance is now $%d\n\n", 
                		id, amount, fromIndex, toIndex, fromIndex, fromAccount.getBalance(), transactionNumber, 
                		id, amount, toIndex, toAccount.getBalance());
            } 
            // Queue next transfer
            try 
            {
                Thread.sleep(random.nextInt(MAX_SLEEP));
            } 
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Changes the balances for the transfer
    private boolean transfer(BankAccount from, BankAccount to, int amount) 
    {
    	// Lock origin account
        from.getLock().lock();
        try 
        {
        	// Lock destination account
            to.getLock().lock();
            try {
            	// If there's enough funds, transfer
                if (from.getBalance() >= amount) 
                {
                    from.withdraw(amount);
                    to.deposit(amount);
                    return true;
                }
                return false;
            } 
            finally 
            {
            	// Unlock destination account
                to.getLock().unlock();
            }
        } 
        finally 
        {
        	// Unlock origin account
            from.getLock().unlock();
        }
    }
}