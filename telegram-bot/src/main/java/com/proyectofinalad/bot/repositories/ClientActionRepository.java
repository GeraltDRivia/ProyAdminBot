package com.proyectofinalad.bot.repositories;

import com.proyectofinalad.bot.models.domain.ClientAction;

public interface ClientActionRepository {

    ClientAction findActionByChatId(Long chatId);

    void updateActionByChatId(Long chatId, ClientAction clientAction);

    void deleteActionByChatId(Long chatId);

}
