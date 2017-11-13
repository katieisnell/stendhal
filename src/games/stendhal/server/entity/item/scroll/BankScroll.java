package games.stendhal.server.entity.item.scroll;

import java.util.Iterator;
import java.util.Map;

import games.stendhal.server.entity.item.Item;
import games.stendhal.server.entity.player.Player;
import marauroa.common.game.RPObject;

public class BankScroll extends PersistentScroll{
	
	private String playerName = null;
	private String bankName;
	private String bankNameMapping;

	public BankScroll(final String name, final String clazz, final String subclass,
			final Map<String, String> attributes) {
		super(name, clazz, subclass, attributes);
	}

	public BankScroll(final BankScroll item) {
		super(item);
	}
	
	@Override
	public boolean useScroll(final Player player) {
		String[] info = getInfoString().split(" ");
		playerName = info[0];
		bankName = info[1];
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
		if (player.getName().equals(playerName)){
			Iterator<RPObject> iterator = player.getSlot(bankName).iterator();
			String contents;
			if (iterator.hasNext())
			{
				Item item = (Item)iterator.next();
				contents = item.getQuantity() + " " + item.getName();
				
				while (iterator.hasNext())
				{
					item = (Item)iterator.next();
					contents += ", " + item.getQuantity() + " " + item.getName();
				}
			}
			else
			{
				contents = "nothing";
			}
			player.sendPrivateText(bankNameMapping + " contains " + contents);
			return true;
		}
		
		
		
		return false;
	}
	
	public void setInfo(Player player, String bankName)
	{
		setInfoString(player.getName() + " " + bankName);
	}
}
