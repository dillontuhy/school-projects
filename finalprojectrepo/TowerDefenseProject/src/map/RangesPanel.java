package map;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

import tower.Laser;
import tower.RingTower;
import tower.Tower;

import composite.BlendComposite;
import composite.BlendComposite.BlendingMode;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * this class is where tower fx are drawn, to be layered on top of the map
 *
 */
public class RangesPanel extends JPanel {

	private List<Tower> towers;
	private Graphics2D g;
	private List<Laser> lasers;

/**
 * this method takes care of low level gui overhead 
 * @param towers
 */
	public RangesPanel(List<Tower> towers) {
		this.setOpaque(false);
		this.setLayout(null);
		this.towers = towers;
		lasers = new ArrayList<Laser>();
	}
	
/**
 * this method turns off the laser drawing 
 * (to be used when there are no more enemies to attack)
 */
	public void clearLasers() {
		lasers.clear();
	}
/**
 * this method draws the laser
 * (to be used when there are enemies to attack)
 * @param l
 */
	public void drawLaser(Laser l) {
		if (l == null)
			return;
		lasers.add(l);
	}
	

/**
 * this method draws various tower fx components; green ring, red laser, ranges
 */
	public void paintComponent(Graphics g2) {
		super.paintComponent(g2);
		g = (Graphics2D) g2;
		

		Stroke s = g.getStroke();
		g.setStroke(new BasicStroke (15));

		for (Tower t : towers) {
			g.setComposite(BlendComposite.getInstance(BlendingMode.ADD,
					t.getRangeAlpha()));
			g.setColor(t.getTowerColor());
			if (t instanceof RingTower)
				g.draw(((RingTower)t).getAttackRing());
			g.fill(t.getRangeShape());
		}

		g.setColor(Color.WHITE);
		for (Laser l : lasers) {
			g.setComposite(BlendComposite.getInstance(BlendingMode.DIFFERENCE));
			g.setStroke(new BasicStroke(l.getBrushStrokeWidth()));
			g.draw(l.getLaserLine());
		}
		g.setStroke(s);
	}
}
