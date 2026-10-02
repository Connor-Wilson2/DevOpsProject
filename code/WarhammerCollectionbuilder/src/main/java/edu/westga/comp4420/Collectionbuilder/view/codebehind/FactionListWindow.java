package edu.westga.comp4420.Collectionbuilder.view.codebehind;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import edu.westga.comp4420.Collectionbuilder.Main;
import edu.westga.comp4420.Collectionbuilder.model.FactionList;
import edu.westga.comp4420.Collectionbuilder.model.Unit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

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
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource(Main.ADD_UNIT_WINDOW_RESOURCE));
			Parent parent = loader.load();
			Stage stage = new Stage();
			stage.setTitle("Add Unit");
			stage.initOwner(this.units.getScene().getWindow());
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(new Scene(parent));
			stage.showAndWait();
			Unit newUnit = loader.<AddUnitWindow>getController().getCreatedUnit();
			if (newUnit != null) {
				this.factionList.addUnit(newUnit);
				this.refreshUnits();
			}
		} catch (IOException error) {
			new Alert(Alert.AlertType.ERROR, "Unable to open the add-unit window.").showAndWait();
		}
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

}