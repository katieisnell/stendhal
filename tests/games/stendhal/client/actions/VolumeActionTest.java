package games.stendhal.client.actions;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

import games.stendhal.client.ClientSingletonRepository;
import games.stendhal.client.MockStendhalClient;
import games.stendhal.client.gui.MockUserInterface;
import marauroa.common.game.RPAction;

public class VolumeActionTest {

	@Test
	public void testExecute() {
		new MockStendhalClient() {
			@Override
			public void send(final RPAction action) {
				assertEquals("volume", action.get("type"));
			}
		};
		MockUserInterface inter = new MockUserInterface();
		ClientSingletonRepository.setUserInterface(inter);
		final VolumeAction action = new VolumeAction();
		assertTrue(action.execute(new String[] {null}, ""));
		assertEquals(inter.getLastEventLine(), "master -> 0");
		assertTrue(action.execute(new String[] {"master", null}, ""));
		assertEquals(inter.getLastEventLine(), "Please use /volume for help.");
		assertTrue(action.execute(new String[] {"master", "85"}, ""));
	}
	
	/**
	 * Tests for getMaximumParameters.
	 */
	@Test
	public void testGetMaximumParameters() {
		final VolumeAction action = new VolumeAction();
		assertThat(action.getMaximumParameters(), is(2));
	}

	/**
	 * Tests for getMinimumParameters.
	 */
	@Test
	public void testGetMinimumParameters() {
		final VolumeAction action = new VolumeAction();
		assertThat(action.getMinimumParameters(), is(0));
	}
}
