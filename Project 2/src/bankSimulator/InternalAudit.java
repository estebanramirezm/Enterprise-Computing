package bankSimulator;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

// Conducts an internal audit of the
public class InternalAudit implements Runnable 
{
	// Define constant value
    private static final int MAX_SLEEP_INTERNAL = 1000;
    
    // Members
    private final BankAccount[] accounts;
    private final Random random;
    private final AtomicInteger transactionCounter;
    private int lastTransactionCount;

    // Initializes internal audit agent
    public InternalAudit(BankAccount[] accounts, AtomicInteger transactionCounter) {
        this.accounts = accounts;
        this.random = new Random();
        this.transactionCounter = transactionCounter;
        this.lastTransactionCount = 0;
    }

    @Override
    public void run() 
    {
        while (!Thread.currentThread().isInterrupted()) 
        {
        	// Lock all accounts while audit is being conducted
            boolean allLocked = lockAllAccounts();
            if (allLocked) 
            {
                try 
                {
                	// Do audit
                    performAudit();
                } finally 
                {
                	// Unlock accounts
                    unlockAllAccounts();
                }
            }
        	// Queue next audit
            try 
            {
                Thread.sleep(random.nextInt(MAX_SLEEP_INTERNAL));
            } 
            catch (InterruptedException e) 
            {
                Thread.currentThread().interrupt();
            }
        }
    }

    // Lock all acounts
    private boolean lockAllAccounts() 
    {
        for (BankAccount account : accounts) 
        {
            if (!account.getLock().tryLock()) {
                for (BankAccount releasedAccount : accounts) 
                {
                    if (releasedAccount == account) break; // Check for locking failures
                    releasedAccount.getLock().unlock();
                }
                return false;
            }
        }
        return true;
    }

    // Unlock all accounts
    private void unlockAllAccounts() {
        for (BankAccount account : accounts) {
            account.getLock().unlock();
        }
    }

    // Generate audit report
    private void performAudit() 
    {
    	// Variables
        int currentTransactionCount = transactionCounter.get();
        int transactionsSinceLastAudit = currentTransactionCount - lastTransactionCount;
        lastTransactionCount = currentTransactionCount;

        // Print audit
        System.out.printf("\n**************************************\n\n\n");
        System.out.printf("Internal Bank Audit Report:\n");
        System.out.printf("The total number of transactions since last audit is: %d\n\n", transactionsSinceLastAudit);
        for (int i = 0; i < accounts.length; i++) 
        {
            System.out.printf("Account %d balance: $%d\n", i, accounts[i].getBalance());
        }
        System.out.printf("Internal Bank Audit Complete\n\n\n");
        System.out.printf("**************************************\n\n\n");
    }
}