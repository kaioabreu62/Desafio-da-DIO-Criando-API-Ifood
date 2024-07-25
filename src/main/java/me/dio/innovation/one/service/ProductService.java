package me.dio.innovation.one.service;

import me.dio.innovation.one.domain.model.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductService {

    Product findProductById(Long id);

    List<Product> findAllProducts();

    Product createProduct(Product productToCreate);

    Product updateProduct(Long id, Product productToUpdate);

    void deleteProduct(Long id);
}
