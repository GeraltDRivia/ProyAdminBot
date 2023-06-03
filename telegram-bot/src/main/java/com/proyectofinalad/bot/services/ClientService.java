package com.proyectofinalad.bot.services;

import com.proyectofinalad.bot.models.entities.Client;

public interface ClientService {

    Client findByChatId(Long chatId);

    void save(Client client);

    void update(Client client);

}
