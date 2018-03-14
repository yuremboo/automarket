package com.automarket.service;

import java.util.Calendar;
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

	@Override
	public List<CommodityCirculation> getTodaySales() {
		//TODO: change to LocalDateTime java 8
		Calendar startDate = Calendar.getInstance();
		Calendar endDate = Calendar.getInstance();
		startDate.set(Calendar.HOUR_OF_DAY, 0);
		startDate.set(Calendar.MINUTE, 0);
		startDate.set(Calendar.SECOND, 0);
		return commodityCirculationJpaRepository.findAllByDateBetweenAndSale(startDate.getTime(), endDate.getTime(), true);
	}

	public void someMethod(int[] a ) {
		System.out.println("hello");
	}
}
