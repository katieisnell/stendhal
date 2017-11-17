package games.stendhal.server.maps.ados.rosshouse;

import static org.junit.Assert.*;

import java.net.URI;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.config.ZonesXMLLoader;
import games.stendhal.server.entity.mapstuff.portal.Portal;
import games.stendhal.server.entity.player.Player;
import utilities.QuestHelper;
import utilities.ZoneAndPlayerTestImpl;

/**
 * Tests for the bottom floor of the Ross house.
 * @author Anthony Sikosa
 */
public class ItemsOnTableTest extends ZoneAndPlayerTestImpl {

	private Player player;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		QuestHelper.setUpBeforeClass();
		ZonesXMLLoader zoneLoader = new ZonesXMLLoader(new URI("/data/conf/zones/ados.xml"));
		zoneLoader.load();
	}
	
	@Override
	@Before
	public void setUp() {
		zone = setupZone("int_ados_ross_house");
		player = createPlayer("player");
		registerPlayer(player, zone);
	}
	
	public ItemsOnTableTest() {
		super("int_ados_ross_house");
	}
	/**
	 *  Tests whether on the bottom floor of the Ross house there is a portal which
	 *  leads upstairs.
	 */
	@Test
	public void shouldHavePortalToUpstairs() {	
		// check if portal is in the correct place
		Portal portal = zone.getPortal(1, 12);
		assertNotNull(portal);
		
		// check if player can enter portal and ends up upstairs
		player.setPosition(1, 13);
		assertTrue(portal.onUsed(player));
		assertEquals(player.getZone().getName(), "int_ados_ross_house_1");
	}
}
