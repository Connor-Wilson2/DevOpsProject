package edu.westga.comp4420.Collectionbuilder.view;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import edu.westga.comp4420.Collectionbuilder.model.Unit;

public class CreateUnitUtility {

	/**
	 * Creates a unit from values entered in the add-unit window.
	 *
	 * @param name the unit's name
	 * @param unitType the unit's type
	 * @param pointValueText the unit's point value
	 * @param modelCountText the number of models in the unit
	 * @param wargearOptionsText comma-separated wargear options
	 * @return the created unit
	 * @throws NumberFormatException if either numeric value is not a whole number
	 * @throws IllegalArgumentException if a value is invalid for a unit
	 */
	public static Unit createUnit(String name, String unitType, String pointValueText,
			String modelCountText, String wargearOptionsText) {
		int pointValue = Integer.parseInt(pointValueText.trim());
		int modelCount = Integer.parseInt(modelCountText.trim());
		return new Unit(name, unitType, pointValue, modelCount,
				parseWargearOptions(wargearOptionsText));
	}

	private static List<String> parseWargearOptions(String text) {
		if (text.trim().isEmpty()) {
			return Arrays.asList();
		}
		return Arrays.stream(text.split(","))
				.map(String::trim)
				.filter(option -> !option.isEmpty())
				.collect(Collectors.toList());
	}
}
