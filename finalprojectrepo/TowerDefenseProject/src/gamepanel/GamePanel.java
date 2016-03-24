package gamepanel;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import map.MapCoordinator;
import map.MapPanel;
import map.MiniMapPanel;
import model.Clickable;
import model.ClickableManager;
import model.Game;
import model.Statistics;
import model.Wave;
import model.WaveClickable;
import networking.GamePlayer;
import observer.ClickableObserver;
import observer.CurrencyObserver;
import tower.TowerType;
import view.GameFrame;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 * 
 */

/**
 * 
 * This class is the GamePanel. It holds and manipulates the values of the game.
 * 
 */
public class GamePanel extends JPanel implements CurrencyObserver,
		ClickableObserver {
	private JButton backToMenu;
	private Game game;
	private MapCoordinator layer;
	private StorePanel store;
	private ClickableDisplayPanel displayPanel;
	private JLabel currency, wave, life, time, waiting, enemyLives, enemyMoney;
	private JButton pauseButton, sendWave, multiHardMap, multiEasyMap;
	private MapPanel map;
	private ClickableManager manager;
	private List<ClickableObserver> clickableObservers;
	private Queue<Wave> waveQueue;
	private GameFrame frame;
	private JPanel buttonPanel;
	private MiniMapPanel multi;
	private boolean multiplayer;
	private Timer waveTimer;
	private boolean isPaused;
	private GamePlayer player;
	private int timeBetweenWaves = 10;
	private int mapInt;
	private int waveTimerCounter = 1, waveCounter;
	private Wave currentWave;
	private String[] mapPics = { "", "stars2.png", "stars1.png" };
	private BufferedImage mapChoiceImage, waitingImage, currentImage;
	private boolean multiPause = false;
	private Statistics stats;
	private WaveClickable storeWave;

	/**
	 * 
	 * GamePanel constructor
	 * 
	 * @param frame
	 * @param returnToMainMenu
	 */

	public GamePanel(ActionListener returnToMainMenu, GameFrame frame) {
		this.game = null;
		loadImages();
		waveTimer = new Timer(1000, new WaveLauncher());
		stats = new Statistics();
		setupWaves();

		manager = new ClickableManager(game, stats);
		this.frame = frame;
		clickableObservers = new ArrayList<ClickableObserver>();

		multiplayer = false;
		isPaused = false;

		createButtonsAndLabels();
		setUpButtons();

		registerListeners(returnToMainMenu);

	}

	/**
	 * Method creates the buttons seen on the Main Menu and the action buttons,
	 * Pause, Buy Wave and Quit.
	 */
	private void createButtonsAndLabels() {

		pauseButton = new JButton("Pause");
		sendWave = new JButton("Buy Wave");
		backToMenu = new JButton("Quit");
		waiting = new JLabel("Waiting for other player...");
		waiting.setFont(new Font("Arial", Font.BOLD, 73));
		waiting.setForeground(Color.GREEN);
		waiting.setBounds(50, 275, 900, 370);
		multiEasyMap = new JButton("Easy");
		multiEasyMap.setBounds(382, 283, 100, 100);// x, y, width, height
		multiHardMap = new JButton("Hard");
		multiHardMap.setBounds(671, 283, 100, 100);// x, y, width, height
		setInvisible(multiEasyMap);
		setInvisible(multiHardMap);

		enemyLives = new JLabel();
		enemyMoney = new JLabel();
	}

	/**
	 * Sets the characteristics of our buttons
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
	 * This method sets up the waves needed for single player and how many are
	 * sent. Also, pauses the enemies when the pause button is selected.
	 */
	private void setupWaves() {
		waveQueue = new ArrayDeque<Wave>();

		waveTimerCounter = 1;
		waveCounter = 0;

		isPaused = false;
		if (pauseButton != null) {
			pauseButton.setText("Pause");
		}
		if (!multiplayer) {
			for (int i = 0; i < 10; i++) {
				addWave(new Wave(10 + i * 5, 5 + i * 5, i * 5));
			}
		}
		waveTimer.stop();
	}

	/**
	 * This method adds a wave of enemies to the game when called. Checks to see
	 * if the timer is running, if not and it's in single player mode, the timer
	 * is started. Once its sent the wave counter increases for the display
	 * panel to show.
	 * 
	 * @param wave
	 */
	public void addWave(Wave wave) {
		if (wave == null)
			return;
		if (!waveTimer.isRunning() && !multiplayer)
			waveTimer.start();
		if (waveQueue.isEmpty())
			waveTimerCounter = 1;
		waveQueue.add(wave);

		if (multiplayer && !isPaused && waveQueue.size() > 0) {
			layer.start();
			currentWave = waveQueue.remove();// removes current wave from queue
			layer.launchWave(currentWave);
			waveCounter++;
			this.wave.setText("" + waveCounter);
		}
	}

	/**
	 * This method sets up the map on the game.
	 */
	private void setUpModel() {
		game.setMap(map);
	}

	/**
	 * This method adds the sendWave and pauseButton buttons to the button
	 * panel. The sendWave button is only for multiplayer. It checks with the
	 * if(multiplayer).
	 */
	private void setUpButtons() {
		this.setBackground(Color.GRAY);

		buttonPanel = new JPanel();
		buttonPanel.setBounds(6, 490, 283, 45);
		buttonPanel.setLayout(new GridLayout(1, 0));

		this.setLayout(null);
		buttonPanel.add(backToMenu);

		if (multiplayer)
			buttonPanel.add(sendWave);
		else
			buttonPanel.add(pauseButton);

		this.add(buttonPanel);
	}

	/**
	 * Lays out the GUI and its components. Both single player and multiplayer
	 * are done here.
	 */
	private void layoutGUI() {

		this.removeAll();
		displayPanel = new ClickableDisplayPanel();
		this.add(displayPanel);
		clickableObservers.clear();
		clickableObservers.add(displayPanel);
		clickableObservers.add(manager);
		if (multiplayer)
			clickableObservers.add(this);
		layer = new MapCoordinator(clickableObservers, game, mapInt,
				displayPanel, player, StorePanel.getMinWavePrice(), stats);
		layer.setBounds(295, 6, 660, 528);

		this.add(new BackgroundPanel(layer, mapPics[mapInt]));
		map = layer.getMap();
		layer.addConditionObserver(frame);

		store = new StorePanel(clickableObservers, multiplayer);
		this.add(new BackgroundPanel(store, mapPics[mapInt]));

		game.setStore(store);
		multi = new MiniMapPanel(multiplayer, clickableObservers, mapInt);

		this.add(new BackgroundPanel(multi, mapPics[mapInt]));

		JPanel labelPanel = new JPanel();
		labelPanel.setLayout(new GridLayout(0, 4));
		Font myFont = new Font("Arial", Font.PLAIN, 11);
		if (multiplayer) {
			JLabel money = new JLabel("Gold:");
			JLabel myLives = new JLabel("Lives:");
			JLabel eLives = new JLabel("E Lives:");
			JLabel eCash = new JLabel("E Money:");
			JLabel wavesSent = new JLabel("Waves:");
			labelPanel.setLayout(new GridLayout(0, 5));
			labelPanel.add(money).setFont(myFont);
			labelPanel.add(myLives).setFont(myFont);
			labelPanel.add(eLives).setFont(myFont);
			labelPanel.add(eCash).setFont(myFont);
			labelPanel.add(wavesSent).setFont(myFont);
			currency = new JLabel("$" + game.getCurrency());
			labelPanel.add(currency);
			life = new JLabel("" + game.getLives());
			labelPanel.add(life);
			enemyLives = new JLabel("10");
			labelPanel.add(enemyLives);
			enemyMoney = new JLabel("$300");
			labelPanel.add(enemyMoney);
			wave = new JLabel("0");
			labelPanel.add(wave);
		} else {
			labelPanel.setLayout(new GridLayout(0, 4));
			labelPanel.add(new JLabel("Currency:"));
			labelPanel.add(new JLabel("Wave:"));
			labelPanel.add(new JLabel("Life:"));
			labelPanel.add(new JLabel("Next wave:"));
			currency = new JLabel("$" + game.getCurrency());
			labelPanel.add(currency);
			wave = new JLabel("0");
			labelPanel.add(wave);
			life = new JLabel("" + game.getLives());
			labelPanel.add(life);
			time = new JLabel(this.timeFormat(timeBetweenWaves));
			labelPanel.add(time);
		}
		labelPanel.setBounds(6, 6, 283, 50);
		this.add(labelPanel);

		game.addObserver(this);
		game.addObserver(manager);
	}

	/**
	 * Registers the buttons on the panel.
	 * 
	 * @param returnToMenu
	 */
	private void registerListeners(ActionListener returnToMenu) {
		backToMenu.addActionListener(returnToMenu);
		backToMenu.addActionListener(new QuitListener());
		pauseButton.addActionListener(new PauseListener());
		sendWave.addActionListener(new WaveBuyer());
		ActionListener al = new MapChoiceListener();
		multiHardMap.addActionListener(al);
		multiEasyMap.addActionListener(al);
	}

	/**
	 * 
	 * This class/listener launches the waves in single player. Calculates the
	 * time on the timer and sends them accordingly.
	 * 
	 */
	private class WaveLauncher implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (!isPaused && waveQueue.size() > 0
					&& waveTimerCounter % timeBetweenWaves == 0) {
				layer.start();
				currentWave = waveQueue.remove();
				layer.launchWave(currentWave);
				waveCounter++;
				wave.setText("" + waveCounter);
			}
			if (waveQueue.size() > 0)
				time.setText(timeFormat(timeBetweenWaves - waveTimerCounter
						% timeBetweenWaves));
			repaint();
			waveTimerCounter++;

		}
	}

	/**
	 * This formats how the time is shown on the displayPanel
	 * 
	 * @param seconds
	 * @return time
	 */
	private String timeFormat(int seconds) {
		String time = "";
		time += seconds / 60;
		time += ":";
		if (seconds >= 10)
			time += seconds % 60;
		else
			time += "0" + seconds % 60;
		return time;
	}

	/**
	 * This class listens to when the pause button is pressed and then pauses
	 * the game. Note: The pause button only shows in the single player mode.
	 * 
	 */
	private class PauseListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {

			game.setPaused(!isPaused);
			if (isPaused) {
				layer.start();
				pauseButton.setText("Pause");
				waveTimer.start();
				if (currentWave != null)
					currentWave.resume();
			} else {
				waveTimer.stop();
				if (currentWave != null)
					currentWave.pause();
				layer.stop();
				pauseButton.setText("Resume");
			}
			isPaused = !isPaused;
		}
	}

	/**
	 * This method updates the currency and repaints is on the displayPanel when
	 * called.
	 */
	@Override
	public void updateCurrency() {
		currency.setText("$" + game.getCurrency());
		life.setText("" + game.getLives());
		repaint();
	}

	/**
	 * This method resets all the variables needed to start a brand new game.
	 * 
	 * @param multiplayer
	 */
	public void restart(boolean multiplayer) {
		this.multiplayer = multiplayer;
		multiPause = multiplayer;
		currentImage = waitingImage;
		removeAll();
		repaint();

		game = new Game(multiplayer);
		stats = new Statistics();
		stats.setHealth(game.getLives());
		stats.addGold(game.getCurrency());

		if (layer != null)
			layer.stop();
		setupWaves();
		if (!multiplayer) {
			waveTimer.start();
		} else {
			isPaused = true;
			game.setPaused(true);
		}
		manager = new ClickableManager(game, stats);
		if (multiplayer) {
			player = new GamePlayer(this, game);
			player.addConditionObserver(frame);
			manager.setGamePlayer(player);
		} else {
			player = null;
			manager.setGamePlayer(null);
		}

		if (!multiplayer) {
			setUpEverything();
		}
	}

	/**
	 * This method is called in restart to set up the gui, buttons, model and
	 * update the currency to beginning amount.
	 */
	private void setUpEverything() {
		this.removeAll();

		layoutGUI();
		setUpModel();
		setUpButtons();
		updateCurrency();

		this.validate();
		this.repaint();
	}

	/**
	 * This method repaints the minimap in multiplayer
	 * 
	 * @param t
	 * @param xIndex
	 * @param yIndex
	 */
	public void updateMinimap(TowerType t, int xIndex, int yIndex) {
		multi.updateMinimap(t, xIndex, yIndex);
	}

	/**
	 * This method stops the wave timer and the layer timer.
	 */
	public void pauseGamePanelSwitching() {
		waveTimer.stop();
		if (layer != null)
			layer.stop();
	}

	/**
	 * Gives MapOne a numerical value for the enemy path. This is used when
	 * randomizing the enemies' paths.
	 */
	public void setMapOne() {
		mapInt = 1;

	}

	/**
	 * Gives MapTwo a numerical value for the enemy path. This is used when
	 * randomizing the enemies' paths.
	 */
	public void setMapTwo() {
		mapInt = 2;

	}

	/**
	 * Returns the mapInt after calling which map to setup.
	 * 
	 * @return mapInt
	 */
	public int getMapType() {
		return mapInt;
	}

	/**
	 * This method is used to display the the player and its number on the
	 * frame.
	 * 
	 * @param playerNumber
	 */
	public void setPlayerNumber(int playerNumber) {
		// quality of life console identifier
		System.out.println("Player " + playerNumber + "'s console.");
		Container c = this;
		try {
			for (; c.getParent() != null; c = c.getParent()) {
			}
			if (c instanceof JFrame)
				((JFrame) c).setTitle("Player " + playerNumber);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (playerNumber == 1) {
			this.removeAll();
			chooseMap();
		}
	}

	/**
	 * When multiplayer is selected the this method sets up the game without
	 * pause and starts the wave timer and layer timer.
	 * 
	 * @param mapInt
	 */
	public void startMultiplayerGame(int mapInt) {
		multiPause = false;
		isPaused = false;
		game.setPaused(false);
		waveTimer.start();

		this.mapInt = mapInt;
		setUpEverything();
		layer.start();
		player.sendStats(stats);
	}

	/**
	 * Checks if the click of a wave is valid. If so, it sends the wave.
	 */
	@Override
	public void notifyOfClick(Clickable wave) {
		if (invalidWave(wave))
			sendWave.setEnabled(false);
		else {
			sendWave.setEnabled(true);
			storeWave = ((WaveClickable) wave);
		}
	}

	/**
	 * Checks of valid wave.
	 * 
	 * @param wave
	 * @return
	 */
	private boolean invalidWave(Clickable wave) {
		return wave == null || !(wave instanceof WaveClickable)
				|| wave.getPrice() > game.getCurrency() || !wave.isSelected()
				|| isPaused;
	}

	/**
	 * Listener buys the wave sent to the other player.
	 * 
	 */
	private class WaveBuyer implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			player.sendWave(storeWave.getWave());
			stats.addGold(-storeWave.getPrice());
			player.sendStats(stats);
			storeWave.setSelected(false);
			sendWave.setEnabled(false);
			game.setCurrency(game.getCurrency() - storeWave.getPrice());
		}
	}

	/**
	 * Quit Listener quits the game when clicked
	 * 
	 */
	private class QuitListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			quit();
		}

	}

	/**
	 * When quit is selected this method sends the stats and quits the game.
	 */
	public void quit() {
		if (multiplayer) {
			player.sendStats(stats);
			player.quit();
		}
	}

	/**
	 * Used for testing purposes. Sets the player for tesing.
	 * 
	 * @return
	 */
	public GamePlayer getGamePlayer() {
		// for testing purposes
		return player;
	}

	/**
	 * This method selects the map when easy or hard is clicked in the menu
	 */
	public void chooseMap() {
		multiPause = true;
		currentImage = mapChoiceImage;
		this.add(multiEasyMap);
		this.add(multiHardMap);
		this.validate();
		this.repaint();
	}

	/**
	 * Listener chooses map in Menu
	 * 
	 */
	private class MapChoiceListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			removeMapButtons();
			repaint();
			if (((JButton) arg0.getSource()).getText().equals("Hard")) {
				player.selectMap(2);
			} else
				player.selectMap(1);
		}
	}

	/**
	 * takes away map buttons on menu screen. This makes room for the
	 * "...waiting for second player..." message.
	 */
	private void removeMapButtons() {
		this.remove(multiEasyMap);
		this.remove(multiHardMap);
		currentImage = waitingImage;
	}

	/**
	 * returns the game
	 * 
	 * @return game
	 */
	public Game getModel() {
		return game;
	}

	/**
	 * returns the layer
	 * 
	 * @return
	 */
	public MapCoordinator getMapCoordinator() {
		return layer;
	}

	/**
	 * Takes the enemyLives and enemyMoney variables and sets their text to be
	 * viewed in the display when playing multiplayer. The stats are of the
	 * opposing player.
	 * 
	 * @param enemyStats
	 */
	public void updateStats(Statistics enemyStats) {
		enemyLives.setText("" + enemyStats.getHealth());
		enemyMoney.setText("$" + enemyStats.getGold());
	}

	/**
	 * loads the images needed in the panel.
	 */
	private void loadImages() {
		String baseDir = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "Images"
				+ System.getProperty("file.separator");
		try {
			mapChoiceImage = ImageIO.read(new File(baseDir
					+ "diffBackGroundPanel.png"));
		} catch (IOException e) {
			System.out.println("Could not find map choice image");
		}
		try {
			waitingImage = ImageIO.read(new File(baseDir + "waiting.png"));
		} catch (IOException e) {
			System.out.println("Could not find multiplayer waiting image");
		}
	}

	/**
	 * Paint Component
	 */
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		if (multiPause)
			g2.drawImage(currentImage, 0, 0, null);
	}
}
