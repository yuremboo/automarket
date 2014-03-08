package com.automarket.service;

import java.util.List;

import com.automarket.entity.Store;

public interface StoreService {
	void addStore(Store store);
	void remove(int id);
	List<Store> getAllStores();
	Store getStoreByName(String name);
	Store getDefault();
}
