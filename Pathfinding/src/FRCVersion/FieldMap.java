package FRCVersion;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
//Note: as long as second number is for angle is lower than the first it will work?????


// Creates Grid Canvas
public class FieldMap extends JPanel implements MouseListener, MouseMotionListener {

    double theta = FrcApp.theta;
    double robotAngleTheta;
    private static List<Double> realPathTheta = new ArrayList<Double>();
    private static List<Integer> xThenYList = new ArrayList<Integer>();

    public FieldMap() {
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
            if(FrcApp.fieldState == 0){
                image = ImageIO.read(new File("C:/Users/Mohammad/Documents/GitHub/Java-Pathfinding/Pathfinding/src/imgs/2022_field.png"));
                g.drawImage(image, 0, 0, null);
            }

            if(FrcApp.fieldState == 1){
                image = ImageIO.read(new File("C:/Users/Mohammad/Documents/GitHub/Java-Pathfinding/Pathfinding/src/imgs/2021_field.png"));
                g.drawImage(image, 0, 0, null);
            }

        } catch (IOException e) {e.printStackTrace();}

        // Creates Grid of boxes
        // goes through loop to create boxes on
        for (int x = 0; x < FrcApp.cellsWidth; x++) {
            for (int y = 0; y < FrcApp.cellsHeight; y++) {
                
                int value = FrcApp.map[x][y]/*[angle]*/.getType();
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
                    case 6:
                        //White for bounds
                        g.setColor(new Color(89, 89, 89, 80));
                        break;
                    default:
                        //White
                        g.setColor(new Color(0, 0, 0, 80));
                }
                
                // Draws and Colours the boxes
                g.fillRect(x * FrcApp.CSIZE, y * FrcApp.CSIZE, FrcApp.CSIZE, FrcApp.CSIZE);
                g.setColor(Color.BLACK);
                g.drawRect(x * FrcApp.CSIZE, y * FrcApp.CSIZE, FrcApp.CSIZE, FrcApp.CSIZE);
                
            }
        }
        fillArea();

    }

    // Mouse Handlers
    @Override
    public void mouseDragged(MouseEvent e) {
        try {
            int x = e.getX() / FrcApp.CSIZE;
            int y = e.getY() / FrcApp.CSIZE;
            Node current = FrcApp.map[x][y]/*[robotAngleTheta]*/;
            if ((FrcApp.tool == 1 || FrcApp.tool == 2) && (current.getType() != 0 && current.getType() != 3))
                current.setType(FrcApp.tool);
            FrcApp.updateGrid();
        } catch (Exception z) {
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        try {
            // Gets the X and Y value of where the mouse was clicked according to the screen
            int x = e.getX() / FrcApp.CSIZE;
            int y = e.getY() / FrcApp.CSIZE;
            double angle = robotAngleTheta;
            Node current = FrcApp.map[x][y]/*[0]*/;
            System.out.println("Co-ordinates:" + x + ", " + y + ", " + angle);
            switch (FrcApp.tool) {
                case 0:  // START NODE
                    //if not a wall
                    System.out.println("Start Error: " + current.getType());
                    if (current.getType() != 1) { 
                        //if start exist sets it to blank
                        if (FrcApp.startx > -1 && FrcApp.starty > -1) {
                            FrcApp.map[FrcApp.startx][FrcApp.starty]/*[angle]*/.setType(2);
                            FrcApp.map[FrcApp.startx][FrcApp.starty]/*[angle]*/.setHops(-1); // -1 reperesent the start node location
                            
                            //remove theta and xtheny
                            realPathTheta.remove(0);
                            xThenYList.remove(0);
                        }
                        current.setHops(0);
                        FrcApp.startx = x;
                        FrcApp.starty = y;
                        // sets the clicked box to become the START Node
                        current.setType(0);
                        //Prompts for Robot Angle
                        promptAngle();
                    }
                    break;
                
                case 3: // FINISH NODE
                    //if not a wall
                    System.out.println("Finish Error: " + current.getType());
                    if (current.getType() != 1) { 
                        // if fininsh exists set it to empty
                        if (FrcApp.finishx > -1 && FrcApp.finishy > -1){
                            FrcApp.map[FrcApp.finishx][FrcApp.finishy]/*[(int) theta]*/.setType(2);

                            //remove theta and xtheny
                            realPathTheta.remove(realPathTheta.size() - 1);
                            xThenYList.remove(xThenYList.size() - 1);
                        }
                        FrcApp.finishx = x; 
                        FrcApp.finishy = y;
                        //sets clicked node to be red
                        current.setType(3); 
                        //Prompts for Robot Angle
                        promptAngle();
                    }
                    break;
                
                default:
                    if (current.getType() != 0 && current.getType() != 1)
                        current.setType(FrcApp.tool);
                    break;
            }
            FrcApp.updateGrid();
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

    
    //Asks for Robot Angle and movement type 
    public void promptAngle(){
        String msg = "Enter Robot Angle"; 
        Object[] msgContent = {FrcApp.xThenY, FrcApp.yThenX, FrcApp.xAndY, msg}; 
        String robotAngle =  JOptionPane.showInputDialog( FrcApp.frame,  msgContent,  "Settings", JOptionPane.PLAIN_MESSAGE); 
        FrcApp.theta = Double.parseDouble(robotAngle);
        robotAngleTheta = Double.parseDouble(robotAngle);
        realPathTheta.add(robotAngleTheta); 
        xThenYList.add(FrcApp.returnXYBool());
        System.out.println("Robot Angle: " + realPathTheta);
        System.out.println("xySelected " + xThenYList);

    }

    

    public List<Double> realThetaCords(){
        return realPathTheta;
    }
    public List<Integer> xThenYDetails(){
        return xThenYList;
    }
    public Integer retutnXYSelected(int intSelected){
        if(intSelected == 1){
            return 1;
        }
        else if(intSelected == 2){
            return 2;
        }
        else{
            return 3;
        }
    }

    public static void fillArea(){
        //Multi dimentional arrays 
        //blocked areas that does not depend on your alliance color
        int[][] blocked;

        switch(FrcApp.fieldState){
            //for 2022 field
            case 0: 
                int[][] blocked2022 = {
                    //bottom wall
                    {31, 88}, {32, 88}, {33, 88}, {34, 88}, {35, 88}, {36, 88}, {37, 88}, {38, 88}, {39, 88}, {40, 88}, {41, 88}, {42, 88}, {43, 88}, {44, 88}, {45, 88}, {46, 88}, {47, 88}, {48, 88}, {49, 88}, {50, 88}, {51, 88}, {52, 88}, {53, 88}, {54, 88}, {55, 88}, {56, 88}, {57, 88}, {58, 88}, {59, 88}, {60, 88}, {61, 88}, {62, 88}, {63, 88}, {64, 88}, {65, 88}, {66, 88}, {67, 88}, {68, 88}, {69, 88}, {70, 88}, {71, 88}, {72, 88}, {73, 88}, {74, 88}, {75, 88}, {76, 88}, {77, 88}, {78, 88}, {79, 88}, {80, 88}, {81, 88}, {82, 88}, {83, 88}, {84, 88}, {85, 88}, {86, 88}, {87, 88}, {88, 88}, {89, 88}, {90, 88}, {91, 88}, {92, 88}, {93, 88}, {94, 88}, {95, 88}, {96, 88}, {97, 88}, {98, 88}, {99, 88}, {100, 88}, {101, 88}, {102, 88}, {103, 88}, {104, 88}, {105, 88}, {106, 88}, {107, 88}, {108, 88}, {109, 88}, {110, 88}, {111, 88}, {112, 88}, {113, 88}, {114, 88}, {115, 88}, {116, 88}, {117, 88}, {118, 88}, {119, 88}, {120, 88}, {121, 88}, {122, 88}, {123, 88}, {124, 88}, {125, 88}, {126, 88}, {127, 88}, {128, 88}, {129, 88}, {130, 88}, {131, 88}, {132, 88}, {133, 88}, {134, 88}, {135, 88}, {136, 88}, {137, 88}, {138, 88}, {139, 88}, {140, 88}, {141, 88}, {142, 88}, {143, 88}, {144, 88}, {145, 88}, {146, 88}, {147, 88}, {148, 88}, {149, 88}, {150, 88}, {151, 88}, {152, 88}, {153, 88}, {154, 88}, {155, 88}, {156, 88}, {157, 88}, {158, 88}, {159, 88}, {160, 88}, {161, 88}, {162, 88}, {163, 88}, {164, 88}, {165, 88}, {166, 88}, {167, 88}, {168, 88}, {169, 88}, {170, 88}, {171, 88}, {172, 88}, {173, 88}, {174, 88}, {175, 88}, {176, 88}, {177, 88}, {178, 88},

                    //top wall
                    {14, 3}, {15, 3}, {16, 3}, {17, 3}, {18, 3}, {19, 3}, {20, 3}, {21, 3}, {22, 3}, {23, 3}, {24, 3}, {25, 3}, {26, 3}, {27, 3}, {28, 3}, {29, 3}, {30, 3}, {31, 3}, {32, 3}, {33, 3}, {34, 3}, {35, 3}, {36, 3}, {37, 3}, {38, 3}, {39, 3}, {40, 3}, {41, 3}, {42, 3}, {43, 3}, {44, 3}, {45, 3}, {46, 3}, {47, 3}, {48, 3}, {49, 3}, {50, 3}, {51, 3}, {52, 3}, {53, 3}, {54, 3}, {55, 3}, {56, 3}, {57, 3}, {58, 3}, {59, 3}, {60, 3}, {61, 3}, {62, 3}, {63, 3}, {64, 3}, {65, 3}, {66, 3}, {67, 3}, {68, 3}, {69, 3}, {70, 3}, {71, 3}, {72, 3}, {73, 3}, {74, 3}, {75, 3}, {76, 3}, {77, 3}, {78, 3}, {79, 3}, {80, 3}, {81, 3}, {82, 3}, {83, 3}, {84, 3}, {85, 3}, {86, 3}, {87, 3}, {88, 3}, {89, 3}, {90, 3}, {91, 3}, {92, 3}, {93, 3}, {94, 3}, {95, 3}, {96, 3}, {97, 3}, {98, 3}, {99, 3}, {100, 3}, {101, 3}, {102, 3}, {103, 3}, {104, 3}, {105, 3}, {106, 3}, {107, 3}, {108, 3}, {109, 3}, {110, 3}, {111, 3}, {112, 3}, {113, 3}, {114, 3}, {115, 3}, {116, 3}, {117, 3}, {118, 3}, {119, 3}, {120, 3}, {121, 3}, {122, 3}, {123, 3}, {124, 3}, {125, 3}, {126, 3}, {127, 3}, {128, 3}, {129, 3}, {130, 3}, {131, 3}, {132, 3}, {133, 3}, {134, 3}, {135, 3}, {136, 3}, {137, 3}, {138, 3}, {139, 3}, {140, 3}, {141, 3}, {142, 3}, {143, 3}, {144, 3}, {145, 3}, {146, 3}, {147, 3}, {148, 3}, {149, 3}, {150, 3}, {151, 3}, {152, 3}, {153, 3}, {154, 3}, {155, 3}, {156, 3}, {157, 3}, {158, 3}, {159, 3}, 

                    //right wall
                    {178, 23}, {178, 24}, {178, 25}, {178, 26}, {178, 27}, {178, 28}, {178, 29}, {178, 30}, {178, 31}, {178, 32}, {178, 33}, {178, 34}, {178, 35}, {178, 36}, {178, 37}, {178, 38}, {178, 39}, {178, 40}, {178, 41}, {178, 42}, {178, 43}, {178, 44}, {178, 45}, {178, 46}, {178, 47}, {178, 48}, {178, 49}, {178, 50}, {178, 51}, {178, 52}, {178, 53}, {178, 54}, {178, 55}, {178, 56}, {178, 57}, {178, 58}, {178, 59}, {178, 60}, {178, 61}, {178, 62}, {178, 63}, {178, 64}, {178, 65}, {178, 66}, {178, 67}, {178, 68}, {178, 69}, {178, 70}, {178, 71}, {178, 72}, {178, 73}, {178, 74}, {178, 75}, {178, 76}, {178, 77}, {178, 78}, {178, 79}, {178, 80}, {178, 81}, {178, 82}, {178, 83}, {178, 84}, {178, 85}, {178, 86}, {178, 87},
                
                    //left wall
                    {13, 3}, {13, 4}, {13, 5}, {13, 6}, {13, 7}, {13, 8}, {13, 9}, {13, 10}, {13, 11}, {13, 12}, {13, 13}, {13, 14}, {13, 15}, {13, 16}, {13, 17}, {13, 18}, {13, 19}, {13, 20}, {13, 21}, {13, 22}, {13, 23}, {13, 24}, {13, 25}, {13, 26}, {13, 27}, {13, 28}, {13, 29}, {13, 30}, {13, 31}, {13, 32}, {13, 33}, {13, 34}, {13, 35}, {13, 36}, {13, 37}, {13, 38}, {13, 39}, {13, 40}, {13, 41}, {13, 42}, {13, 43}, {13, 44}, {13, 45}, {13, 46}, {13, 47}, {13, 48}, {13, 49}, {13, 50}, {13, 51}, {13, 52}, {13, 53}, {13, 54}, {13, 55}, {13, 56}, {13, 57}, {13, 58}, {13, 59}, {13, 60}, {13, 61}, {13, 62}, {13, 63}, {13, 64}, {13, 65}, {13, 66}, {13, 67}, {13, 68}, {13, 69}, 
               
                    //bottom left corner
                    {13, 70}, {14, 71}, {15, 72}, {16, 73}, {17, 74}, {18, 75}, {19, 76}, {20, 77}, {21, 78}, {22, 79}, {23, 80}, {24, 81}, {25, 82}, {26, 83}, {27, 84}, {28, 85}, {29, 86}, {30, 87},
                    {14, 70}, {15, 71}, {16, 72}, {17, 73}, {18, 74}, {19, 75}, {20, 76}, {21, 77}, {22, 78}, {23, 79}, {24, 80}, {25, 81}, {26, 82}, {27, 83}, {28, 84}, {29, 85}, {30, 86}, {31, 87},

                    //top right corner
                    {160, 3}, {161, 4}, {162, 5}, {163, 6}, {164, 7}, {165, 8}, {166, 9}, {167, 10}, {168, 11}, {169, 12}, {170, 13}, {171, 14}, {172, 15}, {173, 16}, {174, 17}, {175, 18}, {176, 19}, {177, 20}, {178, 21}, 
                    {160, 4}, {161, 5}, {162, 6}, {163, 7}, {164, 8}, {165, 9}, {166, 10}, {167, 11}, {168, 12}, {169, 13}, {170, 14}, {171, 15}, {172, 16}, {173, 17}, {174, 18}, {175, 19}, {176, 20}, {177, 21}, {178, 22}, 

                    //top half line
                    {77, 4}, {78, 4}, {78, 5}, {78, 6}, {79, 6}, {79, 7}, {79, 8}, {79, 9}, {80, 9}, {80, 10}, {80, 11}, {81, 11}, {81, 12}, {81, 13}, {82, 13}, {82, 14}, {82, 15}, {83, 15}, {83, 16}, {83, 17}, {83, 18}, {84, 18}, {84, 19}, {84, 20}, {85, 20}, {85, 21}, {85, 22}, {86, 22}, {86, 23}, {86, 24}, {87, 24}, {87, 25}, {87, 26}, {87, 27}, {88, 27}, {88, 28}, {88, 29}, {89, 29}, 
               
                    //bottom half line
                    {103, 62}, {103, 63}, {104, 63}, {104, 64}, {104, 65}, {104, 66}, {105, 66}, {105, 67}, {105, 68}, {106, 68}, {106, 69}, {106, 70}, {107, 70}, {107, 71}, {107, 72}, {107, 73}, {108, 73}, {108, 74}, {108, 75}, {109, 75}, {109, 76}, {109, 77}, {110, 77}, {110, 78}, {110, 79}, {111, 79}, {111, 80}, {111, 81}, {111, 82}, {112, 82}, {112, 83}, {112, 84}, {113, 84}, {113, 85}, {113, 86}, {114, 86}, {114, 87},
                
                    // bottom right blue post
                    {44, 30}, {45,30 }, {45, 31}, {45, 32}, {45, 33}, {44, 33},{43, 33},{43, 32},{44, 32}, {44, 31}, {43, 31}, {43, 30},
                
                    //top right blue post
                    {45, 9}, {44, 9}, {43, 9}, {45, 8}, {44, 8}, {43, 8}, {45, 7}, {44, 7}, {43, 7}, {45, 6}, {44, 6}, {43, 6},

                    //bottom left blue post
                    {18, 33}, {17, 33}, {16, 33}, {18, 32}, {17, 32}, {16, 32}, {18, 31}, {17, 31}, {16, 31}, {18, 30}, {17, 30}, {16, 30},
               
                    //top left blue post
                    {18, 9}, {17, 9}, {16, 9}, {18, 8}, {17, 8}, {16, 8}, {18, 7}, {17, 7}, {16, 7}, {18, 6}, {17, 6}, {16, 6},

                    //top left red post 
                    {146, 61}, {147, 61}, {148, 61}, {146, 60}, {147, 60}, {148, 60}, {146, 59}, {147, 59}, {148, 59}, {146, 58}, {147, 58}, {148, 58},
                
                    //top right red post  
                    {173, 58}, {174, 58}, {175, 58}, {173, 59}, {174, 59}, {175, 59}, {173, 60}, {174, 60}, {175, 60}, {173, 61}, {174, 61}, {175, 61},
                
                    //bottom left red post 
                    {146, 83}, {147, 83}, {148, 83}, {146, 84}, {147, 84}, {148, 84}, {146, 85}, {147, 85}, {148, 85}, {146, 86}, {147, 86}, {148, 86},
                
                    //bottom right red post 
                    {173, 83}, {174, 83}, {175, 83}, {173, 84}, {174, 84}, {175, 84}, {173, 85}, {174, 85}, {175, 85}, {173, 86}, {174, 86}, {175, 86}, 
                
                    //red side of hub top half
                    {89, 30}, {90, 30}, {91, 30}, {91, 29}, {92, 29}, {92, 30}, {92, 31}, {91, 31}, {91, 32}, {91, 33}, {92, 33}, {92, 34}, {92, 35}, {93, 35}, {93, 36}, {94, 36}, {95, 36}, {95, 37}, {96, 37}, {97, 37}, {97, 38}, {98, 38}, {99, 38}, {100, 38}, {100, 39}, {101, 39}, {102, 39}, {103, 39}, {103, 40}, {104, 40}, {105, 40}, {105, 39}, {106, 39}, {107, 39}, {107, 38}, {108, 38}, {109, 38}, {109, 37}, {109, 36}, {110, 36}, {110, 37}, {110, 38}, {111, 38}, {111, 39}, {111, 40}, {111, 41}, {112, 41}, {112, 40}, {112, 42}, {111, 41}, {110, 41}, {109, 41}, {109, 42}, {108, 42}, {107, 42}, {106, 42}, {106, 43},
                
                    
                    //red side of hub bottom half
                    {106, 44}, {106, 45}, {105, 45}, {105, 46}, {105, 47}, {105, 48}, {104, 48}, {104, 49}, {104, 50}, {103, 51}, {103, 52}, {102, 52}, {102, 54}, {102, 55}, {102, 56}, {103, 56}, {103, 57}, {103, 58}, {103, 59}, {104, 59}, {105, 59}, {105, 60}, {106, 60}, {104, 61}, {104, 60}, {103, 61}, {102, 53},   
                
                
                };


                blocked = null;
                blocked = blocked2022;
                bounds(blocked, 6);
                block(blocked, 1);

            break;

            //for 2021 field
            case 1: 
                int[][] blocked2021 = {
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
                    //Top Wall 
                    {23,4}, {24,4}, {25,4}, {26,4}, {27,4}, {28,4}, {29,4}, {30,4}, {31,4}, {32,4}, {33,4}, {34,4}, {35,4}, {36,4}, {37,4}, {38,4}, {39,4}, {40,4}, {41,4}, {42,4}, {43,4}, {44,4}, {45,4}, {46,4}, {47,4}, {48,4}, {49,4}, {50,4}, {51,4}, {52,4}, {53,4}, {54,4}, {55,4}, {56,4}, {57,4}, {58,4}, {59,4}, {60,4}, {61,4}, {62,4}, {63,4}, {64,4}, {65,4}, {66,4}, {67,4}, {68,4}, {69,4}, {70,4}, {71,4}, {72,4}, {73,4}, {74,4}, {75,4}, {76,4}, {77,4}, {78,4}, {79,4}, {80,4}, {81,4}, {82,4}, {83,4}, {84,4}, {85,4}, {86,4}, {87,4}, {88,4}, {89,4}, {90,4}, {91,4}, {92,4}, {93,4}, {94,4}, {95,4}, {96,4}, {97,4}, {98,4}, {99,4}, {100,4}, {101,4}, {102,4}, {103,4}, {104,4}, {105,4}, {106,4}, {107,4}, {108,4}, {109,4}, {110,4}, {111,4}, {112,4}, {113,4}, {114,4}, {115,4}, {116,4}, {117,4}, {118,4}, {119,4}, {120,4}, {121,4}, {122,4}, {123,4}, {124,4}, {125,4}, {126,4}, {127,4}, {128,4}, {129,4}, {130,4}, {131,4}, {132,4}, {133,4}, {134,4}, {135,4}, {136,4}, {137,4}, {138,4}, {139,4}, {140,4}, {141,4}, {142,4}, {143,4}, {144,4}, {145,4}, {146,4}, {147,4}, {148,4}, {149,4}, {150,4}, {151,4}, {152,4}, {153,4}, {154,4}, {155,4}, {156,4}, {157,4}, {158,4}, {159,4}, {160,4}, {161,4}, {162,4}, {163,4}, {164,4}, {165,4}, {166,4}, {167,4}, {168,4}, {169,4},
                    //Bottom Wall
                    {23,87}, {24,87}, {25,87}, {26,87}, {27,87}, {28,87}, {29,87}, {30,87}, {31,87}, {32,87}, {33,87}, {34,87}, {35,87}, {36,87}, {37,87}, {38,87}, {39,87}, {40,87}, {41,87}, {42,87}, {43,87}, {44,87}, {45,87}, {46,87}, {47,87}, {48,87}, {49,87}, {50,87}, {51,87}, {52,87}, {53,87}, {54,87}, {55,87}, {56,87}, {57,87}, {58,87}, {59,87}, {60,87}, {61,87}, {62,87}, {63,87}, {64,87}, {65,87}, {66,87}, {67,87}, {68,87}, {69,87}, {70,87}, {71,87}, {72,87}, {73,87}, {74,87}, {75,87}, {76,87}, {77,87}, {78,87}, {79,87}, {80,87}, {81,87}, {82,87}, {83,87}, {84,87}, {85,87}, {86,87}, {87,87}, {88,87}, {89,87}, {90,87}, {91,87}, {92,87}, {93,87}, {94,87}, {95,87}, {96,87}, {97,87}, {98,87}, {99,87}, {100,87}, {101,87}, {102,87}, {103,87}, {104,87}, {105,87}, {106,87}, {107,87}, {108,87}, {109,87}, {110,87}, {111,87}, {112,87}, {113,87}, {114,87}, {115,87}, {116,87}, {117,87}, {118,87}, {119,87}, {120,87}, {121,87}, {122,87}, {123,87}, {124,87}, {125,87}, {126,87}, {127,87}, {128,87}, {129,87}, {130,87}, {131,87}, {132,87}, {133,87}, {134,87}, {135,87}, {136,87}, {137,87}, {138,87}, {139,87}, {140,87}, {141,87}, {142,87}, {143,87}, {144,87}, {145,87}, {146,87}, {147,87}, {148,87}, {149,87}, {150,87}, {151,87}, {152,87}, {153,87}, {154,87}, {155,87}, {156,87}, {157,87}, {158,87}, {159,87}, {160,87}, {161,87}, {162,87}, {163,87}, {164,87}, {165,87}, {166,87}, {167,87}, {168,87}, {169,87},
                    //Left Wall
                    {22,4}, {22,5}, {22,6}, {21,6}, {21,7}, {21,8}, {21,9}, {20,9}, {20,10}, {20,11}, {20,12}, {19,12}, {19,13}, {19,14}, {19,15}, {18,15}, {18,16}, {18,17}, {17,17}, {17,18}, {17,19}, {17,20}, {16,20}, {16,21}, {16,23}, {16,22}, {16,24}, {16,25}, {16,26}, {16,27}, {16,28}, {16,29}, {16,30}, {16,31}, {16,32}, {16,33}, {16,34}, {16,35}, {16,36}, {16,37}, {16,38}, {16,39}, {16,40}, {16,41}, {16,42}, {16,43}, {16,44}, {16,45}, {16,46}, {16,47}, {16,48}, {16,49}, {16,50}, {16,51}, {16,52}, {16,53}, {16,54}, {16,55}, {16,56}, {16,57}, {16,58}, {16,59}, {16,60}, {16,61}, {16,62}, {16,63}, {16,64}, {16,65}, {16,66}, {16,67}, {16,68}, {22,87}, {22,86}, {22,85}, {21,85}, {21,84}, {21,83}, {21,82}, {20,81}, {20,82}, {20,80}, {20,79}, {19,79}, {19,78}, {19,77}, {19,76}, {18,76}, {18,75}, {18,74}, {18,73}, {17,73}, {17,72}, {17,71}, {16,71}, {16,70}, {16,69}, 
                    //Right Wall
                    {170,4}, {170,5}, {170,6}, {171,6}, {171,7}, {171,8}, {171,9}, {172,9}, {172,10}, {172,11}, {172,12}, {173,12}, {173,13}, {173,14}, {173,15}, {174,15}, {174,16}, {174,17}, {175,17}, {175,18}, {175,19}, {176,19}, {176,20}, {176,20}, {176,21}, {176,22}, {176,23}, {176,24}, {176,25}, {176,26}, {176,27}, {176,28}, {176,29}, {176,30}, {176,31}, {176,32}, {176,33}, {176,34}, {176,35}, {176,36}, {176,37}, {176,38}, {176,39}, {176,40}, {176,41}, {176,42}, {176,43}, {176,44}, {176,45}, {176,46}, {176,47}, {176,48}, {176,49}, {176,50}, {176,51}, {176,52}, {176,53}, {176,54}, {176,55}, {176,56}, {176,57},  {176,58}, {176,59}, {176,60}, {176,61}, {176,62}, {176,63}, {176,64}, {176,65}, {176,66}, {176,67}, {176,68}, {176,69}, {176,70}, {176,71}, {175,71}, {175,71}, {175,72}, {175,73}, {175,74}, {174,74}, {174,75}, {174,76}, {174,77}, {173,77}, {173,78}, {173,79}, {173,80}, {172,80}, {172,81}, {172,82}, {172,83}, {171,83}, {171,84}, {171,85}, {170,85}, {170,86}, {170,87}, 
                };

                blocked= null;
                blocked = blocked2021;
                bounds(blocked, 6);
                block(blocked, 1);
                
            break;
        }




        switch(FrcApp.allianceState){
            //Blue Aliance
            case 0:
                //blocked arreas based on your alliance color and fieldState

                switch(FrcApp.fieldState){

                    case 0: 
                        int[][] blockRed2022 = { 

                        };
                        //loops through array
                        bounds(blockRed2022, 6);
                        block(blockRed2022, 1);
                    break;


                    case 1: 
                        int[][] blockRed2021 = {
                            //Red Alliance Scoring Zone
                            {17,23}, {18,24}, {19,25}, {20,26}, {21,27}, {22,27}, {23,28}, {22,29}, {21,30}, {20,31}, {19,32}, {18,33}, {16,35}, {23,29}, {22,30}, {21,31}, {20,32}, {19,33}, {18,24}, {17,34}, {18,34}, {16,35}, {17,35}, {22,28}, {21,26}, {20,25}, {19,24}, {18,23},
                            //Red Alliance Trench Zone
                            {69, 4}, {69,5}, {69,6}, {69,7}, {69,8}, {69,9}, {69,10} ,{69,11}, {69,12}, {69,13}, {69,14}, {69,15}, {69,16}, {69,17}, {69,18},
                            {70,18}, {71,18}, {72,18}, {73,18}, {74,18}, {75,18}, {76,18}, {77,18}, {78,18}, {79,18}, {80,18},{81,18},{82,18}, {83,18}, {84,18}, {85,18}, {86,18}, {87,18},{88,18},{89,18},{90,18},{91,18},{92,18}, {93,18}, {94,18},{95,18},{96,18},{97,18},{98,18},{99,18},{100,18},{101,18},{102,18},{113,18}, {114,18},{115,18}, {116,18},{117,18},{118,18},{119,18},{120,18},{121,18},{122,18},
                            {123, 4}, {123,5}, {123,6}, {123,7}, {123,8}, {123,9}, {123,10} ,{123,11}, {123,12}, {123,13}, {123,14}, {123,15}, {123,16}, {123,17}, {123,18},
                            //Red Alliance Loading Zone
                            {176,22}, {176,23}, {175,24}, {174,25}, {173,25}, {173,26}, {172,26}, {172,27}, {171,27}, {171,28}, {170,28}, {170,29}, {169,29},  {174,24}, {175,23}, {169,30}, {170,30}, {170,31}, {171,31}, {171,32}, {172,32}, {172,33}, {173,33}, {173,34}, {174,34}, {174,35}, {175,35}, {175,36}, {176,36}, {176,37}, 
                        };
                        //loops through array
                        bounds(blockRed2021, 6);
                        block(blockRed2021, 1);
                    break;
                }


            break;

            //Red Alliance
            case 1:


                switch(FrcApp.fieldState){

                    case 0: 
                        int[][] blockBlue2022 = { 

                        };
                        //loops through array
                        bounds(blockBlue2022, 6);
                        block(blockBlue2022, 1);
                    break;


                    case 1: 
                        int[][] blockBlue2021 = {
                            //Blue Alliance Scoring Zone
                            {176,57}, {175,57}, {175,58}, {174,58}, {174,59}, {173,59}, {173,60}, {172,60}, {171,61}, {170,61}, {170,62}, {169,62}, {169,63}, {170,63}, {170,64}, {171,64}, {172,65}, {173,66}, {174,67}, {175,67}, {176,68}, {171,60}, {171,65}, {172,66}, {175,68}, {174,66},
                            //Blue Alliance Trench Zone
                            {69, 73}, {69,74}, {69,75}, {69,76}, {69,77}, {69,78}, {69,79}, {69,80} ,{69,81}, {69,82}, {69,83}, {69,84}, {69,85}, {69,86}, {69,87},
                            {70,73}, {71,73}, {72,73}, {73,73}, {74,73}, {75,73}, {76,73}, {77,73}, {78,73}, {79,73},{91,73},{92,73}, {93,73}, {94,73},{95,73},{96,73},{97,73},{98,73},{99,73},{100,73},{101,73},{102,73},{103,73},{104,73},{105,73},{106,73},{107,73},{108,73},{109,73},{110,73},{111,73}, {112,73}, {113,73}, {114,73},{115,73}, {116,73},{117,73},{118,73},{119,73},{120,73},{121,73},{122,73},
                            {123, 73}, {123,74}, {123,75}, {123,76}, {123,77}, {123,78}, {123,79}, {123,80} ,{123,81}, {123,82}, {123,83}, {123,84}, {123,85}, {123,86}, {123,87},
                            //Blue Alliance Loading Zone
                            {16,68}, {17,68}, {17,67}, {18,67}, {18,66}, {19,66}, {19,65}, {20,65}, {20,64}, {21,63}, {21,64}, {22,63}, {22,62}, {23,62}, {23,61}, {23,60}, {22,60}, {22,59}, {21,59}, {21,58}, {20,58}, {20,57}, {19,57}, {19,56}, {18,56}, {18,55}, {17,55}, {17,54}, {16,54},
                        };
        
                        //loops through array
                        bounds(blockBlue2021, 6);
                        block(blockBlue2021, 1);
                    break;
                }
            break; 
        }
    }

    public static void block(int[][] array, int color){
        int x;
        int y;
        for(int i = 0; array.length > i; i++){
            x = array[i][0];
            y = array[i][1]; 
            Node current = FrcApp.map[x][y]/*[0]*/;
            current.setType(color);//1 for black
        }
    }

    public static void bounds(int[][] array, int color){
        int x;
        int y;
        for(int i = 0; array.length > i; i++){
            x = array[i][0];
            y = array[i][1];
            for(int counter = 0; counter <= 2; counter++){
                y++;
                Node current = FrcApp.map[x][y]/*[0]*/;
                current.setType(color);//6 for grey
            }
        }
        for(int i = 0; array.length > i; i++){
            x = array[i][0];
            y = array[i][1]; 
            for(int counter = 0; counter <= 2; counter++){
                y--;
                Node current = FrcApp.map[x][y]/*[0]*/;
                current.setType(color);//6 for grey
            }
        }
        for(int i = 0; array.length > i; i++){
            x = array[i][0];
            y = array[i][1]; 
            for(int counter = 0; counter <= 2; counter++){
                x++;
                Node current = FrcApp.map[x][y]/*[0]*/;
                current.setType(color);//6 for grey
            }
        }
        for(int i = 0; array.length > i; i++){
            x = array[i][0];
            y = array[i][1]; 
            for(int counter = 0; counter <= 2; counter++){
                x--;
                Node current = FrcApp.map[x][y]/*[0]*/;
                current.setType(color);//6 for grey
            }
        }
    }
}