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
import static org.junit.Assert.assertNull;

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
	public void testLoad() throws SAXException, URISyntaxException {
		
		// Load the action
		ActionXMLLoader action = new ActionXMLLoader();
		action.load(new URI("/data/conf/actions/admin_actions.xml"));
		
		//Check XML data is dealt with properly
		assertEquals(action.getName(), ("tellall"));
		assertEquals(action.getMinimumParameters(), ("0"));
		assertEquals(action.getMaximumParameters(), ("0"));
		assertEquals(action.getImplementation(), ("games.stendhal.client.actions.DefaultAction"));
		assertEquals(action.getType(), ("tellall"));
		
		// Parameters and remainders specific to this action
		assertNull(action.getParamIndices(0));
		assertEquals(action.getRemainder(), ("text"));

	}

}


