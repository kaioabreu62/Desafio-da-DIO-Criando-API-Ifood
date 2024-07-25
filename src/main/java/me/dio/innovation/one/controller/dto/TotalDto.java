package me.dio.innovation.one.controller.dto;

import me.dio.innovation.one.domain.model.Payment;
import me.dio.innovation.one.domain.model.Total;
import me.dio.innovation.one.domain.model.User;

import java.math.BigDecimal;

public record TotalDto(
        Long id,
        BigDecimal subtotal,
        BigDecimal service_fee,
        BigDecimal delivery_fee,
        BigDecimal discount_restaurant,
        BigDecimal incentives_ifood,
        BigDecimal final_total) {

    public TotalDto(Total model) {
        this(
                model.getId(),
                model.getSubtotal(),
                model.getServiceFee(),
                model.getDeliveryFee(),
                model.getDiscountRestaurant(),
                model.getIncentivesIfood(),
                model.getFinalTotal()
        );
    }

    public Total toModel() {
        Total model = new Total();
        model.setId(this.id);
        model.setSubtotal(this.subtotal);
        model.setServiceFee(this.service_fee);
        model.setDeliveryFee(this.delivery_fee);
        model.setDiscountRestaurant(this.discount_restaurant);
        model.setIncentivesIfood(this.incentives_ifood);
        model.setFinalTotal(this.final_total);
        return model;
    }

    public static TotalDto fromModel(Total model) {
        return new TotalDto(
                model.getId(),
                model.getSubtotal(),
                model.getServiceFee(),
                model.getDeliveryFee(),
                model.getDiscountRestaurant(),
                model.getIncentivesIfood(),
                model.getFinalTotal()
        );
    }
}
