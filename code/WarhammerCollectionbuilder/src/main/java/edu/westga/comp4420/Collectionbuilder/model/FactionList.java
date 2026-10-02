package edu.westga.comp4420.Collectionbuilder.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a titled army list for a faction and subfaction.
 *
 * @author Comp 4420
 */
public class FactionList {
	private final String title;
	private final LocalDate dateCreated;
	private final String faction;
	private final String subFaction;
	private final List<Unit> units;

	/**
	 * Creates an empty faction list.
	 *
	 * @param title the list's title
	 * @param dateCreated the date the list was created
	 * @param faction the list's faction
	 * @param subFaction the list's subfaction
	 * @throws IllegalArgumentException if a text value is blank
	 * @throws NullPointerException if a required value is null
	 */
	public FactionList(String title, LocalDate dateCreated, String faction, String subFaction) {
		this.title = requireText(title, "title");
		this.dateCreated = Objects.requireNonNull(dateCreated, "dateCreated");
		this.faction = requireText(faction, "faction");
		this.subFaction = requireText(subFaction, "subFaction");
		this.units = new ArrayList<>();
	}

	/**
	 * Gets the list's title.
	 *
	 * @return the list's title
	 */
	public String getTitle() {
		return this.title;
	}

	/**
	 * Gets the list's creation date.
	 *
	 * @return the list's creation date
	 */
	public LocalDate getDateCreated() {
		return this.dateCreated;
	}

	/**
	 * Gets the list's faction.
	 *
	 * @return the list's faction
	 */
	public String getFaction() {
		return this.faction;
	}

	/**
	 * Gets the list's subfaction.
	 *
	 * @return the list's subfaction
	 */
	public String getSubFaction() {
		return this.subFaction;
	}

	/**
	 * Gets the units in this list.
	 *
	 * @return an unmodifiable copy of the units
	 */
	public List<Unit> getUnits() {
		return Collections.unmodifiableList(new ArrayList<>(this.units));
	}

	/**
	 * Adds a unit to this list.
	 *
	 * @param unit the unit to add
	 * @throws NullPointerException if unit is null
	 */
	public void addUnit(Unit unit) {
		this.units.add(Objects.requireNonNull(unit, "unit"));
	}

	/**
	 * Removes a unit from this list.
	 *
	 * @param unit the unit to remove
	 * @return true if the unit was in the list
	 * @throws NullPointerException if unit is null
	 */
	public boolean removeUnit(Unit unit) {
		return this.units.remove(Objects.requireNonNull(unit, "unit"));
	}

	/**
	 * Gets the combined point value of all units in this list.
	 *
	 * @return the total points for this list
	 */
	public int getTotalPoints() {
		return this.units.stream().mapToInt(Unit::getPointValue).sum();
	}

	private static String requireText(String value, String fieldName) {
		Objects.requireNonNull(value, fieldName);
		if (value.trim().isEmpty()) {
			throw new IllegalArgumentException(fieldName + " must not be blank");
		}
		return value;
	}
}
