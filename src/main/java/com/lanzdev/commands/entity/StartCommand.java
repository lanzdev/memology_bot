package com.lanzdev.commands.entity;

import com.lanzdev.commands.Commands;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.implementation.MySqlChatManager;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import com.lanzdev.util.MarkdownParser;
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

        String msgHeader = "*Start*";
        StringBuilder msgBody = new StringBuilder();
        appendStartInfo(msgBody, chat);
        String parsedMsgBody = MarkdownParser.parse(msgBody.toString());
        Sender sender = new MessageSender();
        sender.send(absSender, chat.getId().toString(), msgHeader);
        sender.send(absSender, chat.getId().toString(), parsedMsgBody);

        updateChatLastCommand(chat.getId());
    }

    private void appendStartInfo(StringBuilder builder, Chat chat) {

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.model.entity.Chat currentChat = chatManager.getById(chat.getId());

        if (currentChat == null) {
            currentChat = new com.lanzdev.model.entity.Chat();
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

    private void updateChatLastCommand(Long chatId) {

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.model.entity.Chat currentChat = chatManager.getById(chatId);
        currentChat.setLastCommand(Commands.START);
        chatManager.update(currentChat);
    }
}
