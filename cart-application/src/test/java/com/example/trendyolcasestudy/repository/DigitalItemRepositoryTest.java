    package com.example.trendyolcasestudy.repository;

    import com.example.trendyolcasestudy.model.item.DigitalItem;
    import com.example.trendyolcasestudy.model.item.Item;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
    import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.springframework.test.annotation.DirtiesContext;
    import org.springframework.test.context.ContextConfiguration;
    import org.springframework.test.context.TestPropertySource;

    import java.util.Optional;

    import static org.junit.jupiter.api.Assertions.*;

    @SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
    @AutoConfigureMockMvc
    @TestPropertySource(locations = "classpath:application.properties")
    class DigitalItemRepositoryTest {

        @Autowired
        private DigitalItemRepository digitalItemRepository;

        @BeforeEach
        void setUp() {
            digitalItemRepository.deleteAll();
        }
        @Test
        void it_should_return_item_when_findItemByItemId_called() {
            //given
           DigitalItem item = new DigitalItem(1, 1, 1, 10, 5);
           DigitalItem item2 = new DigitalItem(3, 3, 3, 100, 5);

            digitalItemRepository.saveAll(
                    java.util.List.of(item, item2)
            );

            //when
            Optional<DigitalItem> optionalDigitalItem = digitalItemRepository.findItemByItemId(1);

            //then
            assertTrue(optionalDigitalItem.isPresent());
        }

        @Test
        void it_should_return_empty_when_findItemByItemId_called() {
            //given

            //when
            Optional<DigitalItem> optionalDigitalItem = digitalItemRepository.findItemByItemId(2);

            //then
            assertFalse(optionalDigitalItem.isPresent());
        }
    }
