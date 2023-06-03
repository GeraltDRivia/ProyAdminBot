package com.proyectofinalad.bot.services;

import com.proyectofinalad.bot.models.domain.ClientAction;

public interface ClientActionService {

    ClientAction findActionByChatId(Long chatId);

    void updateActionByChatId(Long chatId, ClientAction clientAction);

    void deleteActionByChatId(Long chatId);
}
