package com.lanzdev.commands.entity;

import com.lanzdev.commands.AbstractCommand;
import com.lanzdev.commands.Commands;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.impl.MySqlChatManager;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import com.lanzdev.util.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;

public class StartCommand extends AbstractCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(StartCommand.class);

    public StartCommand( ) {
        super(Commands.START, "Start to use the bot");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String msgHeader = "/start.";
        StringBuilder msgBody = new StringBuilder();
        appendStartInfo(msgBody, chat);
        String parsedMsgBody = Parser.parseMarkdown(msgBody.toString());
        Sender sender = new MessageSender();
        sender.send(absSender, chat.getId().toString(), msgHeader);
        sender.send(absSender, chat.getId().toString(), parsedMsgBody);

        updateChatLastCommand(chat.getId(), Commands.START);
    }

    private void appendStartInfo(StringBuilder builder, Chat chat) {

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.domain.Chat currentChat = chatManager.getById(chat.getId());

        if (currentChat == null) {
            currentChat = new com.lanzdev.domain.Chat();
            currentChat.setId(chat.getId());
            currentChat.setFirstName(chat.getFirstName());
            currentChat.setLastName(chat.getLastName());
            currentChat.proceed();
            chatManager.add(currentChat);
            LOGGER.debug("Joined by {} {}", currentChat.getFirstName(), currentChat.getLastName());
            builder.append("Welcome to memology bot, ");
        } else {
            currentChat.setFirstName(chat.getFirstName());
            currentChat.setLastCommand(chat.getLastName());
            chatManager.update(currentChat);
            LOGGER.debug("Rejoined by {} {}", currentChat.getFirstName(), currentChat.getLastName());
            builder.append("Welcome back to memology bot, ");
        }

        builder.append(currentChat.getFirstName()).append(". Have fun!\n")
                .append("Print /help to see more info.");
    }
}
