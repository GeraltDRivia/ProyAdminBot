package com.proyectofinalad.bot.services;

import com.proyectofinalad.bot.models.entities.Message;

public interface MessageService {

    Message findByName(String messageName);

}
