import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.w3c.dom.Node;
import java.awt.*;
import java.awt.event.*;
import java.util.Hashtable;


public class App implements ActionListener, ChangeListener {

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
    private int startx = -1;
    private int starty = -1;
    private int finishx = -1;
    private int finishy = -1;
    private int tool = 0;
    private int algo = 0;
    private String[] tools = { "Start", "Finish", "Wall", "Eraser" };
    private String[] algoPicker = { "A*", "Dijkstra" };

    // intitalizing
    public static Map mapCanvas;
    JFrame frame;
    JPanel panel;
    JOptionPane popup;
    Hashtable<Integer, JLabel> labels;
    JSlider speedSlider;
    static Node[][] map;

    App() {
        System.out.println("________Starting_______");
        frame = new JFrame("Java Pathfinding");
        panel = new JPanel();
        popup = new JOptionPane();

        //Buttons
        JButton button1 = new JButton("Start");
        JButton button2 = new JButton("Reset");
        JButton button3 = new JButton("Generate Map");
        JButton button4 = new JButton("Clear Map");
        JButton button5 = new JButton("Credits");
        //Slider
        speedSlider = new JSlider(JSlider.HORIZONTAL, minSpdSlider, maxSpdSlider, initSpdSlider);
        labels = new Hashtable<>();
        mapCanvas = new Map();

        // Action listners For Buttons
        button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Generate New Map
                Map.newMap();
                System.out.println("Generated New Map");
            }
        });

        button5.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Credits pop up message
                JOptionPane.showMessageDialog(frame,
                        "                        Pathfinding Project \n                       Mohammad Awwad",
                        "Credits", JOptionPane.PLAIN_MESSAGE);

            }
        });

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
    public static class Map extends JPanel implements MouseListener, MouseMotionListener {

        public Map() {
            addMouseListener(this);
            addMouseMotionListener(this);
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g); // paint parent's background
            setBackground(Color.white); // set background color for this JPanel

            // Creates Grid of boxes
            for (int x = 0; x < cells; x++) { 
                // goes through loop to create boxes on
                for (int y = 0; y < cells; y++) {

                    //Generates Wall if Number is under 3
                    int randomNum = (int) (Math.random() * 10); // 0 to 9
                    if (randomNum < 3) {
                        g.setColor(Color.BLACK);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    //Draws and Colours the boxes
                    g.fillRect(x * CSIZE, y * CSIZE, CSIZE, CSIZE);
                    g.setColor(Color.BLACK); 
                    g.drawRect(x * CSIZE, y * CSIZE, CSIZE, CSIZE);
                }

            }
        }

        //Updates Canvas
        public void update() { 
            CSIZE = MSIZE / cells;
            mapCanvas.repaint();
        }

        //Generates New Map
        public static void newMap() {
            CSIZE = MSIZE / cells;
            mapCanvas.repaint();
        }


        //Mouse Handlers
        @Override
        public void mouseDragged(MouseEvent e) {
            int x = e.getX() / CSIZE;
            int y = e.getY() / CSIZE;
            // Node current = map[x][y];
            System.out.println("Mouse Dragged To: " + x + "," + y);
            // update();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            try {
                // GET THE X AND Y OF THE MOUSE CLICK IN RELATION TO THE SIZE OF THE GRID
                int x = e.getX() / CSIZE; 
                int y = e.getY() / CSIZE;
                // Dijkstra.Node current = map[x][y];
                System.out.println("Mouse Clicked: " + x + "," + y);
                // update();
            } catch (Exception z) {
            } // EXCEPTION HANDLER
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


 

 



    //For slider use
    @Override
    public void stateChanged(ChangeEvent e) {
        // Get Value of Slider
        JSlider slider = (JSlider)e.getSource();
        if (!slider.getValueIsAdjusting()) {
                System.out.println("Slider Pos: " + slider.getValue());
        }
    }


    @Override
    public void actionPerformed(ActionEvent e) {}


	public static void main(String[] args) {
        new App();
    }



}


    
