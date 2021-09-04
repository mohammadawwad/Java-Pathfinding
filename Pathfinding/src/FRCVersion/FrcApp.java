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
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
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
import org.json.simple.JSONObject;
import java.util.LinkedHashMap;
import org.json.simple.JSONArray;

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
    public static int theta = 1;
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
    public int pathNumber = 1;
    private String[] tools = { "Start", "Wall", "Erase", "Finish" };
    private String[] algoPicker = { "A*", "Dijkstra" };
    private String[] allianceColor = { "Blue", "Red" };
    public String path = "C:\\Users\\Mohammad\\Documents\\GitHub\\Java-Pathfinding\\Pathfinding\\src\\FRCVersion\\generatedPaths\\autoPath";


    // drop down list
    JComboBox dropDown = new JComboBox(algoPicker);
    JComboBox toolBx = new JComboBox(tools);
    JComboBox alliance = new JComboBox(allianceColor);
    JCheckBox diagonal = new JCheckBox("Diagonal Movement");

    public static boolean start = false;
    public static int check = 0;
    public static int length = 0;

    // intitalizing
    public static String drivePStr, driveIStr, driveDStr, driveILimitStr, driveThresholdStr;
    public static String rotationPStr, rotationIStr, rotationDStr, rotationILimitStr, rotationThresholdStr;
    public static Map mapCanvas;
    static JFrame frame;
    static JPanel topPanel, midPanel, bottomPanel;
    JOptionPane popup;
    Hashtable<Integer, JLabel> labels;
    JSlider speedSlider;
    public static Node[][]/*[]*/ map;
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
        map = new Node[cellsWidth][cellsHeight]/*[theta]*/; 
        for (int x = 0; x < cellsWidth; x++) {
            for (int y = 0; y < cellsHeight; y++) {
                // for (int angle = 0; angle < theta; angle++) {
                //     map[x][y][angle] = new Node(2, x, y, angle); 
                //     //sets all nodes to blank
                // }
                map[x][y] = new Node(2, x, y); 
               
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
            current = map[ranX][ranY]/*[0]*/; // FIND A RANDOM NODE IN THE GRID
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
        topPanel = new JPanel();
        midPanel = new JPanel();
        bottomPanel = new JPanel();
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
        JButton button5 = new JButton("Generate Path");

        //PID Settings For Drive
        JLabel driveLabel = new JLabel("Drive Settings");
        JTextField driveP = new JTextField("P Value: 0.045", 15);
        JTextField driveI = new JTextField("I Value: 0.03", 15);
        JTextField driveD = new JTextField("D Value: 0.00000000008", 15);
        JTextField driveILimit = new JTextField("ILimit Value: 0.2", 15);
        JTextField driveThreshold = new JTextField("Threshold Value: 0.025", 15);

        //PID Settings For Rotation
        JLabel rotationLabel = new JLabel("Rotation Settings");
        JTextField rotationP = new JTextField("P Value: 0.0015", 15);
        JTextField rotationI = new JTextField("I Value: 0.005", 15);
        JTextField rotationD = new JTextField("D Value: 0.0", 15);
        JTextField rotationILimit = new JTextField("ILimit Value: 10.0", 15);
        JTextField rotationThreshold = new JTextField("Threshold Value: 0.2", 15);

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
                "                        Pathfinding Project \n                       Mohammad Awwad", "Credits", JOptionPane.PLAIN_MESSAGE);
            }
        });

        // Path Generator Button
        button5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File pathFile = new File(path + pathNumber + ".json");
                    pathFile.createNewFile();
                    while(pathFile.exists() == true){
                        pathNumber++;
                        System.out.println("Path number: " + pathNumber);
                        File tmpDir = new File(path + pathNumber + ".json");
                        if(tmpDir.exists() == false){
                            pathFile = tmpDir;
                            pathFile.createNewFile();
                            break;
                        }
                    }

                    
                    //Write JSON file
                    try (FileWriter file = new FileWriter(pathFile)) {

                        // creating JSONObject
                        JSONObject jsonObj = new JSONObject();
                        
                        // creating JSONArray's
                        JSONArray jaPos = new JSONArray();
                        JSONArray jaDrive = new JSONArray();
                        JSONArray jaRotation = new JSONArray();

                        LinkedHashMap m;

                        //add for loop to create obj based off list size
                        for(int i = 0; i < Algorithm.xCords().size(); i++){
                            //Cords and Drive/Rotation Arrays
                            m = new LinkedHashMap(2);
                            m.put("x", Algorithm.xCords().get(i));
                            m.put("y", Algorithm.yCords().get(i));
                            m.put("theta", 0.0);
                            m.put("drive", jaDrive);
                            m.put("rotation", jaRotation);
                            jaPos.add(m);
                        }

                        //Drive
                        drivePStr = drivePStr.replaceAll("[^\\d.]", "");
                        driveIStr = driveIStr.replaceAll("[^\\d.]", "");
                        driveDStr = driveDStr.replaceAll("[^\\d.]", "");
                        driveILimitStr = driveILimitStr.replaceAll("[^\\d.]", "");
                        driveThresholdStr = driveThresholdStr.replaceAll("[^\\d.]", "");

                        double driveP = Double.parseDouble(drivePStr);
                        double driveI = Double.parseDouble(driveIStr);
                        double driveD = Double.parseDouble(driveDStr);
                        double driveILimit = Double.parseDouble(driveILimitStr);
                        double driveThreshold = Double.parseDouble(driveThresholdStr);

                        m = new LinkedHashMap();
                        m.put("p", driveP);
                        m.put("i", driveI);
                        m.put("d", driveD);
                        m.put("iLimit", driveILimit);
                        m.put("threshold", driveThreshold);
                        jaDrive.add(m);

                        //Rotation
                        rotationPStr = rotationPStr.replaceAll("[^\\d.]", "");
                        rotationIStr = rotationIStr.replaceAll("[^\\d.]", "");
                        rotationDStr = rotationDStr.replaceAll("[^\\d.]", "");
                        rotationILimitStr = rotationILimitStr.replaceAll("[^\\d.]", "");
                        rotationThresholdStr = rotationThresholdStr.replaceAll("[^\\d.]", "");

                        double rotationP = Double.parseDouble(rotationPStr);
                        double rotationI = Double.parseDouble(rotationIStr);
                        double rotationD = Double.parseDouble(rotationDStr);
                        double rotationILimit = Double.parseDouble(rotationILimitStr);
                        double rotationThreshold = Double.parseDouble(rotationThresholdStr);

                        m = new LinkedHashMap();
                        m.put("p", rotationP);
                        m.put("i", rotationI);
                        m.put("d", rotationD);
                        m.put("iLimit", rotationILimit);
                        m.put("threshold", rotationThreshold);
                        jaRotation.add(m);
                        
                        
                        jsonObj.put("pos", jaPos);

                        file.write(jsonObj.toJSONString());
                        file.flush();
                        file.close();
                    } 

                } catch (IOException e1) {
                    e1.printStackTrace();
                }
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


        //taking input from drive and rotation Settings
        drivePStr = driveP.getText();
        driveIStr = driveI.getText();
        driveDStr = driveD.getText();
        driveILimitStr = driveILimit.getText();
        driveThresholdStr = driveThreshold.getText();

        rotationPStr = rotationP.getText();
        rotationIStr = rotationI.getText();
        rotationDStr = rotationD.getText();
        rotationILimitStr = rotationILimit.getText();
        rotationThresholdStr = rotationThreshold.getText();

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
        topPanel.add(toolBx);
        topPanel.add(button1);
        topPanel.add(button2);
        topPanel.add(button3);
        topPanel.add(speedSlider);
        topPanel.add(dropDown);
        topPanel.add(alliance);
        topPanel.add(button4);
        topPanel.add(diagonal);
        topPanel.add(button5);

        midPanel.add(driveLabel);
        midPanel.add(driveP);
        midPanel.add(driveI);
        midPanel.add(driveD);
        midPanel.add(driveILimit);
        midPanel.add(driveThreshold);

        bottomPanel.add(rotationLabel);
        bottomPanel.add(rotationP);
        bottomPanel.add(rotationI);
        bottomPanel.add(rotationD);
        bottomPanel.add(rotationILimit);
        bottomPanel.add(rotationThreshold);


        //Scrolling
        // JScrollPane scrPane = new JScrollPane(panel);
        // frame.add(scrPane);

        topPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 30));
        topPanel.setLayout(new GridLayout(0, 1));
        topPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        midPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 30));
        midPanel.setLayout(new GridLayout(0, 1));
        midPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 30, 10, 30));
        bottomPanel.setLayout(new GridLayout(0, 1));
        bottomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));


        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(frameWidth, frameHeight);
        frame.add(topPanel, BorderLayout.NORTH);
        frame.add(midPanel, BorderLayout.CENTER);
        frame.add(bottomPanel, BorderLayout.PAGE_END);
        frame.setLayout(new FlowLayout(FlowLayout.LEFT));
        frame.setVisible(true);

        // adds grid canvas in the
        mapCanvas.setPreferredSize(new Dimension(canvasWidth, canvasHeight));
        frame.getContentPane().add(mapCanvas);

        


        startFind();
        
    }
    

}