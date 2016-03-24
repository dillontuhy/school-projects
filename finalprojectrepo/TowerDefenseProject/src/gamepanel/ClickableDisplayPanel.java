package gamepanel;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import model.Clickable;
import observer.ClickableObserver;
import observer.EnemyHealthObserver;
import enemy.Enemy;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * 
 * This class holds the stats for the clickables.
 *
 */

public class ClickableDisplayPanel extends JPanel implements ClickableObserver,
		EnemyHealthObserver {

	JTextArea displayText;

	public ClickableDisplayPanel() {
		layoutGUI();
	}

	private void layoutGUI() {
		this.setBounds(5, 532 - 236, 285 - 118 - 6, 172);
		this.setLayout(null);
		displayText = new JTextArea("Nothing to see here");
		displayText.setBounds(0, 0, 283, 172);
		displayText.setEditable(false);
		displayText.setWrapStyleWord(true);
		displayText.setLineWrap(true);
		this.add(displayText);
	}

	@Override
	public void notifyOfClick(Clickable c) {
		if (c != null && c.isSelected())
			displayText.setText(c.toString());
		else
			displayText.setText("Nothing to see here");
	}

	@Override
	public void notifyOfUpdateTo(Enemy e) {
		if (e.isDead() || !e.isSelected())
			displayText.setText("Nothing to see here");
		else
			displayText.setText(e.toString());
	}

}
