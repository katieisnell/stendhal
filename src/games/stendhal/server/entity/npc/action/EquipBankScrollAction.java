package games.stendhal.server.entity.npc.action;

import games.stendhal.common.parser.Sentence;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.events.TutorialNotifier;
import games.stendhal.server.entity.item.scroll.BankScroll;
import games.stendhal.server.entity.npc.ChatAction;
import games.stendhal.server.entity.npc.EventRaiser;
import games.stendhal.server.entity.player.Player;

/* Equips the player with a BankScroll
 * 
 * author: Diana Pislaru
 * 
 * */
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
	
	/* Equips the player with a new BankScroll */
	@Override
	public void fire(final Player player, final Sentence sentence, final EventRaiser npc) {

		// Create a BankScroll
		BankScroll scroll = (BankScroll) SingletonRepository.getEntityManager().getItem(itemName);
		
		// Link the BankScroll to the correct bank
		scroll.setInfo(player, bankName);
		scroll.setQuantity(1);
		
		// Equip the player with the BankScroll and notify the game
		player.equipOrPutOnGround(scroll);
		TutorialNotifier.equippedByNPC(player, scroll);
		player.notifyWorldAboutChanges();
	}
}
