/*
* CNT 4714 Enterprise Computing - Project 3: Two-Tier app
* Description: Launches both versions of the app with interactive buttons
* Author: Esteban Ramirez
* Date: November 3rd , 2024
* Class: AppHome
*/

package twoTierApp;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.Box;
import javax.swing.BoxLayout;

public class AppHome extends JFrame 
{
	// Initialize GUI
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    AppHome frame = new AppHome();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public AppHome() {
        setTitle("Application Home");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 528, 130);
        
        // Main content pane
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS)); // Vertical layout
        
        // Center the buttons vertically
        contentPane.add(Box.createVerticalGlue());

        // Panel for buttons - horizontal layout
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS)); // Horizontal layout for buttons
        contentPane.add(buttonPanel);

        // Option 1: Two Tier app
        JButton btnFullGUI = new JButton("Launch Two-Tier App");
        buttonPanel.add(btnFullGUI);
        
	        // Action listener 
	        btnFullGUI.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                FullGUI fullGui = new FullGUI();
	                fullGui.setVisible(true);
	                dispose();
	            }
	        });
        
        // Spacing between buttons - horizontal
        buttonPanel.add(Box.createHorizontalStrut(20));
        
        // Option 2: Accountant app
        JButton btnAccountantGUI = new JButton("Launch Accountant App");
        buttonPanel.add(btnAccountantGUI);
        
	        // Action listener
	        btnAccountantGUI.addActionListener(new ActionListener() {
	            public void actionPerformed(ActionEvent e) {
	                AccountantGUI accountantGui = new AccountantGUI();
	                accountantGui.setVisible(true);
	                dispose();
	            }
	        });
        
	        
        // Center the buttons vertically
        contentPane.add(Box.createVerticalGlue());
    }
}
