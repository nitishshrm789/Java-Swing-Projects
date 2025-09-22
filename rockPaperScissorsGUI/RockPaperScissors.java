package rockPaperScissorsGUI;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class RockPaperScissors extends JFrame {
	private JLabel playerImageLabel;
    private JLabel computerImageLabel;
    private JLabel resultLabel;

    private final String[] choices = { "Rock", "Paper", "Scissors" };
    private final Random random = new Random();

    public RockPaperScissors() {
        setTitle("Rock, Paper, Scissors with Images");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        initializeUI();
        setVisible(true);
    }

    private void initializeUI() {
        // Buttons Panel
        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton rockButton = new JButton("Rock");
        JButton paperButton = new JButton("Paper");
        JButton scissorsButton = new JButton("Scissors");

        rockButton.addActionListener(e -> playRound("Rock"));
        paperButton.addActionListener(e -> playRound("Paper"));
        scissorsButton.addActionListener(e -> playRound("Scissors"));

        buttonPanel.add(rockButton);
        buttonPanel.add(paperButton);
        buttonPanel.add(scissorsButton);
        add(buttonPanel, BorderLayout.NORTH);

        // Images & Result Panel
        JPanel imagesPanel = new JPanel(new GridLayout(1, 2, 20, 10));

        playerImageLabel = new JLabel("Your Choice", SwingConstants.CENTER);
        playerImageLabel.setVerticalTextPosition(JLabel.BOTTOM);
        playerImageLabel.setHorizontalTextPosition(JLabel.CENTER);
        playerImageLabel.setFont(new Font("Arial", Font.BOLD, 16));

        computerImageLabel = new JLabel("Computer's Choice", SwingConstants.CENTER);
        computerImageLabel.setVerticalTextPosition(JLabel.BOTTOM);
        computerImageLabel.setHorizontalTextPosition(JLabel.CENTER);
        computerImageLabel.setFont(new Font("Arial", Font.BOLD, 16));

        imagesPanel.add(playerImageLabel);
        imagesPanel.add(computerImageLabel);

        add(imagesPanel, BorderLayout.CENTER);

        // Result label
        resultLabel = new JLabel("Make your move!", SwingConstants.CENTER);
        resultLabel.setFont(new Font("Arial", Font.BOLD, 18));
        add(resultLabel, BorderLayout.SOUTH);
    }

    private void playRound(String playerChoice) {
        String computerChoice = choices[random.nextInt(3)];

        // Set images
        playerImageLabel.setIcon(loadImageIcon(playerChoice));
        playerImageLabel.setText("Your Choice: " + playerChoice);

        computerImageLabel.setIcon(loadImageIcon(computerChoice));
        computerImageLabel.setText("Computer's Choice: " + computerChoice);

        // Show result
        resultLabel.setText(determineWinner(playerChoice, computerChoice));
    }

    private ImageIcon loadImageIcon(String choice) {
        String path = switch (choice) {
            case "Rock" -> "Rock.png";
            case "Paper" -> "Paper.png";
            case "Scissors" -> "Scissors.png";
            default -> "";
        };
        ImageIcon icon = new ImageIcon(path);
        // Optional: scale image to fit label nicely
        Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImage);
    }

    private String determineWinner(String player, String computer) {
        if (player.equals(computer)) return "It's a Draw!";

        return switch (player) {
            case "Rock" -> computer.equals("Scissors") ? "You Win! Paper beats Scissors" : "You Lose! Paper beats Rock";
            case "Paper" -> computer.equals("Rock") ? "You Win! Paper beats Rock" : "You Lose! Scissors beats Paper";
            case "Scissors" -> computer.equals("Paper") ? "You Win! Scissors beats Paper" : "You Lose! Rock beats Scissors";
            default -> "Error";
        };
    }
}
