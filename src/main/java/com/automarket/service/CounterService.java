package com.automarket.service;

import java.util.List;

import com.automarket.entity.Counter;
import com.automarket.entity.Goods;
import com.automarket.entity.Store;

public interface CounterService {
	void addCounter(Counter counter);
	void removeCounter(int id);
	void updateCounter(Counter counter);
	Counter getCounterById(int id);
	List<Counter> getCountersList();
    List<Counter> getCountersListByStore(Store store);
	int sale(Goods goods, Store store, int count);
}
