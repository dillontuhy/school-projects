//Dillon Tuhy, Lamec Angel Gabriel Fletez Reyes, Laura Vonessen, Martin Stankard
package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

import model.Game;
import observer.WumpObserver;
import songplayer.EndOfSongEvent;
import songplayer.EndOfSongListener;
import songplayer.SongPlayer;
/**
 * This class presents the animated view of the game.
 */
@SuppressWarnings("serial")
public class AnimGUI extends JPanel implements WumpObserver {

	private BufferedImage background, currentImage, arrowImage, pitDeath,
			arrowDeath, winning, batMove, batLeave;
	private boolean batsJustGotYou = false;
	private List<BufferedImage> wumpusAnimation;
	private final int LEFT = 0, CENTER = 1, RIGHT = 2;
	private List<JButton> caveButtons = new ArrayList<JButton>();
	private JButton help, restart, newGame, quiver;
	private Game currentGame;
	private int roomNumberWidth = 70;
	private JLabel room0, room1, room2, arrowCount, bats, wumpus, pit,
			currentRoom;
	Font roomNumberFont = new Font("Times New Roman", Font.BOLD + Font.ITALIC,
			45);
	Font hazardFont = new Font("Times New Roman", Font.BOLD + Font.ITALIC, 45);
	private JTextArea helpText;
	private JScrollPane scroller;
	private Timer wumpusTimer, arrowTimer, batTimer;
	private int arrowMovement = 50;
	private int currentArrowX = -530;
	private String wumpusSound = System.getProperty("user.dir")
			+ System.getProperty("file.separator") + "flute.aif";
	private boolean labelsShouldBeVisible = true;
	public static final String animHelpString = "The Wumpus lives in a cave of 20 cave rooms.  Each cave room has three tunnels leading to other cave rooms.  The tunnel system is three dimensional roughly in the shape of a dodecahedron.  If you do not know about dodecahedrons, Google it. \n\n"
			+ "Hazards: There are two hazards you must be aware of:\n1)	Bottomless pits – two rooms have bottomless pits in them.  If you go there, you fall into the pit and lose.\n2)	Bats – two other (rooms have super bats. If you dare enter their cave room, the gaggle of bats carry you to some other cave room.  This can be a hazardous because if you are flown to a cave room with a pit or the Wumpus, you lose.\n\n"
			+ "Warnings: When you are one cave room away from a Wumpus or hazard, you will be warned:\n1)	Wumpus: ‘Smell something foul’\n2)	Bats: ‘Hear squeaking’\n3)	Pit: ‘Feel a draft’\n\n"
			+ "Wumpus: The hunted and the hunter.  Two interactions can occur:\n1)	If the Wumpus is shot by your arrow, you win.\n2)	If you walk into the cave room with the Wumpus, you lose.\n\n"
			+ "You: Each turn you may move or shoot a crooked arrow:\n1)	Moving: you can move one cave room by clicking one of the numbered tunnels.\n2)	Shooting Arrows: You have 3 arrows indicated on the quiver icon.  To open the arrow shooting interface, click the quiver.  You lose when you shoot without having any arrows left. Each arrow can travel from 1 to 5 rooms.  You aim by listing the rooms you want the arrow to go to.\n	-If the arrow can’t travel the path, it hits a wall and falls harmlessly.\n	-If the arrow gets to a cave room with the Wumpus, you win.\n	-If the arrow hits you, you lose.\n\n"
			+ "To restart the level, press the Reset button.  To start a new game, press the New Game button.  To see this message again, press the Help button.";

	
	/**
	 * This is the animation view's constructor.
	 * @param currentGame This parameter allows this view to use the same game.
	 */
	public AnimGUI(Game currentGame) {
		this.currentGame = currentGame;
		wumpusTimer = new Timer(250, new WumpusTimerListener());
		arrowTimer = new Timer(250 / 6, new ArrowTimerListener());
		wumpusAnimation = new ArrayList<BufferedImage>();
		batTimer = new Timer(1300, new BatTimerListener());
		currentGame.addObserver(this);
		loadImages();
		layoutGUI(); 
		registerListeners();
		setupLabels();

	}
	
	/**
	 * This method sets up the values and/or visibilities of all the JLabels.
	 */
	private void setupLabels() {
		currentRoom.setText("" + currentGame.getCurrentRoom());
		room0.setText("" + currentGame.getCurrentNeighbors()[0]);
		room1.setText("" + currentGame.getCurrentNeighbors()[1]);
		room2.setText("" + currentGame.getCurrentNeighbors()[2]);
		arrowCount.setText("" + currentGame.getArrowsLeft());

		String temp = currentGame.smellsSightsandFeels();
		bats.setVisible(temp.contains("squeak") && labelsShouldBeVisible);
		wumpus.setVisible(temp.contains("foul") && labelsShouldBeVisible);
		pit.setVisible(temp.contains("draft") && labelsShouldBeVisible);
	}

	/**
	 * This method registers all the listeners
	 */
	private void registerListeners() {
		CaveButtonListener bl = new CaveButtonListener();
		for (int i = 0; i < 3; i++)
			caveButtons.get(i).addActionListener(bl);
		help.addActionListener(new HelpListener());
		restart.addActionListener(new RestartListener());
		newGame.addActionListener(new NewGameListener());
		quiver.addActionListener(new QuiverListener());
	}

	/**
	 * This method loads all the images from files into instance variables
	 */
	private void loadImages() {
		try {
			background = ImageIO.read(new File("cave_final.png"));
		} catch (IOException e) {
			System.out.println("Could not find cave photo");
		}
		BufferedImage temp;
		String baseDir = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "wumpus_monster"
				+ System.getProperty("file.separator");

		for (int i = 1; i < 10; i++) {
			try {
				temp = ImageIO.read(new File(baseDir + "monster_00" + i
						+ ".png"));
				wumpusAnimation.add(temp);
			} catch (IOException e) {
				System.out.println("Could not find monster " + i);
			}
		}
		for (int i = 10; i <= 17; i++) {
			try {
				temp = ImageIO
						.read(new File(baseDir + "monster_0" + i + ".png"));
				wumpusAnimation.add(temp);
			} catch (IOException e) {
				System.out.println("Could not find monster " + i);
			}
		}

		try {
			wumpusAnimation.add(ImageIO.read(new File(baseDir
					+ "Wumpus_death.png")));
		} catch (IOException e) {
			System.out.println("Could not find final wumpus.");
		}

		try {
			arrowImage = (ImageIO.read(new File("arrow.png")));
		} catch (IOException e) {
			System.out.println("Could not find final arrow.");
		}
		currentImage = background;

		try {
			pitDeath = ImageIO.read(new File("pitfall.png"));
		} catch (IOException e) {
			System.out.println("Could not find pit photo");
		}

		try {
			arrowDeath = ImageIO.read(new File("arrow_death.png"));
		} catch (IOException e) {
			System.out.println("Could not find arrow death photo");
		}

		try {
			winning = ImageIO.read(new File("winning.png"));
		} catch (IOException e) {
			System.out.println("Could not find winning photo");
		}

		try {
			batMove = ImageIO.read(new File("batsgotyou.png"));
		} catch (IOException e) {
			System.out.println("Could not find bats got you photo");
		}

		try {
			batLeave = ImageIO.read(new File("batsleftalone.png"));
		} catch (IOException e) {
			System.out.println("Could not find bat left alone photo");
		}
	}

	/**
	 * This method lays out the GUI components.
	 */
	private void layoutGUI() {
		this.setLayout(null);

		for (int i = 0; i < 3; i++) {
			caveButtons.add(new JButton());
			setInvisible(caveButtons.get(i));
			this.add(caveButtons.get(i));
		}

		caveButtons.get(LEFT).setBounds(16, 137, 178, 218);
		caveButtons.get(CENTER).setBounds(16 + 178 + 43, 137, 178, 218);
		caveButtons.get(RIGHT).setBounds(16 + 178 * 2 + 43 + 66, 137, 178, 218);

		help = new JButton("");
		setInvisible(help);
		help.setBounds(24, 548, 60, 31);
		this.add(help);

		restart = new JButton("");
		setInvisible(restart);
		restart.setBounds(8 + 70 + 13, 548, 60, 31);
		this.add(restart);

		newGame = new JButton("");
		setInvisible(newGame);
		newGame.setBounds(135 + 26, 548, 60, 31);
		this.add(newGame);

		quiver = new JButton("");
		setInvisible(quiver);
		quiver.setBounds(614, 399, 65, 178);
		this.add(quiver);

		room0 = new JLabel("XX");
		room0.setVisible(true);
		room0.setForeground(Color.RED);
		room0.setBounds(92, 117, roomNumberWidth, roomNumberWidth);
		room0.setFont(roomNumberFont);
		this.add(room0);

		room1 = new JLabel("XX");
		room1.setVisible(true);
		room1.setForeground(Color.RED);
		room1.setBounds(309, 117, roomNumberWidth, roomNumberWidth);
		room1.setFont(roomNumberFont);
		this.add(room1);

		room2 = new JLabel("XX");
		room2.setVisible(true);
		room2.setForeground(Color.RED);
		room2.setBounds(553, 117, roomNumberWidth, roomNumberWidth);
		room2.setFont(roomNumberFont);
		this.add(room2);

		arrowCount = new JLabel("3");
		arrowCount.setVisible(true);
		arrowCount.setForeground(Color.RED);
		arrowCount.setFont(roomNumberFont);
		arrowCount.setBounds(631, 435, roomNumberWidth, roomNumberWidth);
		this.add(arrowCount);

		bats = new JLabel("You hear squeaking...");
		bats.setVisible(true);
		bats.setForeground(Color.RED);
		bats.setBounds(165, 424, 700, 50);
		bats.setFont(hazardFont);
		wumpus = new JLabel("You smell something foul...");
		wumpus.setVisible(true);
		wumpus.setForeground(Color.RED);
		wumpus.setBounds(49, 474, 700, 50);
		wumpus.setFont(hazardFont);
		pit = new JLabel("You feel a draft...");
		pit.setVisible(true);
		pit.setForeground(Color.RED);
		pit.setBounds(46, 374, 700, 50);
		pit.setFont(hazardFont);

		this.add(bats);
		this.add(pit);
		this.add(wumpus);

		helpText = new JTextArea(animHelpString);
		helpText.setLineWrap(true);
		helpText.setWrapStyleWord(true);
		helpText.setEditable(false);
		scroller = new JScrollPane(helpText);
		scroller.setPreferredSize(new Dimension(600, 530));

		currentRoom = new JLabel("XX");
		currentRoom.setVisible(true);
		currentRoom.setForeground(Color.RED);
		currentRoom.setFont(roomNumberFont);
		currentRoom.setBounds(540, 522, roomNumberWidth, roomNumberWidth);
		this.add(currentRoom);

	}
	
	/**
	 * This method sets the given button "invisible"
	 * @param button The button to set invisible
	 */
	private void setInvisible(JButton button) {
		button.setOpaque(false);
		button.setContentAreaFilled(false);
		button.setBorderPainted(false);
	}

	/**
	 * This is the overridden paintComponent method, it draws the pictures.
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gr = (Graphics2D) g;

		gr.drawImage(currentImage, 0, 0, null);
		gr.drawImage(arrowImage, currentArrowX, 203, null);
	}

	/**
	 * This method is from WumpusObserver; it allows this class to observe
	 * the game.
	 */
	@Override
	public void update() {
		if (currentGame.getState().contains("restart")
				|| currentGame.getState().contains("new game")
				|| currentGame.getState().contains("test"))
			switchFromAnimation();
		setupLabels();

		if (Wumpus.getTabWithFocus() == Wumpus.ANIMATION_INDEX
				&& currentGame.getState().contains("ats ") && !batsJustGotYou) {
			batsJustGotYou = true;
			this.setLabelsVisible(false);
			if (currentGame.getState().contains("got a hold of you")) {
				currentImage = batMove;
			} else {
				currentImage = batLeave;
			}
			repaint();
			batTimer.start();
			return;
		}
		batsJustGotYou = false;

		if (currentGame.isDead()) {
			if (currentGame.getCauseOfDeath().contains("umpus")
					&& Wumpus.getTabWithFocus() == Wumpus.ANIMATION_INDEX) {
				setLabelsVisible(false);
				enableAllButtons(false);
				Wumpus.setTabsEnabled(false);
				wumpusTimer.start();
				SongPlayer.playFile(new WumpusEndOfSongListener(), wumpusSound);
			} else if (currentGame.getCauseOfDeath().contains("umpus")
					&& Wumpus.getTabWithFocus() != Wumpus.ANIMATION_INDEX) {
				setEndScreen(wumpusAnimation.get(17));
			} else if (currentGame.getCauseOfDeath().contains("really deep")) {
				setEndScreen(pitDeath);
			} else {
				setEndScreen(arrowDeath);
			}
		}
	}

	/**
	 * This action listener listens to the restart button
	 */
	private class RestartListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			switchFromAnimation();
			currentGame.resetLevel();
			setupLabels();
		}
	}

	/**
	 * This method resets the background image, enables all
	 * buttons and sets all labels visible
	 */
	private void switchFromAnimation() {
		currentImage = background;
		enableAllButtons(true);
		setLabelsVisible(true);
	}

	/**
	 * This method sets all labels either visible or invisible, depending
	 * on the value of the boolean "visible"
	 * @param visible Which direction to set the labels
	 */
	private void setLabelsVisible(boolean visible) {
		room0.setVisible(visible);
		room1.setVisible(visible);
		room2.setVisible(visible);
		currentRoom.setVisible(visible);
		arrowCount.setVisible(visible);

		labelsShouldBeVisible = visible;
		bats.setVisible(visible);
		wumpus.setVisible(visible);
		pit.setVisible(visible);
		repaint();
	}

	/**
	 * This method enables or disables all buttons depending
	 * on the value of the boolean "enable"
	 * @param enable Which direction to set the buttons
	 */
	private void enableAllButtons(boolean enable) {
		restart.setEnabled(enable);
		newGame.setEnabled(enable);
		help.setEnabled(enable);

		caveButtons.get(0).setEnabled(enable);
		caveButtons.get(1).setEnabled(enable);
		caveButtons.get(2).setEnabled(enable);
		quiver.setEnabled(enable);
		repaint();
	}

	/**
	 * This action listener listens to the new game button
	 */
	private class NewGameListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			switchFromAnimation();
			currentGame.newGame();
			setupLabels();
		}
	}

	/**
	 * This action listener listens to the help button
	 */
	private class HelpListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(null, scroller);
		}
	}

	/**
	 * This action listener listens to the quiver button
	 */
	private class QuiverListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String arrowCaves = JOptionPane
					.showInputDialog(
							null,
							"Separate Arrow destination rooms with spaces\nThe first room must be a neighbor of the room you are in.");

			if (arrowCaves == null)
				return;

			String temp = currentGame.shootArrow(arrowCaves);
			if (temp.contains("nvalid"))
				JOptionPane.showMessageDialog(null, temp);
			else if (!currentGame.getState().contains("ran out")) {
				enableAllButtons(false);
				arrowTimer.start();
			}
		}

	}

	/**
	 * This action listener listens to the cave buttons
	 */
	private class CaveButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			int i = caveButtons.indexOf((JButton) (arg0.getSource()));
			System.out.println("You clicked cave " + i);
			currentGame.moveRoom(currentGame.getCurrentNeighbors()[i]);
			setupLabels();
		}

	}

	/**
	 * This action listener listens to the wumpus timer
	 */
	private class WumpusTimerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			int i = wumpusAnimation.indexOf(currentImage);
			if (i < 17)
				currentImage = wumpusAnimation.get(i + 1);
			else {
				wumpusTimer.stop();
				setEndScreen(wumpusAnimation.get(17));
			}
			repaint();
		}
	}

	/**
	 * This action listener listens to the arrow timer
	 */
	private class ArrowTimerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			currentArrowX += arrowMovement;
			if (currentArrowX > 700) {
				arrowTimer.stop();
				currentArrowX = -530;
				if (currentGame.hasWon()) {
					setEndScreen(winning);
					return;
				} else if (currentGame.isDead()) {
					setEndScreen(arrowDeath);
					return;
				} else {
					enableAllButtons(true);
					setLabelsVisible(true);
				}
			}
			repaint();
		}
	}

	/**
	 * This method sets a screen with a different image and a reduced set of buttons
	 * enabled
	 * @param screen The new background image
	 */
	private void setEndScreen(BufferedImage screen) {
		currentImage = screen;
		enableAllButtons(false);
		setLabelsVisible(false);

		restart.setEnabled(true);
		newGame.setEnabled(true);
		help.setEnabled(true);
	}

	/**
	 * This end of song listener listens to the end of the wumpus sound
	 */
	private class WumpusEndOfSongListener implements EndOfSongListener {

		@Override
		public void songFinishedPlaying(
				EndOfSongEvent eventWithFileNameAndDateFinished) {
			Wumpus.setTabsEnabled(true);
		}

	}

	/**
	 * This action listener listens to the bat timer
	 */
	private class BatTimerListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			currentImage = background;
			setLabelsVisible(true);
			batTimer.stop();
			repaint();
			update();
		}

	}
}
