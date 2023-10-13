package com.example.trendyolcasestudy.service.cart;
import com.example.trendyolcasestudy.model.cart.Cart;
import com.example.trendyolcasestudy.model.item.DefaultItem;
import com.example.trendyolcasestudy.model.item.Item;
import com.example.trendyolcasestudy.model.item.VasItem;
import com.example.trendyolcasestudy.model.promotion.DiscountCalculationResult;
import com.example.trendyolcasestudy.repository.ItemRepository;
import com.example.trendyolcasestudy.response.ResponseService;
import com.example.trendyolcasestudy.service.promotion.PromotionChooser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class CartService {

    private  final ItemRepository itemRepository;
    private final PromotionChooser promotionChooser;
    private final ResponseService responseService;

    @Autowired
    public CartService(ItemRepository itemRepository, PromotionChooser promotionChooser, ResponseService responseService) {
        this.itemRepository = itemRepository;
        this.promotionChooser = promotionChooser;
        this.responseService = responseService;
    }

    public Cart  displayCart() {
        List<Item> items = itemRepository.findAll();
        Cart cart = configCart(items);

        if (items.isEmpty()) {
            return new Cart();
        }


        DiscountCalculationResult discountCalculationResult = promotionChooser.choosePromotion(cart);

        updatePriceWithDiscount(cart, discountCalculationResult);

        return cart;

    }

    private Cart configCart(List<Item> items) {

        Map<Integer, DefaultItem> defaultItemsMap = items.stream()
                .filter(item -> item instanceof DefaultItem)
                .map(item -> (DefaultItem) item)
                .collect(Collectors.toMap(DefaultItem::getItemId, Function.identity()));


        if (defaultItemsMap.isEmpty()) {
            return new Cart(items);
        }


        items.stream()
                .filter(item -> item instanceof VasItem)
                .map(item -> (VasItem) item)
                .forEach(vasItem -> {
                    DefaultItem defaultItem = defaultItemsMap.get(vasItem.getItemId());
                    if (defaultItem != null) {
                        defaultItem.getVasItems().add(vasItem);
                    }
                });

        List<Item> defaultItems = new ArrayList<>(defaultItemsMap.values());
        return new Cart(defaultItems);

    }

    public ResponseEntity<Map<String, Object>> resetCart() {
        itemRepository.deleteAll();
        return responseService.createApiResponse(true, "Your cart is empty now");
        }

      void updatePriceWithDiscount(Cart cart, DiscountCalculationResult discountCalculationResult) {
        double totalDiscount = discountCalculationResult.getTotalDiscount();
        double totalPrice = cart.getTotalPrice();

        double newTotalPrice = calculateNewTotalPrice(totalPrice, totalDiscount);

        double newTotalDiscount = Math.min(totalPrice, totalDiscount);

        cart.setTotalPrice(newTotalPrice);
        cart.setTotalDiscount(newTotalDiscount);
        cart.setDiscountId(discountCalculationResult.getPromotionId());
    }

    double calculateNewTotalPrice(double totalPrice, double totalDiscount) {
        double totalPriceWithDiscount = totalPrice - totalDiscount;
        return Math.max(0, totalPriceWithDiscount);
    }












}
