package games.stendhal.server.entity.item.scroll;

import java.util.Iterator;
import java.util.Map;

import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.player.Player;
import marauroa.common.game.RPObject;

/**
 * Represents a scroll that returns the contents of an associated bank chest
 * as a private message when used
 * @author mbaxatr2
 */
public class BankScroll extends PersistentScroll{
	
	// Associated values for a bank scroll
	private String playerName = null;
	private String bankName;
	private String bankNameMapping;

	/**
	 * Creates a new scroll.
	 *
	 * @param name
	 * @param clazz
	 * @param subclass
	 * @param attributes
	 */
	public BankScroll(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
	}

	/**
	 * Copy constructor.
	 *
	 * @param item
	 *            item to copy
	 */
	public BankScroll(final BankScroll item) {
		super(item);
	}
	
	/**
	 * Use a scroll.
	 *
	 * @param player
	 *            The player using the scroll.
	 *
	 * @return <code>true</code> if successful, <code>false</code>
	 *         otherwise.
	 */
	@Override
	public boolean useScroll(final Player player) {
		//Split the info string to get the player name and the bank name
		String[] info = getInfoString().split(" ");
		playerName = info[0];
		bankName = info[1];
		
		// Map the bank name to a player friendly form
		switch (bankName)
		{
		case "bank":
			bankNameMapping = "Semos bank chest"; break;
		case "bank_ados":
			bankNameMapping = "Ados bank chest"; break;
		case "bank_fado":
			bankNameMapping = "Fado bank chest"; break;
		case "bank_nalwor":
			bankNameMapping = "Nalwor bank chest"; break;
		case "zaras_chest_ados":
			bankNameMapping = "Zara's chest"; break;
		default:
			bankNameMapping = "Semos bank chest"; break;
		}
		
		// If the player using the scroll is the owner
		if (player.getName().equals(playerName)){
			// Create an iterator for the bank chest and a string that will store the
			// bank statement
			Iterator<RPObject> iterator = player.getSlot(bankName).iterator();
			String contents;
			
			// If there is an item in the chest
			if (iterator.hasNext())
			{
				// Add it to the bank statement with its quantity
				Item item = (Item)iterator.next();
				contents = item.getQuantity() + " " + item.getName();
				
				// While there are more items in the chest
				while (iterator.hasNext())
				{
					// Add them to the bank statement
					item = (Item)iterator.next();
					contents += ", " + item.getQuantity() + " " + item.getName();
				}
			}
			// otherwise make the bank statement say the chest contains nothing
			else
			{
				contents = "nothing";
			}
			// Return the bank statement message to the player
			player.sendPrivateText(bankNameMapping + " contains " + contents);
			return true;
		}
		// If the player is not the owner
		else if (player.getName() != null)
		{
			// Remove the scroll
			splitOff(1);
			
			// Inform the player that they cannot use bank scrolls not belonging to them
			player.sendPrivateText("You cannot view another player's bank statement");	
			player.notifyWorldAboutChanges();
			return true;
		}
		return false;
		
		
	}
	
	/**
	 * Method to be used for setting the info string of a bank scroll
	 * by concatenating the player name and the associated bank
	 * @param player
	 * @param bankName
	 */
	public void setInfo(Player player, String bankName)
	{
		setInfoString(player.getName() + " " + bankName);
	}
}
