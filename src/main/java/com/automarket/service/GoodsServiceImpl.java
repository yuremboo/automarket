package com.automarket.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.automarket.persistence.DAO.GoodsDAO;
import com.automarket.persistence.DAO.GoodsDAOImpl;
import com.automarket.entity.Goods;
import com.automarket.persistence.repository.GoodsJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
		return goodsJpaRepository.findAll();
	}

	@Override
	public Goods getGoodsByName(String name) {
		return goodsJpaRepository.findOneByName(name);
	}

    @Override
    public List<Goods> searchGoods(String text) {
        return goodsDAO.searchGoods(text);
    }

    @Override
    public List<String> getAllGoodsNames() {
        List<Goods> goodsList = new ArrayList<>();
        goodsList.addAll(goodsJpaRepository.findAll());
	    return goodsList.stream().map(Goods::getName).collect(Collectors.toList());
    }

    @Override
    public Integer getMaxIdentity() {
        return goodsDAO.getMaxIdentity();
    }

}
