package games.stendhal.server.maps.ados.rosshouse;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.Iterator;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.config.ZonesXMLLoader;
import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.mapstuff.chest.Chest;
import games.stendhal.server.entity.mapstuff.portal.Portal;
import games.stendhal.server.entity.player.Player;
import marauroa.common.game.RPObject;
import utilities.QuestHelper;
import utilities.ZoneAndPlayerTestImpl;
import static org.hamcrest.CoreMatchers.is;

/**
 * Tests for the upper floor of the Ross house.
 * @author Anthony Sikosa
 */
public class ItemsUpstairsTest extends ZoneAndPlayerTestImpl {

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
		zone = setupZone("int_ados_ross_house_1");
		player = createPlayer("player");
		registerPlayer(player, zone);
	}
	
	public ItemsUpstairsTest() {
		super("int_ados_ross_house_1");
	}
	
	/**
	 *  Tests whether the upper floor has an chest in it which contains
	 *  a teddy.
	 */
	@Test
	public void shouldHaveChestContainingTeddy() {
		Chest chest = (Chest)zone.getEntityAt(2, 7);
		assertNotNull(chest); // check if chest exists
		assertThat(chest.isEmpty(), is(false)); // check if chest is empty
		
		// check if chest opens
		chest.open();
		assertThat(chest.isOpen(), is(true)); 
		
		// check if chest contains teddy
		Iterator<RPObject> chestIt = chest.getContent();
		Item firstItem = (Item) chestIt.next();
		assertEquals(firstItem.getName(), "teddy");
	}
	/** 
	 *  Tests whether there is a portal which leads to downstairs on
	 *  the upstairs floor.
	 */
	@Test
	public void shouldHavePortalToDownstairs() {	
		// check if portal exists and is in the correct place
		Portal portal = zone.getPortal(1, 12);
		assertNotNull(portal);
		
		// check if player can enter portal and ends up downstairs
		player.setPosition(1, 13);
		assertTrue(portal.onUsed(player));
		assertEquals(player.getZone().getName(), "int_ados_ross_house");
	}
}

