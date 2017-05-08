package com.lanzdev.commands.entity;

import com.lanzdev.commands.Commands;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.implementation.MySqlChatManager;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class LoginCommand extends BotCommand {

    public LoginCommand() {
        super("login", "Login to change list of pre picked publics");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String msgHeader = "*Login*";
        StringBuilder msgBody = new StringBuilder();



        updateChatLastCommand(chat.getId());
    }

    private void appendLoginMessage(StringBuilder builder, Long chatId) {


    }

    private void updateChatLastCommand(Long chatId) {

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.model.entity.Chat currentChat = chatManager.getById(chatId);
        currentChat.setLastCommand(Commands.LOGIN);
        chatManager.update(currentChat);
    }
}
