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
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;
import org.xml.sax.SAXException;
import games.stendhal.client.core.config.ActionXMLLoader;


public class ActionXMLLoaderTest {

	/**
	 * Tests for Invisible.
	 * @throws URISyntaxException 
	 * @throws SAXException 
	 */
	@Test
	public void testInvisible() throws SAXException, URISyntaxException {
		
		// Load the action
		ActionXMLLoader action = new ActionXMLLoader();
		action.load(new URI("/data/conf/actions/invisible.xml"));
		
		//Check XML data is dealt with properly
		assertEquals(action.get("name"), ("invisible"));
		assertEquals(action.get("minimum"), ("0"));
		assertEquals(action.get("maximum"), ("0"));
		assertEquals(action.get("class-name"), ("games.stendhal.client.actions.InvisibleAction"));
		assertEquals(action.get("type"), ("invisible"));

	}
	
	@Test
	public void testTeleport() throws SAXException, URISyntaxException {
		
		// Load the action
		ActionXMLLoader action = new ActionXMLLoader();
		action.load(new URI("/data/conf/actions/teleport.xml"));
		
		//Check XML data is dealt with properly
		assertEquals(action.get("name"), ("teleport"));
		assertEquals(action.get("minimum"), ("4"));
		assertEquals(action.get("maximum"), ("4"));
		assertEquals(action.get("class-name"), ("games.stendhal.client.actions.TeleportAction"));
		assertEquals(action.get("type"), ("teleport"));

	}
	
	@Test
	public void testMessage() throws SAXException, URISyntaxException {
		
		// Load the action
		ActionXMLLoader action = new ActionXMLLoader();
		action.load(new URI("/data/conf/actions/message.xml"));
		
		//Check XML data is dealt with properly
		assertEquals(action.get("name"), ("tell"));
		assertEquals(action.get("minimum"), ("1"));
		assertEquals(action.get("maximum"), ("1"));
		assertEquals(action.get("class-name"), ("games.stendhal.client.actions.MessageAction"));
		assertEquals(action.get("type"), ("tell"));

	}
}


