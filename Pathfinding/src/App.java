import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class App {

    //Speed Slider Values
    static final int minSpdSlider = 0;             
    static final int maxSpdSlider = 30;
    static final int initSpdSlider = 15;
    public static int sliderValue = 15;
    //Sets frame height and width
    public static final int frameHeight  = 1000;    
    public static final int frameWidth = 1000;     
    //Sets canvas grid height and width
    public static final int canvasHeight  = 1000;   
    public static final int canvasWidth = 1000;   
    //grid dimensions 20x20  
    private static int cells = 20; 
    private final int HEIGHT = 650;
    private final static int MSIZE = 600;
    //Canvas Size
    private static int CSIZE = MSIZE / cells; 
    int mouseX = -10;
    int mouseY = -10;
    private static int startx = -1;
    private static int starty = -1;
    private static int finishx = -1;
    private static int finishy = -1;
    private static int tool = 0;
    private int algo = 0;
    private String[] tools = { "Start", "Wall", "Erase", "Finish" };
    private String[] algoPicker = { "A*", "Dijkstra" };

    // intitalizing
    public static Map mapCanvas;
    JFrame frame;
    JPanel panel;
    JOptionPane popup;
    Hashtable<Integer, JLabel> labels;
    JSlider speedSlider;
    static Node[][] map;

    
    public static void main(String[] args) {
        new App();
    }

    //Constructor 
    public App(){
        cleanMap();
        initGUI();
    }

    //Loops through to reset every box
    public void cleanMap(){
        for(int x = 0; x > 20; x++){
            for(int y = 0; y > 20; y++){
                Node current = map[x][y];
                current.setType(3);
            }
        }
        System.out.println("Map Has Been Cleaned...");
    }

    // Updates Canvas
    //update() is a reserved word
    public void updateGrid() {
        CSIZE = MSIZE / cells;
        mapCanvas.repaint();
    }

    // Generates New Map
    public void newMap() {
        CSIZE = MSIZE / cells;
        mapCanvas.repaint();
    }



    public void initGUI(){
        System.out.println("________Starting_______");
        cleanMap();
        frame = new JFrame("Java Pathfinding");
        panel = new JPanel();
        popup = new JOptionPane();

        // Project Icon
        Image icon = Toolkit.getDefaultToolkit().getImage("C:/Users/Mohammad/Documents/GitHub/Java-Pathfinding/Pathfinding/src/icon.png");
        frame.setIconImage(icon);

        // Buttons
        JButton button1 = new JButton("Start");
        JButton button2 = new JButton("Reset");
        JButton button3 = new JButton("Generate Map");
        JButton button4 = new JButton("Clear Map");
        JButton button5 = new JButton("Credits");
        // Slider
        speedSlider = new JSlider(JSlider.HORIZONTAL, minSpdSlider, maxSpdSlider, initSpdSlider);
        labels = new Hashtable<>();
        mapCanvas = new Map();

        // Action listners For Buttons
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Generate New Map
                newMap();
                System.out.println("Generated New Map");
            }
        });

        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Credits pop up message
                JOptionPane.showMessageDialog(frame,
                        "                        Pathfinding Project \n                       Mohammad Awwad",
                        "Credits", JOptionPane.PLAIN_MESSAGE);

            }
        });

        
        //For slider use
        speedSlider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent e) {
                // Get Value of Slider
                JSlider slider = (JSlider)e.getSource();
                if (!slider.getValueIsAdjusting()) {
                        System.out.println("Slider Pos: " + slider.getValue());
                }
            }
        });


        // Turn on labels at major tick marks.
        speedSlider.setMajorTickSpacing(10);
        speedSlider.setMinorTickSpacing(5);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);

        // creatig Spead labels
        labels.put(minSpdSlider, new JLabel("slow"));
        labels.put(maxSpdSlider, new JLabel("fast"));
        speedSlider.setLabelTable(labels);

        // drop down list
        JComboBox dropDown = new JComboBox(algoPicker);
        JComboBox toolBx = new JComboBox(tools);

        dropDown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                algo = dropDown.getSelectedIndex();
                // changes algo box
            }
        });

        toolBx.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                tool = toolBx.getSelectedIndex();
                // changes tools box
            }
        });


        // Menu Bar for Controls
        panel.add(button1);
        panel.add(button2);
        panel.add(toolBx);
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

        // adds grid canvas to the frame last so that menu bar loads on top
        mapCanvas.setPreferredSize(new Dimension(canvasHeight, canvasWidth));
        frame.getContentPane().add(mapCanvas);

    }


    // Creates Grid Canvas
    public class Map extends JPanel implements MouseListener, MouseMotionListener {

        public Map() {
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g); // paint parent's background
            setBackground(Color.white);
            // Creates Grid of boxes
            for (int x = 0; x < cells; x++) {
                // goes through loop to create boxes on
                for (int y = 0; y < cells; y++) {

                    // Generates Random Walls
                    // int randomNum = (int) (Math.random() * 10); // 0 to 9
                    // if (randomNum < 3) {
                    // g.setColor(Color.BLACK);
                    // } else {
                    // g.setColor(Color.WHITE);
                    // }

                    //Constructor error here on map[x][y]
                    //System.out.println(map[x][y]);
                    int value = map[x][y].getType();
                    switch (value) {
                        case 0:
                            g.setColor(Color.GREEN);
                            break;
                        case 1:
                            g.setColor(Color.BLACK);
                            break;
                        case 2:
                            g.setColor(Color.WHITE);
                            break;
                        case 3:
                            g.setColor(Color.WHITE);
                            break;
                        case 4:
                            g.setColor(Color.CYAN);
                            break;
                        case 5:
                            g.setColor(Color.YELLOW);
                            break;
                        default:
                            g.setColor(Color.WHITE);
                    }

                    // Draws and Colours the boxes
                    g.fillRect(x * CSIZE, y * CSIZE, CSIZE, CSIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(x * CSIZE, y * CSIZE, CSIZE, CSIZE);
                }
            }
        }



        // Mouse Handlers
        @Override
        public void mouseDragged(MouseEvent e) {
            try {
                int x = e.getX() / CSIZE;
                int y = e.getY() / CSIZE;
                Node current = map[x][y];
                if ((tool == 2 || tool == 3) && (current.getType() != 0 && current.getType() != 1))
                    current.setType(tool);
                updateGrid();
            } catch (Exception z) {}
        }

        @Override
        public void mousePressed(MouseEvent e) {
            try {
                // Gets the X and Y value of where the mouse was clicked according to the screen
                int x = e.getX() / CSIZE;
                int y = e.getY() / CSIZE;
                Node current = map[x][y];
                switch (tool) {
                    case 0: { // START NODE
                        if (current.getType() != 2) { // IF NOT WALL
                            if (startx > -1 && starty > -1) { // IF START EXISTS SET IT TO EMPTY
                                map[startx][starty].setType(3);
                                map[startx][starty].setHops(-1);
                            }
                            current.setHops(0);
                            startx = x;
                            starty = y;
                            // sets the clicked box to become the START Node
                            current.setType(0);
                        }
                        break;
                    }
                    case 1: {// FINISH NODE
                        if (current.getType() != 2) { // IF NOT WALL
                            if (finishx > -1 && finishy > -1) // IF FINISH EXISTS SET IT TO EMPTY
                                map[finishx][finishy].setType(3);
                            finishx = x; // SET THE FINISH X AND Y
                            finishy = y;
                            current.setType(1); // SET THE NODE CLICKED TO BE FINISH
                        }
                        break;
                    }
                    default:
                        if (current.getType() != 0 && current.getType() != 1)
                            current.setType(tool);
                        break;
                }
                updateGrid();
                System.out.println("Type: " + current.getType());
            } catch (Exception z) {} // EXCEPTION HANDLER
        }

        @Override
        public void mouseMoved(MouseEvent e) {}
        @Override
        public void mouseClicked(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}

    }


    class Node {
		// Box Types
		// 0 = start 
        // 1 = finish
        // 4 = checked
        // 5 = finalpath
		private int boxType = 0;
		private int hops;
		private int x;
		private int y;
		private int lastX;
		private int lastY;
		private double dToEnd = 0;
	
        //CONSTRUCTOR
		public Node(int type, int x, int y) {	
			boxType = type;
			this.x = x;
			this.y = y;
			hops = -1;
		}
		
        //CALCULATES THE EUCLIDIAN DISTANCE TO THE FINISH NODE
		public double getEuclidDist() {		
			int xdif = Math.abs(x-finishx);
			int ydif = Math.abs(y-finishy);
			dToEnd = Math.sqrt((xdif*xdif)+(ydif*ydif));
			return dToEnd;
		}
		
        //Getting Methods 
		public int getX() {return x;}		
		public int getY() {return y;}
		public int getLastX() {return lastX;}
		public int getLastY() {return lastY;}
		public int getType() {return boxType;}
		public int getHops() {return hops;}
		
        //Setting Methods 
		public void setType(int type) {boxType = type;}		
		public void setLastNode(int x, int y) {lastX = x; lastY = y;}
		public void setHops(int hops) {this.hops = hops;}
	}


}


    
