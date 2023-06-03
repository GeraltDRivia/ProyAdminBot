package com.proyectofinalad.bot.commands.impl.order;

import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton.builder;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import com.proyectofinalad.bot.commands.CommandSequence;
import com.proyectofinalad.bot.commands.Commands;
import com.proyectofinalad.bot.models.domain.ClientAction;
import com.proyectofinalad.bot.models.domain.MessageSend;
import com.proyectofinalad.bot.models.entities.Order;
import com.proyectofinalad.bot.services.ClientActionService;
import com.proyectofinalad.bot.services.OrderStepService;
import com.proyectofinalad.bot.services.TelegramService;
import com.proyectofinalad.bot.services.impl.ClientActionServiceDefault;
import com.proyectofinalad.bot.services.impl.OrderStepServiceDefault;
import com.proyectofinalad.bot.services.impl.TelegramServiceDefault;

public class OrderEnterPhoneNumberCommand implements CommandSequence<Long> {

    private static final OrderEnterPhoneNumberCommand INSTANCE = new OrderEnterPhoneNumberCommand();

    private final TelegramService telegramService = TelegramServiceDefault.getInstance();
    private final ClientActionService clientActionService = ClientActionServiceDefault.getInstance();
    private final OrderStepService orderStepService = OrderStepServiceDefault.getInstance();

    private static final Pattern PHONE_NUMBER_PATTERN =
            Pattern.compile("^(\\s*)?(\\+)?([- _():=+]?\\d[- _():=+]?){10,14}(\\s*)?$");

    private OrderEnterPhoneNumberCommand() {
    }

    public static OrderEnterPhoneNumberCommand getInstance() {
        return INSTANCE;
    }

    @Override
    public void execute(Long chatId) {
        orderStepService.updateOrderStepByChatId(chatId, this);
        clientActionService.updateActionByChatId(chatId, new ClientAction("order=enter-client-phone-number"));

        telegramService.sendMessage(new MessageSend(chatId,
                "Introduzca su número de teléfono o pulse el botón", createKeyboard(false)));

        sendCurrentPhoneNumber(chatId);
    }

    private void sendCurrentPhoneNumber(Long chatId) {
        Order order = orderStepService.findCachedOrderByChatId(chatId);

        if (!StringUtils.isBlank(order.getClient().getPhoneNumber())) {
            telegramService.sendMessage(new MessageSend(chatId,
                    "Número actual: " + order.getClient().getPhoneNumber(), createKeyboard(true)));
        }
    }

    private ReplyKeyboardMarkup createKeyboard(boolean skipStep) {
        ReplyKeyboardMarkup.ReplyKeyboardMarkupBuilder keyboardBuilder = ReplyKeyboardMarkup.builder();
        keyboardBuilder.resizeKeyboard(true);
        keyboardBuilder.selective(true);

        if (skipStep) {
            keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                    builder().text(Commands.ORDER_NEXT_STEP_COMMAND).build()
            )));
        }

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text("\uD83D\uDCF1 Enviar numero de télefono").requestContact(true).build()
        )));

        keyboardBuilder.keyboardRow(new KeyboardRow(Arrays.asList(
                builder().text(Commands.ORDER_CANCEL_COMMAND).build(),
                builder().text(Commands.ORDER_PREVIOUS_STEP_COMMAND).build()
        )));

        return keyboardBuilder.build();
    }

    public void doEnterPhoneNumber(Long chatId, String phoneNumber) {
        Matcher matcher = PHONE_NUMBER_PATTERN.matcher(phoneNumber);
        if (!matcher.find()) {
            telegramService.sendMessage(new MessageSend(chatId,
                    "Ha introducido un número de teléfono incorrecto, inténtelo de nuevo o pulse el botón ."));
            return;
        }

        Order order = orderStepService.findCachedOrderByChatId(chatId);
        order.getClient().setPhoneNumber(phoneNumber);
        orderStepService.updateCachedOrder(chatId, order);

        executeNext(chatId);
    }

    @Override
    public void executePrevious(Long chatId) {
        OrderEnterNameCommand.getInstance().execute(chatId);
    }

    @Override
    public void executeNext(Long chatId) {
        OrderEnterCityCommand.getInstance().execute(chatId);
    }

}
