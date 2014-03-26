package com.automarket.DAO;

import java.util.List;

import com.automarket.entity.Counter;
import com.automarket.entity.Goods;
import com.automarket.entity.Store;

public interface CounterDAO {
	void addCounter(Counter counter);
	void removeCounter(int id);
	void updateCounter(Counter counter);
	Counter getCounterById(int id);
    Counter getCounterByGoodsStore(Goods goods, Store store);
	List<Counter> getCountersList();
    List<Counter> getCountersListByStore(Store store);
    int sale(Goods goods, Store store, int count);
    void addOrUpdateCounter(Counter counter);
    void addOrUpdateCounterList(List<Counter> counterList);
    List<Counter> searchCountersByGoods(String s);
}
