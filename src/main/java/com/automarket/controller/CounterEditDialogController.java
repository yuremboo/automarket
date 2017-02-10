package com.automarket.controller;

import com.automarket.entity.CommodityCirculation;
import com.automarket.entity.Counter;
import com.automarket.entity.Goods;
import com.automarket.entity.Store;
import com.automarket.fxutils.AutoCompleteComboBoxListener;
import com.automarket.service.CommodityCirculationsService;
import com.automarket.service.CounterService;
import com.automarket.service.GoodsService;
import com.automarket.service.StoreService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class CounterEditDialogController {

	private static final Logger log = LoggerFactory.getLogger(CounterEditDialogController.class);
	@FXML
	private ComboBox<String> goodsBox;
	@FXML
	private ChoiceBox<String> containerChoice;
	@FXML
	private TextField countField;
	@FXML
	private Label stateLabel;
	private Stage dialogStage;
	private final GoodsService goodsService;
	private final CounterService counterService;
	private final StoreService storeService;
	private final CommodityCirculationsService circulationsService;
	private boolean okClicked = false;
	private ObservableList<String> goodNames = FXCollections.observableArrayList();
	private ObservableList<String> storeNames = FXCollections.observableArrayList();
	private boolean analogMode;
	private Counter selectedCounter;

	@Autowired
	public CounterEditDialogController(GoodsService goodsService, CounterService counterService, StoreService storeService,
			CommodityCirculationsService circulationsService) {
		this.goodsService = goodsService;
		this.counterService = counterService;
		this.storeService = storeService;
		this.circulationsService = circulationsService;
	}

	@FXML
	private void initialize() {
		log.debug("Edit counters initialization");
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
		goodsBox.getEditor().focusedProperty().addListener((observableValue, aBoolean1, aBoolean2) -> {
			if(aBoolean2) {
				searchGoods(goodsBox.getValue());
				new AutoCompleteComboBoxListener<>(goodsBox);
			}
		});
		log.debug("Edit counters initialized");
	}

	/**
	 * Sets the stage of this dialog.
	 *
	 * @param dialogStage
	 */
	void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	/**
	 * Sets the counter to be edited in the dialog.
	 *
	 * @param counter
	 */
	public void setCounter(Counter counter) {
		this.selectedCounter = counter;
		if(selectedCounter != null && !analogMode) {
			goodsBox.setValue(counter.getGoodsName());
			containerChoice.setValue(counter.getStoreName());
			countField.setText(String.valueOf(counter.getCount()));
		}
	}

	/**
	 * Returns true if the user clicked OK, false otherwise.
	 *
	 * @return
	 */
	boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleOk() {
		if(StringUtils.isNotBlank(goodsBox.getValue())) {
			Goods goods = goodsService.getGoodsByName(goodsBox.getValue());
			Store store = storeService.getStoreByName(containerChoice.getValue());
			if(goods == null) {
				goods = new Goods();
				goods.setName(goodsBox.getValue().replaceAll("\\s+", " "));
				goods.setDescription(goodsBox.getValue());
				goods = goodsService.addGoods(goods);
			}
			if(analogMode) {
				Set<Goods> goodsSet = new HashSet<>();
				goodsSet.add(goods);
				goodsService.addAnalogs(selectedCounter.getGoods(), goodsSet);
			}
			Counter counter = counterService.getCounterByGoodsStore(goods, store);
			int count = StringUtils.isNotBlank(countField.getText()) ? Integer.parseInt(countField.getText()) : 0;
			if(counter == null) {
				counter = new Counter();
				counter.setGoods(goods);
				counter.setStore(store);
				counter.setCount(count);
				counter = counterService.addOrUpdateCounter(counter);
				addCirculation(counter, count);
			} else if(!analogMode) {
				counter.setCount(count + counter.getCount());
				counter = counterService.addOrUpdateCounter(counter);
				addCirculation(counter, count);
			}
			okClicked = true;
			dialogStage.close();
		}
	}

	private void addCirculation(Counter counter, int count) {
		CommodityCirculation circulation = new CommodityCirculation();
		circulation.setCount(count);
		circulation.setDate(new Date());
		circulation.setGoods(counter.getGoods());
		circulation.setStore(counter.getStore());
		circulation.setSale(false);
		circulationsService.addCirculation(circulation);
	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	private void searchGoods(String s) {
		List<Goods> goodsList = new ArrayList<>(goodsService.searchGoods(s));
		goodNames.clear();
		goodNames.addAll(goodsList.stream().map(Goods::getName).collect(Collectors.toList()));
		goodsBox.setItems(goodNames);
	}

	void setAnalogMode(boolean analogMode) {
		this.analogMode = analogMode;
		stateLabel.setText(analogMode ? "Аналог для " + selectedCounter.getGoods().getName() : "");
	}
}
