package com.proyectofinalad.bot.repositories.database;

import static com.proyectofinalad.bot.repositories.hibernate.HibernateTransactionFactory.inTransaction;

import java.util.List;

import com.proyectofinalad.bot.models.entities.Category;
import com.proyectofinalad.bot.repositories.CategoryRepository;

public class CategoryRepositoryDefault implements CategoryRepository {

    @Override
    public List<Category> findAll() {
        String query = "from Category";

        return inTransaction(session -> session.createQuery(query, Category.class).getResultList());
    }

}
