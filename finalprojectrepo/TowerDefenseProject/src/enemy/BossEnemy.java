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
 * This class sets up the Boss Enemy extends abstract class Enemy
 * 
 */
public class BossEnemy extends Enemy {

	private static final Polygon BOSS_ENEMY_SHAPE = new Polygon(new int[] { 0,
			10, 20, 20, 10, 0 }, new int[] { 5, 0, 5, 15, 20, 15 }, 6);
	private static final Color BOSS_ENEMY_COLOR = new Color(255, 0, 255, 205);
	private int avgWidth = 15, avgHeight = 15, ampWidth = 7, ampHeight = 7;
	/**
	 * Initializes the BossEnemy on creation providing the health, gold on death and other characteristics
	 * 
	 * @param  clickObservers, path
	 */
	public BossEnemy(List<ClickableObserver> clickObservers, List<Point> path) {

		super(clickObservers, path, 20, 7, 3, 3, BOSS_ENEMY_SHAPE,
				BOSS_ENEMY_COLOR);
	}

	/**
	 * When this enemy is clicked the Clickable tells the user that it is the
	 * Boss enemy and gives its statistics
	 */
	@Override
	public Clickable getNewInstanceOf() {

		return new BossEnemy(getClickableObservers(), getPath());
	}

	/**
	 * This method gives the cycle time which manipulates the speed of the Boss
	 * Enemy
	 */
	@Override
	public double getCycleTime() {
		return 8;
	}

	/**
	 * This method manipulates the width movement/animation of the Boss Enemy
	 * 
	 * @param radian
	 */
	@Override
	public int getMovementAxisWidth(double radian) {
		return avgWidth + (int) (Math.sin(radian) * ampWidth);
	}

	/**
	 * This method manipulates the width movement/animation of the Boss Enemy
	 * 
	 * @param radian
	 */
	@Override
	public int getMovementAxisLength(double radian) {
		return avgHeight + (int) (-Math.sin(radian) * ampHeight);
	}
}
