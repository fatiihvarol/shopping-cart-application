package com.example.trendyolcasestudy.service.cart;




import com.example.trendyolcasestudy.model.item.Item;
import java.util.List;



public interface CartValidation <T extends Item>{
    void isCartItemsUnique(List<T> items);
    void isCartTotalItemCountValid(List<T> items);
    void isCartTotalAmountValid(List<T> items);
    void isItemQuantityValid(List<T> items);
    void isItemQuantityValidForDigitalItems(List<T> items);
    void isCardValidForNoDigitalItems(List<T> items);

}
