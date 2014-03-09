package com.automarket.service;

import java.util.ArrayList;
import java.util.List;

import com.automarket.DAO.CommodityCirculationDAO;
import com.automarket.DAO.CommodityCirculationDAOImpl;
import com.automarket.entity.CommodityCirculation;

public class CommodityCirculationsServiceImpl implements
		CommodityCirculationsService {
	
	private CommodityCirculationDAO circulationDAO = new CommodityCirculationDAOImpl();

	@Override
	public void addCirculation(CommodityCirculation commodityCirculation) {
		circulationDAO.addCirculation(commodityCirculation);
	}

	@Override
	public void addCirculations(ArrayList<CommodityCirculation> circulations) {
		circulationDAO.addCirculations(circulations);
	}

	@Override
	public List<CommodityCirculation> commodityCirculations() {
		return circulationDAO.commodityCirculations();
	}

	@Override
	public List<CommodityCirculation> commodityCirculationsByDay() {
		return circulationDAO.commodityCirculationsByDay();
	}

	@Override
	public List<CommodityCirculation> commodityCirculationsByMonth() {
		return circulationDAO.commodityCirculationsByMonth();
	}

}
