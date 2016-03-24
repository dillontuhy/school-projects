package _start;
import view.GameFrame;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

public class Start {

	/**
	 * @param args
	 * 
	 * This main sets the Game Frame as visible
	 */
	public static void main(String[] args) {
		GameFrame NG = new GameFrame();
		NG.pack();
		NG.setVisible(true);
		NG.setResizable(false);
	}
}