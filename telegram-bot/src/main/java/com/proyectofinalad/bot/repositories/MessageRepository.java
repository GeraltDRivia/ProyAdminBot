package com.proyectofinalad.bot.repositories;

import com.proyectofinalad.bot.models.entities.Message;

public interface MessageRepository {

    Message findByName(String messageName);

}
