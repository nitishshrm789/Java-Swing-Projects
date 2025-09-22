package game2048;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import java.util.Stack;

public class Game2048 extends JPanel {
	
	public enum Direction {
	    UP( -1,  0),
	    DOWN( 1,  0),
	    LEFT( 0, -1),
	    RIGHT(0,  1);

	    public final int dx;
	    public final int dy;

	    Direction(int dx, int dy) {
	        this.dx = dx;  // row movement
	        this.dy = dy;  // column movement
	    }

	    // Optional: get the opposite direction
	    public Direction opposite() {
	        return switch (this) {
	            case UP -> DOWN;
	            case DOWN -> UP;
	            case LEFT -> RIGHT;
	            case RIGHT -> LEFT;
	        };
	    }
	}
	
	enum State { START, RUNNING, WON, OVER }
    private State gameState = State.START;
    private final int size = 4;
    private final Tile[][] tiles = new Tile[size][size];
    private final Random rand = new Random();
    private int score = 0, highest = 0;
    private final int target = 2048;

    public Game2048() {
        setPreferredSize(new Dimension(500, 600)); // Adjust to fit grid + score
        setBackground(new Color(0xFAF8EF));
        setFont(new Font("SansSerif", Font.BOLD, 48));
        setFocusable(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startGame();
                repaint();
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (gameState == State.RUNNING) {
                    boolean moved = false;
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP: moved = moveUp(); break;
                        case KeyEvent.VK_DOWN: moved = moveDown(); break;
                        case KeyEvent.VK_LEFT: moved = moveLeft(); break;
                        case KeyEvent.VK_RIGHT: moved = moveRight(); break;
                    }
                    if (moved) repaint();
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics gg) {
        super.paintComponent(gg);
        Graphics2D g = (Graphics2D) gg;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawGrid(g);
    }

    private void startGame() {
        if (gameState != State.RUNNING) {
            score = 0;
            highest = 0;
            gameState = State.RUNNING;
            for (int i = 0; i < size; i++)
                for (int j = 0; j < size; j++)
                    tiles[i][j] = null;
            addRandomTile();
            addRandomTile();
        }
    }
    
    private boolean move(Direction dir) {
        boolean moved = false;
        flushMergedFlags();
        for (int i = 0; i < size; i++) {
            Tile[] line = new Tile[size];
            for (int j = 0; j < size; j++) {
                int r = dir == Direction.UP || dir == Direction.DOWN ? j : i;
                int c = dir == Direction.LEFT || dir == Direction.RIGHT ? j : i;
                if (dir == Direction.DOWN) r = size - 1 - j;
                if (dir == Direction.RIGHT) c = size - 1 - j;
                line[j] = tiles[r][c];
            }
            Tile[] merged = mergeLine(line);
            for (int j = 0; j < size; j++) {
                int r = dir == Direction.UP || dir == Direction.DOWN ? j : i;
                int c = dir == Direction.LEFT || dir == Direction.RIGHT ? j : i;
                if (dir == Direction.DOWN) r = size - 1 - j;
                if (dir == Direction.RIGHT) c = size - 1 - j;
                if (tiles[r][c] != merged[j]) {
                    tiles[r][c] = merged[j];
                    moved = true;
                }
            }
        }
        if (moved) addRandomTile();
        return moved;
    }

    private Tile[] mergeLine(Tile[] oldLine) {
        Stack<Tile> stack = new Stack<>();
        for (Tile tile : oldLine) {
            if (tile != null) {
                if (!stack.isEmpty() && stack.peek().canMergeWith(tile)) {
                    stack.peek().mergeWith(tile);
                    score += stack.peek().getValue();
                    stack.peek().setMerged(true);
                } else {
                    stack.push(tile);
                }
            }
        }
        Tile[] newLine = new Tile[size];
        int idx = 0;
        while (!stack.isEmpty()) newLine[idx++] = stack.remove(0);
        return newLine;
    }

    private void flushMergedFlags() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                if (tiles[i][j] != null) tiles[i][j].setMerged(false);
    }

    private void drawGrid(Graphics2D g) {
        final int gridX = 50, gridY = 100, gridSize = 400;
        final int cellSize = gridSize / size;

        // Background
        g.setColor(new Color(0xBBADA0));
        g.fillRoundRect(gridX, gridY, gridSize, gridSize, 15, 15);

        if (gameState == State.RUNNING) {
            for (int r = 0; r < size; r++) {
                for (int c = 0; c < size; c++) {
                    if (tiles[r][c] == null) {
                        g.setColor(new Color(0xCDC1B4));
                        g.fillRoundRect(gridX + c * cellSize + 5, gridY + r * cellSize + 5, cellSize - 10, cellSize - 10, 7, 7);
                    } else {
                        drawTile(g, r, c, gridX, gridY, cellSize);
                    }
                }
            }
        } else {
            // Start/Win/Over screen
            g.setColor(new Color(0xFFEBCD));
            g.fillRoundRect(gridX + 5, gridY + 5, gridSize - 10, gridSize - 10, 7, 7);

            g.setColor(new Color(0x8F7A66));
            g.setFont(new Font("SansSerif", Font.BOLD, 64));
            String msg = switch (gameState) {
                case START -> "2048";
                case WON -> "You Win!";
                case OVER -> "Game Over";
                default -> "";
            };
            FontMetrics fm = g.getFontMetrics();
            int w = fm.stringWidth(msg);
            g.drawString(msg, gridX + (gridSize - w) / 2, gridY + gridSize / 2);
            g.setFont(new Font("SansSerif", Font.PLAIN, 24));
            String sub = "Click to play";
            fm = g.getFontMetrics();
            w = fm.stringWidth(sub);
            g.drawString(sub, gridX + (gridSize - w) / 2, gridY + gridSize / 2 + 40);
        }

        // Score display
        g.setColor(new Color(0x776E65));
        g.setFont(new Font("SansSerif", Font.BOLD, 24));
        g.drawString("Score: " + score, gridX, gridY - 20);
    }

    private void drawTile(Graphics2D g, int r, int c, int gridX, int gridY, int cellSize) {
        int val = tiles[r][c].getValue();
        int index = (int) (Math.log(val) / Math.log(2)) + 1;
        Color[] colors = {
            new Color(0xEEE4DA), new Color(0xEDE0C8), new Color(0xF2B179),
            new Color(0xF59563), new Color(0xF67C5F), new Color(0xF65E3B),
            new Color(0xEDCF72), new Color(0xEDCC61), new Color(0xEDC850),
            new Color(0xEDC53F), new Color(0xEDC22E)
        };
        g.setColor(colors[Math.min(index, colors.length - 1)]);
        int x = gridX + c * cellSize + 5, y = gridY + r * cellSize + 5;
        g.fillRoundRect(x, y, cellSize - 10, cellSize - 10, 7, 7);

        g.setColor(val < 128 ? Color.BLACK : Color.WHITE);
        String s = String.valueOf(val);
        FontMetrics fm = g.getFontMetrics();
        int asc = fm.getAscent(), dec = fm.getDescent();
        int tx = x + (cellSize - 10 - fm.stringWidth(s)) / 2;
        int ty = y + (asc + (cellSize - 10 - (asc + dec)) / 2);
        g.drawString(s, tx, ty);
    }

    private void addRandomTile() {
        var empty = java.util.stream.IntStream.range(0, size * size)
            .filter(i -> tiles[i / size][i % size] == null)
            .toArray();
        if (empty.length == 0) return;
        int pos = empty[rand.nextInt(empty.length)];
        tiles[pos / size][pos % size] = new Tile(rand.nextInt(10) == 0 ? 4 : 2);
    }

    boolean moveUp() { return move(Direction.UP); }
    boolean moveDown() { return move(Direction.DOWN); }
    boolean moveLeft() { return move(Direction.LEFT); }
    boolean moveRight() { return move(Direction.RIGHT); }

    static class Tile {
        private int value;
        private boolean merged;
        Tile(int v) { value = v; }
        int getValue() { return value; }
        boolean canMergeWith(Tile o) { return o != null && !merged && o.value == value; }
        int mergeWith(Tile o) { if (canMergeWith(o)) { merged = true; value *= 2; return value; } return -1; }
        void setMerged(boolean m) { merged = m; }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            var frame = new JFrame("2048");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.add(new Game2048());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
