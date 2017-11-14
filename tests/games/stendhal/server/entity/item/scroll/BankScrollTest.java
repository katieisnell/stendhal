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
	
	@BeforeClass
	public static void setUpBeforeClass()
	{
		player = PlayerTestHelper.createPlayer("bob");
	}
	
	@Test
	public void shouldReturnChestContents()
	{
		PassiveEntity money = new Money(200);
		PassiveEntity sword = new Item("sword", "", "", null);
		String bank = "bank";
		player.getSlot(bank).add(money);
		player.getSlot(bank).add(sword);
		BankScroll scroll = (BankScroll) SingletonRepository.getEntityManager().getItem("bank scroll");
		scroll.setInfo(player,bank);
		player.getSlot("bag").add(scroll);
		scroll.useScroll(player);
		assertEquals("Semos bank chest contains 200 money, 1 sword", PlayerTestHelper.getPrivateReply(player));
	}
	
	@Test
	public void shouldRemainInBag()
	{
		String bank = "bank";
		BankScroll scroll = (BankScroll) SingletonRepository.getEntityManager().getItem("bank scroll");
		scroll.setInfo(player,bank);
		player.equip("bag", scroll);
		scroll.useScroll(player);
		assertTrue(scroll.equals(player.getSlot("bag").getFirst()));
	}
	
	@Test
	public void shouldDisappearWhenUsedByWrongPlayer()
	{
		Player player1 = PlayerTestHelper.createPlayer("tom");
		String bank = "bank";
		BankScroll scroll = (BankScroll) SingletonRepository.getEntityManager().getItem("bank scroll");
		scroll.setInfo(player, bank);
		player1.equip("bag", scroll);
		assertTrue(scroll.equals(player1.getSlot("bag").getFirst()));
		scroll.useScroll(player1);
		assertFalse(scroll.equals(player1.getSlot("bag").getFirst()));
		assertEquals("You cannot view another player's bank statement", PlayerTestHelper.getPrivateReply(player1));
	}

}
