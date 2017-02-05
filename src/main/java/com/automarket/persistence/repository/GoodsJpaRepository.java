package com.automarket.persistence.repository;

import com.automarket.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GoodsJpaRepository extends JpaRepository<Goods, Integer> {
	Goods findOneByName(String name);
}
