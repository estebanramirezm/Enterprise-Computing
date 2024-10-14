package niledotcom.project1;

import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

public class Logic {

    private Map<String, String[]> inventory = new HashMap<>();
    private List<String[]> shoppingCart = new ArrayList<>();

    // Constructor for logic
    public Logic() {
        // Load inventory at startup
        loadInventory("resources/inventory.csv");
    }

    // Method that loads the inventory from a CSV file
    private void loadInventory(String fileName) 
    {
    	// Open file
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) 
        {
            String line;
            
            // Copy contents
            while ((line = br.readLine()) != null) 
            {
                String[] parts = line.split(",", 5);
                String itemId = parts[0];
                System.out.println("Loading item: " + itemId);
                inventory.put(itemId, parts); // Add to the inventory map
            }
        } 
        // Debug - test exceptions
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method that searchs for an item in the inventory
    public String searchItem(String itemId, int quantity) 
    {
    	// Load data for ID
        String[] itemData = inventory.get(itemId);

        // If ID exists
        if (itemData != null) 
        {
            // Parse stock quantity
            int stockQuantity = Integer.parseInt(itemData[3].trim());

            // Check stock availability
            if (stockQuantity == 0) 
            {
                return "Out of stock"; // Completely out
            }
            if (stockQuantity < quantity) {
                return "Insufficient stock"; // Partially out
            }
            
            // If there's sufficient stock...
            else
            {
            	// Copy data
	            String description = itemData[1];
	            double price = Double.parseDouble(itemData[4].trim());
	            double totalPrice = price * quantity;
	            double discount = calculateDiscount(quantity);
	            double discountedPrice = totalPrice * (1 - discount);
	
	            // Return formatted string containing the item's info
	            return String.format("%s %s $%.2f %d %d%% $%.2f", itemData[0], description, price, quantity,
	                    (int) (discount * 100), discountedPrice);
            }
        }
        // ID not found
        else 
        {
            return "Item not found";
        }
    }

    // Method that gets the stock in inventory of an item
    public int getStock(String itemId) 
    {
        String[] itemData = inventory.get(itemId);
        return Integer.parseInt(itemData[3].trim());
    }

    // Method that calculates discount based on quantity
    private double calculateDiscount(int quantity) 
    {
    	// 20% discount
        if (quantity >= 15) 
        {
            return 0.20;         
        } 
        // 15% discount
        else if (quantity >= 10) 
        {
            return 0.15;         
        } 
        // 10% discount
        else if (quantity >= 5) 
        {
            return 0.10; 
        } 
        // No discount
        else 
        {
            return 0.00; 
        }
    }

    // Method that adds an item to the cart
    public void addItemToCart(String itemId, int quantity) 
    {
    	// Load data for ID
        String[] itemData = inventory.get(itemId);
        
        // If item exists
        if (itemData != null) 
        {
        	// Parse stock quantity
            int stockQuantity = Integer.parseInt(itemData[3].trim());
           
            // Make sure there's enough stock
            if (stockQuantity >= quantity) 
            {
            	// Update stock
                itemData[3] = String.valueOf(stockQuantity - quantity);
                
                // Add item
                shoppingCart.add(new String[] { itemId, itemData[1], String.valueOf(quantity), itemData[4] });
            }    
        }
    }

    // Method that displays the cart content
    public String viewCart() 
    {
    	// Variable to keep count of items
        int itemCount = 1;

        // String to display cart
        StringBuilder cart = new StringBuilder("");

        // Loop through the cart
        for (String[] cartItem : shoppingCart) 
        {
            String itemId = cartItem[0]; // SKU/Item ID
            String description = cartItem[1]; // Item description
            int quantity = Integer.parseInt(cartItem[2]); // Quantity
            double price = Double.parseDouble(cartItem[3]); // Price per item
            double totalItemPrice = quantity * price; // Total price for the item
            double discount = calculateDiscount(quantity); // Discount (by quantity)
            double discountedPrice = totalItemPrice * (1 - discount); // Price with discount

            // Append item info to string
            cart.append( String.format("%s. %s %s $%.2f %d %d%% $%.2f\n", itemCount, itemId, description, price, quantity,
                            (int) (discount * 100), discountedPrice));

            // Increment the item counter
            itemCount++;
        }
        
        // Return cart content string
        return cart.toString();
    }

    // Method to view the current order
    public String currentOrder() 
    {
    	// Variable to keep count of items
        int itemCount = 1; 

        // String to display order
        StringBuilder orderDetails = new StringBuilder("");
        
        // Loop through cart
        for (String[] cartItem : shoppingCart) 
        {
            String itemId = cartItem[0]; // SKU/Item ID
            String description = cartItem[1]; // Item description
            int quantity = Integer.parseInt(cartItem[2]); // Quantity
            double price = Double.parseDouble(cartItem[3]); // Price per item
            double totalItemPrice = quantity * price; // Total price for the item

            // Append item info to string
            orderDetails.append(String.format("Item %d - SKU: %s, Desc:%s, Price Ea: $%.2f, Qty: %d, Total: $%.2f\n",
                    itemCount, itemId, description, price, quantity, totalItemPrice));

            itemCount++; // Increment the item counter
        }

        // Return current order string
        return orderDetails.toString();
    }

    // Method that checks out the transaction and writes to transactions.csv
    public String checkOut() 
    {
    	// Variables to keep track of prices
        double subtotal = getTotalPrice();
        double tax = 0.06; // Assuming always 6% tax
        double total = 0.0;

        // String to display invoice
        StringBuilder invoice = new StringBuilder("Date: " + generateTransactionDate() + "\n\n"); // Date
        invoice.append("Number of line items: ").append(getCartSize() + "\n\n"); // Items
        invoice.append("Item# / ID / Title / Price / Qty / Disc % / Subtotal:\n\n"); // Explanation
        invoice.append(viewCart()); // We can reuse viewCart() here as it uses the same format

        // Calculate tax paid and new total with tax
        double taxAmount = subtotal * tax;
        total = subtotal + taxAmount;

        // Continue generating the invoice...
        invoice.append("\nSubtotal: $").append(String.format("%.2f", subtotal)).append("\n\n");
        invoice.append("Tax rate: 6%\n\n");
        invoice.append("Tax amount: $").append(String.format("%.2f", taxAmount)).append("\n\n");
        invoice.append("ORDER TOTAL: $").append(String.format("%.2f", total)).append("\n\n");
        
        // Thank the user for choosing Nile Dot Com
        invoice.append("Thanks for shopping at Nile Dot Com! :)").append("\n");

        // Write the transaction to transactions.csv
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("resources/transactions.csv", true))) 
        {
        	// Generate both formats for date: ID and written
            String transactionId = generateTransactionID();
            String transactionDate = generateTransactionDate();

            // Loop through cart
            bw.write("\n");
            for (String[] cartItem : shoppingCart) 
            {
                String itemId = cartItem[0]; // SKU/Item ID
                String description = cartItem[1]; // Item description
                int quantity = Integer.parseInt(cartItem[2]); // Quantity
                double price = Double.parseDouble(cartItem[3]); // Price per item
                double discount = calculateDiscount(quantity); // Discount (based on quantity)
                double totalItemPrice = quantity * price; // Total price for the item
                double discountedPrice = totalItemPrice * (1 - discount); // Discounted price

                // Write item info to file
                bw.write("\n" + transactionId + ", " + itemId + ", " + description + ", " + price + ", " + quantity
                        + ", " + discount + (", ") + discountedPrice + ", " + transactionDate);
            }

            // Close file when finished copying the invoice
            bw.close();
        } 
        catch (IOException e) 
        {
            e.printStackTrace(); // Debug for fails
        }

        // Clear the cart after checkout
        return invoice.toString();
    }

    // Method that generates a transaction date - written
    private String generateTransactionDate() {
        return new SimpleDateFormat("MMMM dd, yyyy, hh:mm:ss a z").format(new Date());
    }

    // Method that generates a transaction ID - numerical
    private String generateTransactionID() {
        return new SimpleDateFormat("ddMMyyyyhhmmss").format(new Date());
    }

    // Method that returns the size of the cart
    public int getCartSize() 
    {
        return shoppingCart.size();
    }

    // Method that returns the total price of the current cart
    public int getTotalPrice() 
    {
    	// Variable to hold the total
        int totalP = 0;
        
        // Loop through each item
        for (int i = 0; i < shoppingCart.size(); i++) 
        {
            int quantity = Integer.parseInt(shoppingCart.get(i)[2].trim());
            double price = Double.parseDouble(shoppingCart.get(i)[3].trim());
            totalP += quantity * price; // Total = quantity * price
        }
        
        // Return current subtotal
        return totalP;
    }

    // Method that returns the total number of items in the cart (lines AND quantity per line)
    public int getTotalItems() 
    {
    	// Variable to hold the total
        int totalI = 0;
        
        // Loop through each item
        for (int i = 0; i < shoppingCart.size(); i++) 
        {
            int quantity = Integer.parseInt(shoppingCart.get(i)[2].trim());
            totalI += quantity; // Add parsed int 
        }
        
        // Return current total of items
        return totalI;
    }
}