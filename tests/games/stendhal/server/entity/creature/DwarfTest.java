package games.stendhal.server.entity.creature;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import games.stendhal.server.core.config.CreatureGroupsXMLLoader;
//import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.rule.defaultruleset.DefaultCreature;

public class DwarfTest {

	@Test
	public void testSpeed() {
		CreatureGroupsXMLLoader loader = new CreatureGroupsXMLLoader("/data/conf/creatures.xml");
		List<DefaultCreature> creatures = loader.load();
		DefaultCreature dwarf = null;
		DefaultCreature elderDwarf = null;
		for(DefaultCreature creature : creatures) {
			if (creature.getCreatureName().equals("dwarf")) {
				dwarf = creature;
			}
			if (creature.getCreatureName().equals("elder dwarf")) {
				elderDwarf = creature;
			}
		}
		
		assertNotNull(dwarf);
		assertNotNull(elderDwarf);
		assertTrue("Dwarf is faster than the elder dwarf", dwarf.getSpeed() > elderDwarf.getSpeed()); 
		
	}

}
