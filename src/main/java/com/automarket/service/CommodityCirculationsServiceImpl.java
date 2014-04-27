package com.automarket.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.automarket.DAO.CommodityCirculationDAO;
import com.automarket.DAO.CommodityCirculationDAOImpl;
import com.automarket.entity.CommodityCirculation;
import com.automarket.entity.Goods;
import com.automarket.entity.Store;

public class CommodityCirculationsServiceImpl implements
		CommodityCirculationsService {
	
	private CommodityCirculationDAO circulationDAO = new CommodityCirculationDAOImpl();

	@Override
	public void addCirculation(CommodityCirculation commodityCirculation) {
		circulationDAO.addCirculation(commodityCirculation);
	}

	@Override
	public void addCirculations(List<CommodityCirculation> circulations) {
		circulationDAO.addCirculations(circulations);
	}

    @Override
    public void removeZeroCirculations() {
        circulationDAO.removeZeroCirculations();
    }

    @Override
	public List<CommodityCirculation> commodityCirculations() {
		return circulationDAO.commodityCirculations();
	}

	@Override
	public List<CommodityCirculation> commodityCirculationsByDay(boolean b) {
		return circulationDAO.commodityCirculationsByDay(b);
	}

	@Override
	public List<CommodityCirculation> commodityCirculationsByMonth() {
		return circulationDAO.commodityCirculationsByMonth();
	}

    @Override
    public List<CommodityCirculation> commodityCirculationsByTerm(Date fromDate, Date toDate, Store store, Goods goods, Boolean issale) {
        return circulationDAO.commodityCirculationsByTerm(fromDate, toDate, store, goods, issale);
    }

    @Override
    public List<CommodityCirculation> commodityCirculationsByTerm(Date fromDate, Date toDate, Store filterStore, Goods filterGoods) {
        return circulationDAO.commodityCirculationsByTerm(fromDate, toDate, filterStore, filterGoods);
    }
}
