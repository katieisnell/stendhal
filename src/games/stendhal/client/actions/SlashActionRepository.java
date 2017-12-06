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
import games.stendhal.server.core.rule.defaultruleset.DefaultAction;


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
		final SlashAction msg = new MessageAction();
		final SlashAction supporta = new SupportAnswerAction();
		final SlashAction who = new WhoAction();
		final SlashAction help = new HelpAction();

		final GroupMessageAction groupMessage = new GroupMessageAction();
		
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
		
		// now go through all DefaultActions from the table 
		// and update the ones that have parameters or remainder
		// (for now just Teleport and Message)

		actions.put("/", new RemessageAction());
		actions.put("add", new AddBuddyAction());
		actions.put("adminlevel", new AdminLevelAction());
		actions.put("adminnote", new AdminNoteAction());
		actions.put("alter", new AlterAction());
		actions.put("altercreature", new AlterCreatureAction());
		actions.put("alterquest", new AlterQuestAction());
		actions.put("answer", new AnswerAction());
		actions.put("atlas", new AtlasBrowserLaunchCommand());
		actions.put("away", new AwayAction());

		actions.put("ban", new BanAction());

		actions.put("clear", new ClearChatLogAction());
		actions.put("clickmode", new ClickModeAction());
		actions.put("clientinfo", new ClientInfoAction());
		actions.put("commands", help);
		actions.put("config", new ConfigAction());

		actions.put("drop", new DropAction());

		actions.put("cast", new CastSpellAction());

		actions.put("gag", new GagAction());
		actions.put("gmhelp", new GMHelpAction());

		// actions.put("group", new GroupManagementAction(groupMessage));
        DefaultAction groupManagementAction = (DefaultAction) actions.get("group_management");
        groupManagementAction.addParamKeyAtIndex(0, "action");
        groupManagementAction.addRemainderKey("params");
        actions.put("group", groupManagementAction);

		// actions.put("groupmessage", groupMessage);
        DefaultAction groupMessageAction = (DefaultAction) actions.get("group_message");
        groupMessageAction.addRemainderKey("text");
        actions.put("groupmessage", groupMessageAction);

        //actions.put("grumpy", new GrumpyAction());
        DefaultAction grumpyAction = (DefaultAction) actions.get("grumpy");
        grumpyAction.addRemainderKey("reason");
        actions.put("grumpy", grumpyAction);
        
		actions.put("help", help);

		actions.put("ignore", new IgnoreAction());

		//actions.put("inspect", new InspectAction());
	    DefaultAction inspectAction = (DefaultAction) actions.get("inspect");
	    inspectAction.addParamKeyAtIndex(0, "target");
	    actions.put("inspect", inspectAction);
	    
		// actions that don't have any parameters or remainder don't have to be updated in the table
		// actions.put("invisible", new InvisibleAction());
		
		// actions.put("jail", new JailAction());
	    DefaultAction jailAction = (DefaultAction) actions.get("jail");
	    jailAction.addParamKeyAtIndex(0, "target");
	    jailAction.addParamKeyAtIndex(1, "minutes");
	    jailAction.addRemainderKey("reason");
	    actions.put("jail", jailAction);
	    
		// actions.put("listproducers", new ListProducersAction());
        DefaultAction listProducersAction = (DefaultAction) actions.get("listproducers");
        actions.put("listproducers", listProducersAction);

		actions.put("me", new EmoteAction());
		actions.put("msg", msg);
		actions.put("mute", new MuteAction());

		actions.put("names", who);

		actions.put("p", groupMessage);
		actions.put("profile", new ProfileAction());
		
		// actions.put("travellog", new TravelLogAction());
		DefaultAction travelLogAction = (DefaultAction) actions.get("progressstatus");
		actions.put("progressstatus", travelLogAction);

		actions.put("quit", new QuitAction());

		// actions.put("remove", new RemoveBuddyAction());
		DefaultAction removeBuddyAction = (DefaultAction) actions.get("removebuddy");
		removeBuddyAction.addParamKeyAtIndex(0, "target");
		actions.put("removebuddy", removeBuddyAction);

		// actions.put("sentence", new SentenceAction());
		DefaultAction sentenceAction = (DefaultAction) actions.get("sentence");
		sentenceAction.addRemainderKey("value");
		actions.put("sentence", sentenceAction);
		
		actions.put("status", new SentenceAction()); // Alias for /sentence
		actions.put("settings", new SettingsAction());

		actions.put("sound", new SoundAction());
		actions.put("volume", new VolumeAction());
		actions.put("vol", new VolumeAction());

		// actions.put("storemessage", new StoreMessageAction());
		DefaultAction storeMessageAction = (DefaultAction) actions.get("storemessage");
		storeMessageAction.addParamKeyAtIndex(0, "target");
		storeMessageAction.addRemainderKey("text");
		actions.put("storemessage", storeMessageAction);
		
		actions.put("postmessage", new StoreMessageAction());

		actions.put("summonat", new SummonAtAction());
		actions.put("summon", new SummonAction());
		
		// actions.put("supportanswer", supporta);
		DefaultAction supportAnswerAction = (DefaultAction) actions.get("supportanswer");
		supportAnswerAction.addParamKeyAtIndex(0, "target");
		supportAnswerAction.addRemainderKey("text");
		actions.put("supportanswer", supportAnswerAction);
		
		actions.put("supporta", supporta);
		
		// actions.put("support", new SupportAction());
		DefaultAction supportAction = (DefaultAction) actions.get("support");
		supportAction.addRemainderKey("text");
		actions.put("support", supportAction);

		actions.put("takescreenshot", new ScreenshotAction());
		
		//actions.put("teleport", new TeleportAction());
		DefaultAction teleportAction = (DefaultAction) actions.get("teleport");
		teleportAction.addParamKeyAtIndex(0,  "target");
		teleportAction.addParamKeyAtIndex(1,  "zone");
		teleportAction.addParamKeyAtIndex(2,  "x");
		teleportAction.addParamKeyAtIndex(3,  "y");
		actions.put("teleport", teleportAction);
		
		// actions.put("teleportto", new TeleportToAction());
		DefaultAction teleportToAction = (DefaultAction) actions.get("teleportto");
		teleportToAction.addRemainderKey("target");
		actions.put("teleportto", teleportToAction);
		
		// actions.put("tellall", new TellAllAction());
		DefaultAction tellAllAction = (DefaultAction) actions.get("tellall");
		tellAllAction.addRemainderKey("text");
		actions.put("tellall", tellAllAction);
		
		
		// actions.put("tell", msg);
		DefaultAction messageAction = (DefaultAction) actions.get("tell");
		messageAction.addParamKeyAtIndex(0, "target");
		messageAction.addRemainderKey("text");
		actions.put("tell", messageAction);

		// actions.put("where", new WhereAction());
		DefaultAction whereAction = (DefaultAction) actions.get("where");
		whereAction.addRemainderKey("target");
		actions.put("where", whereAction);
		
		actions.put("who", who);
		actions.putAll(BareBonesBrowserLaunchCommandsFactory.createBrowserCommands());
		//actions.put("wrap", new WrapAction());

		actions.put("walk", new AutoWalkAction());
		actions.put("stopwalk", new AutoWalkStopAction());

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
