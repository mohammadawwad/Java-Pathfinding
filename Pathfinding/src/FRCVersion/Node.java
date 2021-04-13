// package FRCVersion;

// public class Node {

//         frcAutoNav frc = new frcAutoNav();
// 		// Box Types
// 		// 0 = start 
//         // 1 = wall
//         // 2 = erase
//         // 4 = finish
//         // 4 = checked
//         // 5 = finalpath
// 		private int boxType = 0;
// 		private int hops;
// 		private int x;
// 		private int y;
// 		private int lastX;
// 		private int lastY;
// 		private double distance = 0;
	
//         //CONSTRUCTOR
// 		public Node(int type, int x, int y) {	
// 			boxType = type;
// 			this.x = x;
// 			this.y = y;
// 			hops = -1;
// 		}

//         //Euclidean Distance caluclates the shortest distance between to co-ordinates
//         ///in this case we want it to be the current nodes position with the finish distance
//         // PS I googled the formula I havent taken this in Math Yet :)
//         public double getEDistance(){
//             //current - finish
//             int newX = Math.abs(x - frc.finishx); //absolute value of X
//             int newY = Math.abs(y - frc.finishy); //absolute value of Y
//             distance = Math.sqrt((newX * newX) + (newY * newY));
//             return distance;
//         }
		
//         //Getting Methods 
// 		public int getX() {return x;}		
// 		public int getY() {return y;}
// 		public int getLastX() {return lastX;}
// 		public int getLastY() {return lastY;}
// 		public int getType() {return boxType;}
// 		public int getHops() {return hops;}
		
//         //Setting Methods 
// 		public void setType(int type) {boxType = type;}		
// 		public void setLastNode(int x, int y) {lastX = x; lastY = y;}
// 		public void setHops(int hops) {this.hops = hops;}
// 	}