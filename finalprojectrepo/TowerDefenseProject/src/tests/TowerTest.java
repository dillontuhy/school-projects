package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;

import map.MapPanel;

import org.junit.Test;

import tower.Laser;
import tower.LaserTower;
import tower.RadiationTower;
import tower.RingTower;
import tower.Tower;
import tower.TowerInfo;
import tower.TowerType;
import enemy.Enemy;
import enemy.SmallEnemy;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

public class TowerTest
{

	/**
	 * this method checking the red tower's variables via 
	 * getters and settersagainst a small enemy wave
	 */
	@Test
	public void laserTowerTest()
	{
		Tower laser = new LaserTower(null);
		Point location = new Point(22, 5 * 44 + 22);
		List<Enemy> eList = new ArrayList<Enemy>();
		List<Point> path = (new MapPanel(null, 2)).getPath(0);
		Enemy e = new SmallEnemy(null, path);
		eList.add(e);

		assertEquals(132, laser.getRange());
		assertEquals(3, laser.getDamage());
		assertEquals(25, laser.getPrice());
		laser.setGameLocation(location);
		assertEquals(location, laser.getGameLocation());
		assertEquals(TowerType.LASER, laser.getTowerType());
		assertEquals(laser.getRange() * 2, ((Ellipse2D.Double) laser.getRangeShape()).width, 0.0001);
		assertEquals("LaserTower:\nRange: 132\nDamage: 3\nPrice: 25", laser.toString());
		assertTrue(laser.getNewInstanceOf() instanceof LaserTower);
		assertEquals(0.5f, laser.getRangeAlpha(), 0.001);
		laser.detectEnemies(eList);// detect enemies in the list and damage them
		e.moveAndAnimate();
		laser.detectEnemies(eList);// detect enemies in the list and damage them
		e.moveAndAnimate();
		assertFalse(e.isDead());
		laser.detectEnemies(eList);// detect enemies in the list and damage them
		e.moveAndAnimate();
		laser.detectEnemies(eList);// detect enemies in the list and damage them
		e.moveAndAnimate();
		assertFalse(e.getHealth() == 10);
		Laser l = ((LaserTower) laser).getLaser();
		assertEquals(l.getLaserLine().x1, laser.getGameLocation().x, 0.001);
		assertTrue(l.getBrushStrokeWidth() >= 0);
		laser.detectEnemies(eList);// detect enemies in the list and damage them
		e.moveAndAnimate();
		laser.detectEnemies(eList);// detect enemies in the list and damage them
		e.moveAndAnimate();
		assertTrue(e.isDead());
	}

	
	/**
	 * this method is checking the blue tower's variables via 
	 * getters and setters against a small enemy wave.
	 */	
	@Test
	public void radiationTowerTest()
	{
		Tower rad = new RadiationTower(null);
		Point location = new Point(22, 5 * 44 + 22);
		List<Enemy> eList = new ArrayList<Enemy>();
		List<Point> path = (new MapPanel(null, 2)).getPath(0);
		Enemy e = new SmallEnemy(null, path);
		eList.add(e);

		assertEquals(110, rad.getRange());
		assertEquals(1, rad.getDamage());
		assertEquals(30, rad.getPrice());
		rad.setGameLocation(location);
		assertEquals(location, rad.getGameLocation());
		assertEquals(TowerType.RADIATION, rad.getTowerType());
		assertEquals(rad.getRange() * 2, ((Ellipse2D.Double) rad.getRangeShape()).width, 0.0001);
		assertEquals("RadiationTower:\nRange: 110\nDamage: 1\nPrice: 30", rad.toString());
		assertTrue(rad.getNewInstanceOf() instanceof RadiationTower);
		assertEquals(0.5f, rad.getRangeAlpha(), 0.001);
		assertFalse(e.isDead());
		for (int i=0; i<10; i++){
			rad.detectEnemies(eList);// detect enemies in the list and damage them
			e.moveAndAnimate();
		}
		assertTrue(e.isDead());
	}

	/**
	 * this method is checking the green tower's variables via 
	 * getters and setters against a small enemy wave.
	 */	
	@Test
	public void ringTowerTest()
	{
		Tower ring = new RingTower(null);
		Point location = new Point(22, 5 * 44 + 22);
		List<Enemy> eList = new ArrayList<Enemy>();
		List<Point> path = (new MapPanel(null, 2)).getPath(0);
		Enemy e = new SmallEnemy(null, path);
		eList.add(e);

		assertEquals(198, ring.getRange());
		assertEquals(1, ring.getDamage());
		assertEquals(50, ring.getPrice());
		ring.setGameLocation(location);
		assertEquals(location, ring.getGameLocation());
		assertEquals(TowerType.RING, ring.getTowerType());
		assertEquals(ring.getRange() * 2, ((Ellipse2D.Double) ring.getRangeShape()).width, 0.0001);
		assertEquals("RingTower:\nRange: 198\nDamage: 1\nPrice: 50", ring.toString());
		assertTrue(ring.getNewInstanceOf() instanceof RingTower);
		assertEquals(0.3f, ring.getRangeAlpha(), 0.001);
		assertFalse(e.isDead());
		assertEquals(10,e.getHealth());
		for (int i=0; i<100; i++){
			ring.detectEnemies(eList);// detect enemies in the list and damage them
			e.moveAndAnimate();
		}
		assertTrue(e.getHealth()!=10);
		assertTrue(((Ellipse2D.Double)((RingTower)ring).getAttackRing()).width<=ring.getRange()*2);
	}

	/**
	 * this method is testing the placement process for a tower
	 */
	@Test
	public void testTowerInfo()
	{
		TowerInfo ti = new TowerInfo(TowerType.LASER, 2, 0);
		assertEquals(TowerType.LASER, ti.getTowerType());
		assertEquals(2, ti.getXIndex());
		assertEquals(0, ti.getYIndex());
	}
}
