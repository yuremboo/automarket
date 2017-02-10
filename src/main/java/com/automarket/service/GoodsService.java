package com.automarket.service;

import java.util.List;
import java.util.Set;

import com.automarket.entity.Goods;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GoodsService {
	Goods addGoods(Goods goods);
	void remove(Goods goods);
	List<Goods> getAllGoods();
	Goods getGoodsByName(String name);
	List<Goods> addGoodsList(List<Goods> goods);
    List<Goods> searchGoods(String text);
    List<String> getAllGoodsNames();
	Set<Goods> addAnalogs(Goods goods, Set<Goods> analogs);
	Set<Goods> getGoodsAnalogs(Goods selectedGoods);
	Page<Goods> getGoodsPage(Pageable pageable);
}
