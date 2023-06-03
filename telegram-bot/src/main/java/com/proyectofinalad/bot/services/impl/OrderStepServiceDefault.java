package com.proyectofinalad.bot.services.impl;

import com.proyectofinalad.bot.commands.CommandSequence;
import com.proyectofinalad.bot.exceptions.ValidationException;
import com.proyectofinalad.bot.models.entities.Order;
import com.proyectofinalad.bot.repositories.OrderStepRepository;
import com.proyectofinalad.bot.repositories.memory.OrderStepRepositoryDefault;
import com.proyectofinalad.bot.services.OrderStepService;

public class OrderStepServiceDefault implements OrderStepService {

    private static final OrderStepService INSTANCE = new OrderStepServiceDefault();

    private final OrderStepRepository repository = new OrderStepRepositoryDefault();

    private OrderStepServiceDefault() {
    }

    public static OrderStepService getInstance() {
        return INSTANCE;
    }

    @Override
    public void updateOrderStepByChatId(Long chatId, CommandSequence<Long> orderStep) {
        repository.updateOrderStepByChatId(chatId, orderStep);
    }

    @Override
    public void executePreviousOrderStep(Long chatId) {
        CommandSequence<Long> orderStep = repository.findOrderStepByClientChatId(chatId);

        if (orderStep != null) {
            orderStep.executePrevious(chatId);
        }
    }

    @Override
    public void executeCurrentOrderStep(Long chatId) {
        CommandSequence<Long> orderStep = repository.findOrderStepByClientChatId(chatId);

        if (orderStep != null) {
            orderStep.execute(chatId);
        }
    }

    @Override
    public void executeNextOrderStep(Long chatId) {
        CommandSequence<Long> orderStep = repository.findOrderStepByClientChatId(chatId);

        if (orderStep != null) {
            orderStep.executeNext(chatId);
        }
    }

    @Override
    public Order findCachedOrderByChatId(Long chatId) {
        if (chatId == null) {
            throw new IllegalArgumentException("ChatId of Client should not be NULL");
        }

        return repository.findCachedOrderByChatId(chatId);
    }

    @Override
    public void updateCachedOrder(Long chatId, Order order) {
        if (order == null) {
            throw new IllegalArgumentException("Order should not be NULL");
        }
        if (order.getClient() == null) {
            throw new ValidationException("Client should not be NULL");
        }

        repository.updateCachedOrder(chatId, order);
    }

    @Override
    public void deleteCachedOrderByChatId(Long chatId) {
        if (chatId == null) {
            throw new IllegalArgumentException("ChatId of Client should not be NULL");
        }

        repository.deleteCachedOrderByChatId(chatId);
    }

}
