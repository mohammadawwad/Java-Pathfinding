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
    // grid dimensions cellsxcells 82 x 160
    public static int cellsWidth = 193;
    public static int cellsHeight = 92;
    private final static int MSIZE = 1500;
    // Canvas Size
    public static int CSIZE = /*MSIZE / cellsWidth;*/ 7;
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
        CSIZE = 7;
        mapCanvas.repaint();
    }

    // Creates New Map
    public void newMap() {
        CSIZE = 7;
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
        //blocked areas that does not depend on your alliance color
        int[][] blocked = {
            //Red Trench Side
            {103,18}, {104,18}, {105,18}, {106,18}, {107,18}, {108,18}, {109,18}, {110,18}, {111,18}, {112,18},
            //Blue Trench Side
            {80,73}, {81,73}, {82,73}, {83,73}, {84,73}, {85,73}, {86,73}, {87,73}, {88,73}, {89,73}, {90,73},
            //Top Right Post
            {104,19}, {105,19}, {106,19}, {107,19}, {108,19}, {109,19}, {104,20}, {105,20} ,{106,20}, {107,20}, {108,20}, {109,20}, {110,20}, {105,21}, {106,21}, {107,21}, {108,21}, {109,21}, {110,21}, {105,22}, {106,22}, {107,22}, {108,22}, {109,22}, {110,22}, {111,22}, {106,23}, {107,23}, {108,23}, {109,23}, {106,24}, {107,24},
            //Bottom Right Post
            {123,52}, {122,52}, {121,52}, {119,53}, {120,53}, {121,53}, {122,53}, {123,53}, {124,53}, {118,54}, {119,54}, {120,54}, {121,54}, {122,54}, {123,54}, {124,54},{119,55}, {120,55}, {121,55}, {122,55}, {123,55}, {124,55}, {125,55}, {119,56}, {120,56}, {121,56}, {122,56}, {123,56}, {124,56}, {125,56}, {120,57}, {121,57}, {122,57}, {123,57}, {124,57}, {120,58}, {121,58},
            //Top Left Post
            {72,33}, {71,33}, {70,33}, {73,34}, {72,34}, {71,34}, {70,34}, {69,34}, {68,34}, {67,34}, {68,34}, {68,35}, {69,35}, {70,35}, {71,35}, {72,35}, {73,35}, {68,35}, {68,36}, {69,36}, {70,36}, {71,36}, {72,36}, {73,36}, {69,37}, {70,37,}, {71,37}, {72,37}, {73,37}, {74,37}, {69,38}, {70,38}, {71,38}, {72,38}, {73,38}, {69,39}, {70,39},
            //Bottom Left Post
            {86,67}, {85,67}, {87,68}, {86,68}, {85,68}, {84,68}, {82,69}, {83,68}, {87,69}, {86,69}, {85,69}, {84,69}, {83,69}, {83,69}, {82,70}, {83,70}, {84,70}, {85,70}, {86,70}, {87,70}, {88,71}, {87,71}, {86,71}, {85,71}, {84,71}, {83,71}, {82,71}, {83,72}, {84,72}, {85,72}, {86,72}, {87,72}, {88,72},
        };


        switch(allianceState){
            //Blue Aliance
            case 0:
                //blocked arreas based on your alliance color
                int[][] blockRed = {
                    //Red Alliance Scoring Zone
                    {17,23}, {18,24}, {19,25}, {20,26}, {21,27}, {22,27}, {23,28}, {22,29}, {21,30}, {20,31}, {19,32}, {18,33}, {16,35}, {23,29}, {22,30}, {21,31}, {20,32}, {19,33}, {18,24}, {17,34}, {18,34}, {16,35}, {17,35}, {22,28}, {21,26}, {20,25}, {19,24}, {18,23},
                    //Red Alliance Trench Zone
                    {69, 4}, {69,5}, {69,6}, {69,7}, {69,8}, {69,9}, {69,10} ,{69,11}, {69,12}, {69,13}, {69,14}, {69,15}, {69,16}, {69,17}, {69,18},
                    {70,18}, {71,18}, {72,18}, {73,18}, {74,18}, {75,18}, {76,18}, {77,18}, {78,18}, {79,18}, {80,18},{81,18},{82,18}, {83,18}, {84,18}, {85,18}, {86,18}, {87,18},{88,18},{89,18},{90,18},{91,18},{92,18}, {93,18}, {94,18},{95,18},{96,18},{97,18},{98,18},{99,18},{100,18},{101,18},{102,18},{113,18}, {114,18},{115,18}, {116,18},{117,18},{118,18},{119,18},{120,18},{121,18},{122,18},
                    {123, 4}, {123,5}, {123,6}, {123,7}, {123,8}, {123,9}, {123,10} ,{123,11}, {123,12}, {123,13}, {123,14}, {123,15}, {123,16}, {123,17}, {123,18},
                    //Red Alliance Loading Zone
                };
                //loops through array
                block(blockRed, 1);
            break;

            //Red Alliance
            case 1:
                //blocked arreas based on your alliance color
                int[][] blockBlue = {
                    //Blue Alliance Scoring Zone
                    
                    //Blue Alliance Trench Zone
                    {69, 73}, {69,74}, {69,75}, {69,76}, {69,77}, {69,78}, {69,79}, {69,80} ,{69,81}, {69,82}, {69,83}, {69,84}, {69,85}, {69,86}, {69,87},
                    {70,73}, {71,73}, {72,73}, {73,73}, {74,73}, {75,73}, {76,73}, {77,73}, {78,73}, {79,73},{91,73},{92,73}, {93,73}, {94,73},{95,73},{96,73},{97,73},{98,73},{99,73},{100,73},{101,73},{102,73},{103,73},{104,73},{105,73},{106,73},{107,73},{108,73},{109,73},{110,73},{111,73}, {112,73}, {113,73}, {114,73},{115,73}, {116,73},{117,73},{118,73},{119,73},{120,73},{121,73},{122,73},
                    {123, 73}, {123,74}, {123,75}, {123,76}, {123,77}, {123,78}, {123,79}, {123,80} ,{123,81}, {123,82}, {123,83}, {123,84}, {123,85}, {123,86}, {123,87},
                    //Blue Alliance Loading Zone
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