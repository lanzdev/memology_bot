package com.lanzdev.commands.entity;

import com.lanzdev.commands.Commands;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.implementation.MySqlChatManager;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class PauseCommand extends BotCommand {

    public PauseCommand() {
        super("pause", "Suspend distribution from walls");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String msgHeader = "*Pause*";
        StringBuilder msgBody = new StringBuilder();
        appendMessage(msgBody, chat.getId());

        Sender sender = new MessageSender();
        sender.send(absSender, chat.getId().toString(), msgHeader);
        sender.send(absSender, chat.getId().toString(), msgBody.toString());

        updateChatLastCommand(chat.getId());
    }

    private void appendMessage(StringBuilder builder, Long chatId) {

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.model.entity.Chat currentChat = chatManager.getById(chatId);

        if (!currentChat.isSuspended()) {
            currentChat.suspend();
            chatManager.update(currentChat);
            builder.append("Distribution suspended.\n");
        } else {
            builder.append("You have already suspended distribution.\n");
        }
        builder.append("If you want to proceed distribution print /resume");
    }

    private void updateChatLastCommand(Long chatId) {

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.model.entity.Chat currentChat = chatManager.getById(chatId);
        currentChat.setLastCommand(Commands.PAUSE);
        chatManager.update(currentChat);

    }
}
