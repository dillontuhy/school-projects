package view;

import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * MainMenuPanel Class
 * 
 */
public class MainMenuPanel extends JPanel {
	private BufferedImage mainImage, mapChoiceImage, currentImage;
	private JButton singlePlayer;
	private JButton help;
	private JButton multi, easy, hard;

	/**
	 * MainMenuPanel constructor
	 * 
	 * @param panelSwitcher
	 */
	public MainMenuPanel(ActionListener panelSwitcher) {
		loadImages();
		layoutGUI();
		registerListeners(panelSwitcher);
	}

	/**
	 * Lays out the GUI. Makes new buttons for the panel and places them in
	 * their location.
	 */
	private void layoutGUI() {
		singlePlayer = new JButton(
				GameFrame.cardIdentifiers[GameFrame.GAME_PANEL]);
		singlePlayer.setBounds(525, 283, 100, 100);// x, y, width, height
		help = new JButton(GameFrame.cardIdentifiers[GameFrame.HELP_PANEL]);
		help.setBounds(671, 283, 100, 100);// x, y, width, height
		multi = new JButton(GameFrame.cardIdentifiers[GameFrame.MULTI_PANEL]);
		multi.setBounds(382, 283, 100, 100);// x, y, width, height
		easy = new JButton("Easy");
		easy.setBounds(382, 283, 100, 100);// x, y, width, height
		hard = new JButton("Hard");
		hard.setBounds(671, 283, 100, 100);// x, y, width, height

		this.setLayout(null);
		switchToMenu(true);
		this.setBackground(Color.BLACK);

		setInvisible(singlePlayer);
		setInvisible(help);
		setInvisible(multi);
		setInvisible(easy);
		setInvisible(hard);

	}

	/**
	 * This method is used to make buttons invisible at times when the game
	 * doesn't want them to be displayed so they do not overlap with other
	 * images/buttons/text.
	 * 
	 * @param button
	 */
	private void setInvisible(JButton button) {
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
		button.setFont(new Font("Arial", Font.PLAIN, 0));
	}

	/**
	 * Registers the listeners for the buttons that were created in the
	 * layoutGUI.
	 * 
	 * @param panelSwitcher
	 */
	private void registerListeners(ActionListener panelSwitcher) {
		ActionListener mapListener = new MapGameListener();
		singlePlayer.addActionListener(mapListener);
		help.addActionListener(panelSwitcher);
		multi.addActionListener(panelSwitcher);
		easy.addActionListener(panelSwitcher);
		hard.addActionListener(panelSwitcher);

		ActionListener al = new ReturnToMenuListener();
		easy.addActionListener(al);
		hard.addActionListener(al);
	}

	/**
	 * PaintComponent draws the image (current image) in the panel.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(currentImage, 0, 0, null);
	}

	/**
	 * loads the images for them to be used in the panel.
	 */
	private void loadImages() {
		String baseDir = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "Images"
				+ System.getProperty("file.separator");
		try {
			mainImage = ImageIO.read(new File(baseDir
					+ "mainmenuBackgroundPanel.png"));
		} catch (IOException e) {
			System.out.println("Could not find main menu");
		}
		try {
			mapChoiceImage = ImageIO.read(new File(baseDir
					+ "diffBackGroundPanel.png"));
		} catch (IOException e) {
			System.out.println("Could not find map choice image");
		}
		currentImage = mainImage;
	}

	/**
	 * Listener for the map of the single player
	 * 
	 */
	private class MapGameListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			switchToMenu(false);
		}
	}

	/**
	 * Listener for the easy and hard buttons.
	 * 
	 */
	private class ReturnToMenuListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			switchToMenu(true);
		}
	}

	/**
	 * If the initial menu is being viewed the buttons are added.
	 * 
	 * @param toMenu
	 */
	private void switchToMenu(boolean toMenu) {
		if (toMenu) {
			this.add(help);
			this.add(singlePlayer);
			this.add(multi);
			this.remove(easy);
			this.remove(hard);
			currentImage = mainImage;
		} else {
			this.remove(help);
			this.remove(singlePlayer);
			this.remove(multi);
			this.add(easy);
			this.add(hard);
			currentImage = mapChoiceImage;
		}
		repaint();
	}
}
