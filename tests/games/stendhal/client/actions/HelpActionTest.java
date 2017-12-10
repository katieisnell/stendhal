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
package games.stendhal.client.actions;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;

import games.stendhal.client.ClientSingletonRepository;
import games.stendhal.client.MockStendhalClient;
import games.stendhal.client.StendhalClient;
import games.stendhal.client.gui.MockUserInterface;
import marauroa.common.game.RPAction;

public class HelpActionTest {

	@After
	public void tearDown() throws Exception {
		StendhalClient.resetClient();
	}
	
	/**
	 * Tests for execute.
	 */
	@Test
	public void testExecute() {
		new MockStendhalClient() {
			@Override
			public void send(final RPAction action) {
				assertEquals("help", action.get("type"));
				assertEquals("schnick", action.get("text"));

			}
		};
		MockUserInterface mockInterface = new MockUserInterface();
		ClientSingletonRepository.setUserInterface(mockInterface);
		final HelpAction action = new HelpAction();
		// Try to execute the action and make sure it executes correctly
		assertTrue(action.execute(new String[] {null}, ""));
		// Check that the output of execution is as expected for volume
		assertEquals(mockInterface.getLastEventLine(), "- /volume\t\tLists or sets the volume for sound and music");
	}

	/**
	 * Tests for getMaximumParameters.
	 */
	@Test
	public void testGetMaximumParameters() {
		final HelpAction action = new HelpAction();
		assertThat(action.getMaximumParameters(), is(0));
	}

	/**
	 * Tests for getMinimumParameters.
	 */
	@Test
	public void testGetMinimumParameters() {
		final HelpAction action = new HelpAction();
		assertThat(action.getMinimumParameters(), is(0));
	}

}
