package com.automarket.service;

import java.util.Collection;
import java.util.List;

import com.automarket.entity.Counter;
import com.automarket.entity.Goods;
import com.automarket.entity.Store;

public interface CounterService {
	void addCounter(Counter counter);
	void removeCounter(int id);
	void updateCounter(Counter counter);
	Counter getCounterById(int id);
    Counter getCounterByGoodsStore(Goods goods, Store store);
	List<Counter> getCountersList();
    List<Counter> searchCountersByGoods(List<Goods> goodsList);
    List<Counter> getCountersListByStore(Store store);
	int sale(Goods goods, Store store, int count);
    Counter addOrUpdateCounter(Counter counter);
    void addOrUpdateCounterList(List<Counter> counterList);

	List<Counter> searchCountersByGoodsAndStore(List<Goods> goods, Store store);
}
