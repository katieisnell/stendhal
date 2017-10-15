package games.stendhal.server.maps.quests.revivalweeks;

import static org.junit.Assert.*;
import static utilities.SpeakerNPCTestHelper.getReply;

import org.junit.BeforeClass;
import org.junit.Test;


import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.NPCList;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;
import utilities.PlayerTestHelper;

public class PhotographerNPCTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		SingletonRepository.getRPWorld().addRPZone(new StendhalRPZone("0_semos_mountain_n2"));

		// Add all the zones that determinePhoto will check so they don't return null pointers
		SingletonRepository.getRPWorld().addRPZone(new StendhalRPZone("-1_semos_dungeon"));
		SingletonRepository.getRPWorld().addRPZone(new StendhalRPZone("int_afterlife"));
		SingletonRepository.getRPWorld().addRPZone(new StendhalRPZone("0_semos_mountain_n_w2"));
		SingletonRepository.getRPWorld().addRPZone(new StendhalRPZone("0_ados_wall_n"));
		SingletonRepository.getRPWorld().addRPZone(new StendhalRPZone("int_semos_wizards_tower_basement"));
		SingletonRepository.getRPWorld().addRPZone(new StendhalRPZone("-1_ados_outside_nw"));
		SingletonRepository.getRPWorld().addRPZone(new StendhalRPZone("int_semos_wizards_tower_9"));
		SingletonRepository.getRPWorld().addRPZone(new StendhalRPZone("int_imorgens_house"));
		SingletonRepository.getRPWorld().addRPZone(new StendhalRPZone("0_nalwor_city"));
		SingletonRepository.getRPWorld().addRPZone(new StendhalRPZone("int_oni_palace_2"));		
		
		new PhotographerNPC().addToWorld();
	}

	@Test
	public void testNPCisThere() throws Exception {
		assertNotNull(NPCList.get().get("Kirla"));
	}
	
	
	@Test
	public void testConversation() throws Exception {
		SpeakerNPC kirla = NPCList.get().get("Kirla");
		Engine engine = kirla.getEngine();
		Player player=PlayerTestHelper.createPlayer("Player");
				
		
		engine.step(player, "hi");		
		assertEquals("#Pictures! Good #Pictures! Memory #Pictures! Buy #Pictures!", getReply(kirla));
		assertEquals(ConversationStates.ATTENDING, engine.getCurrentState());		
		
		engine.step(player, "picture");
		assertEquals("Ohmmmmm, I see blury mist, Ohmmmmm. The picture is getting clearer, Ohmmmmm. Just a few more seconds...", getReply(kirla));
		assertEquals(ConversationStates.ATTENDING, engine.getCurrentState());
		
		
		engine.step(player, "bye");
		assertEquals("Take care.", getReply(kirla));
		assertEquals(ConversationStates.IDLE, engine.getCurrentState());

	}	
}
