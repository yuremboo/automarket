package com.automarket.controller;

import java.io.IOException;

import com.automarket.HibernateConfig;
import com.automarket.entity.Counter;
import com.automarket.entity.Store;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javafx.util.Callback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automarket.entity.Goods;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MainApp extends Application {

	private static final Logger log = LoggerFactory.getLogger(MainApp.class);
	private AnnotationConfigApplicationContext APPLICATION_CONTEXT;

	private Stage primaryStage;

	public static void main(String[] args) throws Exception {
		launch(args);
	}

	public void start(Stage primaryStage) throws Exception {

		log.info("Starting Hello JavaFX and Maven demonstration application");
		Platform.setImplicitExit(true);

		String fxmlFile = "/fxml/Root.fxml";
		log.debug("Loading FXML for main view from: {}", fxmlFile);
		FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
		APPLICATION_CONTEXT = new AnnotationConfigApplicationContext(HibernateConfig.class);
		loader.setControllerFactory(((ApplicationContext) APPLICATION_CONTEXT)::getBean);
		Parent rootNode = loader.load();
		log.debug("Showing JFX scene");
		Scene scene = new Scene(rootNode);

		primaryStage.setTitle("Auto");
		primaryStage.setScene(scene);
		this.primaryStage = primaryStage;
		MainController controller = loader.getController();
		controller.setMainApp(this);
		controller.setDialogStage(primaryStage);
		primaryStage.show();
	}

	public boolean showGoodsEditDialog(Goods goods) {
		log.info("Showing goods dialog");
		try {
			String fxmlFile = "/fxml/GoodsEditDialog.fxml";
			FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
			loader.setControllerFactory(((ApplicationContext) APPLICATION_CONTEXT)::getBean);
			Parent rootNode = loader.load();
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
		} catch(IOException e) {
			log.error(e.getMessage());
			return false;
		}
	}

	public boolean showCounterEditDialog(boolean analogsMode, Counter selected) {
		log.info("Showing counter dialog");

		try {
			String fxmlFile = "/fxml/CounterEditDialog.fxml";
			FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
			loader.setControllerFactory(((ApplicationContext) APPLICATION_CONTEXT)::getBean);
			Parent rootNode = loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Edit Goods");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(rootNode);
			dialogStage.setScene(scene);
			CounterEditDialogController counterEditDialogController = loader.getController();
			counterEditDialogController.setDialogStage(dialogStage);
			counterEditDialogController.setCounter(selected);
			counterEditDialogController.setAnalogMode(analogsMode);
			dialogStage.showAndWait();
			return counterEditDialogController.isOkClicked();
		} catch(IOException e) {
			log.error(e.getMessage());
			return false;
		}
	}

	public boolean showStoreAddDialog() {
		log.info("Showing store dialog");
		try {
			String fxmlFile = "/fxml/StoreAddDialog.fxml";
			FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
			loader.setControllerFactory(((ApplicationContext) APPLICATION_CONTEXT)::getBean);
			Parent rootNode = loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add store");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(rootNode);
			dialogStage.setScene(scene);

			StoreAddDialogController storeAddDialogController = loader.getController();
			storeAddDialogController.setDialogStage(dialogStage);

			dialogStage.showAndWait();
			return storeAddDialogController.isOkClicked();
		} catch(IOException e) {
			log.error(e.getMessage());
			return false;
		}
	}

	public boolean showSetIdentityDialog(Goods goods) {
		log.info("Showing identity dialog");
		try {
			String fxmlFile = "/fxml/SetIdentityDialog.fxml";
			FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(fxmlFile));
			loader.setControllerFactory(((ApplicationContext) APPLICATION_CONTEXT)::getBean);
			Parent rootNode = loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add identity");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(rootNode);
			dialogStage.setScene(scene);

			SetIdentityController identityController = loader.getController();
			identityController.setDialogStage(dialogStage);
			identityController.setEthalonGoods(goods);

			dialogStage.showAndWait();
			return identityController.isOkClicked();
		} catch(IOException e) {
			log.error(e.getMessage());
			return false;
		}
	}

}
