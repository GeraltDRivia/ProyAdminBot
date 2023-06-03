package com.proyectofinalad.bot.commands.impl;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton.builder;

import java.util.Arrays;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import com.proyectofinalad.bot.commands.Command;
import com.proyectofinalad.bot.models.domain.MessageSend;
import com.proyectofinalad.bot.models.entities.Category;
import com.proyectofinalad.bot.services.CategoryService;
import com.proyectofinalad.bot.services.TelegramService;
import com.proyectofinalad.bot.services.impl.CategoryServiceDefault;
import com.proyectofinalad.bot.services.impl.TelegramServiceDefault;

public class CatalogCommand implements Command<Long> {

    private static final CatalogCommand INSTANCE = new CatalogCommand();

    private final TelegramService telegramService = TelegramServiceDefault.getInstance();
    private final CategoryService categoryService = CategoryServiceDefault.getInstance();

    private CatalogCommand() {
    }

    public static CatalogCommand getInstance() {
        return INSTANCE;
    }

    @Override
    public void execute(Long chatId) {
        telegramService.sendMessage(new MessageSend(chatId, "Selecciona una categoria:", createCategoriesKeyboard()));
    }

    private InlineKeyboardMarkup createCategoriesKeyboard() {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder keyboardBuilder = InlineKeyboardMarkup.builder();

        for (Category category : categoryService.findAll()) {
            String categoryName = category.getName();

            keyboardBuilder.keyboardRow(Arrays.asList(
                    builder().text(categoryName).switchInlineQueryCurrentChat(categoryName).build()
            ));
        }

        return keyboardBuilder.build();
    }

}
