package me.dio.innovation.one.service;

import me.dio.innovation.one.domain.model.Category;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface CategoryService {

    Category createCategory(Category categoryDetails);

    Category updateCategory(Long id, Category categoryDetails);

    List<Category> findOrCreateCategoriesByName(List<String> names);

    Optional<Category> findCategoryById(Long id);

    void deleteCategory(Long id);

    Iterable<Category> findAllCategories();
}
