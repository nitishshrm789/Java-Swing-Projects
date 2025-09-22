package main;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class MyFrame extends JFrame
{
	MyFrame() {
//		JFrame frame = new JFrame(); // Creates a frame
		this.setTitle("JFrame Title goes here"); // Sets title of frame
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit the application
		this.setSize(420,420); // sets the x-dimension and y-dimmension of frame
		this.setVisible(true); // make frame visible
		this.setResizable(false); // Setting frame resizable
		
		ImageIcon image = new ImageIcon("Superman_shield.png"); // Create an ImageIcon
		this.setIconImage(image.getImage()); // change icon of a frame
		this.getContentPane().setBackground(new Color(145,145,145));
		
	}
}