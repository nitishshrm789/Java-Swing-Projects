package diceGameWithAI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.util.Random;

public class DiceGameWithAI extends JFrame {
	private JLabel lblPlayerTurn, lblP1Score, lblP2Score;
    private JLabel playerDiceLabel = new JLabel();
    private JLabel aiDiceLabel = new JLabel();
    private JLabel playerResultLabel = new JLabel("You rolled: --", SwingConstants.CENTER);
    private JLabel aiResultLabel = new JLabel("AI rolled: --", SwingConstants.CENTER);
    private JButton btnRoll;
    private int scoreP1 = 0, scoreP2 = 0;
    private Random random = new Random();

    private final ImageIcon[] diceIcons = new ImageIcon[6];

    public DiceGameWithAI() {
        setTitle("Dice Game vs AI");
        setSize(500, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Load dice images
        for (int i = 0; i < 6; i++) {
            diceIcons[i] = new ImageIcon("dice" + (i+1) + ".png");
        }

        // Top: Turn and scores
        JPanel top = new JPanel(new GridLayout(3,1));
        lblPlayerTurn = new JLabel("Current Turn: You", SwingConstants.CENTER);
        lblP1Score = new JLabel("Your Score: 0", SwingConstants.CENTER);
        lblP2Score = new JLabel("AI Score: 0", SwingConstants.CENTER);
        top.add(lblPlayerTurn);
        top.add(lblP1Score);
        top.add(lblP2Score);
        add(top, BorderLayout.NORTH);

        // Center: Dice + result layout using GridBagLayout
        JPanel center = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        // Player result label
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(10, 30, 5, 30);
        gbc.anchor = GridBagConstraints.WEST;
        center.add(playerResultLabel, gbc);

        // AI result label
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        center.add(aiResultLabel, gbc);

        // Player dice image
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.WEST;
        center.add(playerDiceLabel, gbc);

        // AI dice image
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        center.add(aiDiceLabel, gbc);

        add(center, BorderLayout.CENTER);

        // Bottom: Roll button
        btnRoll = new JButton("Roll Dice");
        btnRoll.addActionListener(e -> playerRoll());
        add(btnRoll, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void playerRoll() {
        btnRoll.setEnabled(false);
        animateRoll(playerDiceLabel, result -> {
            playerResultLabel.setText("You rolled: " + result);
            scoreP1 += result;
            lblP1Score.setText("Your Score: " + scoreP1);
            if (scoreP1 >= 100) {
                endGame("You");
            } else {
                lblPlayerTurn.setText("Current Turn: AI");
                new Timer(800, ev -> {
                    ((Timer)ev.getSource()).stop();
                    aiRoll();
                }).start();
            }
        });
    }

    private void aiRoll() {
        animateRoll(aiDiceLabel, result -> {
            aiResultLabel.setText("AI rolled: " + result);
            scoreP2 += result;
            lblP2Score.setText("AI Score: " + scoreP2);
            if (scoreP2 >= 100) {
                endGame("AI");
            } else {
                lblPlayerTurn.setText("Current Turn: You");
                btnRoll.setEnabled(true);
            }
        });
    }

    private void animateRoll(JLabel label, java.util.function.IntConsumer onComplete) {
        Timer t = new Timer(100, null);
        t.addActionListener(e -> {
            int face = random.nextInt(6);
            label.setIcon(diceIcons[face]);
            if (random.nextInt(10) == 0) {
                t.stop();
                onComplete.accept(face + 1);
            }
        });
        t.start();
    }

    private void endGame(String winner) {
        btnRoll.setEnabled(false);
        JOptionPane.showMessageDialog(this, winner + " wins!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
