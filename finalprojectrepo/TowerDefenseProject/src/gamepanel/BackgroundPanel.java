package gamepanel;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

/**
 * 
 * This class holds the map picture for the background of the game
 *
 */

public class BackgroundPanel extends JPanel {
	private BufferedImage currentImage;
	private String mapPic;
	
	public BackgroundPanel(JComponent comp, String mapPic){//(Rectangle bounds){
		this.mapPic = mapPic;
		loadImages();
		this.setBounds(comp.getBounds());
		comp.setLocation(0, 0);
		comp.setOpaque(false);
		this.setLayout(null);
		this.add(comp);
	}

	private void loadImages() {
		String baseDir = System.getProperty("user.dir")
				+ System.getProperty("file.separator") + "Images"
				+ System.getProperty("file.separator");
		try {
			currentImage = ImageIO.read(new File(baseDir + mapPic));
		} catch (IOException e) {
			System.out.println("Could not find Stars ");
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D gr = (Graphics2D) g;
		gr.drawImage(currentImage, 0, 0, null);
	}
}
