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

import games.stendhal.client.core.rule.defaultruleset.DefaultAction;

public final class ActionXMLLoader extends DefaultHandler {
	
	private static final Logger logger = Logger.getLogger(ActionXMLLoader.class);
	
	private String actionName;

	private String implementation;
	
	private String remainderKey;
	
	private Map<String, String> parameterValues;
	
	private Map<Integer, String> paramIndices;

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
			actionName = attributes.getValue("name");
			paramIndices = new HashMap<>();
		}
		if(qName.equals("implementation")) {
			implementation = attributes.getValue("class-name");
		}
		
		if(qName.equals("type")) {
			type = attributes.getValue("value");
		}
		if(qName.equals("param")){
			int index = Integer.parseInt(attributes.getValue("index"));
			String name = attributes.getValue("key");
			paramIndices.put(index, name);
		}
		if (qName.equals("remainder")) {
			remainderKey = attributes.getValue("key");
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
			action.setName(actionName);

			if(parameterValues.containsKey("minimum")) {
				action.setMinimumParameters(parameterValues.get("minimum"));
			}
			if(parameterValues.containsKey("maximum")) {
				action.setMaximumParameters(parameterValues.get("maximum"));
			}
			
			for (Map.Entry<Integer, String> pair : paramIndices.entrySet())
			{
				action.addParamKeyAtIndex(pair.getKey(), pair.getValue());
			}
			
			action.addRemainderKey(remainderKey);
			
			loadedActions.add(action);
		}
		if(qName.equals("number_of_parameters")) {
			parameterTagFound = false;
		}
	}
	
	public String getMaximumParameters() {
		return parameterValues.get("maximum");
	}

	public String getMinimumParameters() {
		return parameterValues.get("minimum");
	}	
	
	public String getType() {
		return this.type;
	}
	
	public String getImplementation() {
		return implementation;
	}
	
	public String getName() {
		return actionName;
	}
	
	public String getRemainder() {
		return remainderKey;
	}
	
	public String getParamIndices(int index) {
		return paramIndices.get(index);
	}
	
}