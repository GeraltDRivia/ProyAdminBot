package com.proyectofinalad.bot.services.impl;

import java.util.List;

import com.proyectofinalad.bot.models.entities.Product;
import com.proyectofinalad.bot.repositories.ProductRepository;
import com.proyectofinalad.bot.repositories.database.ProductRepositoryDefault;
import com.proyectofinalad.bot.services.ProductService;

public class ProductServiceDefault implements ProductService {

    private static final ProductService INSTANCE = new ProductServiceDefault();

    private final ProductRepository repository = new ProductRepositoryDefault();

    private ProductServiceDefault() {
    }

    public static ProductService getInstance() {
        return INSTANCE;
    }

    @Override
    public Product findById(Integer productId) {
        if (productId == null) {
            throw new IllegalArgumentException("Id of Product should not be NULL");
        }

        return repository.findById(productId);
    }

    @Override
    public List<Product> findAllByCategoryName(String categoryName, int offset, int size) {
        if (categoryName == null) {
            throw new IllegalArgumentException("Name of Category should not be NULL");
        }
        if (offset < 0) {
            throw new IllegalArgumentException("Offset should be more than 0");
        }
        if (size < 1) {
            throw new IllegalArgumentException("Size should be more than 1");
        }

        return repository.findAllByCategoryName(categoryName, offset, size);
    }

}
