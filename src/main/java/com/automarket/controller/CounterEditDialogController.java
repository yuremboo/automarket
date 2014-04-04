package com.automarket.controller;

import com.automarket.entity.Counter;
import com.automarket.entity.Goods;
import com.automarket.service.*;
import com.automarket.utils.Validator;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialogs;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yurik on 13.03.14.
 */
public class CounterEditDialogController {
    private static final Logger log = LoggerFactory.getLogger(CounterEditDialogController.class);
    @FXML
    private ComboBox<String> goodsBox;
    @FXML
    private ChoiceBox<String> containerChoice;
    @FXML
    private TextField countField;
    private Stage dialogStage;
    private Counter counter;
    private Goods goods;
    private GoodsService goodsService = new GoodsServiceImpl();
    private CounterService counterService = new CounterServiceImpl();
    private StoreService storeService = new StoreServiceImpl();
    private boolean okClicked = false;
    private ObservableList<String> goodNames = FXCollections.observableArrayList();
    private ObservableList<String> storeNames = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        storeNames.addAll(storeService.getAllStoresNames());
        goodNames.addAll(goodsService.getAllGoodsNames());
        containerChoice.setItems(storeNames);
        goodsBox.setItems(goodNames);
        goodsBox.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                searchGoods(s2);
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
        this.counter = counter;
        goodsBox.setValue(counter.getGoodsName());
        containerChoice.setValue(counter.getStoreName());
        countField.setText(String.valueOf(counter.getCount()));
    }

    public Counter getCounter() {
        return counter;
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
    private void  handleOk() {
        if (Validator.textFieldNotEmpty(countField) && Integer.valueOf(countField.getText()) > 0) {
            counter.setCount(Integer.parseInt(countField.getText()));
            counter.setGoods(goodsService.getGoodsByName(goodsBox.getValue()));
            counter.setStore(storeService.getStoreByName(containerChoice.getValue()));
            okClicked = true;
            dialogStage.close();
        } else {
            Dialogs.showErrorDialog(dialogStage, "Заповніть всі поля корректно!");
        }
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private void searchGoods(String text) {
        List<Goods> goodsList = new ArrayList<>(goodsService.searchGoods(text));
        goodNames.clear();
        for (Goods goods1:goodsList) {
            goodNames.add(goods1.getName());
        }
        goodsBox.setItems(goodNames);
    }
}
