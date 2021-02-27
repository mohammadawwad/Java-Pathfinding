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
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

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

    // Speed Slider Values
    static final int minSpdSlider = 0;
    static final int maxSpdSlider = 30;
    static final int initSpdSlider = 15;
    public static int sliderValue = 15;
    // Sets frame height and width
    public static final int frameHeight = 1000;
    public static final int frameWidth = 1000;
    // Sets canvas grid height and width
    public static final int canvasHeight = 1000;
    public static final int canvasWidth = 1000;
    // grid dimensions 20x20
    private static int cells = 20;
    private final int HEIGHT = 650;
    private final static int MSIZE = 600;
    // Canvas Size
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

    // drop down list
    JComboBox dropDown = new JComboBox(algoPicker);
    JComboBox toolBx = new JComboBox(tools);

    public boolean start = false;

    // intitalizing
    public static Map mapCanvas;
    JFrame frame;
    JPanel panel;
    JOptionPane popup;
    Hashtable<Integer, JLabel> labels;
    JSlider speedSlider;
    static Node[][] map;
    public int test;
    public Random ran;
    // Algorithm algorithm = new Algorithm();
    Algorithm Algorithms = new Algorithm();
    public int check = 0;
    public int length = 0;

    public static void main(String[] args) {
        new App();
    }

    // Constructor
    public App() {
        cleanMap();
        initGUI();
    }

    // Loops through to reset every box
    public void cleanMap() {
        finishx = -1; // RESET THE START AND FINISH
        finishy = -1;
        startx = -1;
        starty = -1;
        map = new Node[cells][cells]; // CREATE NEW MAP OF NODES
        for (int x = 0; x < cells; x++) {
            for (int y = 0; y < cells; y++) {
                map[x][y] = new Node(2, x, y); // SET ALL NODES TO EMPTY
            }
        }
        System.out.println("Map Has Been Cleaned...");
    }

    // Updates Canvas
    // update() is a reserved word
    public void updateGrid() {
        CSIZE = MSIZE / cells;
        mapCanvas.repaint();
    }

    // Creates New Map
    public void newMap() {
        CSIZE = MSIZE / cells;
        mapCanvas.repaint();
    }

    // Generates Map with Random walls
    public void genNewMap() {
        Node current;
        cleanMap();
        for (int i = 0; i < (cells * cells) * .4; i++) {
            int ranX = (int) (Math.random() * 20);
            int ranY = (int) (Math.random() * 20);
            int x = ranX;
            int y = ranY;
            current = map[x][y]; // FIND A RANDOM NODE IN THE GRID
            current.setType(1); // SET NODE TO BE A WALL
        }
        System.out.println("Random Map Has Been Generated");
    }

    // starts searching for end node
    public void startFind() {
        if (start)
            switch (algo) {
                case 0:
                    Algorithms.Astar();
                    break;
                case 1:
                    Algorithms.Dijkstra();
                    break;
            }
    }

    public void initGUI() {
        System.out.println("________Starting_______");
        frame = new JFrame("Java Pathfinding");
        panel = new JPanel();
        popup = new JOptionPane();

        // Project Icon
        Image icon = Toolkit.getDefaultToolkit()
                .getImage("C:/Users/Mohammad/Documents/GitHub/Java-Pathfinding/Pathfinding/src/icon.png");
        frame.setIconImage(icon);

        // Buttons
        JButton button1 = new JButton("Start");
        JButton button2 = new JButton("Generate Map");
        JButton button3 = new JButton("Clear Map");
        JButton button4 = new JButton("Credits");
        // Slider
        speedSlider = new JSlider(JSlider.HORIZONTAL, minSpdSlider, maxSpdSlider, initSpdSlider);
        labels = new Hashtable<>();
        mapCanvas = new Map();

        // Action listners For Buttons
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Starting Search");
                // Checks if both the start and finish exist
                // if((startx > -1 && starty > -1) && (finishx > 1 && finishy > -1))
                start = true;
                startFind();
            }
        });

        // Generate new Map button
        button2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                genNewMap();
                updateGrid();
            }
        });

        // Clean Map Button
        button3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                newMap();
                cleanMap();
            }
        });

        // Credits Button
        button4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(frame,
                        "                        Pathfinding Project \n                       Mohammad Awwad",
                        "Credits", JOptionPane.PLAIN_MESSAGE);

            }
        });

        // For slider use
        speedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                // Get Value of Slider
                JSlider slider = (JSlider) e.getSource();
                if (!slider.getValueIsAdjusting()) {
                    System.out.println("Slider Pos: " + slider.getValue());
                }
            }
        });

        dropDown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                // changes algo box
                algo = dropDown.getSelectedIndex();
                System.out.println(algo);
            }
        });

        toolBx.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                tool = toolBx.getSelectedIndex();
                // changes tools box
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

        // Menu Bar for Controls
        panel.add(button1);
        panel.add(button2);
        panel.add(toolBx);
        panel.add(button3);
        panel.add(speedSlider);
        panel.add(dropDown);
        panel.add(button4);

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
            super.paintComponent(g);

            // paint parent's background
            setBackground(Color.white);

            // Creates Grid of boxes
            // goes through loop to create boxes on
            for (int x = 0; x < cells; x++) {
                for (int y = 0; y < cells; y++) {

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
                            g.setColor(Color.RED);
                            break;
                        case 4:
                            g.setColor(Color.PINK);
                            break;
                        case 5:
                            g.setColor(Color.CYAN);
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
                if ((tool == 1 || tool == 2) && (current.getType() != 0 && current.getType() != 3))
                    current.setType(tool);
                updateGrid();
            } catch (Exception z) {
            }
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
                        if (current.getType() != 1) { // IF NOT WALL
                            if (startx > -1 && starty > -1) { // IF START EXISTS SET IT TO EMPTY
                                map[startx][starty].setType(2);
                                map[startx][starty].setHops(-1); // -1 reperesent the start node location
                            }
                            current.setHops(0);
                            startx = x;
                            starty = y;
                            // sets the clicked box to become the START Node
                            current.setType(0);
                            // test(startx, starty); this is used for testing to see available slots
                        }
                        break;
                    }
                    case 3: {// FINISH NODE
                        if (current.getType() != 1) { // IF NOT WALL
                            if (finishx > -1 && finishy > -1) // IF FINISH EXISTS SET IT TO EMPTY
                                map[finishx][finishy].setType(2);
                            finishx = x; // SET THE FINISH X AND Y
                            finishy = y;
                            current.setType(3); // SET THE NODE CLICKED TO BE FINISH
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
            } catch (Exception z) {
            } // EXCEPTION HANDLER
        }

        @Override
        public void mouseMoved(MouseEvent e) {
        }

        @Override
        public void mouseClicked(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

    }

    class Algorithm {
        // what Ill need - Order
        // Dijkstra algo -5 (last since i only need one algo to start testing)
        // A* algo -3
        // queing method -1
        // serching method -2
        // backtracking method -4

        public void Dijkstra() {
            System.out.println("Dijkstra");
            ArrayList<Node> priority = new ArrayList<Node>(); // CREATE A PRIORITY QUE
            priority.add(map[startx][starty]); // ADD THE START TO THE QUE
            while (start) {
                if (priority.size() <= 0) { // IF THE QUE IS 0 THEN NO PATH CAN BE FOUND
                    start = false;
                    System.out.println("Cant Find Path");
                    break;
                }
                int hops = priority.get(0).getHops() + 1; // INCREMENT THE HOPS VARIABLE
                ArrayList<Node> explored = exploreNearby(priority.get(0), hops); // CREATE AN ARRAYLIST OF NODES THAT
                                                                                 // WERE EXPLORED
                if (explored.size() > 0) {
                    priority.remove(0); // REMOVE THE NODE FROM THE QUE
                    priority.addAll(explored); // ADD ALL THE NEW NODES TO THE QUE
                    updateGrid();
                    try {
                        Thread.sleep(30);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
				} else {	//IF NO NODES WERE EXPLORED THEN JUST REMOVE THE NODE FROM THE QUE
					priority.remove(0);
				}
			}
        }

        public void Astar(){

        }

        //que is needed for A star only as A star is based of searching 
        //the closest node to the finish
        // public ArrayList<Node> que(ArrayList<Node> sort){
        //     return sort;

        // }

        public ArrayList<Node> exploreNearby(Node current, int hops) {
            ArrayList<Node> explored = new ArrayList<Node>();	//LIST OF NODES THAT HAVE BEEN EXPLORED return null;
            //for loop to check all nodes surrounding the current node
            //setting to -1 since that represents the home node
            System.out.println("Exploring Nearby");
            int xAxis;
            int yAxis;
            for(xAxis = -1; xAxis <= 1; xAxis++){
                for(yAxis = -1; yAxis <= 1; yAxis++){
                    int x = current.getX() + xAxis;
                    int y = current.getY() + yAxis;
                    //checks to see if blocks are in the grid
                    if((x > -1) && (x < cells) && (y > -1) && (y < cells)){
                        Node nearby = map[x][y];
                        //check if its not a wall
                        if((nearby.getHops() == -1 || nearby.getHops() > 1) && (nearby.getType() != 1)){
                            //call the searching method
                            searchNode(nearby, current.getX(), current.getY(), hops);
                            explored.add(nearby);// adding all nearby nodes that arnt walls to list
                        }

                    }
                }
            }
            return explored; //returning the list value
        }

        //Backtracjing so it can draw the correct path
        public void backtrack(int lastX, int lastY, int hops){
            length = hops;
			while(hops > 1) {	
				Node current = map[lastX][lastY];
                //sets it to the final path
                System.out.println("BackTracking");
				current.setType(4);
				lastX = current.getLastX();
				lastY = current.getLastY();
				hops--;
			}
			start = false;
		}
        

        //Explores nearby nodes
        public void searchNode(Node current, int lastX, int lastY, int hops){
            if(current.getType() != 0 && current.getType() != 3){
                //search
                System.out.println("Searching For END Node");
                current.setType(5);
            }
            
            //Keeping track of the nodes and the distance
            current.setLastNode(lastX, lastY);
            current.setHops(hops);
            check++;
            if(current.getType() == 3){
                //start backtracking
                System.out.println("Finish Node Found");
                backtrack(current.getLastX(), current.getLastY(), hops);
            }
        }

    }



    class Node {
		// Box Types
		// 0 = start 
        // 1 = wall
        // 2 = erase
        // 4 = finish
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