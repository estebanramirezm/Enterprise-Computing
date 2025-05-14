/*
* CNT 4714 Enterprise Computing - Project 3: Two-Tier app
* Description: Interacts with a JDBC database through a GUI-based application using SQL commands
* Author: Esteban Ramirez
* Date: November 3rd , 2024
* Class: Main
*/

package twoTierApp;

import javax.swing.SwingUtilities;

public class Main 
{
    public static void main(String[] args) 
    {    
        // Display the main GUI - App Launcher
        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
                AppHome window = new AppHome();
                window.setVisible(true);
            }
        });
    }
}