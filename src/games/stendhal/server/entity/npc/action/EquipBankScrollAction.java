package games.stendhal.server.entity.npc.action;

import games.stendhal.common.parser.Sentence;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.player.Player;

/**
 * Equips the player with a bank scroll.
 */
public class EquipBankScrollAction implements ChatAction {
	
	/**
	 * Creates a new EquipBankScrollAction.
	 *
	 * @param bankName
	 *            name of bank
	 */
	public EquipBankScrollAction(String bankName) {
		
	}
	
	@Override
	public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {

	}
}
