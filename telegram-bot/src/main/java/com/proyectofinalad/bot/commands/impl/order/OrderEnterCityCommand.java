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

public class OrderEnterCityCommand implements CommandSequence<Long> {

    private static final OrderEnterCityCommand INSTANCE = new OrderEnterCityCommand();

    private final TelegramService telegramService = TelegramServiceDefault.getInstance();
    private final ClientActionService clientActionService = ClientActionServiceDefault.getInstance();
    private final OrderStepService orderStepService = OrderStepServiceDefault.getInstance();

    private static final Pattern CITY_PATTERN = Pattern.compile("[a-zA-Z]");

    private OrderEnterCityCommand() {
    }

    public static OrderEnterCityCommand getInstance() {
        return INSTANCE;
    }

    @Override
    public void execute(Long chatId) {
        orderStepService.updateOrderStepByChatId(chatId, this);
        clientActionService.updateActionByChatId(chatId, new ClientAction("order=enter-client-city"));

        telegramService.sendMessage(new MessageSend(chatId, "Ingresa ciudad", createKeyboard(false)));

        sendCurrentCity(chatId);
    }

    private void sendCurrentCity(Long chatId) {
        Order order = orderStepService.findCachedOrderByChatId(chatId);

        if (!StringUtils.isBlank(order.getClient().getCity())) {
            telegramService.sendMessage(new MessageSend(chatId,
                    "Ciudad actual: " + order.getClient().getCity(), createKeyboard(true)));
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
                builder().text(Commands.ORDER_CANCEL_COMMAND).build(),
                builder().text(Commands.ORDER_PREVIOUS_STEP_COMMAND).build()
        )));

        return keyboardBuilder.build();
    }

    public void doEnterCity(Long chatId, String city) {
        Matcher matcher = CITY_PATTERN.matcher(city);
        if (!matcher.find()) {
            telegramService.sendMessage(new MessageSend(chatId, "Ha introducido una ciudad incorrecta, inténtelo de nuevo."));
            return;
        }

        Order order = orderStepService.findCachedOrderByChatId(chatId);
        order.getClient().setCity(city);
        orderStepService.updateCachedOrder(chatId, order);

        executeNext(chatId);
    }

    @Override
    public void executePrevious(Long chatId) {
        OrderEnterPhoneNumberCommand.getInstance().execute(chatId);
    }

    @Override
    public void executeNext(Long chatId) {
        OrderEnterAddressCommand.getInstance().execute(chatId);
    }

}
