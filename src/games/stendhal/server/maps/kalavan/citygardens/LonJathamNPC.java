/* $Id$ */
package games.stendhal.server.maps.kalavan.citygardens;

//import java.util.Arrays;
import java.util.Map;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.StendhalRPZone;
//import games.stendhal.server.entity.npc.ConversationStates;
import games.stendhal.server.entity.npc.SpeakerNPC;
//import games.stendhal.server.entity.npc.condition.NotCondition;
//import games.stendhal.server.entity.npc.condition.PlayerHasItemWithHimCondition;

// Class for NPC Lon Jathan
public class LonJathamNPC implements ZoneConfigurator {

	@Override
	public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
		buildNPC(zone);
	}


    // Build Lon Jatham as a Speaker NPC
	private void buildNPC(final StendhalRPZone zone) {

        // Create a speaker NPC
		final SpeakerNPC npc = new SpeakerNPC("Lon Jatham") {

            // Lon's greetings and responses
			@Override
			public void createDialog() {
				addGreeting("Hi. Do you want to buy my Java book? Maybe take a test?");
				addJob("I'm a Java lecturer! Ask me to take a Java #test!");				
				addHelp("Hm, maybe you'd like to answer some questions in my Java #test?");
				addOffer("I can give XP and a certificate.");
                addQuest("I have a #test for you! Answer some Java questions for me!");
				addReply("study","Buy my book! I update it every year.");	
				addGoodbye("Gooooodbye! Study harder!!!");
			}

		};

        // Set entity class as Sprite for Lon Jatham
		npc.setEntityClass("lonjathamnpc");

        // Set position - chair north of boating lake in Kalavan
		npc.setPosition(118, 5);

        // Set intital HP for Lon
		npc.initHP(100);

        // Description of Lon
		npc.setDescription("I am Lon Jatham! I am an expert on Java. Don't talk to me unless you have your logbook.");

        // Add to the zone
		zone.add(npc);
	}
}
