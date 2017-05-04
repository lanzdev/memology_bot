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
import com.lanzdev.vk.group.GroupItem;
import com.lanzdev.vk.group.VkGroupGetter;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import java.util.ArrayList;
import java.util.List;

public class PickCommand extends BotCommand {

    public PickCommand( ) {
        super("pick", "Pick public from pre picked list");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        StringBuilder pickHeaderBuilder = new StringBuilder();
        pickHeaderBuilder.append("Please click on group from list if you want to pick it.").append("\n");
        pickHeaderBuilder.append("Or you may print ids of groups you want to pick separated by commas.");

        SubscriptionManager subscriptionManager = new MySqlSubscriptionManager();
        WallManager wallManager = new MySqlWallManager();
        List<Wall> walls = wallManager.getAllApproved();
        List<Subscription> subscriptions = subscriptionManager.getByChatId(chat.getId());
        List<Wall> notSubscribedWalls = new ArrayList<>();

        // TODO: change on lambda
        for (int i = 0; i < walls.size(); i++) {
            for (int j = 0; j < subscriptions.size(); j++) {
                if (subscriptions.get(j).getWallDomain().equals(walls.get(i).getWallDomain())
                        && subscriptions.get(j).isActive()) {
                    break;
                } else if (j == subscriptions.size() - 1) {
                    notSubscribedWalls.add(walls.get(i));
                }
            }
        }

        VkGroupGetter groupGetter = new VkGroupGetter();
        List<GroupItem> groupItems = groupGetter.getItems(notSubscribedWalls);
        StringBuilder pickMessageBuilder = new StringBuilder();
        groupItems.stream()
                .forEach(group -> {
                    String command = String.format("/pick_%-3d", group.getId());
                    pickMessageBuilder.append(
                            String.format("[%s -  %s](%s)", command, group.getName(), command))
                            .append("\n");
                });

        Sender sender = new MessageSender();
        if (groupItems.size() != 0) {
            sender.send(absSender, chat.getId().toString(), pickHeaderBuilder.toString());
            sender.send(absSender, chat.getId().toString(), pickMessageBuilder.toString());
        } else {
            String noWallsForPick = "You have already picked all available walls";
            sender.send(absSender, chat.getId().toString(), noWallsForPick);
        }


        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.model.entity.Chat currentChat = chatManager.getById(chat.getId());
        currentChat.setLastCommand(Commands.PICK);
        chatManager.update(currentChat);
    }
}
