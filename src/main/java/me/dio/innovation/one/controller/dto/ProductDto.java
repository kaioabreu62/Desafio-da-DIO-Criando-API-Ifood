package me.dio.innovation.one.controller.dto;

import me.dio.innovation.one.domain.model.Category;
import me.dio.innovation.one.domain.model.Product;

import java.math.BigDecimal;
import java.util.List;

public record ProductDto(
        Long id,
        String name,
        List<String> ingredients,
        List<String> additional_items,
        String cup_size,
        String size,
        Integer quantity,
        BigDecimal price,
        List<String> categories) {

    public ProductDto(Product model) {
        this(
                model.getId(),
                model.getName(),
                model.getIngredients(),
                model.getAdditional_items(),
                model.getCup_size(),
                model.getSize(),
                model.getQuantity(),
                model.getPrice(),
                model.getCategories().stream().map(Category::getName).toList()
        );
    }

    public Product toModel() {
        Product model = new Product();
        model.setId(this.id);
        model.setName(this.name);
        model.setIngredients(this.ingredients);
        model.setAdditional_items(this.additional_items);
        model.setCup_size(this.cup_size);
        model.setSize(this.size);
        model.setQuantity(this.quantity);
        model.setPrice(this.price);
        return model;
    }
}
