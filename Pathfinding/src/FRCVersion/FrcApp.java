package FRCVersion;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Hashtable;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class FrcApp {

    
    public Algorithms Algorithm = new Algorithms();
    // Speed Slider Values
    static final int minSpdSlider = 0;
    static final int maxSpdSlider = 60;
    static final int initSpdSlider = 30;
    public static int sliderValue = 30;
    // Sets frame height and width
    public static final int frameHeight = 1200;
    public static final int frameWidth = 1800;
    // Sets canvas grid height and width
    public static final int canvasHeight = 701;
    public static final int canvasWidth = 1501;
    // grid dimensions cellsxcells 82x 160
    public static int cellsWidth = 125;
    public static int cellsHeight = 43;
    private final static int MSIZE = 1500;
    // Canvas Size
    public static int CSIZE = /*MSIZE / cellsWidth;*/ 10;
    int mouseX = -10;
    int mouseY = -10;
    public static int startx = -1;
    public static int starty = -1;
    public static int finishx = -1;
    public static int finishy = -1;
    public static int allianceState = 0;
    public static int tool = 0;
    public int algo = 0;
    private String[] tools = { "Start", "Wall", "Erase", "Finish" };
    private String[] algoPicker = { "A*", "Dijkstra" };
    private String[] allianceColor = { "Blue", "Red" };

    // drop down list
    JComboBox dropDown = new JComboBox(algoPicker);
    JComboBox toolBx = new JComboBox(tools);
    JComboBox alliance = new JComboBox(allianceColor);

    public static boolean start = false;
    public static int check = 0;
    public static int length = 0;

    // intitalizing
    public static Map mapCanvas;
    static JFrame frame;
    JPanel panel;
    JOptionPane popup;
    Hashtable<Integer, JLabel> labels;
    JSlider speedSlider;
    public static Node[][] map;
    public int test;
    public Random ran;
   
    

    // Constructor
    public FrcApp(){
        cleanMap();
        initGUI();
    }
    public static void staticCleanMap(){
        FrcApp frc = new FrcApp();
        frc.cleanMap();
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
    public static void updateGrid() {
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
                    Algorithm.AStar();
                    break;
                case 1:
                    Algorithm.Dijkstra();
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
    
}