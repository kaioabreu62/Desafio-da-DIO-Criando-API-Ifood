package me.dio.innovation.one.controller.dto;

import me.dio.innovation.one.domain.model.FoodOrder;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public record FoodOrderDto(
        Long id,
        String name,
        List<String> ingredients,
        List<String> additional_items,
        String cup_size,
        String size,
        Integer quantity,
        BigDecimal price,
        BigDecimal subtotal) {

    public FoodOrderDto(FoodOrder model) {
        this(
                model.getId(),
                model.getName(),
                model.getIngredients(),
                model.getAdditional_items(),
                model.getCup_size(),
                model.getSize(),
                model.getQuantity(),
                model.getPrice(),
                model.getSubtotal()
        );
    }

    public FoodOrder toModel() {
        FoodOrder model = new FoodOrder();
        model.setId(this.id);
        model.setName(this.name);
        model.setIngredients(this.ingredients);
        model.setAdditional_items(this.additional_items);
        model.setCup_size(this.cup_size);
        model.setSize(this.size);
        model.setQuantity(this.quantity);
        model.setPrice(this.price);
        model.setSubtotal(this.subtotal);
        return model;
    }

    public static List<FoodOrderDto> fromModelList(List<FoodOrder> foodOrders) {
        return foodOrders.stream()
                .map(FoodOrderDto::new)
                .collect(Collectors.toList());
    }

    public static FoodOrderDto fromModel(FoodOrder model) {
        return new FoodOrderDto(
                model.getId(),
                model.getName(),
                model.getIngredients(),
                model.getAdditional_items(),
                model.getCup_size(),
                model.getSize(),
                model.getQuantity(),
                model.getPrice(),
                model.getSubtotal()
        );
    }
}
