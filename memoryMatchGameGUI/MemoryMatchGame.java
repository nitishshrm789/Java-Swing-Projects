package memoryMatchGameGUI;

import javax.swing.*;
import javax.swing.Timer;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class MemoryMatchGame extends JFrame {
    private final int SIZE = 4;
    private final JButton[][] buttons = new JButton[SIZE][SIZE];
    private final String[][] cardValues = new String[SIZE][SIZE];
    private final boolean[][] matched = new boolean[SIZE][SIZE];
    private boolean boardLocked = false;

    private final Map<String, ImageIcon> imageMap = new HashMap<>();

    private JButton firstSelected = null;
    private JButton secondSelected = null;

    private Timer flipBackTimer;
    private int tries = 0;
    private JLabel triesLabel;
    private JLabel timeLabel;

    private Timer gameTimer;
    private int elapsedSeconds = 0;

    public MemoryMatchGame() {
        setTitle("Memory Match Game (with Images)");
        setSize(700, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        loadImages();
        initializeCards();

        JPanel boardPanel = new JPanel(new GridLayout(SIZE, SIZE));
        createBoard(boardPanel);

        JPanel topPanel = new JPanel(new FlowLayout());
        triesLabel = new JLabel("Tries: 0");
        timeLabel = new JLabel("Time: 0s");
        topPanel.add(triesLabel);
        topPanel.add(timeLabel);

        add(topPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);

        startTimer();
        setVisible(true);
    }

    private void loadImages() {
        // Preload all images
        String[] imageNames = {
            "apple", "banana", "cherry", "grape",
            "lemon", "orange", "strawberry", "watermelon"
        };

        for (String name : imageNames) {
            ImageIcon icon = new ImageIcon(name + ".png");
            Image scaled = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            imageMap.put(name, new ImageIcon(scaled));
        }
    }

    private void initializeCards() {
        List<String> values = new ArrayList<>();
        for (String key : imageMap.keySet()) {
            values.add(key);
            values.add(key); // 2 of each
        }

        Collections.shuffle(values);

        Iterator<String> iterator = values.iterator();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                cardValues[i][j] = iterator.next();
            }
        }
    }

    private void createBoard(JPanel panel) {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                JButton btn = new JButton();
                btn.setBackground(Color.WHITE); // 🔄 Changed from LIGHT_GRAY
                btn.setFocusPainted(false);
                btn.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1)); // 🔄 Optional for clarity
                btn.setIcon(getBackIcon());

                int row = i;
                int col = j;

                btn.addActionListener(e -> handleCardClick(btn, row, col));
                buttons[i][j] = btn;
                panel.add(btn);
            }
        }
    }

    private void handleCardClick(JButton btn, int row, int col) {
        if (boardLocked || matched[row][col] || btn == firstSelected || btn == secondSelected) {
            return;
        }

        btn.setIcon(imageMap.get(cardValues[row][col]));
        btn.setEnabled(false);

        if (firstSelected == null) {
            firstSelected = btn;
        } else if (secondSelected == null) {
            secondSelected = btn;
            tries++;
            triesLabel.setText("Tries: " + tries);

            int firstRow = getButtonRow(firstSelected);
            int firstCol = getButtonCol(firstSelected);

            if (cardValues[firstRow][firstCol].equals(cardValues[row][col])) {
                matched[firstRow][firstCol] = true;
                matched[row][col] = true;
                firstSelected = null;
                secondSelected = null;

                if (checkWin()) {
                    gameTimer.stop();
                    JOptionPane.showMessageDialog(this,
                            "🎉 You matched all pairs in " + tries + " tries and " + elapsedSeconds + " seconds!",
                            "Game Over", JOptionPane.INFORMATION_MESSAGE);
                }
            } else {
            	boardLocked = true;
            	
                flipBackTimer = new Timer(800, e -> {
                    firstSelected.setIcon(getBackIcon());
                    firstSelected.setEnabled(true);
                    secondSelected.setIcon(getBackIcon());
                    secondSelected.setEnabled(true);
                    firstSelected = null;
                    secondSelected = null;
                    boardLocked = false; 
                    flipBackTimer.stop();
                });
                flipBackTimer.setRepeats(false);
                flipBackTimer.start();
            }
        }
    }

    private ImageIcon getBackIcon() {
        // Simple placeholder back image
        ImageIcon back = new ImageIcon("images/back.png");
        if (back.getImageLoadStatus() == MediaTracker.COMPLETE) {
            Image scaled = back.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
            return new ImageIcon(scaled);
        } else {
            // fallback to blank
            return new ImageIcon();
        }
    }

    private int getButtonRow(JButton btn) {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (buttons[i][j] == btn)
                    return i;
        return -1;
    }

    private int getButtonCol(JButton btn) {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (buttons[i][j] == btn)
                    return j;
        return -1;
    }

    private boolean checkWin() {
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                if (!matched[i][j])
                    return false;
        return true;
    }

    private void startTimer() {
        gameTimer = new Timer(1000, e -> {
            elapsedSeconds++;
            timeLabel.setText("Time: " + elapsedSeconds + "s");
        });
        gameTimer.start();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MemoryMatchGame::new);
    }
}
