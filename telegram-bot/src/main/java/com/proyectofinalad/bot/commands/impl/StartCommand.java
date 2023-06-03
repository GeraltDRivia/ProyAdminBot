package com.proyectofinalad.bot.commands.impl;

import com.proyectofinalad.bot.commands.Command;
import com.proyectofinalad.bot.commands.Commands;
import com.proyectofinalad.bot.models.domain.MessageSend;
import com.proyectofinalad.bot.models.entities.Client;
import com.proyectofinalad.bot.services.ClientService;
import com.proyectofinalad.bot.services.MessageService;
import com.proyectofinalad.bot.services.TelegramService;
import com.proyectofinalad.bot.services.impl.ClientServiceDefault;
import com.proyectofinalad.bot.services.impl.MessageServiceCached;
import com.proyectofinalad.bot.services.impl.TelegramServiceDefault;

public class StartCommand implements Command<Long> {

    private static final StartCommand INSTANCE = new StartCommand();

    private final TelegramService telegramService = TelegramServiceDefault.getInstance();
    private final ClientService clientService = ClientServiceDefault.getInstance();
    private final MessageService messageService = MessageServiceCached.getInstance();

    private StartCommand() {
    }

    public static StartCommand getInstance() {
        return INSTANCE;
    }

    @Override
    public void execute(Long chatId) {
        Client client = clientService.findByChatId(chatId);

        if (client == null) {
            saveClient(chatId);
        } else if (!client.isActive()) {
            activateClient(client);
        }

        sendStartMessage(chatId);
    }

    private void saveClient(Long chatId) {
        Client client = new Client();
        client.setChatId(chatId);
        client.setActive(true);
        clientService.save(client);
    }

    private void activateClient(Client client) {
        client.setActive(true);
        clientService.update(client);
    }

    private void sendStartMessage(Long chatId) {
        String message = messageService.findByName("START_MESSAGE").buildText();
        telegramService.sendMessage(new MessageSend(chatId, message, Commands.createGeneralMenuKeyboard()));
    }

}
