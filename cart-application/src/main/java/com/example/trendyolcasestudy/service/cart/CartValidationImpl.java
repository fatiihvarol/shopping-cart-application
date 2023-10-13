package com.example.trendyolcasestudy.service.cart;
import com.example.trendyolcasestudy.model.item.DigitalItem;
import com.example.trendyolcasestudy.model.item.Item;
import com.example.trendyolcasestudy.model.item.VasItem;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;



@Service
public class CartValidationImpl<T extends Item> implements CartValidation<T> {
    @Value("${unique.item.count}")
    private int uniqueItemCount;

    @Value("${max.total.cart.amount}")
    private BigDecimal maxTotalCartAmount;

    @Value("${max.total.item.count}")
    private long maxTotalItemCount;

    @Value("${max.item.exceeds.limit}")
    private int itemExceedsLimit;

    @Value("${max.item.exceeds.limit.for.digital.item}")
    private int itemExceedsLimitForDigitalItems;



    @Override
    public void isCartItemsUnique(List<T> items) {
        long uniqueItemCounter = items.stream()
                .filter(item -> !(item instanceof VasItem))
                .count();

        if (uniqueItemCounter > uniqueItemCount) {
            throw new IllegalArgumentException("Cart items are not unique");
        }
    }

    @Override
    public void isCartTotalItemCountValid(List<T> items) {
        long totalItemCount = items.stream()
                .mapToLong(Item::getQuantity)
                .sum();
        if (totalItemCount > maxTotalItemCount) {
            throw new IllegalArgumentException("Cart can not have more than " + maxTotalItemCount + " items");
        }
    }

    @Override
    public void isCartTotalAmountValid(List<T> items) {
        BigDecimal totalAmount = items.stream()
                .map(item -> BigDecimal.valueOf(item.getPrice() * item.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalAmount.compareTo(maxTotalCartAmount) > 0) {
            throw new IllegalArgumentException("Cart total amount can not exceed 500,000 TL");
        }
    }

    @Override
    public void isItemQuantityValid(List<T> items) {
        boolean anyItemExceedsLimit = items.stream()
                .anyMatch(item -> item.getQuantity() > itemExceedsLimit);
        if (anyItemExceedsLimit) {
            throw new IllegalArgumentException("You can not add more than " + itemExceedsLimit + " items");
        }
    }

    @Override
    public void isItemQuantityValidForDigitalItems(List<T> items) {
        boolean anyItemExceedsLimit = items.stream()
                .anyMatch(item -> item.getQuantity() > itemExceedsLimitForDigitalItems);
        if (anyItemExceedsLimit) {
            throw new IllegalArgumentException("You can not add more than " + itemExceedsLimitForDigitalItems + " digital items");
        }
    }

    @Override
    public void isCardValidForNoDigitalItems(List<T> items) {
        List<T> nonDigitalItems = items.stream()
                .filter(item -> (item instanceof DigitalItem))
                .toList();
        if (!nonDigitalItems.isEmpty()) {
            throw new IllegalArgumentException("You can add only digital items to cart");
        }

    }


}
