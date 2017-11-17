package games.stendhal.server.entity.item.scroll;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.entity.PassiveEntity;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.item.Money;
import games.stendhal.server.entity.player.Player;
import utilities.PlayerTestHelper;


public class BankScrollTest {
	
	private static Player player;
	
	// Set up the test class by creating a player to be used in all tests
	@BeforeClass
	public static void setUpBeforeClass()
	{
		player = PlayerTestHelper.createPlayer("bob");
	}
	
	
	/**
	 * Test for the result of using a bank scroll when the bank chest is empty and
	 * when there are two items in the bank chest
	 */
	@Test
	public void shouldReturnChestContents()
	{
		// Create two items that will go in the bank 
		PassiveEntity money = new Money(200);
		PassiveEntity sword = new Item("sword", "", "", null);
		
		// Set the name of the bank to be used (Semos bank)
		String bank = "bank";
		
		// Create scroll and set it's internal information about the player and the
		// associated bank
		BankScroll scroll = (BankScroll) SingletonRepository.getEntityManager().getItem("bank scroll");
		scroll.setInfo(player, bank);
		player.getSlot("bag").add(scroll);
		
		// Use the scroll and check if it states that the bank chest contains nothing
		scroll.useScroll(player);
		assertEquals("Semos bank chest contains nothing", PlayerTestHelper.getPrivateReply(player));
		
		// Add the two items to the bank chest
		player.getSlot(bank).add(money);
		player.getSlot(bank).add(sword);

		// Use the scroll and check if it states that the bank chest contains the two items
		scroll.useScroll(player);
		assertEquals("Semos bank chest contains 200 money, 1 sword", PlayerTestHelper.getPrivateReply(player));
	}
	
	/**
	 * Test to check if a bank scroll remains in bag after use
	 */
	@Test
	public void shouldRemainInBag()
	{
		// Set the name of the bank to be used (Semos bank)
		String bank = "bank";
		
		// Create the scroll, set its internal information and add it to the player's bag
		// as the first item
		BankScroll scroll = (BankScroll) SingletonRepository.getEntityManager().getItem("bank scroll");
		scroll.setInfo(player,bank);
		player.equip("bag", scroll);
		
		// Use the scroll and check if the first item in the bag is still
		// the scroll we created before
		scroll.useScroll(player);
		assertTrue(scroll.equals(player.getSlot("bag").getFirst()));
	}
	
	/**
	 * Test to check if a bank scroll disappears when used by a different
	 * player than its owner
	 */
	@Test
	public void shouldDisappearWhenUsedByWrongPlayer()
	{
		// Create a new player different from the one set up for this class
		Player player1 = PlayerTestHelper.createPlayer("tom");
		
		// Set the name of the bank to be used (Semos bank)
		String bank = "bank";
		
		// Create the scroll, set its internal configuration (using the first player), and
		// add it to the second player's bag
		BankScroll scroll = (BankScroll) SingletonRepository.getEntityManager().getItem("bank scroll");
		scroll.setInfo(player, bank);
		player1.equip("bag", scroll);
		
		// Check if the scroll is the first thing in the second player's bag
		assertTrue(scroll.equals(player1.getSlot("bag").getFirst()));
		
		// Have the scroll be used by the second player
		scroll.useScroll(player1);
		
		// Check that the scroll is no longer in the second player's bag and that
		// they received the appropriate message
		assertFalse(scroll.equals(player1.getSlot("bag").getFirst()));
		assertEquals("You cannot view another player's bank statement", PlayerTestHelper.getPrivateReply(player1));
	}

}
