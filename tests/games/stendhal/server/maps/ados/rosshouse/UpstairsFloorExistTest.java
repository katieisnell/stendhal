package games.stendhal.server.maps.ados.rosshouse;

import static org.junit.Assert.*;

import java.net.URI;
import java.util.Collection;

import org.junit.BeforeClass;
import org.junit.Test;

import games.stendhal.server.core.config.ZonesXMLLoader;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPWorld;
import games.stendhal.server.core.engine.StendhalRPZone;
import marauroa.common.game.IRPZone;
import utilities.QuestHelper;
import static org.hamcrest.CoreMatchers.is;

public class UpstairsFloorExistTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		QuestHelper.setUpBeforeClass();
		// Load the zones in ados before the test case.
		ZonesXMLLoader zonesInAdos = new ZonesXMLLoader(new URI("/data/conf/zones/ados.xml"));
		zonesInAdos.load();
	}
	
	@Test
	public void shouldHaveUpstairsFloor() {	
		// Create the RPworld that contain all the zones.
		StendhalRPWorld gameWorld = SingletonRepository.getRPWorld();

		// Get the upstairs interior in Ross house from all the zones.
		IRPZone ados_house_upstair = gameWorld.getRPZone("int_ados_ross_house_1"); 
		
		// Check if Ross's upstair floor zone is not null.
		assertNotNull(ados_house_upstair);
		
		// Load all the interior zones in ados that is accessible.
		Collection<StendhalRPZone> ados_interiors_all =  gameWorld.getAllZonesFromRegion("ados", false, null, true);

		// Check if the interior zone in ados contains the upstairs floor in Ross house.
		assertThat(ados_interiors_all.contains(ados_house_upstair), is(true));
	}
}

