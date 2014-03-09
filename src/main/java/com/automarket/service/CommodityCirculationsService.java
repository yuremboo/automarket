package com.automarket.service;

import java.util.ArrayList;
import java.util.List;

import com.automarket.entity.CommodityCirculation;

public interface CommodityCirculationsService {
	void addCirculation(CommodityCirculation commodityCirculation);
	void addCirculations(ArrayList<CommodityCirculation> circulations);
	List<CommodityCirculation> commodityCirculations();
	List<CommodityCirculation> commodityCirculationsByDay();
	List<CommodityCirculation> commodityCirculationsByMonth();
}
