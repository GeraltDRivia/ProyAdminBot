package com.proyectofinalad.bot.commands;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;

import java.util.Arrays;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

public final class Commands {

    public static final String START_COMMAND = "/start";

    public static final String CATALOG_COMMAND = "\uD83D\uDCE6 Catalogo";
    public static final String CART_COMMAND = "\uD83D\uDECD Carrito";

    public static final String ORDER_NEXT_STEP_COMMAND = "\u2714\uFE0F Correcto";
    public static final String ORDER_PREVIOUS_STEP_COMMAND = "\u25C0 Atras";
    public static final String ORDER_CANCEL_COMMAND = "\u274C Cancelar orden";

    private Commands() {
    }

    public static ReplyKeyboardMarkup createGeneralMenuKeyboard() {
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text(CATALOG_COMMAND).build(),
                builder().text(CART_COMMAND).build()
        )));

        return keyboardBuilder.build();
    }

}
