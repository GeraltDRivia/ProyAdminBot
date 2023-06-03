package com.proyectofinalad.bot.repositories;

import com.proyectofinalad.bot.commands.CommandSequence;
import com.proyectofinalad.bot.models.entities.Order;

public interface OrderStepRepository {

    CommandSequence<Long> findOrderStepByClientChatId(Long chatId);

    void updateOrderStepByChatId(Long chatId, CommandSequence<Long> orderStep);

    Order findCachedOrderByChatId(Long chatId);

    void updateCachedOrder(Long chatId, Order order);

    void deleteCachedOrderByChatId(Long chatId);

}
