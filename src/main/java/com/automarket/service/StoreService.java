package com.automarket.service;

import java.util.List;

import javafx.util.Callback;

import com.automarket.entity.Store;

public interface StoreService {
	void addStore(Store store);
	void remove(int id);
	List<Store> getAllStores();
	Store getStoreByName(String name);
	Store getDefault();
	List<String> getAllStoresNames();
    void changeDefault(Store oldDefault, Store newDefault);
}
