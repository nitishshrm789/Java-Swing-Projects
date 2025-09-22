package alienShooterGameGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class AlienShooterGame extends JFrame {
	
	
	public AlienShooterGame() {
        setTitle("Alien Shooter Game");
        setSize(600, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        add(new GamePanel());
        setResizable(false);
        setVisible(true);
    }

    class GamePanel extends JPanel {
        private final int HERO_WIDTH = 60, HERO_HEIGHT = 20;
        private final int BULLET_WIDTH = 5, BULLET_HEIGHT = 10;
        private final int ALIEN_SIZE = 40;
        private final int ALIEN_SPEED = 5;
        private final int BULLET_SPEED = 5;
        private final int SHOOT_INTERVAL = 500; // ms
        private final int ALIEN_SPAWN_INTERVAL = 1500; // ms

        private Rectangle hero;
        private List<Rectangle> bullets;
        private List<Rectangle> aliens;
        private Timer gameTimer, alienSpawner, shootTimer;
        private Random rand;
        private int score = 0;

        public GamePanel() {
            setBackground(Color.BLACK);
            hero = new Rectangle((getWidth() - HERO_WIDTH) / 2, 700, HERO_WIDTH, HERO_HEIGHT);
            bullets = new ArrayList<>();
            aliens = new ArrayList<>();
            rand = new Random();

            // Spawn aliens
            alienSpawner = new Timer(ALIEN_SPAWN_INTERVAL, e -> spawnAlien());
            alienSpawner.start();

            // Shoot bullets automatically
            shootTimer = new Timer(SHOOT_INTERVAL, e -> shootBullet());
            shootTimer.start();

            // Main game loop
            gameTimer = new Timer(20, e -> gameLoop());
            gameTimer.start();

            setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            addMouseMotionListener(new MouseAdapter() {
                @Override
                public void mouseMoved(MouseEvent e) {
                    hero.x = e.getX() - HERO_WIDTH / 2;
                }
            });
        }

        private void spawnAlien() {
            int x = rand.nextInt(getWidth() - ALIEN_SIZE);
            aliens.add(new Rectangle(x, 0, ALIEN_SIZE, ALIEN_SIZE));
        }

        private void shootBullet() {
            int bulletX = hero.x + HERO_WIDTH / 2 - BULLET_WIDTH / 2;
            int bulletY = hero.y;
            bullets.add(new Rectangle(bulletX, bulletY, BULLET_WIDTH, BULLET_HEIGHT));
        }

        private void gameLoop() {
            // Move bullets upward
            Iterator<Rectangle> bulletItr = bullets.iterator();
            while (bulletItr.hasNext()) {
                Rectangle b = bulletItr.next();
                b.y -= BULLET_SPEED;
                if (b.y < 0) bulletItr.remove();
            }

            // Move aliens downward
            Iterator<Rectangle> alienItr = aliens.iterator();
            while (alienItr.hasNext()) {
                Rectangle a = alienItr.next();
                a.y += ALIEN_SPEED;
                if (a.y > getHeight()) {
                    alienItr.remove(); // Missed
                }
            }

            // Detect collisions between bullets and aliens
            List<Rectangle> bulletsToRemove = new ArrayList<>();
            List<Rectangle> aliensToRemove = new ArrayList<>();
            for (Rectangle bullet : bullets) {
                for (Rectangle alien : aliens) {
                    if (bullet.intersects(alien)) {
                        bulletsToRemove.add(bullet);
                        aliensToRemove.add(alien);
                        score++;
                    }
                }
            }
            bullets.removeAll(bulletsToRemove);
            aliens.removeAll(aliensToRemove);

            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            // Draw hero
            g.setColor(Color.GREEN);
            g.fillRect(hero.x, hero.y, hero.width, hero.height);

            // Draw bullets
            g.setColor(Color.YELLOW);
            for (Rectangle bullet : bullets) {
                g.fillRect(bullet.x, bullet.y, bullet.width, bullet.height);
            }

            // Draw aliens
            g.setColor(Color.RED);
            for (Rectangle alien : aliens) {
                g.fillOval(alien.x, alien.y, alien.width, alien.height);
            }

            // Draw score
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("Score: " + score, 20, 30);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(AlienShooterGame::new);
    }
}