package com.automarket.persistence.repository;


import com.automarket.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreJpaRepository extends JpaRepository<Store, Integer> {
	Store findByDefaultStoreTrue();

	Store getFirstByName(String name);
}
