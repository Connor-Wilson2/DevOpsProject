package edu.westga.comp4420.Collectionbuilder.test.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.jupiter.api.Test;

import edu.westga.comp4420.Collectionbuilder.model.Unit;
import edu.westga.comp4420.Collectionbuilder.view.CreateUnitUtility;

public class CreateUnitUtilityTest {

	@Test
	void testCreateUnitParsesEnteredValues() {
		Unit unit = CreateUnitUtility.createUnit("Intercessors", "Battleline", "100", "5",
				" Boltgun, Grenade, , ");

		assertEquals("Intercessors", unit.getName());
		assertEquals("Battleline", unit.getUnitType());
		assertEquals(100, unit.getPointValue());
		assertEquals(5, unit.getModelCount());
		assertEquals(Arrays.asList("Boltgun", "Grenade"), unit.getWargearOptions());
	}

	@Test
	void testCreateUnitAllowsEmptyWargearOptions() {
		Unit unit = CreateUnitUtility.createUnit("Intercessors", "Battleline", "100", "5", " ");

		assertEquals(Arrays.asList(), unit.getWargearOptions());
	}

	@Test
	void testCreateUnitRejectsNonNumericValues() {
		assertThrows(NumberFormatException.class,
				() -> CreateUnitUtility.createUnit("Intercessors", "Battleline", "one", "5", ""));
		assertThrows(NumberFormatException.class,
				() -> CreateUnitUtility.createUnit("Intercessors", "Battleline", "100", "five", ""));
	}
}
