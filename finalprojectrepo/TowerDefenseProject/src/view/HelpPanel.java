package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * This panel displays the rules of the game.
 * 
 * @author lamecfletez
 * 
 */
public class HelpPanel extends JPanel {
	private JButton backMenu;
	private JTextArea textSpot = new JTextArea(helpString);
	private JScrollPane scroller;
	private static final String helpString = "Game Rules:\n\n"
			+ "Game start rules:\n"
			+ "       A player always starts with 10 lives.\n"
			+ "       The game grid is clear of objects, and paths are visible to the player.\n"
			+ "       The player starts with a default currency amount to allow initial building of towers. In multiplayer, players can also buy waves to attack the other player (these are sent automatically in rounds in the single player version).\n\n"
			+ "Game end rules:\n"
			+ "       The Player loses the game if that player loses all lives.\n"
			+ "       In single-player, the player wins the game at the end of the 10th wave, if 800 enemies are killed.\n"
			+ "       In multiplayer, the player must defeat the opponent or, if one player quits, that player forfeits. If at any point in the game there are no enemies in play and neither player has sufficient funds to send a wave of enemies to the other, the game ends in a tie.\n\n"
			+ "Wave rules:\n"
			+ "       In single-player, each wave of enemies starts with a timer counting down until the wave begins and enemies flow. The number of enemies in a round increases as the player progresses through the waves. Players are encouraged to use the time between rounds to place towers effectively.\n"
			+ "       In multi-player, each player can spawn any of three waves, each comprised of all three types of enemies. Both players will be responsible for spawning waves.(Here, \"waves\" refers to the rounds of enemies in single-player mode). Spawned waves will appear on the other player's map as the enemies that they need to kill.\n\n"
			+ "Path rules:\n"
			+ "       The path is visible to the player at all times.\n"
			+ "       The path may fork.\n"
			+ "       The path is tied to the difficulty level--easy has no fork, hard does. In multiplayer, map choice (indicated by difficulty level) is first come, first serve, with the second player just joining the game.\n\n"
			+ "Enemy rules:\n"
			+ "       Enemies are restricted to moving along the path\n"
			+ "       Enemies emerge on the left and proceed, for the most part, towards the right.\n"
			+ "       When an enemy reaches the end of the path, the player loses a life.\n"
			+ "       If a fork in the path is encountered the enemy randomly determines which branch is followed.\n"
			+ "       Enemies have health. If that health is reduced to 0, then the enemy is destroyed.\n"
			+ "       When an enemy is destroyed, the Player earns money depending on the enemy's value.\n"
			+ "       Enemies have a predetermined movement speed, which determines how quickly they move along the path.\n"
			+ "       There are three different types of enemies. The yellow enemy is the weakest and slowest of the three with a total of 10 health points and 5 speed. The blue enemy is stronger and faster with 15 health points and 6 speed. The pink enemy is the strongest and fastest of the three with 20 health points and 7 speed.\n\n"
			+ "Tower rules:\n"
			+ "       Players can place any tower they have the money for at any time.\n"
			+ "       Only one tower can exist in a grid square at a time.\n"
			+ "       Players can place towers on any designated building grid square. Path squares are illegal, as are any which already have towers.\n"
			+ "       Towers have a strength which determines how much damage they deal to enemies. The Laser Tower causes 3 points of damage per tick. The Radiation Tower causes 1 point of damage to the five enemies in its range furthest along the path at each tick. The Ring Tower causes 1 point of damage to all enemies on its moving ring.\n"
			+ "       Towers have a cost, which determines how many of each tower the Player can construct.";

	/**
	 * HelpPanel constructor
	 * 
	 * @param returnToMainMenu
	 */
	public HelpPanel(ActionListener returnToMainMenu) {
		layoutGUI();
		registerListeners(returnToMainMenu);

	}

	/**
	 * Registers the backMenu listener. This takes the user back to the main
	 * menu panel.
	 * 
	 * @param returnToMainMenu
	 */
	private void registerListeners(ActionListener returnToMainMenu) {
		backMenu.addActionListener(returnToMainMenu);

	}

	/**
	 * Lays out the GUI.
	 */
	private void layoutGUI() {
		textSpot = new JTextArea(helpString);

		textSpot.setWrapStyleWord(true);
		textSpot.setLineWrap(true);

		textSpot.setVisible(true);
		textSpot.setEditable(false);
		textSpot.setSize(660, 520);

		scroller = new JScrollPane(textSpot);
		scroller.setPreferredSize(new Dimension(660, 520));
		this.add(scroller, BorderLayout.WEST);

		backMenu = new JButton(GameFrame.cardIdentifiers[GameFrame.MAIN_MENU]);
		backMenu.setBounds(500, 10, 100, 100);
		this.add(backMenu);

	}
}
