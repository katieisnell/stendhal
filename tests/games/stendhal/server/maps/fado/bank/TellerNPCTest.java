package games.stendhal.server.maps.fado.bank;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static utilities.SpeakerNPCTestHelper.getReply;

import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.common.parser.Expression;
import games.stendhal.common.parser.ExpressionType;
import games.stendhal.common.parser.Sentence;
import games.stendhal.common.parser.SentenceImplementation;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.item.StackableItem;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.NPC;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import marauroa.common.game.RPObject.ID;
import utilities.PlayerTestHelper;

/* Tests for the bank teller in Fado Bank 
 * 
 * author: Diana Pislaru
 * 
 * */
public class TellerNPCTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MockStendlRPWorld.get();
	}

	/**
	 * Test the zone configuration
	 */
	@Test
	public void testConfigureZone() {
		SingletonRepository.getRPWorld();
		
		// Create the NPC
		final TellerNPC bankConfigurator = new TellerNPC();

		// Create a test zone
		final StendhalRPZone zone = new StendhalRPZone("testzone");
		
		// Configure the zone and test that at least one NPC exists
		bankConfigurator.configureZone(zone, null);
		assertFalse(zone.getNPCList().isEmpty());
		
		// Check that the NPC is the correct one
		final NPC bankTeller = zone.getNPCList().get(0);
		assertThat(bankTeller.getName(), is("Yance"));
		assertThat(bankTeller.getDescription(), is("Yance is the Fado bank manager. He can give advice on how to use the chests."));
	}
	
	/**
	 * Test the NPC replies for "hi" and "bye" expressions
	 */
	@Test
	public void testHiandBye() {
		// Create a player
		final Player player = PlayerTestHelper.createPlayer("bob");
		SingletonRepository.getRPWorld();
		
		// Create the NPC
		final TellerNPC bankConfigurator = new TellerNPC();
		
		// Create a test zone
		final StendhalRPZone zone = new StendhalRPZone("testzone");
		
		// Configure the zone and test that the NPC is the correct one
		bankConfigurator.configureZone(zone, null);
		final SpeakerNPC bankTeller = (SpeakerNPC) zone.getNPCList().get(0);
		assertThat(bankTeller.getName(), is("Yance"));
		
		// Create an engine
		final Engine engine = bankTeller.getEngine();
		engine.setCurrentState(ConversationStates.IDLE);

		// Player says "hi"
		Sentence sentence = new SentenceImplementation(new Expression("hi", ExpressionType.VERB));
		engine.step(player, sentence);
		
		// Check reply for "hi"
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(bankTeller), is("Welcome to the Fado Bank! Do you need #help?"));

		// Player says "bye"
		sentence = new SentenceImplementation(new Expression("bye", ExpressionType.VERB));
		engine.step(player, sentence);
		
		// Check reply for "bye"
		assertThat(engine.getCurrentState(), is(ConversationStates.IDLE));
		assertThat(getReply(bankTeller), is("Have a nice day."));
	}
	
	/**
	 * Test the mark command when player does not have any empty scrolls
	 */
	@Test
	public void testMarkCommandWhenPlayerDoesNotHaveEmptyScrolls() {
		// Create a player
		final Player player = PlayerTestHelper.createPlayer("bob");
		SingletonRepository.getRPWorld();
		
		// Create the NPC
		final TellerNPC bankConfigurator = new TellerNPC();
		
		// Create a test zone
		final StendhalRPZone zone = new StendhalRPZone("testzone");
		bankConfigurator.configureZone(zone, null);
		final SpeakerNPC bankTeller = (SpeakerNPC) zone.getNPCList().get(0);
		assertThat(bankTeller.getName(), is("Yance"));
		final Engine engine = bankTeller.getEngine();
		engine.setCurrentState(ConversationStates.IDLE);

		// Start conversation
		Sentence sentence = new SentenceImplementation(new Expression("hi", ExpressionType.VERB));
		engine.step(player, sentence);
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(bankTeller), is("Welcome to the Fado Bank! Do you need #help?"));

		// Test player doesn't have any empty scrolls
		assertEquals(0, player.getNumberOfEquipped("empty scroll"));
		
		// Test mark command
		sentence = new SentenceImplementation(new Expression("mark", ExpressionType.VERB));
		engine.step(player, sentence);
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(bankTeller), is("You need an empty scroll so I can mark it!"));
		assertEquals(0, player.getNumberOfEquipped("empty scroll"));
	}
	
	/**
	 * Test the mark command when player has empty scrolls
	 */
	@Test
	public void testMarkCommandWhenPlayerHasEmptyScrolls() {
		// Create a player
		final Player player = PlayerTestHelper.createPlayer("bob");
		SingletonRepository.getRPWorld();
		
		// Create the NPC
		final TellerNPC bankConfigurator = new TellerNPC();

		// Create a test zone
		final StendhalRPZone zone = new StendhalRPZone("testzone");
		bankConfigurator.configureZone(zone, null);
		final SpeakerNPC bankTeller = (SpeakerNPC) zone.getNPCList().get(0);
		assertThat(bankTeller.getName(), is("Yance"));
		final Engine engine = bankTeller.getEngine();
		engine.setCurrentState(ConversationStates.IDLE);

		// Start conversation
		Sentence sentence = new SentenceImplementation(new Expression("hi", ExpressionType.VERB));
		engine.step(player, sentence);
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(bankTeller), is("Welcome to the Fado Bank! Do you need #help?"));
		
		// Equip player with 50 empty scrolls 
		final StackableItem emptyScroll = new StackableItem("empty scroll", "", "", null);
		emptyScroll.setQuantity(50);
		emptyScroll.setID(new ID(2, "testzone"));
		player.getSlot("bag").add(emptyScroll);
		assertEquals(50, player.getNumberOfEquipped("empty scroll"));

		// Check player doesn't have any bank scrolls
		assertEquals(0, player.getNumberOfEquipped("bank scroll"));
		
		// Test mark command
		sentence = new SentenceImplementation(new Expression("mark", ExpressionType.VERB));
		engine.step(player, sentence);
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(bankTeller), is("Here you go!"));
		assertEquals(49, player.getNumberOfEquipped("empty scroll"));
		assertEquals(1, player.getNumberOfEquipped("bank scroll"));
		assertEquals(player.getFirstEquipped("bank scroll").getInfoString(), player.getName() + " " + "bank_fado");
	}
}
