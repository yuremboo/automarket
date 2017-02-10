package com.automarket.persistence.repository;

import com.automarket.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface GoodsJpaRepository extends JpaRepository<Goods, Long> {
	Goods findOneByName(String name);

	List<Goods> findByNameIgnoreCaseContaining(String text);

	Set<Goods> findAllByAnalogousType(Integer analogousType);

	@Query("SELECT max(g.analogousType) FROM Goods g")
	Integer findMaxAnalogousType();
}
