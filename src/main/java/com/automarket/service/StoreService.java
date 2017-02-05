package com.automarket.service;

import java.util.List;

import com.automarket.entity.Store;

public interface StoreService {
	void addStore(Store store);
	void remove(Integer id);
	List<Store> getAllStores();
	Store getStoreByName(String name);
	Store getDefault();
	List<String> getAllStoresNames();
    Store changeDefault(String newDefault);
}
