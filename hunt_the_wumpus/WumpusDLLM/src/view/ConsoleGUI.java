//Dillon Tuhy, Lamec Angel Gabriel Fletez Reyes, Laura Vonessen, Martin Stankard
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import model.Game;
import observer.WumpObserver;
/**
 * This class presents our console view of the game
 */
@SuppressWarnings("serial")
public class ConsoleGUI extends JPanel implements WumpObserver {

	// Instance variables
	private JScrollPane scroller;
	public static final String helpString = "The Wumpus lives in a cave of 20 cave rooms.  Each cave room has three tunnels leading to other cave rooms.  The tunnel system is three dimensional roughly in the shape of a dodecahedron.  If you do not know about dodecahedrons, Google it. \n\n"
			+ "Hazards: There are two hazards you must be aware of:\n1)	Bottomless pits – two rooms have bottomless pits in them.  If you go there, you fall into the pit and lose.\n2)	Bats – two other (rooms have super bats. If you dare enter their cave room, the gaggle of bats carry you to some other cave room.  This can be a hazardous because if you are flown to a cave room with a pit or the Wumpus, you lose.\n\n"
			+ "Warnings: When you are one cave room away from a Wumpus or hazard, you will be warned:\n1)	Wumpus: ‘Smell something foul’\n2)	Bats: ‘Hear squeaking’\n3)	Pit: ‘Feel a draft’\n\n"
			+ "Wumpus: The hunted and the hunter.  Two interactions can occur:\n1)	If the Wumpus is shot by your arrow, you win.\n2)	If you walk into the cave room with the Wumpus, you lose.\n\n"
			+ "You: Each turn you may move or shoot a crooked arrow:\n1)	Moving: you can move one cave room (through one tunnel).\n2)	Shooting Arrows: You have 3 arrows.  You lose when you shoot without having any arrows left. Each arrow can travel from 1 to 5 rooms.  You aim by listing the rooms you want the arrow to go to.\n	-If the arrow can’t travel the path, it hits a wall and falls harmlessly.\n	-If the arrow gets to a cave room with the Wumpus, you win.\n	-If the arrow hits you, you lose.\n\n"
			+ "To restart the level, enter the keyword RESTART into either textbox.  To start a new game, enter the keyword NEW into either textbox.  To see this message again, enter the keyword HELP.  To clear the text area, enter the keyword CLEAR.  To enter the hard-coded test game, enter the keyword TEST.  To enter the other hard-coded test game (like the original, but with the hunter starting in room 18 and the wumpus in room 17), enter the keyword TEST2.  To see the locations of all special caves, enter the keyword GOD MODE.\n\n"
			+ "Enter rooms numbers below to Move or Shoot Arrows (separate Arrow destination rooms with spaces)\n\n";
	private JTextArea gameStatus = new JTextArea(helpString);
	private JTextField moveInput = new JTextField("");
	private JTextField arrowInput = new JTextField("");
	private JLabel move = new JLabel(" Input the room you wish to Explore");
	private JLabel arrow = new JLabel(" Input the path of your Arrow");
	private Game currentGame;
	private Font font = new Font("Courier", Font.PLAIN, 14);

	/**
	 * This constructor initializes instance variables and sets up the view.
	 */
	public ConsoleGUI(Game currentGame) {
		this.currentGame = currentGame;
		currentGame.addObserver(this);
		layoutGUI(); 
		registerListeners();
	}

	/**
	 * This method registers listeners.
	 */
	private void registerListeners() {

		ActionListener moveListener = new MoveListener();
		ActionListener arrowListener = new ArrowListener();

		moveInput.addActionListener(moveListener);
		arrowInput.addActionListener(arrowListener);
	}

	/**
	 * This method lays out the view
	 */
	private void layoutGUI() {

		this.setPreferredSize(new Dimension(700, 600));

		gameStatus.setFont(font);
		gameStatus.setForeground(Color.GREEN);
		gameStatus.setBackground(Color.BLACK);
		gameStatus.setEditable(false);

		gameStatus.setLineWrap(true);
		gameStatus.setWrapStyleWord(true);

		moveInput.setFont(font);
		moveInput.setForeground(Color.GREEN);
		moveInput.setBackground(Color.BLACK);

		arrowInput.setFont(font);
		arrowInput.setForeground(Color.GREEN);
		arrowInput.setBackground(Color.BLACK);

		moveInput.setCaretColor(Color.GREEN);
		arrowInput.setCaretColor(Color.GREEN);

		scroller = new JScrollPane(gameStatus);

		gameStatus.append(currentGame.getState());

		scroller.setPreferredSize(new Dimension(700, 530));
		this.add(scroller, BorderLayout.CENTER);

		// Need input areas, so put them in a JPanel.
		JPanel panel = new JPanel();

		panel.add(move);
		move.setForeground(Color.GREEN);
		panel.setBackground(Color.BLACK);
		panel.add(moveInput);
		panel.add(arrow);
		arrow.setForeground(Color.GREEN);

		panel.add(arrowInput);
		move.setFont(font);
		arrow.setFont(font);
		panel.setLayout(new GridLayout(2, 2, 10, 0));
		this.add(panel, BorderLayout.SOUTH);

		this.setBackground(Color.BLACK);

	}

	/**
	 * Inner class to listen to events from move input
	 */
	private class MoveListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (checkForKeywords()) {
				return;
			}

			gameStatus.append("You chose room " + moveInput.getText() + "\n");
			if (currentGame.isDead()) {
				gameStatus.append("Dead people can't move.\n\n");
				return;
			}
			String moveCave = moveInput.getText();
			moveInput.setText("");
			int move;
			try {
				move = Integer.parseInt(moveCave);
			} catch (Exception e) {
				gameStatus
						.append("Invalid input.  Please put in an integer.\n\n");
				return;
			}

			if (!currentGame.canMove(move)) {
				gameStatus
						.append("Invalid input.  Please put in a valid room.\n\n");
				return;
			}
			//now the input is valid:
			currentGame.moveRoom(move);
		}

	}

	/**
	 * This method appends the status of the game
	 * and scrolls to the end of the text area like a 
	 * real console
	 */
	private void scrollToEnd() {
		gameStatus.append(currentGame.getState());
		gameStatus.setCaretPosition(gameStatus.getDocument().getLength());
	}

	/**
	 * Inner class to listen to events from arrow input
	 */
	private class ArrowListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (checkForKeywords()) {
				return;
			}

			gameStatus.append(currentGame.shootArrow(arrowInput.getText()));
			arrowInput.setText("");
		}

	}
	
	/**
	 * This method checks for keywords in the input from the text areas
	 * @return Whether or not the input contained any keywords
	 */
	private boolean checkForKeywords() {
		if (moveInput.getText().toLowerCase().equals("restart")
				|| moveInput.getText().toLowerCase().equals("reset")
				|| arrowInput.getText().toLowerCase().equals("restart")
				|| arrowInput.getText().toLowerCase().equals("reset")) {
			currentGame.resetLevel();
			moveInput.setText("");
			arrowInput.setText("");
			return true;
		}
		if (moveInput.getText().toLowerCase().equals("new")
				|| arrowInput.getText().toLowerCase().equals("new")) {
			currentGame.newGame();
			moveInput.setText("");
			arrowInput.setText("");
			return true;
		}
		if (moveInput.getText().toLowerCase().equals("god mode")
				|| arrowInput.getText().toLowerCase().equals("god mode")) {
			moveInput.setText("");
			arrowInput.setText("");
			gameStatus.append(currentGame.toString() + "\n");
			scrollToEnd();
			return true;
		}
		if (moveInput.getText().toLowerCase().equals("help")
				|| arrowInput.getText().toLowerCase().equals("help")) {
			moveInput.setText("");
			arrowInput.setText("");
			gameStatus.append(helpString);
			scrollToEnd();
			return true;
		}
		if (moveInput.getText().toLowerCase().equals("test")
				|| arrowInput.getText().toLowerCase().equals("test")) {
			moveInput.setText("");
			arrowInput.setText("");
			currentGame.testValues();
			return true;
		}

		if (moveInput.getText().toLowerCase().equals("test2")
				|| arrowInput.getText().toLowerCase().equals("test2")) {
			moveInput.setText("");
			arrowInput.setText("");
			currentGame.test2Values();
			return true;
		}
		if (moveInput.getText().toLowerCase().equals("clear")
				|| arrowInput.getText().toLowerCase().equals("clear")) {
			moveInput.setText("");
			arrowInput.setText("");
			gameStatus.setText("");
			scrollToEnd();
			return true;
		}
		return false;
	}

	/**
	 * This method is from WumpusObserver; it allows this class to observe
	 * the game.
	 */
	@Override
	public void update() {
		if (currentGame.getArrowString().length() != 0) {
			gameStatus.append("You chose to shoot into the following rooms: "
					+ currentGame.getArrowString() + "\n");
			currentGame.setArrowString("");
			if (!currentGame.isDead() && !currentGame.hasWon()) {
				gameStatus.append("Oh no, you missed.\n\n");
			}
		}
		scrollToEnd();
	}
}
