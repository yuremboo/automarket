package com.automarket.service;

import java.util.List;

import com.automarket.entity.Goods;

public interface GoodsService {
	void addGoods(Goods goods);
	void remove(int id);
	List<Goods> getAllGoods();
	Goods getGoodsByName(String name);
	void addGoodsList(List<Goods> goods);
}
