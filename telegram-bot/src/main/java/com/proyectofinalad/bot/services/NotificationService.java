package com.proyectofinalad.bot.services;

import com.proyectofinalad.bot.models.entities.Order;

public interface NotificationService {

    void notifyAdminChatAboutNewOrder(Order order);

}
