package games.stendhal.server.entity.npc.action;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import marauroa.common.Log4J;
import marauroa.server.game.db.DatabaseFactory;
import utilities.PlayerTestHelper;

/* Tests for EquipBankScrollActionTest
 * 
 * author: Diana Pislaru
 * 
 * */
public class EquipBankScrollActionTest {
	
	@BeforeClass
	public static void beforeClass() {
		Log4J.init();
		MockStendlRPWorld.get();
		new DatabaseFactory().initializeDatabase();
	}
	
	/* Test the fire method */
	@Test
	public void testFire() {
		// Create a player
		Player p = PlayerTestHelper.createPlayer("bob");
		
		// Test that the player does not have any bank scrolls
		assertThat(Boolean.valueOf(p.isEquipped("bank scroll")), is(Boolean.FALSE));
		
		// Create the EquipBankScrollAction with a BankScroll from Ados Bank
		EquipBankScrollAction action = new EquipBankScrollAction("bank_ados");
		
		// Call fire method for the player
		action.fire(p, null, null);
		
		// Test that the player has the correct BankScroll
		assertThat(Boolean.valueOf(p.isEquipped("bank scroll")), is(Boolean.TRUE));
		assertEquals(p.getFirstEquipped("bank scroll").getInfoString(), p.getName() + " " + "bank_ados");
	}
}
