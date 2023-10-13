package com.example.trendyolcasestudy.repository;

import com.example.trendyolcasestudy.model.item.DigitalItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DigitalItemRepository extends JpaRepository<DigitalItem, Integer> {
    @Query("SELECT i FROM DigitalItem i WHERE i.itemId = ?1")
    Optional<DigitalItem> findItemByItemId(int itemId);


}
