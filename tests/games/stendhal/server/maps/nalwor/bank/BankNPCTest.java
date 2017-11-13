package games.stendhal.server.maps.nalwor.bank;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
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

public class BankNPCTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		MockStendlRPWorld.get();
	}

	/**
	 * Tests for configureZone.
	 */
	@Test
	public void testConfigureZone() {
		SingletonRepository.getRPWorld();
		final BankNPC bankConfigurator = new BankNPC();

		final StendhalRPZone zone = new StendhalRPZone("testzone");
		bankConfigurator.configureZone(zone, null);
		assertEquals(2, zone.getNPCList().size());
		
		final NPC bankTeller = zone.getNPCList().get(1);
		assertThat(bankTeller.getName(), is("Nnyddion"));
		assertThat(bankTeller.getDescription(), is("You see a pretty female elf in a beautiful dress."));
	}
	
	/**
	 * Tests for hi and bye.
	 */
	@Test
	public void testHiandBye() {
		
		final Player player = PlayerTestHelper.createPlayer("bob");
		SingletonRepository.getRPWorld();
		final BankNPC bankConfigurator = new BankNPC();
		final StendhalRPZone zone = new StendhalRPZone("testzone");
		bankConfigurator.configureZone(zone, null);
		final SpeakerNPC bankTeller = (SpeakerNPC) zone.getNPCList().get(1);
		assertThat(bankTeller.getName(), is("Nnyddion"));
		final Engine engine = bankTeller.getEngine();
		engine.setCurrentState(ConversationStates.IDLE);

		Sentence sentence = new SentenceImplementation(new Expression("hi", ExpressionType.VERB));
		engine.step(player, sentence);
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(bankTeller), is("Welcome to Nalwor Bank. I'm here to #help."));

		sentence = new SentenceImplementation(new Expression("bye", ExpressionType.VERB));
		engine.step(player, sentence);
		assertThat(engine.getCurrentState(), is(ConversationStates.IDLE));
		assertThat(getReply(bankTeller), is("Goodbye, thank you for your time."));
	}
	
	@Test
	public void testMarkCommandWhenPlayerDoesNotHaveEmptyScrolls() {
		final Player player = PlayerTestHelper.createPlayer("bob");
		SingletonRepository.getRPWorld();
		final BankNPC bankConfigurator = new BankNPC();
		final StendhalRPZone zone = new StendhalRPZone("testzone");
		bankConfigurator.configureZone(zone, null);
		final SpeakerNPC bankTeller = (SpeakerNPC) zone.getNPCList().get(1);
		assertThat(bankTeller.getName(), is("Nnyddion"));
		final Engine engine = bankTeller.getEngine();
		engine.setCurrentState(ConversationStates.IDLE);

		// start conversation
		Sentence sentence = new SentenceImplementation(new Expression("hi", ExpressionType.VERB));
		engine.step(player, sentence);
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(bankTeller), is("Welcome to Nalwor Bank. I'm here to #help."));
		
		assertEquals(0, player.getNumberOfEquipped("empty scroll"));
		
		// test mark command
		sentence = new SentenceImplementation(new Expression("mark", ExpressionType.VERB));
		engine.step(player, sentence);
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(bankTeller), is("You need an empty scroll so I can mark it!"));
		assertEquals(0, player.getNumberOfEquipped("empty scroll"));
	}

	@Test
	public void testMarkCommandWhenPlayerHasEmptyScrolls() {
		final Player player = PlayerTestHelper.createPlayer("bob");
		SingletonRepository.getRPWorld();
		final BankNPC bankConfigurator = new BankNPC();
		final StendhalRPZone zone = new StendhalRPZone("testzone");
		bankConfigurator.configureZone(zone, null);
		final SpeakerNPC bankTeller = (SpeakerNPC) zone.getNPCList().get(1);
		assertThat(bankTeller.getName(), is("Nnyddion"));
		final Engine engine = bankTeller.getEngine();
		engine.setCurrentState(ConversationStates.IDLE);

		// start conversation
		Sentence sentence = new SentenceImplementation(new Expression("hi", ExpressionType.VERB));
		engine.step(player, sentence);
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(bankTeller), is("Welcome to Nalwor Bank. I'm here to #help."));
		
		// equip player with 50 empty scrolls 
		final StackableItem emptyScroll = new StackableItem("empty scroll", "", "", null);
		emptyScroll.setQuantity(50);
		emptyScroll.setID(new ID(2, "testzone"));
		player.getSlot("bag").add(emptyScroll);
		assertEquals(50, player.getNumberOfEquipped("empty scroll"));
		
		assertEquals(0, player.getNumberOfEquipped("bank scroll"));
		
		// test mark command
		sentence = new SentenceImplementation(new Expression("mark", ExpressionType.VERB));
		engine.step(player, sentence);
		assertThat(engine.getCurrentState(), is(ConversationStates.ATTENDING));
		assertThat(getReply(bankTeller), is("Here you go!"));
		assertEquals(49, player.getNumberOfEquipped("empty scroll"));
		assertEquals(1, player.getNumberOfEquipped("bank scroll"));
		assertEquals(player.getFirstEquipped("bank scroll").getInfoString(), player.getName() + " " + "bank_nalwor");
	}

}
