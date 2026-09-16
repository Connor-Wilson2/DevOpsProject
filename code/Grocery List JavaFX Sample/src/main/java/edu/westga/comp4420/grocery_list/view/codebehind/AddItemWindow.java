package edu.westga.comp4420.grocery_list.view.codebehind;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import java.util.List;

import edu.westga.comp4420.grocery_list.model.GroceryItem;

/**
 * CodeBehind To Handle Processing for the AddItemWindow
 *
 * @author	Comp 4420
 * @version Spring 2025
 */
public class AddItemWindow {   
	@FXML private AnchorPane guiPane;
	@FXML private TextField amountNeeded;
    @FXML private TextField name;
	
	private List<GroceryItem> groceryItems;
	
	private void closeWindow() {
		this.guiPane.getScene().getWindow().hide();
	}

    @FXML
    void addItem(ActionEvent event) {
		try {
			String name = this.name.getText();
			int amountNeeded = Integer.parseInt(this.amountNeeded.getText());
			GroceryItem item = new GroceryItem(name, amountNeeded, 0);
			this.groceryItems.add(item);
			this.closeWindow();
		} catch (NumberFormatException error) {
			Alert errorBox = new Alert(AlertType.ERROR);
			errorBox.setContentText("Must provide value number for amount needed.");
			errorBox.showAndWait();
		} catch (IllegalArgumentException error) {
			Alert errorBox = new Alert(AlertType.ERROR);
			errorBox.setContentText(error.getMessage());
			errorBox.showAndWait();
		}

    }

    @FXML
    void cancel(ActionEvent event) {
		this.closeWindow();
    }
	
	
	public void setItemList(List<GroceryItem> groceryItems) {
		this.groceryItems = groceryItems;
	}
	
	@FXML
	void initialize() {
		assert this.amountNeeded != null : "fx:id=\"amountNeeded\" was not injected: check your FXML file 'AddItemWindow.fxml'.";
        assert this.guiPane != null : "fx:id=\"guiPane\" was not injected: check your FXML file 'AddItemWindow.fxml'.";
        assert this.name != null : "fx:id=\"name\" was not injected: check your FXML file 'AddItemWindow.fxml'.";
	}
}
