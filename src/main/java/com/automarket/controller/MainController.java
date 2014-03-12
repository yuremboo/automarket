package com.automarket.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.automarket.entity.Counter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.automarket.entity.CommodityCirculation;
import com.automarket.entity.Goods;
import com.automarket.entity.Store;
import com.automarket.service.CommodityCirculationsService;
import com.automarket.service.CommodityCirculationsServiceImpl;
import com.automarket.service.CounterService;
import com.automarket.service.CounterServiceImpl;
import com.automarket.service.GoodsService;
import com.automarket.service.GoodsServiceImpl;
import com.automarket.service.StoreService;
import com.automarket.service.StoreServiceImpl;
import com.automarket.utils.Validator;
import com.automarket.utils.WorkWithExcel;

public class MainController
{
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    MainApp mainApp;
    Stage primaryStage;
    
	@FXML
	private TextField goodsName;
	@FXML
	private Label messageLabel;
	@FXML
	private Label infoLabel;
	@FXML
	private Label countValidLabel;
	@FXML
	private Label goodsValidLabel;
	@FXML
	private TextField goodsCount;
	@FXML
	private TableView<Goods> goodsTable;
	@FXML
	private TableView<CommodityCirculation> commodityCirculationTable;
	@FXML
	private TableColumn<CommodityCirculation, String> commodityCirculationColumnName;
	@FXML
	private TableColumn<CommodityCirculation, Integer> commodityCirculationColumnCount;
	@FXML
	private TableColumn<CommodityCirculation, String> commodityCirculationColumnContainer;
	@FXML
	private TableColumn<Goods, Long> goodsColumnId;
	@FXML
	private TableColumn<Goods, String> goodsColumnName;
	@FXML
	private TableColumn<Goods, String> goodsColumnDescription;
	@FXML
	private TableColumn<Goods, String> goodsColumnContainer;
	@FXML
	private TableColumn<Goods, Integer> goodsColumnCount;
	@FXML
	private ChoiceBox<String> storeChoise;
    @FXML
    private TableView<Counter> counterTableView;
    @FXML
    private TableColumn<Counter, Long> goodsCounterColumnId;
    @FXML
    private TableColumn<Counter, String> goodsCounterColumnName;
    @FXML
    private TableColumn<Counter, String> goodsCounterColumnDescription;
    @FXML
    private TableColumn<Counter, String> goodsCounterColumnContainer;
    @FXML
    private TableColumn<Counter, Integer> goodsCounterColumnC;
    
	private GoodsService goodsService = new GoodsServiceImpl();
	private CounterService counterService = new CounterServiceImpl();
	private StoreService storeService = new StoreServiceImpl();
	private CommodityCirculationsService circulationsService = new CommodityCirculationsServiceImpl();
	private ObservableList<Goods> goodsList = FXCollections
			.observableArrayList();
    private ObservableList<Counter> goodsFullList = FXCollections
            .observableArrayList();
	private ObservableList<CommodityCirculation> circulationsList = FXCollections
			.observableArrayList();
	private ObservableList<String> storesList = FXCollections.observableArrayList();
	
	public void setMainApp(MainApp mainApp) {
	    this.mainApp = mainApp;
	}
	public void setDialogStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
    
    @FXML public void showAddStage() {
		Goods goods = new Goods();
		boolean okClicked = mainApp.showGoodsEditDialog(goods);
		if (okClicked) {
			goodsService.addGoods(goods);
		}
	}
    
    @FXML protected void saleGoods() {
    	Store store = new Store();
    	if (storeChoise.getValue() == null) {
    		store = storeService.getDefault();
    	} else {
    		store = storeService.getStoreByName(storeChoise.getValue());
    	}
    	goodsValidLabel.setText("");
    	countValidLabel.setText("");
    	boolean name = Validator.textFieldNotEmpty(goodsName, goodsValidLabel, "Заповніть поле");
    	boolean cnt = Validator.textFieldNotEmpty(goodsCount, countValidLabel, "Заповніть поле");
    	if (!name || !cnt) {
    		return;
    	}	
    	int c = 0;
    	String goodsNameStr = goodsName.getText();
    	int count = 0;
    	try {
    		count = Integer.parseInt(goodsCount.getText());
    	} catch (Exception e) {
    		log.error(e.getMessage());
    		countValidLabel.setText("Введіть число!");
    		return;
    	}
    	Goods goods = goodsService.getGoodsByName(goodsNameStr);
    	CommodityCirculation commodityCirculation = new CommodityCirculation(0, new Date(), goods, count);
    	commodityCirculation.setSale(true);
    	commodityCirculation.setStore(store);
    	log.debug("Sale " + goods + " " + store);
    	if (goods != null && goods.getId() != 0) {
    		infoLabel.setText(goods.toString());
    		c = counterService.sale(goods, store, count);
    	} else {
    		infoLabel.setText("Товар не знайдено!");
		}
    	if (c > 0) {
    		circulationsService.addCirculation(commodityCirculation);
    		goodsName.setText("");
    		goodsCount.setText("");
    		infoLabel.setText("Продано: " + goodsNameStr + " Кількість: " + count);
    	} else if (c == -1) {
    		infoLabel.setText("Не вистачає кількості одиниць товару для продажу!");
		} else {
    		infoLabel.setText("Виникла помилка при продажі! Перевірте введені дані!");
		}
    	commodityList();
    }
    
    @FXML protected void cancelSale() {
    	storeChoise.setValue(null);
    	goodsName.setText("");
    	goodsCount.setText("");
    }
    
    @FXML protected void loadStoresClick() {
    	System.out.println("Load...");
    }
    
    @FXML protected void salesSelected() {
    	System.out.println("Sales...");
    }
    
    @FXML
    private void initialize() {
    	//commodityList();
    	storesList = FXCollections.observableArrayList(storeService.getAllStoresNames());
    	storesList.add(null);
    	storeChoise.setItems(storesList);
    	System.out.println(storeChoise.getValue());
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
    	log.debug("Containers...");
        Store store = storeService.getDefault();
        if(!goodsFullList.isEmpty())
            goodsFullList.clear();
        List<Counter> counters = new ArrayList<>();
        counters.addAll(counterService.getCountersListByStore(store));
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                goodsCounterColumnId.setCellValueFactory(new PropertyValueFactory<Counter, Long>("id"));
                goodsCounterColumnName.setCellValueFactory(new PropertyValueFactory<Counter, String>("goodsName"));
//                goodsCounterColumnDescription.setCellValueFactory(new PropertyValueFactory<Counter, String>("description"));
                goodsCounterColumnC.setCellValueFactory(new PropertyValueFactory<Counter, Integer>("count"));
            }
        });
        goodsFullList = FXCollections.observableList(counters);
        counterTableView.setItems(goodsFullList);
    }
    
    @FXML protected void getInfo() {
    	String goodsNameStr = goodsName.getText();
    	Goods goods = goodsService.getGoodsByName(goodsNameStr);
    	if (goods != null && goods.getId() != 0) {
    		infoLabel.setText(goods.toString());
    	} else {
    		infoLabel.setText("Товар не знайдено!");
		}
    }
    
    public void commodityList() {
		List<CommodityCirculation> circulations = new ArrayList<>();
		circulations.addAll(circulationsService.commodityCirculationsByDay());
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				commodityCirculationColumnName
						.setCellValueFactory(new PropertyValueFactory<CommodityCirculation, String>("goodsName"));
				commodityCirculationColumnCount
						.setCellValueFactory(new PropertyValueFactory<CommodityCirculation, Integer>("count"));
				commodityCirculationColumnContainer
						.setCellValueFactory(new PropertyValueFactory<CommodityCirculation, String>("storeName"));
			}
		});
		circulationsList = FXCollections.observableList(circulations);
		commodityCirculationTable.setItems(circulationsList);
	}
    
    @FXML protected void clickSales() {
    	commodityList();
    }
    
    @FXML protected void clickMonthSales() {
    	commodityList();
    }
    
    @FXML protected void onLoad() {
    	FileChooser fileChooser = new FileChooser();
    	ExtensionFilter filter = new ExtensionFilter("MS Office Excell files", "*.xls", "*.xlsx");
    	fileChooser.getExtensionFilters().add(filter);
    	File file = fileChooser.showOpenDialog(primaryStage);
    	List<Goods> goodsList = new ArrayList<>();
        if (file != null) {
        	Map<Integer, List<Object>> data = new HashMap<>(WorkWithExcel.readFromExcell(file));
        	for (Map.Entry<Integer, List<Object>> entry : data.entrySet()) {
        		Goods goods = new Goods();
        		goods.setName((String) entry.getValue().get(0));
        		boolean b = entry.getValue().get(1) != null ? true:false;
        		if (b) {
        			goods.setDescription((String) entry.getValue().get(1));
        		}
        		goodsList.add(goods);
        	}
        	goodsService.addGoodsList(goodsList);
        }
    }

	

}
