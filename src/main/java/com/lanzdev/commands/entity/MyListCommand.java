package com.lanzdev.commands.entity;

import com.lanzdev.commands.Commands;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.entity.SubscriptionManager;
import com.lanzdev.managers.entity.WallManager;
import com.lanzdev.managers.mysql.implementation.MySqlChatManager;
import com.lanzdev.managers.mysql.implementation.MySqlSubscriptionManager;
import com.lanzdev.managers.mysql.implementation.MySqlWallManager;
import com.lanzdev.model.entity.Subscription;
import com.lanzdev.model.entity.Wall;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import com.lanzdev.util.MarkdownParser;
import com.lanzdev.vk.group.GroupItem;
import com.lanzdev.vk.group.VkGroupGetter;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

public class MyListCommand extends BotCommand {

    public MyListCommand( ) {
        super("my_list", "See list of public already subscribed by you");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        SubscriptionManager subscriptionManager = new MySqlSubscriptionManager();
        WallManager wallManager = new MySqlWallManager();

        List<Subscription> subscriptions = subscriptionManager.getByChatId(chat.getId());

        List<Wall> walls = new ArrayList<>();
        subscriptions.stream()
                .forEach(item -> {
                    walls.add(wallManager.getByDomain(item.getWallDomain()));
                });

        VkGroupGetter groupGetter = new VkGroupGetter();
        List<GroupItem> groupItems = groupGetter.getItems(walls);

        StringBuilder myListMessageBuilder = new StringBuilder();
        groupItems.stream()
                .forEach(group -> myListMessageBuilder
                        .append(String.format("%-5d", group.getId()))
                        .append("-  ").append(group.getName()).append("\n"));
        myListMessageBuilder.deleteCharAt(myListMessageBuilder.length() - 1);
        String message = MarkdownParser.parse(myListMessageBuilder.toString());
        Sender sender = new MessageSender();
        sender.send(absSender, chat.getId().toString(), message);

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.model.entity.Chat currentChat = chatManager.getById(chat.getId());
        currentChat.setLastCommand(Commands.MY_LIST);
        chatManager.update(currentChat);
    }
}
