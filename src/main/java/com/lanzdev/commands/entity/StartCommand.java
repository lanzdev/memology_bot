package com.lanzdev.commands.entity;

import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.implementation.MySqlChatManager;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class StartCommand extends BotCommand {

    public StartCommand( ) {
        super("start", "Start to use the bot");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.model.entity.Chat currentChat = chatManager.getById(chat.getId());

        StringBuilder startMessageBuilder = new StringBuilder();

        if (currentChat == null) {
            currentChat = new com.lanzdev.model.entity.Chat();
            currentChat.setId(chat.getId());
            currentChat.setFirstName(chat.getFirstName());
            currentChat.setLastName(chat.getLastName());
            currentChat.setSuspended(false);
            currentChat.setLastCommand("start");
            chatManager.add(currentChat);
            startMessageBuilder.append("Welcome to memology bot, ");
        } else {
            currentChat.setFirstName(chat.getFirstName());
            currentChat.setLastName(chat.getLastName());
            currentChat.setLastCommand("start");
            chatManager.update(currentChat);
            startMessageBuilder.append("Welcome back, ");
        }

        startMessageBuilder.append(currentChat.getFirstName())
                .append(". Have fun!");

        SendMessage startMessage = new SendMessage();
        startMessage.setChatId(chat.getId());
        startMessage.setText(startMessageBuilder.toString());

        try {
            absSender.sendMessage(startMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
