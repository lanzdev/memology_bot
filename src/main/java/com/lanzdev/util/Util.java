package com.lanzdev.util;

import com.lanzdev.managers.entity.SubscriptionManager;
import com.lanzdev.managers.entity.WallManager;
import com.lanzdev.managers.mysql.implementation.MySqlSubscriptionManager;
import com.lanzdev.managers.mysql.implementation.MySqlWallManager;
import com.lanzdev.model.entity.Subscription;
import com.lanzdev.model.entity.Wall;
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
}
