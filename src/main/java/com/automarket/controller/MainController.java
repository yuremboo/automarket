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
import com.automarket.utils.GoodsDTO;
import com.automarket.utils.Validator;
import com.automarket.utils.WorkWithExcel;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class MainController {

	private static final Logger log = LoggerFactory.getLogger(MainController.class);
	private static final String ALL_STORES = "Всі";
	private static final String TABLE_PROGRESS_INDICATOR = "tableProgressIndicator";
	private static final String GOODS_NAME_ID = "goods.name";
	private static final String STORE_NAME_ID = "store.name";
	private static final String COUNT_ID = "count";
	private static final String DATE_ID = "date";
	private static final int PAGE_SIZE = 15;

	private MainApp mainApp;
	private Stage primaryStage;
	private Store defaultStore = new Store();
	private boolean analogsMode;

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
	private Label stateLabel;
	@FXML
	private TextField goodsCount;
	@FXML
	private TextField goodsPrice;
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
	private TableColumn<Goods, Integer> goodsColumnTotalItems;
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
	private TableColumn<Counter, Double> goodsCounterPriceColumn;
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
	@FXML
	private StackPane tableStackPane;

	private final GoodsService goodsService;
	private final CounterService counterService;
	private final StoreService storeService;
	private final CommodityCirculationsService circulationsService;
	private ObservableList<Goods> goodsList = FXCollections.observableArrayList();
	private ObservableList<Counter> goodsFullList = FXCollections.observableArrayList();
	@FXML
	private VBox filterVBox;
	private Label fromLabel = new Label();
	private Label toLabel = new Label();
	private DatePicker fromDatePicker;
	private DatePicker toDatePicker;
	private SingleSelectionModel<Tab> selectionModel;
	private Counter selectedCounter;

	@Autowired
	public MainController(CounterService counterService, GoodsService goodsService, StoreService storeService,
			CommodityCirculationsService circulationsService) {
		this.counterService = counterService;
		this.goodsService = goodsService;
		this.storeService = storeService;
		this.circulationsService = circulationsService;
	}

	void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}

	void setDialogStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	@FXML
	private void initialize() {
		selectionModel = mainTabPane.getSelectionModel();
		initializeDatePickers();
		initCounterTableFields();
		initGoodsTableFields();
		initCommodityTable();
		initializeStores();
		initializeGoods();
		initReportTable();
		initCounterTable();
		progressBar.setVisible(false);
		progressIndicator.setVisible(false);
	}

	private void initCounterTable() {
		counterTableView.setOnKeyPressed(event -> {
			selectedCounter = counterTableView.getSelectionModel().getSelectedItem();
			if(selectedCounter != null) {
				Goods selectedGoods = selectedCounter.getGoods();
				if(event.getCode() == KeyCode.SPACE) {
					if(selectedGoods != null) {
						goodsName.setValue(selectedGoods.getName());
						storeChoise.setValue(counterTableView.getSelectionModel().getSelectedItem().getStoreName());
						String priceString = selectedGoods.getPrice() == null ? "" : String.valueOf(selectedGoods.getPrice());
						goodsPrice.setText(priceString);
						selectionModel.select(salesTab);
					}
				} else if(event.getCode() == KeyCode.A) {
					analogsMode = !analogsMode;
					new Thread(fillContainerTableTask(0)).start();
				}
			}
		});
		searchTextField.textProperty().addListener((observableValue, s, s2) -> {
			if(s2.length() >= 3 || s2.isEmpty()) {
				new Thread(fillContainerTableTask(0)).start();
			}
		});
		counterTableView.setOnMouseClicked(event -> selectedCounter = counterTableView.getSelectionModel().getSelectedItem());
		counterTableView.setOnSort(event -> {
			if(event.getSource().getSortOrder().size() == 1 && COUNT_ID.equals(event.getSource().getSortOrder().get(0).getId())) {
				return;
			}
			new Thread(fillContainerTableTask(0)).start();
		});
	}

	private void initializeGoods() {
		goodsName.getEditor().textProperty().addListener((observableValue, old, newVal) -> {
			if(newVal != null && (newVal.length() >= 3 || newVal.isEmpty())) {
				List<Goods> goodsList1 = new ArrayList<>(goodsService.searchGoods(newVal, new PageRequest(0, 15)));
				ObservableList<String> goodNames = FXCollections.observableArrayList();
				goodNames.addAll(goodsList1.stream().map(Goods::getName).collect(Collectors.toList()));
				goodsName.setItems(goodNames);
				new AutoCompleteComboBoxListener<>(goodsName);
			}
		});
	}

	private void initializeStores() {
		defaultStore = storeService.getDefault();
		ObservableList<String> storesList = FXCollections.observableArrayList(storeService.getAllStoresNames());
		storesList.add(ALL_STORES);
		storeChoise.setItems(storesList);
		containerChoice.setItems(storesList);
		if(defaultStore != null) {
			storeChoise.setValue(defaultStore.getName());
			containerChoice.setValue(defaultStore.getName());
		} else {
			storeChoise.setValue(ALL_STORES);
			containerChoice.setValue(ALL_STORES);
		}
		storeFilterChoice.setItems(storesList);
		containerChoice.getSelectionModel().selectedItemProperty()
				.addListener((observableValue, s, s2) -> new Thread(fillContainerTableTask(0)).start());
	}

	private void initializeDatePickers() {
		fromLabel.setText("З");
		toLabel.setText("По");
		fromDatePicker = new DatePicker(LocalDate.now());
		fromDatePicker.getStylesheets().add("/styles/DatePicker.css");

		toDatePicker = new DatePicker(LocalDate.now());
		toDatePicker.getStylesheets().add("/styles/DatePicker.css");

		filterVBox.getChildren().add(fromLabel);
		filterVBox.getChildren().add(fromDatePicker);
		filterVBox.getChildren().add(toLabel);
		filterVBox.getChildren().add(toDatePicker);
	}

	@FXML
	public void showAddStage() {
		Goods goods = new Goods();
		boolean okClicked = mainApp.showGoodsEditDialog(goods);
		if(okClicked) {
			goods.setName(goods.getName().replaceAll("\\s+", " "));
			goodsService.addGoods(goods);
		}
	}

	@FXML
	protected void saleGoods() {
		Store store = storeService.getStoreByName(storeChoise.getValue());
		goodsValidLabel.setText("");
		countValidLabel.setText("");
		boolean name = Validator.textFieldNotEmpty(goodsName.getEditor(), goodsValidLabel, "Заповніть поле");
		boolean cnt = Validator.textFieldNotEmpty(goodsCount, countValidLabel, "Заповніть поле");
		if(!name || !cnt) {
			return;
		}
		String goodsNameStr = goodsName.getValue();
		int count;
		Double price;
		try {
			count = Integer.parseInt(goodsCount.getText());
			price = StringUtils.isBlank(goodsPrice.getText()) ? null : Double.valueOf(goodsPrice.getText());
		} catch(Exception e) {
			log.error(e.getMessage());
			countValidLabel.setText("Введіть число!");
			return;
		}
		Goods goods = goodsService.getGoodsByName(goodsNameStr);
		if(goods == null) {
			infoLabel.setText("Товар не знайдено!");
			return;
		}

		log.debug("Sale " + goods + " " + store);

		int countLeft;
		infoLabel.setText(goods.toString());
		try {
			countLeft = counterService.sale(goods, store, count, price);
		} catch(RuntimeException e) {
			infoLabel.setText("Не вистачає кількості одиниць товару для продажу! Або виникла непередбачувана помилка.");
			return;
		}

		goodsName.setValue("");
		goodsCount.setText("");
		infoLabel.setText("Продано: " + goodsNameStr + " Кількість: " + count + " Залишилось: " + countLeft);

		commodityList();
	}

	@FXML
	protected void cancelSale() {
		goodsName.setValue("");
		goodsCount.setText("");
	}

	@FXML
	protected void loadStoresClick() {
		log.debug("Load stores...");
	}

	@FXML
	protected void salesSelected() {
		log.debug("Sales...");
		commodityList();
	}

	@FXML
	protected void goodsSelected(Event event) {
		if(mainTabPane.getSelectionModel().getSelectedItem().equals(goodsTab)) {
			log.debug("Load GOODS...");
			goodsList.clear();
			Pageable pageable = new PageRequest(0, PAGE_SIZE, new Sort(Sort.Direction.ASC, "id"));
			Page<Goods> goodsPage = goodsService.getGoodsPage(pageable);
			List<Goods> goods = new ArrayList<>(goodsPage.getContent());
			goodsList = FXCollections.observableList(goods);
			goodsTable.setItems(goodsList);
			goodsFilterChoice.setItems(FXCollections.observableArrayList(goods.stream().map(Goods::getName).collect(Collectors.toList())));
			ScrollBar bar = getVerticalScrollbar(goodsTable);
			bar.valueProperty().addListener(this::scrolledGoods);

		}
	}

	private void scrolledGoods(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		double value = newValue.doubleValue();
		log.debug("Scrolled to {}", value);
		ScrollBar bar = getVerticalScrollbar(goodsTable);
		if(value == bar.getMax()) {
			System.out.println("Adding new persons.");
			int page = goodsList.size() / PAGE_SIZE;
			double targetValue = value * goodsList.size();

			Pageable pageable = new PageRequest(page, PAGE_SIZE, new Sort(Sort.Direction.ASC, "id"));
			Page<Goods> goodsPage = goodsService.getGoodsPage(pageable);
			List<Goods> goods = new ArrayList<>(goodsPage.getContent());
			goodsList.addAll(FXCollections.observableList(goods));
			bar.setValue(targetValue / goodsList.size());
		}
	}

	private ScrollBar getVerticalScrollbar(TableView<?> table) {
		ScrollBar result = null;
		for(Node n : table.lookupAll(".scroll-bar")) {
			if(n instanceof ScrollBar) {
				ScrollBar bar = (ScrollBar) n;
				if(bar.getOrientation().equals(Orientation.VERTICAL)) {
					result = bar;
				}
			}
		}
		return result;
	}

	private void initGoodsTableFields() {
		goodsColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		goodsColumnDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
		goodsColumnTotalItems.setCellValueFactory(new PropertyValueFactory<>("totalItems"));
		goodsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	@FXML
	protected void containerSelected() {
		if(selectionModel.getSelectedItem().equals(containerTab)) {
			log.debug("Containers...");
			analogsMode = false;
			stateLabel.setText("");
			ScrollBar bar = getVerticalScrollbar(counterTableView);
			bar.valueProperty().addListener(this::scrolledCounters);
			new Thread(fillContainerTableTask(0)).start();
		}
	}

	private void scrolledCounters(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
		double value = newValue.doubleValue();
		log.debug("Scrolled to {}", value);
		ScrollBar bar = getVerticalScrollbar(counterTableView);
		if(value == bar.getMax()) {
			int page = goodsFullList.size() / PAGE_SIZE;
			double targetValue = value * goodsFullList.size();
			fillContainerTable(page);
			bar.setValue(targetValue / goodsFullList.size());
		}
	}

	private boolean fillContainerTable(int page) {
		Platform.runLater(() -> stateLabel.setText(""));
		if(page == 0) {
			goodsFullList.clear();
		}
		ObservableList<TableColumn<Counter, ?>> sortOrder = counterTableView.getSortOrder();
		Sort sort = null;
		for(TableColumn tableColumn : sortOrder) {
			Sort.Direction direction = Sort.Direction.ASC;
			TableColumn.SortType sortType = tableColumn.getSortType();
			String sortColumn = "id";
			if(GOODS_NAME_ID.equals(tableColumn.getId())) {
				sortColumn = "goods.name";
			} else if(STORE_NAME_ID.equals(tableColumn.getId())) {
				sortColumn = "store.name";
			}
			if(sortType == TableColumn.SortType.DESCENDING) {
				direction = Sort.Direction.DESC;
			}
			if(sort == null) {
				sort = new Sort(direction, sortColumn);
			} else {
				sort.and(new Sort(direction, sortColumn));
			}
		}

		Pageable pageable = new PageRequest(page, PAGE_SIZE, sort);
		String container = containerChoice.getValue();
		List<Counter> counters = new ArrayList<>();
		if(analogsMode || StringUtils.isNoneEmpty(searchTextField.getText())) {
			List<Goods> goods = new ArrayList<>();
			if(analogsMode) {
				log.info("Analogs called...");
				Platform.runLater(() -> stateLabel.setText("Аналоги для " + selectedCounter.getGoods().getName()));
				goods.addAll(goodsService.getGoodsAnalogs(selectedCounter.getGoods()));
				findCountersByGoods(goods, counters, pageable);
			} else {
				if(ALL_STORES.equals(containerChoice.getValue())) {
					counters.addAll(counterService.getCountersByGoods(searchTextField.getText(), pageable).getContent());
				} else {
					Store store = storeService.getStoreByName(containerChoice.getValue());
					counters.addAll(counterService.getCountersByGoodsAndStore(searchTextField.getText(), store, pageable).getContent());
				}
			}
		} else {
			log.info("Base mode...");
			stateLabel.setText("");
			if(ALL_STORES.equals(container)) {
				counters.addAll(counterService.getCountersPage(pageable).getContent());
			} else {
				Store store = storeService.getStoreByName(container);
				counters.addAll(counterService.getCountersListByStore(store, pageable).getContent());
			}
		}
		goodsFullList.addAll(counters);
		counterTableView.setItems(goodsFullList);
		counterTableView.setDisable(false);
		return true;
	}

	private Task<Boolean> fillContainerTableTask(int page) {
		addProgressIndicator(tableStackPane, counterTableView);
		return new Task<Boolean>() {

			@Override
			protected Boolean call() throws Exception {
				boolean result = fillContainerTable(page);
				Platform.runLater(() -> removeProgressIndicator(tableStackPane));
				return result;
			}
		};
	}

	private void addProgressIndicator(StackPane pane, Control controlToDisable) {
		ProgressIndicator pi = new ProgressIndicator();
		pi.setMaxHeight(40);
		pi.setId(TABLE_PROGRESS_INDICATOR);
		controlToDisable.setDisable(true);
		pane.getChildren().add(pi);
	}

	private void removeProgressIndicator(StackPane pane) {
		pane.getChildren().removeIf(node -> TABLE_PROGRESS_INDICATOR.equals(node.getId()));
	}

	private void initCounterTableFields() {
		goodsCounterColumnName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGoods().getName()));
		goodsCounterColumnName.setId(GOODS_NAME_ID);
		goodsCounterColumnContainer.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStore().getName()));
		goodsCounterColumnContainer.setId(STORE_NAME_ID);
		goodsCounterColumnC.setCellValueFactory(new PropertyValueFactory<>(COUNT_ID));
		goodsCounterColumnC.setId(COUNT_ID);
		goodsCounterPriceColumn.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getGoods().getPrice()));
		goodsCounterPriceColumn.setId("goods.price");
		counterTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	@FXML
	protected void getInfo() {
		String goodsNameStr = goodsName.getValue();
		Goods goods = goodsService.getGoodsByName(goodsNameStr);
		if(goods != null && goods.getId() != 0) {
			infoLabel.setText(goods.toString());
		} else {
			infoLabel.setText("Товар не знайдено!");
		}
	}

	private void commodityList() {
		List<CommodityCirculation> circulations = new ArrayList<>();
		circulations.addAll(circulationsService.getTodaySales());
		ObservableList<CommodityCirculation> circulationsList = FXCollections.observableList(circulations);
		commodityCirculationTable.setItems(circulationsList);
	}

	private void initCommodityTable() {
		commodityCirculationColumnName.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGoods().getName()));
		commodityCirculationColumnCount.setCellValueFactory(new PropertyValueFactory<>(COUNT_ID));
		commodityCirculationColumnContainer.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStore().getName()));
		commodityCirculationTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	@FXML
	protected void clickSales() {
		commodityList();
	}

	@Deprecated
	@FXML
	protected void onLoad() {
		FileChooser fileChooser = new FileChooser();
		ExtensionFilter filter = new ExtensionFilter("MS Office Excel files", "*.xls", "*.xlsx");
		fileChooser.getExtensionFilters().add(filter);
		File file = fileChooser.showOpenDialog(primaryStage);
		List<Goods> goodsList = new ArrayList<>();
		if(file != null) {
			goodsService.addGoodsList(goodsList);
		}
	}

	@FXML
	protected void showAddGoodsStage() {
		boolean okClicked = mainApp.showCounterEditDialog(analogsMode, selectedCounter);
		if(okClicked) {
			new Thread(fillContainerTableTask(0)).start();
		}
	}

	@FXML
	protected void onLoadCounters() {
		FileChooser fileChooser = new FileChooser();
		ExtensionFilter filter = new ExtensionFilter("MS Office Excell files", "*.xls", "*.xlsx");
		fileChooser.getExtensionFilters().add(filter);
		File file = fileChooser.showOpenDialog(primaryStage);

		addCount.setDisable(true);
		importCount.setDisable(true);
		exportCount.setDisable(true);
		Task importTask = importCounters(file);
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

	@FXML
	protected void addContainer() {
		boolean okClicked = mainApp.showStoreAddDialog();
		if(okClicked) {
			containerChoice.getItems().clear();
			storeChoise.getItems().clear();
			List<Store> allStores = storeService.getAllStores();
			storeChoise.getItems().addAll(allStores.stream().map(Store::getName).collect(Collectors.toList()));
			containerChoice.getItems().addAll(allStores.stream().map(Store::getName).collect(Collectors.toList()));
			defaultStore = null;
			containerChoice.setValue(ALL_STORES);
			allStores.stream().filter(Store::isDefaultStore).forEach(store -> {
				defaultStore = store;
				containerChoice.setValue(store.getName());
			});
		}
	}

	@FXML
	protected void setAsDefault() {
		if(containerChoice.getValue().equals(ALL_STORES)) {
			showWarningDialog(primaryStage, "", "Не можна встановити всі контейнери за замовчуванням!");
			return;
		}
		defaultStore = storeService.changeDefault(containerChoice.getValue());
		showInformationDialog(primaryStage, "", "Контейнер встановлено по замовчуванню");
	}

	private void showInformationDialog(Stage primaryStage, String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.initOwner(primaryStage);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@FXML
	protected void createReport() {
		LocalDate fromDate = fromDatePicker.getValue();
		LocalDate toDate = toDatePicker.getValue();
		String storeName = storeFilterChoice.getValue();
		String goodsName = goodsFilterChoice.getValue();

		Store filterStore = storeName != null ? storeService.getStoreByName(storeName) : null;
		Goods filterGoods = goodsName != null ? goodsService.getGoodsByName(goodsName) : null;
		List<CommodityCirculation> commodityCirculationsReport = new ArrayList<>();
		if(incomeOnlyCheck.isSelected() && !salesOnlyCheck.isSelected()) {
			commodityCirculationsReport
					.addAll(circulationsService.commodityCirculationsByTerm(Date.from(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
							Date.from(toDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), filterStore, filterGoods, false));
		} else if(!incomeOnlyCheck.isSelected() && salesOnlyCheck.isSelected()) {
			commodityCirculationsReport
					.addAll(circulationsService.commodityCirculationsByTerm(Date.from(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
							Date.from(toDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), filterStore, filterGoods, true));
		} else {
			commodityCirculationsReport
					.addAll(circulationsService.commodityCirculationsByTerm(Date.from(fromDate.atStartOfDay(ZoneId.systemDefault()).toInstant()),
							Date.from(toDate.atStartOfDay(ZoneId.systemDefault()).toInstant()), filterStore, filterGoods));
		}
		if(commodityCirculationsReport.size() > 0) {
			fillRepotrTable(commodityCirculationsReport);
		} else {
			reportTableView.setItems(null);
		}
	}

	private void fillRepotrTable(List<CommodityCirculation> circulations) {
		ObservableList<CommodityCirculation> circulationsReportList = FXCollections.observableArrayList(circulations);
		reportTableView.setItems(circulationsReportList);
	}

	private void initReportTable() {
		goodsReportColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getGoods().getName()));
		countReportColumn.setCellValueFactory(new PropertyValueFactory<>(COUNT_ID));
		storeReportColumn.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().getStore().getName()));
		dateReportColumn.setCellValueFactory(new PropertyValueFactory<>(DATE_ID));
		saleReportColumn.setCellValueFactory(new PropertyValueFactory<>("saleProp"));

		reportTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	@FXML
	protected void exportReport() {
		goodsReportColumn.setSortType(TableColumn.SortType.ASCENDING);
		ArrayList<CommodityCirculation> report = new ArrayList<>(reportTableView.getItems());
		Map<Integer, ArrayList<Object>> listMap = new HashMap<>();
		for(CommodityCirculation circulation : report) {
			ArrayList<Object> objects = new ArrayList<>();
			objects.add(circulation.getGoodsName());
			objects.add(circulation.getCount());
			objects.add(circulation.getStoreName());
			objects.add(circulation.getDate());
			objects.add(circulation.getSaleProp());
			listMap.put(report.indexOf(circulation), objects);
		}

		boolean result = WorkWithExcel.writeToExcell(listMap);
		if(result) {
			showInformationDialog(primaryStage, "", "Звіт успішно експортовано в папку з програмою");
		} else {
			showErrorDialog(primaryStage, "", "Виникла помилка при експортуванні звіту! Зверніться до розробника");
		}
	}

	private void showErrorDialog(Stage primaryStage, String title, String message) {
		Alert alert = new Alert(Alert.AlertType.ERROR);
		alert.initOwner(primaryStage);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@FXML
	protected void handleDeleteGoods() {
		int selectedIndex = goodsTable.getSelectionModel().getSelectedIndex();
		if(selectedIndex >= 0) {
			Optional<ButtonType> response = showConfirmDialog(primaryStage, "Ви дійсно бажаєте видалити товар? Дію не можливо буде повернути!"
					+ goodsTable.getSelectionModel().selectedItemProperty().get().getName(), "Видалити товар", "Видалення");
			if(response.isPresent() && ButtonType.OK == response.get()) {
				goodsService.remove(goodsTable.getSelectionModel().getSelectedItem());
				goodsTable.getItems().remove(selectedIndex);
			}
		} else {
			showWarningDialog(primaryStage, "Виберіть товар для видалення.", "Не вибрано товару");
		}
	}

	private Optional<ButtonType> showConfirmDialog(Stage primaryStage, String message, String header, String title) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.initOwner(primaryStage);
		alert.setHeaderText(header);
		alert.setTitle(title);
		alert.setContentText(message);
		return alert.showAndWait();
	}

	private void showWarningDialog(Stage primaryStage, String title, String message) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.initOwner(primaryStage);
		alert.setTitle(title);
		alert.setContentText(message);
		alert.showAndWait();
	}

	@FXML
	protected void handleEditGoods() {
		Goods selectedGoods = goodsTable.getSelectionModel().getSelectedItem();
		if(selectedGoods != null) {
			boolean okClicked = mainApp.showGoodsEditDialog(selectedGoods);
			if(okClicked) {
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
		// listMap.put(-1, new ArrayList<Object>(Arrays.asList("Товар" , "Кількість", "Контейнер", "Дата", "Продаж")));
		for(Counter counter : counterArrayList) {
			ArrayList<Object> objects = new ArrayList<>();
			objects.add(counter.getGoodsName());
			objects.add(counter.getStoreName());
			objects.add(counter.getCount());
			listMap.put(counterArrayList.indexOf(counter), objects);
		}

		boolean result = WorkWithExcel.writeToExcell(listMap);
		if(result) {
			showInformationDialog(primaryStage, "", "Дані успішно експортовано в папку з програмою");
		} else {
			showErrorDialog(primaryStage, "", "Виникла помилка при експортуванні звіту! Зверніться до розробника");
		}
	}

	/**
	 *
	 */
	@FXML
	protected void showCopyright() {
		String s = System.getProperty("line.separator");
		showInformationDialog(primaryStage, "Про програму", "Програма для ведення обліку товару." + s + "Розробник Юрій Михалецький" + s
				+ "email:yurik.my@gmail.com" + s + "All rights reserved © Yurembo 2014-2017.");
	}

	private Task importCounters(final File file) {
		return new Task() {

			@Override
			protected Object call() throws Exception {
				progressBar.setVisible(true);
				progressIndicator.setVisible(true);
				updateProgress(0, 1);
				if(file != null) {
					updateMessage("Зчитування файлу...");
					List<GoodsDTO> data = WorkWithExcel.readFromExcell(file);
					int size = data.size();
					updateProgress(0, size);
					updateMessage("Завантаження тоару...");
					Map<String, Store> storeCache = new HashMap<>();
					int i = 0;
					for(GoodsDTO goodsDTO : data) {
						log.debug("Saving {}", goodsDTO);
						if(goodsDTO.getName() == null || goodsDTO.getName().trim().isEmpty()) {
							continue;
						}
						Store store = storeCache.get(goodsDTO.getStore());
						if(store == null) {
							store = storeService.getStoreByName(goodsDTO.getStore());
							storeCache.put(goodsDTO.getStore(), store);
						}
						Counter counter = null;
						Goods goods = goodsService.getGoodsByName(goodsDTO.getName());
						if(goods == null) {
							goods = new Goods();
							goods.setName(goodsDTO.getName().replaceAll("\\s+", " "));
							goods.setDescription(goodsDTO.getDescription());
							goods.setAnalogousType(goodsDTO.getAnalogousType());
							goodsService.addGoods(goods);
						} else {
							counter = counterService.getCounterByGoodsStore(goods, store);
						}

						if(counter == null) {
							counter = new Counter();
						}
						if(goodsDTO.getCount() == null) {
							goodsDTO.setCount(0);
						}
						counter.setCount(counter.getCount() + goodsDTO.getCount());
						counter.setGoods(goods);
						counter.setStore(store);
						counterService.addOrUpdateCounter(counter);

						CommodityCirculation circulation = new CommodityCirculation();

						circulation.setCount(goodsDTO.getCount());
						circulation.setDate(new Date());
						circulation.setGoods(goods);
						circulation.setStore(store);
						circulation.setSale(false);
						circulationsService.addCirculation(circulation);
						updateProgress(++i, data.size());
						updateMessage("Завантаження тоару..." + i + "/" + data.size());
					}
					updateMessage("Виведення таблиці...");
				}
				fillContainerTable(0);
				updateProgress(1, 1);
				importCount.setDisable(false);
				exportCount.setDisable(false);
				addCount.setDisable(false);
				updateMessage("");
				progressBar.setVisible(false);
				progressIndicator.setVisible(false);
				return true;
			}
		};
	}

	private void findCountersByGoods(List<Goods> goods, List<Counter> counters, Pageable pageable) {
		if(ALL_STORES.equals(containerChoice.getValue())) {
			counters.addAll(counterService.searchCountersByGoods(goods, pageable).getContent());
		} else {
			Store store = storeService.getStoreByName(containerChoice.getValue());
			counters.addAll(counterService.searchCountersByGoodsAndStore(goods, store, pageable).getContent());
		}
	}

	@FXML
	protected void contextSaleClick() {
		goodsName.setValue(counterTableView.getSelectionModel().getSelectedItem().getGoods().getName());
		selectionModel.select(salesTab);
	}

	@FXML
	protected void formatGoods() {
		mainTabPane.setDisable(true);
		Task formatDb = formatDb();
		progressBar.progressProperty().unbind();
		progressIndicator.progressProperty().unbind();
		statusLabel.textProperty().unbind();
		progressBar.setProgress(0);
		progressIndicator.setProgress(0);
		progressBar.progressProperty().bind(formatDb.progressProperty());
		progressIndicator.progressProperty().bind(formatDb.progressProperty());
		statusLabel.textProperty().bind(formatDb.messageProperty());
		new Thread(formatDb).start();
	}

	@FXML
	protected void formatReports() {
		mainTabPane.setDisable(true);
		Task formatReports = formatDbReports();
		progressBar.progressProperty().unbind();
		progressIndicator.progressProperty().unbind();
		statusLabel.textProperty().unbind();
		progressBar.setProgress(0);
		progressIndicator.setProgress(0);
		progressBar.progressProperty().bind(formatReports.progressProperty());
		progressIndicator.progressProperty().bind(formatReports.progressProperty());
		statusLabel.textProperty().bind(formatReports.messageProperty());
		new Thread(formatReports).start();
	}

	private Task formatDb() {
		return new Task() {

			@Override
			protected Object call() throws Exception {
				updateProgress(0.05, 1);
				updateMessage("Зчитування товару...");
				List<Goods> goodses = new ArrayList<>(goodsService.getAllGoods());
				updateMessage("Форматування...");
				updateProgress(0.1, 1);
				for(Goods goods : goodses) {
					goods.setName(goods.getName().replaceAll("\\s+", " "));
				}
				updateProgress(0.3, 1);
				updateMessage("Занесення в базу...");
				goodsService.addGoodsList(goodses);
				Platform.runLater(() -> showInformationDialog(primaryStage, "Готово", "Товар відформатовано успішно!"));
				updateMessage("Готово...");
				// {
				// Platform.runLater(() -> showErrorDialog(primaryStage, "Помилка!",
				// "Помилка при форматуванні! Спробуйте спочатку видалити всі повтори товару вручну"));
				// updateMessage("Помилка!");
				// }
				updateProgress(1, 1);
				mainTabPane.setDisable(false);
				return true;
			}
		};
	}

	private Task formatDbReports() {
		return new Task() {

			@Override
			protected Object call() throws Exception {
				updateProgress(0.05, 1);
				updateMessage("Зчитування товару...");
				circulationsService.removeZeroCirculations();
				updateProgress(1, 1);
				updateMessage("");
				mainTabPane.setDisable(false);
				return true;
			}
		};
	}

	@FXML
	protected void setIdentityClick() {
		Goods selectedGoods = counterTableView.getSelectionModel().getSelectedItem().getGoods();
		if(selectedGoods != null) {
			boolean okClicked = mainApp.showSetIdentityDialog(selectedGoods);
			if(okClicked) {
				goodsService.addGoods(selectedGoods);
				showInformationDialog(primaryStage, "", "Відповідність встановлено");
			}
		}
	}
}
