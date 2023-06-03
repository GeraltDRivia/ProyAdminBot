package com.proyectofinalad.bot.services;

import com.proyectofinalad.bot.models.domain.InlineQuerySend;
import com.proyectofinalad.bot.models.domain.MessageEdit;
import com.proyectofinalad.bot.models.domain.MessageSend;

public interface TelegramService {

    void sendMessage(MessageSend message);

    void editMessageText(MessageEdit message);

    void sendInlineQuery(InlineQuerySend inlineQuery);

}
