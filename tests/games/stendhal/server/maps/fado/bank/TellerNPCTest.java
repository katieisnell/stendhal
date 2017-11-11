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

public class TellerNPCTest {
	
	private static Player player;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MockStendlRPWorld.get();

		player = PlayerTestHelper.createPlayer("bob");
	}

	/**
	 * Tests for configureZone.
	 */
	@Test
	public void testConfigureZone() {
		SingletonRepository.getRPWorld();
		final TellerNPC bankConfigurator = new TellerNPC();

		final StendhalRPZone zone = new StendhalRPZone("testzone");
		bankConfigurator.configureZone(zone, null);
		assertFalse(zone.getNPCList().isEmpty());
		
		final NPC bankTeller = zone.getNPCList().get(0);
		assertThat(bankTeller.getName(), is("Yance"));
		assertThat(bankTeller.getDescription(), is("Yance is the Fado bank manager. He can give advice on how to use the chests."));
	}
	
	/**
	 * Tests for hi and bye.
	 */
	@Test
	public void testHiandBye() {
		SingletonRepository.getRPWorld();
		final TellerNPC bankConfigurator = new TellerNPC();
		final StendhalRPZone zone = new StendhalRPZone("testzone");
		bankConfigurator.configureZone(zone, null);
		final SpeakerNPC bankTeller = (SpeakerNPC) zone.getNPCList().get(0);
		assertThat(bankTeller.getName(), is("Yance"));
		final Engine engine = bankTeller.getEngine();
		engine.setCurrentState(ConversationStates.IDLE);

		Sentence sentence = new SentenceImplementation(new Expression("hi", ExpressionType.VERB));
		engine.step(player, sentence);
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(bankTeller), is("Welcome to the Fado Bank! Do you need #help?"));

		sentence = new SentenceImplementation(new Expression("bye", ExpressionType.VERB));
		engine.step(player, sentence);
		assertThat(engine.getCurrentState(), is(ConversationStates.IDLE));
		assertThat(getReply(bankTeller), is("Have a nice day."));
	}
	
	@Test
	public void testMarkScroll() {
		SingletonRepository.getRPWorld();
		final TellerNPC bankConfigurator = new TellerNPC();
		final StendhalRPZone zone = new StendhalRPZone("testzone");
		bankConfigurator.configureZone(zone, null);
		final SpeakerNPC bankTeller = (SpeakerNPC) zone.getNPCList().get(0);
		assertThat(bankTeller.getName(), is("Yance"));
		final Engine engine = bankTeller.getEngine();
		engine.setCurrentState(ConversationStates.IDLE);

		// start conversation
		Sentence sentence = new SentenceImplementation(new Expression("hi", ExpressionType.VERB));
		engine.step(player, sentence);
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(bankTeller), is("Welcome to the Fado Bank! Do you need #help?"));
		
		// equip player with 50 empty scrolls
		final StackableItem emptyScroll = new StackableItem("empty scroll", "", "", null);
		emptyScroll.setQuantity(50);
		emptyScroll.setID(new ID(2, "testzone"));
		player.getSlot("bag").add(emptyScroll);
		assertEquals(50, player.getNumberOfEquipped("empty scroll"));
		
		// test mark command
		sentence = new SentenceImplementation(new Expression("mark", ExpressionType.VERB));
		engine.step(player, sentence);
		assertEquals(49, player.getNumberOfEquipped("empty scroll"));
	}
}
