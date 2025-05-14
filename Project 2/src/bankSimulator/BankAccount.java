package bankSimulator;

import java.util.concurrent.locks.ReentrantLock;

// Implementation of a bank account
public class BankAccount 
{
	// Members
    private int balance;
    private final ReentrantLock lock;

    // Initializes the account 
    public BankAccount() {
        this.balance = 0;
        this.lock = new ReentrantLock();
    }

    // Deposits into the accound
    public void deposit(int amount) 
    {
        // Lock account
        lock.lock();
        try 
        {
        	// Add deposit amount, update balance
            balance += amount;
        } finally {
            lock.unlock();
        }
    }

    // Withdraws from account if there's enough funds
    public boolean withdraw(int amount) 
    {
        // Lock account
        lock.lock();
        try 
        {
        	// Failure
            if (balance < amount) 
            {
                return false;
            }
            // Success
            else
            {
            	// Remove amount, update balance
	            balance -= amount;
	            return true; 
            }
        } 
        finally 
        {
            // Unlock account
            lock.unlock();
        }
    }

    // Gets the balance of an account
    public int getBalance() 
    {
        // Lock account
        lock.lock();
        try 
        {
            return balance;
        } 
        finally 
        {
            // Unlock account
            lock.unlock();
        }
    }

    // Checks lock
    public ReentrantLock getLock() 
    {
        return lock;
    }
}