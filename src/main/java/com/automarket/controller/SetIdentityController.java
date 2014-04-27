package com.automarket.controller;

import com.automarket.entity.Goods;
import com.automarket.service.GoodsService;
import com.automarket.service.GoodsServiceImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yurik on 26.04.14.
 */
public class SetIdentityController {
    @FXML
    private ComboBox<String> goodsEthalon;
    @FXML
    private ComboBox<String> goodsIdentity;
    private Stage dialogStage;
    private boolean okClicked = false;
    private ObservableList<String> goodNames = FXCollections.observableArrayList();
    private GoodsService goodsService = new GoodsServiceImpl();
    private Goods ethalonGoods = new Goods();
    private Goods identityGoods = new Goods();
    private boolean isLaunched = true;

    @FXML
    private void initialize() {
        goodNames.addAll(goodsService.getAllGoodsNames());
        goodsEthalon.setItems(goodNames);
        goodsIdentity.setItems(goodNames);
        goodsEthalon.setValue(ethalonGoods.getName());

        goodsEthalon.getEditor().focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                if (aBoolean2 && !isLaunched) goodsEthalon.setItems(searchGoods(goodsEthalon.getValue()));
                isLaunched = false;
            }
        });

        goodsIdentity.getEditor().focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean aBoolean2) {
                if (aBoolean2) goodsIdentity.setItems(searchGoods(goodsIdentity.getValue()));
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
        int identity = ethalonGoods.getAnalog();
        if (identity <= 0) {
            identity = goodsService.getMaxIdentity() + 1;
            ethalonGoods.setAnalog(identity);
        }
        identityGoods = goodsService.getGoodsByName(goodsIdentity.getValue());
        identityGoods.setAnalog(identity);
        goodsService.addGoods(identityGoods);
        goodsService.addGoods(ethalonGoods);
        okClicked = true;
        dialogStage.close();
    }

    /**
     * Called when the user clicks cancel.
     */
    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public void setEthalonGoods(Goods ethalonGoods) {
        this.ethalonGoods = ethalonGoods;
        goodsEthalon.setValue(ethalonGoods.getName());
    }

    protected ObservableList<String> searchGoods(String s) {
        List<Goods> goodsList = new ArrayList<>(goodsService.searchGoods(s));
        ObservableList<String> goodNames = FXCollections.observableArrayList();
        goodNames.clear();
        for (Goods goods1:goodsList) {
            goodNames.add(goods1.getName());
        }
        return goodNames;
    }

}
