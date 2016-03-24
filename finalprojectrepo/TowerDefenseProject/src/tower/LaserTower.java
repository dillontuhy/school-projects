package tower;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.util.List;
import java.util.Random;
import model.Clickable;
import observer.ClickableObserver;
import enemy.Enemy;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

public class LaserTower extends Tower {

	private Laser laser;
	private Color rColor;
	private int sineCounter;
	private static final int MAX_SINE = 7;

	/**
	 * Initialize lasertower instance variables
	 * @param clickObservers
	 */
	public LaserTower(List<ClickableObserver> clickObservers) {

		super(Tower.RED_TOWER_TYPE, 44 * 3, 3, 25, clickObservers);
		setColor();
		laser = null;
		sineCounter = 0;
	}

	/**
	 * Get new LaserTower
	 * @param return
	 */
	@Override
	public Clickable getNewInstanceOf() {
		return new LaserTower(getClickableObservers());
	}

	/**
	 * Get string output of lasertower
	 * @param return
	 */
	@Override
	public String toString() {
		return "Laser" + super.toString();
	}

	/**
	 * Get alpha value of range to draw
	 * @param return
	 */
	@Override
	public float getRangeAlpha() {
		return 0.5f;
	}

	/**
	 * Search through the list and attack enemies based on internal strategy
	 * @param enemies
	 */
	@Override
	public void detectEnemies(List<Enemy> enemies) {
		sineCounter++;
		int sineWidth = (int) Math.abs(Math.sin(sineCounter * Math.PI / 10.0)
				* MAX_SINE);
		for (Enemy enemy : enemies) {
			if (getRangeShape().contains(enemy.getGameLocation())) {

				if (MAX_SINE - sineWidth <= 2)
					enemy.takeDamage(this.getDamage());
				laser = new Laser(new Line2D.Double(this.getGameLocation(),
						enemy.getGameLocation()), sineWidth);
				return;
			}
		}
		laser = null;
	}

	/**
	 * Get Laser (object encoding line and width of line to draw)
	 * @return
	 */
	public Laser getLaser() {
		return laser;
	}

	/**
	 * Get the enum TowerType of this tower
	 * @return
	 */
	@Override
	public TowerType getTowerType() {
		return TowerType.LASER;
	}

	/**
	 * Set a slightly random reddish color for the tower
	 */
	private void setColor() {
		Random generator = new Random();
		int r = generator.nextInt(100);
		int g = generator.nextInt(100);
		int b = generator.nextInt(17);
		rColor = new Color(155 + r, g, b);
		rColor = rColor.darker();
		this.setBackground(rColor);
	}

}
