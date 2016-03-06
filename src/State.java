// STATE OF LIGHTS

import java.util.LinkedList;
import java.awt.Point;

public class State {
	private State parent;
	private int[][] config;
	private int cost;
	private int manhattanDistance;
	private int fValue;


	/*CONSTRUCTORS FOR STATE*/
	
	// root state
	public State () {
		config = new int[3][3];
		parent = null;
		cost = 0;

		for (int x = 0, num = 1; x < 3; x += 1) {
			for (int y = 0; y < 3; y += 1, num += 1) {
				if (num == 9) num = 0;
				config[x][y] = num;
			}
		}

		manhattanDistance = computeManhattanDistance();
		fValue = cost + manhattanDistance;
	}

	// root state with predefined configuration
	public State (int[][] tiles) {
		config = new int[3][3];
		parent = null;
		cost = 0;

		for (int x = 0; x < 3; x += 1) {
			for (int y = 0; y < 3; y += 1) {
				config[x][y] = tiles[x][y];
			}
		}

		manhattanDistance = computeManhattanDistance();
		fValue = cost + manhattanDistance;
	}
	
	// child state with predefined configuration
	public State (int[][] tiles, State oldState) {
		config = new int[3][3];
		parent = oldState;
		cost = oldState.getCost() + 1;

		for (int x = 0; x < 3; x += 1) {
			for (int y = 0; y < 3; y += 1) {
				config[x][y] = tiles[x][y];
			}
		}

		manhattanDistance = computeManhattanDistance();
		fValue = cost + manhattanDistance;
	}
	/*END OF CONSTRUCTORS*/

	// will toggle one tile to another
	public void toggleTile (Point currentPoint, Point nextPoint) {
		int tmp = 0;
		
		tmp = config[currentPoint.x][currentPoint.y];
		config[currentPoint.x][currentPoint.y] = config[nextPoint.x][nextPoint.y];
		config[nextPoint.x][nextPoint.y] = tmp;
	}
	
	// get goal value
	public Point getGoalValue (int toFind) {
		int num = 1;
		for (int x = 0; x < 3; x += 1) {
			for (int y = 0; y < 3; y += 1, num += 1) {
				if (num == 9) num = 0;
				if (toFind == num) {
					return new Point(x, y);
				}
			}
		}
		return new Point(0, 0);
	}

	// computes for manhattan distance
	public int computeManhattanDistance () {
		int total = 0;
		Point tmpPoint = null;

		for (int x = 0; x < 3; x += 1) {
			for (int y = 0; y < 3; y += 1) {
				tmpPoint = getGoalValue(config[x][y]);
				if (config[x][y] != 0) total += Math.abs(x - tmpPoint.x) + Math.abs(y - tmpPoint.y);
			}
		}

		return total;
	}

	// prints the current config of the state
	public void printConfig () {
		for (int x = 0; x < 3; x += 1) {
			for (int y = 0; y < 3; y += 1) {
				System.out.print(config[x][y]);
				System.out.print(" ");
			}
			System.out.print("\n");
		}

		System.out.print("\n");
	}

	// prints a point
	public void printPoint (Point point) {
		System.out.println(point.x + " and " + point.y);
		System.out.println("");            
	}

	// get configuration of the state
	public int[][] getConfig () {
		return config;
	}
	
	// get value of config per lights
	public void setConfigValue (int x, int y, int value) {
		config[x][y] = value;
	}

	// get value of config per lights
	public int getConfigValue (int x, int y) {
		return config[x][y];
	}
	
	// get cost
	public int getCost () {
		return cost;
	}

	// get manhattanDistance
	public int getManhattanDistance () {
		return manhattanDistance;
	}

	// get f value
	public int getFValue () {
		return fValue;
	}
	
	// get parent
	public State getParent () {
		return parent;
	}
}
