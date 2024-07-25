package me.dio.innovation.one.domain.repository;

import me.dio.innovation.one.domain.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {

    @Query("SELECT sc FROM ShoppingCart sc WHERE sc.id = :cartId")
    Optional<ShoppingCart> findById(@Param("cartId") Long cartId);
}
