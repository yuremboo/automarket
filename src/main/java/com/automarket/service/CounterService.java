package com.automarket.service;

import java.util.List;

import com.automarket.entity.Counter;
import com.automarket.entity.Goods;
import com.automarket.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CounterService {
	void addCounter(Counter counter);
	void removeCounter(int id);
	void updateCounter(Counter counter);
	Counter getCounterById(int id);
    Counter getCounterByGoodsStore(Goods goods, Store store);
	List<Counter> getCountersList();
    List<Counter> searchCountersByGoods(List<Goods> goodsList);
    Page<Counter> getCountersListByStore(Store store, Pageable pageable);
    List<Counter> getCountersListByStore(Store store);
	int sale(Goods goods, Store store, int count);
    Counter addOrUpdateCounter(Counter counter);
    void addOrUpdateCounterList(List<Counter> counterList);

	List<Counter> searchCountersByGoodsAndStore(List<Goods> goods, Store store);
}
