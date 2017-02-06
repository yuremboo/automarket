package com.automarket.controller;

import com.automarket.entity.Store;
import com.automarket.service.StoreService;
import com.automarket.service.StoreServiceImpl;
import com.automarket.utils.Validator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class StoreAddDialogController {
	private static final Logger log = LoggerFactory.getLogger(StoreAddDialogController.class);
	
	@FXML
	private TextField storeNameField;
	@FXML
	private TextField storeDescField;
	@FXML
	private CheckBox storeIsDefaultField;
	
	private Stage dialogStage;

	@Autowired
	private StoreService storeService;
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
	 * Returns true if the user clicked OK, false otherwise.
	 * 
	 * @return true if the user clicked OK, false otherwise
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
			Store store = new Store();
			store.setName(storeNameField.getText());
			store.setDescription(storeDescField.getText());
			store.setDefaultStore(storeIsDefaultField.isSelected());
			storeService.addStore(store);
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
		okClicked = false;
		dialogStage.close();
	}
}
