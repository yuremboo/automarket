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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class MainController {

	private static final Logger log = LoggerFactory.getLogger(MainController.class);
	private static final String ALL_STORES = "Всі";

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
	}

	private void initializeGoods() {
		containerChoice.getSelectionModel().selectedItemProperty().addListener((observableValue, s, s2) -> fillContainerTable(s2));

		searchTextField.textProperty().addListener((observableValue, s, s2) -> searchField(s2));

		goodsName.getEditor().focusedProperty().addListener((observableValue, aBoolean, aBoolean2) -> {
			if(aBoolean2) {
				List<Goods> goodsList1 = new ArrayList<>(goodsService.searchGoods(goodsName.getValue()));
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
			goods.setName(goods.getName().replaceAll("\\s+", ""));
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
		try {
			count = Integer.parseInt(goodsCount.getText());
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

		infoLabel.setText(goods.toString());
		int countLeft;
		try {
			countLeft = counterService.sale(goods, store, count);
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
			List<Goods> goods = new ArrayList<>(goodsService.getAllGoods());
			goodsList = FXCollections.observableList(goods);
			goodsTable.setItems(goodsList);
			goodsFilterChoice.setItems(FXCollections.observableArrayList(goods.stream().map(Goods::getName).collect(Collectors.toList())));
		}
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
			fillContainerTable(containerChoice.getValue());
		}
	}

	private void fillContainerTable(String storeName) {
		if(storeName == null) {
			storeName = containerChoice.getValue();
		}
		goodsFullList.clear();
		List<Counter> counters = new ArrayList<>();
		if(!storeName.equals(ALL_STORES)) {
			Store store = storeService.getStoreByName(storeName);
			counters.addAll(counterService.getCountersListByStore(store));
		} else {
			counters.addAll(counterService.getCountersList());
		}

		goodsFullList = FXCollections.observableList(counters);
		counterTableView.setItems(goodsFullList);
		counterTableView.setOnKeyPressed(event -> {
			if(event.getCode() == KeyCode.SPACE) {
				Goods selectedGoods = counterTableView.getSelectionModel().getSelectedItem().getGoods();
				if(selectedGoods != null) {
					goodsName.setValue(selectedGoods.getName());
					selectionModel.select(salesTab);
				}
			} else if(event.getCode() == KeyCode.A) {
				Goods selectedGoods = counterTableView.getSelectionModel().getSelectedItem().getGoods();
				analogsMode = !analogsMode;
				if(analogsMode) {
					log.info("Analogs called...");
					Set<Goods> analogs = goodsService.getGoodsAnalogs(selectedGoods);
					List<Counter> analogsCounters = new ArrayList<>();
					if(ALL_STORES.equals(containerChoice.getValue())) {
						analogsCounters.addAll(counterService.searchCountersByGoods(new ArrayList<>(analogs)));
					} else {
						Store store = storeService.getStoreByName(containerChoice.getValue());
						analogsCounters.addAll(counterService.searchCountersByGoodsAndStore(new ArrayList<>(analogs), store));
					}

					goodsFullList = FXCollections.observableList(analogsCounters);
					counterTableView.setItems(goodsFullList);
					counterTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
				} else {
					log.info("Base mode...");
				}
			}
		});
	}

	private void initCounterTableFields() {
		goodsCounterColumnName.setCellValueFactory(new PropertyValueFactory<>("goodsName"));
		goodsCounterColumnContainer.setCellValueFactory(new PropertyValueFactory<>("storeName"));
		goodsCounterColumnC.setCellValueFactory(new PropertyValueFactory<>("count"));
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
		commodityCirculationColumnName.setCellValueFactory(new PropertyValueFactory<>("goodsName"));
		commodityCirculationColumnCount.setCellValueFactory(new PropertyValueFactory<>("count"));
		commodityCirculationColumnContainer.setCellValueFactory(new PropertyValueFactory<>("storeName"));
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
		boolean okClicked = mainApp.showCounterEditDialog();
		if(okClicked) {
			fillContainerTable(containerChoice.getValue());
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
		goodsReportColumn.setCellValueFactory(new PropertyValueFactory<>("goodsName"));
		countReportColumn.setCellValueFactory(new PropertyValueFactory<>("count"));
		storeReportColumn.setCellValueFactory(new PropertyValueFactory<>("storeName"));
		dateReportColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
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
				+ "email:yurik.my@gmail.com" + s + "All rights reserved © Yurembo 2014.");
	}

	private Task importCounters(final File file) {
		return new Task() {

			@Override
			protected Object call() throws Exception {
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
						Goods goods = goodsService.getGoodsByName(goodsDTO.getName());
						if(goods == null) {
							goods = new Goods();
							goods.setName(goodsDTO.getName());
							goods.setDescription(goodsDTO.getDescription());
							goodsService.addGoods(goods);
						}

						Store store = storeCache.get(goodsDTO.getStore());
						if(store == null) {
							store = storeService.getStoreByName(goodsDTO.getStore());
							storeCache.put(goodsDTO.getStore(), store);
						}

						Counter counter = counterService.getCounterByGoodsStore(goods, store);
						if(counter == null) {
							counter = new Counter();
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
						updateProgress(++i, data.size());
					}
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

	private void searchField(String text) {
		goodsFullList.clear();
		List<Goods> goods = goodsService.searchGoods(text);
		if(goods.isEmpty()) {
			return;
		}
		List<Counter> counters = new ArrayList<>();
		if(ALL_STORES.equals(storeChoise.getValue())) {
			counters.addAll(counterService.searchCountersByGoods(goods));
		} else {
			Store store = storeService.getStoreByName(storeChoise.getValue());
			counters.addAll(counterService.searchCountersByGoodsAndStore(goods, store));
		}

		goodsFullList = FXCollections.observableList(counters);
		counterTableView.setItems(goodsFullList);
		counterTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		System.out.println(text);
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
					goods.setName(goods.getName().replaceAll("\\s+", ""));
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
