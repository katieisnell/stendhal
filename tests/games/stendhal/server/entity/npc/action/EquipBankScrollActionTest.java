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

public class EquipBankScrollActionTest {
	@BeforeClass
	public static void beforeClass() {
		Log4J.init();
		MockStendlRPWorld.get();
		new DatabaseFactory().initializeDatabase();
	}

	@Test
	public void testFire() {
		Player p = PlayerTestHelper.createPlayer("bob");
		assertThat(Boolean.valueOf(p.isEquipped("bank scroll")), is(Boolean.FALSE));
		EquipBankScrollAction action = new EquipBankScrollAction("bank_ados");
		action.fire(p, null, null);
		assertThat(Boolean.valueOf(p.isEquipped("bank scroll")), is(Boolean.TRUE));
		assertEquals(p.getFirstEquipped("bank scroll").getInfoString(), p.getName() + " " + "bank_ados");
		
	}
}
