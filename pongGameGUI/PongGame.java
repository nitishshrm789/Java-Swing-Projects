package pongGameGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PongGame extends JPanel implements ActionListener, KeyListener {
	private final int WIDTH = 800, HEIGHT = 600;
    private final int PADDLE_WIDTH = 10, PADDLE_HEIGHT = 100;
    private final int BALL_SIZE = 15;
    private final int PADDLE_SPEED = 15, BALL_SPEED = 5;

    private int player1Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private int player2Y = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private int ballX = WIDTH / 2 - BALL_SIZE / 2, ballY = HEIGHT / 2 - BALL_SIZE / 2;
    private int ballDX = BALL_SPEED, ballDY = BALL_SPEED;

    private int score1 = 0, score2 = 0;

    public PongGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);

        Timer timer = new Timer(20, this);
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw paddles
        g.setColor(Color.WHITE);
        g.fillRect(10, player1Y, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillRect(WIDTH - 20, player2Y, PADDLE_WIDTH, PADDLE_HEIGHT);

        // Draw ball
        g.fillRect(ballX, ballY, BALL_SIZE, BALL_SIZE);

        // Draw scores
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString(String.valueOf(score1), WIDTH / 4, 50);
        g.drawString(String.valueOf(score2), 3 * WIDTH / 4, 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveBall();
        movePaddles();
        repaint();
    }

    private void moveBall() {
        ballX += ballDX;
        ballY += ballDY;

        // Ball collision with top and bottom walls
        if (ballY <= 0 || ballY >= HEIGHT - BALL_SIZE) {
            ballDY = -ballDY;
        }

        // Ball collision with paddles
        if (ballX <= PADDLE_WIDTH + 10 && ballY + BALL_SIZE >= player1Y && ballY <= player1Y + PADDLE_HEIGHT) {
            ballDX = -ballDX;
            score1++;
        }
        if (ballX >= WIDTH - PADDLE_WIDTH - 20 && ballY + BALL_SIZE >= player2Y && ballY <= player2Y + PADDLE_HEIGHT) {
            ballDX = -ballDX;
            score2++;
        }

        // Ball out of bounds
        if (ballX <= 0 || ballX >= WIDTH - BALL_SIZE) {
            if (ballX <= 0) score2++;
            else score1++;
            resetBall();
        }
    }

    private void movePaddles() {
        // Player 1 controls (W/S)
        if (player1Y > 0) player1Y -= PADDLE_SPEED;
        if (player1Y < HEIGHT - PADDLE_HEIGHT) player1Y += PADDLE_SPEED;

        // Player 2 controls (Up/Down)
        if (player2Y > 0) player2Y -= PADDLE_SPEED;
        if (player2Y < HEIGHT - PADDLE_HEIGHT) player2Y += PADDLE_SPEED;
    }

    private void resetBall() {
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;
        ballDX = BALL_SPEED;
        ballDY = BALL_SPEED;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Player 1 controls
        if (key == KeyEvent.VK_W && player1Y > 0) player1Y -= PADDLE_SPEED;
        if (key == KeyEvent.VK_S && player1Y < HEIGHT - PADDLE_HEIGHT) player1Y += PADDLE_SPEED;

        // Player 2 controls
        if (key == KeyEvent.VK_UP && player2Y > 0) player2Y -= PADDLE_SPEED;
        if (key == KeyEvent.VK_DOWN && player2Y < HEIGHT - PADDLE_HEIGHT) player2Y += PADDLE_SPEED;
    }

    @Override
    public void keyReleased(KeyEvent e) {}
    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pong Game");
        PongGame game = new PongGame();
        frame.add(game);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
