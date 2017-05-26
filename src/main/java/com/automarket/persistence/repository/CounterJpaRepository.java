package com.automarket.persistence.repository;

import com.automarket.entity.Counter;
import com.automarket.entity.Goods;
import com.automarket.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CounterJpaRepository extends JpaRepository<Counter, Integer> {
	Counter findOneByGoodsAndStore(Goods goods, Store store);

	Page<Counter> findAllByStore(Store store, Pageable pageable);

	Page<Counter> findAllByGoodsIn(List<Goods> goodsList, Pageable pageable);

	Page<Counter> findAllByGoodsInAndStore(List<Goods> goods, Store store, Pageable pageable);

	List<Counter> findAllByStore(Store store);

	@Query("select c from Counter c where c.goods.name like %?1%")
	Page<Counter> findByGoodsName(String searchTerm, Pageable pageable);

	@Query("select c from Counter c where c.goods.name like %?1% and c.store = ?2")
	Page<Counter> findByGoodsNameAndStore(String searchTerm, Store store, Pageable pageable);
}
