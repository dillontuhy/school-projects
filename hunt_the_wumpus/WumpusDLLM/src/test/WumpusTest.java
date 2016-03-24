//Dillon Tuhy, Lamec Angel Gabriel Fletez Reyes, Laura Vonessen, Martin Stankard
package test;

import static org.junit.Assert.*;

import model.Game;

import observer.WumpObserver;

import org.junit.Test;

/**
 * This is our JUnit tests
 */
public class WumpusTest implements WumpObserver {

	@Test
	public void gameTest() {
		Game firstGame = new Game();
		firstGame.testValues();
		int[] arrows = { 3, 12, 13, 17, 16 };
		assertTrue(firstGame.shootArrow(arrows));
		assertTrue(firstGame.shootArrow(arrows));
		assertTrue(firstGame.shootArrow(arrows));
		assertFalse(firstGame.shootArrow(arrows));
		assertTrue(firstGame.hasWon());

		Game firstGame2 = new Game();
		firstGame2.testValues();
		int[] arrowsz = { 3, 12, 13, 17, 9 };
		assertFalse(firstGame2.shootArrow(arrowsz));

		Game firstGame3 = new Game();
		firstGame3.testValues();
		int[] arr = { 3, 12, 13, 17, 18 };
		assertFalse(firstGame3.shootArrow(arr));
		
		Game firstGame4 = new Game();
		firstGame4.testValues();
		int[] arrz = {12,3 };
		assertFalse(firstGame4.shootArrow(arrz));
	}

	@Test
	public void emptypath() {
		Game firstGame = new Game();
		firstGame.testValues();
		int[] arrows = {};
		assertFalse(firstGame.shootArrow(arrows));

	}


	@Test
	public void deathbywumpus2() {
		Game firstGame = new Game();
		firstGame.testValues();
		assertEquals(2, firstGame.getCurrentRoom());
		firstGame.moveRoom(3);
		firstGame.moveRoom(4);
		firstGame.moveRoom(14);
		firstGame.moveRoom(15);
		assertEquals(15, firstGame.getCurrentRoom());
		firstGame.moveRoom(16);
	}

	@Test
	public void deathbyfalling() {
		Game firstGame = new Game();
		firstGame.testValues();
		assertEquals(2, firstGame.getCurrentRoom());
		firstGame.moveRoom(3);
		firstGame.moveRoom(4);
		firstGame.moveRoom(5);
		firstGame.moveRoom(6);
		assertEquals(6, firstGame.getCurrentRoom());
		firstGame.moveRoom(7);
	}

	@Test
	public void bats() {
		Game firstGame = new Game();
		firstGame.testValues();
		assertEquals(2, firstGame.getCurrentRoom());
		firstGame.moveRoom(1);

	}
	
	@Test
	public void testToString()
	{
		Game firstGame = new Game();
		System.out.println(firstGame);
	}
	
	@Test
	public void testNewGame()
	{
		Game firstGame = new Game();
		firstGame.newGame();
		System.out.println(firstGame);
		
	}
	@Test
	public void testShootString(){
		Game firstGame= new Game();
		firstGame.test2Values();
		firstGame.addObserver(this);
		firstGame.notifyObservers();
		assertEquals( "",firstGame.shootArrow("17"));
		firstGame.getState();
		
		Game firstGame2= new Game();
		firstGame2.test2Values();
		firstGame2.shootArrow("11 18");
		assertTrue(firstGame2.isDead());
		assertEquals("You shot yourself in room ",firstGame2.getCauseOfDeath());
		firstGame2.moveRoom(11);
		assertFalse(firstGame2.canMove(11));
		assertEquals("11 18",firstGame2.getArrowString());
		assertEquals("Dead people can't shoot.\n\n",firstGame2.shootArrow("17"));
		firstGame2.batRelocator(17);
		firstGame2.resetLevel();
		assertEquals("Invalid input.  Please put in only integers, and only separate them with spaces.\n\n",firstGame2.shootArrow("blah"));
		assertEquals("Invalid input.  Please do not put more than 5 integers.\n\n",firstGame2.shootArrow("11 10 2 1 20 7 8"));
		assertEquals(3,firstGame2.getArrowsLeft());
		assertTrue(firstGame2.canMove(17));
		firstGame2.getState();
		firstGame2.moveRoom(17);
		firstGame2.getState();
		firstGame2.resetLevel();
		firstGame2.moveRoom(11);
		firstGame2.getState();
		
	}

	@Override
	public void update() {
		System.out.println("hello");
		
	}
}
