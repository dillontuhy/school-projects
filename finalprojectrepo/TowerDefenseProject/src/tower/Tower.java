//current tower
package tower;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.List;
import model.Clickable;
import observer.ClickableObserver;
import enemy.Enemy;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

public abstract class Tower extends Clickable {

	private Shape rangeCircle;
	private int rangeRadius;
	private int damage;
	private int price;
	public final int TOWER_TYPE;
	public static final int RED_TOWER_TYPE = 0, BLUE_TOWER_TYPE = 1, GREEN_TOWER_TYPE = 2;
	private Point gameLocation;

	/**
	 * Initialize Tower's instance variables
	 * @param towerType
	 * @param rangeRadius
	 * @param damage
	 * @param price
	 * @param clickObservers
	 */
	public Tower(int towerType, int rangeRadius, int damage, int price,
			List<ClickableObserver> clickObservers) {
		super(clickObservers);
		TOWER_TYPE = towerType;
		this.rangeRadius = rangeRadius;
		this.damage = damage;
		this.price = price;
		setSelectable(true);
		setHoverable(true);

		this.setBorder(this.getDefaultBorder());
	}

	/**
	 * Get the enum TowerType of this tower
	 * @return
	 */
	public abstract TowerType getTowerType();

	/**
	 * Get the alpha value the range should be drawn with
	 */
	public abstract float getRangeAlpha();

	/**
	 * Return the radius of the range
	 * @return
	 */
	public int getRange() {
		return rangeRadius;
	}

	/**
	 * Return the amount of damage the tower does each time it attacks
	 * @return
	 */
	public int getDamage() {
		return damage;
	}

	/**
	 * Return the price of the tower
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * Get the tower's game location
	 * @param return
	 */
	@Override
	public Point getGameLocation() {
		return gameLocation;
	}

	/**
	 * set the tower's game location
	 * @param location
	 */
	public void setGameLocation(Point location) {
		gameLocation = location;
		rangeCircle = new Ellipse2D.Double(gameLocation.x - rangeRadius,
				gameLocation.y - rangeRadius, 2 * rangeRadius, 2 * rangeRadius);
	}

	/**
	 * Search through the list and attack enemies based on internal strategy
	 * @param enemies
	 */
	public abstract void detectEnemies(List<Enemy> enemies);

	/**
	 * Print out the tower's attributes
	 */
	public String toString() {
		return "Tower:\nRange: " + rangeRadius + "\nDamage: " + damage
				+ "\nPrice: " + price;
	}

	/**
	 * Get the tower's color
	 * @return
	 */
	public Color getTowerColor() {
		return this.getBackground();
	}

	/**
	 * set the tower's color brighter
	 */
	public void setTowerColorBrighter() {
		this.setBackground(this.getBackground().brighter());
	}

	/**
	 * Get the circle showing the tower's range
	 * @return
	 */
	public Shape getRangeShape() {
		return rangeCircle;
	}

}
