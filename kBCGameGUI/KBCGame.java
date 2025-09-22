package kBCGameGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class KBCGame extends JFrame {
	
	private static final String[] PRIZES = {
	        "₹25k", "₹50k", "₹1L", "₹3L", "₹5L", "₹10L", "₹50L", "₹1Cr", "₹5Cr", "₹7Cr"
	    };
	    private final JLabel[] prizeLabels = new JLabel[PRIZES.length];
	    private int currentPrizeIndex = 0;

	    private final JLabel prizePanelTitle = new JLabel("Money Tree", SwingConstants.CENTER);
	    private final JTextArea questionArea = new JTextArea();
	    private final JRadioButton[] optionButtons = new JRadioButton[4];
	    private final ButtonGroup optionsGroup = new ButtonGroup();
	    private final JButton fiftyFiftyButton = new JButton("50:50");
	    private final JButton audiencePollButton = new JButton("Audience Poll");
	    private final JButton lockButton = new JButton("Lock Answer");
	    private final JButton flipButton = new JButton("Flip the Question");

	    private final List<Question> questions = new ArrayList<>();
	    private int currentQuestionIndex = 0;
	    private int selectedAnswer = -1;
	    
	    private boolean fiftyUsed = false;
	    private boolean audienceUsed = false;
	    private boolean flipUsed = false;

	    public KBCGame() {
	        setTitle("Kaun Banega Crorepati");
	        setSize(1000, 600);
	        setDefaultCloseOperation(EXIT_ON_CLOSE);
	        setLocationRelativeTo(null);
	        setLayout(new BorderLayout(10, 10));

	        initPrizesPanel();
	        initCenter();
	        initLifelinesPanel();
	        initQuestions();

	        loadQuestion();
	        setVisible(true);
	    }

	    private void initPrizesPanel() {
	        JPanel ladder = new JPanel(new GridLayout(PRIZES.length + 1, 1, 5, 5));
	        ladder.setBackground(new Color(30, 0, 60));
	        prizePanelTitle.setForeground(Color.YELLOW);
	        prizePanelTitle.setFont(new Font("Tahoma", Font.BOLD, 18));
	        ladder.add(prizePanelTitle);

	        for (int i = 0; i < PRIZES.length; i++) {
	            prizeLabels[i] = new JLabel(PRIZES[i], SwingConstants.CENTER);
	            prizeLabels[i].setForeground(Color.WHITE);
	            prizeLabels[i].setFont(new Font("Tahoma", Font.BOLD, 16));
	            ladder.add(prizeLabels[i]);
	        }
	        highlightPrize();
	        add(ladder, BorderLayout.WEST);
	    }

	    private void highlightPrize() {
	        for (int i = 0; i < prizeLabels.length; i++) {
	            prizeLabels[i].setForeground(i == currentPrizeIndex ? Color.GREEN : Color.WHITE);
	        }
	    }

	    private void initCenter() {	    	
	        JPanel center = new JPanel(new BorderLayout(10, 10));
	        center.setBackground(new Color(10, 0, 50));

	        questionArea.setWrapStyleWord(true);
	        questionArea.setLineWrap(true);
	        questionArea.setEditable(false);
	        questionArea.setFont(new Font("Arial", Font.BOLD, 24));
	        questionArea.setForeground(Color.WHITE);
	        questionArea.setBackground(new Color(20, 0, 70));
	        questionArea.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
	        center.add(questionArea, BorderLayout.NORTH);

	        JPanel optsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
	        optsPanel.setBackground(new Color(10, 0, 50));

	        for (int i = 0; i < 4; i++) {
	            JRadioButton btn = new JRadioButton();
	            final int idx = i;
	            btn.setFont(new Font("Arial", Font.BOLD, 18));
	            btn.setForeground(Color.WHITE);
	            btn.setBackground(new Color(40, 0, 90));
	            btn.addActionListener(e -> selectedAnswer = idx);
	            optionsGroup.add(btn);
	            optsPanel.add(btn);
	            optionButtons[i] = btn;
	        }

	        center.add(optsPanel, BorderLayout.CENTER);

	        lockButton.setFont(new Font("Arial", Font.BOLD, 18));
	        lockButton.addActionListener(e -> checkAnswer());
	        center.add(lockButton, BorderLayout.SOUTH);
	        
	        

	        add(center, BorderLayout.CENTER);
	    }

	    private void initLifelinesPanel() {	    	
	        JPanel lifelines = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
	        lifelines.setBackground(new Color(30, 0, 60));

	        fiftyFiftyButton.setFont(new Font("Arial", Font.BOLD, 16));
	        fiftyFiftyButton.addActionListener(e -> useFiftyFifty());
	        lifelines.add(fiftyFiftyButton);

	        audiencePollButton.setFont(new Font("Arial", Font.BOLD, 16));
	        audiencePollButton.addActionListener(e -> useAudiencePoll());
	        lifelines.add(audiencePollButton);
	        
	        flipButton.setFont(new Font("Arial", Font.BOLD, 16));
	    	flipButton.addActionListener(e -> useFlip());
	    	lifelines.add(flipButton);

	        add(lifelines, BorderLayout.SOUTH);
	    }

	    private void initQuestions() {
	    	// Within your KBCGame class, in initQuestions():
	    	questions.add(new Question(
	    	    "Who is known as the Father of the Indian Constitution?",
	    	    new String[]{"Mahatma Gandhi", "Jawaharlal Nehru", "Dr. B.R. Ambedkar", "Sardar Patel"},
	    	    2
	    	));

	    	questions.add(new Question(
	    	    "Which river is known as the 'Ganga of the South'?",
	    	    new String[]{"Godavari", "Kaveri", "Krishna", "Narmada"},
	    	    1
	    	));

	    	questions.add(new Question(
	    	    "The famous Khajuraho temples are located in which Indian state?",
	    	    new String[]{"Uttar Pradesh", "Madhya Pradesh", "Rajasthan", "Bihar"},
	    	    1
	    	));

	    	questions.add(new Question(
	    	    "Which Indian state is known as the 'Land of Five Rivers'?",
	    	    new String[]{"Uttar Pradesh", "Haryana", "Punjab", "Gujarat"},
	    	    2
	    	));

	    	questions.add(new Question(
	    	    "What is the national animal of India?",
	    	    new String[]{"Lion", "Elephant", "Tiger", "Deer"},
	    	    2
	    	));

	    	questions.add(new Question(
	    	    "Which state in India is known as the 'Land of the Rising Sun'?",
	    	    new String[]{"Kerala", "Goa", "Arunachal Pradesh", "Assam"},
	    	    2
	    	));

	    	questions.add(new Question(
	    	    "When did India win the Cricket World Cup for the first time?",
	    	    new String[]{"1975", "1983", "1992", "2007"},
	    	    1
	    	));

	    	questions.add(new Question(
	    	    "What is India's national flower?",
	    	    new String[]{"Tulip", "Rose", "Lotus", "Jasmine"},
	    	    2
	    	));

	    	questions.add(new Question(
	    	    "Which Indian state is referred to as the 'Land of Temples'?",
	    	    new String[]{"Kerala", "Goa", "Tamil Nadu", "Rajasthan"},
	    	    2
	    	));

	    	questions.add(new Question(
	    	    "Who started the first newspaper in India?",
	    	    new String[]{"Mahatma Gandhi", "Rabindranath Tagore", "Bal Gangadhar Tilak", "Syed Ahmed Khan"},
	    	    3
	    	));
	    	
	    	questions.add(new Question(
	    		    "Which of the following scientists became the first Indian woman to be elected as a Fellow of the Royal Society in 2019?",
	    		    new String[]{"Indira Hinduja", "Gita Ramjee", "Gagandeep Kang", "Anandi Gopal Joshi"},
	    		    2
	    		));
	    }

	    private void loadQuestion() {
	        if (currentQuestionIndex >= questions.size()) {
	            JOptionPane.showMessageDialog(this, "🎉 You win ₹" + PRIZES[currentPrizeIndex] + "!", "Winner!", JOptionPane.INFORMATION_MESSAGE);
	            System.exit(0);
	        }
	        optionsGroup.clearSelection();
	        for (JRadioButton btn : optionButtons) {
	            btn.setEnabled(true);
	            btn.setVisible(true);
	        }
	        selectedAnswer = -1;

	        Question q = questions.get(currentQuestionIndex);
	        questionArea.setText("Q"+(currentQuestionIndex+1)+": " + q.questionText);
	        for (int i = 0; i < 4; i++) {
	            optionButtons[i].setText(q.options[i]);
	        }
	    }

	    private void checkAnswer() {
	        if (selectedAnswer == -1) {
	            JOptionPane.showMessageDialog(this, "Select an answer!", "Alert", JOptionPane.WARNING_MESSAGE);
	            return;
	        }
	        
	        Question q = questions.get(currentQuestionIndex);
	        
	        
	        	if (q.isCorrect(selectedAnswer)) {
		            currentPrizeIndex++;
		            highlightPrize();
		            JOptionPane.showMessageDialog(this, "Correct! You won " + PRIZES[currentPrizeIndex-1], "Correct", JOptionPane.INFORMATION_MESSAGE);
		            currentQuestionIndex++;
		            loadQuestion();
		        } else {
		            JOptionPane.showMessageDialog(this, "Wrong! Correct answer: " + q.options[q.correctIndex] + "\nYou take home " + PRIZES[currentPrizeIndex-1], "Game Over", JOptionPane.ERROR_MESSAGE);
		            System.exit(0);
		        }
	        if(currentPrizeIndex == PRIZES.length) {
	        	dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
	        }
	    }

	    private void useFiftyFifty() {
	        if (fiftyUsed) return;
	        fiftyUsed = true;
	        fiftyFiftyButton.setVisible(false);

	        Question q = questions.get(currentQuestionIndex);
	        List<Integer> wrong = new ArrayList<>();
	        for (int i = 0; i < 4; i++) if (i != q.correctIndex) wrong.add(i);

	        Collections.shuffle(wrong);
	        int toHide1 = wrong.get(0), toHide2 = wrong.get(1);
	        optionButtons[toHide1].setVisible(false);
	        optionButtons[toHide2].setVisible(false);
	    }

	    private void useAudiencePoll() {
	        if (audienceUsed) return;
	        audienceUsed = true;
	        audiencePollButton.setVisible(false);

	        Question q = questions.get(currentQuestionIndex);
	        int[] perc = new int[4];
	        perc[q.correctIndex] = 60;
	        List<Integer> others = new ArrayList<>(Arrays.asList(0,1,2,3));
	        others.remove(Integer.valueOf(q.correctIndex));
	        Collections.shuffle(others);
	        perc[others.get(0)] = 20;
	        perc[others.get(1)] = 15;
	        perc[others.get(2)] = 5;

	        StringBuilder sb = new StringBuilder("Audience Poll:\n");
	        for (int i = 0; i < 4; i++) {
	            sb.append(optionButtons[i].getText()).append(": ").append(perc[i]).append("%\n");
	        }
	        JOptionPane.showMessageDialog(this, sb.toString(), "Audience Poll", JOptionPane.INFORMATION_MESSAGE);
	    }
	    
	    private void useFlip() {
	        if (flipUsed) {
	            JOptionPane.showMessageDialog(this, "You have already used this lifeline.", "Lifeline Used", JOptionPane.WARNING_MESSAGE);
	            return;
	        }

	        flipUsed = true;
	        flipButton.setVisible(false);

	        // Select a new question index, different from the current one
	        int newIndex;
	        do {
	            newIndex = new Random().nextInt(questions.size());
	        } while (newIndex == currentQuestionIndex);

	        currentQuestionIndex = newIndex;
	        loadQuestion();
	    }

	    static class Question {
	        String questionText;
	        String[] options;
	        int correctIndex;
	        public Question(String q, String[] opts, int correct) {
	            this.questionText = q; this.options = opts; this.correctIndex = correct;
	        }
	        public boolean isCorrect(int sel) { return sel == correctIndex; }
	    }
}
