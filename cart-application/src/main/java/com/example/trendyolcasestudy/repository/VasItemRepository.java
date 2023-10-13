package com.example.trendyolcasestudy.repository;

import com.example.trendyolcasestudy.model.item.VasItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VasItemRepository extends JpaRepository<VasItem, Integer> {
    @Query("SELECT i FROM VasItem i WHERE i.itemId = ?1")
    Optional<VasItem> findItemByItemId(int itemId);

    @Query("SELECT v FROM VasItem v WHERE v.itemId = :itemId AND v.vasItemId = :vasItemId")
    VasItem findByItemIdAndVasItemId(@Param("itemId") int itemId, @Param("vasItemId") int vasItemId);




}
