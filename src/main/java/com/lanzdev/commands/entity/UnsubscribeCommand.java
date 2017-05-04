package com.lanzdev.commands.entity;

import com.lanzdev.MemologyBot;
import com.lanzdev.commands.Commands;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.implementation.MySqlChatManager;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class UnsubscribeCommand extends BotCommand{

    public UnsubscribeCommand() {
        super("unsubscribe", "Unsubscribe form public distribution");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String unsubscribeMessage = "Please print publics ids you want to unsubscribe from separated by commas.";

        Sender sender = new MessageSender();
        sender.send(absSender, chat.getId().toString(), unsubscribeMessage.toLowerCase());

        MemologyBot.COMMANDS.get(Commands.MY_LIST).getCommand()
                .execute(absSender, user, chat, arguments);

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.model.entity.Chat currentChat = chatManager.getById(chat.getId());
        currentChat.setLastCommand(Commands.UNSUBSCRIBE);
        chatManager.update(currentChat);
    }
}
