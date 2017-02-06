package com.automarket.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.automarket.persistence.DAO.StoreDAO;
import com.automarket.persistence.DAO.StoreDAOImpl;
import com.automarket.entity.Store;
import com.automarket.persistence.repository.StoreJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StoreServiceImpl implements StoreService {

	private final StoreJpaRepository storeJpaRepository;

	@Autowired
	public StoreServiceImpl(StoreJpaRepository storeJpaRepository) {
		this.storeJpaRepository = storeJpaRepository;
	}

	@Transactional
	@Override
	public void addStore(Store store) {
		if(store.isDefaultStore()) {
			Store oldDefaultStore = storeJpaRepository.findByDefaultStoreTrue();
			if(oldDefaultStore != null) {
				oldDefaultStore.setDefaultStore(false);
				storeJpaRepository.save(oldDefaultStore);
			}
		}
		storeJpaRepository.saveAndFlush(store);
	}

	@Transactional
	@Override
	public void remove(Integer id) {
		storeJpaRepository.delete(id);
	}

	@Transactional
	@Override
	public List<Store> getAllStores() {
		return storeJpaRepository.findAll();
	}

	@Transactional
	@Override
	public Store getStoreByName(String name) {
		return storeJpaRepository.getFirstByName(name);
	}

	@Transactional
	@Override
	public Store getDefault() {
		return storeJpaRepository.findByDefaultStoreTrue();
	}

	@Override
	public List<String> getAllStoresNames() {
		List<Store> stores = new ArrayList<>();
		stores.addAll(storeJpaRepository.findAll());
		return stores.stream().map(Store::getName).collect(Collectors.toList());
	}

	@Transactional
	@Override
	public Store changeDefault(String newDefault) {
		Store activeStore = getDefault();
		activeStore.setDefaultStore(false);
		storeJpaRepository.save(activeStore);
		Store storeByName = getStoreByName(newDefault);
		storeByName.setDefaultStore(true);
		return storeJpaRepository.saveAndFlush(storeByName);
	}

}
