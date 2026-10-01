package edu.westga.comp4420.Collectionbuilder.test.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.westga.comp4420.Collectionbuilder.model.Unit;

class UnitTest {
	@Test
	void testConstructorAndGetters() {
		List<String> wargearOptions = new ArrayList<>(Arrays.asList("Boltgun", "Grenade"));
		Unit unit = new Unit("Intercessors", "Battleline", 100, 5, wargearOptions);
		wargearOptions.add("Shield");

		assertEquals("Intercessors", unit.getName());
		assertEquals("Battleline", unit.getUnitType());
		assertEquals(100, unit.getPointValue());
		assertEquals(5, unit.getModelCount());
		assertEquals(Arrays.asList("Boltgun", "Grenade"), unit.getWargearOptions());
		assertThrows(UnsupportedOperationException.class,
				() -> unit.getWargearOptions().add("Shield"));
	}

	@Test
	void testConstructorRejectsInvalidValues() {
		List<String> validOptions = Arrays.asList("Boltgun");

		assertThrows(NullPointerException.class,
				() -> new Unit(null, "Battleline", 100, 5, validOptions));
		assertThrows(IllegalArgumentException.class,
				() -> new Unit(" ", "Battleline", 100, 5, validOptions));
		assertThrows(NullPointerException.class,
				() -> new Unit("Intercessors", null, 100, 5, validOptions));
		assertThrows(IllegalArgumentException.class,
				() -> new Unit("Intercessors", " ", 100, 5, validOptions));
		assertThrows(IllegalArgumentException.class,
				() -> new Unit("Intercessors", "Battleline", -1, 5, validOptions));
		assertThrows(IllegalArgumentException.class,
				() -> new Unit("Intercessors", "Battleline", 100, 0, validOptions));
		assertThrows(NullPointerException.class,
				() -> new Unit("Intercessors", "Battleline", 100, 5, null));
		assertThrows(NullPointerException.class,
				() -> new Unit("Intercessors", "Battleline", 100, 5, Arrays.asList((String) null)));
		assertThrows(IllegalArgumentException.class,
				() -> new Unit("Intercessors", "Battleline", 100, 5, Arrays.asList(" ")));
	}
}