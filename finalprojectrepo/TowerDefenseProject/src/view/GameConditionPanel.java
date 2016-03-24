package view;

import gamepanel.GamePanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.Timer;

import model.Game;

import observer.GameConditionObserver;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * GameConditionalPanel class. This is the panel shown when the game ends. Shows
 * either the Win image, loss image or tie image.
 * 
 */
public class GameConditionPanel extends JPanel {

	private BufferedImage currentImage, winImage, lossImage, tieImage;
	private JButton newGame;
	private boolean isLost;
	private String store;
	private JTextArea stats;
	private int imageInt;

	/**
	 * GameConditional constructor
	 * 
	 * @param panelSwitcher
	 * @param resetGame
	 */
	public GameConditionPanel(ActionListener panelSwitcher, GamePanel resetGame) {
		layoutGUI();
		registerListeners(panelSwitcher);
		loadImages();
	}

	/**
	 * Registers the newGame listener. Takes caller to main menu
	 */
	private void registerListeners(ActionListener returnToMainMenu) {
		newGame.addActionListener(returnToMainMenu);
	}

	/**
	 * lays out the GUI for the tie panel
	 */
	private void layoutGUI() {
		newGame = new JButton(GameFrame.cardIdentifiers[GameFrame.MAIN_MENU]);
		newGame.setBounds(10, 10, 175, 20);
		this.setLayout(null);
		stats = new JTextArea(store);
		stats.setSize(360, 320);
		stats.setBounds(10, 40, 175, 200);
		stats.setEditable(false);
		stats.setWrapStyleWord(false);
		this.add(stats);
		this.add(newGame);

	}

	/**
	 * Takes panelInt and sets the correct image for a win, loss or tie.
	 * 
	 * @param panelInt
	 */
	public void setImage(int panelInt) {
		this.imageInt = panelInt;
		if (imageInt == 1) {
			currentImage = lossImage;
		} else if (imageInt == 2) {
			currentImage = winImage;
		} else if (imageInt == 3) {
			currentImage = tieImage;
		}
		repaint();

	}

	/**
	 * loads image for GUI.
	 */
	private void loadImages() {
		String baseDir = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "Images"
				+ System.getProperty("file.separator");
		try {
			lossImage = ImageIO.read(new File(baseDir + "mercer.png"));
		} catch (IOException e) {
			System.out.println("Could not find Mercer ");
		}
		try {
			winImage = ImageIO.read(new File(baseDir + "win.png"));
		} catch (IOException e) {
			System.out.println("Could not find win");
		}
		try {
			tieImage = ImageIO.read(new File(baseDir + "tied.png"));
		} catch (IOException e) {
			System.out.println("can't find tie image");
		}

	}

	/**
	 * paintComponent draws the image in the panel.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gr = (Graphics2D) g;
		gr.drawImage(currentImage, 0, 0, null);
	}

	/**
	 * This method gets the previous stats and updates them for an up to date
	 * view when the panel is displayed.
	 * 
	 * @param game
	 */
	public void updateStats(Game game) {
		String lives = "";
		if (isLost) {
			lives = "0";
		} else {
			lives += game.getLives();
		}
		store = " Statistics:\n\n" + " Lives: " + lives + " \n\n" + " Kills: "
				+ game.getKills() + "\n\n" + " Gold: " + game.getCurrency()
				+ "\n\n" + " Towers Created: " + game.getTowers().size();
		stats.setText(store);

	}
}
