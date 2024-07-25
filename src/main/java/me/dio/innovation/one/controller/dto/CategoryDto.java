package me.dio.innovation.one.controller.dto;

import me.dio.innovation.one.domain.model.Category;

public record CategoryDto(
        Long id,
        String name) {

    public CategoryDto(Category model) {
        this(
                model.getId(),
                model.getName()
        );
    }

    public Category toModel() {
        Category model = new Category();
        model.setId(this.id);
        model.setName(this.name);
        return model;
    }
}
