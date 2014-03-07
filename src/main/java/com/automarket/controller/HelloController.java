package com.automarket.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automarket.entity.Goods;
import com.automarket.service.GoodsService;
import com.automarket.service.GoodsServiceImpl;

public class HelloController
{
    private static final Logger log = LoggerFactory.getLogger(HelloController.class);

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private Label messageLabel;
    @FXML private TableView<Goods> goodsTable;
    
    private GoodsService goodsService = new GoodsServiceImpl();
    private ObservableList<Goods> goodsList=FXCollections.observableArrayList();
    
    public void addGoods(Goods goods) {
    	goodsService.addGoods(goods);
    }

    @FXML public void sayHello() {

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        
        Goods goods = new Goods(1, firstName, lastName);
        goodsService.addGoods(goods);

        StringBuilder builder = new StringBuilder();

        if (!StringUtils.isEmpty(firstName)) {
            builder.append(firstName);
        }

        if (!StringUtils.isEmpty(lastName)) {
            if (builder.length() > 0) {
                builder.append(" ");
            }
            builder.append(lastName);
        }

        if (builder.length() > 0) {
            String name = builder.toString();
            log.debug("Saying hello to " + name);
            messageLabel.setText("Hello " + name);
        } else {
            log.debug("Neither first name nor last name was set, saying hello to anonymous person");
            messageLabel.setText("Hello mysterious person");
        }
    }
    
    @FXML public void showStage() {
		Stage newStage = new Stage();
		
		String fxmlFile = "/fxml/AlertDialog_css.fxml";
        log.debug("Loading FXML for main view from: {}", fxmlFile);
        Parent rootNode = null;
		try {
			rootNode = (Parent) FXMLLoader.load(getClass().getResource(fxmlFile));
		} catch (IOException e) {
			e.printStackTrace();
		}

        log.debug("Showing JFX scene");
        Scene scene = new Scene(rootNode);
        scene.getStylesheets().add("/styles/AlertDialog.css");

        newStage.setTitle("Hello JavaFX and Maven");
        newStage.setScene(scene);
        newStage.show();
        
        if(!goodsList.isEmpty())
        	goodsList.clear();
        List<Goods> goods = new ArrayList<>();
        goods.addAll(goodsService.getAllGoods());
        goodsList = FXCollections.observableList(goods);
        goodsTable.setItems(goodsList);

	}

}
