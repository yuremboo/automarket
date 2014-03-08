package com.automarket.service;

import java.util.List;

import com.automarket.DAO.CounterDAO;
import com.automarket.DAO.CounterDAOImpl;
import com.automarket.entity.Counter;
import com.automarket.entity.Goods;
import com.automarket.entity.Store;

public class CounterServiceImpl implements CounterService {
	
	CounterDAO counterDAO = new CounterDAOImpl();

	@Override
	public void addCounter(Counter counter) {
		// TODO Auto-generated method stub

	}

	@Override
	public void removeCounter(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCounter(Counter counter) {
		// TODO Auto-generated method stub

	}

	@Override
	public Counter getCounterById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Counter> getCountersList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int sale(Goods goods, Store store, int count) {
		return counterDAO.sale(goods, store, count);
	}

}
