package com.nsteuerberg.service;

import com.nsteuerberg.model.entities.Product;
import com.nsteuerberg.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Optional<Product> updateProduct(Product product) {
        if (!productRepository.existsById(product.getId())){
            return Optional.empty();
        }
        return Optional.of(productRepository.save(product));
    }

    public Optional<Product> getProductById(Long id) {
        Product product = productRepository.getReferenceById(id);
        System.out.println(product);
        return Optional.of(product);
    }

    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
