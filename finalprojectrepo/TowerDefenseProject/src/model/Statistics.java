package model;

import java.io.Serializable;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * This class stores all of the statistics information to be sent to the other
 * player over the server
 */
public class Statistics implements Serializable {
	private int gold;
	private int health;
	private int kills;
	private int wavesPurchased;

	/**
	 * this constructor initializes the statistics object
	 */
	public Statistics() {
		gold = 0;
		health = 0;
		kills = 0;
		wavesPurchased = 0;
	}

	/**
	 * this method adds the the gold total for the other player when they kill
	 * an enemy
	 * 
	 * @param goldOnDeath
	 */
	public void addGold(int goldOnDeath) {
		gold += goldOnDeath;
	}

	/**
	 * method sets the health based on the other players game
	 * 
	 * @param lives
	 */
	public void setHealth(int lives) {
		health = lives;
	}

	/**
	 * method adds to the kills when the other player kills an enemy sent to
	 * them
	 * 
	 * @param killsToPlayer
	 */
	public void addKills(int killsToPlayer) {
		kills += killsToPlayer;
	}

	/**
	 * method adds to waves sent for each wave purchased by the enenmy player
	 * 
	 * @param wavesSent
	 */
	public void setWavesSent(int wavesSent) {
		wavesPurchased = wavesSent;
	}

	/**
	 * returns the gold of the player on the other side of multiplayer
	 * 
	 */
	public int getGold() {
		return gold;
	}

	/**
	 * returns the health of the player on the other side of multi
	 * 
	 */
	public int getHealth() {
		return health;
	}

	/**
	 * This method returns the kills of the player on the other side of multi
	 */
	public int getKills() {
		return kills;
	}

	/**
	 * returns the number of waves the enemy purchased on the other side of
	 * multi
	 */
	public int getWavesSent() {
		return wavesPurchased;
	}

}
