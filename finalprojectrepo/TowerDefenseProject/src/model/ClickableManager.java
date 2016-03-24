package model;

import java.io.Serializable;

import map.Tile;
import networking.GamePlayer;
import observer.ClickableObserver;
import observer.CurrencyObserver;
import tower.Tower;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 * 
 */

public class ClickableManager implements ClickableObserver, Serializable, CurrencyObserver {
	private Clickable previous, current;
	private Game game;
	private GamePlayer gp;
	private Statistics stats;

	/**
	 * Constructor initializes Clickable Manager variables
	 * 
	 * @param game, stats
	 */
	public ClickableManager(Game game, Statistics stats) {
		this.game = game;
		this.stats = stats;
	}

	/**
	 * returns the current item that is selected
	 */
	public Clickable getSelected() {
		if (current != null && current.isSelected())
			return current;
		else
			return null;
	}

	/**
	 * This method lets know of an item clicked and allows the user to place
	 * towers and click on items to notify of information
	 * 
	 * @param c
	 */
	@Override
	public void notifyOfClick(Clickable c) {
		if (current != null && current != c) {
			current.setSelected(false);
		}
		previous = current;
		current = c;
		if (canPlaceTower(current)) {
			Clickable newTower = previous.getNewInstanceOf();
			game.placeTower((Tile) current, (Tower) newTower);
			game.setCurrency(game.getCurrency() - previous.getPrice());
			stats.addGold(-previous.getPrice());

			current.setSelected(false);

			if (game.isMultiPlayer()) {

				gp.sendStats(stats);

				gp.notifyOfTowerPlacement(((Tower) newTower).getTowerType(),
						((Tile) current).X_INDEX, ((Tile) current).Y_INDEX);
			}

			game.setTilesHoverable(false);
		}

		game.setTilesHoverable(shouldSetTilesHoverable());
	}

	/**
	 * Makes tiles hoverable
	 */
	private boolean shouldSetTilesHoverable() {
		return current != null && (current instanceof Tower) && game.storeContains(current) && current.isSelected()
				&& !game.isPaused() && current.getPrice()<=game.getCurrency();
	}

	/**
	 * Takes information from game and lets user know if the Tower can be placed
	 * where clicked/selected on the map
	 * 
	 * @param c
	 */
	private boolean canPlaceTower(Clickable c) {
		return (current instanceof Tile) && ((Tile) current).canPlaceTower()
				&& current.isHoverable() // means prev is valid tower
				&& game.getCurrency() >= previous.getPrice()
				&& !game.isOver() && !game.isPaused();
	}

	/**
	 * set the gamePlayer. Either 1st player or 2nd
	 * 
	 * @param gp2
	 */
	public void setGamePlayer(GamePlayer gp2) {
		this.gp = gp2;
	}

	/**
	 * When currency is updated, checks if map tiles should be hoverable
	 */
	@Override
	public void updateCurrency() {
		game.setTilesHoverable(shouldSetTilesHoverable());
	}
}
