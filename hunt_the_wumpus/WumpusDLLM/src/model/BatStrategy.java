//Dillon Tuhy, Lamec Angel Gabriel Fletez Reyes, Laura Vonessen, Martin Stankard
package model;

/**
 * This class is part of our overall strategy design pattern which implements our RoomStrategy
 * interface.
 *
 */

public class BatStrategy implements RoomStrategy {

	private Game currentGame;

	public BatStrategy(Game currentGame) {
		this.currentGame = currentGame;
	}

	/**This method does the strategy for this type of room, a room with bats in it.
	 * 
	 * There is a 1 in 4 chance that the hunter can enter this room and nothing will happen.  The other 75 %
	 * of the time the hunter is transported to another random room.  Either case returns different strings.
	 */
	public String doStrategy() {//teleport to a random room 1-20

		if (!(((int) (Math.random() * 4) + 1) == 1)) {

			int randomRoom = (int) (Math.random() * 20) + 1;
			while (randomRoom == currentGame.getCurrentRoom()) { 

				randomRoom = (int) (Math.random() * 20) + 1;
			}

			currentGame.batRelocator(randomRoom);
			
			if (!currentGame.isDead())
				return "The Bats got a hold of you...";
			else
				return "The Bats got a hold of you...\n"+currentGame.getCauseOfDeath();
		}
		return "The Bats are here, and they left you alone.";
	}
}
