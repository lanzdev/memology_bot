package com.lanzdev.commands.entity;

import com.lanzdev.commands.Commands;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.implementation.MySqlChatManager;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class SubscribeCommand extends BotCommand {

    public SubscribeCommand() {
        super("subscribe", "Subscribe to new public");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String subscribeText = "Please enter publics url";
        SendMessage subscribeMessage = new SendMessage();
        subscribeMessage.setChatId(chat.getId().toString());
        subscribeMessage.setText(subscribeText);
        try {
            absSender.sendMessage(subscribeMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.model.entity.Chat currentChat = chatManager.getById(chat.getId());
        currentChat.setLastCommand(Commands.SUBSCRIBE);
        chatManager.update(currentChat);
    }
}
