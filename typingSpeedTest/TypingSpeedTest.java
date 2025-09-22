package typingSpeedTest;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.*;
import javax.swing.Timer;

public class TypingSpeedTest extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private int selectedTime = 30;
    private String selectedDifficulty = "Easy";

    private final Map<String, List<String>> difficultyTexts = Map.of(
        "Easy", List.of("dog", "sun", "cup", "book", "hello", "world", "pen", "car", "home", "apple", "pistol",
            "human", "coincidence", "surrender", "systematic", "perfect", "study", "the",
            "mistake", "express", "station", "master", "metro", "state", "stand", "working"),
        "Medium", List.of(
            "I usually wake up around 7 a.m. on weekdays. Drinking water first thing in the morning helps me feel refreshed. I try to avoid checking my phone right after waking up. Breakfast is the most important meal of my day.",
            "Smartphones have changed the way we communicate. Cloud storage makes accessing files much easier. Artificial intelligence is becoming a big part of modern life. Cybersecurity is a growing concern in the digital age.",
            "Reading regularly helps improve vocabulary. Online learning offers flexibility for students. Group discussions encourage different perspectives. Practice tests can help reduce exam anxiety.",
            "Recycling helps reduce waste in landfills. Planting trees can combat climate change. Using public transport helps cut down pollution. Saving electricity is good for the environment and your bills."),
        "Hard", List.of(
            "Regular exercise can improve both physical and mental health. Getting enough sleep is essential for daily functioning. A balanced diet includes fruits, vegetables, and proteins. Taking short breaks during work helps reduce stress.",
            "Traveling exposes you to new cultures and perspectives. Trying local food is a great way to experience a new place. Learning a few phrases in the local language is always helpful. Cultural festivals showcase the traditions of a community.",
            "Swing-based apps require careful event handling and layout management. Setting clear goals can boost your productivity. Managing time well reduces last-minute stress. Teamwork often leads to better problem-solving. Taking notes during meetings helps retain key points.")
    );

    public TypingSpeedTest() {
        setTitle("Typing Speed Test");
        setSize(700, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        mainPanel.add(createWelcomePanel(), "Welcome");
        setContentPane(mainPanel);
        setVisible(true);
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridLayout(6, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        JLabel titleLabel = new JLabel("Typing Speed Test", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        panel.add(titleLabel);

        JPanel timePanel = new JPanel();
        timePanel.add(new JLabel("Choose Time Limit:"));
        JComboBox<String> timeBox = new JComboBox<>(new String[]{"15", "30", "60"});
        timePanel.add(timeBox);
        panel.add(timePanel);

        JPanel diffPanel = new JPanel();
        diffPanel.add(new JLabel("Choose Difficulty:"));
        JComboBox<String> diffBox = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        diffPanel.add(diffBox);
        panel.add(diffPanel);

        JButton startButton = new JButton("Start Test");
        startButton.addActionListener(e -> {
            selectedTime = Integer.parseInt((String) timeBox.getSelectedItem());
            selectedDifficulty = (String) diffBox.getSelectedItem();
            mainPanel.add(createTestPanel(), "Test");
            cardLayout.show(mainPanel, "Test");
        });

        panel.add(startButton);

        return panel;
    }	

    private JPanel createTestPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        JTextArea textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 16));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);

        String generatedText = getRandomText(selectedDifficulty);
        textArea.setText(generatedText);

        // ✅ Use JTextArea instead of JTextField for input
        JTextArea inputArea = new JTextArea(3, 20);
        inputArea.setFont(new Font("Monospaced", Font.BOLD, 18));
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        JScrollPane inputScrollPane = new JScrollPane(inputArea);

        JLabel timerLabel = new JLabel("Time: " + selectedTime + "s");
        JLabel wpmLabel = new JLabel("WPM: 0");

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(timerLabel, BorderLayout.WEST);
        topPanel.add(wpmLabel, BorderLayout.EAST);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(textArea), BorderLayout.CENTER);
        panel.add(inputScrollPane, BorderLayout.SOUTH);

        inputArea.requestFocusInWindow();

        // Timer Logic
        final int[] timeLeft = {selectedTime};
        final int[] typedChars = {0};
        Timer timer = new Timer(1000, null);

        timer.addActionListener(e -> {
            timeLeft[0]--;
            timerLabel.setText("Time: " + timeLeft[0] + "s");

            int wordsTyped = inputArea.getText().trim().isEmpty() ? 0 :
                    inputArea.getText().trim().split("\\s+").length;

            int secondsElapsed = selectedTime - timeLeft[0];
            if (secondsElapsed > 0) {
                int wpm = (int) ((wordsTyped / (secondsElapsed / 60.0)));
                wpmLabel.setText("WPM: " + wpm);
            }

            if (timeLeft[0] <= 0) {
                timer.stop();
                showResultPanel(inputArea.getText(), generatedText, typedChars[0]);
            }
        });

        inputArea.addKeyListener(new KeyAdapter() {
            boolean started = false;

            @Override
            public void keyTyped(KeyEvent e) {
                if (!started) {
                    timer.start();
                    started = true;
                }
                typedChars[0]++;
            }
        });

        return panel;
    }

    private void showResultPanel(String typedText, String originalText, int keystrokes) {
        JPanel panel = new JPanel(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 50, 20, 50));

        int typedWords = typedText.trim().isEmpty() ? 0 : typedText.trim().split("\\s+").length;
        int originalWords = originalText.trim().split("\\s+").length;

        int correctWords = countMatchingWords(typedText, originalText);
        int accuracy = originalWords > 0 ? (int) (((double) correctWords / originalWords) * 100) : 0;
        int wpm = (int) ((typedWords / (selectedTime / 60.0)));

        panel.add(new JLabel("Test Complete!", SwingConstants.CENTER));
        panel.add(new JLabel("Words Per Minute (WPM): " + wpm, SwingConstants.CENTER));
        panel.add(new JLabel("Accuracy: " + accuracy + "%", SwingConstants.CENTER));
        panel.add(new JLabel("Keystrokes: " + keystrokes, SwingConstants.CENTER));

        JButton restartBtn = new JButton("Restart");
        restartBtn.addActionListener(e -> cardLayout.show(mainPanel, "Welcome"));

        panel.add(restartBtn);

        mainPanel.add(panel, "Result");
        cardLayout.show(mainPanel, "Result");
    }

    private int countMatchingWords(String typed, String original) {
        String[] typedWords = typed.trim().split("\\s+");
        String[] originalWords = original.trim().split("\\s+");
        int correct = 0;
        for (int i = 0; i < Math.min(typedWords.length, originalWords.length); i++) {
            if (typedWords[i].equalsIgnoreCase(originalWords[i])) {
                correct++;
            }
        }
        return correct;
    }

    private String getRandomText(String difficulty) {
        List<String> items = difficultyTexts.get(difficulty);
        Random rand = new Random();
        if (difficulty.equals("Easy")) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < 30; i++) {
                sb.append(items.get(rand.nextInt(items.size()))).append(" ");
            }
            return sb.toString().trim();
        } else {
            return items.get(rand.nextInt(items.size()));
        }
    }
}
