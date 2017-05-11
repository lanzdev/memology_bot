package com.lanzdev.commands.entity;

import com.lanzdev.commands.AbstractCommand;
import com.lanzdev.commands.Commands;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.impl.MySqlChatManager;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;

public class PauseCommand extends AbstractCommand {

    public PauseCommand() {
        super(Commands.PAUSE, "Suspend distribution from walls");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String msgHeader = "*Pause*";
        StringBuilder msgBody = new StringBuilder();
        appendMessage(msgBody, chat.getId());

        Sender sender = new MessageSender();
        sender.send(absSender, chat.getId().toString(), msgHeader);
        sender.send(absSender, chat.getId().toString(), msgBody.toString());

        updateChatLastCommand(chat.getId(), Commands.PAUSE);
    }

    private void appendMessage(StringBuilder builder, Long chatId) {

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.domain.Chat currentChat = chatManager.getById(chatId);

        if (!currentChat.isSuspended()) {
            currentChat.suspend();
            chatManager.update(currentChat);
            builder.append("Distribution suspended.\n");
        } else {
            builder.append("You have already suspended distribution.\n");
        }
        builder.append("If you want to proceed distribution print /resume");
    }
}
