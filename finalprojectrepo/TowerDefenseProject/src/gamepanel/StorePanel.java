package gamepanel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import model.Clickable;
import model.Wave;
import model.WaveClickable;
import observer.ClickableObserver;
import tower.LaserTower;
import tower.RadiationTower;
import tower.RingTower;
import tower.Tower;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * StorePanel class. Displays items available for purchase.
 * 
 */
public class StorePanel extends JPanel {
	private List<Clickable> clickableList;
	private boolean multiplayer;

	/**
	 * StorePanel constructor
	 * 
	 * @param clickObservers
	 * @param multiplayer
	 */
	public StorePanel(List<ClickableObserver> clickObservers,
			boolean multiplayer) {
		clickableList = new LinkedList<Clickable>();
		this.multiplayer = multiplayer;
		layoutGUI(clickObservers);
	}

	/**
	 * Lays out the GUI of the store panel
	 * 
	 * @param clickObservers
	 */
	private void layoutGUI(List<ClickableObserver> clickObservers) {
		this.setPreferredSize(new Dimension(172, 118));

		this.setBackground(Color.GREEN);
		this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		this.setBounds(5 + 285 - 118, 532 - 236, 118, 172);

		this.setLayout(new GridLayout(3, 0, 10, 10));// always 3 rows

		clickableList.add(new LaserTower(clickObservers));
		clickableList.add(new RadiationTower(clickObservers));
		clickableList.add(new RingTower(clickObservers));

		if (multiplayer) {
			clickableList.add(new WaveClickable(clickObservers, new Wave(10,
					10, 10), 100));
			clickableList.get(3).add(new JLabel("Small"));
			clickableList.add(new WaveClickable(clickObservers, new Wave(30,
					30, 30), 270));
			clickableList.get(4).add(new JLabel("Medium"));
			clickableList.add(new WaveClickable(clickObservers, new Wave(50,
					50, 50), 375));
			clickableList.get(5).add(new JLabel("Large"));
		}

		for (Clickable c : clickableList) {
			this.add(c);
			if (c instanceof Tower)
				((Tower) c).setTowerColorBrighter();
		}

	}

	/**
	 * Checks clickable list for Clickable c's.
	 * 
	 * @param c
	 * @return clickableList.contains(c)
	 */
	public boolean contains(Clickable c) {
		return clickableList.contains(c);
	}

	/**
	 * This method returns the minumim waves' price.
	 * 
	 * @return 100
	 */
	public static int getMinWavePrice() {

		return 100;
	}
}
