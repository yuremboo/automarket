package com.automarket.DAO;

import java.util.ArrayList;
import java.util.List;

import com.automarket.entity.CommodityCirculation;

public interface CommodityCirculationDAO {
	void addCirculation(CommodityCirculation commodityCirculation);
	void addCirculations(ArrayList<CommodityCirculation> circulations);
	List<CommodityCirculation> commodityCirculations();
	List<CommodityCirculation> commodityCirculationsByDay();
	List<CommodityCirculation> commodityCirculationsByMonth();
}
