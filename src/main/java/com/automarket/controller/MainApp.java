package com.automarket.controller;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automarket.entity.Goods;

public class MainApp extends Application {

    private static final Logger log = LoggerFactory.getLogger(MainApp.class);

    private Stage primaryStage;
    
    public static void main(String[] args) throws Exception {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {

        log.info("Starting Hello JavaFX and Maven demonstration application");

        String fxmlFile = "/fxml/New.fxml";
        log.debug("Loading FXML for main view from: {}", fxmlFile);
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
		Parent rootNode = (Parent) loader.load();
        log.debug("Showing JFX scene");
        Scene scene = new Scene(rootNode);
        scene.getStylesheets().add("/styles/styles.css");

        primaryStage.setTitle("Auto");
        primaryStage.setScene(scene);
        this.primaryStage = primaryStage;
        MainController controller = loader.getController();
        controller.setMainApp(this);
        primaryStage.show();
    }
    
    public boolean showGoodsEditDialog(Goods goods) {
    	log.info("Showing goods dialog");
    	try {
    		String fxmlFile = "/fxml/GoodsEditDialog.fxml";
    		FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
    		Parent rootNode = (Parent) loader.load();
    		Stage dialogStage = new Stage();
    		dialogStage.setTitle("Edit Goods");
    		dialogStage.initModality(Modality.WINDOW_MODAL);
    		dialogStage.initOwner(primaryStage);
    		Scene scene = new Scene(rootNode);
    		dialogStage.setScene(scene);
    		
    		GoodsEditDialogController goodsEditDialogController = loader.getController();
    		goodsEditDialogController.setDialogStage(dialogStage);
    		goodsEditDialogController.setGoods(goods);
    		
    		dialogStage.showAndWait();
    		return goodsEditDialogController.isOkClicked();
    	} catch (IOException e) {
    		log.error(e.getMessage());
    		return false;
    	}
    }
}
