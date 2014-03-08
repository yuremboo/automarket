package com.automarket.DAO;

import java.util.List;

import com.automarket.entity.Store;

public interface StoreDAO {
	void addStore(Store store);
	void remove(int id);
	List<Store> getAllStores();
	Store getStoreByName(String name);
	Store getDefault();
}
