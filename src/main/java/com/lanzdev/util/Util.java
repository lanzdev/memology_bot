package com.lanzdev.util;

import com.lanzdev.domain.Chat;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.entity.SubscriptionManager;
import com.lanzdev.managers.entity.WallManager;
import com.lanzdev.managers.mysql.impl.MySqlChatManager;
import com.lanzdev.managers.mysql.impl.MySqlSubscriptionManager;
import com.lanzdev.managers.mysql.impl.MySqlWallManager;
import com.lanzdev.domain.Subscription;
import com.lanzdev.domain.Wall;
import com.lanzdev.vk.group.GroupItem;
import com.lanzdev.vk.group.VkGroupGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Util {

    private static final Logger LOGGER = LoggerFactory.getLogger(Util.class);

    public static String getResponse(String _url) throws IOException {

        URL url = new URL(_url);
        String response;
        try (Scanner scanner = new Scanner(url.openStream())) {
            StringBuilder sb = new StringBuilder();
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
            response = sb.toString();
        }

        return response;
    }

    public static List<GroupItem> getSubscribedWalls(Long chatId) {

        SubscriptionManager subscriptionManager = new MySqlSubscriptionManager();
        WallManager wallManager = new MySqlWallManager();
        List<Subscription> subscriptions = subscriptionManager.getByChatId(chatId);
        List<Wall> walls = new ArrayList<>();
        subscriptions.forEach(item -> {
                    if (item.isActive()) {
                        walls.add(wallManager.getByDomain(item.getWallDomain()));
                    }
                }
        );

        VkGroupGetter groupGetter = new VkGroupGetter();

        return groupGetter.getItems(walls);
    }

    public static void appendPauseChecking(StringBuilder builder, Long chatId) {

        ChatManager chatManager = new MySqlChatManager();
        Chat currentChat = chatManager.getById(chatId);

        if (currentChat.isSuspended()) {
            builder.append("You have suspended distribution, if you want to proceed it print /resume\n");
        }
    }
}
