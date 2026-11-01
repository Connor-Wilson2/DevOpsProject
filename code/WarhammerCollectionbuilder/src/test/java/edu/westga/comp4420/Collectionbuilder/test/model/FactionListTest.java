package edu.westga.comp4420.Collectionbuilder.test.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.westga.comp4420.Collectionbuilder.model.FactionList;
import edu.westga.comp4420.Collectionbuilder.model.Unit;

class FactionListTest {
	@Test
	void testConstructorAndGetters() {
		LocalDate dateCreated = LocalDate.of(2026, 10, 1);
		FactionList factionList = new FactionList("Strike Force", dateCreated,
				"Space Marines", "Ultramarines");

		assertEquals("Strike Force", factionList.getTitle());
		assertEquals(dateCreated, factionList.getDateCreated());
		assertEquals("Space Marines", factionList.getFaction());
		assertEquals("Ultramarines", factionList.getSubFaction());
		assertTrue(factionList.getUnits().isEmpty());
		assertEquals(0, factionList.getTotalPoints());
	}

	@Test
	void testUnitManagement() {
		FactionList factionList = new FactionList("Strike Force", LocalDate.now(),
				"Space Marines", "Ultramarines");
		Unit unit = new Unit("Intercessors", "Battleline", 100, 5, List.of("Boltgun"));

		factionList.addUnit(unit);
		List<Unit> units = factionList.getUnits();
		assertEquals(1, units.size());
		assertEquals(unit, units.get(0));
		assertEquals(100, factionList.getTotalPoints());
		assertThrows(UnsupportedOperationException.class, () -> units.add(unit));
		assertTrue(factionList.removeUnit(unit));
		assertEquals(0, factionList.getTotalPoints());
		assertFalse(factionList.removeUnit(unit));
		assertThrows(NullPointerException.class, () -> factionList.addUnit(null));
		assertThrows(NullPointerException.class, () -> factionList.removeUnit(null));
	}

	@Test
	void testConstructorRejectsInvalidValues() {
		LocalDate dateCreated = LocalDate.now();

		assertThrows(NullPointerException.class,
				() -> new FactionList(null, dateCreated, "Space Marines", "Ultramarines"));
		assertThrows(IllegalArgumentException.class,
				() -> new FactionList(" ", dateCreated, "Space Marines", "Ultramarines"));
		assertThrows(NullPointerException.class,
				() -> new FactionList("Strike Force", null, "Space Marines", "Ultramarines"));
		assertThrows(NullPointerException.class,
				() -> new FactionList("Strike Force", dateCreated, null, "Ultramarines"));
		assertThrows(IllegalArgumentException.class,
				() -> new FactionList("Strike Force", dateCreated, " ", "Ultramarines"));
		assertThrows(NullPointerException.class,
				() -> new FactionList("Strike Force", dateCreated, "Space Marines", null));
		assertThrows(IllegalArgumentException.class,
				() -> new FactionList("Strike Force", dateCreated, "Space Marines", " "));
	}
}