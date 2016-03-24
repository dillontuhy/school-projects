package model;

import gamepanel.StorePanel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import map.MapPanel;
import map.Tile;

import observer.CurrencyObservable;
import observer.CurrencyObserver;
import tower.Tower;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * This class is the Game. Sets up the model of the TD game.
 */
public class Game implements CurrencyObservable, Serializable {

	private MapPanel map;
	private List<Tower> towerOnMap;
	private boolean win, lose;
	private int playerLives;
	private int totalCurrency;
	private StorePanel store;
	private List<CurrencyObserver> currencyObservers;
	private int numWinPoints;
	private boolean isPaused;
	private boolean multiplayer, tied;

	/**
	 * Constructor initializes the variables for the Game
	 * 
	 * @param multiplayer
	 */
	public Game(boolean multiplayer) {
		playerLives = 10;
		this.multiplayer = multiplayer;
		if (multiplayer)
			totalCurrency = 300;
		else
			totalCurrency = 100;
		towerOnMap = new ArrayList<Tower>();
		currencyObservers = new ArrayList<CurrencyObserver>();
		win = false;
		lose = false;
		tied = false;
		numWinPoints = 0;
		isPaused = false;
	}

	/**
	 * sets the store info so that it can be viewed in the display panel.
	 * 
	 * @param store
	 */
	public void setStore(StorePanel store) {
		this.store = store;
	}

	/**
	 * returns if the store contains a Clickable c
	 * 
	 * @param c
	 */
	public boolean storeContains(Clickable c) {
		return store.contains(c);
	}

	/**
	 * sets the Map. 1 of 2 maps chosen in the Main Menu Panel
	 * 
	 * @param map
	 */
	public void setMap(MapPanel map) {
		this.map = map;
	}

	/**
	 * Lets the caller know if the player has won the game
	 */
	public boolean hasWon() {
		return win;
	}
	
	/**
	 * Lets the caller know if the player has tied the game
	 */
	public boolean hasTied() {
		return tied;
	}

	/**
	 * Lets the caller know if the player has lost the game
	 */
	public boolean hasLost() {
		return lose;
	}

	/**
	 * Lets the caller know if the game has ended/is over
	 */
	public boolean isOver() {
		return win || lose || tied;
	}

	/**
	 * Returns the currency
	 */
	public int getCurrency() {
		return totalCurrency;
	}

	/**
	 * This method sets the total currency.
	 * 
	 * @param currency
	 */
	public void setCurrency(int currency) {
		this.totalCurrency = currency;
		this.notifyObservers();
	}

	/**
	 * Returns the lives of the player
	 */
	public int getLives() {
		return playerLives;
	}

	/**
	 * sets the lives when called by the caller
	 * 
	 * @param lives
	 */
	public void setLives(int lives) {
		if (lives <= 0) {
			playerLives = 0;
			lose = true;
			System.out.println("You just lost.");
		} else {
			playerLives = lives;
		}
		this.notifyObservers();
	}

	/**
	 * This method places a tower for the caller on the map
	 * 
	 * @param tile
	 * @param tower
	 */
	public void placeTower(Tile tile, Tower tower) {
		map.placeTower(tile, tower);
		towerOnMap.add(tower);
	}

	/**
	 * This method sets the tiles as hoverable on the map
	 * 
	 * @param hoverable
	 */
	public void setTilesHoverable(boolean hoverable) {
		map.setAllTilesHoverable(hoverable);
	}

	/**
	 * Returning the list of towers in the game
	 */
	public List<Tower> getTowers() {
		return towerOnMap;
	}

	/**
	 * Method adds observer to the currencyObservers list
	 * 
	 * @param o
	 */
	@Override
	public void addObserver(CurrencyObserver o) {
		currencyObservers.add(o);
	}

	/**
	 * Goes through the currency observers list and updates the observer
	 */
	@Override
	public void notifyObservers() {
		for (CurrencyObserver o : currencyObservers)
			o.updateCurrency();
	}

	/**
	 * Increments the win point total
	 */
	public void addWinPoint() {
		this.numWinPoints++;
		if (numWinPoints >= 800 && !multiplayer) {
			win = true;
			System.out.println("You just won.");
		}
	}

	/**
	 * Returns the numWinPoints to the caller
	 */
	public int getKills() {
		return numWinPoints;
	}

	/**
	 * Method toggles pause in game
	 * 
	 * @param isPaused
	 */
	public void setPaused(boolean isPaused) {
		this.isPaused = isPaused;
	}

	/**
	 * Returns if game is paused
	 */
	public boolean isPaused() {
		return isPaused;
	}

	/**
	 * Returns if the game is multiplayer
	 */
	public boolean isMultiPlayer() {
		return this.multiplayer;
	}

	/**
	 * Sets if the game is tied
	 * 
	 * @param tied
	 */
	public void setTied(boolean tied) {
		this.tied = tied;
	}
	
	/**
	 * Sets if the game is won
	 * 
	 * @param win
	 */
	public void setWon(boolean win) {
		this.win = win;
	}
	
	/**
	 * Sets if the game is lost
	 * 
	 * @param lose
	 */
	public void setLost(boolean lose) {
		this.lose = lose;
	}
}
