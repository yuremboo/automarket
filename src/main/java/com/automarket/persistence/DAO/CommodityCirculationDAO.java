package com.automarket.persistence.DAO;

import java.util.Date;
import java.util.List;

import com.automarket.entity.CommodityCirculation;
import com.automarket.entity.Goods;
import com.automarket.entity.Store;

public interface CommodityCirculationDAO {
	void addCirculation(CommodityCirculation commodityCirculation);
	void addCirculations(List<CommodityCirculation> circulations);
	List<CommodityCirculation> commodityCirculations();
	List<CommodityCirculation> commodityCirculationsByDay(boolean b);
	List<CommodityCirculation> commodityCirculationsByMonth();
    List<CommodityCirculation> commodityCirculationsByTerm(Date fromDate, Date toDate, Store store, Goods goods, Boolean issale);
    List<CommodityCirculation> commodityCirculationsByTerm(Date fromDate, Date toDate, Store filterStore, Goods filterGoods);
    void removeZeroCirculations();
}
