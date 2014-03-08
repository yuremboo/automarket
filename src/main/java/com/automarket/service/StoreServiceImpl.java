package com.automarket.service;

import java.util.List;

import com.automarket.DAO.StoreDAO;
import com.automarket.DAO.StoreDAOImpl;
import com.automarket.entity.Store;

public class StoreServiceImpl implements StoreService {

	StoreDAO storeDAO = new StoreDAOImpl();
	
	@Override
	public void addStore(Store store) {
		storeDAO.addStore(store);
	}

	@Override
	public void remove(int id) {
		storeDAO.remove(id);
	}

	@Override
	public List<Store> getAllStores() {
		return storeDAO.getAllStores();
	}

	@Override
	public Store getStoreByName(String name) {
		return storeDAO.getStoreByName(name);
	}
	
	@Override
	public Store getDefault() {
		return storeDAO.getDefault();
	}

}
