package com.proyectofinalad.bot.services.impl;

import java.util.List;

import com.proyectofinalad.bot.core.ConfigReader;
import com.proyectofinalad.bot.models.domain.MessageSend;
import com.proyectofinalad.bot.models.entities.Client;
import com.proyectofinalad.bot.models.entities.Order;
import com.proyectofinalad.bot.models.entities.OrderItem;
import com.proyectofinalad.bot.services.NotificationService;
import com.proyectofinalad.bot.services.TelegramService;

public class NotificationServiceDefault implements NotificationService {

    private static final NotificationService INSTANCE = new NotificationServiceDefault();

    private final TelegramService telegramService = TelegramServiceDefault.getInstance();

    private static final ConfigReader CONFIG = ConfigReader.getInstance();
    private static final String ADMIN_PANEL_BASE_URL = CONFIG.get("admin-panel.base-url");
    private static final Long TELEGRAM_ADMIN_CHAT_ID = Long.parseLong(CONFIG.get("telegram.admin.chat-id"));

    private NotificationServiceDefault() {
    }

    public static NotificationService getInstance() {
        return INSTANCE;
    }

    @Override
    public void notifyAdminChatAboutNewOrder(Order order) {
        telegramService.sendMessage(new MessageSend(TELEGRAM_ADMIN_CHAT_ID, createOrderAndClientInformation(order)));
        telegramService.sendMessage(new MessageSend(TELEGRAM_ADMIN_CHAT_ID, createOrderItemsInformation(order)));
    }

    private String createOrderAndClientInformation(Order order) {
        return "#Pedido_" + order.getId() + "\n" +
                "<b>Pedido url</b>:\n" + buildOrderUrl(order.getId()) + "\n\n" +
                "<b>Información del pedido</b>:\n" + buildOrderInformation(order) + "\n\n" +
                "<b>Información del cliente</b>:\n" + buildClientInformation(order.getClient());
    }

    private String buildOrderUrl(Integer orderId) {
        return ADMIN_PANEL_BASE_URL + "/orders/edit/" + orderId;
    }

    private String buildOrderInformation(Order order) {
        return "-Total: Q." + order.getAmount();
    }

    private String buildClientInformation(Client client) {
        return "-Nombre: " + client.getName() + "\n" +
                "-Número de teléfono: " + client.getPhoneNumber() + "\n" +
                "-Ciudad " + client.getCity() + "\n" +
                "-Dirrección: " + client.getAddress() + "\n" +
                "<a href=\"tg://user?id=" + client.getChatId() + "\">Contactar</a>";
    }

    private String createOrderItemsInformation(Order order) {
        return "#Pedido_" + order.getId() + "\n" +
                "<b>Descripcion del pedido</b>:\n" + buildOrderItemsInformation(order.getItems());
    }

    private String buildOrderItemsInformation(List<OrderItem> orderItems) {
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < orderItems.size(); i++) {
            OrderItem orderItem = orderItems.get(i);

            result.append(i + 1).append(") ").append(orderItem.getProductName()).append(" — ")
                    .append(orderItem.getQuantity()).append(" Cant. = Q.")
                    .append(orderItem.getProductPrice() * orderItem.getQuantity()).append(" \n");
        }

        return result.toString();
    }

}
