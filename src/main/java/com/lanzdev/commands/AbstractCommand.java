package com.lanzdev.commands;

import com.lanzdev.domain.Chat;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.impl.MySqlChatManager;
import org.telegram.telegrambots.bots.commands.BotCommand;

public abstract class AbstractCommand extends BotCommand {

    public AbstractCommand(String commandIdentifier, String description) {
        super(commandIdentifier, description);
    }

    protected void updateChatLastCommand(Long chatId, String command) {

        ChatManager chatManager = new MySqlChatManager();
        Chat currentChat = chatManager.getById(chatId);
        currentChat.setLastCommand(command);
        chatManager.update(currentChat);
    }
}
