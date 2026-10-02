package edu.westga.comp4420.Collectionbuilder.view.codebehind;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import edu.westga.comp4420.Collectionbuilder.model.Unit;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 * Handles entry of a new unit in its own window.
 *
 * @author Comp 4420
 */
public class AddUnitWindow {
	@FXML private TextField nameField;
	@FXML private TextField unitTypeField;
	@FXML private TextField pointValueField;
	@FXML private TextField modelCountField;
	@FXML private TextField wargearOptionsField;
	@FXML private Button doneButton;
	private Unit createdUnit;

	@FXML
	void initialize() {
		assert this.nameField != null : "fx:id=\"nameField\" was not injected.";
		assert this.unitTypeField != null : "fx:id=\"unitTypeField\" was not injected.";
		assert this.pointValueField != null : "fx:id=\"pointValueField\" was not injected.";
		assert this.modelCountField != null : "fx:id=\"modelCountField\" was not injected.";
		assert this.wargearOptionsField != null : "fx:id=\"wargearOptionsField\" was not injected.";
		assert this.doneButton != null : "fx:id=\"doneButton\" was not injected.";
	}

	/**
	 * Gets the unit created by the user, or null if the window was cancelled.
	 *
	 * @return the created unit or null
	 */
	public Unit getCreatedUnit() {
		return this.createdUnit;
	}

	@FXML
	void done() {
		try {
			this.createdUnit = this.createUnit();
			this.closeWindow();
		} catch (NumberFormatException error) {
			new Alert(Alert.AlertType.ERROR, "Point value and model count must be whole numbers.")
					.showAndWait();
		} catch (IllegalArgumentException error) {
			new Alert(Alert.AlertType.ERROR, error.getMessage()).showAndWait();
		}
	}

	@FXML
	void cancel() {
		this.createdUnit = null;
		this.closeWindow();
	}

	private Unit createUnit() {
		int pointValue = Integer.parseInt(this.pointValueField.getText().trim());
		int modelCount = Integer.parseInt(this.modelCountField.getText().trim());
		return new Unit(this.nameField.getText(), this.unitTypeField.getText(), pointValue,
				modelCount, this.parseWargearOptions(this.wargearOptionsField.getText()));
	}

	private List<String> parseWargearOptions(String text) {
		if (text.trim().isEmpty()) {
			return Arrays.asList();
		}
		return Arrays.stream(text.split(","))
				.map(String::trim)
				.filter(option -> !option.isEmpty())
				.collect(Collectors.toList());
	}

	private void closeWindow() {
		this.doneButton.getScene().getWindow().hide();
	}
}