package com.proyectofinalad.bot.services;

import java.util.List;

import com.proyectofinalad.bot.models.entities.Product;

public interface ProductService {

    Product findById(Integer productId);

    List<Product> findAllByCategoryName(String categoryName, int offset, int size);

}
