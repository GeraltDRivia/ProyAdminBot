package com.proyectofinalad.bot.commands.impl.order;

import com.proyectofinalad.bot.commands.Command;
import com.proyectofinalad.bot.commands.Commands;
import com.proyectofinalad.bot.models.domain.MessageSend;
import com.proyectofinalad.bot.models.entities.Order;
import com.proyectofinalad.bot.repositories.hibernate.HibernateTransactionFactory;
import com.proyectofinalad.bot.services.CartService;
import com.proyectofinalad.bot.services.ClientActionService;
import com.proyectofinalad.bot.services.ClientService;
import com.proyectofinalad.bot.services.MessageService;
import com.proyectofinalad.bot.services.NotificationService;
import com.proyectofinalad.bot.services.OrderService;
import com.proyectofinalad.bot.services.OrderStepService;
import com.proyectofinalad.bot.services.TelegramService;
import com.proyectofinalad.bot.services.impl.CartServiceDefault;
import com.proyectofinalad.bot.services.impl.ClientActionServiceDefault;
import com.proyectofinalad.bot.services.impl.ClientServiceDefault;
import com.proyectofinalad.bot.services.impl.MessageServiceCached;
import com.proyectofinalad.bot.services.impl.NotificationServiceDefault;
import com.proyectofinalad.bot.services.impl.OrderServiceDefault;
import com.proyectofinalad.bot.services.impl.OrderStepServiceDefault;
import com.proyectofinalad.bot.services.impl.TelegramServiceDefault;

public class OrderCreateCommand implements Command<Long> {

    private static final OrderCreateCommand INSTANCE = new OrderCreateCommand();

    private final TelegramService telegramService = TelegramServiceDefault.getInstance();
    private final ClientService clientService = ClientServiceDefault.getInstance();
    private final ClientActionService clientActionService = ClientActionServiceDefault.getInstance();
    private final CartService cartService = CartServiceDefault.getInstance();
    private final OrderStepService orderStepService = OrderStepServiceDefault.getInstance();
    private final OrderService orderService = OrderServiceDefault.getInstance();
    private final NotificationService notificationService = NotificationServiceDefault.getInstance();
    private final MessageService messageService = MessageServiceCached.getInstance();

    private OrderCreateCommand() {
    }

    public static OrderCreateCommand getInstance() {
        return INSTANCE;
    }

    @Override
    public void execute(Long chatId) {
        Order order = orderStepService.findCachedOrderByChatId(chatId);

        HibernateTransactionFactory.inTransactionVoid(session -> {
            orderService.save(order);
            clientService.update(order.getClient());
        });

        sendOrderMessageToClient(chatId);
        clearClientOrderCache(chatId);

        notificationService.notifyAdminChatAboutNewOrder(order);
    }

    private void sendOrderMessageToClient(Long chatId) {
        String message = messageService.findByName("ORDER_CREATED_MESSAGE").buildText();
        telegramService.sendMessage(new MessageSend(chatId, message, Commands.createGeneralMenuKeyboard()));
    }

    private void clearClientOrderCache(Long chatId) {
        clientActionService.deleteActionByChatId(chatId);
        orderStepService.deleteCachedOrderByChatId(chatId);
        cartService.deleteAllCartItemsByChatId(chatId);
    }

}
