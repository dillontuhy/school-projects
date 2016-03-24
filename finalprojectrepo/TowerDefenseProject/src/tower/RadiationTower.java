package tower;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;
import javax.swing.Timer;
import enemy.Enemy;
import model.Clickable;
import observer.ClickableObserver;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

public class RadiationTower extends Tower {

	private float rangeAlpha;
	private Timer radiationTimer;
	private int timerCounter;
	private Color bColor;

	/**
	 * Initialize radiationtower instance variables
	 * @param clickObservers
	 */
	public RadiationTower(List<ClickableObserver> clickObservers) {

		super(Tower.BLUE_TOWER_TYPE, 44 * 2 + 22, 1, 30, clickObservers);
		this.setColor();
		rangeAlpha = 0.5f;
		radiationTimer = new Timer(95 + (int) (Math.random() * 10),
				new RadiationTimerListener());
	}

	/**
	 * Get new RadiationTower
	 * @param return
	 */
	@Override
	public Clickable getNewInstanceOf() {
		RadiationTower rt = new RadiationTower(getClickableObservers());
		rt.startTimer();
		return rt;
	}

	/**
	 * Start the timer for the range animation
	 */
	private void startTimer() {
		radiationTimer.start();
		timerCounter = 0;
	}

	/**
	 * Get the alpha value the range should be drawn with
	 */
	@Override
	public float getRangeAlpha() {
		return rangeAlpha;
	}

	/**
	 * Change the alpha value of the range as part of the animation
	 */
	private void changeAlpha() {
		timerCounter++;
		rangeAlpha = (float) (.1 + .6 * Math.abs(Math.sin(Math.PI
				* timerCounter / 25.0)));
		if (this.getParent()!=null)
			this.getParent().repaint();
	}

	/**
	 * Get string output of radiationtower
	 * @param return
	 */
	@Override
	public String toString() {
		return "Radiation" + super.toString();
	}

	/**
	 * This inner class listens to the timer
	 */
	private class RadiationTimerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			changeAlpha();
		}

	}

	/**
	 * Search through the list and attack enemies based on internal strategy
	 * @param enemies
	 */
	@Override
	public void detectEnemies(List<Enemy> enemies) {
		int counter = 0;

		for (Enemy enemy : enemies) {
			if (getRangeShape().contains(enemy.getGameLocation())) {
				enemy.takeDamage(this.getDamage());
				counter++;
			}
			if (counter >= 3)
				break;
		}
	}

	/**
	 * Get the enum TowerType of this tower
	 * @return
	 */
	@Override
	public TowerType getTowerType() {
		return TowerType.RADIATION;
	}

	/**
	 * Set a slightly random bluish color for the tower
	 */
	public void setColor() {
		Random generator = new Random();
		int r = generator.nextInt(17);
		int g = generator.nextInt(135);
		int b = generator.nextInt(100);
		bColor = new Color(r, g, 155 + b);
		this.setBackground(bColor);
	}

}
