import java.awt.Color;
import java.awt.Graphics;

public class Cell {
	private int myX, myY; // x,y position on grid
	private boolean myAlive; // alive (true) or dead (false)
	private int myNeighbors; // count of neighbors with respect to x,y
	private boolean myAliveNextTurn; // Used for state in next iteration
	private Color myColor; // Based on alive/dead rules
	private final static Color Kewlcolor2 = new Color(0xADDFFF);
	private final Color DEFAULT_ALIVE = Kewlcolor2;
	private final Color DEFAULT_DEAD = Color.WHITE;

	public Cell(int x, int y) {
		this(x, y, false, Color.WHITE);
	}        

	public Cell(int row, int col, boolean alive, Color color) {
		myAlive = alive;
		myColor = color;
		myX = col;
		myY = row;
	}

	public boolean getAlive() {
		return myAlive;
	}
	
	public int getAliveInt() {
		if (myAlive) return 1;
		else return 0;
	}

	public int getX() {
		return myX;
	}

	public int getY() {
		return myY;
	}

	public Color getColor() {
		return myColor;
	}

	public void setAlive(boolean alive) {
		if (alive) {
			setAlive(true, DEFAULT_ALIVE);
		} else {
			setAlive(false, DEFAULT_DEAD);
		}
	}
	
	public void toggleAlive() {
		if (myAlive) {
			setAlive(false, DEFAULT_DEAD);
		} else {
			setAlive(true, DEFAULT_ALIVE);
		}
	}

	public void setAlive(boolean alive, Color color) {
		myColor = color;
		myAlive = alive;
	}
	
	public void calcAliveNextTurn(Cell[][] cells) {
		calcNeighbors(cells);
		if (myAlive) {
			if (myNeighbors <= 1) { setAliveNextTurn(false); }
			else if (myNeighbors >= 4) { setAliveNextTurn(false); }
			else { setAliveNextTurn(true); }
		} else {
			if (myNeighbors == 3) { setAliveNextTurn(true); }
			else { setAliveNextTurn(false); }
		}
	}

	public void setAliveNextTurn(boolean alive) {
		myAliveNextTurn = alive;
	}

	public boolean getAliveNextTurn() {
		return myAliveNextTurn;
	}

	public void setColor(Color color) {
		myColor = color;
	}

	public int getNeighbors() {
		return myNeighbors;
	}

	public void calcNeighbors(Cell[][] cell) { //done
		int answer = 0;
		if (myX > 0) answer += cell[myY][myX - 1].getAliveInt();
		if (myX < 99) answer += cell[myY][myX + 1].getAliveInt();
		if (myY > 0) answer += cell[myY - 1][myX].getAliveInt();
		if (myY < 79) answer += cell[myY + 1][myX].getAliveInt();
		if (myX > 0 && myY > 0) answer += cell[myY - 1][myX - 1].getAliveInt();
		if (myX > 0 && myY < 79) answer += cell[myY + 1][myX - 1].getAliveInt();
		if (myX < 99 && myY > 0) answer += cell[myY - 1][myX + 1].getAliveInt();
		if (myX < 99 && myY < 79) answer += cell[myY + 1][myX + 1].getAliveInt();
		myNeighbors = answer;
	}

	public void draw(int x_offset, int y_offset, int width, int height, Graphics g) {
		int xleft = x_offset + 1 + (myX * (width + 1));
		int xright = x_offset + width + (myX * (width + 1));
		int ytop = y_offset + 1 + (myY * (height + 1));
		int ybottom = y_offset + height + (myY * (height + 1));
		Color temp = g.getColor();

		g.setColor(myColor);
		g.fillRect(xleft, ytop, width, height);
	}
}