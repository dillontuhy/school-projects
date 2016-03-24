package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * This server runs in its own thread. It accepts requests to connect from
 * clients. Each connection accepted causes a new Thread to handle that client,
 * or in other words there is one thread per client on the server side.
 * 
 * The server also writes input from one client to all client's chat screens.
 */

public class GameServer implements Runnable {

	private ServerSocket myServerSocket; // client request source

	public final static int PORT_NUMBER = 4009;
	public final static String player1Message = "Got player 1; waiting for player 2",
			mapMessage = "map";
	private boolean reloadPlayer1;
	private ObjectOutputStream player1Out;
	private ObjectInputStream player1In;
	private Socket savedPlayer1;

	/**
	 * Sets up server
	 */
	public static void main(String args[]) {
		GameServer server = new GameServer();
		// always call the start() method on a Thread. Don't call the run
		// method.
		Thread serverThread = new Thread(server);
		serverThread.start();
		System.out.println("This is the server's console.");
	}

	/**
	 * Constructor initializes the server with the port number
	 */
	public GameServer() {
		try {
			myServerSocket = new ServerSocket(PORT_NUMBER);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method temporarily saves information about the second player when the second exits before choosing a map,
	 * making the second player the new first player
	 * @param oos
	 * @param ois
	 * @param player
	 */
	private void savePlayer(ObjectOutputStream oos, ObjectInputStream ois, Socket player) {
		reloadPlayer1 = true;
		player1Out = oos;
		player1In = ois;
		savedPlayer1 = player;
	}

	
	/**
	 * This method listens for connections and creates streams
	 */
	@Override
	public void run() {
		try {
			while (true) {
				// accept blocks until request comes over socket
				Socket player1 = null;
				ObjectInputStream player1Input = null;
				ObjectOutputStream player1Output = null;

				if (reloadPlayer1) {
					reloadPlayer1 = false;
					player1 = savedPlayer1;
					player1Input = player1In;
					player1Output = player1Out;
				} else {
					player1 = myServerSocket.accept();

					player1Input = new ObjectInputStream(
							player1.getInputStream());
					player1Output = new ObjectOutputStream(
							player1.getOutputStream());
				}

				player1Output.writeObject(player1Message);

				Object mapType = null;
				try {
					mapType = player1Input.readObject();
				} catch (Exception e) {
					System.out.println("map wasn't sent correctly");
					//e.printStackTrace();
					continue;
				}

				if (!(mapType instanceof Integer)) {
					continue;
				}

				// accept blocks until request comes over socket
				Socket player2 = null;
				try {
					player2 = myServerSocket.accept();
				} catch (Exception e) {
					System.out
							.println("second player didn't connect correctly");
					//e.printStackTrace();
					continue;
				}

				ObjectInputStream player2Input = new ObjectInputStream(
						player2.getInputStream());
				ObjectOutputStream player2Output = new ObjectOutputStream(
						player2.getOutputStream());

				player2Output.writeObject(GameServer.mapMessage + mapType);
				try {
					player1Output.writeObject(player2Input.readObject());
				} catch (Exception e) {
					savePlayer(player2Output,player2Input, player2);
					System.out.println("failed to pass along start message");
					continue;
				}

				// For each player, provide access to other player's
				// input/output
				ConnectionThreadForEachPlayer threadForPlayer1 = new ConnectionThreadForEachPlayer(
						player1, player1Input, player2Output);
				ConnectionThreadForEachPlayer threadForPlayer2 = new ConnectionThreadForEachPlayer(
						player2, player2Input, player1Output);

				Thread thread1 = new Thread(threadForPlayer1);
				Thread thread2 = new Thread(threadForPlayer2);
				// always call the start() method on a Thread.
				thread1.start();
				thread2.start();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}