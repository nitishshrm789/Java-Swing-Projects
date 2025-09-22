package ludoBoardsquareGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class LudoBoard extends JFrame {

    public LudoBoard() {
        setTitle("Ludo Game (Simple)");
        setSize(650, 730);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        BoardPanel board = new BoardPanel();
        add(board, BorderLayout.CENTER);

        JPanel control = new JPanel();
        JLabel status = new JLabel("Player 1's turn");
        JLabel diceLabel = new JLabel("Dice: -");
        JButton roll = new JButton("Roll Dice");

        roll.addActionListener(e -> {
            board.rollDice();
            board.repaint();
            status.setText(board.getStatus());
            diceLabel.setText("Dice: " + board.getDice());
        });

        control.add(status);
        control.add(roll);
        control.add(diceLabel);
        add(control, BorderLayout.SOUTH);

        setVisible(true);
    }

    class BoardPanel extends JPanel {
        private final int TILE = 40;
        private final int boardSize = 52;
        private final int[][] pos = new int[4][1]; // Each player’s single token
        private int currentPlayer = 0;
        private int dice = 0;
        private final Random rand = new Random();

        BoardPanel() {
            setPreferredSize(new Dimension(600, 600));
            for (int i = 0; i < 4; i++) pos[i][0] = -1;
        }

        void rollDice() {
            dice = rand.nextInt(6) + 1;
            int p = currentPlayer;
            int old = pos[p][0];
            boolean moved = false;

            if (old == -1 && dice == 6) {
                pos[p][0] = p * 13;
                moved = true;
            } else if (old >= 0) {
                pos[p][0] = (old + dice) % boardSize;
                moved = true;
            }

            if (moved) capture(p, pos[p][0]);
            if (dice != 6 || !moved) currentPlayer = (currentPlayer + 1) % 4;
        }

        int getDice() {
            return dice;
        }

        String getStatus() {
            for (int i = 0; i < 4; i++) {
                if (pos[i][0] >= 0 && (pos[i][0] + boardSize * 0) % boardSize == (i * 13 + boardSize) % boardSize) {
                    return "Player " + (i + 1) + " wins!";
                }
            }
            return "Player " + (currentPlayer + 1) + "'s turn";
        }

        private void capture(int player, int location) {
            for (int p = 0; p < 4; p++) {
                if (p != player && pos[p][0] == location) {
                    pos[p][0] = -1;
                    JOptionPane.showMessageDialog(this, "Player " + (player + 1) +
                            " captured Player " + (p + 1) + "!");
                }
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawBoard(g);
            drawTokens(g);
        }

        private void drawBoard(Graphics g) {
            g.setColor(Color.WHITE);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i < boardSize; i++) {
                Point pt = tileLocation(i);
                g.fillRect(pt.x, pt.y, TILE, TILE);
                g.setColor(Color.BLACK);
                g.drawRect(pt.x, pt.y, TILE, TILE);
                g.setColor(Color.LIGHT_GRAY);
            }
            // Home areas
            g.setColor(Color.RED);   g.fillRect(0, 0, TILE * 3, TILE * 3);
            g.setColor(Color.GREEN); g.fillRect(TILE * 9, 0, TILE * 3, TILE * 3);
            g.setColor(Color.YELLOW);g.fillRect(0, TILE * 9, TILE * 3, TILE * 3);
            g.setColor(Color.BLUE);  g.fillRect(TILE * 9, TILE * 9, TILE * 3, TILE * 3);
        }

        private void drawTokens(Graphics g) {
            Color[] cols = {Color.RED, Color.GREEN, Color.YELLOW, Color.BLUE};
            for (int p = 0; p < 4; p++) {
                int loc = pos[p][0];
                if (loc < 0) continue;
                Point pt = tileLocation(loc);
                g.setColor(cols[p]);
                g.fillOval(pt.x + 5, pt.y + 5, TILE - 10, TILE - 10);
                g.setColor(Color.BLACK);
                g.drawOval(pt.x + 5, pt.y + 5, TILE - 10, TILE - 10);
            }
        }

        private Point tileLocation(int idx) {
            int[][] fixed = {
                    {3,0},{3,1},{3,2},{3,3},{3,4},{3,5},
                    {2,6},{1,6},{0,6},{0,7},{0,8},{1,8},{2,8},
                    {3,9},{3,10},{3,11},{3,12},{3,13},{3,14},
                    {4,14},{5,14},{6,14},{6,13},{6,12},{6,11},{6,10},
                    {7,9},{8,8},{8,7},{8,6},{9,6},{10,6},
                    {11,6},{12,6},{13,6},{14,6},{14,5},{14,4},
                    {14,3},{13,3},{12,3},{11,3},{10,3},{9,3},
                    {8,2},{8,1},{8,0},{7,0},{6,0},{6,1},{6,2},{6,3}
            };
            int r = fixed[idx % boardSize][0], c = fixed[idx % boardSize][1];
            return new Point(c * TILE, r * TILE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LudoBoard::new);
    }
}



