package model;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import observer.ClickableObserver;
import enemy.BigEnemy;
import enemy.BossEnemy;
import enemy.Enemy;
import enemy.SmallEnemy;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */
/**
 * This class is part of the store used to send the waves these are the
 * clickable objects inside the store
 * 
 */
public class WaveClickable extends Clickable {
	private Wave wave;
	private int price;
	private Rectangle smallE, bigE, bossE;
	private static final Enemy small = new SmallEnemy(null, null),
			big = new BigEnemy(null, null), boss = new BossEnemy(null, null);

	/**
	 * This method lets know of an item clicked and allows the user to place
	 * towers and click on items to notify of information
	 * 
	 * @param clickObservers
	 *            , wave, price
	 */
	public WaveClickable(List<ClickableObserver> clickObservers, Wave wave,
			int price) {
		super(clickObservers);
		this.wave = wave;
		this.price = price;
		// setPrice();
		smallE = new Rectangle(0, 0, 50, 50);
		bigE = new Rectangle(0, 0, 50, 50);
		bossE = new Rectangle(0, 0, 50, 50);
		this.setBorder(this.getDefaultBorder());
		this.setSelectable(true);
		this.setHoverable(true);
	}

	/**
	 * Returns a new instance of the clickable
	 */
	@Override
	public Clickable getNewInstanceOf() {
		return new WaveClickable(this.getClickableObservers(), wave, price);
	}

	/**
	 * returns the price of this clickable
	 */
	@Override
	public int getPrice() {
		return price;
	}

	/**
	 * returns a string representation of this clickable
	 */
	@Override
	public String toString() {
		return wave.toString() + "\nPrice: " + price;
	}

	/**
	 * unused
	 */
	@Override
	public Point getGameLocation() {
		return null;
	}

	/**
	 * paints the clickable depending on the type of enemies inside the
	 * clickable
	 * 
	 * @param g
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		setX();
		g2.setColor(small.getEnemyColor());
		g2.fill(smallE);
		g2.setColor(big.getEnemyColor());
		g2.fill(bigE);
		g2.setColor(boss.getEnemyColor());
		g2.fill(bossE);
	}

	/**
	 * returns a wave
	 */
	public Wave getWave() {
		return wave;
	}

	/**
	 * sets up the colored rectangles which identify the wave
	 */
	private void setX() {
		bigE.x = this.getWidth() / 3;
		bossE.x = 2 * this.getWidth() / 3;
	}
}
