package com.proyectofinalad.admin.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.proyectofinalad.admin.exceptions.ValidationException;
import com.proyectofinalad.admin.models.entities.Category;
import com.proyectofinalad.admin.repositories.CategoryRepository;
import com.proyectofinalad.admin.services.CategoryService;

@Service
public class CategoryServiceDefault implements CategoryService {

    private final CategoryRepository repository;

    public CategoryServiceDefault(CategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public Category findById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Id of Category should not be NULL");
        }

        return repository.findById(id).orElse(null);
    }

    @Override
    public List<Category> findAll() {
        return repository.findAll();
    }

    @Override
    public Category save(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category should not be NULL");
        }
        if (category.getId() != null) {
            throw new ValidationException("Id of Category should be NULL");
        }

        return repository.save(category);
    }

    @Override
    public Category update(Category category) {
        if (category == null) {
            throw new IllegalArgumentException("Category should not be NULL");
        }
        if (category.getId() == null) {
            throw new ValidationException("Id of Category should not be NULL");
        }

        return repository.save(category);
    }

    @Override
    public void deleteById(Integer id) {
        if (id == null) {
            throw new IllegalArgumentException("Id of Category should not be NULL");
        }

        repository.deleteById(id);
    }

}
