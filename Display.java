import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;


public class Display extends JComponent implements MouseListener, MouseMotionListener {
	public static final int ROWS = 80;
	public static final int COLS = 100;
	public static Cell[][] cells = new Cell[ROWS][COLS];
	private final int X_GRID_OFFSET = 25; // 25 pixels from left
	private final int Y_GRID_OFFSET = 40; // 40 pixels from top
	private final int CELL_WIDTH = 5;
	private final int CELL_HEIGHT = 5;
	private final int DISPLAY_WIDTH;   
	private final int DISPLAY_HEIGHT;
	private StartButton startStop;
	private StepButton step;
	private ClearButton clear;
	private QuitButton quit;
	private boolean paintloop = false;
	private boolean paintforever = false;
	final int TIME_BETWEEN_REPLOTS = 300;
	private final static Color Gray = new Color(0xC0C0C0);


	public Display(int width, int height) {
		DISPLAY_WIDTH = width;
		DISPLAY_HEIGHT = height;
		init();
	}


	public void init() {
		setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		initCells();

		addMouseListener(this);
		addMouseMotionListener(this);

		startStop = new StartButton();
		startStop.setBounds(66, 550, 100, 36);
		add(startStop);
		startStop.setVisible(true);
		
		step = new StepButton();
		step.setBounds(206, 550, 100, 36);
		add(step);
		step.setVisible(true);
		
		clear = new ClearButton();
		clear.setBounds(346, 550, 100, 36);
		add(clear);
		clear.setVisible(true);
		
		quit = new QuitButton();
		quit.setBounds(486, 550, 100, 36);
		add(quit);
		quit.setVisible(true);
		
		repaint();
	}


	public void paintComponent(Graphics g) {

		g.setColor(Gray);
		drawGrid(g);
		drawCells(g);
		drawButtons();

		if (paintloop) {
			try {
				Thread.sleep(TIME_BETWEEN_REPLOTS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if (paintforever) { nextGeneration(); }
			repaint();
			paintloop = paintforever;
		}
		
	}

	public void initCells() {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				cells[row][col] = new Cell(row, col);
			}
		}
		
		cells[36][22].setAlive(true); // sample use of cell mutator method
		cells[36][23].setAlive(true); // sample use of cell mutator method
		cells[36][24].setAlive(true);
		cells[37][23].setAlive(true);
		cells[35][23].setAlive(true);
		cells[35][22].setAlive(true);
		cells[35][24].setAlive(true);
		cells[37][22].setAlive(true);
		cells[37][24].setAlive(true);
	}


	public void togglePaintLoop() {
		paintloop = !paintloop;
	}
	
	public void setPaintLoop(boolean value) {
		paintloop = value;
	}


	void drawGrid(Graphics g) {
		for (int row = 0; row <= ROWS; row++) {
			g.drawLine(X_GRID_OFFSET,
					Y_GRID_OFFSET + (row * (CELL_HEIGHT + 1)), X_GRID_OFFSET
					+ COLS * (CELL_WIDTH + 1), Y_GRID_OFFSET
					+ (row * (CELL_HEIGHT + 1)));
		}
		for (int col = 0; col <= COLS; col++) {
			g.drawLine(X_GRID_OFFSET + (col * (CELL_WIDTH + 1)), Y_GRID_OFFSET,
					X_GRID_OFFSET + (col * (CELL_WIDTH + 1)), Y_GRID_OFFSET
					+ ROWS * (CELL_HEIGHT + 1));
		}
	}

	
	void drawCells(Graphics g) {
		// Have each cell draw itself
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				cells[row][col].draw(X_GRID_OFFSET, Y_GRID_OFFSET, CELL_WIDTH,
						CELL_HEIGHT, g);
			}
		}
	}


	private void drawButtons() {
		startStop.repaint();
		clear.repaint();
		step.repaint();
		quit.repaint();
	}


	private void nextGeneration() {
		for (Cell[] e : cells) {
			for (Cell c : e) {
				c.calcAliveNextTurn(cells);
			}
		}
		
		for (Cell[] e : cells) {
			for (Cell c : e) {
				c.setAlive(c.getAliveNextTurn());
			}
		}

	}


	public void mouseClicked(MouseEvent arg0) {
		//add if within range of cells; use floor functions to locate the cell index
		int cellX = (arg0.getX() - 25) / 6;
		int cellY = (arg0.getY() - 40) / 6;
		if (cellX <= 99 && cellX >= 0 && cellY <= 79 && cellY >= 0) {
			cells[cellY][cellX].toggleAlive();
			paintloop = true;
			repaint();
		}
	}


	public void mouseEntered(MouseEvent arg0) {

	}


	public void mouseExited(MouseEvent arg0) {

	}


	public void mousePressed(MouseEvent arg0) {

	}


	public void mouseReleased(MouseEvent arg0) {

	}


	public void mouseDragged(MouseEvent arg0) {

	}


	public void mouseMoved(MouseEvent arg0) {
		
	}
	

	private class StartButton extends JButton implements ActionListener {
		StartButton() {
			super("Start");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) {
			// nextGeneration(); // test the start button
			if (this.getText().equals("Start")) {
				paintforever = true;
				paintloop = true;
				setText("Pause");
			} else {
				paintforever = false;
				setText("Start");
			}
			repaint();
		}
	}
	
	private class StepButton extends JButton implements ActionListener {
		StepButton() {
			super("Step");
			addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent arg0) {
			setPaintLoop(true);
			repaint();
			nextGeneration();
		}
	}
	
	private class ClearButton extends JButton implements ActionListener {
		ClearButton() {
			super("Clear");
			addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent arg0) {
			for (Cell[] y : cells) {
				for (Cell x : y) {
					x.setAlive(false);
				}
			}
			setPaintLoop(true);
			repaint();
			nextGeneration();
		}
	}
	
	private class QuitButton extends JButton implements ActionListener {
		QuitButton() {
			super("Quit");
			addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent arg0) {
			System.exit(0);
		}
	}
}
