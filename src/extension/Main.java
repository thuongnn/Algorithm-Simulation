package extension;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                frame.setTitle("Merge Sort & Floating Point Number Simulation");
                frame.setContentPane(new Controller().contentPane);
                frame.setExtendedState(JFrame.MAXIMIZED_BOTH); //set JFrame full screen
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setVisible(true);
            }
        });
    }
}
