package com.automarket.service;

import java.util.ArrayList;
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
	
	@Override
	public List<String> getAllStoresNames() {
		List<Store> stores = new ArrayList<>();
		stores.addAll(storeDAO.getAllStores());
		List<String> storeNames = new ArrayList<>();
		for (Store store : stores) {
			storeNames.add(store.getName());
		}
		return storeNames;
	}

    @Override
    public void changeDefault(Store oldDefault, Store newDefault) {
        storeDAO.changeDefault(oldDefault, newDefault);
    }

}
