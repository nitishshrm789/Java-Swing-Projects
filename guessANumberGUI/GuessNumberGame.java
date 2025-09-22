package guessANumberGUI;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.*;

public class GuessNumberGame extends JFrame {
	private CardLayout cardLayout;
    private JPanel mainPanel;

    private int secretNumber;
    private int attemptsLeft;

    private JLabel messageLabel;
    private JTextField guessField;

    public GuessNumberGame() {
        setTitle("Guess the Number Game");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createWelcomePanel(), "welcome");
        mainPanel.add(createGamePanel(), "game");

        add(mainPanel);
        cardLayout.show(mainPanel, "welcome");

        setVisible(true);
    }

    // Panel 1: Welcome screen with difficulty selection
    private JPanel createWelcomePanel() {
    	JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50)); // spacing around

        JLabel title = new JLabel("🎉 Welcome to the Guessing Game!", SwingConstants.CENTER);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);  // Center in BoxLayout
        title.setFont(new Font("Arial", Font.BOLD, 18));   // Optional styling
        panel.add(title);
        panel.add(Box.createRigidArea(new Dimension(0, 20))); // space between components

        JButton easyButton = new JButton("Easy (10 chances)");
        JButton mediumButton = new JButton("Medium (7 chances)");
        JButton hardButton = new JButton("Hard (5 chances)");
        JButton infoButton = new JButton("Know How To Play");
        JButton exitButton = new JButton("Exit");
        
        easyButton.setFocusable(false);
        mediumButton.setFocusable(false);
        hardButton.setFocusable(false);
        infoButton.setFocusable(false);
        exitButton.setFocusable(false);

        // Set buttons center-aligned
        for (JButton button : new JButton[]{easyButton, mediumButton, hardButton, infoButton, exitButton}) {
            button.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(button);
            panel.add(Box.createRigidArea(new Dimension(0, 10))); // spacing between buttons
        }

        easyButton.addActionListener(e -> startGame(10));
        mediumButton.addActionListener(e -> startGame(7));
        hardButton.addActionListener(e -> startGame(5));
        infoButton.addActionListener(e -> {
            String instructions = """
                🎮 How to Play:

                1. Choose a difficulty level:
                   - Easy   → 10 chances
                   - Medium → 7 chances
                   - Hard   → 5 chances

                2. The computer picks a number between 1 and 100.

                3. Your job is to guess the number!

                4. After each guess, you'll get a hint:
                   - "Too high" or "Too low"

                5. Try to guess the number before your chances run out!

                Good luck! 🍀
                """;
            JOptionPane.showMessageDialog(
                    null,                       // parent component
                    instructions,               // message
                    "How to Play",              // title
                    JOptionPane.INFORMATION_MESSAGE // icon type
                );
            });
        exitButton.addActionListener(e -> {
        	String exitingGame = """
        			Are you sure ? \nDo you want to exit the Game...!!!
        			""";
        	int choice = JOptionPane.showConfirmDialog(
        			null, 
        			exitingGame,
        			"Do you want to exit ???",
        			JOptionPane.OK_CANCEL_OPTION
        			);
        	if (choice == JOptionPane.OK_OPTION) {
                System.exit(0);  // exit if user confirms
            }
        });

        return panel;
    }

    // Panel 2: Game screen
    private JPanel createGamePanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        messageLabel = new JLabel("Guess a number between 1 and 100");
        panel.add(messageLabel, BorderLayout.NORTH);

        guessField = new JTextField();
        panel.add(guessField, BorderLayout.CENTER);
        
        ActionListener submitGuessAction = e -> {
            handleGuess();                  // Process guess
            guessField.requestFocusInWindow(); // Return focus to text field
        };

        JButton submitButton = new JButton("Submit Guess");
        panel.add(submitButton, BorderLayout.SOUTH);

        submitButton.addActionListener(submitGuessAction);
        guessField.addActionListener(submitGuessAction);
        
        return panel;
    }

    // Start the game with selected difficulty
    private void startGame(int chances) {
        secretNumber = new Random().nextInt(100) + 1;
        attemptsLeft = chances;
        messageLabel.setText("Guess a number between 1 and 100. You have " + attemptsLeft + " tries.");
        guessField.setText("");
        cardLayout.show(mainPanel, "game");
    }

    // Handle the user's guess
    private void handleGuess() {
        String input = guessField.getText();
        try {
            int guess = Integer.parseInt(input);

            if (guess < 1 || guess > 100) {
                messageLabel.setText("Please enter a number between 1 and 100.");
                return;
            }

            attemptsLeft--;

            if (guess == secretNumber) {
                showEndDialog("🎉 Correct! You guessed the number!");
            } else if (attemptsLeft == 0) {
                showEndDialog("❌ Out of chances! The number was: " + secretNumber);
            } else {
                String hint = (guess < secretNumber) ? "Too low!" : "Too high!";
                messageLabel.setText(hint + " You have " + attemptsLeft + " tries left.");
            }

            guessField.setText("");

        } catch (NumberFormatException e) {
            messageLabel.setText("Invalid input. Please enter a number.");
        }
    }

    // Show end-of-game dialog and offer restart
    private void showEndDialog(String message) {
        int option = JOptionPane.showOptionDialog(
            this,
            message + "\nDo you want to play again?",
            "Game Over",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[]{"Play Again", "Exit"},
            "Play Again"
        );

        if (option == JOptionPane.YES_OPTION) {
            cardLayout.show(mainPanel, "welcome");
        } else {
            System.exit(0);
        }
    }
}
