//Dillon Tuhy, Lamec Angel Gabriel Fletez Reyes, Laura Vonessen, Martin Stankard
package view;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import model.Game;

/**This class coordinates our two views of the model.
 * It is also the only class with a main .
 */
@SuppressWarnings("serial")
public class Wumpus extends JFrame {
	/**
	 * This method sets up the jframe.
	 */
	public static void main(String[] args) {
		JFrame aWindow = new Wumpus();
		aWindow.pack();
		aWindow.setVisible(true);
		aWindow.setResizable(false);
	}

	private static JTabbedPane tabPane = new JTabbedPane();
	public static final int CONSOLE_INDEX = 0, ANIMATION_INDEX = 1;
	private Game wumpus = new Game();

	/**
	 * This method construstcs the GUI
	 */
	public Wumpus() {
		layoutGUI(); 

	}

	/**
	 * This method lets the program know which is the active tab.
	 */
	
	public static int getTabWithFocus() {
		return tabPane.getSelectedIndex();
	}
	
	/**
	 * This method lays out the GUI.
	 */
	private void layoutGUI() {
		this.setTitle("Hunt the Wumpus!!!");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLocation(75, 40);
		
		this.setIconImage((new ImageIcon("wumpus_monster/monster_006.png")).getImage());

		tabPane.addTab("Console", new ConsoleGUI(wumpus));
		tabPane.add("Animation View", new AnimGUI(wumpus));

		this.add(tabPane);
	}
	
	/**
	 * This method allows the tab pane to be disabled and re-enabled.
	 */
	public static void setTabsEnabled(boolean enabled) {
		tabPane.setEnabled(enabled);
	}
}