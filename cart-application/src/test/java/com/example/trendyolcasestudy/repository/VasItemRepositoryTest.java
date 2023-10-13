package com.example.trendyolcasestudy.repository;

import com.example.trendyolcasestudy.model.item.VasItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application.properties")
class VasItemRepositoryTest {

    @Autowired
    private VasItemRepository vasItemRepository;

    @BeforeEach
    void setUp() {
        vasItemRepository.deleteAll();
    }

    @Test
    void it_should_return_empty_list_when_findItemByItemId_called()  {
        //given

        //when
        Optional<VasItem> itemList = vasItemRepository.findItemByItemId(1);

        //then
        assertTrue(itemList.isEmpty());
    }

    @Test
    void it_should_return_list_when_findItemByItemId_called()  {
        // Given
        VasItem vasItem = new VasItem(1, 1, 1, 1, 1, 1);
        VasItem vasItem2 = new VasItem(3, 3, 3, 3, 3, 3);
        vasItemRepository.saveAll(List.of(vasItem, vasItem2));

        // When
        Optional<VasItem> itemList = vasItemRepository.findItemByItemId(3);

        // Then
        assertTrue(itemList.isPresent(), "itemList should be present for itemId 3");

        VasItem expectedVasItem = itemList.get();
        assertEquals(vasItem2.getItemId(), expectedVasItem.getItemId());

    }

    @Test
    void it_should_return_vasItem_when_findByItemIdAndVasItemId_called()  {
        // Given
        VasItem vasItem = new VasItem(1, 1, 1, 1, 1, 1);
        VasItem vasItem2 = new VasItem(3, 3, 3, 3, 3, 3);
        vasItemRepository.saveAll(List.of(vasItem, vasItem2));

        // When
        VasItem expectedVasItem = vasItemRepository.findByItemIdAndVasItemId(3, 3);

        // Then
        assertEquals(vasItem2.getItemId(), expectedVasItem.getItemId());
        assertEquals(vasItem2.getVasItemId(), expectedVasItem.getVasItemId());
    }

    @Test
    void it_should_return_null_when_findByItemIdAndVasItemId_called()  {
        // Given
        VasItem vasItem = new VasItem(1, 1, 1, 1, 1, 1);
        VasItem vasItem2 = new VasItem(3, 3, 3, 3, 3, 3);
        vasItemRepository.saveAll(List.of(vasItem, vasItem2));

        // When
        VasItem expectedVasItem = vasItemRepository.findByItemIdAndVasItemId(3, 4);

        // Then
        assertNull(expectedVasItem);
    }

}