package com.proyectofinalad.bot.repositories.database;

import static com.proyectofinalad.bot.repositories.hibernate.HibernateTransactionFactory.inTransaction;

import java.util.List;

import com.proyectofinalad.bot.models.entities.Product;
import com.proyectofinalad.bot.repositories.ProductRepository;

public class ProductRepositoryDefault implements ProductRepository {

    @Override
    public Product findById(Integer productId) {
        return inTransaction(session -> session.get(Product.class, productId));
    }

    @Override
    public List<Product> findAllByCategoryName(String categoryName, int offset, int size) {
        String query = "from Product where category.name = :categoryName";

        return inTransaction(session ->
                session.createQuery(query, Product.class)
                        .setParameter("categoryName", categoryName)
                        .setFirstResult(offset)
                        .setMaxResults(size)
                        .getResultList()
        );
    }

}
