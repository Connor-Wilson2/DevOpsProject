package edu.westga.comp4420.Collectionbuilder.view.codebehind;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import edu.westga.comp4420.Collectionbuilder.model.FactionList;
import edu.westga.comp4420.Collectionbuilder.model.Unit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

/**
 * Displays and edits the units in a faction list.
 *
 * @author Comp 4420
 */
public class FactionListWindow {
	@FXML private Label listTitle;
	@FXML private Label listDetails;
	@FXML private Label pointsTotal;
	@FXML private ListView<Unit> units;
	@FXML private Button deleteUnitButton;
	private FactionList factionList;
	private final ObservableList<Unit> displayedUnits = FXCollections.observableArrayList();

	@FXML
	void initialize() {
		assert this.listTitle != null : "fx:id=\"listTitle\" was not injected.";
		assert this.listDetails != null : "fx:id=\"listDetails\" was not injected.";
		assert this.pointsTotal != null : "fx:id=\"pointsTotal\" was not injected.";
		assert this.units != null : "fx:id=\"units\" was not injected.";
		assert this.deleteUnitButton != null : "fx:id=\"deleteUnitButton\" was not injected.";
		this.units.setItems(this.displayedUnits);
		this.units.setCellFactory(listView -> new ListCell<>() {
			@Override
			protected void updateItem(Unit unit, boolean empty) {
				super.updateItem(unit, empty);
				if (empty || unit == null) {
					this.setText(null);
				} else {
					this.setText(unit.getName() + "  |  " + unit.getUnitType() + "  |  "
							+ unit.getModelCount() + " models  |  " + unit.getPointValue() + " pts"
							+ this.formatWargear(unit.getWargearOptions()));
				}
			}

			private String formatWargear(List<String> wargearOptions) {
				if (wargearOptions.isEmpty()) {
					return "";
				}
				return "  |  " + String.join(", ", wargearOptions);
			}
		});
		this.deleteUnitButton.disableProperty().bind(
				this.units.getSelectionModel().selectedItemProperty().isNull());
	}

	/**
	 * Sets the faction list displayed in this window.
	 *
	 * @param factionList the list to display
	 */
	public void setFactionList(FactionList factionList) {
		this.factionList = Objects.requireNonNull(factionList, "factionList");
		this.listTitle.setText(factionList.getTitle());
		this.listDetails.setText(factionList.getFaction() + " / " + factionList.getSubFaction()
				+ "  |  Created " + factionList.getDateCreated());
		this.refreshUnits();
	}

	@FXML
	void addUnit() {
		Optional<Unit> result = this.createUnitDialog().showAndWait();
		result.ifPresent(unit -> {
			this.factionList.addUnit(unit);
			this.refreshUnits();
		});
	}

	@FXML
	void deleteUnit() {
		Unit selectedUnit = this.units.getSelectionModel().getSelectedItem();
		if (selectedUnit != null) {
			this.factionList.removeUnit(selectedUnit);
			this.refreshUnits();
		}
	}

	private void refreshUnits() {
		this.displayedUnits.setAll(this.factionList.getUnits());
		this.pointsTotal.setText("Total: " + this.factionList.getTotalPoints() + " points");
	}

	private Dialog<Unit> createUnitDialog() {
		Dialog<Unit> dialog = new Dialog<>();
		dialog.setTitle("Add Unit");
		dialog.setHeaderText("Enter the unit details");
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		GridPane form = this.createUnitForm();
		dialog.getDialogPane().setContent(form);
		dialog.setResultConverter(button -> this.createUnitResult(button, form));
		return dialog;
	}

	private GridPane createUnitForm() {
		GridPane form = new GridPane();
		form.setHgap(10);
		form.setVgap(10);
		this.addTextField(form, "Name", 0);
		this.addTextField(form, "Unit type", 1);
		this.addTextField(form, "Point value", 2);
		this.addTextField(form, "Model count", 3);
		this.addTextField(form, "Wargear options", 4);
		return form;
	}

	private void addTextField(GridPane form, String labelText, int row) {
		form.add(new Label(labelText), 0, row);
		form.add(new TextField(), 1, row);
	}

	private Unit createUnitResult(ButtonType button, GridPane form) {
		if (button != ButtonType.OK) {
			return null;
		}
		try {
			return this.buildUnit(form);
		} catch (NumberFormatException error) {
			new Alert(Alert.AlertType.ERROR, "Point value and model count must be whole numbers.")
					.showAndWait();
			return null;
		} catch (IllegalArgumentException error) {
			new Alert(Alert.AlertType.ERROR, error.getMessage()).showAndWait();
			return null;
		}
	}

	private Unit buildUnit(GridPane form) {
		String name = this.getFieldText(form, 0);
		String unitType = this.getFieldText(form, 1);
		int pointValue = Integer.parseInt(this.getFieldText(form, 2).trim());
		int modelCount = Integer.parseInt(this.getFieldText(form, 3).trim());
		List<String> wargearOptions = this.parseWargearOptions(this.getFieldText(form, 4));
		return new Unit(name, unitType, pointValue, modelCount, wargearOptions);
	}

	private List<String> parseWargearOptions(String text) {
		if (text.trim().isEmpty()) {
			return new ArrayList<>();
		}
		return Arrays.stream(text.split(","))
				.map(String::trim)
				.filter(option -> !option.isEmpty())
				.collect(Collectors.toList());
	}

	private String getFieldText(GridPane form, int row) {
		return ((TextField) form.getChildren().get(row * 2 + 1)).getText();
	}
}