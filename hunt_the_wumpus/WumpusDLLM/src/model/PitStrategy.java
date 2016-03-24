//Dillon Tuhy, Lamec Angel Gabriel Fletez Reyes, Laura Vonessen, Martin Stankard
package model;

/**
 * This class is part of our overall strategy design pattern which implements our RoomStrategy
 * interface.
 *
 */
public class PitStrategy implements RoomStrategy {

	private Game currentGame;

	public PitStrategy(Game currentGame) {
		this.currentGame = currentGame;
	}

	/**This method does the strategy for this type of room, a room with a pit in it.
	 * 
	 * If the hunter steps in this room, he dies.
	 */
	public String doStrategy() {
		currentGame.kill();
		return "There is a really deep pit here in room ";

	}

}
