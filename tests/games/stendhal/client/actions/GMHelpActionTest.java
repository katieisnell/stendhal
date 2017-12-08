package games.stendhal.client.actions;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.client.ClientSingletonRepository;
import games.stendhal.client.MockStendhalClient;
import games.stendhal.client.StendhalClient;
import games.stendhal.client.gui.MockUserInterface;
//import games.stendhal.client.util.UserInterfaceTestHelper;
import marauroa.common.game.RPAction;

public class GMHelpActionTest {
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
		StendhalClient.resetClient();
	}

	@Test
	public void testExecute() {
		new MockStendhalClient() {
			@Override
			public void send(final RPAction action) {
				assertEquals("gmhelp", action.get("type"));
			}
		};
		MockUserInterface inter = new MockUserInterface();
		ClientSingletonRepository.setUserInterface(inter);
		final GMHelpAction action = new GMHelpAction();
		assertTrue(action.execute(new String[] {null}, ""));
		assertEquals(inter.getLastEventLine(), "- /destroy <entity> \t\t\tDestroy an entity completely");
		assertTrue(action.execute(new String[] {"alter"}, ""));
		assertEquals(inter.getLastEventLine(), "This will make <testplayer> look like danter");
		assertTrue(action.execute(new String[] {"script"}, ""));
		assertEquals(inter.getLastEventLine(), "#/script #ResetSlot.class #player #slot : Resets the named slot such as !kills or !quests. Useful for debugging.");
		assertTrue(action.execute(new String[] {"support"}, ""));
		assertEquals(inter.getLastEventLine(), "$notsupport - Hi %s, sorry, but support cannot help with this issue. Please use #https://stendhalgame.org and the wiki #https://stendhalgame.org/wiki/Stendhal as information sources.");
		assertFalse(action.execute(new String[] {"schnick", "schnack"}, ""));
		assertFalse(action.execute(new String[] {"schnick"}, ""));
		
	}
	
	/**
	 * Tests for getMaximumParameters.
	 */
	@Test
	public void testGetMaximumParameters() {
		final GMHelpAction action = new GMHelpAction();
		assertThat(action.getMaximumParameters(), is(1));
	}

	/**
	 * Tests for getMinimumParameters.
	 */
	@Test
	public void testGetMinimumParameters() {
		final GMHelpAction action = new GMHelpAction();
		assertThat(action.getMinimumParameters(), is(0));
	}

}
