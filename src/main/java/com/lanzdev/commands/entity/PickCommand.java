package com.lanzdev.commands.entity;

import com.lanzdev.commands.AbstractCommand;
import com.lanzdev.commands.Commands;
import com.lanzdev.domain.Subscription;
import com.lanzdev.domain.Wall;
import com.lanzdev.managers.entity.SubscriptionManager;
import com.lanzdev.managers.entity.WallManager;
import com.lanzdev.managers.mysql.impl.MySqlSubscriptionManager;
import com.lanzdev.managers.mysql.impl.MySqlWallManager;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import com.lanzdev.util.Util;
import com.lanzdev.vk.group.GroupItem;
import com.lanzdev.vk.group.VkGroupGetter;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;

import java.util.ArrayList;
import java.util.List;

public class PickCommand extends AbstractCommand {

    public PickCommand( ) {
        super(Commands.PICK, "Pick public from pre picked list");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String msgHeader = "*Pick*";
        StringBuilder msgBody = new StringBuilder();
        StringBuilder msgPause = new StringBuilder();
        appendRecommendedUnsubscribedWalls(msgBody, chat.getId());
        Util.appendPauseChecking(msgPause, chat.getId());

        Sender sender = new MessageSender();
        sender.send(absSender, chat.getId().toString(), msgHeader);
        if (msgPause.length() != 0) {
            sender.send(absSender, chat.getId().toString(), msgPause.toString());
        }
        sender.send(absSender, chat.getId().toString(), msgBody.toString());

        updateChatLastCommand(chat.getId(), Commands.PICK);
    }


    private void appendRecommendedUnsubscribedWalls(StringBuilder builder, Long chatId) {

        SubscriptionManager subscriptionManager = new MySqlSubscriptionManager();
        WallManager wallManager = new MySqlWallManager();
        List<Wall> walls = wallManager.getAllApproved();
        List<Subscription> subscriptions = subscriptionManager.getByChatId(chatId);
        List<Wall> notSubscribedWalls = new ArrayList<>(walls);

        // TODO: change on lambda
        for (int i = 0; i < walls.size(); i++) {
            for (int j = 0; j < subscriptions.size(); j++) {
                if (subscriptions.get(j).getWallDomain().equals(walls.get(i).getWallDomain())
                        && subscriptions.get(j).isActive()) {
                    notSubscribedWalls.remove(walls.get(i));
                }
            }
        }

        VkGroupGetter groupGetter = new VkGroupGetter();
        List<GroupItem> groupItems = groupGetter.getItems(notSubscribedWalls);
        groupItems.stream()
                .forEach(group -> {
                    String command = String.format("/pick_%-3d", group.getId());
                    builder.append(
                            String.format("[%s -  %s](%s)", command, group.getName(), command))
                            .append("\n");

                });

        if (groupItems.size() != 0) {
            builder.insert(0, "\n")
                    .insert(0, "Or you may print ids of walls you want to pick separated by commas.")
                    .insert(0, "\n");
            builder.insert(0, "Please click on walls from list if you want to pick it.");
        } else {
            builder.append("You have already picked all recommended walls");
        }
    }
}
