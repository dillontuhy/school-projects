package networking;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * Each instance of this class allows a client to send messages to all other clients.
 * It also handles clean-up when client quits.
 */
class ConnectionThreadForEachPlayer implements Runnable {

	private Socket playerSocket;

	private ObjectInputStream readFromSelf;
	private ObjectOutputStream writeToOther;

	/**
	 * Construct an object that will run in its own Thread.
	 */
	public ConnectionThreadForEachPlayer(Socket socketFromServer,
			ObjectInputStream ois, ObjectOutputStream oos) {
		this.playerSocket = socketFromServer;
		readFromSelf = ois;
		writeToOther = oos;
	}

	/**
	 * The void run() method in class Thread must be overridden in every class
	 * that extends Thread. The Server created a new instance of this class and
	 * sends it a start method, which is in Thread. The start method creates a new
	 * Thread which then calls the following run method where our domain specific
	 * logic goes.
	 */
	@Override
	public void run() {
		Object messageFromThisPlayer = null;
		boolean wantToStayConnected = true;

		// Loop as long as we are connected using a boolean variable
		while (wantToStayConnected) {
			try {
				messageFromThisPlayer = readFromSelf.readObject();
			} catch (IOException e1) {
				System.out
						.println("IOException in ServerThread.run when reading from client");
				return;
			} catch (ClassNotFoundException e1) {
				System.out
						.println("ClassCastException in ServerThread.run casting object from client to String");
				e1.printStackTrace();
			}

			try { // Process the input

				writeToOther.writeObject(messageFromThisPlayer);

				if (messageFromThisPlayer == null) {
					// Process a closed client
					// Clean up and avoid exceptions
					System.out
							.println("Got sent a null object, so assume you want to quit.");
					playerSocket.close();
					readFromSelf.close();
					writeToOther.close();
					wantToStayConnected = false;
					return;
				}

			} catch (Exception e) {
				wantToStayConnected = false;
				return;
			}
		}
	}
}