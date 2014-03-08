package com.automarket.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automarket.entity.Goods;
import com.automarket.service.GoodsService;
import com.automarket.service.GoodsServiceImpl;

public class MainController
{
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private Label messageLabel;
    @FXML private TableView<Goods> goodsTable;
    @FXML private TableColumn<Goods, Long> goodsColumnId;
    @FXML private TableColumn<Goods, String> goodsColumnName;
    @FXML private TableColumn<Goods, String> goodsColumnDescription;
    @FXML private TableColumn<Goods, String> goodsColumnContainer;
    @FXML private TableColumn<Goods, Integer> goodsColumnCount;
    
    private GoodsService goodsService = new GoodsServiceImpl();
    private ObservableList<Goods> goodsList=FXCollections.observableArrayList();
    

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
    
    @FXML public void showAddStage() {
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
	}
    
    @FXML protected void saleGoods() {
    	System.out.println("Sale...");
    }
    
    @FXML protected void cancelSale() {
    	System.out.println("Cancel...");
    }
    
    @FXML protected void loadStoresClick() {
    	System.out.println("Load...");
    }
    
    @FXML protected void salesSelected() {
    	System.out.println("Sales...");
    }
    
    @FXML
    private void initialize() {
    	    	
    }
    
    @FXML protected void goodsSelected(Event event) {
    	log.debug("Load GOODS...");
    	if(!goodsList.isEmpty())
        	goodsList.clear();
        List<Goods> goods = new ArrayList<>();
        goods.addAll(goodsService.getAllGoods());
        Platform.runLater(new Runnable() {
			@Override
			public void run() {
				goodsColumnId.setCellValueFactory(new PropertyValueFactory<Goods, Long>("id"));
		    	goodsColumnName.setCellValueFactory(new PropertyValueFactory<Goods, String>("name"));
		        goodsColumnDescription.setCellValueFactory(new PropertyValueFactory<Goods, String>("description"));
			}
		});
        goodsList = FXCollections.observableList(goods);
        goodsTable.setItems(goodsList);
        
        
    }
    
    @FXML protected void containerSelected() {
    	System.out.println("Containers...");
    }

}
