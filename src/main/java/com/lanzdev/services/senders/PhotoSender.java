package com.lanzdev.services.senders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

/**
 * Sends photo to chat
 */
public class PhotoSender implements Sender {

    private static final Logger LOGGER = LoggerFactory.getLogger(PhotoSender.class);

    @Override
    public void send(AbsSender absSender, String chatId, String data) {

        SendPhoto sendPhoto = new SendPhoto();
        sendPhoto.setChatId(chatId.toString());
        sendPhoto.setPhoto(data);
        try {
            absSender.sendPhoto(sendPhoto);
            LOGGER.debug("Successfully sent photo to chat {}, src: {}.", chatId, data);
        } catch (TelegramApiException e) {
            LOGGER.error("Cannot send photo to chat {}, src: {}.", chatId, data);
        }
    }
}
