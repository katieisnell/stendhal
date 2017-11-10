/* $Id$ */
// Quest where Lon Jatham asks Java questions as a quest and
// gives XP and a certificate for answering them correctly.

package games.stendhal.server.maps.quests;

import java.util.List;
import games.stendhal.server.entity.player.Player;
import games.stendhal.server.entity.npc.SpeakerNPC;


public class JavaQuiz extends AbstractQuest {
    

	@Override
	public String getSlotName()
	{
		return "";
	}
	
	@Override
	public List<String> getHistory(final Player player)
	{
		return null;
	}

	private void createCertificate()
	{
	}

	@Override
	public void addToWorld()
	{
	}
	
	@Override
	public String getName()
	{
		return "";
	}

	@Override
	public String getRegion() {
		return "";
	}
	@Override
	
	public String getNPCName() {
		return "";
	}
}