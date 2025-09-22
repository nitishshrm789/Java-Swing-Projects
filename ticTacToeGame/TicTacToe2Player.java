package ticTacToeGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TicTacToe2Player extends JFrame {
	private JButton[][] buttons = new JButton[3][3];
    private boolean isPlayerXTurn = true;
    private boolean gameOver = false;

    public TicTacToe2Player() {
        setTitle("Tic Tac Toe - 2 Player");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        initializeBoard();
        setVisible(true);
    }

    private void initializeBoard() {
        setLayout(new GridLayout(3, 3));
        Font font = new Font("Arial", Font.BOLD, 60);

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                JButton btn = new JButton("");
                btn.setFont(font);
                btn.setFocusPainted(false);
                int r = row, c = col;
                btn.addActionListener(e -> handlePlayerMove(r, c));
                buttons[row][col] = btn;
                add(btn);
            }
        }
    }

    private void handlePlayerMove(int row, int col) {
        if (gameOver) return;

        JButton btn = buttons[row][col];
        if (!btn.getText().equals("")) return; // already clicked

        btn.setText(isPlayerXTurn ? "X" : "O");

        if (checkWin(isPlayerXTurn ? "X" : "O")) {
            endGame((isPlayerXTurn ? "Player X" : "Player O") + " wins! 🎉");
        } else if (isBoardFull()) {
            endGame("It's a Draw! 🤝");
        } else {
            isPlayerXTurn = !isPlayerXTurn;
        }
    }

    private boolean checkWin(String symbol) {
        for (int i = 0; i < 3; i++) {
            if (
                buttons[i][0].getText().equals(symbol) &&
                buttons[i][1].getText().equals(symbol) &&
                buttons[i][2].getText().equals(symbol)
            ) return true;

            if (
                buttons[0][i].getText().equals(symbol) &&
                buttons[1][i].getText().equals(symbol) &&
                buttons[2][i].getText().equals(symbol)
            ) return true;
        }

        if (
            buttons[0][0].getText().equals(symbol) &&
            buttons[1][1].getText().equals(symbol) &&
            buttons[2][2].getText().equals(symbol)
        ) return true;

        if (
            buttons[0][2].getText().equals(symbol) &&
            buttons[1][1].getText().equals(symbol) &&
            buttons[2][0].getText().equals(symbol)
        ) return true;

        return false;
    }

    private boolean isBoardFull() {
        for (JButton[] row : buttons) {
            for (JButton btn : row) {
                if (btn.getText().equals("")) return false;
            }
        }
        return true;
    }

    private void endGame(String message) {
        gameOver = true;

        int choice = JOptionPane.showOptionDialog(
            this,
            message + "\nPlay again?",
            "Game Over",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[] { "Play Again", "Exit" },
            "Play Again"
        );

        if (choice == JOptionPane.YES_OPTION) {
            restartGame();
        } else {
            System.exit(0);
        }
    }

    private void restartGame() {
        for (JButton[] row : buttons) {
            for (JButton btn : row) {
                btn.setText("");
            }
        }
        gameOver = false;
        isPlayerXTurn = true;
    }
}
