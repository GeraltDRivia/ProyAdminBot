package com.proyectofinalad.bot.services.impl;

import java.util.List;

import com.proyectofinalad.bot.models.entities.Category;
import com.proyectofinalad.bot.repositories.CategoryRepository;
import com.proyectofinalad.bot.repositories.database.CategoryRepositoryDefault;
import com.proyectofinalad.bot.services.CategoryService;

public class CategoryServiceDefault implements CategoryService {

    private static final CategoryService INSTANCE = new CategoryServiceDefault();

    private final CategoryRepository repository = new CategoryRepositoryDefault();

    private CategoryServiceDefault() {
    }

    public static CategoryService getInstance() {
        return INSTANCE;
    }

    @Override
    public List<Category> findAll() {
        return repository.findAll();
    }

}
