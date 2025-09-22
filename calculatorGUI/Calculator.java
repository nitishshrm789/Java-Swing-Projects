package calculatorGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame implements ActionListener {

    private JTextField display;
    private JLabel expressionLabel;

    private StringBuilder input;
    private String operator;
    private double firstNumber;
    private boolean startNewNumber = true;

    public Calculator() {
        setTitle("Calculator");
        setSize(400, 520);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        input = new StringBuilder();

        // Top panel for expression and display
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout());

        expressionLabel = new JLabel(" ");
        expressionLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        expressionLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        expressionLabel.setForeground(Color.GRAY);

        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 30));
        display.setEditable(false);
        display.setHorizontalAlignment(SwingConstants.RIGHT);

        topPanel.add(expressionLabel, BorderLayout.NORTH);
        topPanel.add(display, BorderLayout.CENTER);

        add(topPanel, BorderLayout.NORTH);

        // Buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5));

        String[] buttons = {
            "C", "←", "/", "*",
            "7", "8", "9", "-",
            "4", "5", "6", "+",
            "1", "2", "3", "=",
            "0", ".", "", ""
        };

        for (String text : buttons) {
            if (!text.equals("")) {
                JButton button = new JButton(text);
                button.setFont(new Font("Arial", Font.BOLD, 24));
                button.addActionListener(this);
                buttonPanel.add(button);
            } else {
                buttonPanel.add(new JLabel());
            }
        }

        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.matches("\\d")) {
            if (startNewNumber) {
                input.setLength(0);
                startNewNumber = false;
            }
            input.append(command);
            display.setText(input.toString());
        } else if (command.equals(".")) {
            if (startNewNumber) {
                input.setLength(0);
                input.append("0");
                startNewNumber = false;
            }
            if (!input.toString().contains(".")) {
                input.append(".");
                display.setText(input.toString());
            }
        } else if (command.equals("C")) {
            input.setLength(0);
            operator = null;
            firstNumber = 0;
            display.setText("0");
            expressionLabel.setText(" ");
            startNewNumber = true;
        } else if (command.equals("←")) {
            if (!startNewNumber && input.length() > 0) {
                input.deleteCharAt(input.length() - 1);
                display.setText(input.length() == 0 ? "0" : input.toString());
            }
        } else if (command.equals("+") || command.equals("-") || command.equals("*") || command.equals("/")) {
            try {
                firstNumber = Double.parseDouble(display.getText());
                operator = command;
                expressionLabel.setText(formatResult(firstNumber) + " " + operator);
                startNewNumber = true;
            } catch (NumberFormatException ex) {
                display.setText("Error");
            }
        } else if (command.equals("=")) {
            if (operator != null && input.length() > 0) {
                try {
                    double secondNumber = Double.parseDouble(input.toString());
                    double result = switch (operator) {
                        case "+" -> firstNumber + secondNumber;
                        case "-" -> firstNumber - secondNumber;
                        case "*" -> firstNumber * secondNumber;
                        case "/" -> secondNumber == 0 ? Double.NaN : firstNumber / secondNumber;
                        default -> 0;
                    };

                    expressionLabel.setText(formatResult(firstNumber) + " " + operator + " " + formatResult(secondNumber) + " =");
                    display.setText(formatResult(result));
                    input.setLength(0);
                    input.append(formatResult(result));
                    operator = null;
                    startNewNumber = true;
                } catch (NumberFormatException ex) {
                    display.setText("Error");
                }
            }
        }
    }

    private String formatResult(double result) {
        if (result == (long) result)
            return String.format("%d", (long) result);
        else
            return String.format("%s", result);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculator::new);
    }
}

