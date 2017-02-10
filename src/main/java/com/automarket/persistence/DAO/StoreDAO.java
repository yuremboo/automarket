package com.automarket.persistence.DAO;

import java.util.List;

import com.automarket.entity.Store;

@Deprecated
public interface StoreDAO {
	void addStore(Store store);
	void remove(int id);
	List<Store> getAllStores();
	Store getStoreByName(String name);
	Store getDefault();

    void changeDefault(Store oldDefault, Store newDefault);
}
