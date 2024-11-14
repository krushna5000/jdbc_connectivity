import javax.swing.*;  // For Swing components
import java.awt.*;      // For layout managers
import java.awt.event.*; // For handling events

public class Example {
    public static void main(String[] args) {
        // Create the frame
        JFrame frame = new JFrame("Button Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200); // Set frame size
        frame.setLayout(new FlowLayout()); // Set layout manager

        // Create a button
        JButton button = new JButton("Click Me!");

        // Add an action listener to the button
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame, "Button clicked!");
            }
        });

        // Add the button to the frame
        frame.add(button);

        // Make the frame visible
        frame.setVisible(true);
    }
}
