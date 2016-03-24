package map;

import java.awt.Color;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;


import observer.ClickableObserver;
import tower.TowerType;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * This class is the mini map panel, used only in multiplayer.
 *
 */
public class MiniMapPanel extends JPanel {
	private MapPanel miniMap;
	private boolean isMultiplayer;
	private int mapInt;

	public MiniMapPanel(boolean multiplayer,
			List<ClickableObserver> clickObservers, int mapInt) {
		isMultiplayer = multiplayer;
		this.mapInt = mapInt;
		layoutGUI(clickObservers);
	}
/**
 * this method determines if the game is single or multiplayer.
 * @return
 */
	public boolean gameIsMultiplayer() {
		return isMultiplayer;
	}
/**
 * this method lays out the gui overhead for the panel
 * @param clickObservers
 */
	private void layoutGUI(List<ClickableObserver> clickObservers) {
		this.setLayout(null);
		this.setOpaque(true);
		this.setBackground(Color.RED);
		this.setBounds(5, 62, 285, 285 / 15 * 12);

		if (isMultiplayer) {
			miniMap = new MapPanel(clickObservers, mapInt);
			miniMap.setBounds(0, 0, 285, 285 / 15 * 12);
			this.add(miniMap);
		}
	}
/**
 * this method places the towers on the minimap
 * @param t
 * @param xIndex
 * @param yIndex
 */
	public void updateMinimap(TowerType t, int xIndex, int yIndex) {
		miniMap.placeMiniMapTower(t, xIndex, yIndex);
		this.validate();
		this.getParent().repaint();
	}
}
