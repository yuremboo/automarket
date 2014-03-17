package com.automarket.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

import com.automarket.entity.Counter;
import eu.schudt.javafx.controls.calendar.DatePicker;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

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
    Store defaultStore = new Store();
    
	@FXML
	private TextField goodsName;
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
	private ChoiceBox<String> storeChoise;
    @FXML
    private ChoiceBox<String> containerChoice;
    @FXML
    private TableView<Counter> counterTableView;
    @FXML
    private TableColumn<Counter, Long> goodsCounterColumnId;
    @FXML
    private TableColumn<Counter, String> goodsCounterColumnName;
    @FXML
    private TableColumn<Counter, String> goodsCounterColumnContainer;
    @FXML
    private TableColumn<Counter, Integer> goodsCounterColumnC;
    @FXML
    private TableView<CommodityCirculation> reportTableView;
    @FXML
    private TableColumn<CommodityCirculation, String> goodsReportColumn;
    @FXML
    private TableColumn<CommodityCirculation, Integer> countReportColumn;
    @FXML
    private TableColumn<CommodityCirculation, String> storeReportColumn;
    @FXML
    private TableColumn<CommodityCirculation, Date> dateReportColumn;
    @FXML
    private TableColumn<CommodityCirculation, String> saleReportColumn;
    @FXML
    private ChoiceBox<String> storeFilterChoice;
    @FXML
    private ChoiceBox<String> goodsFilterChoice;
    @FXML
    private CheckBox incomeOnlyCheck;
    @FXML
    private CheckBox salesOnlyCheck;
    
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
    @FXML
    private VBox filterVBox;
    private Label fromLabel = new Label();
    private Label toLabel = new Label();
    private DatePicker fromDatePicker;
    private DatePicker toDatePicker;
	
	public void setMainApp(MainApp mainApp) {
	    this.mainApp = mainApp;
	}
	public void setDialogStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

    @FXML
    private void initialize() {
        fromLabel.setText("З");
        toLabel.setText("По");
        fromDatePicker = new DatePicker(Locale.UK);
        fromDatePicker.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        fromDatePicker.getCalendarView().todayButtonTextProperty().set("Today");
        fromDatePicker.getCalendarView().setShowWeeks(false);
        fromDatePicker.getStylesheets().add("/styles/DatePicker.css");

        toDatePicker = new DatePicker(Locale.UK);
        toDatePicker.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        toDatePicker.getCalendarView().todayButtonTextProperty().set("Today");
        toDatePicker.getCalendarView().setShowWeeks(false);
        toDatePicker.getStylesheets().add("/styles/DatePicker.css");

        filterVBox.getChildren().add(fromLabel);
        filterVBox.getChildren().add(fromDatePicker);
        filterVBox.getChildren().add(toLabel);
        filterVBox.getChildren().add(toDatePicker);


        defaultStore = storeService.getDefault();
        storesList = FXCollections.observableArrayList(storeService.getAllStoresNames());
        storesList.add("Всі");
        storeChoise.setItems(storesList);
        storeChoise.setValue(defaultStore.getName());
        containerChoice.setItems(storesList);
        containerChoice.setValue(defaultStore.getName());

        storeFilterChoice.setItems(storesList);
        goodsFilterChoice.setItems(FXCollections.observableArrayList(goodsService.getAllGoodsNames()));

        containerChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                fillContainerTable(s2);
            }
        });
    }

    @FXML public void showAddStage() {
		Goods goods = new Goods();
		boolean okClicked = mainApp.showGoodsEditDialog(goods);
		if (okClicked) {
			goodsService.addGoods(goods);
		}
	}
    
    @FXML protected void saleGoods() {
    	Store store = storeService.getStoreByName(storeChoise.getValue());

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
    	goodsName.setText("");
    	goodsCount.setText("");
    }

    @FXML protected void loadStoresClick() {
    	System.out.println("Load...");
    }

    @FXML protected void salesSelected() {
    	System.out.println("Sales...");
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
        goodsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        goodsList = FXCollections.observableList(goods);
        goodsTable.setItems(goodsList);
    }
    
    @FXML protected void containerSelected() {
    	log.debug("Containers...");
        if (containerChoice.getValue() != null && !containerChoice.getValue().equals("Всі")) {
            fillContainerTable(containerChoice.getValue());
        }
    }

    private void fillContainerTable(String storeName) {
        if(!goodsFullList.isEmpty())
            goodsFullList.clear();
        List<Counter> counters = new ArrayList<>();
        if (!storeName.equals("Всі")) {
            Store store = storeService.getStoreByName(storeName);
            counters.addAll(counterService.getCountersListByStore(store));
        } else {
            counters.addAll(counterService.getCountersList());
        }


        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                goodsCounterColumnId.setCellValueFactory(new PropertyValueFactory<Counter, Long>("id"));
                goodsCounterColumnName.setCellValueFactory(new PropertyValueFactory<Counter, String>("goodsName"));
                goodsCounterColumnContainer.setCellValueFactory(new PropertyValueFactory<Counter, String>("storeName"));
                goodsCounterColumnC.setCellValueFactory(new PropertyValueFactory<Counter, Integer>("count"));
            }
        });
        counterTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
		circulations.addAll(circulationsService.commodityCirculationsByDay(true));
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
        commodityCirculationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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
                if (entry.getValue().size() > 1) {
        			goods.setDescription((String) entry.getValue().get(1));
        		}
        		goodsList.add(goods);
        	}
        	goodsService.addGoodsList(goodsList);
        }
    }

    @FXML protected void showAddGoodsStage() {
        Counter counter = new Counter();
        boolean okClicked = mainApp.showCounterEditDialog(counter);
        if (okClicked) {
            Counter counterByGoodsStore = counterService.getCounterByGoodsStore(counter.getGoods(), counter.getStore());
            counter.setId(counterByGoodsStore.getId());
            counterService.addOrUpdateCounter(counter);
        }
    }

    @FXML protected void onLoadCounters() {
        FileChooser fileChooser = new FileChooser();
        ExtensionFilter filter = new ExtensionFilter("MS Office Excell files", "*.xls", "*.xlsx");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(primaryStage);
        List<Counter> counterList = new ArrayList<>();
        if (file != null) {
            Map<Integer, List<Object>> data = new HashMap<>(WorkWithExcel.readFromExcell(file));
            for (Map.Entry<Integer, List<Object>> entry : data.entrySet()) {
                Goods goods;
                Counter counter = new Counter();
                Store store;
                goods = goodsService.getGoodsByName((String) entry.getValue().get(0));
                if (goods.getId() == 0) {
                    goodsService.addGoods(new Goods(0, (String) entry.getValue().get(0), ""));
                    goods = goodsService.getGoodsByName((String) entry.getValue().get(0));
                }
                store = storeService.getStoreByName(String.valueOf(((Double) entry.getValue().get(1)).intValue()));
                int count = ((Double) entry.getValue().get(2)).intValue();
                counter.setGoods(goods);
                counter.setStore(store);
                counter.setCount(count);
                Counter counterByGoodsStore = counterService.getCounterByGoodsStore(goods, store);
                counter.setId(counterByGoodsStore.getId());
                counterList.add(counter);
            }
            counterService.addOrUpdateCounterList(counterList);
        }
    }

    @FXML protected void addContainer() {
        Store store = new Store();
        boolean okClicked = mainApp.showStoreAddDialog(store);
        if (okClicked) {
            storeService.addStore(store);
            storeChoise.getItems().add(store.getName());
        }
    }

    @FXML protected void setAsDefault() {
        if (containerChoice.getValue().equals("Всі")) {
            Dialogs.showWarningDialog(primaryStage, "Не можна встановити всі контейнери за замовчуванням!");
            return;
        }
        defaultStore.setDefaultStore(false);
        Store newDefault = storeService.getStoreByName(containerChoice.getValue());
        newDefault.setDefaultStore(true);
        storeService.changeDefault(defaultStore, newDefault);
        defaultStore = newDefault;
        Dialogs.showInformationDialog(primaryStage, "Контейнер встановлено по замовчуванню");
    }

    @FXML protected void createReport() {
        Date fromDate = fromDatePicker.getSelectedDate();
        Date toDate = toDatePicker.getSelectedDate();
        String storeName = storeFilterChoice.getValue();
        String goodsName = goodsFilterChoice.getValue();

        Store filterStore = storeName!=null?storeService.getStoreByName(storeName):null;
        Goods filterGoods = goodsName!=null?goodsService.getGoodsByName(goodsName):null;
        List<CommodityCirculation> commodityCirculationsReport = new ArrayList<>();
        if (incomeOnlyCheck.isSelected() && !salesOnlyCheck.isSelected()) {
            commodityCirculationsReport.addAll(circulationsService
                    .commodityCirculationsByTerm(fromDate, toDate, filterStore, filterGoods, false));
        } else if (!incomeOnlyCheck.isSelected() && salesOnlyCheck.isSelected()) {
            commodityCirculationsReport.addAll(circulationsService
                    .commodityCirculationsByTerm(fromDate, toDate, filterStore, filterGoods, true));
        } else {
            commodityCirculationsReport.addAll(circulationsService
                    .commodityCirculationsByTerm(fromDate, toDate, filterStore, filterGoods));
        }
        if (commodityCirculationsReport.size() > 0) {
            fillRepotrTable(commodityCirculationsReport);
        } else {
            reportTableView.setItems(null);
        }
    }

    private void fillRepotrTable(List<CommodityCirculation> circulations) {
        ObservableList<CommodityCirculation> circulationsReportList = FXCollections
                .observableArrayList(circulations);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                goodsReportColumn
                        .setCellValueFactory(new PropertyValueFactory<CommodityCirculation, String>("goodsName"));
                countReportColumn
                        .setCellValueFactory(new PropertyValueFactory<CommodityCirculation, Integer>("count"));
                storeReportColumn
                        .setCellValueFactory(new PropertyValueFactory<CommodityCirculation, String>("storeName"));
                dateReportColumn
                        .setCellValueFactory(new PropertyValueFactory<CommodityCirculation, Date>("date"));
                saleReportColumn
                        .setCellValueFactory(new PropertyValueFactory<CommodityCirculation, String>("saleProp"));

            }
        });

        reportTableView.setItems(circulationsReportList);
        reportTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    protected void exportReport() {
        goodsReportColumn.setSortType(TableColumn.SortType.ASCENDING);
        ArrayList<CommodityCirculation> report = new ArrayList<>(reportTableView.getItems());
        Map<Integer, ArrayList<Object>> listMap = new HashMap<>();
        //listMap.put(-1, new ArrayList<Object>(Arrays.asList("Товар" , "Кількість", "Контейнер", "Дата", "Продаж")));
        for (CommodityCirculation circulation:report) {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(circulation.getGoodsName());
            objects.add(circulation.getCount());
            objects.add(circulation.getStoreName());
            objects.add(circulation.getDate());
            objects.add(circulation.getSaleProp());
            listMap.put(report.indexOf(circulation), objects);
        }

        boolean result = WorkWithExcel.writeToExcell(listMap);
        if (result) {
            Dialogs.showInformationDialog(primaryStage, "Звіт успішно експортовано в папку з програмою");
        } else {
            Dialogs.showErrorDialog(primaryStage, "Виникла помилка при експортуванні звіту! Зверніться до розробника");
        }
    }

    @FXML
    protected void handleDeleteGoods() {
        int selectedIndex = goodsTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            Dialogs.DialogResponse dialogResponse = Dialogs.showConfirmDialog(primaryStage,
                    "Ви дійсно бажаєте видалити товар? " + goodsTable.getSelectionModel().selectedItemProperty().get().getName(),
                    "Видалити товар", "Видалення");
            if (dialogResponse.name().equals("YES")) {
                goodsTable.getItems().remove(selectedIndex);
            }
        } else {
            Dialogs.showWarningDialog(primaryStage,
                    "Please select a person in the table.",
                    "No Person Selected", "No Selection");
        }



    }
}