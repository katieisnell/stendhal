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
package games.stendhal.server.maps.fado.bank;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.core.pathfinder.FixedPath;
import games.stendhal.server.core.pathfinder.Node;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
import games.stendhal.server.entity.npc.action.DropItemAction;
import games.stendhal.server.entity.npc.action.EquipBankScrollAction;
import games.stendhal.server.entity.npc.action.MultipleActions;
import games.stendhal.server.entity.npc.condition.NotCondition;
import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;

/**
 * Builds the bank teller NPC.
 *
 * @author timothyb89
 */
public class TellerNPC implements ZoneConfigurator {
	//
	// ZoneConfigurator
	//

	/**
	 * Configure a zone.
	 *
	 * @param	zone		The zone to be configured.
	 * @param	attributes	Configuration attributes.
	 */
	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}

	//
	// IL0_TellerNPC
	//

	private void buildNPC(final StendhalRPZone zone) {
		final SpeakerNPC bankNPC = new SpeakerNPC("Yance") {

			@Override
			protected void createPath() {
				final List<Node> nodes = new LinkedList<Node>();
				nodes.add(new Node(15, 3));
				nodes.add(new Node(15, 16));
				setPath(new FixedPath(nodes, true));
			}

			@Override
			protected void createDialog() {
				addGreeting("Welcome to the Fado Bank! Do you need #help?");
				addJob("I am the manager for the bank.");
				addHelp("Just to the left, you can see a few chests. Open one and you can store your belongings in it.");
				addGoodbye("Have a nice day.");
				
				final List<ChatAction> mark_bank_scroll = new LinkedList<ChatAction>();
 				mark_bank_scroll.add(new DropItemAction("empty scroll"));
 				mark_bank_scroll.add(new EquipBankScrollAction("bank_fado"));

 				add(ConversationStates.ATTENDING, 
 					"mark", 
 					new PlayerHasItemWithHimCondition("empty scroll"),
 					ConversationStates.ATTENDING, 
 					"Here you go!", 
 					new MultipleActions(mark_bank_scroll));
 				
 				add(ConversationStates.ATTENDING, 
 	 				"mark", 
 	 				new NotCondition(new PlayerHasItemWithHimCondition("empty scroll")),
 	 				ConversationStates.ATTENDING, 
 	 				"You need an empty scroll so I can mark it!", 
 	 				null);
			}
		};

		bankNPC.setEntityClass("noimagenpc"); /* youngnpc */
		bankNPC.setPosition(15, 3);
		bankNPC.initHP(1000);
		bankNPC.setDescription("Yance is the Fado bank manager. He can give advice on how to use the chests.");
		zone.add(bankNPC);
	}
}
