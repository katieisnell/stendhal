package games.stendhal.server.maps.athor.holiday_area;

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
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import games.stendhal.server.maps.quests.AbstractQuest;
import games.stendhal.server.maps.quests.SuntanCreamForZara;
import marauroa.common.game.RPObject.ID;
import utilities.PlayerTestHelper;

/* Tests for the tourist in Athor
 * 
 * author: Diana Pislaru
 * 
 * */
public class TouristFromAdosNPCTest {
	
	private static String questSlot;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MockStendlRPWorld.get();
		AbstractQuest quest = new SuntanCreamForZara();
		questSlot = quest.getSlotName();
	}

	/**
	 * Test the zone configuration
	 */
	@Test
	public void testConfigureZone() {
		SingletonRepository.getRPWorld();
		
		// Create the NPC
		final TouristFromAdosNPC touristConfigurator = new TouristFromAdosNPC();

		// Create a test zone
		final StendhalRPZone zone = new StendhalRPZone("testzone");
		
		// Configure the zone
		touristConfigurator.configureZone(zone, null);
		
		// Check that the NPC is the correct one
		final SpeakerNPC tourist = (SpeakerNPC) zone.getNPCList().get(0);
		assertThat(tourist.getName(), is("Zara"));
		assertThat(tourist.getDescription(), is("You see Zara, sunbathing, and worry that she is looking a little hot."));
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
		final TouristFromAdosNPC touristConfigurator = new TouristFromAdosNPC();

		// Create a test zone
		final StendhalRPZone zone = new StendhalRPZone("testzone");
		
		// Configure the zone and test that the NPC is the correct one
		touristConfigurator.configureZone(zone, null);
		
		final SpeakerNPC tourist = (SpeakerNPC) zone.getNPCList().get(0);
		assertThat(tourist.getName(), is("Zara"));
		
		// Create an engine
		final Engine engine = tourist.getEngine();
		engine.setCurrentState(ConversationStates.IDLE);

		// Player says "hi"
		Sentence sentence = new SentenceImplementation(new Expression("hi", ExpressionType.VERB));
		engine.step(player, sentence);
		
		// Check reply for "hi"
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(tourist), is("Nice to meet you!"));

		// Player says "bye"
		sentence = new SentenceImplementation(new Expression("bye", ExpressionType.VERB));
		engine.step(player, sentence);

		// Check reply for "bye"
		assertThat(engine.getCurrentState(), is(ConversationStates.IDLE));
		assertThat(getReply(tourist), is("I hope to see you soon!"));
	}
	
	/**
	 * Test the mark command when player hadn't completed the quest
	 */
	@Test
	public void testMarkCommandWhenTheQuestIsNotCompleted() {
		// Create a player
		final Player player = PlayerTestHelper.createPlayer("bob");
		SingletonRepository.getRPWorld();
		
		// Create the NPC
		final TouristFromAdosNPC touristConfigurator = new TouristFromAdosNPC();
		
		// Create a test zone
		final StendhalRPZone zone = new StendhalRPZone("testzone");
		touristConfigurator.configureZone(zone, null);
		final SpeakerNPC tourist = (SpeakerNPC) zone.getNPCList().get(0);
		assertThat(tourist.getName(), is("Zara"));
		final Engine engine = tourist.getEngine();
		engine.setCurrentState(ConversationStates.IDLE);

		// Start conversation
		Sentence sentence = new SentenceImplementation(new Expression("hi", ExpressionType.VERB));
		engine.step(player, sentence);
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(tourist), is("Nice to meet you!"));

		// Test player doesn't have any empty scrolls
		assertFalse(player.isQuestCompleted(questSlot));
		
		// Test mark command
		sentence = new SentenceImplementation(new Expression("mark", ExpressionType.VERB));
		engine.step(player, sentence);
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(tourist), is("You don't have access to my house!"));
		assertEquals(0, player.getNumberOfEquipped("empty scroll"));
	}
	
	/**
	 * Test the mark command when player had completed the quest 
	 */
	@Test
	public void testMarkCommandWhenTheQuestIsCompleted() {
		// Create a player
		final Player player = PlayerTestHelper.createPlayer("bob");
		SingletonRepository.getRPWorld();
		
		// Create the NPC
		final TouristFromAdosNPC touristConfigurator = new TouristFromAdosNPC();

		// Create a test zone
		final StendhalRPZone zone = new StendhalRPZone("testzone");
		touristConfigurator.configureZone(zone, null);
		final SpeakerNPC tourist = (SpeakerNPC) zone.getNPCList().get(0);
		assertThat(tourist.getName(), is("Zara"));
		final Engine engine = tourist.getEngine();
		engine.setCurrentState(ConversationStates.IDLE);

		// Start conversation
		Sentence sentence = new SentenceImplementation(new Expression("hi", ExpressionType.VERB));
		engine.step(player, sentence);
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(tourist), is("Nice to meet you!"));
		
		// Equip player with 50 empty scrolls 
		final StackableItem emptyScroll = new StackableItem("empty scroll", "", "", null);
		emptyScroll.setQuantity(50);
		emptyScroll.setID(new ID(2, "testzone"));
		player.getSlot("bag").add(emptyScroll);
		assertEquals(50, player.getNumberOfEquipped("empty scroll"));
		
		// Mark the quest as completed
		player.setQuest(questSlot, "done");
		
		// Test mark command
		sentence = new SentenceImplementation(new Expression("mark", ExpressionType.VERB));
		engine.step(player, sentence);
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(tourist), is("Here you go!"));
		assertEquals(49, player.getNumberOfEquipped("empty scroll"));
		assertEquals(1, player.getNumberOfEquipped("bank scroll"));
		assertEquals(player.getFirstEquipped("bank scroll").getInfoString(), player.getName() + " " + "zaras_chest_ados");
	}

}
