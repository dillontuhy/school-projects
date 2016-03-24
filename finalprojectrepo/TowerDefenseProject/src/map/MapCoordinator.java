package map;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import model.Game;
import model.Statistics;
import model.Wave;
import networking.GamePlayer;
import observer.ClickableObserver;
import observer.EnemyHealthObserver;
import observer.GameConditionObservable;
import observer.GameConditionObserver;
import tower.LaserTower;
import tower.Tower;
import enemy.BigEnemy;
import enemy.BossEnemy;
import enemy.Enemy;
import enemy.EnemyType;
import enemy.SmallEnemy;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 */

/**
 * This class monitors and controls the game's state during play, specifically win,lose and
 * ties
 */
public class MapCoordinator extends JLayeredPane implements
		GameConditionObservable {

	private MapPanel map;
	private JPanel enemyPanel;
	private RangesPanel rangePanel;
	private Timer enemyTimer;
	private List<ClickableObserver> clickableObservers;
	private List<GameConditionObserver> conditionObservers;
	private List<Enemy> enemyList;
	private Game game;
	private List<Tower> towerList;
	private Wave wave;
	private int mapInt;
	private EnemyHealthObserver display;
	private GamePlayer player;
	private Statistics stats;
	private final int LOSE = 1, WIN = 2, TIE = 3, STATS_UPDATE = 0;
	private int minWavePrice;

/**
 * This constructor initializes a new MapCoordinator with each new game.   
 * 
 * @param clickObservers 
 * @param game
 * @param mapInt
 * @param display
 * @param player
 * @param minWavePrice
 * @param stats
 */
	public MapCoordinator(List<ClickableObserver> clickObservers, Game game,
			int mapInt, EnemyHealthObserver display, GamePlayer player,
			int minWavePrice, Statistics stats) {
		
		this.stats = stats;
		this.setOpaque(false);
		towerList = game.getTowers();
		this.game = game;
		this.mapInt = mapInt;
		this.display = display;
		this.player = player;
		this.minWavePrice = minWavePrice;

		map = new MapPanel(clickObservers, mapInt);
		clickableObservers = clickObservers;
		conditionObservers = new ArrayList<GameConditionObserver>();
		enemyPanel = new JPanel();
		enemyPanel.setLayout(null);
		enemyPanel.setBounds(map.getBounds());
		enemyPanel.setOpaque(false);
		rangePanel = new RangesPanel(game.getTowers());
		rangePanel.setBounds(map.getBounds());

		enemyList = new ArrayList<Enemy>();

		setupGui();
		enemyTimer = new Timer(1000 / 21, new EnemyTimerListener());
		clickableObservers = clickObservers;
	}
	
/**this method checks if any enemies are still active eithin the game
 * 
 * @return
 */
	public boolean isRunning() {
		return enemyTimer.isRunning();
	}
	
/**
 * this method handles layout requirements for the class's panel
 */
	
	private void setupGui() {
		this.setPreferredSize(new Dimension(660, 528));
		this.setLayout(null);
		this.add(enemyPanel, new Integer(1));
		this.add(map, new Integer(0));
		this.add(rangePanel, new Integer(2));
	}
	
/**
 * this class is primarily concerned with evaluating 
 * the no enemies are left component of a tie condition
 */
	private class EnemyTimerListener implements ActionListener {
		private int tiePossibleCounter = 1;

/**this method is the primary win, lose, draw logic processor
 * 
 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			for (int i = 0; i < enemyList.size(); i++) {

				if (game.isOver()) {
					enemyTimer.stop();
					return;
				}

				if (enemyList.get(i).hasCrossedFinishLine()) {
					enemyList.get(i).takeDamage(1000);// make sure it's dead so
														// it stops being
														// selected, if it is
														// selected
					enemyPanel.remove(enemyList.get(i));
					game.setLives(game.getLives() - 1);
					stats.setHealth(game.getLives());
					notifyObservers(STATS_UPDATE);
					if (game.hasLost()) {
						enemyTimer.stop();
						stats.setHealth(game.getLives());
						notifyObservers(LOSE);
						if (game.isMultiPlayer())
							player.notifyOfLose();
					}
					enemyList.remove(i);
				} else if (enemyList.get(i).isDead()) {
					enemyPanel.remove(enemyList.get(i));
					game.setCurrency(game.getCurrency()
							+ enemyList.get(i).getGoldOnDeath());

					game.addWinPoint();
					stats.addKills(1);
					stats.addGold(enemyList.get(i).getGoldOnDeath());
					notifyObservers(STATS_UPDATE);
					if (game.hasWon()) {
						enemyTimer.stop();
						notifyObservers(WIN);
					}
					enemyList.remove(i);
				} else
					enemyList.get(i).moveAndAnimate();
			}

			if (game.isMultiPlayer()) {
				if (tiePossible()) {
					tiePossibleCounter++;
					if (tiePossibleCounter % 30 == 0
							&& player.checkForTie(true)) {
						System.out
								.println("2nd player to become tied, tie has occurred");
						enemyTimer.stop();
						notifyObservers(TIE);
					}
				} else {
					tiePossibleCounter = 1;
					player.checkForTie(false);
				}
			}

			Collections.sort(enemyList);

			rangePanel.clearLasers();

			for (Tower t : towerList) {
				t.detectEnemies(enemyList);
				if (t instanceof LaserTower)
					rangePanel.drawLaser(((LaserTower) t).getLaser());

			}
			repaint();

		}
/**
 * this method returns checks both components of whether or not a tie is 
 * possible with this player 
 * @return
 */
		private boolean tiePossible() {
			return enemyList.isEmpty() && game.getCurrency() < minWavePrice;
		}
	}
	
/**
 * this method starts the enemy timer component, if it isn't already running or should be.
 */
	public void start() {
		if (!enemyTimer.isRunning() && !game.isOver() && !game.isPaused())
			enemyTimer.start();
	}
/**
 * this method stops the enemy timer
 */
	public void stop() {
		enemyTimer.stop();
	}
/**
 * getter method for this class's map.
 */
	public MapPanel getMap() {
		return map;
	}
/**
 * This method begins a wave
 * @param wave2
 */
	public void launchWave(Wave wave2) {
		wave = wave2;
		wave.setMapCoordinator(this);
		wave.start();
	}
/**
 * This map adds a new enemy and decides which path it should take on the difficult map
 * @param enemyType
 */
	public void launchEnemy(EnemyType enemyType) {

		switch (enemyType) {
		case SMALL:
			enemyList.add(new SmallEnemy(clickableObservers, map
					.getPath((int) (Math.random() * mapInt))));
			break;
		case BIG:
			enemyList.add(new BigEnemy(clickableObservers, map
					.getPath((int) (Math.random() * mapInt))));
			break;
		case BOSS:
			enemyList.add(new BossEnemy(clickableObservers, map
					.getPath((int) (Math.random() * mapInt))));
			break;
		default:
			return;
		}

		enemyList.get(enemyList.size() - 1).addEnemyHealthObserver(display);
		enemyPanel.add(enemyList.get(enemyList.size() - 1));
	}
/**
 * getter method for this Mapcoordinator's game
 * @return
 */
	public Game getGame() {
		return game;
	}
/**
 * This method alerts the GameConditionObserver of win, lose or draw conditions.
 */
	@Override
	public void notifyObservers(int panelInt) {
		stats.setHealth(game.getLives());
		if (game.isMultiPlayer())
			player.sendStats(stats);
		switch (panelInt) {
		case LOSE:
			game.setLost(true);
			break;
		case WIN:
			game.setWon(true);
			break;
		case TIE:
			game.setTied(true);
			break;
		}
		for (GameConditionObserver condition : conditionObservers)
			if (condition != null)
				condition.updateGameCondition(panelInt);
	}
/**
 * addObserver component of observer design pattern
 */
	@Override
	public void addConditionObserver(GameConditionObserver gco) {
		conditionObservers.add(gco);
	}
}
