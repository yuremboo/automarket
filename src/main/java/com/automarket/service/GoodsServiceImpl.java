package com.automarket.service;

import java.util.ArrayList;
import java.util.List;

import com.automarket.DAO.GoodsDAO;
import com.automarket.DAO.GoodsDAOImpl;
import com.automarket.entity.Goods;

public class GoodsServiceImpl implements GoodsService {
	
	private GoodsDAO goodsDAO = new GoodsDAOImpl();

	@Override
	public byte addGoods(Goods goods) {
		return goodsDAO.addGoods(goods);
	}
	
	@Override
	public byte addGoodsList(List<Goods> goods) {
		return goodsDAO.addGoodsList(goods);
	}

	@Override
	public void remove(Goods goods) {
		goodsDAO.remove(goods);
	}

	@Override
	public List<Goods> getAllGoods() {
		return goodsDAO.getAllGoods();
	}

	@Override
	public Goods getGoodsByName(String name) {
		return goodsDAO.getGoodsByName(name);
	}

    @Override
    public List<Goods> searchGoods(String text) {
        return goodsDAO.searchGoods(text);
    }

    @Override
    public List<String> getAllGoodsNames() {
        List<Goods> goodsList = new ArrayList<>();
        goodsList.addAll(goodsDAO.getAllGoods());
        List<String> goodsNames = new ArrayList<>();
        for (Goods goods : goodsList) {
            goodsNames.add(goods.getName());
        }
        return goodsNames;
    }

    @Override
    public Integer getMaxIdentity() {
        return goodsDAO.getMaxIdentity();
    }

}
