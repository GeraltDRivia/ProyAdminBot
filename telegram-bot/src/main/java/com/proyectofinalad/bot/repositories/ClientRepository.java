package com.proyectofinalad.bot.repositories;

import com.proyectofinalad.bot.models.entities.Client;

public interface ClientRepository {

    Client findByChatId(Long chatId);

    void save(Client client);

    void update(Client client);

}
