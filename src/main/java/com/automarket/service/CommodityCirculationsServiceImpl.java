package com.automarket.service;

import java.util.Date;
import java.util.List;

import com.automarket.persistence.DAO.CommodityCirculationDAO;
import com.automarket.persistence.DAO.CommodityCirculationDAOImpl;
import com.automarket.entity.CommodityCirculation;
import com.automarket.entity.Goods;
import com.automarket.entity.Store;
import com.automarket.persistence.repository.CommodityCirculationJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommodityCirculationsServiceImpl implements CommodityCirculationsService {

	@Autowired
	private CommodityCirculationJpaRepository commodityCirculationJpaRepository;

	private CommodityCirculationDAO circulationDAO = new CommodityCirculationDAOImpl();

	@Override
	public void addCirculation(CommodityCirculation commodityCirculation) {
		commodityCirculationJpaRepository.saveAndFlush(commodityCirculation);
	}

	@Override
	public void addCirculations(List<CommodityCirculation> circulations) {
		commodityCirculationJpaRepository.save(circulations);
	}

	@Override
	public void removeZeroCirculations() {
		circulationDAO.removeZeroCirculations();
	}

	@Override
	public List<CommodityCirculation> commodityCirculations() {
		return commodityCirculationJpaRepository.findAll();
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
