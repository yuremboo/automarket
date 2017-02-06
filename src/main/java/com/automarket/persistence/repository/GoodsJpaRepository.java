package com.automarket.persistence.repository;

import com.automarket.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GoodsJpaRepository extends JpaRepository<Goods, Long> {
	Goods findOneByName(String name);

	List<Goods> findByNameIgnoreCaseContaining(String text);
}
