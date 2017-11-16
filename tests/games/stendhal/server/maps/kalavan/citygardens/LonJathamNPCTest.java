/* $Id$ */
package games.stendhal.server.maps.kalavan.citygardens;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static utilities.SpeakerNPCTestHelper.getReply;

import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import utilities.QuestHelper;
import utilities.ZonePlayerAndNPCTestImpl;

// Test for Lon Jatham NPC
public class LonJathamNPCTest extends ZonePlayerAndNPCTestImpl {

	// The zone where Lon is
	private static final String ZONE_NAME = "0_kalavan_city_gardens";

	// Set up the zone before doing the test
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		QuestHelper.setUpBeforeClass();

		setupZone(ZONE_NAME);
	} // setUpBefore class

	// Get Lon in his zone
	public LonJathamNPCTest() {
		setNpcNames("Lon Jatham");
		setZoneForPlayer(ZONE_NAME);
		addZoneConfigurator(new LonJathamNPC(), ZONE_NAME);
	} // LonJathamNPCTest
	
	// Test interactions with Lon - hi and bye
	@Test
	public void testHiAndBye() {
		
		// Get Lon as a SpeakerNPC
		final SpeakerNPC npc = getNPC("Lon Jatham");
		
		// Check he exists
		assertNotNull(npc);
		
		// Get Lon's engine
		final Engine en = npc.getEngine();

		// Say hi and check response
		assertTrue(en.step(player, "hi"));
		assertEquals("Hi. Do you want to buy my Java book? Maybe take a test?", getReply(npc));

		// Say bye and check response
		assertTrue(en.step(player, "bye"));
		assertEquals("Gooooodbye! Study harder!!!", getReply(npc));
	} // testHiAndBye

	// Test interactions with Lon - talking
	@Test
	public void testTalkToLon() {
		// Lon as speaker NPC
		final SpeakerNPC npc = getNPC("Lon Jatham");
		
		// Get Lon's engine
		final Engine en = npc.getEngine();

	    // Check hi
		assertTrue(en.step(player, "hi"));
		assertEquals("Hi. Do you want to buy my Java book? Maybe take a test?", getReply(npc));

		// Check asking him about his job
		assertTrue(en.step(player, "job"));
		assertEquals("I'm a Java lecturer! Ask me to take a Java #test!", getReply(npc));

		// Check asking him for help
		assertTrue(en.step(player, "help"));
		assertEquals("Hm, maybe you'd like to answer some questions in my Java #test?", getReply(npc));

		// Check asking him about studying
		assertTrue(en.step(player, "study"));
		assertEquals("Buy my book! I update it every year.", getReply(npc));
		
		// Check asking him about his quest
		assertTrue(en.step(player, "quest"));
		assertEquals("I have a #test for you! Answer some Java questions for me!", getReply(npc));
		
		// Check asking him what he offers
		assertTrue(en.step(player, "offer"));
		assertEquals("I can give XP and a certificate.", getReply(npc));
		
		// Check bye
		assertTrue(en.step(player, "bye"));
		assertEquals("Gooooodbye! Study harder!!!", getReply(npc));
		
	} // testTalkToLon
} // LonJathamNPCTest
