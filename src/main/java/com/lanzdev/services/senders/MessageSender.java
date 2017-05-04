package com.lanzdev.services.senders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Sends text message to chat
 */
public class MessageSender implements Sender {

    private static final Integer TEXT_LIMIT = 4096;
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSender.class);

    @Override
    public void send(AbsSender absSender, String chatId, String data) {

        while (data != null) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId);
            String message;
            if (data.length() < TEXT_LIMIT) {
                message = data;
                data = null;
            } else {
                message = data.substring(0, TEXT_LIMIT);
                data = data.substring(TEXT_LIMIT);
            }
            message = message.replace("<br>", "\n");
            sendMessage.setText(message);
            sendMessage.setParseMode("Markdown");

            try {
                absSender.sendMessage(sendMessage);
                LOGGER.debug("Successfully sent message to chat {}, message length = {}.",
                        chatId, sendMessage.getText().length());
            } catch (TelegramApiException e) {
                LOGGER.error("Cannot send message to chat {}, message: {}.",
                        chatId, sendMessage.getText(), e);
            }
        }

    }
}
