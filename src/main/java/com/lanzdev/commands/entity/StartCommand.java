package com.lanzdev.commands.entity;

import com.lanzdev.MemologyBot;
import com.lanzdev.commands.Commands;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.implementation.MySqlChatManager;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class StartCommand extends BotCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartCommand.class);

    public StartCommand( ) {
        super("start", "Start to use the bot");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.model.entity.Chat currentChat = chatManager.getById(chat.getId());

        StringBuilder startMessageBuilder = new StringBuilder();
        if (currentChat == null) {
            currentChat = createChat(chat);
            chatManager.add(currentChat);
            LOGGER.debug("Joined by {} {}", currentChat.getFirstName(), currentChat.getLastName());
            startMessageBuilder.append("Welcome to memology bot, ");
        } else {
            updateChat(currentChat, chat);
            chatManager.update(currentChat);
            LOGGER.debug("Rejoined by {} {}", currentChat.getFirstName(), currentChat.getLastName());
            startMessageBuilder.append("Welcome back to memology bot, ");
        }

        startMessageBuilder.append(currentChat.getFirstName())
                .append(". Have fun!");
        Sender sender = new MessageSender();
        sender.send(absSender, chat.getId().toString(), startMessageBuilder.toString());

        MemologyBot.COMMANDS.get(Commands.HELP).getCommand()
                .execute(absSender, user, chat, arguments);

        currentChat.setLastCommand("start");
        chatManager.update(currentChat);
    }

    private com.lanzdev.model.entity.Chat createChat(Chat chat) {

        com.lanzdev.model.entity.Chat createdChat = new com.lanzdev.model.entity.Chat();
        createdChat.setId(chat.getId());
        createdChat.setFirstName(chat.getFirstName());
        createdChat.setLastName(chat.getLastName());
        createdChat.setSuspended(false);
        return createdChat;
    }

    private void updateChat(com.lanzdev.model.entity.Chat forUpdate, Chat chat) {

        forUpdate.setFirstName(chat.getFirstName());
        forUpdate.setLastCommand(chat.getLastName());
    }
}
