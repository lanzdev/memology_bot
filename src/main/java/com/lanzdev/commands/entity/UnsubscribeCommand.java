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

public class UnsubscribeCommand extends BotCommand{

    public UnsubscribeCommand() {
        super("unsubscribe", "Unsubscribe form public distribution");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String unsubscribeMessage = "Enter numbers from list separated by commas";

        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chat.getId());
        sendMessage.setText(unsubscribeMessage);
        try {
            absSender.sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

        MemologyBot.COMMANDS.get(Commands.MY_LIST).getCommand()
                .execute(absSender, user, chat, arguments);

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.model.entity.Chat currentChat = chatManager.getById(chat.getId());
        currentChat.setLastCommand(Commands.UNSUBSCRIBE);
        chatManager.update(currentChat);
    }
}
