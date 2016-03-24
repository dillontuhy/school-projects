package enemy;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.util.List;
import model.Clickable;
import observer.ClickableObserver;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */
/**
 * The small enemy class extends enemy
 */
public class SmallEnemy extends Enemy {

	private int avgWidth = 15, avgLength = 15, amplitude = 14, cycleTime = 10;
	private static final Polygon SMALL_ENEMY_SHAPE = new Polygon(new int[] { 0,
			0, 30, 30 }, new int[] { 0, 30, 30, 0 }, 4);
	private static final Color SMALL_ENEMY_COLOR = new Color(255, 255, 0, 205);
	/**
	 * Initializes the SmallEnemy on creation providing the health, gold on death and other characteristics
	 * 
	 * @param  clickObservers, path
	 */
	public SmallEnemy(List<ClickableObserver> clickObservers, List<Point> path) {
		super(clickObservers, path, 10, 5, 1, 1, SMALL_ENEMY_SHAPE,
				SMALL_ENEMY_COLOR);

	}
	/**
	 * When this enemy is clicked the Clickable tells the user that it is the
	 * Boss enemy and gives its statistics
	 */
	@Override
	public Clickable getNewInstanceOf() {
		return new SmallEnemy(getClickableObservers(), getPath());
	}
	/**
	 * returns the avgWidth of the enemy
	 * 
	 * @param radian
	 */
	@Override
	public int getMovementAxisWidth(double radian) {
		return avgWidth;
	}
	/**
	 * returns the average length of the enemy
	 * 
	 * @param  radian
	 */
	@Override
	public int getMovementAxisLength(double radian) {
		return avgLength + (int) (Math.sin(radian) * amplitude);
	}
	/**
	 * returns the cycle time of the enemy (how quickly it oscillates)
	 */
	@Override
	public double getCycleTime() {
		return cycleTime;
	}

}