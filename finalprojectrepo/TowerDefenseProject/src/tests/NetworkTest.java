package tests;

import static org.junit.Assert.assertTrue;
import gamepanel.GamePanel;
import model.Game;
import model.Wave;
import networking.GamePlayer;
import networking.GameServer;

import org.junit.BeforeClass;
import org.junit.Test;

import tower.TowerType;

public class NetworkTest {
	
	@BeforeClass 
	public static void onlyOnce() {
		//start the server running
		GameServer.main(null);
	}

	@Test
	public void testQuit()
	{
		GamePanel panel1 = new GamePanel(null, null);
		//start a multiplayer game
		panel1.restart(true);
		//get ourselves a copy of the gameplayer (the class that actually talks to the server)
		GamePlayer player1 = panel1.getGamePlayer();
		
		//wait for the gameplayer to connect
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			System.out.println("insomnia");
		}
		
		//have player1 select the map to play on
		player1.selectMap(2);
		GamePanel panel2 = new GamePanel(null, null);
		//have the second player join the game
		panel2.restart(true);
		GamePlayer player2 = panel2.getGamePlayer();
		
		//wait for the second player to connect
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			System.out.println("insomnia");
		}
		
		//place towers--note that no exceptions are thrown
		player1.notifyOfTowerPlacement(TowerType.RADIATION, 0,0	);
		player2.notifyOfTowerPlacement(TowerType.LASER, 0, 0);
		
		//player 2 x-es out
		player2.quit();
		
		//wait for player 1 to find out
		try
		{
			Thread.sleep(500);
		} catch (InterruptedException e)
		{
			System.out.println("insomnia");
		}
		
		//player 1 wins by default
		assertTrue(panel1.getMapCoordinator().getGame().hasWon());
	}
	
	@Test
	public void testTie()
	{
		GamePanel panel1 = new GamePanel(null, null);
		//start a multiplayer game
		panel1.restart(true);
		//get ourselves a copy of the gameplayer (the class that talks to the server)
		GamePlayer player1 = panel1.getGamePlayer();
		
		//wait for the gameplayer to connect
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			System.out.println("insomnia");
		}
		
		//have player 1 select the map to play on
		player1.selectMap(2);
		GamePanel panel2 = new GamePanel(null, null);
		//have the second player join the game
		panel2.restart(true);
		GamePlayer player2 = panel2.getGamePlayer();
		
		//wait for the second player to connect
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			System.out.println("insomnia");
		}
		
		//get copies of the games
		Game game1 = panel1.getMapCoordinator().getGame();
		Game game2 = panel2.getMapCoordinator().getGame();
		
		//make the players check for ties, sending flags to the server
		player1.checkForTie(true);
		player1.checkForTie(false);
		game1.setCurrency(50);
		game2.setCurrency(50);
		//set the game's currency low enough that the games are actually in a tie configuration
		
		player2.checkForTie(true);
		player1.checkForTie(true);
		
		//wait for them to communicate this to each other
		try
		{
			Thread.sleep(2000);
		} catch (InterruptedException e)
		{
			System.out.println("insomnia");
		}

		//assert that they are both tied
		assertTrue(game1.hasTied());
		assertTrue(game2.hasTied());
	}
	
	@Test
	public void testLose()
	{
		GamePanel panel1 = new GamePanel(null, null);
		//start a multiplayer game
		panel1.restart(true);
		//get ourselves a copy of the gameplayer (class that talks to server)
		GamePlayer player1 = panel1.getGamePlayer();
		
		//wait for gameplayer to connect
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			System.out.println("insomnia");
		}
		
		//have player 1 choose the map to play on
		player1.selectMap(2);
		GamePanel panel2 = new GamePanel(null, null);
		//have player 2 join the game
		panel2.restart(true);
		GamePlayer player2 = panel2.getGamePlayer();
		
		//wait for the second player to connect
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			System.out.println("insomnia");
		}
		
		player2.sendWave(new Wave(10,10,10));
		//this would eventually be sent on its own, but that could take awhile
		player1.notifyOfLose();
		
		//wait for player 2 to notice
		try
		{
			Thread.sleep(500);
		} catch (InterruptedException e)
		{
			System.out.println("insomnia");
		}
		
		//make sure player 2 knows that it has won
		assertTrue(panel2.getMapCoordinator().getGame().hasWon());
	}
}
