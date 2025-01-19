package com.nsteuerberg.controller;

import com.nsteuerberg.model.entities.Product;
import com.nsteuerberg.service.ProductService;
import org.hibernate.Hibernate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {

    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping()
    ResponseEntity<?> getProduct(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String productName
    ) {
        System.out.println(id);
        if (id!=null) {
            Optional<Product> productOptional = productService.getProductById(id);
            if (productOptional.isPresent()) {
                Product product = productOptional.get();
                Hibernate.initialize(product);
                return ResponseEntity.ok(product);
            }
            else {
                return ResponseEntity.badRequest().body("a");
            }

        } else if (productName != null) {
            // recoger por nombre   ToDo
            return ResponseEntity.ok().body(productService.getProducts());
        } else {
            return ResponseEntity.ok(productService.getProducts());
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<?> updateProduct(@PathVariable Long id ,@RequestBody Product updateProduct) {
        Optional<Product> productBefore = productService.getProductById(id);
        if (productBefore.isPresent()) {
            Product product = productBefore.get();
            product.setName(updateProduct.getName());
            product.setPrice(updateProduct.getPrice());
            product.setStock(updateProduct.getStock());
            return ResponseEntity.ok(productService.saveProduct(product));
        }

        return ResponseEntity.badRequest().body("No existe el producto");
    }

    @PostMapping
    ResponseEntity<?> saveProduct(@RequestBody Product product) {
        System.out.println(product.toString());
        return ResponseEntity.ok(productService.saveProduct(product));
    }
}
