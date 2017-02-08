package com.automarket.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.automarket.persistence.DAO.GoodsDAO;
import com.automarket.persistence.DAO.GoodsDAOImpl;
import com.automarket.entity.Goods;
import com.automarket.persistence.repository.GoodsJpaRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GoodsServiceImpl implements GoodsService {
	
	private GoodsDAO goodsDAO = new GoodsDAOImpl();
	private final GoodsJpaRepository goodsJpaRepository;

	@Autowired
	public GoodsServiceImpl(GoodsJpaRepository goodsJpaRepository) {
		this.goodsJpaRepository = goodsJpaRepository;
	}

	@Transactional
	@Override
	public Goods addGoods(Goods goods) {
		return goodsJpaRepository.saveAndFlush(goods);
	}
	
	@Override
	public List<Goods> addGoodsList(List<Goods> goods) {
		return goodsJpaRepository.save(goods);
	}

	@Override
	public void remove(Goods goods) {
		goodsJpaRepository.delete(goods);
	}

	@Override
	public List<Goods> getAllGoods() {
		List<Goods> goodsList = goodsJpaRepository.findAll();
		for (Goods goods:goodsList) {
			Hibernate.initialize(goods.getCounters());
		}
		return goodsList;
	}

	@Override
	public Goods getGoodsByName(String name) {
		return goodsJpaRepository.findOneByName(name);
	}

    @Override
    public List<Goods> searchGoods(String text) {
	    if(text == null) {
		    text = "";
	    }
        return goodsJpaRepository.findByNameIgnoreCaseContaining(text);
    }

    @Override
    public List<String> getAllGoodsNames() {
        List<Goods> goodsList = new ArrayList<>();
        goodsList.addAll(goodsJpaRepository.findAll());
	    return goodsList.stream().map(Goods::getName).collect(Collectors.toList());
    }

    @Transactional
	@Override
	public Set<Goods> addAnalogs(Goods goods, Set<Goods> analogs) {
		Goods goodsFromDb = goodsJpaRepository.findOne(goods.getId());
		goodsFromDb.getMyAnalogs().addAll(analogs);
		goodsJpaRepository.saveAndFlush(goodsFromDb);
	    return goodsFromDb.getAllAnalogs();
	}

	@Transactional
	@Override
	public Set<Goods> getGoodsAnalogs(Goods selectedGoods) {
		Goods goodsFromDb = goodsJpaRepository.findOne(selectedGoods.getId());
		Hibernate.initialize(goodsFromDb.getAnalogsToMe());
		Hibernate.initialize(goodsFromDb.getMyAnalogs());
		return goodsFromDb.getAllAnalogs();
	}

	@Transactional
	@Override
	public Page<Goods> getGoodsPage(Pageable pageable) {
		Page<Goods> goodsList = goodsJpaRepository.findAll(pageable);
		for (Goods goods:goodsList) {
			Hibernate.initialize(goods.getCounters());
		}
		return goodsList;
	}

}
