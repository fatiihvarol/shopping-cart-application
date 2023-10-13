    package com.example.trendyolcasestudy.model.item;

    import jakarta.persistence.DiscriminatorValue;
    import jakarta.persistence.Entity;
    import jakarta.persistence.Table;

    @Entity
    @Table(name = "digital_items")
    @DiscriminatorValue("Digital")
    public class DigitalItem extends Item {


        public DigitalItem(int itemId, int categoryId, int sellerId, double price, int quantity) {
            super(itemId, categoryId, sellerId, price, quantity);
        }

        public DigitalItem() {

        }




    }
