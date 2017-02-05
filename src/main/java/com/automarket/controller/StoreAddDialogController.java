package com.automarket.controller;

import com.automarket.entity.Store;
import com.automarket.service.StoreService;
import com.automarket.service.StoreServiceImpl;
import com.automarket.utils.Validator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StoreAddDialogController {
	private static final Logger log = LoggerFactory.getLogger(StoreAddDialogController.class);
	
	@FXML
	private TextField storeNameField;
	@FXML
	private TextField storeDescField;
	
	private Stage dialogStage;
	private Store store;
	private StoreService storeService = new StoreServiceImpl();
	private boolean okClicked = false;
	
	@FXML
	private void initialize() {
		
	}
	
	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Sets the store to be edited in the dialog.
	 * 
	 * @param store
	 */
	public void setStore(Store store) {
		this.store = store;
        storeNameField.setText(store.getName());
		storeDescField.setText(store.getDescription());
	}

	/**
	 * Returns true if the user clicked OK, false otherwise.
	 * 
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleOk() {
		if (Validator.textFieldNotEmpty(storeNameField)) {
			store.setName(storeNameField.getText());
			store.setDescription(storeDescField.getText());
			okClicked = true;
			dialogStage.close();
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Помилка");
			alert.setContentText("Заповніть поле назва");
			alert.showAndWait();
		}
	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}
}
