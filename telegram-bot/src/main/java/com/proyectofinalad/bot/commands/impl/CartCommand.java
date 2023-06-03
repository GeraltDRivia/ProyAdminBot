package com.proyectofinalad.bot.commands.impl;

import static java.util.Arrays.asList;
import static org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton.builder;
import static com.proyectofinalad.bot.models.domain.MessagePlaceholder.of;

import java.util.List;

import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

import com.proyectofinalad.bot.commands.Command;
import com.proyectofinalad.bot.commands.impl.order.OrderProcessCommand;
import com.proyectofinalad.bot.models.domain.CartItem;
import com.proyectofinalad.bot.models.domain.MessageEdit;
import com.proyectofinalad.bot.models.domain.MessageSend;
import com.proyectofinalad.bot.models.entities.Message;
import com.proyectofinalad.bot.models.entities.Product;
import com.proyectofinalad.bot.services.CartService;
import com.proyectofinalad.bot.services.MessageService;
import com.proyectofinalad.bot.services.OrderStepService;
import com.proyectofinalad.bot.services.TelegramService;
import com.proyectofinalad.bot.services.impl.CartServiceDefault;
import com.proyectofinalad.bot.services.impl.MessageServiceCached;
import com.proyectofinalad.bot.services.impl.OrderStepServiceDefault;
import com.proyectofinalad.bot.services.impl.TelegramServiceDefault;

public class CartCommand implements Command<Long> {

    private static final CartCommand INSTANCE = new CartCommand();

    private final TelegramService telegramService = TelegramServiceDefault.getInstance();
    private final CartService cartService = CartServiceDefault.getInstance();
    private final OrderStepService orderStepService = OrderStepServiceDefault.getInstance();
    private final MessageService messageService = MessageServiceCached.getInstance();

    private static final int MAX_QUANTITY_PER_PRODUCT = 50;

    private CartCommand() {
    }

    public static CartCommand getInstance() {
        return INSTANCE;
    }

    @Override
    public void execute(Long chatId) {
        List<CartItem> cartItems = cartService.findAllCartItemsByChatId(chatId);
        cartService.updatePageNumberByChatId(chatId, 0);

        if (cartItems.isEmpty()) {
            telegramService.sendMessage(new MessageSend(chatId, "Carrito vacío."));
            return;
        }

        telegramService.sendMessage(new MessageSend(chatId,
                createProductText(cartItems.get(0)), createCartKeyboard(cartItems, 0)));
    }

    private String createProductText(CartItem cartItem) {
        Message message = messageService.findByName("CART_MESSAGE");
        if (cartItem != null) {
            Product product = cartItem.getProduct();
            message.applyPlaceholder(of("%PRODUCT_NAME%", product.getName()));
            message.applyPlaceholder(of("%PRODUCT_DESCRIPTION%", product.getDescription()));
            message.applyPlaceholder(of("%PRODUCT_PRICE%", product.getPrice()));
            message.applyPlaceholder(of("%PRODUCT_QUANTITY%", cartItem.getQuantity()));
            message.applyPlaceholder(of("%PRODUCT_TOTAL_PRICE%", product.getPrice() * cartItem.getQuantity()));
        }
        return message.buildText();
    }

    private InlineKeyboardMarkup createCartKeyboard(List<CartItem> cartItems, int currentCartPage) {
        InlineKeyboardMarkup.InlineKeyboardMarkupBuilder keyboardBuilder = InlineKeyboardMarkup.builder();

        keyboardBuilder.keyboardRow(asList(
                builder().text("\u2716").callbackData("cart=delete-product").build(),
                builder().text("\u2796").callbackData("cart=minus-product").build(),
                builder().text(cartItems.get(currentCartPage).getQuantity() + " Cant.").callbackData("cart=product-quantity").build(),
                builder().text("\u2795").callbackData("cart=plus-product").build()
        ));

        keyboardBuilder.keyboardRow(asList(
                builder().text("\u25c0").callbackData("cart=previous-product").build(),
                builder().text((currentCartPage + 1) + "/" + cartItems.size()).callbackData("cart=current-page").build(),
                builder().text("\u25b6").callbackData("cart=next-product").build()
        ));

        keyboardBuilder.keyboardRow(asList(
                builder()
                        .text(String.format("\u2705 Pedido por Q. %d  ¿desea pagar?", cartService.calculateTotalPrice(cartItems)))
                        .callbackData("cart=process-order")
                        .build()
        ));

        return keyboardBuilder.build();
    }

    public void doDeleteProduct(Long chatId, Integer messageId) {
        List<CartItem> cartItems = cartService.findAllCartItemsByChatId(chatId);
        int currentCartPage = cartService.findPageNumberByChatId(chatId);

        if (!cartItems.isEmpty()) {
            CartItem cartItem = cartItems.get(currentCartPage);
            if (cartItem != null) {
                cartItems.remove(cartItem);
                cartService.deleteCartItem(chatId, cartItem.getId());
            }
        }

        if (cartItems.isEmpty()) {
            telegramService.editMessageText(new MessageEdit(chatId, messageId, "Carrito vacíado."));
            return;
        }

        if (cartItems.size() == currentCartPage) {
            currentCartPage -= 1;
            cartService.updatePageNumberByChatId(chatId, currentCartPage);
        }

        telegramService.editMessageText(new MessageEdit(chatId, messageId,
                createProductText(cartItems.get(currentCartPage)), createCartKeyboard(cartItems, currentCartPage)));
    }

    public void doMinusProduct(Long chatId, Integer messageId) {
        List<CartItem> cartItems = cartService.findAllCartItemsByChatId(chatId);
        int currentCartPage = cartService.findPageNumberByChatId(chatId);

        if (cartItems.isEmpty()) {
            telegramService.editMessageText(new MessageEdit(chatId, messageId, "Carrito vacío."));
            return;
        }

        CartItem cartItem = cartItems.get(currentCartPage);

        if (cartItem != null && cartItem.getQuantity() > 1) {
            cartItem.setQuantity(cartItem.getQuantity() - 1);
            cartService.updateCartItem(chatId, cartItem);

            telegramService.editMessageText(new MessageEdit(
                    chatId, messageId, createProductText(cartItem), createCartKeyboard(cartItems, currentCartPage)));
        }
    }

    public void doPlusProduct(Long chatId, Integer messageId) {
        List<CartItem> cartItems = cartService.findAllCartItemsByChatId(chatId);
        int currentCartPage = cartService.findPageNumberByChatId(chatId);

        if (cartItems.isEmpty()) {
            telegramService.editMessageText(new MessageEdit(chatId, messageId, "Carrito vacío."));
            return;
        }

        CartItem cartItem = cartItems.get(currentCartPage);

        if (cartItem != null && cartItem.getQuantity() < MAX_QUANTITY_PER_PRODUCT) {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
            cartService.updateCartItem(chatId, cartItem);

            telegramService.editMessageText(new MessageEdit(
                    chatId, messageId, createProductText(cartItem), createCartKeyboard(cartItems, currentCartPage)));
        }
    }

    public void doPreviousProduct(Long chatId, Integer messageId) {
        List<CartItem> cartItems = cartService.findAllCartItemsByChatId(chatId);
        int currentCartPage = cartService.findPageNumberByChatId(chatId);

        if (cartItems.isEmpty()) {
            telegramService.editMessageText(new MessageEdit(chatId, messageId, "Carrito vacío."));
            return;
        }
        if (cartItems.size() == 1) {
            return;
        }

        if (currentCartPage <= 0) {
            currentCartPage = cartItems.size() - 1;
        } else {
            currentCartPage -= 1;
        }
        cartService.updatePageNumberByChatId(chatId, currentCartPage);

        telegramService.editMessageText(new MessageEdit(chatId, messageId,
                createProductText(cartItems.get(currentCartPage)), createCartKeyboard(cartItems, currentCartPage)));
    }

    public void doNextProduct(Long chatId, Integer messageId) {
        List<CartItem> cartItems = cartService.findAllCartItemsByChatId(chatId);
        int currentCartPage = cartService.findPageNumberByChatId(chatId);

        if (cartItems.isEmpty()) {
            telegramService.editMessageText(new MessageEdit(chatId, messageId, "Carrito vacío."));
            return;
        }
        if (cartItems.size() == 1) {
            return;
        }

        if (currentCartPage >= cartItems.size() - 1) {
            currentCartPage = 0;
        } else {
            currentCartPage += 1;
        }
        cartService.updatePageNumberByChatId(chatId, currentCartPage);

        telegramService.editMessageText(new MessageEdit(chatId, messageId,
                createProductText(cartItems.get(currentCartPage)), createCartKeyboard(cartItems, currentCartPage)));
    }

    public void doProcessOrder(Long chatId, Integer messageId) {
        if (cartService.findAllCartItemsByChatId(chatId).isEmpty()) {
            telegramService.editMessageText(new MessageEdit(chatId, messageId, "Carrito vacío." ));
            return;
        }

        telegramService.editMessageText(new MessageEdit(chatId, messageId, "Creando orden..."));
        orderStepService.updateOrderStepByChatId(chatId, OrderProcessCommand.getInstance());
        orderStepService.executeCurrentOrderStep(chatId);
    }

}
