package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.awt.Point;
import java.util.List;

import map.MapPanel;
import map.Tile;

import org.junit.Test;

import tower.RadiationTower;
import tower.TowerType;

public class MapTest
{
	
	@Test
	public void testTiles() {
		Tile t = new Tile(1,2,null);//y,x,clickobservers
		t.setSelected(true);
		//default is open tile
		assertTrue(t.canPlaceTower());
		assertEquals("Open",t.getType());
		t.setType("Path");
		//can't place towers on path tiles
		assertFalse(t.canPlaceTower());
		assertEquals("Path",t.getType());
		assertNotSame("Tower",t.getType());
		//tiles don't have a price
		assertEquals(0,t.getPrice());
		assertEquals("Path Tile\nX index: 2\nY index: 1",t.toString());
		//make sure these tiles match
		Tile t2 = (Tile)t.getNewInstanceOf();
		//set tile width and height since they're not in the map right now
		t.setBounds(0,0,44,44);
		t2.setBounds(0,0,44,44);
		assertEquals(new Point(2*44+22,1*44+22), t.getGameLocation());
		assertEquals(t.getGameLocation(),t2.getGameLocation());
	}

	@Test
	public void testEasyMapPath()
	{
		MapPanel map = new MapPanel(null, 1);//easy/simple path--no fork
		List<Point> path1 = map.getPath(0);
		List<Point> path2 = map.getPath(1);
		assertEquals(1791,path1.size());
		assertEquals(0,path2.size());
		int numCorners = 0;
		for (Point p: path1)
			if (p.x==0&&p.y==0)
				numCorners ++;
		//simple path has 8 corners
		assertEquals(8,numCorners);
	}
	
	@Test
	public void testHardMapPath()
	{
		MapPanel map = new MapPanel(null, 2);//hard path--has fork
		List<Point> path1 = map.getPath(0);
		List<Point> path2 = map.getPath(1);
		assertEquals(1791,path1.size());
		assertEquals(1749,path2.size());
		int numCorners = 0;
		for (Point p: path1)
			if (p.x==0&&p.y==0)
				numCorners ++;
		//path1 (top fork) has 8 corners
		assertEquals(8,numCorners);
		numCorners = 0;
		for (Point p: path2)
			if (p.x==0&&p.y==0)
				numCorners ++;
		//path2 (lower fork) has 10 corners
		assertEquals(10,numCorners);
	}

	@Test
	public void testPlaceTowers()
	{
		MapPanel map = new MapPanel(null, 2);//hard path--has fork
		//make sure no exceptions thrown, that sort of thing
		map.placeTower(new Tile(0, 0, null), new RadiationTower(null));
		map.placeMiniMapTower(TowerType.LASER, 0, 1);
		map.placeMiniMapTower(TowerType.RADIATION, 1, 1);
		boolean mapIsAwesome = true;
		assertTrue(mapIsAwesome);
	}
}
