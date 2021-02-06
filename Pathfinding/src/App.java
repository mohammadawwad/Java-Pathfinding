import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeListener;
import java.util.Hashtable;


public class App implements ActionListener, ChangeListener {

    static final int minSpdSlider = 0;             //Spees slider values
    static final int maxSpdSlider = 30;
    static final int initSpdSlider = 15;
    public static int sliderValue = 15;
    public static final int frameHeight  = 1000;    //Sets frame height and width
    public static final int frameWidth = 1000;     
    public static final int canvasHeight  = 1000;   //Sets canvas grid height and width
    public static final int canvasWidth = 1000;     
    private int cells = 20;                         //grid dimensions 20x20
    private final int HEIGHT = 650;
	private final int MSIZE = 600;
	private int CSIZE = MSIZE/cells;
    int mouseX = -10;
    int  mouseY = -10; 

    //intitalizing 
    public Map mapCanvas;
    JFrame frame;
    JPanel panel;
    JOptionPane popup;
    Hashtable<Integer, JLabel> labels;
    JSlider speedSlider;
    Dijkstra.Node[][] map;

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
        speedSlider = new JSlider(JSlider.HORIZONTAL, minSpdSlider, maxSpdSlider, initSpdSlider);
        labels = new Hashtable<>();
        mapCanvas = new Map();

        // Action listners
        //button1.addActionListener(this);
        //button2.addActionListener(this);
        //button3.addActionListener(this);
        //button4.addActionListener(this);
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

        // Menu Bar for Controls
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
        frame.setSize(frameWidth, frameHeight);
        frame.add(panel, BorderLayout.CENTER);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT));
        frame.setVisible(true);
        
        //adds grid canvas to the frame last so that menu bar loads on top
        mapCanvas.setPreferredSize(new Dimension(canvasHeight, canvasWidth));
        frame.getContentPane().add(mapCanvas);

    }

    //Creates Grid Canvas
    public class Map extends JPanel{
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);     // paint parent's background
            setBackground(Color.white);  // set background color for this JPanel

            //Creates Grid of boxes
            for(int x = 0; x < cells; x++) {	                //goes through loop to create boxes on 
				for(int y = 0; y < cells; y++) {
					g.setColor(Color.BLACK);                    //sets drawing colour for boxes black
					g.drawRect(x*CSIZE,y*CSIZE,CSIZE,CSIZE);    //draws the boxes
				}
                
            }

        }
        
    }


 

    //Event Actions
    @Override
    public void actionPerformed(ActionEvent e) {
        // Credits pop up message
        JOptionPane.showMessageDialog(frame, "                        Pathfinding Project \n                       Mohammad Awwad", "Credits", JOptionPane.PLAIN_MESSAGE);
        
    }

    @Override
    public void stateChanged(ChangeEvent e) {
        // Get Value of Slider
        JSlider slider = (JSlider)e.getSource();
        if (!slider.getValueIsAdjusting()) {
                System.out.println("Slider Pos: " + slider.getValue());
        }
    }

    /*@Override
    public void mouseDragged(MouseEvent e) {
        try {
            int x = e.getX()/CSIZE;	
            int y = e.getY()/CSIZE;
            Dijkstra.Node current = map[x][y];
            mapCanvas.Update();
        } catch(Exception z) {}
    }
*/




	public static void main(String[] args) {
        new App();
    }

}


    
