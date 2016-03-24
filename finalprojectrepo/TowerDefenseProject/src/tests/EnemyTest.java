package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import observer.EnemyHealthObserver;

import org.junit.Test;

import enemy.BigEnemy;
import enemy.BossEnemy;
import enemy.Enemy;
import enemy.SmallEnemy;
import gamepanel.ClickableDisplayPanel;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

public class EnemyTest {

	@Test
	public void smallEnemyValuesTest() {
		List<Point> path = new ArrayList<Point>();
		path.add(new Point(-44, 6 * 44 + 22));
		// small enemy
		Enemy e = new SmallEnemy(null, path);
		assertFalse(e.isDead());
		e.takeDamage(10);
		assertEquals(0, e.getHealth());
		e.takeDamage(90);
		assertEquals(0, e.getHealth());
		assertTrue(e.isDead());
		assertEquals(1, e.getGoldOnDeath());
		assertEquals(1, e.getPrice());
		assertEquals(new Color(255, 255, 0, 205),e.getEnemyColor());
		assertEquals("Enemy:\nGold on death: 1\nCost: 1\nSpeed: 5\nHealth: 0",e.toString());
		assertEquals(path,e.getPath());
	}
	
	@Test
	public void bigEnemyValuesTest() {
		List<Point> path = new ArrayList<Point>();
		path.add(new Point(-44, 6 * 44 + 22));
		// Big enemy
		Enemy e = new BigEnemy(null, path);
		assertFalse(e.isDead());
		e.takeDamage(15);
		assertEquals(0, e.getHealth());
		e.takeDamage(90);
		assertEquals(0, e.getHealth());
		assertTrue(e.isDead());
		assertEquals(2, e.getGoldOnDeath());
		assertEquals(2, e.getPrice());
		assertEquals(new Color(0, 255, 255, 205),e.getEnemyColor());
		assertEquals("Enemy:\nGold on death: 2\nCost: 2\nSpeed: 6\nHealth: 0",e.toString());
	}
	
	@Test
	public void bossEnemyValuesTest() {
		List<Point> path = new ArrayList<Point>();
		path.add(new Point(-44, 6 * 44 + 22));
		// Boss enemy
		Enemy e = new BossEnemy(null, path);
		assertFalse(e.isDead());
		e.takeDamage(15);
		assertEquals(5, e.getHealth());
		e.setSelected(true);
		e.takeDamage(5);
		assertEquals(0, e.getHealth());
		assertTrue(e.isDead());
		assertEquals(3, e.getGoldOnDeath());
		assertEquals(3, e.getPrice());
		assertEquals(new Color(255, 0, 255, 205),e.getEnemyColor());
		assertEquals(path,e.getPath());
		assertEquals("Enemy:\nGold on death: 3\nCost: 3\nSpeed: 7\nHealth: 0",e.toString());
	}
	
	@Test
	public void testGeneralEnemyThings(){
		List<Point> path = new ArrayList<Point>();
		path.add(new Point(-44, 6 * 44 + 22));
		path.add(new Point(0,0));
		Enemy e = new BigEnemy(null, path);
		
		assertFalse(e.isDead());
		assertEquals(path,e.getPath());
		e.setGameLocation(path.get(0));
		e.moveAndAnimate();
		assertTrue(e.hasCrossedFinishLine());
		EnemyHealthObserver eho = new ClickableDisplayPanel();
		e.addEnemyHealthObserver(eho);
		e.notifyOfUpdate();
		e.removeEnemyHealthObserver(eho);
	}
	
	@Test public void testEnemyMovement()
	{
		//after first point of path, all others are relative
		List<Point> path = new ArrayList<Point>();
		path.add(new Point(-44, 6 * 44 + 22));
		path.add(new Point(1,0));
		path.add(new Point(0,0));
		for (int i=0; i<7; i++)
			path.add(new Point(0,1));
		path.add(new Point(0,0));
		for (int i=0; i<7; i++)
			path.add(new Point(1,0));
		Enemy e = new BigEnemy(null, path);
		Enemy e2 = new SmallEnemy(null,path);
		
		assertEquals((double)path.get(0).x,e.getGameLocation().x,15);
		assertEquals((double)path.get(0).y,e.getGameLocation().y,15);
		//compareTo is based on enemy's location on the map
		assertEquals(0, e2.compareTo(e));
		e.moveAndAnimate();
		assertEquals(6, e2.compareTo(e));
		e2.moveAndAnimate();
		assertEquals(1, e2.compareTo(e));
		e.moveAndAnimate();
		e.moveAndAnimate();
		//enemy has moved far enough that it's done with our minipath
		assertTrue(e.hasCrossedFinishLine());
	}
}
