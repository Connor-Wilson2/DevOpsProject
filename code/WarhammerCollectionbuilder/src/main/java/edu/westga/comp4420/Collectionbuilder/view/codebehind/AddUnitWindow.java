package edu.westga.comp4420.Collectionbuilder.view.codebehind;

import edu.westga.comp4420.Collectionbuilder.model.Unit;
import edu.westga.comp4420.Collectionbuilder.view.CreateUnitUtility;
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
			this.createdUnit = CreateUnitUtility.createUnit(this.nameField.getText(),
					this.unitTypeField.getText(), this.pointValueField.getText(),
					this.modelCountField.getText(), this.wargearOptionsField.getText());
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

	private void closeWindow() {
		this.doneButton.getScene().getWindow().hide();
	}
}