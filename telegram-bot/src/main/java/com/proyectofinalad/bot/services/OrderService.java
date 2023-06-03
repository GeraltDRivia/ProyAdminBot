package com.proyectofinalad.bot.services;

import java.util.List;

import com.proyectofinalad.bot.models.domain.CartItem;
import com.proyectofinalad.bot.models.entities.Order;
import com.proyectofinalad.bot.models.entities.OrderItem;

public interface OrderService {

    void save(Order order);

    List<OrderItem> fromCartItems(List<CartItem> cartItems);

}
