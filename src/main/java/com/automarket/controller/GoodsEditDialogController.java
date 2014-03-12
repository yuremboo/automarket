package com.automarket.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Dialogs;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automarket.entity.Goods;
import com.automarket.service.GoodsService;
import com.automarket.service.GoodsServiceImpl;
import com.automarket.utils.Validator;

public class GoodsEditDialogController {
	private static final Logger log = LoggerFactory.getLogger(GoodsEditDialogController.class);
	
	@FXML
	private TextField nameField;
	@FXML
	private TextField descField;
	
	private Stage dialogStage;
	private Goods goods;
	private GoodsService goodsService = new GoodsServiceImpl();
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
	 * Sets the goods to be edited in the dialog.
	 * 
	 * @param goods
	 */
	public void setGoods(Goods goods) {
		this.goods = goods;
		nameField.setText(goods.getName());
		descField.setText(goods.getDescription());
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
		if (Validator.textFieldNotEmpty(nameField)) {
			goods.setName(nameField.getText());
			goods.setDescription(descField.getText());
			okClicked = true;
			dialogStage.close();
		} else {
			Dialogs.showErrorDialog(dialogStage, "Заповніть поле назва");
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
