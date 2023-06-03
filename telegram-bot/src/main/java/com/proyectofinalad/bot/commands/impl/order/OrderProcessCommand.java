package com.proyectofinalad.bot.commands.impl.order;

import java.time.LocalDateTime;
import java.util.List;

import com.proyectofinalad.bot.commands.CommandSequence;
import com.proyectofinalad.bot.exceptions.EntityNotFoundException;
import com.proyectofinalad.bot.models.domain.CartItem;
import com.proyectofinalad.bot.models.entities.Client;
import com.proyectofinalad.bot.models.entities.Order;
import com.proyectofinalad.bot.models.entities.OrderStatus;
import com.proyectofinalad.bot.services.CartService;
import com.proyectofinalad.bot.services.ClientService;
import com.proyectofinalad.bot.services.OrderService;
import com.proyectofinalad.bot.services.OrderStepService;
import com.proyectofinalad.bot.services.impl.CartServiceDefault;
import com.proyectofinalad.bot.services.impl.ClientServiceDefault;
import com.proyectofinalad.bot.services.impl.OrderServiceDefault;
import com.proyectofinalad.bot.services.impl.OrderStepServiceDefault;

public class OrderProcessCommand implements CommandSequence<Long> {

    private static final OrderProcessCommand INSTANCE = new OrderProcessCommand();

    private final ClientService clientService = ClientServiceDefault.getInstance();
    private final CartService cartService = CartServiceDefault.getInstance();
    private final OrderStepService orderStepService = OrderStepServiceDefault.getInstance();
    private final OrderService orderService = OrderServiceDefault.getInstance();

    private OrderProcessCommand() {
    }

    public static OrderProcessCommand getInstance() {
        return INSTANCE;
    }

    @Override
    public void execute(Long chatId) {
        Client client = clientService.findByChatId(chatId);

        if (client == null) {
            throw new EntityNotFoundException("Client with chatId '" + chatId + "' not found");
        }

        Order order = buildOrder(client, cartService.findAllCartItemsByChatId(chatId));
        orderStepService.updateCachedOrder(chatId, order);

        executeNext(chatId);
    }

    private Order buildOrder(Client client, List<CartItem> cartItems) {
        Order order = new Order();

        order.setClient(client);
        order.setCreatedDate(LocalDateTime.now());
        order.setStatus(OrderStatus.WAITING);
        order.setAmount(cartService.calculateTotalPrice(cartItems));
        order.setItems(orderService.fromCartItems(cartItems));

        return order;
    }

    @Override
    public void executePrevious(Long chatId) {
        OrderCancelCommand.getInstance().execute(chatId);
    }

    @Override
    public void executeNext(Long chatId) {
        OrderEnterNameCommand.getInstance().execute(chatId);
    }

}
