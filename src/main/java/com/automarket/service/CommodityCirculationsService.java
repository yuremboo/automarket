package com.automarket.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.automarket.entity.CommodityCirculation;
import com.automarket.entity.Goods;
import com.automarket.entity.Store;

public interface CommodityCirculationsService {
	void addCirculation(CommodityCirculation commodityCirculation);
	void addCirculations(ArrayList<CommodityCirculation> circulations);
	List<CommodityCirculation> commodityCirculations();
	List<CommodityCirculation> commodityCirculationsByDay(boolean b);
	List<CommodityCirculation> commodityCirculationsByMonth();
    List<CommodityCirculation> commodityCirculationsByTerm(Date fromDate, Date toDate, Store store, Goods goods, Boolean issale);
    List<CommodityCirculation> commodityCirculationsByTerm(Date fromDate, Date toDate, Store filterStore, Goods filterGoods);
}
