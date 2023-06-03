package com.proyectofinalad.bot.commands.impl.order;

import com.proyectofinalad.bot.commands.Command;
import com.proyectofinalad.bot.commands.Commands;
import com.proyectofinalad.bot.models.domain.MessageSend;
import com.proyectofinalad.bot.services.ClientActionService;
import com.proyectofinalad.bot.services.OrderStepService;
import com.proyectofinalad.bot.services.TelegramService;
import com.proyectofinalad.bot.services.impl.ClientActionServiceDefault;
import com.proyectofinalad.bot.services.impl.OrderStepServiceDefault;
import com.proyectofinalad.bot.services.impl.TelegramServiceDefault;

public class OrderCancelCommand implements Command<Long> {

    private static final OrderCancelCommand INSTANCE = new OrderCancelCommand();

    private final TelegramService telegramService = TelegramServiceDefault.getInstance();
    private final OrderStepService orderStepService = OrderStepServiceDefault.getInstance();
    private final ClientActionService clientActionService = ClientActionServiceDefault.getInstance();

    private OrderCancelCommand() {
    }

    public static OrderCancelCommand getInstance() {
        return INSTANCE;
    }

    @Override
    public void execute(Long chatId) {
        clearClientOrderCache(chatId);

        telegramService.sendMessage(new MessageSend(chatId, "Pedido cancelado.", Commands.createGeneralMenuKeyboard()));
    }

    private void clearClientOrderCache(Long chatId) {
        clientActionService.deleteActionByChatId(chatId);
        orderStepService.deleteCachedOrderByChatId(chatId);
    }

}
