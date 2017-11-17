package games.stendhal.server.maps.ados.rosshouse;

import java.util.Map;

import games.stendhal.server.core.config.ZoneConfigurator;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.mapstuff.chest.Chest;

/**
 * Creates a chest with teddy and dice at coordinate (2,7) in Ross house upstairs.
 *
 * @author Khoi Nguyen (mbaxamn2)
 * 
 */
public class ItemsInChest implements ZoneConfigurator {
    @Override
    public void configureZone(final StendhalRPZone zone, final Map<String, String> attributes) {
        makeChestWithItems(zone);
    }

    private void makeChestWithItems(final StendhalRPZone zone) {
      Chest newChest = new Chest();
		
      // Add a teddy, a dice and an apple to the chest.
      newChest.add(SingletonRepository.getEntityManager().getItem("teddy"));
      newChest.add(SingletonRepository.getEntityManager().getItem("dice"));
      newChest.add(SingletonRepository.getEntityManager().getItem("apple"));

      // Set chest position at 2,7 and add it to the zone.
      newChest.setPosition(2, 7);
      zone.add(newChest);	
    }
}
