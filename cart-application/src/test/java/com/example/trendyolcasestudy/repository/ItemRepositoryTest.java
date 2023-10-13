package com.example.trendyolcasestudy.repository;


import com.example.trendyolcasestudy.model.item.DefaultItem;
import com.example.trendyolcasestudy.model.item.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @BeforeEach
    void setUp() {
        itemRepository.deleteAll();
    }
    @Test
    void it_should_return_empty_list_when_findItemByItemIdforDelete_called()  {
        //given
        Item item = new Item(1, 1, 1, 10, 5);
        Item item2 = new Item(3, 3, 3, 100, 5);

        itemRepository.saveAll(
                java.util.List.of(item, item2)
        );

        //when
        java.util.List<DefaultItem> itemList = itemRepository.findItemByItemIdforDelete(1);

        //then
        Assertions.assertTrue(itemList.isEmpty());
    }

    @Test
    void it_should_return_list_when_findItemByItemIdforDelete_called()  {
        //given
        Item item = new Item(1, 1, 1, 10, 5);
        Item item2 = new Item(3, 3, 3, 100, 5);

        itemRepository.saveAll(
                java.util.List.of(item, item2)
        );

        //when
        java.util.List<DefaultItem> itemList = itemRepository.findItemByItemIdforDelete(3);

        //then
        Assertions.assertTrue(itemList.isEmpty());
    }

}