package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.ArrayList;

import map.MapCoordinator;
import map.MapPanel;
import map.Tile;
import model.ClickableManager;
import model.Game;
import model.Statistics;
import model.Wave;
import model.WaveClickable;
import observer.ClickableObserver;

import org.junit.Test;

import enemy.EnemyType;

/**
 * @author Dillon Tuhy, Lamec Fletez, Laura Vonessen, Martin Stankard
 *
 */

public class ModelTest {
	
	@Test
	public void gameTest() {
		Game game = new Game(false);
		assertFalse(game.isMultiPlayer());
		//make sure initial values are correct
		assertEquals(100, game.getCurrency());
		game.setCurrency(150);
		assertEquals(150, game.getCurrency());
		assertEquals(10, game.getLives());
		//make sure can set store and map
		game.setStore(null);
		game.setMap(null);
		//make sure game isn't over
		assertFalse(game.hasWon());
		assertFalse(game.hasLost());
		assertFalse(game.isOver());
		//make sure pausing works
		assertFalse(game.isPaused());
		game.setPaused(true);
		assertTrue(game.isPaused());
		game.setPaused(false);
		//make sure setting lives works
		game.setLives(1);
		assertEquals(1, game.getLives());
		assertTrue(game.getTowers().isEmpty());
		//make sure hasn't killed anything
		assertEquals(0,game.getKills());
		for (int i=0; i<830; i++)
			game.addWinPoint();
		//make sure has killed 830 enemies
		assertEquals(830,game.getKills());
		//make sure game has won
		assertTrue(game.isOver());
		assertTrue(game.hasWon());
		assertFalse(game.hasTied());
		assertFalse(game.hasLost());
		
		game = new Game(false);
		game.setLives(0);
		//make sure setting lives to 0 makes the game lose
		assertTrue(game.isOver());
		assertTrue(game.hasLost());
		assertFalse(game.hasTied());
		assertFalse(game.hasWon());
		
		//make sure multiplayer setTied works
		game = new Game(true);
		assertTrue(game.isMultiPlayer());
		game.setTied(true);
		assertTrue(game.isOver());
		assertTrue(game.hasTied());
		assertFalse(game.hasWon());
		assertFalse(game.hasLost());
	}
	
	@Test
	public void statisticsTest(){
		//make sure statistics class works correctly
		Statistics stats = new Statistics();
		//test init values
		assertEquals(0,stats.getGold());
		assertEquals(0,stats.getHealth());
		assertEquals(0,stats.getKills());
		assertEquals(0,stats.getWavesSent());
		//test setters/getters
		stats.setHealth(5);
		assertEquals(5,stats.getHealth());
		stats.addGold(5);
		assertEquals(5,stats.getGold());
		stats.addGold(5);
		assertEquals(10,stats.getGold());
		stats.addKills(5);
		assertEquals(5,stats.getKills());
		stats.addKills(5);
		assertEquals(10,stats.getKills());
		stats.setWavesSent(5);
		assertEquals(5,stats.getWavesSent());
	}
	
	@Test
	public void waveAndWaveClickableTest(){
		//test wave and waveclickable
		Wave wave = new Wave(5, 10, 15);
		//make sure init values correct
		assertEquals(5,wave.getNumEnemiesOfType(EnemyType.SMALL));
		assertEquals(10,wave.getNumEnemiesOfType(EnemyType.BIG));
		assertEquals(15,wave.getNumEnemiesOfType(EnemyType.BOSS));
		assertEquals(30,wave.size(),0.001);
		assertEquals("Wave:\nSmall enemies: 5\nBig enemies: 10\nBoss enemies: 15",wave.toString());
		//make a MapCoordinator so you can send off the wave
		MapCoordinator mc = new MapCoordinator(null, new Game(true), 2, null, null, 100, null);
		wave.setMapCoordinator(mc);
		wave.start();
		try
		{
			Thread.sleep(2000);
		} catch (InterruptedException e)
		{
			System.out.println("sleeping didn't work");
		}
		wave.pause();
		//make sure these two methods work
		wave.resume();
		try
		{
			Thread.sleep(2000);
		} catch (InterruptedException e)
		{
			System.out.println("sleeping didn't work");
		}
		//wave over, so nothing happens
		wave.resume();
		
		//make a new waveclickable
		WaveClickable wc = new WaveClickable(null, wave, 100);
		//test init values/getters/setters
		assertEquals(100,wc.getPrice());
		assertEquals(wave,wc.getWave());
		assertEquals(null,wc.getGameLocation());
		assertEquals("Wave:\nSmall enemies: 5\nBig enemies: 10\nBoss enemies: 15\nPrice: 100",wc.toString());
		assertTrue(wc.getNewInstanceOf() instanceof WaveClickable);
	}
	
	@Test
	public void testClickableManager()
	{
		//test the clickable manager--make sure it does things as expected
		Game gm = new Game(false);
		gm.setMap(new MapPanel(null, 2));
		ClickableManager cm = new ClickableManager(gm, null);
		gm.addObserver(cm);
		List<ClickableObserver> co = new ArrayList<ClickableObserver>();
		co.add(cm);
		Tile t = new Tile(0, 0, co);

		//make sure clickable observable things work--that clickables are correctly selected/deselected
		assertEquals(null,cm.getSelected());
		t.setSelected(true);
		t.notifyClickableObservers();
		assertEquals(t,cm.getSelected());
		t.setSelected(false);
		t.notifyClickableObservers();
		assertEquals(null,cm.getSelected());
		
		t.setSelected(true);
		t.notifyClickableObservers();
		assertEquals(t,cm.getSelected());
		
		Tile t1 = new Tile(1,1,co);
		t1.setSelected(true);
		t1.notifyClickableObservers();
		assertEquals(t1,cm.getSelected());
		assertFalse(t.isSelected());
		
		//make sure this calls clickablemanager (through coverage)
		gm.setCurrency(50);
	}
}
