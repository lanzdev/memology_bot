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

public class ResumeCommand extends AbstractCommand {

    public ResumeCommand() {
        super(Commands.RESUME, "Proceed distribution from publics");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String msgHeader = "*Resume*";
        StringBuilder msgBody = new StringBuilder();
        appendMessage(msgBody, chat.getId());

        Sender sender = new MessageSender();
        sender.send(absSender, chat.getId().toString(), msgHeader);
        sender.send(absSender, chat.getId().toString(), msgBody.toString());

        updateChatLastCommand(chat.getId(), Commands.RESUME);
    }

    private void appendMessage(StringBuilder builder, Long chatId) {

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.domain.Chat currentChat = chatManager.getById(chatId);

        if (currentChat.isSuspended()) {
            currentChat.proceed();
            chatManager.update(currentChat);
            builder.append("Distribution proceeded.\n");
        } else {
            builder.append("You have already proceeded distribution.\n");
        }
        builder.append("If you want to suspend distribution print /pause");
    }
}
