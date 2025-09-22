package main;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

class CreatingFrame {
	public static void main(String[] args) {
		/*
		JFrame frame = new JFrame(); // Creates a frame
		frame.setTitle("JFrame Title goes here"); // Sets title of frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit the application
		frame.setSize(420,420); // sets the x-dimension and y-dimmension of frame
		frame.setVisible(true); // make frame visible
		frame.setResizable(false); // Setting frame resizable
		
		ImageIcon image = new ImageIcon("Superman_shield.png"); // Create an ImageIcon
		frame.setIconImage(image.getImage()); // change icon of a frame
		frame.getContentPane().setBackground(new Color(145,145,145));
		*/
		
		new MyFrame();
	}
}