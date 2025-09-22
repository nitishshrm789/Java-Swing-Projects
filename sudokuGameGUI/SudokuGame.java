package sudokuGameGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SudokuGame extends JFrame {
    private static final int GRID_SIZE = 9;
    private static final int CELL_SIZE = 60;
    private static final int[][] BOARD = {
        {5, 3, 0, 0, 7, 0, 0, 0, 0},
        {6, 0, 0, 1, 9, 5, 0, 0, 0},
        {0, 9, 8, 0, 0, 0, 0, 6, 0},
        {8, 0, 0, 0, 6, 0, 0, 0, 3},
        {4, 0, 0, 8, 0, 3, 0, 0, 1},
        {7, 0, 0, 0, 2, 0, 0, 0, 6},
        {0, 6, 0, 0, 0, 0, 2, 8, 0},
        {0, 0, 0, 4, 1, 9, 0, 0, 5},
        {0, 0, 0, 0, 8, 0, 0, 7, 9}
    };

    private JTextField[][] cells;

    public SudokuGame() {
        setTitle("Sudoku");
        setLayout(new BorderLayout());
        JPanel gridPanel = new JPanel(new GridLayout(GRID_SIZE, GRID_SIZE));

        cells = new JTextField[GRID_SIZE][GRID_SIZE];
        Font font = new Font("SansSerif", Font.BOLD, 20);

        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                JTextField tf = new JTextField();
                tf.setHorizontalAlignment(JTextField.CENTER);
                tf.setFont(font);
                tf.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                tf.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                if (BOARD[r][c] != 0) {
                    tf.setText(String.valueOf(BOARD[r][c]));
                    tf.setEditable(false);
                    tf.setBackground(Color.LIGHT_GRAY);
                }

                // Apply thicker borders for 3x3 subgrid separation
                int top = (r % 3 == 0) ? 3 : 1;
                int left = (c % 3 == 0) ? 3 : 1;
                int bottom = (r == GRID_SIZE - 1) ? 3 : 1;
                int right = (c == GRID_SIZE - 1) ? 3 : 1;
                tf.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));

                cells[r][c] = tf;
                gridPanel.add(tf);
            }
        }

        JButton btnSolve = new JButton("Solve");
        btnSolve.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[][] currentBoard = getCurrentBoard();
                if (solveSudoku(currentBoard)) {
                    updateBoard(currentBoard);
                    JOptionPane.showMessageDialog(SudokuGame.this, "Puzzle Solved!");
                    gridPanel.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(SudokuGame.this, "No solution found.");
                }
            }
        });

        add(gridPanel, BorderLayout.CENTER);
        add(btnSolve, BorderLayout.SOUTH);
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    private int[][] getCurrentBoard() {
        int[][] currentBoard = new int[GRID_SIZE][GRID_SIZE];
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                String text = cells[r][c].getText();
                currentBoard[r][c] = text.isEmpty() ? 0 : Integer.parseInt(text);
            }
        }
        return currentBoard;
    }

    private void updateBoard(int[][] board) {
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                cells[r][c].setText(board[r][c] == 0 ? "" : String.valueOf(board[r][c]));
            }
        }
    }

    private boolean solveSudoku(int[][] board) {
        for (int r = 0; r < GRID_SIZE; r++) {
            for (int c = 0; c < GRID_SIZE; c++) {
                if (board[r][c] == 0) {
                    for (int num = 1; num <= 9; num++) {
                        if (isSafe(board, r, c, num)) {
                            board[r][c] = num;
                            if (solveSudoku(board)) {
                                return true;
                            }
                            board[r][c] = 0;
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isSafe(int[][] board, int row, int col, int num) {
        for (int x = 0; x < GRID_SIZE; x++) {
            if (board[row][x] == num || board[x][col] == num) {
                return false;
            }
        }

        int startRow = row - row % 3, startCol = col - col % 3;
        for (int r = 0; r < 3; r++) {
            for (int c = 0; c < 3; c++) {
                if (board[r + startRow][c + startCol] == num) {
                    return false;
                }
            }
        }

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SudokuGame();
            }
        });
    }
}

