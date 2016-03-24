package tower;

import java.awt.Point;
import java.awt.geom.Line2D;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * Laser class. This is for the laser tower.
 * 
 */
public class Laser {

	private Line2D.Double line;
	private int brushWidth;
	
	/**
	 * Initializes laser variables
	 * @param line
	 * @param brushWidth
	 */
	public Laser(Line2D.Double line, int brushWidth) {

		this.line = line;
		this.brushWidth = brushWidth;
	}

	/**
	 * Gets laser line to draw
	 * @return 
	 */
	public Line2D.Double getLaserLine() {
		return line;
	}

	/**
	 * Gets width of laser to draw
	 * @return
	 */
	public int getBrushStrokeWidth() {
		return brushWidth;
	}

}
