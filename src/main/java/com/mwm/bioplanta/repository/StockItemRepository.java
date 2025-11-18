package com.mwm.bioplanta.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mwm.bioplanta.model.StockItem;

public interface StockItemRepository extends JpaRepository<StockItem, Long> {

}
