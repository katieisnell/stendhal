// Test to see if answer to question "Waves are caused by ...?" from
// "ScubaLicenseQuiz.java" is given in the "Diver's Handbook" in the
// ados library where it should be located.

package games.stendhal.server.maps.quests;


import games.stendhal.server.core.config.ZonesXMLLoader;
import games.stendhal.server.core.engine.SingletonRepository;
import games.stendhal.server.core.engine.StendhalRPZone;
import games.stendhal.server.entity.mapstuff.sign.Sign;
import java.net.URI;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

import org.junit.Test;


public class ScubaLicenseQuizTest {

	@Test
	public void testBookContainsAnswers() {
		
		// Set the book to test up as a sign.
		Sign diverBook = (Sign)zone.getEntityAt(28,4);
		
		// Test that the keywords "wind" and "wave" are in the book.
		assertTrue(diverBook.getText().contains("Waves are caused by wind."));
	} // testBookContainsAnswer
	
	// Our zone to work within.
	public static StendhalRPZone zone;
	
	@BeforeClass
	public static void setUp() throws Exception {
		// Create an ados zone.
		ZonesXMLLoader adosZone = new ZonesXMLLoader(new URI("/build/build_server_xmlconf/data/conf/zones/ados.xml"));

		// Load the zone.
		adosZone.load();
		
		// Set that we specifically want to be in the library within our ados zone.
		zone = SingletonRepository.getRPWorld().getZone("int_ados_library");
	} // setUp

} // ScubaLicenseQuiz
