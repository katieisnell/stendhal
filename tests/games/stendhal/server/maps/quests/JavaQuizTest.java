package games.stendhal.server.maps.quests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static utilities.SpeakerNPCTestHelper.getReply;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import games.stendhal.server.maps.Region;
import utilities.PlayerTestHelper;
import utilities.QuestHelper;
import games.stendhal.server.maps.kalavan.citygardens.LonJathamNPC;
import games.stendhal.server.maps.kalavan.cottage.HouseKeeperNPC;

// Test for Java quiz
public class JavaQuizTest {

	// Quest slot is the java quest
	private static String questSlot = "java_quest";

	// A player to attempt the quest
	private Player player = null;

	// A speakerNPC, Lon Jatham, to give the quest to our player
	private SpeakerNPC lon = null;

	// The Granny NPC which deals with giving the user their logbook
	private SpeakerNPC granny = null;

	// The NPC's engine
	private Engine lonEn = null;

	// The granny's engine
	private Engine grannyEn = null;

	// The Java quiz test
	public static final AbstractQuest quest = new JavaQuiz();

	// Before the tests, set up the world and add Lon and the quest to it
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
		// Set up using quest helper
		QuestHelper.setUpBeforeClass();

		// Get a mock Stendhal world
		MockStendlRPWorld.get();
		
        // Set up a testing zone
		final StendhalRPZone zone = new StendhalRPZone("admin_test");

		// Put Lon and the housekeeper in the zone
		new LonJathamNPC().configureZone(zone, null);
		new HouseKeeperNPC().configureZone(zone, null);

		// Add the quest to the world
		quest.addToWorld();

	} // setUpBeforeClass

	// Set up the player before the tests
	@Before
	public void setUp() {
		// Create a testing player		
		player = PlayerTestHelper.createPlayer("player");
	} // setUp

	// Test the quest if the user has their logbook
	@Test
	public void testQuestWithLogbook() {
		// Check slot is correct
		assertTrue(quest.getSlotName()==questSlot);

		/* TEST CASE 1: THE PLAYER FAILS THE TEST AND THEN PASSES IT.*/

        // Quest name
		assertEquals(quest.getName(), "JavaQuiz");

		// Region name
		assertEquals(quest.getRegion(), Region.KALAVAN);

		// NPC name
		assertEquals(quest.getNPCName(), "Lon Jatham");

		// The NPC is Lon Jatham
		lon = SingletonRepository.getNPCList().get("Lon Jatham");

		// Also need the Granny so we can get the logbook
		granny = SingletonRepository.getNPCList().get("Granny Graham");

		// The engine is Lon's engine
		lonEn = lon.getEngine();

		// Get the Granny's engine
		grannyEn = granny.getEngine();

		// Get the XP of the player
		final int xp2 = player.getXP();

		// Request logbook from Granny
		grannyEn.step(player, "hello");
		grannyEn.step(player, "logbook");

		// Make sure the player has a logbook
		assertTrue(player.isEquipped("logbook"));

		// Player starts by saying hi to Lon
		lonEn.step(player, "hi");

		// Make sure Lon can see that the user has their logbook
		assertEquals("Hello strang- Oh you have your logbook now! Would you like to try my Java #test?", getReply(lon));

		// He then asks to take the test
		lonEn.step(player, "test");

		// Lon asks him if he is ready
		assertEquals("Are you ready to take the test?", getReply(lon));

		// The player says he's ready
		lonEn.step(player, "yes");

		// Lon's response and the question he asks - this is variable.
	    String question = getReply(lon);

	    // Check response given before question
		assertTrue(question.contains("Very well. Here is your question"));

		// Check whether is each of the questions
	    boolean isQ1 = question.contains("Do you attend all my lectures?");
	    boolean isQ2 = question.contains("An object is an instance of a ...");
	    boolean isQ3 = question.contains("What result would Java give for 1/0.0?");

	    // Check is one of the questions
	    boolean isQ = (isQ1 || isQ2 || isQ3);
	    assertTrue(isQ);

	    // If is a question, check which one and give an incorrect answer
	    if(isQ)
	    {
	      if(isQ1)
	      {
	    	  lonEn.step(player,"I don't know");
	    	  assertEquals("Incorrect. #Study up and come back to me.", getReply(lon));
	      } // if
	      else if(isQ2)
	      {
	    	  lonEn.step(player,"Also don't know");
	    	  assertEquals("Incorrect. #Study up and come back to me.", getReply(lon));
	      } // if
	      else if(isQ3)
	      {
	    	  lonEn.step(player,"Again, don't know");
	    	  assertEquals("Incorrect. #Study up and come back to me.", getReply(lon));
	      } // else if
	    } // if

	    // Check the xp didn't increase
	    assertFalse(player.getXP()==(xp2+100));

	    // Check didn't get given a certificate
		assertFalse(player.isEquipped("certificate"));

		// Check that the history has been updated
		assertTrue(quest.getHistory(player).contains("The question I must answer is " + player.getQuest(questSlot) + "."));

		// Check has correct response to saying bye
	    lonEn.step(player, "bye");
		assertEquals("Gooooodbye! Study harder!!!", getReply(lon));

		// Hi again, now we try again
	    lonEn.step(player, "hi");

	    // Make sure Lon recognises that the user already tried the quiz
	    assertEquals("Hello student. As you have your logbook now, would you like to try my Java #test again?", getReply(lon));

	    // Do the test again
		lonEn.step(player, "test");

		// Give correct answer to whichever question
	    if(isQ)
	    {
	      if(isQ1)
	      {
	    	  lonEn.step(player,"yes");
	    	  assertEquals("Correct, well done! You now get some XP and a certificate!", getReply(lon));
	      } // if
	      else if(isQ2)
	      {
	    	  lonEn.step(player,"class");
	    	  assertEquals("Correct, well done! You now get some XP and a certificate!", getReply(lon));
	      } // else if
	      else if(isQ3)
	      {
	    	  lonEn.step(player,"Infinity");
	    	  assertEquals("Correct, well done! You now get some XP and a certificate!", getReply(lon));
	      } // else if
	    } // if

	    // Check got the additional XP
	    assertTrue(player.getXP()==(xp2+100));

	    // Check slot changed
		assertThat(player.getQuest(questSlot), is("done"));

		// Check got the certificate
		assertTrue(player.isEquipped("certificate"));

		// Check history changed
		assertTrue(quest.getHistory(player).contains("I passed Lon's exam and got the certificate."));

		// Check bye response correct
	    lonEn.step(player, "bye");
	    assertEquals("Gooooodbye! Study harder!!!", getReply(lon));

	    // Try and take the test again
	    lonEn.step(player, "hi");
	    lonEn.step(player, "test");
	    assertEquals("You've already taken the test! Now go ahead and code!!!", getReply(lon));

        // End conversation
	    lonEn.step(player, "bye");
	    
		/* TEST CASE 2: THE PLAYER PASSES THE TEST AND TRIES TO DO IT AGAIN.*/

	    // Get Lon and Granny again
		lon = SingletonRepository.getNPCList().get("Lon Jatham");
		granny = SingletonRepository.getNPCList().get("Granny Graham");

		// Get Lon's engine again
		lonEn = lon.getEngine();

		// Get Granny's engine again
		grannyEn = granny.getEngine();

		// Set the quest as null so can do it again
		player.setQuest(questSlot, null);

		// get the player's XP
		final int xp = player.getXP();

		// Request logbook from Granny
		grannyEn.step(player, "hello");
		grannyEn.step(player, "logbook");

		// Make sure the player has a logbook
		assertTrue(player.isEquipped("logbook"));

		// Say hi to Lon
		lonEn.step(player, "hi");

		// Make sure Lon can see that the user has their logbook
		assertEquals("Hello strang- Oh you have your logbook now! Would you like to try my Java #test?", getReply(lon));

		// Check the quest history has been updated
		assertTrue(quest.getHistory(player).contains("I met a Java lecturer! If I can pass his exam I can get some XP and a certificate!"));

		// Check he has correct response to asking to take the test
		lonEn.step(player, "test");
		assertEquals("Are you ready to take the test?", getReply(lon));

		// Say yes to test and get the question
		lonEn.step(player, "yes");
	    question = getReply(lon);

	    // Check given bit before question
		assertTrue(question.contains("Very well. Here is your question"));

		// Check if is each question
	    isQ1 = question.contains("Do you attend all my lectures?");
	    isQ2 = question.contains("An object is an instance of a ...");
	    isQ3 = question.contains("What result would Java give for 1/0.0?");

	    // Check if one of the questions is given.
	    isQ = (isQ1 || isQ2 || isQ3);
	    assertTrue(isQ);

	    // If is a question, see which one and give correct response and check Lon's response
	    if(isQ)
	    {
	      if(isQ1)
	      {
	    	  lonEn.step(player, "yes");
	    	  assertEquals("Correct, well done! You now get some XP and a certificate!", getReply(lon));
	      } // if
	      else if(isQ2)
	      {
	    	  lonEn.step(player, "class");
	    	  assertEquals("Correct, well done! You now get some XP and a certificate!", getReply(lon));
	      } // else if
	      else if(isQ3)
	      {
	    	  lonEn.step(player, "Infinity");
	    	  assertEquals("Correct, well done! You now get some XP and a certificate!", getReply(lon));
	      } // else if
	    } // if

	    // Check got increased XP
	    assertTrue(player.getXP()==(xp+100));

	    // Check quest now done
		assertThat(player.getQuest(questSlot), is("done"));

		// Check history is updated
		assertTrue(quest.getHistory(player).contains("I passed Lon's exam and got the certificate."));

		// Check bye response
	    lonEn.step(player, "bye");
	    assertEquals("Gooooodbye! Study harder!!!", getReply(lon));

	    // Say hi, try to do quiz again
	    lonEn.step(player, "hi");

	    // Make sure Lon can see that the user has their logbook
	    assertEquals("Hello my favourite student! Thank you for always keeping your logbook on you and getting 100% in my test!", getReply(lon));

	    // Ask to do again
	    lonEn.step(player, "test");
	    assertEquals("You've already taken the test! Now go ahead and code!!!", getReply(lon));

	    // End conversation
	    lonEn.step(player, "bye");

		/* TEST CASE 3: THE PLAYER DOESN'T WANT TO TAKE THE TEST.*/

	    // Get Lon again
		lon = SingletonRepository.getNPCList().get("Lon Jatham");

		// Get Lon's engine
		lonEn = lon.getEngine();

		// Set quest as null
		player.setQuest(questSlot, null);

		// Say hi
		lonEn.step(player, "hi");

		// Make sure Lon can see that the user has their logbook
		assertEquals("Hello strang- Oh you have your logbook now! Would you like to try my Java #test?", getReply(lon));

		// Ask to take the test
		lonEn.step(player, "test");
		assertEquals("Are you ready to take the test?", getReply(lon));

		// Say no
		lonEn.step(player, "no");
		assertEquals("Why wouldn't you want to take the test? Haven't you read my book?", getReply(lon));
		
		// Say bye to Lon
		lonEn.step(player, "bye");

	} // test

	// Test that Lon does not speak to the user if they don't have a logbook
	@Test
	public void testQuestWithoutLogbook() {
		/* TEST CASE 1: THE PLAYER TRIES TO TALK TO LON FOR THE FIRST TIME.*/

		// The NPC is Lon Jatham
		lon = SingletonRepository.getNPCList().get("Lon Jatham");

		// The engine is Lon's engine
		lonEn = lon.getEngine();
		
		// Make sure the player doesn't have a logbook
		assertFalse(player.isEquipped("logbook"));

		// Speak to Lon for the first time
		lonEn.step(player, "hello");

		// Make sure Lon makes it clear he does not want to speak to the player
		assertEquals("Hello stranger. I will not talk to you until you bring your logbook! I think I left some in Granny Graham's cottage...", getReply(lon));

		// Make sure Lon becomes idle (so he will not speak to the user anymore)
		assertEquals(ConversationStates.IDLE, lonEn.getCurrentState());
		
		/* TEST CASE 2: THE PLAYER STARTS THE QUEST BUT THEN TRIES TO TALK TO LON WITHOUT A LOGBOOK.*/
		
		// Check slot is correct
		assertTrue(quest.getSlotName()==questSlot);

        // Quest name
		assertEquals(quest.getName(), "JavaQuiz");

		// Region name
		assertEquals(quest.getRegion(), Region.KALAVAN);
		
		// Set the quest to be active but not complete
		player.setQuest(questSlot, "Do you attend all my lectures?");
		
		// Make sure the player doesn't have a logbook
		assertFalse(player.isEquipped("logbook"));

		// Speak to Lon 
		lonEn.step(player, "hello");
		
		// Make sure Lon makes it clear he does not want to speak to the player
		assertEquals("Hello student. Where did your logbook go?! I won't let you finish my Java quiz unless you have your logbook!", getReply(lon));

		// Make sure Lon becomes idle (so he will not speak to the user anymore)
		assertEquals(ConversationStates.IDLE, lonEn.getCurrentState());		
		
		
		/* TEST CASE 3: THE PLAYER HAS COMPLETED THE QUEST BUT THEN TRIES TO TALK TO LON WITHOUT A LOGBOOK.*/
		
		// Set the quest to be complete
		player.setQuest(questSlot, "done");
		
		// Make sure the player doesn't have a logbook
		assertFalse(player.isEquipped("logbook"));

		// Speak to Lon 
		lonEn.step(player, "hello");
		
		// Make sure Lon makes it clear he does not want to speak to the player
		assertEquals("Hello student. Where did your logbook go?! I don't talk to students who don't have their logbook, even if they get 100% in my test...", getReply(lon));

		// Make sure Lon becomes idle (so he will not speak to the user anymore)
		assertEquals(ConversationStates.IDLE, lonEn.getCurrentState());

	} // test
}
