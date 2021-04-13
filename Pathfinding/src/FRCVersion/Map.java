package FRCVersion;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;


// Creates Grid Canvas
public class Map extends JPanel implements MouseListener, MouseMotionListener {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    frcAutoNav frc = new frcAutoNav();

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
        for (int x = 0; x < frc.cellsWidth; x++) {
            for (int y = 0; y < frc.cellsHeight; y++) {

                int value = frc.map[x][y].getType();
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
                g.fillRect(x * frc.CSIZE, y * frc.CSIZE, frc.CSIZE, frc.CSIZE);
                g.setColor(Color.BLACK);
                g.drawRect(x * frc.CSIZE, y * frc.CSIZE, frc.CSIZE, frc.CSIZE);
                
            }
        }
        frcAutoNav.fillArea();

    }

    // Mouse Handlers
    @Override
    public void mouseDragged(MouseEvent e) {
        try {
            int x = e.getX() / frc.CSIZE;
            int y = e.getY() / frc.CSIZE;
            Node current = frc.map[x][y];
            if ((frc.tool == 1 || frc.tool == 2) && (current.getType() != 0 && current.getType() != 3))
                current.setType(frc.tool);
            frc.updateGrid();
        } catch (Exception z) {
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        try {
            // Gets the X and Y value of where the mouse was clicked according to the screen
            int x = e.getX() / frc.CSIZE;
            int y = e.getY() / frc.CSIZE;
            Node current = frc.map[x][y];
            System.out.println("Co-ordinates:" + x + ", " + y);
            switch (frc.tool) {
                case 0: { // START NODE
                    //if not a wall
                    if (current.getType() != 1) { 
                        //if start exist sets it to blank
                        if (frc.startx > -1 && frc.starty > -1) {
                            frc.map[frc.startx][frc.starty].setType(2);
                            frc.map[frc.startx][frc.starty].setHops(-1); // -1 reperesent the start node location
                        }
                        current.setHops(0);
                        frc.startx = x;
                        frc.starty = y;
                        // sets the clicked box to become the START Node
                        current.setType(0);
                    }
                    break;
                }
                case 3: {// FINISH NODE
                    //if not a wall
                    if (current.getType() != 1) { 
                        // if fininsh exists set it to empty
                        if (frc.finishx > -1 && frc.finishy > -1) 
                            frc.map[frc.finishx][frc.finishy].setType(2);
                        frc.finishx = x; 
                        frc.finishy = y;
                        current.setType(3); 
                        //sets clicked node to be red
                    }
                    break;
                }
                default:
                    if (current.getType() != 0 && current.getType() != 1)
                        current.setType(frc.tool);
                    break;
            }
            frc.updateGrid();
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