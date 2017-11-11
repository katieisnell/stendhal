package games.stendhal.server.entity.item.scroll;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.entity.PassiveEntity;
import games.stendhal.server.entity.item.Money;
import games.stendhal.server.entity.player.Player;
import utilities.PlayerTestHelper;


public class BankScrollTest {
	
	static Player player;
	
	@BeforeClass
	public static void setUpBeforeClass()
	{
		player = PlayerTestHelper.createPlayer("bob");
	}
	
	@Test
	public void shouldReturnChestContents()
	{
		PassiveEntity money = new Money(200);
		String bank = "bank";
		player.getSlot(bank).add(money);

		BankScroll scroll = new BankScroll(player, bank);
		
		scroll.useScroll(player);
		assertEquals(bank + " chest has: 200 money", PlayerTestHelper.getPrivateReply(player));
		
	}
	
	@Test
	public void shouldRemainInBag()
	{
		String bank = "bank";
		BankScroll scroll = new BankScroll(player, bank);
		scroll.useScroll(player);
		assertTrue(scroll.equals(player.getSlot("bag").getFirst()));
	}

}
