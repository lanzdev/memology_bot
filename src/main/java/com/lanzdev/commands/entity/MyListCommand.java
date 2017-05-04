package com.lanzdev.commands.entity;

import com.lanzdev.commands.Commands;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.implementation.MySqlChatManager;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import com.lanzdev.util.Util;
import com.lanzdev.vk.group.GroupItem;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import java.util.List;

public class MyListCommand extends BotCommand {

    public MyListCommand( ) {
        super("my_list", "See list of public already subscribed by you");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        List<GroupItem> groupItems = Util.getSubscribedWalls(chat.getId());

        StringBuilder myListMessageBuilder = new StringBuilder();
        groupItems.forEach(group -> myListMessageBuilder
                .append(String.format("%-5d", group.getId()))
                .append("-  ").append(group.getName()).append("\n"));

        Sender sender = new MessageSender();
        sender.send(absSender, chat.getId().toString(), myListMessageBuilder.toString());

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.model.entity.Chat currentChat = chatManager.getById(chat.getId());
        currentChat.setLastCommand(Commands.MY_LIST);
        chatManager.update(currentChat);
    }
}
