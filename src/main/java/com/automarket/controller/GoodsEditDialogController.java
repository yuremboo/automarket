package com.automarket.controller;

import com.automarket.entity.Goods;
import com.automarket.service.GoodsService;
import com.automarket.service.GoodsServiceImpl;
import com.automarket.utils.Validator;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class GoodsEditDialogController {

	private static final Logger log = LoggerFactory.getLogger(GoodsEditDialogController.class);

	@FXML
	private TextField nameField;
	@FXML
	private TextField descField;

	private Stage dialogStage;
	private Goods goods;
	@Autowired
	private GoodsService goodsService;
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
		if(Validator.textFieldNotEmpty(nameField)) {
			goods.setName(nameField.getText());
			goods.setDescription(descField.getText());
			okClicked = true;
			dialogStage.close();
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Увага!");
			alert.setContentText("Заповніть поле назва!");
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
