package com.proyectofinalad.bot.handlers.impl;

import java.util.HashMap;
import java.util.Map;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;

import com.proyectofinalad.bot.commands.Command;
import com.proyectofinalad.bot.commands.Commands;
import com.proyectofinalad.bot.commands.impl.CartCommand;
import com.proyectofinalad.bot.commands.impl.CatalogCommand;
import com.proyectofinalad.bot.commands.impl.ShowProductsCommand;
import com.proyectofinalad.bot.commands.impl.StartCommand;
import com.proyectofinalad.bot.commands.impl.order.OrderCancelCommand;
import com.proyectofinalad.bot.handlers.Handler;
import com.proyectofinalad.bot.services.OrderStepService;
import com.proyectofinalad.bot.services.impl.OrderStepServiceDefault;

class MessageHandler implements Handler {

    private final OrderStepService orderStepService = OrderStepServiceDefault.getInstance();

    private final StartCommand startCommand = StartCommand.getInstance();
    private final CatalogCommand catalogCommand = CatalogCommand.getInstance();
    private final CartCommand cartCommand = CartCommand.getInstance();
    private final OrderCancelCommand orderCancelCommand = OrderCancelCommand.getInstance();
    private final ShowProductsCommand showProductsCommand = ShowProductsCommand.getInstance();

    private final Map<String, Command<Long>> commands = createCommandHandlers();

    private Map<String, Command<Long>> createCommandHandlers() {
        Map<String, Command<Long>> result = new HashMap<>();

        result.put(Commands.CATALOG_COMMAND, catalogCommand::execute);
        result.put(Commands.CART_COMMAND, cartCommand::execute);
        result.put(Commands.ORDER_PREVIOUS_STEP_COMMAND, orderStepService::executePreviousOrderStep);
        result.put(Commands.ORDER_NEXT_STEP_COMMAND, orderStepService::executeNextOrderStep);
        result.put(Commands.ORDER_CANCEL_COMMAND, orderCancelCommand::execute);

        return result;
    }

    @Override
    public boolean supports(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return false;
        }

        String text = update.getMessage().getText();
        return text.startsWith(Commands.START_COMMAND) || commands.containsKey(text);
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String text = message.getText();
        System.out.println(chatId+"   "+text);
        if (text.startsWith(Commands.START_COMMAND)) {
            System.out.println("Comando Start");
            startCommand.execute(chatId);
            
        } else if (commands.containsKey(text)) {
            commands.get(text).execute(chatId);
        }
    }

}
