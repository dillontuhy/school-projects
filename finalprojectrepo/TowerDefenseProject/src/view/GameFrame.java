package view;

import gamepanel.GamePanel;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import observer.GameConditionObserver;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * GameFrame class. Holds the different panels.
 * 
 */
public class GameFrame extends JFrame implements GameConditionObserver {
	public static final int X_DIM = 960, Y_DIM = 540;
	public static final int MAIN_MENU = 0, GAME_PANEL = 1, HELP_PANEL = 2,
			MULTI_PANEL = 3, COND_PANEL = 4;
	public static final String[] cardIdentifiers = { "Main Menu", "Game",
			"Help", "Multiplayer", "Lost" };
	private ActionListener panelSwitch;
	private static int consoleCounter = 0;

	private JPanel cardPanel;
	private MainMenuPanel main;
	private GamePanel resetGame;
	private GameConditionPanel condPane;

	/**
	 * GrameFrame constructor
	 */
	public GameFrame() {
		consoleCounter++;
		this.setTitle("Player " + consoleCounter);
		setUpListeners();
		layoutGUI();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowClosingListener());
		this.setAlwaysOnTop(true);
		this.setLocationRelativeTo(null);
		this.setLocation(this.getLocation().x - 3 * this.getLocation().x / 4,
				this.getLocation().y - 3 * this.getLocation().y / 4);
	}

	/**
	 * Sets up the panelSwith listener. This is used to change between panels.
	 */
	private void setUpListeners() {
		panelSwitch = new PanelChangeButtonListener();
	}

	/**
	 * lays out the GUI for the GameFrame. Adds card panels for each panel the
	 * user sees in the game.
	 */
	private void layoutGUI() {
		cardPanel = new JPanel();
		cardPanel.setPreferredSize(new Dimension(X_DIM, Y_DIM));
		cardPanel.setLayout(new CardLayout());
		// menu
		main = new MainMenuPanel(panelSwitch);
		cardPanel.add(main, cardIdentifiers[MAIN_MENU]);
		// game
		resetGame = new GamePanel(panelSwitch, this);
		cardPanel.add(resetGame, cardIdentifiers[GAME_PANEL]);
		// help
		cardPanel.add(new HelpPanel(panelSwitch), cardIdentifiers[HELP_PANEL]);
		// multi

		condPane = new GameConditionPanel(panelSwitch, resetGame);
		cardPanel.add(condPane, cardIdentifiers[COND_PANEL]);
		((CardLayout) cardPanel.getLayout()).show(cardPanel,
				cardIdentifiers[MAIN_MENU]);
		this.add(cardPanel);
	}

	/**
	 * This listener checks for matching text to tell it which panel to switch
	 * to.
	 * 
	 */
	private class PanelChangeButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			resetGame.pauseGamePanelSwitching();
			if (((JButton) e.getSource()).getText().equals("Easy")) {
				((CardLayout) cardPanel.getLayout()).show(cardPanel,
						cardIdentifiers[GAME_PANEL]);
				resetGame.setMapOne();
				resetGame.restart(false);
			} else if (((JButton) e.getSource()).getText().equals("Hard")) {
				((CardLayout) cardPanel.getLayout()).show(cardPanel,
						cardIdentifiers[GAME_PANEL]);
				resetGame.setMapTwo();
				resetGame.restart(false);
			}

			else if (((JButton) e.getSource()).getText().equals(
					cardIdentifiers[MULTI_PANEL])) {
				((CardLayout) cardPanel.getLayout()).show(cardPanel,
						cardIdentifiers[GAME_PANEL]);
				resetGame.repaint();
				resetGame.restart(true);
				return;
			} else if (((JButton) e.getSource()).getText().equals("Quit")) {
				((CardLayout) cardPanel.getLayout()).show(cardPanel,
						cardIdentifiers[MAIN_MENU]);
				return;
			}
			((CardLayout) cardPanel.getLayout()).show(cardPanel,
					((JButton) e.getSource()).getText());
		}
	}

	/**
	 * This method takes the condition of the game as an int. If it is anything
	 * other that 0, it will switch to the GameConditionPanel where it will
	 * there determine which image to display.
	 */
	@Override
	public void updateGameCondition(int condition) {
		condPane.updateStats(resetGame.getMapCoordinator().getGame());

		if (condition == 0)
			return;

		condPane.setImage(condition);
		((CardLayout) cardPanel.getLayout()).show(cardPanel,
				cardIdentifiers[COND_PANEL]);

	}

	/**
	 * This method returns main which will tell the panelSwitcher to switch to
	 * the main menu panel
	 * 
	 * @return main
	 */
	public MainMenuPanel getMenu() {
		return main;
	}

	/**
	 * This class quits the game and exits the program.
	 * 
	 * 
	 */
	class WindowClosingListener extends WindowAdapter {

		@Override
		public void windowClosing(WindowEvent arg0) {
			resetGame.quit();
			System.exit(0);
		}
	}
}
