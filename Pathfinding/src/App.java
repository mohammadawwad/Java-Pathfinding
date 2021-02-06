import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;

public class App implements ActionListener, ChangeListener {
    // frames per second
    static final int minSpdSlider = 0;
    static final int maxSpdSlider = 30;
    static final int initSpdSlider = 15;
    JFrame frame;
    JPanel panel;
    JOptionPane popup;

    App() {
        System.out.println("________Starting_______");
        frame = new JFrame("Java Pathfinding");
        panel = new JPanel();
        popup = new JOptionPane();
        JButton button1 = new JButton("Start");
        JButton button2 = new JButton("Reset");
        JButton button3 = new JButton("Generate Map");
        JButton button4 = new JButton("Clear Map");
        JButton button5 = new JButton("Credits");
        JSlider speedSlider = new JSlider(JSlider.HORIZONTAL, minSpdSlider, maxSpdSlider, initSpdSlider);
        Hashtable<Integer, JLabel> labels = new Hashtable<>();

        // Testing Section Remove When Done

        // Action listners
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);
        button5.addActionListener(this);
        speedSlider.addChangeListener(this);

        // Turn on labels at major tick marks.
        speedSlider.setMajorTickSpacing(10);
        speedSlider.setMinorTickSpacing(5);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);

        // creatig Spead labels
        labels.put(minSpdSlider, new JLabel("slow"));
        labels.put(maxSpdSlider, new JLabel("fast"));
        speedSlider.setLabelTable(labels);

        // drop down list for algorithims
        String[] algoPicker = { "A*", "Dijkstra" };
        JComboBox dropDown = new JComboBox(algoPicker);
        dropDown.setSelectedIndex(0);
        // algoPicker.addActionListener(this);

        // Adding things that need show here
        panel.add(button1);
        panel.add(button2);
        panel.add(button3);
        panel.add(button4);
        panel.add(speedSlider);
        panel.add(dropDown);
        panel.add(button5);

        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        frame.add(panel, BorderLayout.CENTER);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT));
        frame.setVisible(true);

    }


    public static void main(String[] args) {
        new App();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        JOptionPane.showMessageDialog(frame, "                        Pathfinding Project \n                       Mohammad Awwad", "Credits", JOptionPane.PLAIN_MESSAGE);
        
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // TODO Auto-generated method stub

    }

  
    
}