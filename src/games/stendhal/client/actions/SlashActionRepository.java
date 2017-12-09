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

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.apache.log4j.Logger;

import games.stendhal.client.core.config.ActionGroupsXMLLoader;
import games.stendhal.client.core.rule.defaultruleset.DefaultAction;


/**
 * Manages Slash Action Objects.
 */
public class SlashActionRepository {
	
	/** the logger instance. */
	private static final Logger LOGGER = Logger.getLogger(SlashActionRepository.class);

	/** Set of client supported Actions. */
	private static HashMap<String, SlashAction> actions = new HashMap<String, SlashAction>();

	/**
	 * Registers the available Action.
	 */
	public static void register() {

		final SlashAction help = new HelpAction();
		
		// build table of DefaultActions
		try {
			final ActionGroupsXMLLoader loader = new ActionGroupsXMLLoader(new URI("/data/conf/actions.xml"));
			List<DefaultAction> loadedDefaultActions = loader.load();
			for (DefaultAction defaultAction : loadedDefaultActions) {
				// check if action is already in the table
				if(actions.containsKey(defaultAction.getType())) {
					LOGGER.warn("Repeated action name: " + defaultAction.getType());
				}
				// add the current action to the table
				actions.put(defaultAction.getType(), defaultAction);
			}
		} catch (Exception e) {
			LOGGER.error("actions.xml could not be loaded", e);
		}

		actions.put("/", new RemessageAction());
		actions.put("add", new AddBuddyAction());
		actions.put("adminlevel", new AdminLevelAction());
		actions.put("alter", new AlterAction());
		actions.put("altercreature", new AlterCreatureAction());
		actions.put("alterquest", new AlterQuestAction());
		actions.put("answer", new AnswerAction());
		actions.put("atlas", new AtlasBrowserLaunchCommand());
		actions.put("clear", new ClearChatLogAction());
		actions.put("clickmode", new ClickModeAction());
		actions.put("clientinfo", new ClientInfoAction());
		actions.put("commands", help);
		actions.put("config", new ConfigAction());
		actions.put("drop", new DropAction());
		actions.put("cast", new CastSpellAction());
		actions.put("gmhelp", new GMHelpAction());  
		actions.put("help", help);
		actions.put("ignore", new IgnoreAction());
		actions.put("me", actions.get("emote"));
		actions.put("msg", actions.get("tell"));
		actions.put("mute", new MuteAction());
		actions.put("names", actions.get("who"));
		actions.put("p", actions.get("group_message"));
		actions.put("profile", new ProfileAction());
		actions.put("progressstatus", actions.get("progresssstatus"));
		actions.put("quit", new QuitAction());
		actions.put("status", actions.get("sentence")); // Alias for /sentence
		actions.put("settings", new SettingsAction());
		actions.put("sound", new SoundAction());
		actions.put("volume", new VolumeAction());
		actions.put("vol", new VolumeAction());	
		actions.put("postmessage", actions.get("storemessage"));
		actions.put("summonat", new SummonAtAction());
		actions.put("summon", new SummonAction());		
		actions.put("supporta", actions.get("supportanswer"));
		actions.put("takescreenshot", new ScreenshotAction());
		actions.putAll(BareBonesBrowserLaunchCommandsFactory.createBrowserCommands());
		actions.put("stopwalk", actions.get("walk"));

		// PvP challenge actions
		actions.put("challenge", new CreateChallengeAction());		
		actions.put("accept", new AcceptChallengeAction());
	}

	/**
	 * Gets the Action object for the specified Action name.
	 *
	 * @param name
	 *            name of Action
	 * @return Action object
	 */
	public static SlashAction get(String name) {
		String temp = name.toLowerCase(Locale.ENGLISH);
		return actions.get(temp);
	}

	/**
	 * Get all known command names.
	 *
	 * @return set of commands
	 */
	public static Set<String> getCommandNames() {
		return actions.keySet();
	}
}
