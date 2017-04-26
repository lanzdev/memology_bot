package com.lanzdev.commands.entity;

import com.lanzdev.MemologyBot;
import com.lanzdev.commands.Commands;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.implementation.MySqlChatManager;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class PickCommand extends BotCommand {

    public PickCommand() {
        super("pick", "Pick public from pre picked list");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {


        StringBuilder pickMessageBuilder = new StringBuilder();
        pickMessageBuilder.append("Please enter numbers from list separated by commas");

        SendMessage pickMessage = new SendMessage();
        pickMessage.setChatId(chat.getId().toString());
        pickMessage.setText(pickMessageBuilder.toString());

        try {
            absSender.sendMessage(pickMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        MemologyBot.COMMANDS.get(Commands.LIST).getCommand()
                .execute(absSender, user, chat, arguments);

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.model.entity.Chat currentChat = chatManager.getById(chat.getId());
        currentChat.setLastCommand(Commands.PICK);
        chatManager.update(currentChat);
    }
}
