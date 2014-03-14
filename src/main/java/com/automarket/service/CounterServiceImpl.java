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
		counterDAO.addCounter(counter);
	}

	@Override
	public void removeCounter(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateCounter(Counter counter) {
		counterDAO.updateCounter(counter);
	}

	@Override
	public Counter getCounterById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public Counter getCounterByGoodsStore(Goods goods, Store store) {
        return counterDAO.getCounterByGoodsStore(goods, store);
    }

    @Override
	public List<Counter> getCountersList() {
		// TODO Auto-generated method stub
		return null;
	}

    @Override
    public List<Counter> getCountersListByStore(Store store) {
        return counterDAO.getCountersListByStore(store);
    }

    @Override
	public int sale(Goods goods, Store store, int count) {
		return counterDAO.sale(goods, store, count);
	}

    @Override
    public void addOrUpdateCounter(Counter counter) {
        counterDAO.addOrUpdateCounter(counter);
    }

    @Override
    public void addOrUpdateCounterList(List<Counter> counterList) {
        counterDAO.addOrUpdateCounterList(counterList);
    }

}
