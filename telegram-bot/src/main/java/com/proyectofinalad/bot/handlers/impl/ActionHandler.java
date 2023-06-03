package com.proyectofinalad.bot.handlers.impl;

import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import com.proyectofinalad.bot.commands.impl.order.OrderEnterAddressCommand;
import com.proyectofinalad.bot.commands.impl.order.OrderEnterCityCommand;
import com.proyectofinalad.bot.commands.impl.order.OrderEnterNameCommand;
import com.proyectofinalad.bot.commands.impl.order.OrderEnterPhoneNumberCommand;
import com.proyectofinalad.bot.handlers.Handler;
import com.proyectofinalad.bot.models.domain.ClientAction;
import com.proyectofinalad.bot.services.ClientActionService;
import com.proyectofinalad.bot.services.impl.ClientActionServiceDefault;

class ActionHandler implements Handler {

    private final ClientActionService clientActionService = ClientActionServiceDefault.getInstance();

    private final OrderEnterNameCommand enterNameCommand = OrderEnterNameCommand.getInstance();
    private final OrderEnterPhoneNumberCommand enterPhoneNumberCommand = OrderEnterPhoneNumberCommand.getInstance();
    private final OrderEnterCityCommand enterCityNameCommand = OrderEnterCityCommand.getInstance();
    private final OrderEnterAddressCommand orderEnterAddressCommand = OrderEnterAddressCommand.getInstance();

    @Override
    public boolean supports(Update update) {
        return update.hasMessage() && update.getMessage().getReplyMarkup() == null;
    }

    @Override
    public void handle(Update update) {
        Message message = update.getMessage();
        Long chatId = message.getChatId();
        String text = message.getText();

        ClientAction clientAction = clientActionService.findActionByChatId(chatId);
        String action = clientAction != null ? clientAction.getAction() : null;

        if ("order=enter-client-name".equals(action)) {
            enterNameCommand.doEnterName(chatId, text);
        } else if ("order=enter-client-phone-number".equals(action)) {
            enterPhoneNumberCommand.doEnterPhoneNumber(chatId, text);
        } else if ("order=enter-client-city".equals(action)) {
            enterCityNameCommand.doEnterCity(chatId, text);
        } else if ("order=enter-client-address".equals(action)) {
            orderEnterAddressCommand.doEnterAddress(chatId, text);
        }
    }

}
