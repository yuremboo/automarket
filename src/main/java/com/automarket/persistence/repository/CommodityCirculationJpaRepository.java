package com.automarket.persistence.repository;

import com.automarket.entity.CommodityCirculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface CommodityCirculationJpaRepository extends JpaRepository<CommodityCirculation, Long> {
	List<CommodityCirculation> findAllByDateBetween(Date startDate, Date endDateTime);

	List<CommodityCirculation> findAllByDateBetweenAndSale(Date time, Date time1, boolean isSale);
}
