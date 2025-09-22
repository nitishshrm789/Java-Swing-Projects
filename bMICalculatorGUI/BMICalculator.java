package bMICalculatorGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BMICalculator extends JFrame implements ActionListener {

    private JTextField weightField, heightField, ageField;
    private JComboBox<String> genderComboBox;
    private JLabel resultLabel, categoryLabel, suggestionLabel;

    public BMICalculator() {
        setTitle("Advanced BMI Calculator");
        setSize(520, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // center window
        setResizable(false);
        setLayout(new BorderLayout(10, 10));

        // ====== Input Panel ======
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));

        JLabel weightLabel = new JLabel("Weight (kg):");
        weightLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(weightLabel);

        weightField = new JTextField();
        weightField.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(weightField);

        JLabel heightLabel = new JLabel("Height (cm):");
        heightLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(heightLabel);

        heightField = new JTextField();
        heightField.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(heightField);

        JLabel ageLabel = new JLabel("Age:");
        ageLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(ageLabel);

        ageField = new JTextField();
        ageField.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(ageField);

        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(genderLabel);

        genderComboBox = new JComboBox<>(new String[]{"Male", "Female"});
        genderComboBox.setFont(new Font("Arial", Font.PLAIN, 16));
        inputPanel.add(genderComboBox);

        JButton calculateButton = new JButton("Calculate BMI");
        calculateButton.setFont(new Font("Arial", Font.BOLD, 18));
        calculateButton.addActionListener(this);
        inputPanel.add(calculateButton);

        add(inputPanel, BorderLayout.NORTH);

        // ====== Output Panel ======
        JPanel outputPanel = new JPanel();
        outputPanel.setLayout(new BoxLayout(outputPanel, BoxLayout.Y_AXIS));
        outputPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        resultLabel = new JLabel("Your BMI: ");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 20));
        outputPanel.add(resultLabel);

        categoryLabel = new JLabel("Category: ");
        categoryLabel.setFont(new Font("Arial", Font.BOLD, 20));
        outputPanel.add(categoryLabel);

        suggestionLabel = new JLabel("<html><div style='width:460px;'>Suggestions will appear here...</div></html>");
        suggestionLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        outputPanel.add(Box.createVerticalStrut(10));
        outputPanel.add(suggestionLabel);

        add(outputPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            double weight = Double.parseDouble(weightField.getText().trim());
            double heightCm = Double.parseDouble(heightField.getText().trim());
            int age = Integer.parseInt(ageField.getText().trim());
            String gender = genderComboBox.getSelectedItem().toString();

            double heightM = heightCm / 100.0;
            double bmi = weight / (heightM * heightM);

            String category;
            String suggestion;

            // Interpret BMI slightly differently based on age/gender
            if (bmi < 18.5) {
                category = "Underweight";
                suggestion = """
                        ➤ Eat more calories and protein.<br>
                        ➤ Try strength training to build muscle.<br>
                        ➤ Ensure you're sleeping enough.<br>
                        """;
                if (gender.equals("Female")) {
                    suggestion += "➤ Consult a nutritionist to ensure hormonal balance.<br>";
                }
            } else if (bmi < 24.9) {
                category = "Normal weight";
                suggestion = """
                        ✅ Great! You're in a healthy range.<br>
                        ➤ Keep up regular physical activity.<br>
                        ➤ Maintain a balanced diet.<br>
                        """;
                if (age > 50) {
                    suggestion += "➤ Consider bone-strengthening exercises and calcium intake.<br>";
                }
            } else if (bmi < 29.9) {
                category = "Overweight";
                suggestion = """
                        ⚠️ You may benefit from losing some weight.<br>
                        ➤ Reduce sugar and refined carbs.<br>
                        ➤ 30–45 minutes of daily walking or light cardio.<br>
                        ➤ Drink water and get proper sleep.<br>
                        """;
                if (gender.equals("Male")) {
                    suggestion += "➤ Focus on reducing abdominal fat through core exercises.<br>";
                }
            } else {
                category = "Obese";
                suggestion = """
                        ⚠️ Health risk due to high BMI.<br>
                        ➤ Consult with a doctor or dietitian.<br>
                        ➤ Start with light daily walking or swimming.<br>
                        ➤ Avoid junk food, sugary drinks, and alcohol.<br>
                        ➤ Set small weekly goals and track progress.<br>
                        """;
            }

            resultLabel.setText(String.format("Your BMI: %.2f", bmi));
            categoryLabel.setText("Category: " + category);
            suggestionLabel.setText("<html><div style='width:460px;'>" + suggestion + "</div></html>");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for weight, height, and age.", "Input Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BMICalculator::new);
    }
}
