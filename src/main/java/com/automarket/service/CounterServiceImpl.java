package com.automarket.service;

import java.util.List;

import com.automarket.entity.CommodityCirculation;
import com.automarket.persistence.DAO.CounterDAO;
import com.automarket.persistence.DAO.CounterDAOImpl;
import com.automarket.entity.Counter;
import com.automarket.entity.Goods;
import com.automarket.entity.Store;
import com.automarket.persistence.repository.CounterJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class CounterServiceImpl implements CounterService {

	private final CommodityCirculationsService circulationsService;

	private final CounterJpaRepository counterJpaRepository;
	private CounterDAO counterDAO = new CounterDAOImpl();

	@Autowired
	public CounterServiceImpl(CounterJpaRepository counterJpaRepository, CommodityCirculationsService circulationsService) {
		this.counterJpaRepository = counterJpaRepository;
		this.circulationsService = circulationsService;
	}

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
        return counterJpaRepository.findOneByGoodsAndStore(goods, store);
    }

    @Override
	public List<Counter> getCountersList() {
		return counterJpaRepository.findAll();
	}

    @Override
    public List<Counter> searchCountersByGoods(List<Goods> goodsList) {
        return counterJpaRepository.findAllByGoodsIn(goodsList);
    }

    @Override
    public Page<Counter> getCountersListByStore(Store store, Pageable pageable) {
	    return counterJpaRepository.findAllByStore(store, pageable);
    }

    @Override
    public List<Counter> getCountersListByStore(Store store) {
	    return counterJpaRepository.findAllByStore(store);
    }

    @Override
	public int sale(Goods goods, Store store, int count) {
	    Counter goodsCount = counterJpaRepository.findOneByGoodsAndStore(goods, store);
	    if(goodsCount.getCount() < count) {
		    throw new RuntimeException("Not enough goods"); //TODO: change
	    }
	    goodsCount.setCount(goodsCount.getCount() - count);
	    counterJpaRepository.saveAndFlush(goodsCount);

	    CommodityCirculation commodityCirculation = new CommodityCirculation(goods, count, store);
	    commodityCirculation.setSale(true);
	    circulationsService.addCirculation(commodityCirculation);

	    return goodsCount.getCount();
	}

    @Override
    public Counter addOrUpdateCounter(Counter counter) {
        return counterJpaRepository.saveAndFlush(counter);
    }

    @Override
    public void addOrUpdateCounterList(List<Counter> counterList) {
        counterDAO.addOrUpdateCounterList(counterList);
    }

	@Override
	public List<Counter> searchCountersByGoodsAndStore(List<Goods> goods, Store store) {
		return counterJpaRepository.findAllByGoodsInAndStore(goods, store);
	}

}
