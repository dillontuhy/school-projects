//Dillon Tuhy, Lamec Angel Gabriel Fletez Reyes, Laura Vonessen, Martin Stankard

package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import observer.WumpObservable;
import observer.WumpObserver;
/**
 * This class is the model for the Hunt the Wumpus game
 */
public class Game implements WumpObservable {
	private RoomCollection ourGame;
	private Room currentRoom;
	private int arrows;
	private boolean isDead, hasWon;
	private List<Integer> intList;
	private List<WumpObserver> observers;
	private String recentStrategy, causeOfDeath, newGameString = "";
	private String arrowString = "";

	/**
	 * This constructor initializes instance variables and sets up
	 * a new game.
	 */
	public Game() {
		ourGame = new RoomCollection();
		isDead = false;
		hasWon = false;
		observers = new ArrayList<WumpObserver>();
		newGame();
	}

	/**
	 * This method resets the level
	 */
	public void resetLevel() {
		if (newGameString.length() == 0)
			newGameString = "You have now restarted the level.\n\n";
		hasWon = false;
		isDead = false;
		arrows = 3;
		ourGame.getRoom(intList.get(0)).setRoomStrategy(new BatStrategy(this));
		ourGame.getRoom(intList.get(1)).setRoomStrategy(new BatStrategy(this));
		ourGame.getRoom(intList.get(2)).setRoomStrategy(
				new WumpusStrategy(this));
		ourGame.getRoom(intList.get(3)).setRoomStrategy(new PitStrategy(this));
		ourGame.getRoom(intList.get(4)).setRoomStrategy(new PitStrategy(this));

		moveRoom(intList.get(5));

		recentStrategy = "";
	}

	/**
	 * This method starts a new game
	 */
	public void newGame() {
		newGameString = "You have now started a new game.\n\n";
		for (int i = 1; i < 21; i++) {
			ourGame.getRoom(i).setRoomStrategy(new BlankStrategy());
		}

		intList = new ArrayList<Integer>();
		for (int i = 1; i <= 20; i++) {
			intList.add(i);
		}
		Collections.shuffle(intList);

		resetLevel();
	}

	/**
	 * This method initializes a hard-coded test game
	 */
	public void testValues() {
		newGameString = "You are now in the hard-coded test game.\n\n";

		for (int i = 1; i < 21; i++) {
			ourGame.getRoom(i).setRoomStrategy(new BlankStrategy());
		}

		intList = new ArrayList<Integer>();

		intList.add(1);//bat room
		intList.add(11);//bat room
		intList.add(16);//wumpus
		intList.add(19);//pits
		intList.add(7);//pits
		intList.add(2);//initial room

		resetLevel();
	}

	/**
	 * This method initializes the other hard-coded test game
	 */
	public void test2Values() {
		newGameString = "You are now in the second hard-coded test game.\n\n";

		for (int i = 1; i < 21; i++) {
			ourGame.getRoom(i).setRoomStrategy(new BlankStrategy());
		}

		intList = new ArrayList<Integer>();

		intList.add(1);//bat room
		intList.add(11);//bat room
		intList.add(17);//wumpus
		intList.add(19);//pits
		intList.add(7);//pits
		intList.add(18);//initial room

		resetLevel();
	}

	/**
	 * This method validates if the hunter can move to chosen room before it moves
	 */
	public boolean canMove(int i) {
		return currentRoom.hasNeighbor(i) && !isDead();
	}

	/**
	 * This method returns the cave numbers of the neighbors of the current room.
	 */
	public int[] getCurrentNeighbors() {
		return getNeighbors(currentRoom.getRoomNumber());
	}

	/**
	 * This method gets the neighbors of the current room the hunter is in.
	 */
	private int[] getNeighbors(int i) {
		return ourGame.getRoom(i).getNeighbors();
	}

	/**
	 * This method converts a string to an arrow path.
	 * @param path The arrow path.
	 * @return Error message if the string isn't valid.
	 */
	public String shootArrow(String path) {
		setArrowString(path);

		if (isDead()) {
			return "Dead people can't shoot.\n\n";
		}

		Scanner scan = new Scanner(path);

		//parse arrows
		int[] arrowArray = new int[5];

		for (int i = 0; i < 5; i++) {
			if (scan.hasNext()) {
				try {
					arrowArray[i] = scan.nextInt();
				} catch (Exception e) {
					scan.close();
					return "Invalid input.  Please put in only integers, and only separate them with spaces.\n\n";
				}
			}
		}

		if (scan.hasNext()) {
			scan.close();
			return "Invalid input.  Please do not put more than 5 integers.\n\n";
		}

		shootArrow(arrowArray);
		scan.close();
		return "";
	}

	/**
	 * This method takes in an array of cave rooms
	 * that is the path for an arrow;
	 * @return true if you shoot the wumpus
	 */
	public boolean shootArrow(int[] path) {

		//die if no arrows
		if (arrows <= 0) {
			causeOfDeath = "You ran out of arrows in room ";
			kill();
			notifyObservers();
			return false;
		}

		//lose arrow
		arrows--;

		//no path or not connected to current room
		if (path.length == 0 || !currentRoom.hasNeighbor(path[0])) {
			notifyObservers();
			return false;
		}
		//check if the arrow works
		for (int i = 0; i < path.length; i++) {
			//room has wumpus
			if (ourGame.getRoom(path[i]).hasWumpus()) {
				hasWon = true;
				notifyObservers();
				return true;
			}
			//room has self
			if (path[i] == getCurrentRoom()) {
				kill();
				causeOfDeath = "You shot yourself in room ";
				notifyObservers();
				return false;
			}
			//room not connected to next
			if (i < path.length - 1
					&& !(ourGame.getRoom(path[i]).hasNeighbor(path[i + 1]))) {
				notifyObservers();
				return false;
			}
		}

		//didn't hit the wumpus
		notifyObservers();
		return false;
	}

	/**
	 * @return The number of arrows left to shoot.
	 */
	public int getArrowsLeft() {
		return arrows;
	}

	/**
	 * This method moves the hunter to the requested room.
	 */
	public void moveRoom(int roomToMove) {
		if (isDead) {
			return;
		}
		currentRoom = ourGame.getRoom(roomToMove);
		recentStrategy = currentRoom.doStrategy();
		smellsSightsandFeels();

		if (isDead) {
			setCauseOfDeath(recentStrategy);
		}
		notifyObservers();
		newGameString = "";
	}

	/**
	 * This method is exactly like moveRoom, but it doesn't notify observers in 
	 * case the room the bats move the hunter to also has a strategy.
	 * @param roomToMove
	 */
	public void batRelocator(int roomToMove) {
		//difference: doesn't notify observers because you're not done
		if (isDead) {
			return;
		}
		currentRoom = ourGame.getRoom(roomToMove);
		recentStrategy = currentRoom.doStrategy();
		smellsSightsandFeels();

		if (isDead) {
			setCauseOfDeath(recentStrategy);
		}
		
		newGameString = "";
	}

	/**
	 * This method returns hints about the hazards in neighboring rooms.
	 */
	public String smellsSightsandFeels() {
		String result = "";

		int[] neighbors = getCurrentNeighbors();

		if (ourGame.getRoom(neighbors[0]).hasBats()
				|| ourGame.getRoom(neighbors[1]).hasBats()
				|| ourGame.getRoom(neighbors[2]).hasBats()) {
			result += "You hear squeaking.\n";
		}
		if (ourGame.getRoom(neighbors[0]).hasPit()
				|| ourGame.getRoom(neighbors[1]).hasPit()
				|| ourGame.getRoom(neighbors[2]).hasPit()) {
			result += "You feel a draft.\n";
		}
		if (ourGame.getRoom(neighbors[0]).hasWumpus()
				|| ourGame.getRoom(neighbors[1]).hasWumpus()
				|| ourGame.getRoom(neighbors[2]).hasWumpus()) {
			result += "You smell something foul.\n";
		}
		return result;
	}

	/**
	 * This method kills the hunter.
	 */
	public void kill() {
		isDead = true;
	}

	/**
	 * This method checks if the hunter has died.
	 */
	public boolean isDead() {
		return isDead;
	}

	/**
	 * This method returns the index of the current room.
	 */
	public int getCurrentRoom() {
		return currentRoom.getRoomNumber();
	}

	/**
	 * This method checks Game if the user has won the game.
	 */
	public boolean hasWon() {
		return hasWon;
	}

	/**
	 * This method returns the state of the game.
	 * @return The state of the game.
	 */
	public String getState() {
		if (isDead()) {
			return "Oh no, you died!!! " + getCauseOfDeath() + getCurrentRoom()
					+ ".\n\n";
		}
		if (hasWon()) {
			return "You won!!! You shot the wumpus in room " + intList.get(2)
					+ ".\n\n";
		}
		if (recentStrategy == "" || recentStrategy == null) {
			return newGameString + "You're in room " + getCurrentRoom()
					+ " with the option to go to rooms "
					+ currentRoom.getNeighbors()[0] + ", "
					+ currentRoom.getNeighbors()[1] + ", and "
					+ currentRoom.getNeighbors()[2] + ".  Arrow count: "
					+ arrows + ".\n" + this.smellsSightsandFeels() + "\n";
		} else {
			return newGameString + recentStrategy + "\nYou're in room "
					+ getCurrentRoom() + " with the option to go to rooms "
					+ currentRoom.getNeighbors()[0] + ", "
					+ currentRoom.getNeighbors()[1] + ", and "
					+ currentRoom.getNeighbors()[2] + ".  Arrow count: "
					+ arrows + ".\n" + this.smellsSightsandFeels() + "\n";
		}
	}

	/**
	 * This method returns the location of all the important things
	 * in the game.
	 */
	public String toString() {
		String str = "";

		str += "Bats are in rooms " + intList.get(0) + " and " + intList.get(1)
				+ ".\n";
		str += "Pits are in rooms " + intList.get(3) + " and " + intList.get(4)
				+ ".\n";
		str += "Wumpus is in room " + intList.get(2) + ".\n";
		str += "Hunter is in room " + getCurrentRoom() + ".\n";

		return str;
	}
	
	/**
	 * This method is from WumpusObservable, it allows the view of the game 
	 * to observe it.
	 */
	@Override
	public void addObserver(WumpObserver observer) {
		observers.add(observer);
	}
	
	/**
	 * This method, from WumpusObservable, notifies all observers of the game.
	 */
	@Override
	public void notifyObservers() {
		for (WumpObserver o : observers)
			o.update();
	}

	/**
	 * This method returns the cause of death.
	 * @return Cause of death.
	 */
	public String getCauseOfDeath() {
		return causeOfDeath;
	}
	
	/**
	 * This sets the cause of death.
	 * @param causeOfDeath The cause of death.
	 */
	private void setCauseOfDeath(String causeOfDeath) {
		this.causeOfDeath = causeOfDeath;
	}
	
	/**
	 * This sets the arrow string (the input from the views)
	 * @param str The arrow path
	 */
	public void setArrowString(String str) {
		arrowString = str;
	}

	/**
	 * This gets the arrow string.
	 * @return The arrow string.
	 */
	public String getArrowString() {
		return arrowString;
	}
}
