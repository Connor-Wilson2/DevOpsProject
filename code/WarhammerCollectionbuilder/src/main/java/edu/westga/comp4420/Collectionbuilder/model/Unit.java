package edu.westga.comp4420.Collectionbuilder.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a unit that can be added to a Warhammer collection.
 *
 * @author Comp 4420
 */
public class Unit {
	private final String name;
	private final String unitType;
	private final int pointValue;
	private final int modelCount;
	private final List<String> wargearOptions;

	/**
	 * Creates a unit with its name, type, points, model count, and wargear options.
	 *
	 * @param name the unit's name
	 * @param unitType the unit's type
	 * @param pointValue the unit's point value
	 * @param modelCount the number of models in the unit
	 * @param wargearOptions the wargear options available to the unit
	 * @throws IllegalArgumentException if a text value is blank, pointValue is negative,
	 *         or modelCount is less than one
	 * @throws NullPointerException if wargearOptions or any required text value is null
	 */
	public Unit(String name, String unitType, int pointValue, int modelCount,
			List<String> wargearOptions) {
		this.name = requireText(name, "name");
		this.unitType = requireText(unitType, "unitType");
		if (pointValue < 0) {
			throw new IllegalArgumentException("pointValue must not be negative");
		}
		if (modelCount < 1) {
			throw new IllegalArgumentException("modelCount must be at least one");
		}
		this.pointValue = pointValue;
		this.modelCount = modelCount;
		this.wargearOptions = new ArrayList<>();
		for (String option : Objects.requireNonNull(wargearOptions, "wargearOptions")) {
			this.wargearOptions.add(requireText(option, "wargear option"));
		}
	}

	/**
	 * Gets the unit's name.
	 *
	 * @return the unit's name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the unit's type.
	 *
	 * @return the unit's type
	 */
	public String getUnitType() {
		return this.unitType;
	}

	/**
	 * Gets the unit's point value.
	 *
	 * @return the unit's point value
	 */
	public int getPointValue() {
		return this.pointValue;
	}

	/**
	 * Gets the number of models in the unit.
	 *
	 * @return the number of models in the unit
	 */
	public int getModelCount() {
		return this.modelCount;
	}

	/**
	 * Gets the unit's wargear options.
	 *
	 * @return an unmodifiable list of the unit's wargear options
	 */
	public List<String> getWargearOptions() {
		return Collections.unmodifiableList(this.wargearOptions);
	}

	private static String requireText(String value, String fieldName) {
		Objects.requireNonNull(value, fieldName);
		if (value.trim().isEmpty()) {
			throw new IllegalArgumentException(fieldName + " must not be blank");
		}
		return value;
	}
}
