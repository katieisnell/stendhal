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
package games.stendhal.client.core.rule.defaultruleset;

import static org.junit.Assert.assertEquals;
import java.net.URI;
import java.net.URISyntaxException;
import org.junit.Test;
import org.xml.sax.SAXException;
import games.stendhal.client.core.config.ActionXMLLoader;


public class DefaultActionTest {

	@Test
	public void testSetAndGetMethods() {
		// Create the action
		DefaultAction action = new DefaultAction("type");
        String[] params = { "param0", "param1" };
        
		// Put the attributes in
		action.setImplementation("aURI");
		action.setMinimumParameters("2");
		action.setMaximumParameters("2");
		action.setName("type");
        action.addParamKeyAtIndex(0, params[0]);
        action.addParamKeyAtIndex(1, params[1]);
        action.addRemainderKey("remainder");
        
        // Check attributes in right places
        assertEquals(action.getMaximumParameters(), 2);
        assertEquals(action.getMinimumParameters(), 2);
        assertEquals(action.getType(), "type");
        assertEquals(action.getImplementation(), "aURI");
        assertEquals(action.getName(), "type");
        assertEquals(action.getParamsKeys()[0], params[0]);
        assertEquals(action.getParamsKeys()[1], params[1]);
        assertEquals(action.getRemainder(), "remainder");
	}

	@Test
	public void testExecute() throws SAXException, URISyntaxException {
		
		// Load the action - this involves calling the execute method
		ActionXMLLoader action = new ActionXMLLoader();
		action.load(new URI("/data/conf/actions/invisible.xml"));
		
		//Check XML data is dealt with properly
		assertEquals(action.getName(), ("invisible"));
		assertEquals(action.getMinimumParameters(), ("0"));
		assertEquals(action.getMaximumParameters(), ("0"));
		assertEquals(action.getImplementation(), ("games.stendhal.client.actions.DefaultAction"));
		assertEquals(action.getType(), ("invisible"));
	}
}
