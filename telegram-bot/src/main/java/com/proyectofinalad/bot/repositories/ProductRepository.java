package com.proyectofinalad.bot.repositories;

import java.util.List;

import com.proyectofinalad.bot.models.entities.Product;

public interface ProductRepository {

    Product findById(Integer productId);

    List<Product> findAllByCategoryName(String categoryName, int offset, int size);

}
