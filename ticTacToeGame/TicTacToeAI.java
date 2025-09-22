package ticTacToeGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class TicTacToeAI extends JFrame {
	private JButton[][] buttons = new JButton[3][3];
    private boolean playerTurn = true; // true = player's turn, false = computer
    private boolean gameOver = false;

    public TicTacToeAI() {
        setTitle("Tic Tac Toe - Player vs Computer");
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
        if (!playerTurn || gameOver) return;

        JButton btn = buttons[row][col];
        if (!btn.getText().equals("")) return; // already played

        btn.setText("X");
        playerTurn = false;

        if (checkWin("X")) {
            endGame("🎉 You Win!");
        } else if (isBoardFull()) {
            endGame("🤝 It's a Draw!");
        } else {
            SwingUtilities.invokeLater(this::handleComputerMove);
        }
    }

    private void handleComputerMove() {
        if (gameOver) return;

        List<Point> available = getEmptyCells();
        if (available.isEmpty()) return;

        // Basic AI: random move
        Point move = available.get(new Random().nextInt(available.size()));
        JButton btn = buttons[move.x][move.y];
        btn.setText("O");

        if (checkWin("O")) {
            endGame("💻 Computer Wins!");
        } else if (isBoardFull()) {
            endGame("🤝 It's a Draw!");
        } else {
            playerTurn = true;
        }
    }

    private List<Point> getEmptyCells() {
        List<Point> empty = new ArrayList<>();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (buttons[row][col].getText().equals("")) {
                    empty.add(new Point(row, col));
                }
            }
        }
        return empty;
    }

    private boolean checkWin(String symbol) {
        // Rows, Columns, Diagonals
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

        int option = JOptionPane.showOptionDialog(
            this,
            message + "\nDo you want to play again?",
            "Game Over",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            new String[] { "Play Again", "Exit" },
            "Play Again"
        );

        if (option == JOptionPane.YES_OPTION) {
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
        playerTurn = true;
        gameOver = false;
    }
}
