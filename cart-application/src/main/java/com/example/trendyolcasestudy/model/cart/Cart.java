package com.example.trendyolcasestudy.model.cart;

import com.example.trendyolcasestudy.model.item.DefaultItem;
import com.example.trendyolcasestudy.model.item.Item;
import com.example.trendyolcasestudy.model.item.VasItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Cart {
    private List<Item> items;
    private double totalPrice;

    private int discountId;

    private double totalDiscount;



    public Cart(List<Item> items) {
        this.items = items;
        this.totalPrice = calculateTotalPrice();
    }
    public Cart() {

    }

    private double calculateTotalPrice() {
        double totalPrice = 0;

        for (Item item : items) {
            totalPrice += item.getPrice() * item.getQuantity();

            if (item instanceof DefaultItem defaultItem) {
                List<VasItem> vasItems = defaultItem.getVasItems();
                for (VasItem vasItem : vasItems) {
                    totalPrice += vasItem.getPrice() * vasItem.getQuantity();
                }
            }
        }

        return totalPrice;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getDiscountId() {
        return discountId;
    }

    public void setDiscountId(int discountId) {
        this.discountId = discountId;
    }

    public double getTotalDiscount() {
        return totalDiscount;
    }

    public void setTotalDiscount(double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }


}
