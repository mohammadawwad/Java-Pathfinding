package FRCVersion;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JOptionPane;
import javax.swing.text.html.HTMLDocument.Iterator;

public class Algorithms{

    private List<Double> pathX;
    private List<Double> pathY;
    private List<Double> realPathX;
    private List<Double> realPathY;
    private List<Double> tempPathX;
    private List<Double> tempPathY;

    public void Dijkstra() {
        System.out.println("Dijkstra");
        //creates a priority que
        ArrayList<Node> priority = new ArrayList<Node>(); 
        //add the start node first to the que
        priority.add(FrcApp.map[FrcApp.startx][FrcApp.starty]/*[(int) FrcApp.theta]*/); 
        while (FrcApp.start) {
            // if the que == 0 no path exists or cannot be ound
            if (priority.size() <= 0) { 
                FrcApp.start = false;
                System.out.println("Cant Find Path");
                JOptionPane.showMessageDialog(FrcApp.frame, "             No Path Found \n        Map Will be Cleared ");
                FrcApp.staticCleanMap();
                FrcApp.updateGrid();
                break;
            }
            int hops = priority.get(0).getHops() + 1; 
            //array list of explored nodes
            ArrayList<Node> explored = exploreNearby(priority.get(0), hops); 
            if (explored.size() > 0) {
                //remove from que
                priority.remove(0); 
                //add all new nodes
                priority.addAll(explored); 
                try {
                    Thread.sleep(FrcApp.sliderValue);
                    FrcApp.updateGrid();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {	//IF NO NODES WERE EXPLORED THEN JUST REMOVE THE NODE FROM THE QUE
                priority.remove(0);
            }
        }
    }

    //Similar to Dijkstra Just has a sort queing method to search in the direction of the end Finish Node
    public void AStar() {
        ArrayList<Node> priority = new ArrayList<Node>();
        priority.add(FrcApp.map[FrcApp.startx][FrcApp.starty]/*[(int) FrcApp.theta]*/);
        while(FrcApp.start) {
            if(priority.size() <= 0) {
                FrcApp.start = false;
                System.out.println("Cant Find Path");
                JOptionPane.showMessageDialog(FrcApp.frame, "             No Path Found \n        Map Will be Cleared ");
                FrcApp.staticCleanMap();
                FrcApp.updateGrid();
                break;
            }
            int hops = priority.get(0).getHops() + 1;
            ArrayList<Node> explored = exploreNearby(priority.get(0),hops);
            if(explored.size() > 0) {
                priority.remove(0);
                priority.addAll(explored);
                try {
                    Thread.sleep(FrcApp.sliderValue);
                    FrcApp.updateGrid();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                priority.remove(0);
            }
            sortPri(priority);
        }
    }
    

    public ArrayList<Node> sortPri(ArrayList<Node> sort){
        int count = 0;
        while(count < sort.size()){
            int cAdd = count;
            //count++ in for loop causes errors
            for(int x = count + 1; x < sort.size(); x++){
                //compare a euclid distance to a diffrent ecuclid distance by having 2 variable 
                // 1 variable will always be greater than the other giving diffrent distances back
                if(sort.get(x).getEDistance() + sort.get(x).getHops() < sort.get(cAdd).getEDistance() + sort.get(cAdd).getHops()){
                    cAdd = x;
                }
            }

            if(count != cAdd){
                Node temp = sort.get(count);
                sort.set(count, sort.get(cAdd));
                sort.set(cAdd, temp);
            }

            count++;
        }

    return sort;
    }

    public ArrayList<Node> exploreNearby(Node current, int hops) {
        //list of nodes that have been explored
        ArrayList<Node> explored = new ArrayList<Node>();	
        //for loop to check all nodes surrounding the current node
        //setting to -1 since that represents the home node
        System.out.println("Exploring Nearby");
        int xAxis;
        int yAxis;

        if(FrcApp.movement == false){
            //seperate loops to avoid diagonal movements
            for(xAxis = -1; xAxis <= 1; xAxis++){
                int x = current.getX() + xAxis;
                //checks to see if next X bloxk is in the grid
                if(x > -1 && x < FrcApp.cellsWidth){
                    Node nearby = FrcApp.map[x][current.getY()]/*[(int) FrcApp.theta]*/;
                    if((nearby.getHops() == -1 || nearby.getHops() > hops) && (nearby.getType() != 1 && nearby.getType() !=6)){
                        //call the searching method
                        searchNode(nearby, current.getX(), current.getY(), hops);
                        explored.add(nearby);// adding all nearby nodes that arnt walls to list
                    }
                }
            }
            for(yAxis = -1; yAxis <= 1; yAxis++){
                int y = current.getY() + yAxis;
                //checks to see if next Y block is in the Grid
                if(y > -1 && y < FrcApp.cellsHeight){
                    Node nearby = FrcApp.map[current.getX()][y]/*[(int) FrcApp.theta]*/;
                    //check if its not a wall
                    if((nearby.getHops() == -1 || nearby.getHops() > hops) && (nearby.getType() != 1 && nearby.getType() !=6)){
                        //call the searching method
                        searchNode(nearby, current.getX(), current.getY(), hops);
                        explored.add(nearby);// adding all nearby nodes that arnt walls to lis
                    }

                }
            }
        }
        else if(FrcApp.movement == true){
            //ruuning both loops togather allows for diagonal movements
            for(xAxis = -1; xAxis <= 1; xAxis++){
                for(yAxis = -1; yAxis <= 1; yAxis++){
                    int x = current.getX() + xAxis;
                    int y = current.getY() + yAxis;
                    //checks to see if next X bloxk is in the grid
                    if((x > -1 && x < FrcApp.cellsWidth) && (y > -1 && y < FrcApp.cellsHeight)){
                        Node nearby = FrcApp.map[x][y]/*[(int) FrcApp.theta]*/;
                        //check if its not a wall
                        if((nearby.getHops() == -1 || nearby.getHops() > hops) && (nearby.getType() != 1 && nearby.getType() !=6)){
                            //call the searching method
                            searchNode(nearby, current.getX(), current.getY(), hops);
                            explored.add(nearby);// adding all nearby nodes that arnt walls to lis
                        }
                    }
                }
            }
        }
        
        return explored; //returning the list value
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
        FrcApp.check++;
        if(current.getType() == 3){
            //start backtracking
            System.out.println("Finish Node Found");
            backtrack(current.getLastX(), current.getLastY(), /*current.getLastTheta(),*/ hops);
        }
    }

    //Backtracjing so it can draw the correct path
    public void backtrack(int lastX, int lastY, /*int lastTheta,*/ int hops){
        DecimalFormat df = new DecimalFormat("0.0");
        FrcApp.length = hops;
        pathX = new ArrayList<Double>();  
        pathY = new ArrayList<Double>(); 

        while(hops > 1) {	
            Node current = FrcApp.map[lastX][lastY]/*[lastTheta]*/;
            //sets it to the final path
            System.out.println("BackTracking: " + lastX + ", " + lastY);
            pathX.add(Double.parseDouble(df.format((lastX - FrcApp.startx) * 0.1)));  
            pathY.add(Double.parseDouble(df.format((lastY - FrcApp.starty) * 0.1)));  
            current.setType(4);
            lastX = current.getLastX();
            lastY = current.getLastY();
            hops--;
        }
        //Reversing the lists
        pathX.add(0.0);
        pathY.add(0.0);
        pathX.add(0, (FrcApp.finishx - FrcApp.startx) * 0.1);
        pathY.add(0, (FrcApp.finishy - FrcApp.starty) * 0.1);
        Collections.reverse(pathX);
        Collections.reverse(pathY);
        System.out.println("Path X list:" + pathX);
        System.out.println("Path Y list:" + pathY);
        FrcApp.start = false;
        cordMerge();
    }

    public List<Double> xCords(){
        return pathX;
    }

    public List<Double> yCords(){
        return pathY;
    }

    public List<Double> tempXCords(){
        return tempPathX;
    }

    public List<Double> tempYCords(){
        return tempPathY;
    }

    public List<Double> realXCords(){
        return realPathX;
    }

    public List<Double> realYCords(){
        return realPathY;
    }

    //reverse remove List 
    public void rrList(List<Double> list) {
        Collections.reverse(list);
        for(int itr = list.size() - 1; itr >= 1 ; itr--){
            list.remove(itr);
        }
    }


    //sorting algorithm that merges coords
    public void cordMerge(){
        DecimalFormat df = new DecimalFormat("0.0");
        realPathX = new ArrayList<Double>();  
        realPathY = new ArrayList<Double>(); 
        tempPathX = new ArrayList<Double>();  
        tempPathY = new ArrayList<Double>(); 

        for(int x = 0; x < xCords().size(); x++){
            //Checks to see if there is another element in list
            if (xCords().size() > x + 1) {
                System.out.println("Next True");

                //Right Path Mege Algorithm
                if((Double.parseDouble(df.format(xCords().get(x) - xCords().get(x + 1))) == -0.1) && (Double.parseDouble(df.format(yCords().get(x) - yCords().get(x + 1))) == 0.0)){
                    tempPathX.add(Double.parseDouble(df.format(xCords().get(x))));
                    tempPathY.add(Double.parseDouble(df.format(yCords().get(x))));

                    rrList(tempPathX);
                    rrList(tempPathY);

                    realPathX.add(tempPathX.get(0));
                    realPathY.add(tempPathY.get(0));
                }

                //Left Path Mege Algorithm
                if((Double.parseDouble(df.format(xCords().get(x) - xCords().get(x + 1))) == 0.1) && (Double.parseDouble(df.format(yCords().get(x) - yCords().get(x + 1))) == 0.0)){
                    tempPathX.add(Double.parseDouble(df.format(xCords().get(x))));
                    tempPathY.add(Double.parseDouble(df.format(yCords().get(x))));

                    rrList(tempPathX);
                    rrList(tempPathY);

                    realPathX.add(tempPathX.get(0));
                    realPathY.add(tempPathY.get(0));
                }

                //Down Path Mege Algorithm
                if((Double.parseDouble(df.format(yCords().get(x) - yCords().get(x + 1))) == -0.1) && (Double.parseDouble(df.format(xCords().get(x) - xCords().get(x + 1))) == 0.0)){
                    tempPathX.add(Double.parseDouble(df.format(xCords().get(x))));
                    tempPathY.add(Double.parseDouble(df.format(yCords().get(x))));

                    rrList(tempPathX);
                    rrList(tempPathY);

                    realPathX.add(tempPathX.get(0));
                    realPathY.add(tempPathY.get(0));
                }

                //Up Path Mege Algorithm
                if((Double.parseDouble(df.format(yCords().get(x) - yCords().get(x + 1))) == 0.1) && (Double.parseDouble(df.format(xCords().get(x) - xCords().get(x + 1))) == 0.0)){
                    tempPathX.add(Double.parseDouble(df.format(xCords().get(x))));
                    tempPathY.add(Double.parseDouble(df.format(yCords().get(x))));

                    rrList(tempPathX);
                    rrList(tempPathY);

                    realPathX.add(tempPathX.get(0));
                    realPathY.add(tempPathY.get(0));
                }
            }
            else{
                System.out.println("Next False");

                //Right Path Mege Algorithm
                if((Double.parseDouble(df.format(xCords().get(x) - xCords().get(x - 1))) == 0.1) && (Double.parseDouble(df.format(yCords().get(x) - yCords().get(x - 1))) == 0.0)){
                    tempPathX.add(Double.parseDouble(df.format(xCords().get(x))));
                    tempPathY.add(Double.parseDouble(df.format(yCords().get(x))));
                    
                    rrList(tempPathX);
                    rrList(tempPathY);

                    realPathX.add(tempPathX.get(0));
                    realPathY.add(tempPathY.get(0));
                }

                //Left Path Mege Algorithm
                if((Double.parseDouble(df.format(xCords().get(x) - xCords().get(x - 1))) == -0.1) && (Double.parseDouble(df.format(yCords().get(x) - yCords().get(x - 1))) == 0.0)){
                    tempPathX.add(Double.parseDouble(df.format(xCords().get(x))));
                    tempPathY.add(Double.parseDouble(df.format(yCords().get(x))));
                    
                    rrList(tempPathX);
                    rrList(tempPathY);

                    realPathX.add(tempPathX.get(0));
                    realPathY.add(tempPathY.get(0));
                }

                //Down Path Mege Algorithm
                if((Double.parseDouble(df.format(yCords().get(x) - yCords().get(x - 1))) == 0.1) && (Double.parseDouble(df.format(xCords().get(x) - xCords().get(x - 1))) == 0.0)){
                    tempPathX.add(Double.parseDouble(df.format(xCords().get(x))));
                    tempPathY.add(Double.parseDouble(df.format(yCords().get(x))));
                    
                    rrList(tempPathX);
                    rrList(tempPathY);

                    realPathX.add(tempPathX.get(0));
                    realPathY.add(tempPathY.get(0));
                }

                //Up Path Mege Algorithm
                if((Double.parseDouble(df.format(yCords().get(x) - yCords().get(x - 1))) == -0.1) && (Double.parseDouble(df.format(xCords().get(x) - xCords().get(x - 1))) == 0.0)){
                    tempPathX.add(Double.parseDouble(df.format(xCords().get(x))));
                    tempPathY.add(Double.parseDouble(df.format(yCords().get(x))));
                    
                    rrList(tempPathX);
                    rrList(tempPathY);

                    realPathX.add(tempPathX.get(0));
                    realPathY.add(tempPathY.get(0));
                }
            }
        }
        

        
        //prints temp path lists 
        for(int x = 0; x < tempPathX.size(); x++){
            System.out.println("temp PathX :" + tempPathX.get(x));
        }
        for(int y = 0; y < tempPathY.size(); y++){
            System.out.println("temp PathY :" + tempPathY.get(y));
        }

        //prints real path lists
        for(int x = 0; x < tempPathX.size(); x++){
            System.out.println("temp PathX :" + tempPathX.get(x));
        }
        for(int y = 0; y < tempPathY.size(); y++){
            System.out.println("temp PathY :" + tempPathY.get(y));
        }
    }
}