package enemy;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.List;
import model.Clickable;
import observer.ClickableObserver;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * 
 * This class sets up the Big Enemy extends abstract class Enemy
 * 
 */
public class BigEnemy extends Enemy {

	private int[][] guide;
	private BufferedImage enemy;
	private int avgWidth = 15, avgHeight = 15, ampWidth = 7, ampHeight = 7;
	private static final Polygon BIG_ENEMY_SHAPE = new Polygon(new int[] { 0,
			0, 22, 22 }, new int[] { 0, 22, 22, 0 }, 4);
	private static final Color BIG_ENEMY_COLOR = new Color(0, 255, 255, 205);

	/**
	 * Initializes the BigEnemy on creation providing the health, gold on death and other characteristics
	 * 
	 * @param  clickObservers, path
	 */
	public BigEnemy(List<ClickableObserver> clickObservers, List<Point> path) {

		super(clickObservers, path, 15, 6, 2, 2, BIG_ENEMY_SHAPE,
				BIG_ENEMY_COLOR);
	}

	/**
	 * When this enemy is clicked the Clickable tells the user that it is the
	 * big enemy and gives its statistics
	 */
	@Override
	public Clickable getNewInstanceOf() {
		return new BigEnemy(getClickableObservers(), getPath());
	}

	/**
	 * This method gives the cycle time which manipulates the speed of the Big
	 * Enemy
	 */
	@Override
	public double getCycleTime() {
		return 8;
	}

	/**
	 * This method manipulates the width movement/animation of the Big Enemy
	 * 
	 * @param radian
	 */
	@Override
	public int getMovementAxisWidth(double radian) {
		return avgWidth + (int) (Math.sin(radian) * ampWidth);
	}

	/**
	 * This method returns the axis length of the Big Enemy's side to side
	 * movement
	 * 
	 * @param radian
	 */
	@Override
	public int getMovementAxisLength(double radian) {
		return avgHeight + (int) (Math.sin(radian) * ampHeight);
	}
}
