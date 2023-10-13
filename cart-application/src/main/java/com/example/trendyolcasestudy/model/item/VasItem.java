    package com.example.trendyolcasestudy.model.item;


    import jakarta.persistence.*;
    import org.springframework.stereotype.Component;

    @Entity
    @DiscriminatorValue("Vas")
    @Table(name = "vas_items")
    public class VasItem extends Item {

        private    int vasItemId;


        public VasItem(int itemId, int categoryId, int sellerId, double price, int quantity, int vasItemId) {
            super(itemId, categoryId, sellerId, price, quantity);
            this.vasItemId = vasItemId;
        }


        public VasItem() {

        }

        public int getVasItemId() {
            return vasItemId;
        }
    }
