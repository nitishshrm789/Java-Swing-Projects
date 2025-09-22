package pongGameGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PongGameAI extends JPanel implements ActionListener, KeyListener {

	private final int WIDTH = 800, HEIGHT = 600;
    private final int PADDLE_WIDTH = 10, PADDLE_HEIGHT = 100;
    private final int BALL_SIZE = 15;
    private final int BALL_SPEED = 5, AI_SPEED = 4;

    private int playerY = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private int aiY = HEIGHT / 2 - PADDLE_HEIGHT / 2;
    private int ballX = WIDTH / 2 - BALL_SIZE / 2, ballY = HEIGHT / 2 - BALL_SIZE / 2;
    private int ballDX = BALL_SPEED, ballDY = BALL_SPEED;

    private int scorePlayer = 0, scoreAI = 0;

    public PongGameAI() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        new Timer(17, this).start(); // ~60 FPS
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.WHITE);
        g.fillRect(10, playerY, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillRect(WIDTH - 20, aiY, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

        g.setFont(new Font("Arial", Font.BOLD, 36));
        g.drawString(String.valueOf(scorePlayer), WIDTH / 4, 50);
        g.drawString(String.valueOf(scoreAI), 3 * WIDTH / 4, 50);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        moveBall();
        moveAI();
        repaint();
    }

    private void moveBall() {
        ballX += ballDX;
        ballY += ballDY;

        // Bounce off top/bottom
        if (ballY <= 0 || ballY >= HEIGHT - BALL_SIZE) ballDY = -ballDY;

        // Player paddle collision
        if (ballX <= 20 && ballY + BALL_SIZE >= playerY && ballY <= playerY + PADDLE_HEIGHT)
            ballDX = -ballDX;

        // AI paddle collision
        if (ballX + BALL_SIZE >= WIDTH - 20 && ballY + BALL_SIZE >= aiY && ballY <= aiY + PADDLE_HEIGHT)
            ballDX = -ballDX;

        // Left/right boundary
        if (ballX < 0) {
            scoreAI++;
            resetBall();
        } else if (ballX > WIDTH - BALL_SIZE) {
            scorePlayer++;
            resetBall();
        }
    }

    private void moveAI() {
        int aiCenter = aiY + PADDLE_HEIGHT / 2;
        if (ballY < aiCenter && aiY > 0) {
            aiY -= AI_SPEED;
        } else if (ballY > aiCenter && aiY < HEIGHT - PADDLE_HEIGHT) {
            aiY += AI_SPEED;
        }
    }

    private void resetBall() {
        ballX = WIDTH / 2 - BALL_SIZE / 2;
        ballY = HEIGHT / 2 - BALL_SIZE / 2;
        ballDX = -ballDX;
        ballDY = BALL_SPEED * (Math.random() > 0.5 ? 1 : -1);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_UP && playerY > 0) playerY -= 15;
        if (key == KeyEvent.VK_DOWN && playerY < HEIGHT - PADDLE_HEIGHT) playerY += 15;
    }
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Pong vs AI");
        frame.add(new PongGameAI());
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
	
}
