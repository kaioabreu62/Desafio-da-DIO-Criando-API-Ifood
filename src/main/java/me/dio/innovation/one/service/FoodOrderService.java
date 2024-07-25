package me.dio.innovation.one.service;

import me.dio.innovation.one.domain.model.FoodOrder;
import me.dio.innovation.one.domain.model.Total;

import java.util.List;

public interface FoodOrderService {

    FoodOrder addFoodOrder(Long cartId, FoodOrder foodOrder);

    FoodOrder updateFoodOrder(Long cartId, Long foodOrderId, FoodOrder foodOrder);

    void removeFoodOrder(Long cartId, Long foodOrderId);

    Total checkoutShoppingCart(Long cartId, String paymentMethod, Long userId);

    List<FoodOrder> getFoodOrdersByCart(Long cartId);

}
