package FRCVersion;

import java.util.ArrayList;
import javax.swing.JOptionPane;

public class Algorithms{

    public void Dijkstra() {
        System.out.println("Dijkstra");
        //creates a priority que
        ArrayList<Node> priority = new ArrayList<Node>(); 
        //add the start node first to the que
        priority.add(FrcApp.map[FrcApp.startx][FrcApp.starty]); 
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
        priority.add(FrcApp.map[FrcApp.startx][FrcApp.starty]);
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
                    Node nearby = FrcApp.map[x][current.getY()];
                    if((nearby.getHops() == -1 || nearby.getHops() > hops) && (nearby.getType() != 1)){
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
                    Node nearby = FrcApp.map[current.getX()][y];
                    //check if its not a wall
                    if((nearby.getHops() == -1 || nearby.getHops() > hops) && (nearby.getType() != 1)){
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
                        Node nearby = FrcApp.map[x][y];
                        //check if its not a wall
                        if((nearby.getHops() == -1 || nearby.getHops() > hops) && (nearby.getType() != 1)){
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
            backtrack(current.getLastX(), current.getLastY(), hops);
        }
    }

    //Backtracjing so it can draw the correct path
    public void backtrack(int lastX, int lastY, int hops){
        FrcApp.length = hops;
        while(hops > 1) {	
            Node current = FrcApp.map[lastX][lastY];
            //sets it to the final path
            System.out.println("BackTracking");
            current.setType(4);
            lastX = current.getLastX();
            lastY = current.getLastY();
            hops--;
        }
        FrcApp.start = false;
    }
    
}