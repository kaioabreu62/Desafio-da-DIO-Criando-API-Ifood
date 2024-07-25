package me.dio.innovation.one.domain.repository;

import me.dio.innovation.one.domain.model.FoodOrder;
import me.dio.innovation.one.domain.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FoodOrderRepository extends JpaRepository<FoodOrder, Long> {

    List<FoodOrder> findByShoppingCart(ShoppingCart shoppingCart);
}
