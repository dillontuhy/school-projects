package model;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.Timer;

import map.MapCoordinator;
import enemy.EnemyType;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */
/**
 * This class is the wave sent across the server to the other player.s
 * 
 */

public class Wave implements Serializable {
	List<EnemyType> allEnemies;
	private int[] numEnemiesOfType;
	private Timer timer;
	private int timerCounter, totalNumEnemies;
	private MapCoordinator layer;

	/**
	 * The constructor initializes the number of enemies inside the wave
	 * 
	 * @param numSmallEnemies
	 *            , numBigEnemies, numBossEnemies
	 */
	public Wave(int numSmallEnemies, int numBigEnemies, int numBossEnemies) {
		numEnemiesOfType = new int[] { numSmallEnemies, numBigEnemies,
				numBossEnemies };
		createList();
		totalNumEnemies = allEnemies.size();
		timerCounter = 0;
		timer = new Timer(3500 / totalNumEnemies, new LaunchTimer());
	}

	/**
	 * This returns the enemy type
	 * 
	 * @param et
	 */
	public int getNumEnemiesOfType(EnemyType et) {
		switch (et) {
		case SMALL:
			return numEnemiesOfType[0];
		case BIG:
			return numEnemiesOfType[1];
		case BOSS:
			return numEnemiesOfType[2];
		}
		return -1;
	}

	/**
	 * This method creates all the enemies in a wave depending on the number of
	 * enemies specified by the button press
	 * 
	 */
	private void createList() {
		allEnemies = new ArrayList<EnemyType>();

		for (int i = 0; i < numEnemiesOfType[0]; i++) {
			allEnemies.add(EnemyType.SMALL);
		}
		for (int i = 0; i < numEnemiesOfType[1]; i++) {
			allEnemies.add(EnemyType.BIG);
		}
		for (int i = 0; i < numEnemiesOfType[2]; i++) {
			allEnemies.add(EnemyType.BOSS);
		}
		Collections.shuffle(allEnemies);
	}

	/**
	 * This method starts the wave timer running
	 */
	public void start() {
		timer = new Timer(3000 / totalNumEnemies, new LaunchTimer());
		timer.start();
	}

	/**
	 * adds the given layer panel to our class
	 * 
	 * @param layer
	 */
	public void setMapCoordinator(MapCoordinator layer) {
		this.layer = layer;
	}

	/**
	 * This class lauches enemies on the timer click until there are no enemies
	 * left to be launched
	 */
	private class LaunchTimer implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (timerCounter >= allEnemies.size()) {
				timer.stop();
				return;
			}
			layer.launchEnemy(allEnemies.get(timerCounter));

			timerCounter++;
		}

	}

	/**
	 * This method pauses the wave
	 */
	public void pause() {
		timer.stop();
	}

	/**
	 * This method resumes the wave timer
	 */
	public void resume() {
		if (timerCounter < allEnemies.size()) {
			timer.start();
		}
	}

	/**
	 * This method prints a string of all the enemies inside the wave based on
	 * type
	 */
	@Override
	public String toString() {
		return "Wave:\nSmall enemies: " + numEnemiesOfType[0]
				+ "\nBig enemies: " + numEnemiesOfType[1] + "\nBoss enemies: "
				+ numEnemiesOfType[2];
	}

	/**
	 * This method returns the total number of enemies in a way
	 */
	public double size() {
		return totalNumEnemies;
	}
}
