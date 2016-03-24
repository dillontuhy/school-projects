//Dillon Tuhy, Lamec Angel Gabriel Fletez Reyes, Laura Vonessen, Martin Stankard
package model;

import java.util.ArrayList;
import java.util.List;

/**
 * This class creates all of the rooms and places them in a List.
 * 
 */
public class RoomCollection {

	private List<Room> roomList;

	/**
	 * This constructor creates all of the rooms and places them in a List.
	 */
	public RoomCollection() {// the collection of rooms

		roomList = new ArrayList<Room>();
		//This 2 dimensional array is made up of all of the rooms neighbors.
		int[][] neighbors = {

		{ 20, 5, 2 }, { 1, 3, 10 }, { 4, 12, 2 }, { 5, 14, 3 }, { 6, 4, 1 },
				{ 7, 15, 5 }, { 20, 8, 6 }, { 7, 19, 16 }, { 20, 10, 19 },
				{ 2, 11, 9 }, { 12, 18, 10 }, { 3, 13, 11 }, { 14, 17, 12 },
				{ 4, 15, 13 }, { 6, 16, 14 }, { 15, 8, 17 }, { 16, 18, 13 },
				{ 17, 19, 11 }, { 8, 9, 18 }, { 7, 1, 9 } };

		for (int i = 0; i < 20; i++) {

			roomList.add(new Room(i + 1, neighbors[i]));
		}

	}

	/**
	 * This method gets the requested room 
	 */
	public Room getRoom(int i) {
		return roomList.get(i - 1);
	}

}
