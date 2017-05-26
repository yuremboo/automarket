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
    Counter getCounterByGoodsStore(Goods goods, Store store);
	List<Counter> getCountersList();
    Page<Counter> searchCountersByGoods(List<Goods> goodsList, Pageable pageable);
    Page<Counter> getCountersListByStore(Store store, Pageable pageable);
    List<Counter> getCountersListByStore(Store store);
	int sale(Goods goods, Store store, int count, Double price);
    Counter addOrUpdateCounter(Counter counter);
    void addOrUpdateCounterList(List<Counter> counterList);

	Page<Counter> searchCountersByGoodsAndStore(List<Goods> goods, Store store, Pageable pageable);

	Page<Counter> getCountersPage(Pageable pageable);

	Page<Counter> getCountersByGoods(String searchTerm, Pageable pageable);

	Page<Counter> getCountersByGoodsAndStore(String searchTerm, Store store, Pageable pageable);
}
