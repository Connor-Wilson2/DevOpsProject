package edu.westga.comp4420.Collectionbuilder.view.codebehind;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

import edu.westga.comp4420.Collectionbuilder.Main;
import edu.westga.comp4420.Collectionbuilder.model.FactionList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Handles the faction-list home page.
 *
 * @author Comp 4420
 */
public class MainWindow {
	@FXML private ListView<FactionList> factionLists;
	@FXML private Button deleteListButton;
	@FXML private Button openListButton;

	@FXML
	void initialize() {
		assert this.factionLists != null : "fx:id=\"factionLists\" was not injected.";
		assert this.deleteListButton != null : "fx:id=\"deleteListButton\" was not injected.";
		assert this.openListButton != null : "fx:id=\"openListButton\" was not injected.";
		this.factionLists.setCellFactory(listView -> new ListCell<>() {
			@Override
			protected void updateItem(FactionList factionList, boolean empty) {
				super.updateItem(factionList, empty);
				if (empty || factionList == null) {
					this.setText(null);
				} else {
					this.setText(factionList.getTitle() + "  |  " + factionList.getFaction()
							+ " / " + factionList.getSubFaction() + "  |  "
							+ factionList.getDateCreated());
				}
			}
		});
		this.deleteListButton.disableProperty().bind(
				this.factionLists.getSelectionModel().selectedItemProperty().isNull());
		this.openListButton.disableProperty().bind(
				this.factionLists.getSelectionModel().selectedItemProperty().isNull());
	}

	@FXML
	void createList() {
		Dialog<FactionList> dialog = this.createListDialog();
		Optional<FactionList> result = dialog.showAndWait();
		result.ifPresent(this.factionLists.getItems()::add);
	}

	@FXML
	void deleteList() {
		FactionList selectedList = this.factionLists.getSelectionModel().getSelectedItem();
		if (selectedList != null) {
			this.factionLists.getItems().remove(selectedList);
		}
	}

	@FXML
	void openList() {
		FactionList selectedList = this.factionLists.getSelectionModel().getSelectedItem();
		if (selectedList == null) {
			return;
		}
		try {
			FXMLLoader loader = new FXMLLoader(Main.class.getResource(Main.FACTION_LIST_WINDOW_RESOURCE));
			Parent parent = loader.load();
			FactionListWindow controller = loader.getController();
			controller.setFactionList(selectedList);
			Stage stage = new Stage();
			stage.setTitle(selectedList.getTitle());
			stage.initOwner(this.factionLists.getScene().getWindow());
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setScene(new Scene(parent));
			stage.showAndWait();
		} catch (IOException error) {
			new Alert(Alert.AlertType.ERROR, "Unable to open faction list.").showAndWait();
		}
	}

	private Dialog<FactionList> createListDialog() {
		Dialog<FactionList> dialog = new Dialog<>();
		dialog.setTitle("Create Faction List");
		dialog.setHeaderText("Enter the list details");
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		GridPane form = this.createListForm();
		dialog.getDialogPane().setContent(form);
		dialog.setResultConverter(button -> this.createListResult(button, form));
		return dialog;
	}

	private GridPane createListForm() {
		GridPane form = new GridPane();
		form.setHgap(10);
		form.setVgap(10);
		form.add(new Label("Title"), 0, 0);
		form.add(new TextField(), 1, 0);
		form.add(new Label("Faction"), 0, 1);
		form.add(new TextField(), 1, 1);
		form.add(new Label("Subfaction"), 0, 2);
		form.add(new TextField(), 1, 2);
		return form;
	}

	private FactionList createListResult(ButtonType button, GridPane form) {
		if (button != ButtonType.OK) {
			return null;
		}
		try {
			return new FactionList(this.getFieldText(form, 0), LocalDate.now(),
					this.getFieldText(form, 1), this.getFieldText(form, 2));
		} catch (IllegalArgumentException error) {
			new Alert(Alert.AlertType.ERROR, error.getMessage()).showAndWait();
			return null;
		}
	}

	private String getFieldText(GridPane form, int row) {
		return ((TextField) form.getChildren().get(row * 2 + 1)).getText();
	}
}
