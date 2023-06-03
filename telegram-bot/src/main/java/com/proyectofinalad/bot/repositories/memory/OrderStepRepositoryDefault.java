package com.proyectofinalad.bot.repositories.memory;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.SerializationUtils;

import com.proyectofinalad.bot.commands.CommandSequence;
import com.proyectofinalad.bot.models.entities.Order;
import com.proyectofinalad.bot.repositories.OrderStepRepository;

public class OrderStepRepositoryDefault implements OrderStepRepository {

    private final Map<Long, CommandSequence<Long>> clientOrderSteps = new HashMap<>();

    private final Map<Long, Order> cachedOrders = new HashMap<>();

    @Override
    public CommandSequence<Long> findOrderStepByClientChatId(Long chatId) {
        return clientOrderSteps.get(chatId);
    }

    @Override
    public void updateOrderStepByChatId(Long chatId, CommandSequence<Long> orderStep) {
        clientOrderSteps.put(chatId, orderStep);
    }

    @Override
    public Order findCachedOrderByChatId(Long chatId) {
        return SerializationUtils.clone(cachedOrders.get(chatId));
    }

    @Override
    public void updateCachedOrder(Long chatId, Order order) {
        cachedOrders.put(chatId, SerializationUtils.clone(order));
    }

    @Override
    public void deleteCachedOrderByChatId(Long chatId) {
        clientOrderSteps.remove(chatId);
        cachedOrders.remove(chatId);
    }

}
