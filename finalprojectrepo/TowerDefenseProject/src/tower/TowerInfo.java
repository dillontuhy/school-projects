package tower;

import java.io.Serializable;

/**
 * Class containing information necessary to make duplicate minimap tower
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */
public class TowerInfo implements Serializable {
	private TowerType t;
	private int xIndex, yIndex;

	/**
	 * Initialize TowerInfo instance variables
	 * @param t
	 * @param xIndex
	 * @param yIndex
	 */
	public TowerInfo(TowerType t, int xIndex, int yIndex) {
		this.t = t;
		this.xIndex = xIndex;
		this.yIndex = yIndex;
	}

	/**
	 * Get the tower's enum TowerType
	 * @return
	 */
	public TowerType getTowerType() {
		return t;
	}

	/**
	 * Get the tower's x index on the map
	 * @return
	 */
	public int getXIndex() {
		return xIndex;
	}

	/**
	 * Get the tower's y index on the map
	 * @return
	 */
	public int getYIndex() {
		return yIndex;
	}
}
