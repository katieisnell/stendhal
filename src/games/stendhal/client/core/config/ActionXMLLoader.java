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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import games.stendhal.server.core.rule.defaultruleset.DefaultAction;

public final class ActionXMLLoader extends DefaultHandler {
	
	private static final Logger logger = Logger.getLogger(ActionXMLLoader.class);
	
	private String action;

	private String implementation;
	
	private Map<String, String> parameterValues;

	private List<DefaultAction> loadedActions;

	private boolean parameterTagFound;

	private String type;

	public Collection<DefaultAction> load(URI uri) throws SAXException {
		loadedActions = new LinkedList<DefaultAction>();
		// Use the default (non-validating) parser
		final SAXParserFactory factory = SAXParserFactory.newInstance();
		try {
			// Parse the input
			final SAXParser saxParser = factory.newSAXParser();
	
			final InputStream is = ActionXMLLoader.class.getResourceAsStream(uri.getPath());
	
			if (is == null) {
				throw new FileNotFoundException("cannot find resource '" + uri
						+ "' in classpath");
			}
			try {
				saxParser.parse(is, this);
			} finally {
				is.close();
			}
		} catch (final ParserConfigurationException t) {
			logger.error(t);
		} catch (final IOException e) {
			logger.error(e);
			throw new SAXException(e);
		}
		return loadedActions;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		if(qName.equals("action")) {
			action = attributes.getValue("name");
		}
		if(qName.equals("implementation")) {
			implementation = attributes.getValue("class-name");
		}
		
		if(qName.equals("type")) {
			type = attributes.getValue("value");
		}
		if(qName.equals("number_of_parameters")) {
			parameterTagFound = true;
			parameterValues = new HashMap<String, String>();
		}
		if(parameterTagFound) {
			parameterValues.put(qName, attributes.getValue("value"));
		}
	}
	
	
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		if(qName.equals("action")) {
			DefaultAction action = new DefaultAction(type);
			
			action.setImplementation(implementation);
			
			if(parameterValues.containsKey("minimum")) {
				action.setMinimumParameters(parameterValues.get("minimum"));
			}
			if(parameterValues.containsKey("maximum")) {
				action.setMaximumParameters(parameterValues.get("maximum"));
			}
			
			
			
			loadedActions.add(action);
		}
		if(qName.equals("number_of_parameters")) {
			parameterTagFound = false;
		}
	}

	public String get(String name)  {
		switch (name) {
		
		case "name" :       return action;
		case "minimum" :    return parameterValues.get(name);
		case "maximum" :    return parameterValues.get(name);
		case "type" :       return type;
		case "class-name" : return implementation;
		default :           return "Doesn't exist";
		
	    }	
	}
}