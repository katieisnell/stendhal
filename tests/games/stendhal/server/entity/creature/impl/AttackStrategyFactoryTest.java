package games.stendhal.server.entity.creature.impl;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AttackStrategyFactoryTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Tests for getArcher.
	 */
	@Test
	public void testGetArcher() {
		final Map<String, String> profiles = new HashMap<String, String>();
		assertTrue(AttackStrategyFactory.get(profiles) instanceof HandToHand);
		profiles.put("archer", null);
		assertTrue(AttackStrategyFactory.get(profiles) instanceof RangeAttack);

	}

	/**
	 * Tests for getGandhi.
	 */
	@Test
	public void testGetGandhi() {
		final Map<String, String> profiles = new HashMap<String, String>();
		assertTrue(AttackStrategyFactory.get(profiles) instanceof HandToHand);
		profiles.put("gandhi", null);
		assertTrue(AttackStrategyFactory.get(profiles) instanceof Gandhi);

	}

	/**
	 * Tests for getCoward.
	 */
	@Test
	public void testGetCoward() {
		final Map<String, String> profiles = new HashMap<String, String>();
		assertTrue(AttackStrategyFactory.get(profiles) instanceof HandToHand);
		profiles.put("coward", null);
		assertTrue(AttackStrategyFactory.get(profiles) instanceof Coward);

	}

	/**
	 * Tests for getStupidCoward.
	 */
	@Test
	public void testGetStupidCoward() {
		final Map<String, String> profiles = new HashMap<String, String>();
		assertTrue(AttackStrategyFactory.get(profiles) instanceof HandToHand);
		profiles.put("stupid coward", null);
		assertTrue(AttackStrategyFactory.get(profiles) instanceof StupidCoward);

	}

	/**
	 * Tests for getting AttackWeakest profile.
	 */
	@Test
	public void testGetAttackWeakest() {
		final Map<String, String> profiles = new HashMap<String, String>();
		assertTrue(AttackStrategyFactory.get(profiles) instanceof HandToHand);
		profiles.put("attack weakest", null);
		assertTrue(AttackStrategyFactory.get(profiles) instanceof AttackWeakest);
	}
}
