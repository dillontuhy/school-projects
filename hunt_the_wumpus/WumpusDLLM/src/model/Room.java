//Dillon Tuhy, Lamec Angel Gabriel Fletez Reyes, Laura Vonessen, Martin Stankard
package model;
/**
 * This class is our basic room object, which contains all of the information a room needs.
 * 
 */
public class Room {
	private int roomNumber;
	private int[] roomNeighbor;
	private RoomStrategy strategy;

	/**This is the room constructor which initializes the instance variables
	 * 
	 */
	public Room(int roomNumber, int[] roomNeighbor) {

		this.roomNumber = roomNumber;
		this.roomNeighbor = roomNeighbor;

	}

	/**
	 * This method is called after the construction of a room and is used to assign that rooms specific strategy.
	 */
	public void setRoomStrategy(RoomStrategy masterStrategy) {
		strategy = masterStrategy;
	}

	/**
	 * This method retrieves the private variable, roomNumber.
	 */
	public int getRoomNumber() {
		return roomNumber;
	}

	/**
	 * This method retrieves the private variable, roomNeighbor.
	 */
	public int[] getNeighbors() {
		return roomNeighbor;
	}

	/**
	 * this method determines if a room is the neighbor or another room.
	 */
	public boolean hasNeighbor(int possibleNeighbor) {
		for (int j : roomNeighbor) {
			if (possibleNeighbor == j) {
				return true;
			}
		}
		return false;
	}

	/**
	 * this method carries out the behavior of the determined strategy.
	 */
	public String doStrategy() {
		return strategy.doStrategy();
	}

	/**
	 * This method is used to determine whether the room has bats 
	 */
	public boolean hasBats() {
		return strategy instanceof BatStrategy;
	}

	/**
	 * This method is used to determine whether the room has a pit
	 */
	public boolean hasPit() {
		return strategy instanceof PitStrategy;
	}

	/**
	 * This method is used to determine whether the room has a wumpus.
	 */
	public boolean hasWumpus() {
		return strategy instanceof WumpusStrategy;
	}
}
