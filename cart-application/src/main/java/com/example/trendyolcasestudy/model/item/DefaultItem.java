package com.example.trendyolcasestudy.model.item;

import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "default_items")
@DiscriminatorValue("Default")
public  class DefaultItem extends Item {
    @Transient
    private List<VasItem> vasItems = new ArrayList<>();
    public DefaultItem(int itemId, int categoryId, int sellerId, double price, int quantity, List<VasItem> vasItems) {
        super(itemId, categoryId, sellerId, price, quantity);
        this.vasItems = vasItems;

    }

    public DefaultItem() {
    }

    public List<VasItem> getVasItems() {
        return vasItems;
    }

    public void setVasItems(List<VasItem> vasItems) {
        this.vasItems = vasItems;
    }
}