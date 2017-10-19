package games.stendhal.server.maps.kalavan.citygardens;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static utilities.SpeakerNPCTestHelper.getReply;

import java.util.Calendar;
import java.util.TimeZone;

import org.junit.Test;
import org.junit.BeforeClass;

import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;

import utilities.QuestHelper;
import utilities.ZonePlayerAndNPCTestImpl;

/**
 * Test for Weird Gardener Greeting.
 * 
 * @author Khoi Nguyen (mbaxamn2)
 * 
 * Completed on 13/10/2017.
 */
public class GardenerNPCTest extends ZonePlayerAndNPCTestImpl {

	private static final String ZONE_NAME = "0_kalavan_city_gardens";

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {				
		
		// Run the method to set the current time in game to 9pm so we can test it.
		setTimeZoneTo9PM();
		QuestHelper.setUpBeforeClass();
		setupZone(ZONE_NAME);
	}
	
	/**
	 * Method to set the current time zone to 9pm, so that we can test for sunset conversation.
	 * The method go through all the possible time zone and set the one at 9pm(21).
	 */
	private static void setTimeZoneTo9PM() {

		// Get current hour.
		Calendar cal = Calendar.getInstance();
		int currentHour = cal.get(Calendar.HOUR_OF_DAY);

		// Get all the zones ID and store in a string array.
		String[] allZoneIDs = TimeZone.getAvailableIDs();

		for (int i = 0; i < allZoneIDs.length && currentHour != 21; i++) {

			TimeZone.setDefault(TimeZone.getTimeZone(allZoneIDs[i]));	
			cal = Calendar.getInstance();
			currentHour = cal.get(Calendar.HOUR_OF_DAY);
		}
	}
	
	// Setup zone which included the gardener Sue.
	public GardenerNPCTest() {
		setNpcNames("Sue");
		setZoneForPlayer(ZONE_NAME);
		addZoneConfigurator(new GardenerNPC(), ZONE_NAME);		
	}

	/**
	 * Tests for Gardener greeting at sunset.
	 */
	@Test
	public void testSunsetGreeting() {

		final SpeakerNPC npc = getNPC("Sue");

		// Check if the GardenerNPC (Sue) exists.
		assertNotNull(npc);

		final Engine en = npc.getEngine();

		// Test for conversion answer, should be sunset instead of unset.
		assertTrue(en.step(player, "hi"));
		assertEquals("Fine sunset, isn't it?", getReply(npc));
	}

}
