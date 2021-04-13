package FRCVersion;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class frcAutoNav {

    
    public Algorithms Algorithm = new Algorithms();
    // Speed Slider Values
    static final int minSpdSlider = 0;
    static final int maxSpdSlider = 60;
    static final int initSpdSlider = 30;
    public int sliderValue = 30;
    // Sets frame height and width
    public static final int frameHeight = 1200;
    public static final int frameWidth = 1800;
    // Sets canvas grid height and width
    public static final int canvasHeight = 701;
    public static final int canvasWidth = 1501;
    // grid dimensions cellsxcells 82x 160
    public int cellsWidth = 90;
    public int cellsHeight = 43;
    private final static int MSIZE = 1500;
    // Canvas Size
    public int CSIZE = /*MSIZE / cellsWidth;*/ 15;
    int mouseX = -10;
    int mouseY = -10;
    public int startx = -1;
    public int starty = -1;
    public int finishx = -1;
    public int finishy = -1;
    public static int allianceState = 0;
    public int tool = 0;
    public int algo = 0;
    private String[] tools = { "Start", "Wall", "Erase", "Finish" };
    private String[] algoPicker = { "A*", "Dijkstra" };
    private String[] allianceColor = { "Blue", "Red" };

    // drop down list
    JComboBox dropDown = new JComboBox(algoPicker);
    JComboBox toolBx = new JComboBox(tools);
    JComboBox alliance = new JComboBox(allianceColor);

    public boolean start = false;
    public int check = 0;
    public int length = 0;

    // intitalizing
    public static Map mapCanvas;
    JFrame frame;
    JPanel panel;
    JOptionPane popup;
    Hashtable<Integer, JLabel> labels;
    JSlider speedSlider;
    public static Node[][] map;
    public int test;
    public Random ran;
   
    

    // Constructor
    public frcAutoNav(){
        cleanMap();
        initGUI();
    }

    // Loops through to reset every box
    public void cleanMap() {
        //Resets start and finish 
        finishx = -1; 
        finishy = -1;
        startx = -1;
        starty = -1;
        //creates new map of nodes
        map = new Node[cellsWidth][cellsHeight]; 
        for (int x = 0; x < cellsWidth; x++) {
            for (int y = 0; y < cellsHeight; y++) {
                map[x][y] = new Node(2, x, y); 
                //sets all nodes to blank
            }
        }
        System.out.println("Map Has Been Cleaned...");
    }

    // Updates Canvas
    // update() is a reserved word
    public void updateGrid() {
        CSIZE = /*MSIZE / cellsWidth*/ 15;
        mapCanvas.repaint();
    }

    // Creates New Map
    public void newMap() {
        CSIZE = /*MSIZE / cellsWidth*/ 15;
        mapCanvas.repaint();
    }

    // Generates Map with Random walls
    public void genNewMap() {
        Node current;
        cleanMap();
        for (int i = 0; i < (cellsHeight * cellsWidth) * .4; i++) {
            int ranX = (int) (Math.random() * cellsHeight + cellsWidth / 2);
            int ranY = (int) (Math.random() * cellsHeight + cellsWidth / 2);
            current = map[ranX][ranY]; // FIND A RANDOM NODE IN THE GRID
            current.setType(1); // SET NODE TO BE A WALL
        }
        System.out.println("Random Map Has Been Generated");
    }

    // starts searching for end node
    public void startFind() {
        if (start){
            switch (algo) {
                case 0:
                    //Algorithm.AStar();
                    break;
                case 1:
                    //Algorithm.Dijkstra();
                    break;
            }
        }  
        //Allows us to see algorithm
        while(!start){
            try {
                Thread.sleep(1);
            } catch(Exception e) {}
        }
        startFind();
    }

    public void initGUI(){
        System.out.println("________Starting_______");

        frame = new JFrame("Java Pathfinding");
        panel = new JPanel();
        popup = new JOptionPane();
        mapCanvas = new Map();

        // Project Icon
        Image icon = Toolkit.getDefaultToolkit()
                .getImage("C:/Users/Mohammad/Documents/GitHub/Java-Pathfinding/Pathfinding/src/imgs/icon.png");
        frame.setIconImage(icon);

        // Buttons
        JButton button1 = new JButton("Start");
        JButton button2 = new JButton("Generate Map");
        JButton button3 = new JButton("Clear Map");
        JButton button4 = new JButton("Credits");
        // Slider
        speedSlider = new JSlider(JSlider.HORIZONTAL, minSpdSlider, maxSpdSlider, initSpdSlider);
        labels = new Hashtable<>();

        // Action listners For Buttons
        button1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Starting Search");
                //sets boolean to true allowing algorithim to
                start = true;
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
                fillArea();
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
                sliderValue = slider.getValue();
                if (!slider.getValueIsAdjusting()) {
                    System.out.println("Slider Pos: " + slider.getValue());
                }
            }
        });

        //Algorithms drop down
        dropDown.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                algo = dropDown.getSelectedIndex();
                System.out.println("Algorithm: " + algo);
            }
        });

        //Tool box drop down 
        toolBx.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                tool = toolBx.getSelectedIndex();
                System.out.println("Tool: " + tool);
            }
        });

        //Alliance Station Drop Down
        alliance.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                allianceState = alliance.getSelectedIndex();
                updateGrid();
                cleanMap();
                fillArea();
                System.out.println("Alliance: " + 1);
            }
        });


        // Turn on labels at major tick marks.
        speedSlider.setMajorTickSpacing(10);
        speedSlider.setMinorTickSpacing(5);
        speedSlider.setPaintTicks(true);
        speedSlider.setPaintLabels(true);

        // creatig Spead labels
        labels.put(minSpdSlider, new JLabel("Fast"));
        labels.put(maxSpdSlider, new JLabel("Slow"));
        speedSlider.setLabelTable(labels);

        // Menu Bar for Controls
        panel.add(toolBx);
        panel.add(button1);
        panel.add(button2);
        panel.add(button3);
        panel.add(speedSlider);
        panel.add(dropDown);
        panel.add(alliance);
        panel.add(button4);

        //Scrolling
        JScrollPane scrPane = new JScrollPane(panel);
        frame.add(scrPane);

        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        panel.setLayout(new GridLayout(0, 1));
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.add(panel, BorderLayout.CENTER);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT));
        frame.setVisible(true);

        // adds grid canvas to the frame last so that menu bar loads on top
        mapCanvas.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        frame.getContentPane().add(mapCanvas);

        startFind();
        
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
            setBackground(Color.WHITE);
            setOpaque(true);

            //Creates Field
            BufferedImage image;
            try {
                image = ImageIO.read(new File("C:/Users/Mohammad/Documents/GitHub/Java-Pathfinding/Pathfinding/src/imgs/2021_field.png"));
                g.drawImage(image, 0, 0, null);
            } catch (IOException e) {e.printStackTrace();}

            // Creates Grid of boxes
            // goes through loop to create boxes on
            for (int x = 0; x < cellsWidth; x++) {
                for (int y = 0; y < cellsHeight; y++) {

                    int value = map[x][y].getType();
                    switch (value) {
                        case 0:
                            //Green
                            g.setColor(new Color(51, 255, 0, 85));
                            break;
                        case 1:
                            //Black
                            g.setColor(new Color(0, 0, 0));
                            break;
                        case 2:
                            //White
                            g.setColor(new Color(0, 0, 0, 80));
                            break;
                        case 3:
                            //Red
                            g.setColor(new Color(255, 0, 0, 85));
                            break;
                        case 4:
                            //Cyan
                            g.setColor(new Color(255, 183, 0, 80));
                            break;
                        case 5:
                            //Orange
                            g.setColor(new Color(66, 209, 245, 80));
                            break;
                        default:
                            //White
                            g.setColor(new Color(0, 0, 0, 80));
                    }
                    
                    // Draws and Colours the boxes
                    g.fillRect(x * CSIZE, y * CSIZE, CSIZE, CSIZE);
                    g.setColor(Color.BLACK);
                    g.drawRect(x * CSIZE, y * CSIZE, CSIZE, CSIZE);
                    
                }
            }
            fillArea();

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
                System.out.println("Co-ordinates:" + x + ", " + y);
                switch (tool) {
                    case 0: { // START NODE
                        //if not a wall
                        if (current.getType() != 1) { 
                            //if start exist sets it to blank
                            if (startx > -1 && starty > -1) {
                                map[startx][starty].setType(2);
                                map[startx][starty].setHops(-1); // -1 reperesent the start node location
                            }
                            current.setHops(0);
                            startx = x;
                            starty = y;
                            // sets the clicked box to become the START Node
                            current.setType(0);
                        }
                        break;
                    }
                    case 3: {// FINISH NODE
                        //if not a wall
                        if (current.getType() != 1) { 
                            // if fininsh exists set it to empty
                            if (finishx > -1 && finishy > -1) 
                                map[finishx][finishy].setType(2);
                            finishx = x; 
                            finishy = y;
                            current.setType(3); 
                            //sets clicked node to be red
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

    public static void fillArea(){
        //Multi dimentional arrays 
        //blocked areas that dont depend on your alliance color
        int[][] blocked = {
            {32,16}, {32,17}, {33,16}, {33,17}, //top left post
            {49,9}, {50,9}, {49,10}, {50,10},   //top right post
            {39,31}, {40,31}, {40,32}, {39,32}, //bottom left post
            {56,25}, {57,25}, {56,26}, {57,26},  //bottom right post
            //Trench walls
            {41,33}, {40,33}, {39,33}, {38,33}, {37,33},
            {48,8}, {49,8}, {50,8}, {51,8}, {52,8}
        };


        switch(allianceState){
            //Blue Aliance
            case 0:
                //blocked arreas based on your alliance color
                int[][] blockRed = {
                    //Enemy Trench
                    {32,2}, {32,3}, {32,4}, {32,5}, {32,6}, {32,7}, 
                    {32,8}, {33,8}, {34,8}, {35,8}, {36,8}, {37,8}, {38,8}, {39,8}, {40,8}, {41,8}, {42,8}, {43,8}, {44,8}, {45,8}, {46,8}, {47,8}, {48,8}, {49,8}, {50,8}, {51,8}, {52,8}, {53,8}, {54,8}, {55,8}, {56,8}, {57,8},
                    {57,7}, {57,6}, {57,5}, {57,4}, {57,3}, {57,2}, 
                    //Enemy Scoring Zone
                    {8,11}, {9,12}, {10,13}, {9,14}, {8,15}, 
                    //Enemy Loading Zone
                    {81,16}, {80,15}, {79,14}, {79,13}, {80,12}, {81,11}, 
                };
                //loops through array
                block(blockRed, 1);
            break;

            //Red Alliance
            case 1:
                //blocked arreas based on your alliance color
                int[][] blockBlue = {
                    {0,0}, {1,1}
                };
                //loops through array
                block(blockBlue, 1);
            break; 

            default:
                block(blocked, 1);

        }

        
        block(blocked, 1);
    }

    public static void block(int[][] array, int color){
        int x;
        int y;
        for(int i = 0; array.length > i; i++){
            x = array[i][0];
            y = array[i][1]; 
            Node current = map[x][y];
            current.setType(color);//1 for black
        }
    }

    // class Algorithm {

    //     public void Dijkstra() {
    //         System.out.println("Dijkstra");
    //         //creates a priority que
    //         ArrayList<Node> priority = new ArrayList<Node>(); 
    //         //add the start node first to the que
    //         priority.add(map[startx][starty]); 
    //         while (start) {
    //             // if the que == 0 no path exists or cannot be ound
    //             if (priority.size() <= 0) { 
    //                 start = false;
    //                 System.out.println("Cant Find Path");
    //                 JOptionPane.showMessageDialog(frame, "             No Path Found \n        Map Will be Cleared ");
    //                 cleanMap();
    //                 updateGrid();
    //                 break;
    //             }
    //             int hops = priority.get(0).getHops() + 1; 
    //             //array list of explored nodes
    //             ArrayList<Node> explored = exploreNearby(priority.get(0), hops); 
    //             if (explored.size() > 0) {
    //                 //remove from que
    //                 priority.remove(0); 
    //                 //add all new nodes
    //                 priority.addAll(explored); 
    //                 try {
    //                     Thread.sleep(sliderValue);
    //                     updateGrid();
    //                 } catch (InterruptedException e) {
    //                     e.printStackTrace();
    //                 }
	// 			} else {	//IF NO NODES WERE EXPLORED THEN JUST REMOVE THE NODE FROM THE QUE
	// 				priority.remove(0);
	// 			}
	// 		}
    //     }

    //     //Similar to Dijkstra Just has a sort queing method to search in the direction of the end Finish Node
	// 	public void AStar() {
	// 		ArrayList<Node> priority = new ArrayList<Node>();
	// 		priority.add(map[startx][starty]);
	// 		while(start) {
	// 			if(priority.size() <= 0) {
	// 				start = false;
    //                 System.out.println("Cant Find Path");
    //                 JOptionPane.showMessageDialog(frame, "             No Path Found \n        Map Will be Cleared ");
    //                 cleanMap();
    //                 updateGrid();
	// 				break;
	// 			}
	// 			int hops = priority.get(0).getHops() + 1;
	// 			ArrayList<Node> explored = exploreNearby(priority.get(0),hops);
	// 			if(explored.size() > 0) {
	// 				priority.remove(0);
	// 				priority.addAll(explored);
    //                 try {
    //                     Thread.sleep(sliderValue);
    //                     updateGrid();
    //                 } catch (InterruptedException e) {
    //                     e.printStackTrace();
    //                 }
	// 			} else {
	// 				priority.remove(0);
	// 			}
	// 			sortPri(priority);
	// 		}
	// 	}
		
	
    //     public ArrayList<Node> sortPri(ArrayList<Node> sort){
    //         int count = 0;
    //         while(count < sort.size()){
    //             int cAdd = count;
    //             //count++ in for loop causes errors
    //             for(int x = count + 1; x < sort.size(); x++){
    //                 //compare a euclid distance to a diffrent ecuclid distance by having 2 variable 
    //                 // 1 variable will always be greater than the other giving diffrent distances back
    //                 if(sort.get(x).getEDistance() + sort.get(x).getHops() < sort.get(cAdd).getEDistance() + sort.get(cAdd).getHops()){
    //                     cAdd = x;
    //                 }
    //             }

    //             if(count != cAdd){
	// 				Node temp = sort.get(count);
	// 				sort.set(count, sort.get(cAdd));
	// 				sort.set(cAdd, temp);
    //             }

    //             count++;
    //         }

    //     return sort;
    //     }

    //     public ArrayList<Node> exploreNearby(Node current, int hops) {
    //         //list of nodes that have been explored
    //         ArrayList<Node> explored = new ArrayList<Node>();	
    //         //for loop to check all nodes surrounding the current node
    //         //setting to -1 since that represents the home node
    //         System.out.println("Exploring Nearby");
    //         int xAxis;
    //         int yAxis;

    //         //seperate loops to avoid diagonal movements
    //         for(xAxis = -1; xAxis <= 1; xAxis++){
    //             int x = current.getX() + xAxis;
    //             //checks to see if next X bloxk is in the grid
    //             if(x > -1 && x < cellsWidth){
    //                 Node nearby = map[x][current.getY()];
    //                 if((nearby.getHops() == -1 || nearby.getHops() > hops) && (nearby.getType() != 1)){
    //                     //call the searching method
    //                     searchNode(nearby, current.getX(), current.getY(), hops);
    //                     explored.add(nearby);// adding all nearby nodes that arnt walls to list
    //                 }
    //             }
    //         }
    //         for(yAxis = -1; yAxis <= 1; yAxis++){
    //             int y = current.getY() + yAxis;
    //             //checks to see if next Y block is in the Grid
    //             if(y > -1 && y < cellsHeight){
    //                 Node nearby = map[current.getX()][y];
    //                 //check if its not a wall
    //                 if((nearby.getHops() == -1 || nearby.getHops() > hops) && (nearby.getType() != 1)){
    //                     //call the searching method
    //                     searchNode(nearby, current.getX(), current.getY(), hops);
    //                     explored.add(nearby);// adding all nearby nodes that arnt walls to lis
    //                 }

    //             }
    //         }
    //         return explored; //returning the list value
    //     }

        
    //     //Explores nearby nodes
    //     public void searchNode(Node current, int lastX, int lastY, int hops){
    //         if(current.getType() != 0 && current.getType() != 3){
    //             //search
    //             System.out.println("Searching For END Node");
    //             current.setType(5);
    //         }
            
    //         //Keeping track of the nodes and the distance
    //         current.setLastNode(lastX, lastY);
    //         current.setHops(hops);
    //         check++;
    //         if(current.getType() == 3){
    //             //start backtracking
    //             System.out.println("Finish Node Found");
    //             backtrack(current.getLastX(), current.getLastY(), hops);
    //         }
    //     }

    //     //Backtracjing so it can draw the correct path
    //     public void backtrack(int lastX, int lastY, int hops){
    //         length = hops;
	// 		while(hops > 1) {	
	// 			Node current = map[lastX][lastY];
    //             //sets it to the final path
    //             System.out.println("BackTracking");
	// 			current.setType(4);
	// 			lastX = current.getLastX();
	// 			lastY = current.getLastY();
	// 			hops--;
	// 		}
	// 		start = false;
	// 	}
        
    // }



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
		private double distance = 0;
	
        //CONSTRUCTOR
		public Node(int type, int x, int y) {	
			boxType = type;
			this.x = x;
			this.y = y;
			hops = -1;
		}

        //Euclidean Distance caluclates the shortest distance between to co-ordinates
        ///in this case we want it to be the current nodes position with the finish distance
        // PS I googled the formula I havent taken this in Math Yet :)
        public double getEDistance(){
            //current - finish
            int newX = Math.abs(x - finishx); //absolute value of X
            int newY = Math.abs(y - finishy); //absolute value of Y
            distance = Math.sqrt((newX * newX) + (newY * newY));
            return distance;
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