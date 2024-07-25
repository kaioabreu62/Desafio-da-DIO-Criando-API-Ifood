package me.dio.innovation.one.service.impl;

import me.dio.innovation.one.domain.model.Category;
import me.dio.innovation.one.domain.repository.CategoryRepository;
import me.dio.innovation.one.service.CategoryService;
import me.dio.innovation.one.service.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category createCategory(Category categoryDetails) {
        Category category = new Category();
        category.setName(categoryDetails.getName());
        return this.categoryRepository.save(category);
    }

    @Override
    public Category updateCategory(Long id, Category categoryDetails) {

        Optional<Category> optionalCategory = categoryRepository.findById(id);
        if (optionalCategory.isPresent()) {
            Category category = optionalCategory.get();
            category.setName(categoryDetails.getName());
            return this.categoryRepository.save(category);
        } else {
            throw new BusinessException("Category not found with id " + id);
        }
    }

    @Override
    public List<Category> findOrCreateCategoriesByName(List<String> names) {
        List<Category> categories = new ArrayList<>();
        for (String name : names) {
            Category existingCategory = categoryRepository.findByName(name).orElse(null);
            if (existingCategory == null) {
                Category category = new Category();
                category.setName(name);
                existingCategory = this.createCategory(category);
            }
            categories.add(existingCategory);
        }

        return categories;
    }

    @Override
    public Optional<Category> findCategoryById(Long id) {
        return this.categoryRepository.findById(id);
    }

    @Override
    public void deleteCategory(Long id) {

        this.categoryRepository.deleteById(id);
    }

    @Override
    public Iterable<Category> findAllCategories() {
        return this.categoryRepository.findAll();
    }

}
