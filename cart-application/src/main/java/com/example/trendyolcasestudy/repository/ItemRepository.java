    package com.example.trendyolcasestudy.repository;

    import com.example.trendyolcasestudy.model.item.DefaultItem;
    import com.example.trendyolcasestudy.model.item.Item;
    import jakarta.transaction.Transactional;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Modifying;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.stereotype.Repository;

    import java.util.List;
    import java.util.Optional;

    @Repository
    public interface ItemRepository extends JpaRepository<Item, Integer> {
        @Query("SELECT i FROM DefaultItem i WHERE i.itemId = ?1")
        List<DefaultItem> findItemByItemIdforDelete(int itemId);


    }
