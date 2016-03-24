package map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import model.Clickable;
import observer.ClickableObserver;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * This class defines the square components in the maps and store.
 * @author martinstankard
 *
 */
public class Tile extends Clickable {

	private boolean isPath;
	private boolean isTower;
	private boolean isOpen;
	private boolean hasHover;
	public final int X_INDEX, Y_INDEX;

	/**
	 * this method creates the tiles at a neutral starting state.
	 * @param yIndex
	 * @param xIndex
	 * @param clickObservers
	 */
	public Tile(int yIndex, int xIndex, List<ClickableObserver> clickObservers) {
		super(clickObservers);
		layoutGui();
		isPath = false;
		isTower = false;
		isOpen = true;
		hasHover = false;
		setHoverable(false);
		this.setBackground(Color.BLACK);
		this.setDefaultBorder(null);
		setSelectable(true);

		X_INDEX = xIndex;
		Y_INDEX = yIndex;
		this.setOpaque(false);
	}

	/**
	 * this method determines if a tower can be built based on if it is available and
	 * on what is selected.
	 * @return
	 */
	public boolean canPlaceTower() {
		return isOpen && isSelected();
	}

	/**
	 * this method redraws tiles based of mouseListener event interaction
	 */
	@Override
	public void hover(boolean hovering) {
		hasHover = hovering && isHoverable();
		repaint();
	}

	/**
	 * This method determines the red/green onMouseover 
	 * colors based on mouseListener event interaction; 
	 * green - buildable here, red = no.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gr = (Graphics2D) g;
		if (hasHover) {
			if (isOpen)
				gr.setColor(new Color(0, 255, 0, 155));
			else
				gr.setColor(new Color(255, 0, 0, 155));
			gr.fillRect(0, 0, 44, 44);
		}
	}

	/** this method determines 
	 * what color the tile is, based on what kind of job it is doing.
	 * @param string
	 */
	public void setType(String string) {
		if (string.equals("Path")) {
			isOpen = false;
			isPath = true;
			isTower = false;
			this.setBackground(Color.GRAY);
			this.setOpaque(true);
			repaint();
		}
		if (string.equals("Tower")) {
			isOpen = false;
			isTower = true;
			isPath = false;
			repaint();
		}
		if (string.equals("Open")) {
			isOpen = true;
			isTower = false;
			isPath = false;
			this.setBackground(Color.BLACK);
			this.setOpaque(false);
			repaint();
		}

	}
/**
 * This method determines what kind of role a specific tile is filling. 
 * @return
 */
	public String getType() {
		if (isOpen == true) {
			return "Open";
		}

		if (isTower == true) {
			return "Tower";
		}
		if (isPath == true) {
			return "Path";
		}
		return "something bad happened";

	}

	/**
	 * this method takes calls a minor gui overhead method
	 */
	private void layoutGui() {
		this.setLayout(null);
	}
/**
 * this method is overriden and has to be here because it is an abstract method
 */
	@Override
	public int getPrice() {
		return 0;
	}
/**this method is a  to string and returns the string that goes into the display panel.
 * 
 */
	@Override
	public String toString() {

		return getType() + " Tile\nX index: " + X_INDEX + "\nY index: " + Y_INDEX;
	}
/**
 * this method creates a new tile instance.
 */
	@Override
	public Clickable getNewInstanceOf() {
		return new Tile(Y_INDEX, X_INDEX, getClickableObservers());
	}
/**
 * this method determines the center of the tile's pixel coordinates relative to the rest of the map panel
 */
	@Override
	public Point getGameLocation() {
		return new Point(X_INDEX * this.getWidth() + this.getWidth() / 2,
				Y_INDEX * this.getHeight() + this.getHeight() / 2);
	}
}
