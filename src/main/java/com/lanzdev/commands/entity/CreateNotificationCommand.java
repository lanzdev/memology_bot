package com.lanzdev.commands.entity;

import com.lanzdev.commands.AbstractCommand;
import com.lanzdev.commands.Commands;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.impl.MySqlChatManager;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;

public class CreateNotificationCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreateNotificationCommand.class);

    public CreateNotificationCommand() {
        super(Commands.CREATE_NOTIFICATION, "Create notification which will be distributed to all chats");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String msgHeader = "*Create Notification*";
        String msgBody = "Print notification which will be sent to each chat which is subscribed on this bot.";
        Sender sender = new MessageSender();

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.domain.Chat currentChat = chatManager.getById(chat.getId());
        if (!currentChat.isAdmin()) {
            LOGGER.debug("Non admin chat is trying to execute command {}.", Commands.CREATE_NOTIFICATION);
            String error = "You don't have enough permissions for this command!";
            sender.send(absSender, chat.getId().toString(), error);
            return;
        }
        sender.send(absSender, chat.getId().toString(), msgHeader);
        sender.send(absSender, chat.getId().toString(), msgBody);

        LOGGER.debug("{} is successfully executed.", Commands.CREATE_NOTIFICATION);
        updateChatLastCommand(chat.getId(), Commands.CREATE_NOTIFICATION);
    }
}
