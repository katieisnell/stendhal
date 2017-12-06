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
package games.stendhal.client.core.config;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import games.stendhal.server.core.rule.defaultruleset.DefaultAction;
import org.junit.Test;
import org.xml.sax.SAXException;


public class ActionGroupsXMLLoaderTest {

	/**
	 * Tests for Invisible.
	 * @throws URISyntaxException 
	 * @throws SAXException 
	 * @throws IOException 
	 */
	@Test
	public void testInvisible() throws SAXException, URISyntaxException, IOException {
		
		// Load the action
		ActionGroupsXMLLoader loader = new ActionGroupsXMLLoader(new URI("actionstest.xml"));
		List<DefaultAction> list = loader.load();
		DefaultAction action = list.get(0);		
		
		//Check XML data is dealt with properly
		assertEquals(action.getName(), ("invisible"));
		assertEquals(action.getMinimumParameters(), 0);
		assertEquals(action.getMaximumParameters(), 0);
		assertEquals(action.getImplementation(), ("games.stendhal.client.actions.InvisibleAction"));
		assertEquals(action.getType(), ("invisible"));

	}
	
	@Test
	public void testTeleport() throws SAXException, URISyntaxException, IOException {
		
		// Load the action
		ActionGroupsXMLLoader loader = new ActionGroupsXMLLoader(new URI("actionstest.xml"));
		List<DefaultAction> list = loader.load();
		DefaultAction action = list.get(1);	
		
		//Check XML data is dealt with properly
		assertEquals(action.getName(), ("teleport"));
		assertEquals(action.getMinimumParameters(), 4);
		assertEquals(action.getMaximumParameters(), 4);
		assertEquals(action.getImplementation(), ("games.stendhal.client.actions.TeleportAction"));
		assertEquals(action.getType(), ("teleport"));

	}
	
	@Test
	public void testMessage() throws SAXException, URISyntaxException, IOException {
		
		// Load the action
		ActionGroupsXMLLoader loader = new ActionGroupsXMLLoader(new URI("actionstest.xml"));
		List<DefaultAction> list = loader.load();
		DefaultAction action = list.get(2);	
		
		//Check XML data is dealt with properly
		assertEquals(action.getName(), ("tell"));
		assertEquals(action.getMinimumParameters(), 1);
		assertEquals(action.getMaximumParameters(), 1);
		assertEquals(action.getImplementation(), ("games.stendhal.client.actions.MessageAction"));
		assertEquals(action.getType(), ("tell"));

	}
}
