package snakeGameGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JPanel implements ActionListener {
    static final int WIDTH = 600, HEIGHT = 600, UNIT = 20;
    static final int GAME_UNITS = (WIDTH * HEIGHT) / (UNIT * UNIT);
    static final int DELAY = 120;

    final int[] snakeX = new int[GAME_UNITS];
    final int[] snakeY = new int[GAME_UNITS];
    int snakeLength = 3;
    int foodX, foodY;
    char direction = 'R';
    boolean running = false;
    Timer timer;
    Random random;
    JButton playAgainBtn;
    
    private JButton playAgainButton;

    public SnakeGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.black);
        setFocusable(true);
        random = new Random();
        startGame();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT -> direction = direction != 'R' ? 'L' : direction;
                    case KeyEvent.VK_RIGHT -> direction = direction != 'L' ? 'R' : direction;
                    case KeyEvent.VK_UP -> direction = direction != 'D' ? 'U' : direction;
                    case KeyEvent.VK_DOWN -> direction = direction != 'U' ? 'D' : direction;
                }
            }
        });
        
        playAgainButton = new JButton("Play Again");
        playAgainButton.setBounds(WIDTH / 2 - 75, HEIGHT / 2 + 50, 150, 30);
        playAgainButton.setVisible(false);
        playAgainButton.addActionListener(e -> restartGame());
        add(playAgainButton);
    }

    public void startGame() {
        newFood();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();
    }

    public void newFood() {
        foodX = random.nextInt(WIDTH / UNIT) * UNIT;
        foodY = random.nextInt(HEIGHT / UNIT) * UNIT;
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        if (running) {
            g.setColor(Color.red);
            g.fillOval(foodX, foodY, UNIT, UNIT);

            for (int i = 0; i < snakeLength; i++) {
                g.setColor(i == 0 ? Color.green : new Color(45, 180, 0));
                g.fillRect(snakeX[i], snakeY[i], UNIT, UNIT);
            }

            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 18));
            g.drawString("Score: " + (snakeLength - 3), 10, 20);

        } else {
            gameOver(g);
        }
    }

    private void move() {
        for (int i = snakeLength; i > 0; i--) {
            snakeX[i] = snakeX[i - 1];
            snakeY[i] = snakeY[i - 1];
        }

        switch (direction) {
            case 'U' -> snakeY[0] -= UNIT;
            case 'D' -> snakeY[0] += UNIT;
            case 'L' -> snakeX[0] -= UNIT;
            case 'R' -> snakeX[0] += UNIT;
        }
    }

    private void checkFoodCollision() {
        if (snakeX[0] == foodX && snakeY[0] == foodY) {
            snakeLength++;
            newFood();
        }
    }

    private void checkCollisions() {
        // self collision
        for (int i = snakeLength; i > 0; i--) {
            if (snakeX[0] == snakeX[i] && snakeY[0] == snakeY[i]) running = false;
        }
        // wall collision
        if (snakeX[0] < 0 || snakeX[0] >= WIDTH || snakeY[0] < 0 || snakeY[0] >= HEIGHT)
            running = false;

        if (!running) timer.stop();
    }

    private void gameOver(Graphics g) {
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = getFontMetrics(g.getFont());
        String msg = "Game Over";
        g.drawString(msg, (WIDTH - fm.stringWidth(msg)) / 2, HEIGHT / 2);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String scoreMsg = "Score: " + (snakeLength - 3);
        g.drawString(scoreMsg, (WIDTH - fm.stringWidth(scoreMsg)) / 2, HEIGHT / 2 + 50);

        playAgainButton.setVisible(true);
    }

    private void restartGame() {
        // Reset game variables
        snakeLength = 3;
        direction = 'R';
        for (int i = 0; i < snakeLength; i++) {
            snakeX[i] = 0;
            snakeY[i] = 0;
        }
        newFood();
        running = true;
        timer.start();
        playAgainButton.setVisible(false);
        repaint();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkFoodCollision();
            checkCollisions();
        }
        repaint();
    }
}
