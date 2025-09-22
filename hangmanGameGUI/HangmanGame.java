package hangmanGameGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class HangmanGame extends JFrame {
	private final String[] wordList = {
	        "JAVA", "SWING", "OBJECT", "PYTHON", "VARIABLE", "FUNCTION", "PROGRAM"
	    };

	    private String selectedWord;
	    private char[] displayedWord;
	    private Set<Character> guessedLetters = new HashSet<>();
	    private Set<Character> wrongGuesses = new HashSet<>();
	    private int lives = 6;

	    // GUI components
	    private JLabel wordLabel;
	    private JLabel livesLabel;
	    private JLabel wrongLabel;
	    private JTextField inputField;
	    private JButton guessButton;

	    public HangmanGame() {
	        setTitle("Hangman Game");
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        setSize(500, 300);
	        setLocationRelativeTo(null);

	        initializeGame();
	        initializeUI();

	        setVisible(true);
	    }

	    private void initializeGame() {
	        selectedWord = wordList[new Random().nextInt(wordList.length)];
	        displayedWord = new char[selectedWord.length()];
	        Arrays.fill(displayedWord, '_');
	        guessedLetters.clear();
	        wrongGuesses.clear();
	        lives = 6;
	    }

	    private void initializeUI() {
	        setLayout(new BorderLayout(10, 10));
	        JPanel centerPanel = new JPanel(new GridLayout(4, 1, 10, 10));
	        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

	        wordLabel = new JLabel(getDisplayedWord(), SwingConstants.CENTER);
	        wordLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
	        centerPanel.add(wordLabel);

	        livesLabel = new JLabel("Lives left: " + lives, SwingConstants.CENTER);
	        livesLabel.setFont(new Font("Arial", Font.PLAIN, 16));
	        centerPanel.add(livesLabel);

	        wrongLabel = new JLabel("Wrong guesses: ", SwingConstants.CENTER);
	        centerPanel.add(wrongLabel);

	        JPanel inputPanel = new JPanel();
	        inputField = new JTextField(5);
	        guessButton = new JButton("Guess");

	        inputPanel.add(new JLabel("Enter a letter: "));
	        inputPanel.add(inputField);
	        inputPanel.add(guessButton);
	        centerPanel.add(inputPanel);

	        add(centerPanel, BorderLayout.CENTER);

	        // Action listener for both the button and Enter key
	        ActionListener guessAction = e -> {
	            String text = inputField.getText().trim().toUpperCase();
	            if (text.length() != 1 || !Character.isLetter(text.charAt(0))) {
	                JOptionPane.showMessageDialog(this, "Please enter a single letter (A-Z).");
	                inputField.setText("");
	                return;
	            }
	            handleGuess(text.charAt(0));
	            inputField.setText("");
	            inputField.requestFocusInWindow();
	        };

	        guessButton.addActionListener(guessAction);
	        inputField.addActionListener(guessAction);
	    }

	    private void handleGuess(char guess) {
	        if (guessedLetters.contains(guess) || wrongGuesses.contains(guess)) {
	            JOptionPane.showMessageDialog(this, "You already guessed '" + guess + "'");
	            return;
	        }

	        if (selectedWord.indexOf(guess) >= 0) {
	            guessedLetters.add(guess);
	            for (int i = 0; i < selectedWord.length(); i++) {
	                if (selectedWord.charAt(i) == guess) {
	                    displayedWord[i] = guess;
	                }
	            }
	        } else {
	            wrongGuesses.add(guess);
	            lives--;
	        }

	        updateDisplay();

	        if (String.valueOf(displayedWord).equals(selectedWord)) {
	            showEndDialog("🎉 You won! The word was: " + selectedWord);
	        } else if (lives == 0) {
	            showEndDialog("💀 You lost! The word was: " + selectedWord);
	        }
	    }

	    private void updateDisplay() {
	        wordLabel.setText(getDisplayedWord());
	        livesLabel.setText("Lives left: " + lives);
	        wrongLabel.setText("Wrong guesses: " + wrongGuesses);
	    }

	    private String getDisplayedWord() {
	        StringBuilder sb = new StringBuilder();
	        for (char c : displayedWord) {
	            sb.append(c).append(' ');
	        }
	        return sb.toString();
	    }

	    private void showEndDialog(String message) {
	        int choice = JOptionPane.showOptionDialog(
	                this,
	                message + "\nPlay again?",
	                "Game Over",
	                JOptionPane.YES_NO_OPTION,
	                JOptionPane.INFORMATION_MESSAGE,
	                null,
	                new String[]{"Restart", "Exit"},
	                "Restart"
	        );

	        if (choice == JOptionPane.YES_OPTION) {
	            initializeGame();
	            updateDisplay();
	        } else {
	            System.exit(0);
	        }
	    }
}
