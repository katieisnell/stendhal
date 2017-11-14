package games.stendhal.server.entity.npc.action;

import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.events.TutorialNotifier;
import games.stendhal.server.entity.item.scroll.BankScroll;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.player.Player;

/**
 * Equips the player with a bank scroll.
 */
public class EquipBankScrollAction implements ChatAction {
	
	private final String bankName;
	private final String itemName = "bank scroll";
	
	/**
	 * Creates a new EquipBankScrollAction.
	 *
	 * @param itemName
	 *            name of item
	 */
	public EquipBankScrollAction(String bankName) {
		this.bankName = bankName;
	}
	
	@Override
	public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {

		BankScroll scroll = (BankScroll) SingletonRepository.getEntityManager().getItem(itemName);
		scroll.setInfo(player, bankName);
		scroll.setQuantity(1);
		player.equipOrPutOnGround(scroll);
		TutorialNotifier.equippedByNPC(player, scroll);
		player.notifyWorldAboutChanges();
	}
}
