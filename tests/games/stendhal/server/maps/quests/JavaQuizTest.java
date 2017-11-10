package games.stendhal.server.maps.quests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static utilities.SpeakerNPCTestHelper.getReply;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.fsm.Engine;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.maps.MockStendlRPWorld;
import games.stendhal.server.maps.Region;
import utilities.PlayerTestHelper;
import utilities.QuestHelper;
import games.stendhal.server.maps.kalavan.citygardens.LonJathamNPC;

// Test for Java quiz
public class JavaQuizTest {

	// Quest slot is the java quest
	private static String questSlot = "java_quest";

	// A player to attempt the quest
	private Player player = null;
	
	// A speakerNPC, Lon Jatham, to give the quest to our player
	private SpeakerNPC npc = null;
	
	// The NPC's engine
	private Engine en = null;
	
	// The Java quiz test
	public static final AbstractQuest quest = new JavaQuiz();
	
	// Before the tests, set up the world and add Lon and the quest to it
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		QuestHelper.setUpBeforeClass();

		MockStendlRPWorld.get();

		final StendhalRPZone zone = new StendhalRPZone("admin_test");

		new LonJathamNPC().configureZone(zone, null);

		quest.addToWorld();

	} // setUpBeforeClass
	
	// Set up the player before the tests
	@Before
	public void setUp() {
		player = PlayerTestHelper.createPlayer("player");
	} // setUp
	
	// Test the quest
	@Test
	public void testQuest() {
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
		npc = SingletonRepository.getNPCList().get("Lon Jatham");

		// The engine is Lon's engine
		en = npc.getEngine();

		// Get the XP of the player
		final int xp2 = player.getXP();
		
		// Player starts by saying hi to Lon
		en.step(player, "hi");
        
		// He then asks to take the test
		en.step(player, "test");
		
		// Lon asks him if he is ready
		assertEquals("Are you ready to take the test?", getReply(npc));
		
		// The player says he's ready
		en.step(player, "yes");
		
		// Lon's response and the question he asks - this is variable.
	    String question = getReply(npc);

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
	    	  en.step(player,"I don't know");
	    	  assertEquals("Incorrect. #Study up and come back to me.", getReply(npc));
	      } // if
	      else if(isQ2)
	      {
	    	  en.step(player,"Also don't know");
	    	  assertEquals("Incorrect. #Study up and come back to me.", getReply(npc));
	      } // if
	      else if(isQ3)
	      {
	    	  en.step(player,"Again, don't know");
	    	  assertEquals("Incorrect. #Study up and come back to me.", getReply(npc));
	      } // else if
	    } // if
	    
	    // Check the xp didn't increase
	    assertFalse(player.getXP()==(xp2+100));
	    
	    // Check didn't get given a certificate
		assertFalse(player.isEquipped("certificate"));
		
		// Check that the history has been updated
		assertTrue(quest.getHistory(player).contains("The question I must answer is " + player.getQuest(questSlot) + "."));

		// Check has correct response to saying bye
	    en.step(player, "bye");
		assertEquals("Gooooodbye! Study harder!!!", getReply(npc));

		// Hi again, now we try again
	    en.step(player, "hi");

	    // Do the test again
		en.step(player, "test");
		
		// Give correct answer to whichever question
	    if(isQ)
	    {
	      if(isQ1)
	      {
	    	  en.step(player,"yes");
	    	  assertEquals("Correct, well done! You now get some XP and a certificate!", getReply(npc));
	      } // if
	      else if(isQ2)
	      {
	    	  en.step(player,"class");
	    	  assertEquals("Correct, well done! You now get some XP and a certificate!", getReply(npc));
	      } // else if
	      else if(isQ3)
	      {
	    	  en.step(player,"Infty");
	    	  assertEquals("Correct, well done! You now get some XP and a certificate!", getReply(npc));
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
	    en.step(player, "bye");
	    assertEquals("Gooooodbye! Study harder!!!", getReply(npc));

	    // Try and take the test again
	    en.step(player, "hi");
	    en.step(player, "test");
	    assertEquals("You've already taken the test! Now go ahead and code!!!", getReply(npc));
	    
		/* TEST CASE 2: THE PLAYER PASSES THE TEST AND TRIES TO DO IT AGAIN.*/
		
	    // Get Lon again
		npc = SingletonRepository.getNPCList().get("Lon Jatham");

		// Get Lon's engine again
		en = npc.getEngine();
		
		// Set the quest as null so can do it again
		player.setQuest(questSlot, null);
		
		// get the player's XP
		final int xp = player.getXP();
		
		// Say hi to Lon
		en.step(player, "hi");
		
		// Check the quest history has been updated
		assertTrue(quest.getHistory(player).contains("I met a Java lecturer! If I can pass his exam I can get some XP and a certificate!"));
		
		// Check he has correct response to asking to take the test
		en.step(player, "test");
		assertEquals("Are you ready to take the test?", getReply(npc));
		
		// Say yes to test and get the question
		en.step(player, "yes");
	    question = getReply(npc);
	    
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
	    	  en.step(player, "yes");
	    	  assertEquals("Correct, well done! You now get some XP and a certificate!", getReply(npc));
	      } // if
	      else if(isQ2)
	      {
	    	  en.step(player, "class");
	    	  assertEquals("Correct, well done! You now get some XP and a certificate!", getReply(npc));
	      } // else if
	      else if(isQ3)
	      {
	    	  en.step(player, "Infty");
	    	  assertEquals("Correct, well done! You now get some XP and a certificate!", getReply(npc));
	      } // else if
	    } // if
	    
	    // Check got increased XP
	    assertTrue(player.getXP()==(xp+100));
	    
	    // Check quest now done
		assertThat(player.getQuest(questSlot), is("done"));
		
		// Check history is updated
		assertTrue(quest.getHistory(player).contains("I passed Lon's exam and got the certificate."));

		// Check bye response
	    en.step(player, "bye");
	    assertEquals("Gooooodbye! Study harder!!!", getReply(npc));

	    // Say hi, try to do quiz again
	    en.step(player, "hi");

	    // Ask to do again
	    en.step(player, "test");
	    assertEquals("You've already taken the test! Now go ahead and code!!!", getReply(npc));
		
		/* TEST CASE 3: THE PLAYER PASSES THE TEST AND TRIES TO DO IT AGAIN.*/

	    // Get Lon again
		npc = SingletonRepository.getNPCList().get("Lon Jatham");

		// Get Lon's engine
		en = npc.getEngine();

		// Set quest as null
		player.setQuest(questSlot, null);
		
		// Say hi
		en.step(player, "hi");
		
		// Ask to take the test
		en.step(player, "test");
		assertEquals("Are you ready to take the test?", getReply(npc));
		
		// Say no
		en.step(player, "no");
		assertEquals("Why wouldn't you want to take the test? Haven't you read my book?", getReply(npc));		
		
	} // test
}
