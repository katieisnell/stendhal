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

import java.io.IOException;
import java.net.URI;
import java.util.LinkedList;
import java.util.List;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import games.stendhal.client.core.rule.defaultruleset.DefaultAction;

/**
 * Load and configure actions via an XML configuration file.
 *
 */
public class ActionGroupsXMLLoader extends DefaultHandler {

	private static final Logger LOGGER = Logger.getLogger(ActionGroupsXMLLoader.class);

	/** The main action configuration file. */
	protected URI uri;
	
	/**
	 * Create an xml based loader of action groups.
	 *
	 * @param uri
	 *            The location of the configuration file.
	 */
	public ActionGroupsXMLLoader(final URI uri) {
		this.uri = uri;
	}
	
	/**
	 * Load actions.
	 *
	 * @return list of actions
	 * @throws SAXException
	 *             If a SAX error occurred.
	 * @throws IOException
	 *             If an I/O error occurred.
	 */
	public List<DefaultAction> load() throws SAXException, IOException {
		final GroupsXMLLoader groupsLoader = new GroupsXMLLoader(uri);
		final List<URI> groups = groupsLoader.load();

		final ActionXMLLoader loader = new ActionXMLLoader();
		final List<DefaultAction> list = new LinkedList<DefaultAction>();
		for (final URI groupUri : groups) {
			LOGGER.debug("Loading actions group [" + groupUri + "]");
			list.addAll(loader.load(groupUri));
		}

		return list;
	}
}