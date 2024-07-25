package me.dio.innovation.one.service.impl;

import jakarta.transaction.Transactional;
import me.dio.innovation.one.domain.model.Category;
import me.dio.innovation.one.domain.model.Product;
import me.dio.innovation.one.domain.repository.CategoryRepository;
import me.dio.innovation.one.domain.repository.ProductRepository;
import me.dio.innovation.one.service.ProductService;
import me.dio.innovation.one.service.exception.BusinessException;
import me.dio.innovation.one.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional
    @Override
    public List<Product> findAllProducts() {
        return this.productRepository.findAll();
    }

    @Transactional
    @Override
    public Product findProductById(Long id) {
        return this.productRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Transactional
    @Override
    public Product createProduct(Product productToCreate) {
        Optional.ofNullable(productToCreate).orElseThrow(() -> new BusinessException("Product to create must not be null"));
        Optional.ofNullable(productToCreate.getName()).orElseThrow(() -> new BusinessException("Product to be created must not have a null name"));

        if (productToCreate.getIngredients() == null) {
            productToCreate.setIngredients(new ArrayList<>());
        }
        if (productToCreate.getAdditional_items() == null) {
            productToCreate.setAdditional_items(new ArrayList<>());
        }
        if (productToCreate.getCup_size() == null) {
            productToCreate.setCup_size("");
        }
        if (productToCreate.getSize() == null) {
            productToCreate.setSize("");
        }

        Optional.ofNullable(productToCreate.getQuantity()).orElseThrow(() -> new BusinessException("Product to be created must not have a null quantity"));
        Optional.ofNullable(productToCreate.getPrice()).orElseThrow(() -> new BusinessException("Product to be created must not have a null price"));

        if (productRepository.existsByNameAndSizeAndCupSize(productToCreate.getName(), productToCreate.getSize(), productToCreate.getCup_size())) {
            throw new BusinessException("This product already exists");
        }

        return this.productRepository.save(productToCreate);
    }

    @Transactional
    @Override
    public Product updateProduct(Long id, Product productToUpdate) {
        Product dbProduct = this.findProductById(id);
        if (!dbProduct.getId().equals(productToUpdate.getId())) {
            throw new BusinessException("Update IDs must be the same");
        }
        dbProduct.setName(productToUpdate.getName());
        dbProduct.setIngredients(productToUpdate.getIngredients());
        dbProduct.setAdditional_items(productToUpdate.getAdditional_items());
        dbProduct.setCup_size(productToUpdate.getCup_size());
        dbProduct.setSize(productToUpdate.getSize());
        dbProduct.setQuantity(productToUpdate.getQuantity());
        dbProduct.setPrice(productToUpdate.getPrice());

        if (productToUpdate.getCategories() != null) {
            List<Category> categories = productToUpdate.getCategories().stream()
                    .map(category -> categoryRepository.findByName(category.getName())
                            .orElseThrow(() -> new BusinessException("Category not found: " + category.getName())))
                    .collect(Collectors.toList());
            dbProduct.setCategories(categories);
        }

        return this.productRepository.save(dbProduct);
    }

    @Transactional
    @Override
    public void deleteProduct(Long id) {
        Product dbProduct = this.findProductById(id);
        this.productRepository.delete(dbProduct);
    }
}
