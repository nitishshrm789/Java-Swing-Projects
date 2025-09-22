package brickBreakerGame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BrickBreakerGame extends JFrame {

	public BrickBreakerGame() {
        setTitle("Brick Breaker");
        setSize(800, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(new GamePanel());
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new BrickBreakerGame();
    }
}

class GamePanel extends JPanel implements KeyListener, ActionListener {
    private boolean play = true;
    private int score = 0, totalBricks = 21;
    private Timer timer;
    private final int delay = -500;
    private int playerX = 310;
    private int ballX = 120, ballY = 350;
    private int ballXDir = -1, ballYDir = -2;
    private MapGenerator map;

    public GamePanel() {
        map = new MapGenerator(3, 7);
        setFocusable(true);
        addKeyListener(this);
        setFocusTraversalKeysEnabled(false);
        timer = new Timer(delay, this);
        timer.start();
    }

    @Override
    public void paint(Graphics g) {
        // Background
        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        // Draw bricks
        map.draw((Graphics2D) g);

        // Borders
        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        // Paddle
        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        // Ball
        g.setColor(Color.yellow);
        g.fillOval(ballX, ballY, 20, 20);

        // Score display
        g.setColor(Color.white);
        g.setFont(new Font("Verdana", Font.BOLD, 25));
        g.drawString("Score: " + score, 530, 30);

        // Win condition
        if (totalBricks <= 0) {
            play = false;
            ballXDir = ballYDir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("Verdana", Font.BOLD, 30));
            g.drawString("You Won! Score: " + score, 190, 300);
            g.setFont(new Font("Verdana", Font.BOLD, 20));
            g.drawString("Press Enter to restart.", 230, 350);
        }

        // Game over condition
        if (ballY > 570) {
            play = false;
            ballXDir = ballYDir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("Verdana", Font.BOLD, 30));
            g.drawString("Game Over! Score: " + score, 190, 300);
            g.setFont(new Font("Verdana", Font.BOLD, 20));
            g.drawString("Press Enter to restart", 230, 350);
        }
        
        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (play) {
            if (new Rectangle(ballX, ballY, 20, 20)
                    .intersects(new Rectangle(playerX, 550, 100, 8))) {
                ballYDir = -ballYDir;
            }

            outer:
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;
                        int width = map.brickWidth;
                        int height = map.brickHeight;

                        Rectangle ballRect = new Rectangle(ballX, ballY, 20, 20);
                        Rectangle brickRect = new Rectangle(brickX, brickY, width, height);

                        if (ballRect.intersects(brickRect)) {
                            map.setBrickValue(0, i, j);
                            totalBricks--;
                            score += 10;

                            if (ballX + 19 <= brickRect.x || ballX + 1 >= brickRect.x + width)
                                ballXDir = -ballXDir;
                            else
                                ballYDir = -ballYDir;

                            break outer;
                        }
                    }
                }
            }

            ballX += ballXDir;
            ballY += ballYDir;

            if (ballX < 0) ballXDir = -ballXDir;
            if (ballY < 0) ballYDir = -ballYDir;
            if (ballX > 670) ballXDir = -ballXDir;
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX < 600) playerX += 20;
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX > 10) playerX -= 20;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                resetGame();
            }
        }
    }

    private void resetGame() {
        play = true;
        ballX = 120; ballY = 350;
        ballXDir = -1; ballYDir = -2;
        playerX = 310;
        score = 0;
        totalBricks = 21;
        map = new MapGenerator(3, 7);
        repaint();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}

class MapGenerator {
    public int map[][];
    public int brickWidth, brickHeight;

    public MapGenerator(int rows, int cols) {
        map = new int[rows][cols];
        for (int[] row : map)
            java.util.Arrays.fill(row, 1);
        brickWidth = 540 / cols;
        brickHeight = 150 / rows;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (map[i][j] > 0) {
                    int x = j * brickWidth + 80, y = i * brickHeight + 50;
                    g.setColor(new Color(0xFFAA33));
                    g.fillRect(x, y, brickWidth, brickHeight);
                    g.setStroke(new BasicStroke(4));
                    g.setColor(Color.BLACK);
                    g.drawRect(x, y, brickWidth, brickHeight);
                }
            }
        }
    }

    public void setBrickValue(int val, int row, int col) {
        map[row][col] = val;
    }
	
}
