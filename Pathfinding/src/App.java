import javax.swing.*;
import java.awt.*;

public class App {
    App(){
        System.out.println("________Starting_______");
        JFrame frame = new JFrame("My First GUI");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 1000);
        JButton button1 = new JButton("Start");
        JButton button2 = new JButton("Reset");
        JButton button3 = new JButton("Generate Map");
        JButton button4 = new JButton("Clear Map");
        JButton button5 = new JButton("Credits");
        frame.getContentPane().add(button1);// Adds Button to content pane of frame
        frame.getContentPane().add(button2);
        frame.getContentPane().add(button3);
        frame.getContentPane().add(button4);
        frame.getContentPane().add(button5);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT));
        frame.setVisible(true);

    }

    
    public static void main(String[] args) {  
        new App();  
    }
}