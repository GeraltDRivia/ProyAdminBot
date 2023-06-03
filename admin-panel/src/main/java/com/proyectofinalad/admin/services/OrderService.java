package com.proyectofinalad.admin.services;

import java.util.List;

import com.proyectofinalad.admin.models.entities.Order;

public interface OrderService {

    Order findById(Integer id);

    List<Order> findAll();

    Order save(Order order);

    Order update(Order order);

    void deleteById(Integer id);

}
