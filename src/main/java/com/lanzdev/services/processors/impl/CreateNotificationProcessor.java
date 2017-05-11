package com.lanzdev.services.processors.impl;

import com.lanzdev.domain.Chat;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.impl.MySqlChatManager;
import com.lanzdev.services.processors.AbstractProcessor;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import com.lanzdev.util.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.AbsSender;

import java.util.List;

public class CreateNotificationProcessor extends AbstractProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateNotificationProcessor.class);

    public CreateNotificationProcessor(Message message, AbsSender absSender) {
        super(message, absSender);
    }

    @Override
    public void process( ) {

        if (message.getText() == null || message.getText().isEmpty()) {
            LOGGER.debug("Notification message doesn't contain any text.");
            return;
        }
        String msgBody = Parser.parseMarkdown(message.getText());
        String msgHeader = "#Notification";

        ChatManager chatManager = new MySqlChatManager();
        List<Chat> chatList = chatManager.getAll();
        if (chatList.size() == 0) {
            LOGGER.debug("Bot isn't subscribed by at least one chat.");
            return;
        }
        Sender sender = new MessageSender();
        chatList.forEach(chat -> {
            sender.send(bot, chat.getId().toString(), msgHeader);
            sender.send(bot, chat.getId().toString(), msgBody);
        });
        LOGGER.info("Sent notification \"{}\" to {} chats.", msgBody, chatList.size());
        String result = String.format("Your notification was successfully sent to %d chats.", chatList.size());
        sender.send(bot, message.getChatId().toString(), result);
    }
}
