/*
 * This program is designed to solve Maze problem:
 * Program finds optimal way from A, B, C points  to X:  
 *  X1110A
 *  100111
 *  B1111C
 *  
 *  example: format of solution: A: D1L2U1L3 which means step Down 1 times, 
 *  then move 2 times left etc. until reachX point. 
 * author: Boris Wainberg
 * status: done.  
 */

package mazePathfinder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/*
 *  Simple stack implementation based on LinkedList
 */

class NodeStack<Cell> {
	private static LinkedList listNodes = null;

	public NodeStack() {
		listNodes = new LinkedList<Cell>();
	}

	public int getSize() {
		return listNodes.size();
	}

	public Cell pop() {
		return (Cell) listNodes.removeFirst();
	}

	public void push(Cell c) {
		listNodes.addFirst(c);
	}

	public Cell top() {
		if (listNodes.size() > 0) {
			return (Cell) listNodes.getFirst();
		} else
			return null;
	}
}

class Cell {
	boolean isVisited;
	boolean isWall;
	private Cell cellUp;
	private Cell cellDown;
	private Cell cellLeft;
	private Cell cellRight;
	private boolean isEnd;
	private int x, y;
	private int number;
	private int pathLength = 0;
	public Cell prevNode = null;

	public boolean isVisited() {
		return isVisited;
	}

	public void setVisited(boolean isVisited) {
		this.isVisited = isVisited;
	}

	public boolean isWall() {
		return isWall;
	}

	public void setWall(boolean isWall) {
		this.isWall = isWall;
	}

	public Cell getCellUp() {
		return cellUp;
	}

	public void setCellUp(Cell cellUp) {
		this.cellUp = cellUp;
	}

	public Cell getCellDown() {
		return cellDown;
	}

	public void setCellDown(Cell cellDown) {
		this.cellDown = cellDown;
	}

	public Cell getCellLeft() {
		return cellLeft;
	}

	public void setCellLeft(Cell cellLeft) {
		this.cellLeft = cellLeft;
	}

	public Cell getCellRight() {
		return cellRight;
	}

	public void setCellRight(Cell cellRight) {
		this.cellRight = cellRight;
	}

	public boolean isEnd() {
		return isEnd;
	}

	public void setEnd(boolean isEnd) {
		this.isEnd = isEnd;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setPathLength(int length) {
		this.pathLength = length;
	}

	public void incPathLength() {
		this.pathLength++;
	}

	public int getX() {
		return this.x;
	}

	public int getY() {
		return this.y;
	}

	public int getPathLength() {
		return this.pathLength;
	}

	public Cell getPrevNode() {
		return this.prevNode;
	}

	public void setPrevNode(Cell cell) {
		this.prevNode = cell;
	}

}

class InitCells {
	public static Cell[][] CellMap = null;
	public static List<Cell> cellList = new ArrayList<Cell>();
	public static boolean isWayfound = false;
	public static Cell node = null;
	public static NodeStack<Cell> nodes = new NodeStack<Cell>();
	public static int exits;
	public static StringBuilder way = new StringBuilder();

	private static Cell getCellByXY(int x, int y) {
		for (Cell c : cellList) {
			if (c.getX() == x && c.getY() == y) {
				return c;
			}
		}
		return null;
	}

	public static void sequenceCount(String s) {
		char[] arr = s.toCharArray();
		int i = 0, n = arr.length;
		while (i < n) {
			char c = arr[i];
			int count = 0;
			do {
				++i;
				++count;
			} while (i < n && arr[i] == c);
			System.out.print(count + "" + c);
		}
	}

	public static void findWay(List cells, Cell startPoint) {
		Cell current = null;
		startPoint.isVisited = true;

		if (startPoint.isEnd()) {
			isWayfound = true;
		} else {
			Cell left = startPoint.getCellLeft();
			Cell right = startPoint.getCellRight();
			Cell down = startPoint.getCellDown();
			Cell up = startPoint.getCellUp();

			exits = 0;
			// count number of possible exits
			if (left != null && !left.isVisited && !isWayfound) {
				exits++;
			}
			if (up != null && !up.isVisited && !isWayfound) {
				exits++;
			}
			if (down != null && !down.isVisited && !isWayfound) {
				exits++;
			}
			if (right != null && !right.isVisited && !isWayfound) {
				exits++;
			}
			if (exits > 1) {
				// set current cell as node
				nodes.push(startPoint);
				node = startPoint;
				nodes.top().setPathLength(0);
			}

			if (left != null && !left.isVisited && !isWayfound) {
				current = left;
				current.isVisited = true;
				if (nodes.top() != null)
					nodes.top().incPathLength();
				way.append("L");
				findWay(cellList, current);
			} else

			if (up != null && !up.isVisited && !isWayfound) {
				current = up;
				current.isVisited = true;
				if (nodes.top() != null)
					nodes.top().incPathLength();
				way.append("U");
				findWay(cellList, current);
			} else

			if (down != null && !down.isVisited && !isWayfound) {
				current = down;
				current.isVisited = true;
				if (nodes.top() != null)
					nodes.top().incPathLength();
				way.append("D");
				findWay(cellList, current);

			} else if (right != null && !right.isVisited && !isWayfound) {
				current = right;
				current.isVisited = true;
				if (nodes.top() != null)
					nodes.top().incPathLength();
				way.append("R");
				findWay(cellList, current);
			}

			// way blocked. continue from last saved node.
			if (!isWayfound) {
				way.delete(way.length() - nodes.top().getPathLength(),
						way.length());
				findWay(cellList, nodes.pop());
			}
		}
	}

	public static void createMap(int[][] array, int n, int m) {
		InitCells.cellList = new ArrayList<Cell>();
		int rows = array.length;
		int cc = 0;
		int h;
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				Cell c = new Cell();
				int val = array[j][i];
				c.setX(i);
				c.setY(j);
				c.isVisited = false;
				if (val == 0) {
					c.isWall = true;
					c.setCellDown(null);
					c.setCellLeft(null);
					c.setCellRight(null);
					c.setCellUp(null);
					if (i == 0 && j == 0) {
						c.setEnd(true);
					}

				} else {
					c.isWall = false;
					c.isVisited = false;
				}
				cellList.add(c);
			}
		}

		// here we set Neighbor of each cell
		// for border cells we set null as pointer to its neighbor
		// TODO: it looks a bit awkward but leave as is until redesign.

		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				Cell c = getCellByXY(i, j);
				if (c.isWall)
					continue;
				if (c.getX() == 0 && c.getY() == 0) {
					c.setEnd(true);
				}
				if (c.getX() == 0) {
					c.setCellUp(null);
				} else if (!getCellByXY(c.getX() - 1, j).isWall) {
					c.setCellUp(getCellByXY(c.getX() - 1, j));
				}
				if (c.getX() == m - 1) {
					c.setCellDown(null);
				} else if (!getCellByXY(c.getX() + 1, j).isWall) {
					c.setCellDown(getCellByXY(c.getX() + 1, j));
				}
				if (c.getY() == 0) {
					c.setCellLeft(null);
				} else if (!getCellByXY(i, c.getY() - 1).isWall) {
					c.setCellLeft(getCellByXY(i, c.getY() - 1));
				}
				if (c.getY() == n - 1) {
					c.setCellRight(null);
				} else if (!getCellByXY(i, c.getY() + 1).isWall) {
					c.setCellRight(getCellByXY(i, c.getY() + 1));
				}
			}
		}
	}
}

public class Maze {
	public static final String fileName = "data/1.txt";

	private static int getN(String fileName) {
		File file = null;
		InputStream fis = null;
		int n = 0;
		int m = 0;

		try {
			file = new File(fileName);
			fis = new FileInputStream(file);
			Scanner in = new Scanner(file);

			n = in.nextInt();
			m = in.nextInt();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return n;
	}

	private static int getM(String fileName) {
		File file = null;
		InputStream fis = null;
		int n = 0;
		int m = 0;

		try {
			file = new File(fileName);
			fis = new FileInputStream(file);
			Scanner in = new Scanner(file);

			n = in.nextInt();
			m = in.nextInt();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return m;
	}

	private static int[][] getInputData(String fileName) {
		InputStream fis = null;
		int[][] data = null;
		int m = 0;
		int n = 0;
		String line = null;
		try {
			File file = new File(fileName);
			fis = new FileInputStream(file);
			Scanner in = new Scanner(file);
			n = in.nextInt();
			m = in.nextInt();
			data = new int[n][m];
			int j = 0;
			line = in.nextLine();
			while (in.hasNextLine()) {
				line = in.next();
				for (int i = 0; i < line.length(); i++) {
					int k = Integer.parseInt(String.valueOf(line.charAt(i)));
					data[i][j] = k;
				}
				j++;
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				fis.close();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		return data;
	}

	private static void newEdge() {
		int n = getN(fileName);
		int m = getM(fileName);

		InitCells.createMap(getInputData(fileName), n, m);
		InitCells.way = new StringBuilder();
		InitCells.isWayfound = false;
		InitCells.node = null;
	}

	public static void main(String[] args) {
		// TODO: main is ugly. leave as is until redesign.

		int n = getN(fileName);
		int m = getM(fileName);
		InitCells.createMap(getInputData(fileName), n, m);

		// from A
		InitCells.findWay(InitCells.cellList, InitCells.cellList.get(n - 1));
		InitCells.sequenceCount(InitCells.way.toString());

		// from B
		System.out.print(" ");
		newEdge();
		InitCells
				.findWay(InitCells.cellList, InitCells.cellList.get(n * m - n));

		InitCells.sequenceCount(InitCells.way.toString());
		// from C
		System.out.print(" ");
		newEdge();
		InitCells
				.findWay(InitCells.cellList, InitCells.cellList.get(n * m - 1));
		InitCells.sequenceCount(InitCells.way.toString());
	}
}
