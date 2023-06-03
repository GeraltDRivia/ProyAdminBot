package com.proyectofinalad.bot.repositories.database;

import static com.proyectofinalad.bot.repositories.hibernate.HibernateTransactionFactory.inTransactionVoid;

import com.proyectofinalad.bot.models.entities.Order;
import com.proyectofinalad.bot.repositories.OrderRepository;

public class OrderRepositoryDefault implements OrderRepository {

    @Override
    public void save(Order order) {
        inTransactionVoid(session -> session.persist(order));
    }

}
