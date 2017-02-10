package com.automarket.controller;

import com.automarket.entity.CommodityCirculation;
import com.automarket.entity.Counter;
import com.automarket.entity.Goods;
import com.automarket.entity.Store;
import com.automarket.service.*;
import com.automarket.utils.Validator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class CounterEditDialogController {

	private static final Logger log = LoggerFactory.getLogger(CounterEditDialogController.class);
	@FXML
	private ComboBox<String> goodsBox;
	@FXML
	private ChoiceBox<String> containerChoice;
	@FXML
	private TextField countField;
	private Stage dialogStage;
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private CounterService counterService;
	@Autowired
	private StoreService storeService;
	@Autowired
	private CommodityCirculationsService circulationsService;
	private boolean okClicked = false;
	private ObservableList<String> goodNames = FXCollections.observableArrayList();
	private ObservableList<String> storeNames = FXCollections.observableArrayList();

	@FXML
	private void initialize() {
		storeNames.addAll(storeService.getAllStoresNames());
		goodNames.addAll(goodsService.getAllGoodsNames());
		containerChoice.setItems(storeNames);
		containerChoice.setValue(storeService.getDefault().getName());
		goodsBox.setItems(goodNames);

		goodsBox.getEditor().focusedProperty().addListener((observableValue, aBoolean1, aBoolean2) -> {
			if(aBoolean2) {
				searchGoods(goodsBox.getValue());
			}
		});

	}

	/**
	 * Sets the stage of this dialog.
	 *
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public Stage getDialogStage() {
		return dialogStage;
	}

	/**
	 * Sets the counter to be edited in the dialog.
	 *
	 * @param counter
	 */
	public void setCounter(Counter counter) {
		goodsBox.setValue(counter.getGoodsName());
		containerChoice.setValue(counter.getStoreName());
		countField.setText(String.valueOf(counter.getCount()));
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
		if(Validator.textFieldNotEmpty(countField) && Integer.valueOf(countField.getText()) >= 0) {
			Goods goods = goodsService.getGoodsByName(goodsBox.getValue());
			Store store = storeService.getStoreByName(containerChoice.getValue());
			if(goods == null) {
				goods = new Goods();
				goods.setName(goodsBox.getValue().replaceAll("\\s+", " "));
				goods.setDescription(goodsBox.getValue());
				goods = goodsService.addGoods(goods);
			}
			Counter counter = counterService.getCounterByGoodsStore(goods, store);
			int count = Integer.parseInt(countField.getText());
			if(counter == null) {
				counter = new Counter();
				counter.setGoods(goods);
				counter.setStore(store);
				counter.setCount(count);
			} else {
				counter.setCount(count + counter.getCount());
			}
			counter = counterService.addOrUpdateCounter(counter);

			CommodityCirculation circulation = new CommodityCirculation();
			circulation.setCount(count);
			circulation.setDate(new Date());
			circulation.setGoods(counter.getGoods());
			circulation.setStore(counter.getStore());
			circulation.setSale(false);
			circulationsService.addCirculation(circulation);
			okClicked = true;
			dialogStage.close();
		} else {
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.initOwner(dialogStage);
			alert.setTitle("Увага!");
			alert.setContentText("Заповніть всі поля корректно!");
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

	protected void searchGoods(String s) {
		List<Goods> goodsList = new ArrayList<>(goodsService.searchGoods(s));
		goodNames.clear();
		for(Goods goods1 : goodsList) {
			goodNames.add(goods1.getName());
		}
		goodsBox.setItems(goodNames);
	}
}
