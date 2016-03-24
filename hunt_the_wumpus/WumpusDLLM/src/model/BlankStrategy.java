//Dillon Tuhy, Lamec Angel Gabriel Fletez Reyes, Laura Vonessen, Martin Stankard
package model;
/**
 * This class is part of our overall strategy design pattern which implements our RoomStrategy
 * interface.
 *
 */
public class BlankStrategy implements RoomStrategy {
	
	/**
	 * This method allows rooms with nothing special in them to have a strategy
	 */
	public String doStrategy() {
		return "";
	}

}
