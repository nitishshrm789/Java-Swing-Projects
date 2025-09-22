package radioButton;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JRadioButton;

public class MyFrame extends JFrame implements ActionListener {
	
	JRadioButton pizzaButton ;
	JRadioButton hamburgerButton ;
	JRadioButton hotDogButton ;
	
	ImageIcon pizzaIcon ;
	ImageIcon hamburgerIcon ;
	ImageIcon hotDogIcon ;
	
	
	MyFrame(){
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(new FlowLayout());
		
		pizzaIcon = new ImageIcon("pizza.png");
		hamburgerIcon = new ImageIcon("hamburger.png");
		hotDogIcon = new ImageIcon("hotdog.png");
		
		pizzaButton = new JRadioButton("Pizza");
		hamburgerButton = new JRadioButton("Hamburger");
		hotDogButton = new JRadioButton("Hot Dog");
		
		ButtonGroup group = new ButtonGroup();
		group.add(pizzaButton);
		group.add(hamburgerButton);
		group.add(hotDogButton);
		
		pizzaButton.addActionListener(this);
		hamburgerButton.addActionListener(this);
		hotDogButton.addActionListener(this);
		
		pizzaButton.setIcon(pizzaIcon);
		hamburgerButton.setIcon(hamburgerIcon);
		hotDogButton.setIcon(hotDogIcon);
		
		this.add(pizzaButton);
		this.add(hamburgerButton);
		this.add(hotDogButton);
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == pizzaButton) {
			System.out.println("You ordered Pizza");
		}
		else if(e.getSource() == hamburgerButton) {
			System.out.println("You ordered Hamburger");
		}else {
			System.out.println("You ordered Hot Dog");
		}
	}
	
}
