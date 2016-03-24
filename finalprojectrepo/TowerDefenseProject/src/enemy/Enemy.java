package enemy;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;

import model.Clickable;
import observer.ClickableObserver;
import observer.EnemyHealthObserver;

import composite.BlendComposite;
import composite.BlendComposite.BlendingMode;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 * 
 */

public abstract class Enemy extends Clickable implements Comparable<Enemy> {
	private int goldOnDeath, speed, cost, health;
	private boolean death, crossedFinishLine;
	private Point gameLocation;
	private boolean eastWest;
	private int pathIndex, periodOffset;
	private List<Point> path;
	private Shape enemyShape;
	private Color enemyColor;
	private List<EnemyHealthObserver> enemyObservers;

	/**
	 * This is the constructor for the abstract class enemy
	 * 
	 * 
	 * @param List
	 *            of Clicable Observers, List of points that are paths enemies
	 *            will move along variables for health, speed, gold when the
	 *            enemies die, and the cost to buy one enemy shape is for the
	 *            image that the enemy displays, color is also what the enemy
	 *            displays
	 */
	public Enemy(List<ClickableObserver> clickObservers, List<Point> path,
			int health, int speed, int goldOnDeath, int cost,
			Polygon enemyShape, Color enemyColor) {
		super(clickObservers);
		this.enemyShape = new Rectangle(0, 0, 30, 30);
		this.enemyColor = enemyColor;

		this.enemyObservers = new ArrayList<EnemyHealthObserver>();

		this.health = health;
		this.speed = speed;
		this.goldOnDeath = goldOnDeath;
		this.cost = cost;

		this.path = path;
		death = false;
		crossedFinishLine = false;
		this.setPreferredSize(new Dimension(20, 20));
		pathIndex = 0;
		
		periodOffset = (int) (Math.random() * 10);

		if (path != null) {
			gameLocation = new Point(path.get(pathIndex).x
					+ (int) (Math.random() * 28) - 14, path.get(pathIndex).y
					+ (int) (Math.random() * 28) - 14);
			this.setWidthAndHeight(
					this.getMovementAxisLength(periodOffset * Math.PI
							/ getCycleTime()),
					this.getMovementAxisWidth(periodOffset * Math.PI
							/ getCycleTime()));
		}
		setSelectable(true);
		setHoverable(true);
		eastWest = true;

		this.setOpaque(false);

		this.setDefaultBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		this.setBorder(this.getDefaultBorder());
	}

	/**
	 * Returns the enemies color
	 */
	public Color getEnemyColor() {
		return enemyColor;

	}

	/**
	 * Sets bounds based on inputed width, height and preset gameLocation
	 * 
	 * @param width
	 * @param height
	 */
	private void setWidthAndHeight(int width, int height) {
		this.setBounds(gameLocation.x - width / 2, gameLocation.y - height / 2,
				width, height);
	}

	/**
	 * Notifies the asker if the enemy is dead
	 * 
	 */
	public boolean isDead() {
		return death;
	}

	/**
	 * Returns the path associated with this enemy
	 */
	public List<Point> getPath() {
		return path;
	}

	/**
	 * swaps the east and west values to handle a enemy moving in a corner
	 */
	public void turn() {
		eastWest = !eastWest;
		this.setWidthAndHeight(getHeight(), getWidth());

	}

	/**
	 * Moves and animates the enemies based on the location on the path
	 */
	public void moveAndAnimate() {

		int dx = 0, dy = 0;
		for (int i = 0; i < speed && pathIndex < path.size() - 1; i++) {
			pathIndex++;
			dx += path.get(pathIndex).x;
			dy += path.get(pathIndex).y;
			if (path.get(pathIndex).x == 0 && path.get(pathIndex).y == 0)
				turn();
		}
		// checks to see when the enemy crosses the finishline
		if (pathIndex >= path.size() - 3) {
			crossedFinishLine = true;
			return;
		}

		gameLocation.x += dx;
		gameLocation.y += dy;

		double radian = (pathIndex / speed + periodOffset) * Math.PI
				/ getCycleTime();// the speed of resizeing the enemies
		if (eastWest)
			this.setWidthAndHeight(getMovementAxisLength(radian),
					getMovementAxisWidth(radian));
		else
			this.setWidthAndHeight(getMovementAxisWidth(radian),
					getMovementAxisLength(radian));
	}

	/**
	 * returns the cycle time depending upon the enemy type
	 */
	public abstract double getCycleTime();

	/**
	 * returns the movement depending on the enemy type
	 * 
	 * @param radian
	 */
	public abstract int getMovementAxisWidth(double radian);

	/**
	 * returns the movement depending on the enemy type
	 * 
	 * @param width
	 */
	public abstract int getMovementAxisLength(double radian);

	/**
	 * returns the enemies current health
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * Damages the enemy and alters it's health
	 * 
	 * @param damage
	 */
	public void takeDamage(int damage) {
		health -= damage;
		if (health <= 0) {
			health = 0;
			death = true;
		}
		if (this.isSelected())
			notifyOfUpdate();
	}

	/**
	 * Adds an enemy health observer to this enemy
	 * 
	 * @param eho
	 */
	public void addEnemyHealthObserver(EnemyHealthObserver eho) {
		enemyObservers.add(eho);
	}

	/**
	 * removes an enemy health observer for when an enemy dies
	 * 
	 * @param eho
	 */
	public void removeEnemyHealthObserver(EnemyHealthObserver eho) {
		enemyObservers.remove(eho);
	}

	/**
	 * updates all the health observsers in the enemyObservers list
	 */
	public void notifyOfUpdate() {
		for (EnemyHealthObserver eo : enemyObservers) {
			eo.notifyOfUpdateTo(this);
		}
	}

	/**
	 * returns the enenmies gold on death
	 */
	public int getGoldOnDeath() {
		return goldOnDeath;
	}

	/**
	 * returns the enemies characteristics for the display panel
	 */
	public String toString() {
		return "Enemy:\nGold on death: " + goldOnDeath + "\nCost: " + cost
				+ "\nSpeed: " + speed + "\nHealth: " + health;
	}

	/**
	 * returns the exact Point of the center of the enemy any time this is
	 * called
	 */
	@Override
	public Point getGameLocation() {
		return gameLocation;
	}

	/**
	 * sets the location to a specific point
	 * 
	 * @param location
	 */
	public void setGameLocation(Point location) {
		gameLocation = location;
	}

	/**
	 * lets the caller know if the enemy has crossed the finish line
	 */
	public boolean hasCrossedFinishLine() {
		return crossedFinishLine;
	}

	/**
	 * returns the cost of the enemy based on the enemy
	 */
	@Override
	public int getPrice() {
		return cost;
	}
	
	/** compares two pathIndices to see which is further ahead along the path
	 *  
	 * @param arg0
	 */
	@Override
	public int compareTo(Enemy arg0) {
		return arg0.pathIndex - this.pathIndex;
	}

	/**
	 * This is the overridden paintComponent method, it draws the pictures.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gr = (Graphics2D) g;
		gr.setComposite(BlendComposite.getInstance(BlendingMode.MULTIPLY));
		gr.setColor(enemyColor);
		gr.fill(enemyShape);
	}
}
