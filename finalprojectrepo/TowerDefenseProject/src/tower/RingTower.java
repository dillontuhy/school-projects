package tower;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.List;
import java.util.Random;

import model.Clickable;
import observer.ClickableObserver;
import enemy.Enemy;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

public class RingTower extends Tower
{

	private float rangeAlpha;
	private int attackCounter;
	private Color gColor;
	private Ellipse2D.Double attackCircle, innerCircle;
	private int xCenter, yCenter;
	private int attackRingWidth = 8;

	/**
	 * Initialize ringtower instance variables
	 * @param clickObservers
	 */
	public RingTower(List<ClickableObserver> clickObservers)
	{
		super(Tower.GREEN_TOWER_TYPE, 44 * 4 + 22, 1, 50, clickObservers);
		//(int towerType, int rangeRadius, int damage, int price,
		this.setColor();
		rangeAlpha = 0.3f;
		attackCounter = 7 + (int) (Math.random() * 22);
		attackCircle = new Ellipse2D.Double(-80, -80, attackCounter * 2, attackCounter * 2);
		innerCircle = new Ellipse2D.Double(-80, -80, Math.max(0, (attackCounter - attackRingWidth) * 2), Math.max(0, (attackCounter - attackRingWidth) * 2));
	}

	/**
	 * Get new RadiationTower
	 * @param return
	 */
	@Override
	public Clickable getNewInstanceOf()
	{
		return new RingTower(getClickableObservers());
	}

	/**
	 * Get the alpha value the range should be drawn with
	 */
	@Override
	public float getRangeAlpha()
	{
		return rangeAlpha;
	}

	/**
	 * Get the ring the tower attacks with
	 * @return
	 */
	public Shape getAttackRing()
	{
		return attackCircle;
	}

	/**
	 * Change the radius of the attack ring as part of the tower's attack animation
	 */
	private void changeRing()
	{
		attackCounter += 3;

		attackCircle.width = (2 * attackCounter) % (this.getRange() * 2);
		attackCircle.height = (2 * attackCounter) % (this.getRange() * 2);
		attackCircle.x = xCenter - attackCircle.width / 2;
		attackCircle.y = yCenter - attackCircle.height / 2;

		innerCircle.width = Math.max(0, (attackCounter - attackRingWidth) * 2) % (this.getRange() * 2);
		innerCircle.height = Math.max(0, (attackCounter - attackRingWidth) * 2) % (this.getRange() * 2);
		innerCircle.x = xCenter - innerCircle.width / 2;
		innerCircle.y = yCenter - innerCircle.height / 2;
		
		if (this.getParent() != null)
			this.getParent().repaint();
	}

	/**
	 * Get string output of ringtower
	 * @param return
	 */
	@Override
	public String toString()
	{
		return "Ring" + super.toString();
	}

	/**
	 * Search through the list and attack enemies based on internal strategy
	 * @param enemies
	 */
	@Override
	public void detectEnemies(List<Enemy> enemies)
	{
		changeRing();

		for (Enemy enemy : enemies)
		{
			if (attackCircle.contains(enemy.getGameLocation()) && !innerCircle.contains(enemy.getGameLocation()))
			{
				enemy.takeDamage(this.getDamage());
			}
		}
	}

	/**
	 * Get the enum TowerType of this tower
	 * @return
	 */
	@Override
	public TowerType getTowerType()
	{
		return TowerType.RING;
	}

	/**
	 * Set a slightly random greenish color for the tower
	 */
	public void setColor()
	{
		Random generator = new Random();
		int r = generator.nextInt(17);
		int b = generator.nextInt(135);//just flipped blue and green numbers
		int g = generator.nextInt(100);
		gColor = new Color(r, g + 155, b);
		this.setBackground(gColor);
	}

	/**
	 * Overrides Tower's setGameLocation to extract the point
	 * to center the attack circle around
	 */
	@Override
	public void setGameLocation(Point location)
	{
		super.setGameLocation(location);
		xCenter = location.x;
		yCenter = location.y;
	}

}
