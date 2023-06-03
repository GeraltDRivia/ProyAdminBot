package com.proyectofinalad.bot.handlers.impl;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.inlinequery.InlineQuery;

import com.proyectofinalad.bot.commands.impl.ShowProductsCommand;
import com.proyectofinalad.bot.handlers.Handler;

class InlineQueryHandler implements Handler {

    private final ShowProductsCommand showProductsCommand = ShowProductsCommand.getInstance();

    @Override
    public boolean supports(Update update) {
        return update.hasInlineQuery();
    }

    @Override
    public void handle(Update update) {
        InlineQuery inlineQuery = update.getInlineQuery();
        showProductsCommand.execute(inlineQuery);
    }

}
