//Laura Vonessen & Martin Stankard

package networking;

import gamepanel.GamePanel;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import model.Game;
import model.Statistics;
import model.Wave;
import observer.GameConditionObservable;
import observer.GameConditionObserver;
import tower.TowerInfo;
import tower.TowerType;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * This client program allows multiple players to play a multiplayer game; they join the game in pairs
 */

public class GamePlayer implements GameConditionObservable {
	public static final String HOST_NAME = "localhost";
	public static final int PORT_NUMBER = 4009;
	private static final String quitMessage = "quit message",
			loseMessage = "lose message", possibleTieString = "possible tie",
			impossibleTieString = "impossible tie", tieString = "tie";
	public static final String disconnectionMessage = "END OF THE GAME";

	private ObjectOutputStream output;
	private ObjectInputStream input;
	private IncomingReader incomingMessageReader;
	private GamePanel gamePanel;
	private Game game;
	private Integer mapType = 1;
	private boolean selfTiePossible = false, opponentTiePossible = false;
	private int playerNumber;
	private List<GameConditionObserver> conditionObservers;
	private final int LOSE = 1, WIN = 2, TIE = 3;
	private boolean haveDisconnectedFromServer = false;

	/**
	 * Starts the thread, connecting to the server and initializing the reader
	 */
	private void startTheReadThread() {
		incomingMessageReader = new IncomingReader();
		Thread thread = new Thread(incomingMessageReader);
		thread.start();
	}

	/**
	 * Constructor to set up the private variables used to talk between players
	 * 
	 * @param gamePanel
	 *            , game
	 */
	public GamePlayer(GamePanel gamePanel, Game game) {

		this.gamePanel = gamePanel;
		this.game = game;
		conditionObservers = new ArrayList<GameConditionObserver>();
		startTheReadThread();
	}

	/**
	 * Connects our gamePlayer to the server and sets up streams
	 */
	private void connectToServer() {
		Socket sock = null;
		try {
			// connect to the server
			sock = new Socket(HOST_NAME, PORT_NUMBER);
		} catch (Exception e) {
			System.out.println("Client was unable to connect to server");
			e.printStackTrace();
		}
		try {
			output = new ObjectOutputStream(sock.getOutputStream());
			input = new ObjectInputStream(sock.getInputStream());
		} catch (Exception e) {
			System.out
					.println("Unable to obtain Input/Output streams from Socket");
			e.printStackTrace();
		}
	}

	/**
	 * Sends the wave across the server
	 * 
	 * @param wave
	 */
	public void sendWave(Wave wave) {
		try {
			if (wave != null)
				output.reset();
			output.writeObject(wave);
		} catch (IOException ioe) {
			System.out.println("Error writing wave to server");
			ioe.printStackTrace();
		}
	}

	/**
	 * This method notifies the minimap of the other game that a tower has been
	 * purchased and placed updating the correct objects
	 * 
	 * @param t
	 *            ,xIndex.yIndex
	 */
	public void notifyOfTowerPlacement(TowerType t, int xIndex, int yIndex) {
		try {
			if (t != null) {
				output.reset();
				output.writeObject(new TowerInfo(t, xIndex, yIndex));
			}
		} catch (IOException ioe) {
			System.out.println("Error writing wave to server");
			ioe.printStackTrace();
		}
	}

	/**
	 * Sends the stats of the other mulitplayer to update mini map info
	 * 
	 * @param stats
	 */
	public void sendStats(Statistics stats) {
		try {
			output.reset();
			output.writeObject(stats);
		} catch (IOException e) {
			System.out.println("failed to send stats");
			e.printStackTrace();
		}
	}

	/**
	 * This class runs to listen for objects being written onto the stream and
	 * handles them accordingly
	 */

	class IncomingReader implements Runnable {

		private volatile boolean keepListening = true;

		public void run() {
			connectToServer();
			while (keepListening) {
				Object o = null;
				try {
					o = input.readObject();
				} catch (ClassNotFoundException cnfe) {
					keepListening = false;
					System.out
							.println("Server sent wrong kind of object--we're not listening anymore");
					cnfe.printStackTrace();
				} catch (IOException ioe) {
					keepListening = false;
					System.out
							.println("Error reading from server--we're not listening anymore");
					ioe.printStackTrace();
				}

				if (o == null) {
					System.out.println("Error--object was null");
				} else if (o.toString().equals(GameServer.player1Message)) {
					gamePanel.setPlayerNumber(1);
					playerNumber = 1;
				} else if (o.toString().contains(GameServer.mapMessage)) {
					gamePanel.setPlayerNumber(2);
					playerNumber = 2;
					gamePanel.startMultiplayerGame(Integer.parseInt(o
							.toString().substring(
									GameServer.mapMessage.length())));
					try {
						output.reset();
						output.writeObject("Start");
					} catch (IOException e) {
						System.out
								.println("Failed to tell other game to start");
						e.printStackTrace();
					}
				} else if (o.toString().equals("Start")) {
					gamePanel.startMultiplayerGame(mapType.intValue());
				} else if (o.toString().equals(possibleTieString)) {
					opponentTiePossible = true;
				} else if (o.toString().equals(impossibleTieString)) {
					opponentTiePossible = false;
				} else if (o.toString().equals(loseMessage)) {
					game.setWon(true);
					notifyObservers(WIN);
					disconnectFromServer();
				} else if (o.toString().equals(quitMessage)) {
					game.setWon(true);
					notifyObservers(WIN);
					disconnectFromServer();
				} else if (o.toString().equals(tieString)) {
					game.setTied(true);
					notifyObservers(TIE);
					System.out
							.println("1st player to become tied, other player has notified that tie has occurred");
					disconnectFromServer();

				} else if (o instanceof TowerInfo) {
					gamePanel.updateMinimap(((TowerInfo) o).getTowerType(),
							((TowerInfo) o).getXIndex(),
							((TowerInfo) o).getYIndex());
				} else if (o instanceof Wave) {
					gamePanel.addWave((Wave) o);
				} else if (o instanceof Statistics) {

					gamePanel.updateStats((Statistics) o);
				}
			}
		}
	}

	/**
	 * Updates the ability to tie based on the currentplayers currency and
	 * enmies currently in play
	 * 
	 * @param updatedSelfTieIsPossible
	 */
	public boolean checkForTie(boolean updatedSelfTieIsPossible) {
		if (!haveDisconnectedFromServer) {
			if (updatedSelfTieIsPossible && opponentTiePossible
					&& !game.isOver()) {
				try {
					game.setTied(true);
					output.reset();
					output.writeObject(tieString);
				} catch (IOException e) {
					System.out.println("failed to notify of tie");
					e.printStackTrace();
				}
				return true;
			}

			if (selfTiePossible == updatedSelfTieIsPossible) {
				return false;
			}

			System.out.println(playerNumber + " is tie possible: "
					+ updatedSelfTieIsPossible);
			selfTiePossible = updatedSelfTieIsPossible;
			if (selfTiePossible) {
				try {
					output.reset();
					output.writeObject(possibleTieString);
				} catch (IOException e) {
					System.out.println("failed to notify of possible tie");
					e.printStackTrace();
				}
			} else {
				try {
					output.reset();
					output.writeObject(impossibleTieString);
				} catch (IOException e) {
					System.out.println("failed to notify of impossible tie");
					e.printStackTrace();
				}
			}
		}
		return false;
	}

	/**
	 * Selects the map based on the difficulty
	 * 
	 * @param mapInt
	 */
	public void selectMap(int mapInt) {
		mapType = mapInt;
		try {
			output.reset();
			output.writeObject(mapType);
		} catch (IOException e) {
			System.out.println("Failed to choose map");
			e.printStackTrace();
		}
	}

	public void notifyOfLose() {
		if (!haveDisconnectedFromServer) {
			try {
				output.reset();
				output.writeObject(loseMessage);
			} catch (Exception e) {
				System.out.println("Error writing lose message to server");
				e.printStackTrace();
			}
			disconnectFromServer();
		}
	}

	public void quit() {
		notifyOfLose();
	}

	/**
	 * disconnects from the server
	 */
	private void disconnectFromServer() {
		incomingMessageReader.keepListening = false;
		if (!haveDisconnectedFromServer) {
			try {
				output.reset();
				output.writeObject(disconnectionMessage);
				haveDisconnectedFromServer = true;
			} catch (Exception e) {
				System.out
						.println("Error writing disconnect message to server");
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Adds a gameconditionobserver (gets notified of game state when game ends)
	 * @param o
	 */
	@Override
	public void addConditionObserver(GameConditionObserver o) {
		conditionObservers.add(o);
	}

	/**
	 * Notify gameconditionobservers of game state (win/lose/tie) when game ends
	 * @param condition
	 */
	@Override
	public void notifyObservers(int condition) {
		for (GameConditionObserver o : conditionObservers)
			if (o != null)
				o.updateGameCondition(condition);
	}
}