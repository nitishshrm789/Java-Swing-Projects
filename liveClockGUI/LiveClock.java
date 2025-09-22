package liveClockGUI;

import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LiveClock extends JFrame {

    private JLabel timeLabel;
    private SimpleDateFormat timeFormat;

    public LiveClock() {
        setTitle("Live Digital Clock");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window

        // Time format: HH:mm:ss (24-hour) or hh:mm:ss a (12-hour)
        timeFormat = new SimpleDateFormat("HH:mm:ss");

        timeLabel = new JLabel();
        timeLabel.setFont(new Font("Arial", Font.BOLD, 40));
        timeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        timeLabel.setForeground(Color.BLUE);

        add(timeLabel);

        // Timer to update the clock every 1000ms (1 second)
        Timer timer = new Timer(1000, e -> updateTime());
        timer.start();

        // Initial update
        updateTime();

        setVisible(true);
    }

    private void updateTime() {
        String currentTime = timeFormat.format(new Date());
        timeLabel.setText(currentTime);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LiveClock::new);
    }
}
