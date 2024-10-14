/*
* CNT 4714 Enterprise Computing - Project 1: NileDotCom
* Description: Displays a GUI to interact with a virtual store, Nile Dot Com. Writes invoice to an external file.
* Author: Esteban Ramirez
* Date: September 8th, 2024
*/

package niledotcom.project1;

import javax.swing.SwingUtilities;

public class Main 
{
    public static void main(String[] args) 
    {    
        // Create and display the program's GUI
        SwingUtilities.invokeLater(new Runnable() 
        {
            public void run() 
            {
                GUI gui = new GUI();
                gui.setVisible(true);
            }
        });
    }
}
