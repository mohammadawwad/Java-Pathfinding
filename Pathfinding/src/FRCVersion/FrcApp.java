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
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextField;
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
    JCheckBox diagonal = new JCheckBox("Diagonal Movement");

    //Text Fields
    public static double theta = 0.0;
    public static boolean askAngle;

    public static boolean start = false;
    public static int check = 0;
    public static int length = 0;

    // intitalizing
    public static Map mapCanvas;
    static JFrame frame;
    static JPanel panel;
    JOptionPane popup;
    Hashtable<Integer, JLabel> labels;
    JSlider speedSlider;
    public static Node[][] map;
    public int test;
    public Random ran;
    public static boolean movement;
   
    

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
                Map.fillArea();
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
                Map.fillArea();
                System.out.println("Alliance: " + 1);
            }
        });

        diagonal.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e){
                movement = diagonal.isSelected();
                System.out.println("Diagonal Movement: " + movement);
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
        panel.add(diagonal);


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
    

}