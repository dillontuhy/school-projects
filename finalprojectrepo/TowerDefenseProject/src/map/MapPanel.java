package map;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import model.Clickable;
import observer.ClickableObserver;
import tower.LaserTower;
import tower.RadiationTower;
import tower.RingTower;
import tower.Tower;
import tower.TowerType;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * 
 * 
 *
 */
public class MapPanel extends JPanel {

	private Clickable[][] tileCollection;
	private List<ClickableObserver> clickableObservers;
	private final int numXTiles = 15, numYTiles = 12;
	private List<Point> path, pathTwo;

	
/**
 * this method chooses which map is used
 * @param clickObservers
 * @param i
 */
	public MapPanel(List<ClickableObserver> clickObservers, int i) {
		clickableObservers = clickObservers;
		path = new ArrayList<Point>();
		pathTwo = new ArrayList<Point>();
		tileCollection = new Clickable[numYTiles][numXTiles];
		createMap();
		if (i == 1) {
			createMapOne();
		} else {
			createMapTwo();
		}
		layoutGui();
		this.setOpaque(false);
	}

	/**
	 * makes all of the map components interactable with the mouse on hover
	 * @param hoverable
	 */
	public void setAllTilesHoverable(boolean hoverable) {
		for (int i = 0; i < numYTiles; i++) {
			for (int k = 0; k < numXTiles; k++) {
				tileCollection[i][k].setHoverable(hoverable);
			}
		}
	}
/**
 * assigns map gui dimensions to the panel
 */
	private void layoutGui() {

		setLayout(new GridLayout(numYTiles, numXTiles, 0, 0));
		this.setPreferredSize(new Dimension(660, 528));
		this.setBounds(0, 0, 660, 528);

		addMapToPanel();

	}
	
/**
 * this method empties, then assigns tiles to the entire map.
 */
	
	private void addMapToPanel() {
		this.removeAll();

		for (int i = 0; i < numYTiles; i++) {
			for (int k = 0; k < numXTiles; k++) {
				this.add(tileCollection[i][k]);
			}
		}
	}
	
/**
 * This method initializes all of the tiles
 */
	
	private void createMap() {
		for (int k = 0; k < numYTiles; k++) {
			for (int i = 0; i < numXTiles; i++) {
				tileCollection[k][i] = new Tile(k, i, clickableObservers);// y,x,clickableObservers
				((Tile) tileCollection[k][i]).setType("Open");
			}
		}
	}
/**
 * This method serves as conductor, ushering enemies either left or right
 * @param i
 * @return
 */
	public List<Point> getPath(int i) {
		switch (i) {
		case 0:
			return path;
		default:
			return pathTwo;
		}
	}

	/**Rohit, this was our first straight line path...leaving it here only in case 
	 * you decide to tinker with something or incase you need to see something
	 * noving cool
	 * 
	 */
	
	private void createStraightPath() {
		for (int j = 0; j < 15; j++) {
			((Tile) tileCollection[6][j]).setType("Path");
		}

		path.add(new Point(-44, 6 * 44 + 22));
		for (int i = 0; i < 670; i++) {
			path.add(new Point(1, 0));
		}
	}

	/**
	 * this method creates the more difficult map 
	 */
	private void createMapTwo() {
		path.add(new Point(-44, 6 * 44 + 22));
		pathTwo.add(new Point(-44, 6 * 44 + 22));
		for (int i = 0; i < 22; i++) {
			path.add(new Point(1, 0));// x,y
			pathTwo.add(new Point(1, 0));
		}// getting onto the screen

		// segment 1
		for (int j = 0; j < 3; j++) {// three tiles
			((Tile) tileCollection[6][j]).setType("Path");
			for (int i = 0; i < 44; i++) {// 44 pix in each tile
				path.add(new Point(1, 0));
				pathTwo.add(new Point(1, 0));
			}
		}//
		path.add(new Point(0, 0));// indicates a turn
		// segment 2
		for (int j = 6; j > 0; j--) {// start on 6th row, end on the first
			((Tile) tileCollection[j][2]).setType("Path");
			if (j != 1)
				for (int i = 0; i < 44; i++) {
					path.add(new Point(0, -1));// add to the vertical path
				}
		}
		path.add(new Point(0, 0));
		// segment 3
		for (int j = 3; j < 7; j++) {
			((Tile) tileCollection[1][j]).setType("Path");
			// if (j != 6)
			for (int i = 0; i < 44; i++) {
				path.add(new Point(1, 0));
			}
		}
		path.add(new Point(0, 0));
		// segment 4
		for (int j = 1; j < 9; j++) {
			((Tile) tileCollection[j][6]).setType("Path");
			if (j != 8)
				for (int i = 0; i < 44; i++) {
					path.add(new Point(0, 1));
				}
		}
		path.add(new Point(0, 0));
		// segment 5
		for (int j = 6; j < 11; j++) {
			((Tile) tileCollection[8][j]).setType("Path");
			if (j != 10)
				for (int i = 0; i < 44; i++) {
					path.add(new Point(1, 0));
				}
		}
		path.add(new Point(0, 0));
		// segment 6
		for (int j = 8; j > 0; j--) {
			((Tile) tileCollection[j][10]).setType("Path");
			if (j != 1)
				for (int i = 0; i < 44; i++) {
					path.add(new Point(0, -1));
				}
		}

		path.add(new Point(0, 0));
		// segment 7
		for (int j = 10; j < 14; j++) {
			((Tile) tileCollection[1][j]).setType("Path");
			if (j != 13)
				for (int i = 0; i < 44; i++) {
					path.add(new Point(1, 0));
				}
		}

		path.add(new Point(0, 0));
		// segment 4
		for (int j = 1; j < 6; j++) {
			((Tile) tileCollection[j][13]).setType("Path");
			if (j != 5)
				for (int i = 0; i < 44; i++) {
					path.add(new Point(0, 1));
				}
		}

		path.add(new Point(0, 0));
		// segment 7
		for (int j = 13; j < 16; j++) {
			if (j >= 15) {
				for (int i = 0; i < 44; i++) {
					path.add(new Point(1, 0));
				}
			} else {
				((Tile) tileCollection[5][j]).setType("Path");
				for (int i = 0; i < 44; i++) {
					path.add(new Point(1, 0));
				}
			}
		}
		// Start of pathTwo
		pathTwo.add(new Point(0, 0));
		// segment 2
		for (int j = 6; j > 2; j--) {
			((Tile) tileCollection[j][2]).setType("Path");
			if (j != 3)
				for (int i = 0; i < 44; i++) {
					pathTwo.add(new Point(0, -1));
				}
		}
		pathTwo.add(new Point(0, 0));
		// segment 3
		for (int j = 2; j < 5; j++) {
			((Tile) tileCollection[3][j]).setType("Path");
			if (j != 4)
				for (int i = 0; i < 44; i++) {
					pathTwo.add(new Point(1, 0));
				}
		}

		pathTwo.add(new Point(0, 0));
		// segment 4
		for (int j = 3; j < 9; j++) {
			((Tile) tileCollection[j][4]).setType("Path");
			if (j != 8)
				for (int i = 0; i < 44; i++) {
					pathTwo.add(new Point(0, 1));
				}
		}

		pathTwo.add(new Point(0, 0));
		// segment 5
		for (int j = 4; j > 0; j--) {
			((Tile) tileCollection[8][j]).setType("Path");
			if (j != 1)
				for (int i = 0; i < 44; i++) {
					pathTwo.add(new Point(-1, 0));
				}
		}

		// Start of pathTwo!!
		pathTwo.add(new Point(0, 0));
		// segment 6
		for (int j = 8; j < 11; j++) {
			((Tile) tileCollection[j][1]).setType("Path");
			if (j != 10)
				for (int i = 0; i < 44; i++) {
					pathTwo.add(new Point(0, 1));
				}
		}
		// Start of pathTwo
		pathTwo.add(new Point(0, 0));
		// segment 7
		for (int j = 1; j < 14; j++) {
			((Tile) tileCollection[10][j]).setType("Path");
			if (j != 13)
				for (int i = 0; i < 44; i++) {
					pathTwo.add(new Point(1, 0));
				}
		}
		// Start of pathTwo
		pathTwo.add(new Point(0, 0));
		// segment 8
		for (int j = 10; j > 7; j--) {
			((Tile) tileCollection[j][13]).setType("Path");
			if (j != 8)
				for (int i = 0; i < 44; i++) {
					pathTwo.add(new Point(0, -1));
				}
		}

		// Start of pathTwo
		pathTwo.add(new Point(0, 0));
		// segment 9
		for (int j = 13; j > 11; j--) {
			((Tile) tileCollection[8][j]).setType("Path");
			if (j != 12)
				for (int i = 0; i < 44; i++) {
					pathTwo.add(new Point(-1, 0));
				}
		}
		// Start of pathTwo
		pathTwo.add(new Point(0, 0));
		// segment 8
		for (int j = 8; j > 4; j--) {
			((Tile) tileCollection[j][12]).setType("Path");
			if (j != 5)
				for (int i = 0; i < 44; i++) {
					pathTwo.add(new Point(0, -1));
				}
		}

		pathTwo.add(new Point(0, 0));
		// segment 7
		for (int j = 13; j < 16; j++) {
			if (j >= 15) {
				for (int i = 0; i < 44; i++) {
					pathTwo.add(new Point(1, 0));
				}
			} else {
				((Tile) tileCollection[5][j]).setType("Path");
				if (j != 16)
					for (int i = 0; i < 44; i++) {
						pathTwo.add(new Point(1, 0));
					}
			}
		}

	}
/**
 * This method creates the other portion of the higher difficulty map, or just the easier map
 */
	private void createMapOne() {
		path.add(new Point(-44, 6 * 44 + 22));
		for (int i = 0; i < 22; i++) {
			path.add(new Point(1, 0));// x,y
		}// getting onto the screen

		// segment 1
		for (int j = 0; j < 3; j++) {// three tiles
			((Tile) tileCollection[6][j]).setType("Path");
			for (int i = 0; i < 44; i++) {// 44 pix in each tile
				path.add(new Point(1, 0));
			}
		}//
		path.add(new Point(0, 0));// indicates a turn
		// segment 2
		for (int j = 6; j > 0; j--) {// start on 6th row, end on the first
			((Tile) tileCollection[j][2]).setType("Path");
			if (j != 1)
				for (int i = 0; i < 44; i++) {
					path.add(new Point(0, -1));// add to the vertical path
				}
		}
		path.add(new Point(0, 0));
		// segment 3
		for (int j = 3; j < 7; j++) {
			((Tile) tileCollection[1][j]).setType("Path");
			// if (j != 6)
			for (int i = 0; i < 44; i++) {
				path.add(new Point(1, 0));
			}
		}
		path.add(new Point(0, 0));
		// segment 4
		for (int j = 1; j < 9; j++) {
			((Tile) tileCollection[j][6]).setType("Path");
			if (j != 8)
				for (int i = 0; i < 44; i++) {
					path.add(new Point(0, 1));
				}
		}
		path.add(new Point(0, 0));
		// segment 5
		for (int j = 6; j < 11; j++) {
			((Tile) tileCollection[8][j]).setType("Path");
			if (j != 10)
				for (int i = 0; i < 44; i++) {
					path.add(new Point(1, 0));
				}
		}
		path.add(new Point(0, 0));
		// segment 6
		for (int j = 8; j > 0; j--) {
			((Tile) tileCollection[j][10]).setType("Path");
			if (j != 1)
				for (int i = 0; i < 44; i++) {
					path.add(new Point(0, -1));
				}
		}

		path.add(new Point(0, 0));
		// segment 7
		for (int j = 10; j < 14; j++) {
			((Tile) tileCollection[1][j]).setType("Path");
			if (j != 13)
				for (int i = 0; i < 44; i++) {
					path.add(new Point(1, 0));
				}
		}

		path.add(new Point(0, 0));
		// segment 4
		for (int j = 1; j < 6; j++) {
			((Tile) tileCollection[j][13]).setType("Path");
			if (j != 5)
				for (int i = 0; i < 44; i++) {
					path.add(new Point(0, 1));
				}
		}

		path.add(new Point(0, 0));
		// segment 7
		for (int j = 13; j < 16; j++) {
			if (j >= 15) {
				for (int i = 0; i < 44; i++) {
					path.add(new Point(1, 0));
				}
			} else {
				((Tile) tileCollection[5][j]).setType("Path");
				if (j != 16)
					for (int i = 0; i < 44; i++) {
						path.add(new Point(1, 0));
					}
			}
		}
	}

	/**
	 * this method, given the proper tile coordinates and type of tower.
	 * @param tile
	 * @param tower
	 */
	public void placeTower(Tile tile, Tower tower) {
		tileCollection[tile.Y_INDEX][tile.X_INDEX] = tower;
		tower.setGameLocation(tile.getGameLocation());
		tower.setSelected(false);
		addMapToPanel();
	}

	
	/**
	 * This method mirrors the placement of a tower on the main map, 
	 * with the placement on the minimap. 
	 * @param tower
	 * @param xIndex
	 * @param yIndex
	 */
	public void placeMiniMapTower(TowerType tower, int xIndex, int yIndex) {
		Tower t = newTower(tower);
		t.setTowerColorBrighter();
		placeTower((Tile)tileCollection[yIndex][xIndex],t);
	}

	/**
	 * this method differentiates the type of tower to be created by enums cases.
	 * @param tower
	 * @return
	 */
	private Tower newTower(TowerType tower) {
		switch(tower)
		{
		case RADIATION:
			return new RadiationTower(clickableObservers);
		case LASER:
			return new LaserTower(clickableObservers);
		case RING:
			return new RingTower(clickableObservers);
		}
		return null;

	}
}
