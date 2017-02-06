package com.automarket.persistence.repository;

import com.automarket.entity.CommodityCirculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommodityCirculationJpaRepository extends JpaRepository<CommodityCirculation, Long> {
}
