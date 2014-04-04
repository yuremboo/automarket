package com.automarket.DAO;

import java.util.List;

import com.automarket.entity.Goods;

public interface GoodsDAO {
	byte addGoods(Goods goods);
	void remove(Goods goods);
	List<Goods> getAllGoods();
	Goods getGoodsByName(String name);
	void addGoodsList(List<Goods> goods);
    List<Goods> searchGoods(String text);
}
