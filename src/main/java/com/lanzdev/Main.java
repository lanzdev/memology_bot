package com.lanzdev;

import com.lanzdev.managers.entity.SubscriptionManager;
import com.lanzdev.managers.entity.WallManager;
import com.lanzdev.managers.mysql.implementation.MySqlSubscriptionManager;
import com.lanzdev.managers.mysql.implementation.MySqlWallManager;
import com.lanzdev.model.entity.Subscription;
import com.lanzdev.model.entity.Wall;
import com.lanzdev.vk.group.GroupItem;
import com.lanzdev.vk.group.VkGroupGetter;
import com.lanzdev.vk.wall.Photo;
import com.lanzdev.vk.wall.VkWallGetter;
import com.lanzdev.vk.wall.WallItem;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {

    public static void main(String[] args) {

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        MemologyBot bot = new MemologyBot();
        try {
            telegramBotsApi.registerBot(bot);
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

        Main main = new Main();
        main.distribution(bot);
    }

    private void distribution(MemologyBot bot) {

        SubscriptionManager subscriptionManager = new MySqlSubscriptionManager();
        WallManager wallManager = new MySqlWallManager();

        new Runnable() {

            @Override
            public void run( ) {

                while (true) {
                    List<Subscription> subscriptions = subscriptionManager.getAll();
                    subscriptions.stream()
                            .forEach(item -> {
                                Wall wall = wallManager.getByDomain(item.getWallDomain());
                                sendMessages(wall, item, bot);
                                subscriptionManager.update(item);
                            });
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.run();
    }

    private void sendMessages(Wall wall, Subscription subscription, MemologyBot bot) {

        VkWallGetter vkWallGetter = new VkWallGetter();
        List<WallItem> wallItems = vkWallGetter.getItems(wall.getWallDomain(), 10, 0);
        List<WallItem> newItems = pickNewest(wallItems, subscription.getLastPostId());
        if (newItems.size() != 0) {
            sendPostsOwner(wall, subscription, bot);
            newItems.stream()
                    .forEach(item -> {
                        sendPostsText(item, subscription, bot);
                        item.getPhotos().stream()
                                .forEach(photo -> {
                                    sendPostsPhoto(photo, subscription, bot);
                                });
                    });
            subscription.setLastPostId(newItems.get(newItems.size() - 1).getId());
        }
    }

    private void sendPostsOwner(Wall wall, Subscription subscription, MemologyBot bot) {

        GroupItem groupItem = new VkGroupGetter().getItems(Collections.singletonList(wall)).iterator().next();

        StringBuilder sendPublicBuilder = new StringBuilder();
        sendPublicBuilder.append("<b>").append(groupItem.getName()).append("</b>");

        SendMessage sendPublic = new SendMessage();
        sendPublic.setChatId(subscription.getChatId());
        sendPublic.enableHtml(true);
        sendPublic.setText(sendPublicBuilder.toString());
        try {
            bot.sendMessage(sendPublic);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void sendPostsText(WallItem item, Subscription subscription, MemologyBot bot) {

        if (item.getText() != null && !item.getText().isEmpty()) {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(subscription.getChatId());
            sendMessage.setText(item.getText());
            try {
                bot.sendMessage(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendPostsPhoto(Photo photo, Subscription subscription, MemologyBot bot) {

        if (photo.getSrcBig() != null && !photo.getSrcBig().isEmpty()) {
            SendPhoto sendPhoto = new SendPhoto();
            sendPhoto.setChatId(subscription.getChatId());
            sendPhoto.setPhoto(photo.getSrcBig());
            try {
                bot.sendPhoto(sendPhoto);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private List<WallItem> pickNewest(List<WallItem> wallItems, Long lastPostId) {

        List<WallItem> list = new ArrayList<>();

        int last = 0;
        for (int i = 0; i < wallItems.size(); i++) {
            if (wallItems.get(i).getId().equals(lastPostId)) {
                last = i;
                break;
            }
        }

        if (lastPostId != 0) {
            for (int i = last + 1; i < wallItems.size(); i++) {
                list.add(wallItems.get(i));
            }
        } else {
            list = Collections.singletonList(wallItems.get(wallItems.size() - 1));
        }

        return list;
    }
}
