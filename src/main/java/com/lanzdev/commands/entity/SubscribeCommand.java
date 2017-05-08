package com.lanzdev.commands.entity;

import com.lanzdev.commands.Commands;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.implementation.MySqlChatManager;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import com.lanzdev.util.Util;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class SubscribeCommand extends BotCommand {

    public SubscribeCommand() {
        super("subscribe", "Subscribe to new wall");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String msgHeader = "*Subscribe*";
        String msgBody = "Please enter walls url:";
        StringBuilder msgPause = new StringBuilder();
        Util.appendPauseChecking(msgPause, chat.getId());

        Sender sender = new MessageSender();
        sender.send(absSender, chat.getId().toString(), msgHeader);
        if (msgPause.length() != 0) {
            sender.send(absSender, chat.getId().toString(), msgPause.toString());
        }
        sender.send(absSender, chat.getId().toString(), msgBody);

        updateChatLastCommand(chat.getId());
    }

    private void updateChatLastCommand(Long chatId) {

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.model.entity.Chat currentChat = chatManager.getById(chatId);
        currentChat.setLastCommand(Commands.SUBSCRIBE);
        chatManager.update(currentChat);
    }
}
