package com.proyectofinalad.bot.repositories;

import java.util.List;

import com.proyectofinalad.bot.models.entities.Category;

public interface CategoryRepository {

    List<Category> findAll();

}
