package com.automarket.service;

import java.util.List;

import com.automarket.DAO.GoodsDAO;
import com.automarket.DAO.GoodsDAOImpl;
import com.automarket.entity.Goods;

public class GoodsServiceImpl implements GoodsService {
	
	private GoodsDAO goodsDAO = new GoodsDAOImpl();

	@Override
	public void addGoods(Goods goods) {
		goodsDAO.addGoods(goods);
	}
	
	@Override
	public void addGoodsList(List<Goods> goods) {
		goodsDAO.addGoodsList(goods);
	}

	@Override
	public void remove(int id) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<Goods> getAllGoods() {
		return goodsDAO.getAllGoods();
	}

	@Override
	public Goods getGoodsByName(String name) {
		return goodsDAO.getGoodsByName(name);
	}

}
