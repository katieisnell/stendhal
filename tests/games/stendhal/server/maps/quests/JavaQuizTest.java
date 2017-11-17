/* $Id$ */
/***************************************************************************
 *                   (C) Copyright 2003-2010 - Stendhal                    *
 ***************************************************************************
 ***************************************************************************
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 ***************************************************************************/
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

/**
 * Tests for Java quiz quest.
 *
 * @author Poppy Reid
 * @author Katie Snell
 */
public class JavaQuizTest {
	private static final String LOGBOOK = "logbook";

	private static String questSlot = "java_quest";

	public static final AbstractQuest quest = new JavaQuiz();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		QuestHelper.setUpBeforeClass();
		MockStendlRPWorld.get();
		
		final StendhalRPZone zone = new StendhalRPZone("testzone");

		new LonJathamNPC().configureZone(zone, null);
		new HouseKeeperNPC().configureZone(zone, null);

		quest.addToWorld();
	} 

	private Player player;
	
	private SpeakerNPC lon;
	
	private SpeakerNPC granny;
	
	private Engine lonEn = null;

	private Engine grannyEn = null;
	
	@Before
	public void setUp() {
		player = PlayerTestHelper.createPlayer("player");		
	} 
	
	/* Variables needed for every test are declared below */ 
	
    String question = null;
    
    boolean isQ1 = false;
    
    boolean isQ2 = false;
    
    boolean isQ3 = false;
    
    boolean isQ = false;
    
    
	/*
	 *  Test the quest if the player fails the test and then passes it.
	 */
	@Test
	public void testPlayerFailsThenPassesWithLogbook() {
		assertTrue(quest.getSlotName()==questSlot);
		assertEquals(quest.getName(), "JavaQuiz");
		assertEquals(quest.getRegion(), Region.KALAVAN);
		assertEquals(quest.getNPCName(), "Lon Jatham");
		lon = SingletonRepository.getNPCList().get("Lon Jatham");
		granny = SingletonRepository.getNPCList().get("Granny Graham");
		lonEn = lon.getEngine();
		grannyEn = granny.getEngine();
		final int xp2 = player.getXP();

		// Request logbook from Granny
		grannyEn.step(player, "hello");
		grannyEn.step(player, LOGBOOK);
		assertTrue(player.isEquipped(LOGBOOK));
		
		// ask to take the test
		lonEn.step(player, "hi");
		assertEquals("Hello strang- Oh you have your logbook now! Would you like to try my Java #test?", getReply(lon));
		lonEn.step(player, "test");
		assertEquals("Are you ready to take the test?", getReply(lon));
		lonEn.step(player, "yes");
	    String question = getReply(lon);
		assertTrue(question.contains("Very well. Here is your question"));
		
		// Check whether is each of the questions
	    boolean isQ1 = question.contains("Do you attend all my lectures?");
	    boolean isQ2 = question.contains("An object is an instance of a ...");
	    boolean isQ3 = question.contains("What result would Java give for 1/0.0?");
	    boolean isQ = (isQ1 || isQ2 || isQ3);
	    assertTrue(isQ);

	    // If is a question, check which one and give an incorrect answer
	    if(isQ)
	    {
	      if(isQ1)
	      {
	    	  lonEn.step(player,"I don't know");
	    	  assertEquals("Incorrect. #Study up and come back to me.", getReply(lon));
	      } 
	      else if(isQ2)
	      {
	    	  lonEn.step(player,"Also don't know");
	    	  assertEquals("Incorrect. #Study up and come back to me.", getReply(lon));
	      } 
	      else if(isQ3)
	      {
	    	  lonEn.step(player,"Again, don't know");
	    	  assertEquals("Incorrect. #Study up and come back to me.", getReply(lon));
	      } 
	    } 

	    assertFalse(player.getXP()==(xp2+100));
		assertFalse(player.isEquipped("certificate"));
		assertTrue(quest.getHistory(player).contains("The question I must answer is " + player.getQuest(questSlot) + "."));
	    lonEn.step(player, "bye");
		assertEquals("Gooooodbye! Study harder!!!", getReply(lon));

		// now try again
	    lonEn.step(player, "hi");
	    assertEquals("Hello student. As you have your logbook now, would you like to try my Java #test again?", getReply(lon));
		lonEn.step(player, "test");
		
	    if(isQ)
	    {
	      if(isQ1)
	      {
	    	  lonEn.step(player,"yes");
	    	  assertEquals("Correct, well done! You now get some XP and a certificate!", getReply(lon));
	      } 
	      else if(isQ2)
	      {
	    	  lonEn.step(player,"class");
	    	  assertEquals("Correct, well done! You now get some XP and a certificate!", getReply(lon));
	      } 
	      else if(isQ3)
	      {
	    	  lonEn.step(player,"Infinity");
	    	  assertEquals("Correct, well done! You now get some XP and a certificate!", getReply(lon));
	      }
	    }

	    assertTrue(player.getXP()==(xp2+100));
		assertThat(player.getQuest(questSlot), is("done"));
		assertTrue(player.isEquipped("certificate"));
		assertTrue(quest.getHistory(player).contains("I passed Lon's exam and got the certificate."));
	    lonEn.step(player, "bye");
	    assertEquals("Gooooodbye! Study harder!!!", getReply(lon));

	    // Try and take the test again
	    lonEn.step(player, "hi");
	    lonEn.step(player, "test");
	    assertEquals("You've already taken the test! Now go ahead and code!!!", getReply(lon));
	    lonEn.step(player, "bye");
	    
	}
	
	/*
	 *  Test the quest if the player passes the test and tries to do it again.
	 */
	@Test
	public void testPlayerPassesThenTriesAgainWithLogbook() {
		lon = SingletonRepository.getNPCList().get("Lon Jatham");
		granny = SingletonRepository.getNPCList().get("Granny Graham");
		lonEn = lon.getEngine();
		grannyEn = granny.getEngine();
		player.setQuest(questSlot, null);
		final int xp = player.getXP();

		// Request logbook from Granny
		grannyEn.step(player, "hello");
		grannyEn.step(player, LOGBOOK);
		assertTrue(player.isEquipped(LOGBOOK));

		// Talk to Lon
		lonEn.step(player, "hi");
		assertEquals("Hello strang- Oh you have your logbook now! Would you like to try my Java #test?", getReply(lon));
		assertTrue(quest.getHistory(player).contains("I met a Java lecturer! If I can pass his exam I can get some XP and a certificate!"));
		lonEn.step(player, "test");
		assertEquals("Are you ready to take the test?", getReply(lon));
		lonEn.step(player, "yes");
	    question = getReply(lon);
		assertTrue(question.contains("Very well. Here is your question"));

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
	      } 
	      else if(isQ2)
	      {
	    	  lonEn.step(player, "class");
	    	  assertEquals("Correct, well done! You now get some XP and a certificate!", getReply(lon));
	      } 
	      else if(isQ3)
	      {
	    	  lonEn.step(player, "Infinity");
	    	  assertEquals("Correct, well done! You now get some XP and a certificate!", getReply(lon));
	      }
	    }

	    assertTrue(player.getXP()==(xp+100));
	    assertThat(player.getQuest(questSlot), is("done"));
		assertTrue(quest.getHistory(player).contains("I passed Lon's exam and got the certificate."));
	    lonEn.step(player, "bye");
	    assertEquals("Gooooodbye! Study harder!!!", getReply(lon));

	    // Say hi, try to do quiz again
	    lonEn.step(player, "hi");
	    assertEquals("Hello my favourite student! Thank you for always keeping your logbook on you and getting 100% in my #test!", getReply(lon));
	    lonEn.step(player, "test");
	    assertEquals("You've already taken the test! Now go ahead and code!!!", getReply(lon));
	    lonEn.step(player, "bye");
	    
	}
	
	/* 
	 * Test Lon's response if the player doesn't want to take the test.
	 */
	@Test
	public void testPlayerDoesntWantToTakeTestWithLogbook() {
		lon = SingletonRepository.getNPCList().get("Lon Jatham");
		granny = SingletonRepository.getNPCList().get("Granny Graham");
		lonEn = lon.getEngine();
		grannyEn = granny.getEngine();
		player.setQuest(questSlot, null);
		grannyEn.step(player, "hello");
		grannyEn.step(player, LOGBOOK);
		assertTrue(player.isEquipped(LOGBOOK));

		// Say no to quiz
		lonEn.step(player, "hi");
		assertEquals("Hello strang- Oh you have your logbook now! Would you like to try my Java #test?", getReply(lon));
		lonEn.step(player, "test");
		assertEquals("Are you ready to take the test?", getReply(lon));
		lonEn.step(player, "no");
		assertEquals("Why wouldn't you want to take the test? Haven't you read my book?", getReply(lon));
		lonEn.step(player, "bye");

	} 

	/*
	 * Test what Lon's response is if the player speaks to him for the first time and without a logbook.
	 */
	@Test
	public void testTalkingToLonForFirstTimeWithoutLogbook() {
		lon = SingletonRepository.getNPCList().get("Lon Jatham");
		lonEn = lon.getEngine();
		assertFalse(player.isEquipped(LOGBOOK));
		lonEn.step(player, "hello");
		assertEquals("Hello stranger. I will not talk to you until you bring your logbook! I think I left some in Granny Graham's cottage...", getReply(lon));
		assertEquals(ConversationStates.IDLE, lonEn.getCurrentState());
	}
	
	/*
	 * Test what Lon's response is if the player speaks to him whilst the quest is active but without a logbook.
	 */	
	@Test
	public void testTalkingToLonWithActiveQuestWithoutLogbook() {		
		lon = SingletonRepository.getNPCList().get("Lon Jatham");
		lonEn = lon.getEngine();
		assertTrue(quest.getSlotName()==questSlot);
		assertEquals(quest.getName(), "JavaQuiz");
		assertEquals(quest.getRegion(), Region.KALAVAN);
		
		// Set the quest to be active but not complete
		player.setQuest(questSlot, "Do you attend all my lectures?");
		
		assertFalse(player.isEquipped(LOGBOOK));
		assertEquals(ConversationStates.IDLE, lonEn.getCurrentState());	
		lonEn.step(player, "hello");
		assertEquals("Hello student. Where did your logbook go?! I won't let you finish my Java quiz unless you have your logbook!", getReply(lon));

		// Make sure Lon becomes idle (so he will not speak to the user anymore)
		assertEquals(ConversationStates.IDLE, lonEn.getCurrentState());		
		
	}
	
	/*
	 * Test what Lon's response is if the player speaks to him for having completed the quest but without a logbook.
	 */	
	@Test
	public void testTalkingToLonWithCompletedQuestWithoutLogbook() {		
		lon = SingletonRepository.getNPCList().get("Lon Jatham");
		lonEn = lon.getEngine();
		player.setQuest(questSlot, "done");
		assertFalse(player.isEquipped(LOGBOOK));
		assertEquals(ConversationStates.IDLE, lonEn.getCurrentState());	
		lonEn.step(player, "hello");
		assertEquals("Hello student. Where did your logbook go?! I don't talk to students who don't have their logbook, even if they get 100% in my test...", getReply(lon));

		// Make sure Lon becomes idle (so he will not speak to the user anymore)
		assertEquals(ConversationStates.IDLE, lonEn.getCurrentState());

	} 
}
