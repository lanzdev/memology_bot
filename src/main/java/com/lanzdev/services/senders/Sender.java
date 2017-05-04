package com.lanzdev.services.senders;

import org.telegram.telegrambots.bots.AbsSender;

public interface Sender {

    void send(AbsSender absSender, String chatId, String data);
}
