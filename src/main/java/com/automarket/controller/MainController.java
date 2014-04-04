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
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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

import static javafx.scene.control.Dialogs.*;

public class MainController
{
    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    MainApp mainApp;
    Stage primaryStage;
    Store defaultStore = new Store();

    @FXML
    private TabPane mainTabPane;
    @FXML
    private Tab salesTab;
    @FXML
    private Tab containerTab;
    @FXML
    private Tab goodsTab;
    @FXML
    private Tab reportTab;
	@FXML
	private ComboBox<String> goodsName;
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
    @FXML
    private ProgressBar progressBar;
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Button addCount;
    @FXML
    private Button importCount;
    @FXML
    private Button exportCount;
    @FXML
    private TextField searchTextField;
    @FXML
    private Label statusLabel;
    
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
    private Task importTask;
    private SingleSelectionModel<Tab> selectionModel;

	public void setMainApp(MainApp mainApp) {
	    this.mainApp = mainApp;
	}
	public void setDialogStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

    @FXML
    private void initialize() {
        //Platform.setImplicitExit(false);
        selectionModel = mainTabPane.getSelectionModel();
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

        //progressBar.setVisible(false);
        //progressIndicator.setVisible(false);

        containerChoice.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                fillContainerTable(s2);
            }
        });

        searchTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                searchField(s2);
            }
        });

        selectionModel.selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> observableValue, Tab tab, Tab tab2) {

            }
        });

        goodsName.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String s2) {
                List<Goods> goodsList = new ArrayList<>(goodsService.searchGoods(s2));
                ObservableList<String> goodNames = FXCollections.observableArrayList();
                for (Goods goods1:goodsList) {
                    goodNames.add(goods1.getName());
                }
                goodsName.setItems(goodNames);
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
    	boolean name = Validator.textFieldNotEmpty(goodsName.getEditor(), goodsValidLabel, "Заповніть поле");
    	boolean cnt = Validator.textFieldNotEmpty(goodsCount, countValidLabel, "Заповніть поле");
    	if (!name || !cnt) {
    		return;
    	}	
    	int c = 0;
    	String goodsNameStr = goodsName.getValue();
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
    		goodsName.setValue("");
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
    	goodsName.setValue("");
    	goodsCount.setText("");
    }

    @FXML protected void loadStoresClick() {
    	System.out.println("Load...");
    }

    @FXML protected void salesSelected() {
      //  if (selectionModel.getSelectedIndex() != 0) return;
    	System.out.println("Sales...");
    }
        
    @FXML protected void goodsSelected(Event event) {
        if (!mainTabPane.getSelectionModel().getSelectedItem().equals(goodsTab)) return;
    	log.debug("Load GOODS...");
    	if(!goodsList.isEmpty())
        	goodsList.clear();
        List<Goods> goods = new ArrayList<>();
        goods.addAll(goodsService.getAllGoods());
        goodsList = FXCollections.observableList(goods);
        goodsTable.setItems(goodsList);
				goodsColumnId.setCellValueFactory(new PropertyValueFactory<Goods, Long>("id"));
		    	goodsColumnName.setCellValueFactory(new PropertyValueFactory<Goods, String>("name"));
		        goodsColumnDescription.setCellValueFactory(new PropertyValueFactory<Goods, String>("description"));
        goodsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }
    
    @FXML protected void containerSelected() {
        if (!selectionModel.getSelectedItem().equals(containerTab)) return;
    	log.debug("Containers...");
        if (containerChoice.getValue() != null && !containerChoice.getValue().equals("Всі")) {
            fillContainerTable(containerChoice.getValue());
        }
    }

    private synchronized void fillContainerTable(String storeName) {
        if (storeName == null) {
            storeName = containerChoice.getValue();
        }
        if(!goodsFullList.isEmpty())
            goodsFullList.clear();
        List<Counter> counters = new ArrayList<>();
        if (!storeName.equals("Всі")) {
            Store store = storeService.getStoreByName(storeName);
            counters.addAll(counterService.getCountersListByStore(store));
        } else {
            counters.addAll(counterService.getCountersList());
        }

        goodsFullList = FXCollections.observableList(counters);
        counterTableView.setItems(goodsFullList);
                goodsCounterColumnId.setCellValueFactory(new PropertyValueFactory<Counter, Long>("id"));
                goodsCounterColumnName.setCellValueFactory(new PropertyValueFactory<Counter, String>("goodsName"));
                goodsCounterColumnContainer.setCellValueFactory(new PropertyValueFactory<Counter, String>("storeName"));
                goodsCounterColumnC.setCellValueFactory(new PropertyValueFactory<Counter, Integer>("count"));
        counterTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

    }
    
    @FXML protected void getInfo() {
    	String goodsNameStr = goodsName.getValue();
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
        commodityCirculationTable.setItems(circulationsList);

				commodityCirculationColumnName
						.setCellValueFactory(new PropertyValueFactory<CommodityCirculation, String>("goodsName"));
				commodityCirculationColumnCount
						.setCellValueFactory(new PropertyValueFactory<CommodityCirculation, Integer>("count"));
				commodityCirculationColumnContainer
						.setCellValueFactory(new PropertyValueFactory<CommodityCirculation, String>("storeName"));

        commodityCirculationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		circulationsList = FXCollections.observableList(circulations);

	}
    
    @FXML protected void clickSales() {
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
            CommodityCirculation circulation = new CommodityCirculation();
            circulation.setCount(counter.getCount());
            circulation.setDate(new Date());
            circulation.setGoods(counter.getGoods());
            circulation.setStore(counter.getStore());
            circulation.setSale(false);
            circulationsService.addCirculation(circulation);
            if (counter.getId() > 0) {
                counter.setCount(counter.getCount() + counterByGoodsStore.getCount());
            }

            counterService.addOrUpdateCounter(counter);
            fillContainerTable(containerChoice.getValue());
        }
    }

    @FXML protected void onLoadCounters() {
        FileChooser fileChooser = new FileChooser();
        ExtensionFilter filter = new ExtensionFilter("MS Office Excell files", "*.xls", "*.xlsx");
        fileChooser.getExtensionFilters().add(filter);
        File file = fileChooser.showOpenDialog(primaryStage);

        addCount.setDisable(true);
        importCount.setDisable(true);
        exportCount.setDisable(true);
        importTask = importCounters(file);
        progressBar.progressProperty().unbind();
        progressIndicator.progressProperty().unbind();
        statusLabel.textProperty().unbind();
        progressBar.setProgress(0);
        progressIndicator.setProgress(0);
        progressBar.progressProperty().bind(importTask.progressProperty());
        progressIndicator.progressProperty().bind(importTask.progressProperty());
        statusLabel.textProperty().bind(importTask.messageProperty());
        new Thread(importTask).start();
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
            showWarningDialog(primaryStage, "Не можна встановити всі контейнери за замовчуванням!");
            return;
        }
        defaultStore.setDefaultStore(false);
        Store newDefault = storeService.getStoreByName(containerChoice.getValue());
        newDefault.setDefaultStore(true);
        storeService.changeDefault(defaultStore, newDefault);
        defaultStore = newDefault;
        showInformationDialog(primaryStage, "Контейнер встановлено по замовчуванню");
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

    /**
     *
     * @param circulations
     */
    private void fillRepotrTable(List<CommodityCirculation> circulations) {
        ObservableList<CommodityCirculation> circulationsReportList = FXCollections
                .observableArrayList(circulations);
        reportTableView.setItems(circulationsReportList);

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
            showInformationDialog(primaryStage, "Звіт успішно експортовано в папку з програмою");
        } else {
            showErrorDialog(primaryStage, "Виникла помилка при експортуванні звіту! Зверніться до розробника");
        }
    }

    @FXML
    protected void handleDeleteGoods() {
        int selectedIndex = goodsTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            DialogResponse dialogResponse = showConfirmDialog(primaryStage,
                    "Ви дійсно бажаєте видалити товар? Дію не можливо буде повернути!" + goodsTable.getSelectionModel().selectedItemProperty().get().getName(),
                    "Видалити товар", "Видалення");
            if (dialogResponse.name().equals("YES")) {
                goodsService.remove(goodsTable.getSelectionModel().getSelectedItem());
                goodsTable.getItems().remove(selectedIndex);
            }
        } else {
            showWarningDialog(primaryStage,
                    "Виберіть товар для видалення.",
                    "Не вибрано товару", "Не вибрано");
        }
    }

    @FXML
    protected void handleEditGoods() {
        Goods selectedGoods = goodsTable.getSelectionModel().getSelectedItem();
        if (selectedGoods != null) {
            boolean okClicked = mainApp.showGoodsEditDialog(selectedGoods);
            if (okClicked) {
                goodsService.addGoods(selectedGoods);
                refreshTable();
            }
        }
    }

    private void refreshTable() {
        int selectedIndex = goodsTable.getSelectionModel().getSelectedIndex();
        goodsTable.setItems(null);
        goodsTable.layout();
        goodsList = FXCollections.observableList(goodsService.getAllGoods());
        goodsTable.setItems(goodsList);
        // Must set the selected index again (see http://javafx-jira.kenai.com/browse/RT-26291)
        goodsTable.getSelectionModel().select(selectedIndex);
    }

    @FXML
    protected void exportCounters() {
        ArrayList<Counter> counterArrayList = new ArrayList<>(counterTableView.getItems());
        Map<Integer, ArrayList<Object>> listMap = new HashMap<>();
        //listMap.put(-1, new ArrayList<Object>(Arrays.asList("Товар" , "Кількість", "Контейнер", "Дата", "Продаж")));
        for (Counter counter:counterArrayList) {
            ArrayList<Object> objects = new ArrayList<>();
            objects.add(counter.getGoodsName());
            objects.add(counter.getStoreName());
            objects.add(counter.getCount());
            listMap.put(counterArrayList.indexOf(counter), objects);
        }

        boolean result = WorkWithExcel.writeToExcell(listMap);
        if (result) {
            showInformationDialog(primaryStage, "Дані успішно експортовано в папку з програмою");
        } else {
            showErrorDialog(primaryStage, "Виникла помилка при експортуванні звіту! Зверніться до розробника");
        }
    }

    /**
     *
     */
    @FXML
    protected void showCopyright() {
        String s = System.getProperty("line.separator");
        showInformationDialog(primaryStage, "Програма для ведення обліку товару." + s +
                "Розробник Юрій Михалецький" + s + "email:yurik.my@gmail.com" + s +
                "All rights reserved © Yurembo 2014.", "Про програму", "Copyright");
    }

    private Task importCounters(final File file) {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                updateProgress(0, 1);
                List<Counter> counterList = new ArrayList<>();
                List<CommodityCirculation> circulationList = new ArrayList<>();
                if (file != null) {
                    LinkedHashMap<Integer, List<Object>> data = WorkWithExcel.readFromExcell(file);
                    updateProgress(0.05, 1);
                    updateMessage("Перевірка і внесення товару...");
                    byte b = 1;
                    String s = new String("");
                    Set entrySet = data.entrySet();
                    Iterator iterator = entrySet.iterator();
                    while (iterator.hasNext()) {
                        System.out.println("sdssdsdsd" + iterator.next());
                    }


                    for (Map.Entry<Integer, List<Object>> entry : data.entrySet()) {
                        if (s.equals(entry.getValue().get(0).toString())) continue;
                        s = entry.getValue().get(0).toString();
                        //if (entry.getValue().size() < 3 || entry.getValue().isEmpty() || entry.getValue().contains("")) continue;
                        Goods goods;
                        Counter counter = new Counter();
                        CommodityCirculation circulation = new CommodityCirculation();
                        Store store;
                        goods = goodsService.getGoodsByName(entry.getValue().get(0).toString());
                        if (goods.getId() == 0 || goods.getName() == null) {
                            b = goodsService.addGoods(new Goods(0, entry.getValue().get(0).toString(), ""));
                            goods = goodsService.getGoodsByName(entry.getValue().get(0).toString());
                        }
                        if (b == 0) continue;
                        store = storeService.getStoreByName(String.valueOf(((Double) entry.getValue().get(1)).intValue()));
                        if (store.getId() == 0 || store.getName() == null) {
                            updateMessage("Невірно вказаний контейнер!");
                            continue;
                        }
                        if (entry.getKey() == 554) {
                            System.out.println(553);
                        }
                        int count = ((Double) entry.getValue().get(2)).intValue();
                        counter.setGoods(goods);
                        counter.setStore(store);
                        counter.setCount(count);
                        circulation.setCount(count);
                        circulation.setDate(new Date());
                        circulation.setGoods(goods);
                        circulation.setStore(store);
                        circulation.setSale(false);
                        System.out.println(entry.getKey() + " - " + entry.getValue());
                        Counter counterByGoodsStore = counterService.getCounterByGoodsStore(goods, store);
                        counter.setId(counterByGoodsStore.getId());
                        if (counter.getId() > 0) {
                            counter.setCount(counter.getCount() + counterByGoodsStore.getCount());
                        }
                        System.out.println("A");
                        if (circulation.getCount() > 0) {
                            circulationList.add(circulation);
                        }
                        counterList.add(counter);
                        System.out.println("B");
                    }
                    System.out.println("C");
                    updateProgress(0.3, 1);
                    updateMessage("Завантаження тоару...");
                    counterService.addOrUpdateCounterList(counterList);
                    updateProgress(0.7, 1);
                    updateMessage("Завантаження обороту...");
                    circulationsService.addCirculations(circulationList);
                    updateProgress(0.95, 1);
                    updateMessage("Виведення таблиці...");
                }
                fillContainerTable(storeChoise.getValue());
                updateProgress(1, 1);
                importCount.setDisable(false);
                exportCount.setDisable(false);
                addCount.setDisable(false);
                updateMessage("");
                return true;
            }
        };
    }

    protected void searchField(String text) {
        if (text.length() < 2) {
            return;
        }
        if(!goodsFullList.isEmpty())
            goodsFullList.clear();
        List<Counter> counters = new ArrayList<>();
        if (!storeChoise.getValue().equals("Всі")) {
            counters.addAll(counterService.searchCountersByGoods(text));
        } else {
            counters.addAll(counterService.searchCountersByGoods(text));
        }

        goodsFullList = FXCollections.observableList(counters);
        counterTableView.setItems(goodsFullList);
        goodsCounterColumnId.setCellValueFactory(new PropertyValueFactory<Counter, Long>("id"));
        goodsCounterColumnName.setCellValueFactory(new PropertyValueFactory<Counter, String>("goodsName"));
        goodsCounterColumnContainer.setCellValueFactory(new PropertyValueFactory<Counter, String>("storeName"));
        goodsCounterColumnC.setCellValueFactory(new PropertyValueFactory<Counter, Integer>("count"));
        counterTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        System.out.println(text);
    }

    @FXML
    protected void contextSaleClick() {
        goodsName.setValue(counterTableView.getSelectionModel().getSelectedItem().getGoods().getName());
        selectionModel.select(salesTab);
    }

}