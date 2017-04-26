package com.lanzdev;

import com.lanzdev.managers.entity.SubscriptionManager;
import com.lanzdev.managers.entity.WallManager;
import com.lanzdev.managers.mysql.implementation.MySqlSubscriptionManager;
import com.lanzdev.managers.mysql.implementation.MySqlWallManager;
import com.lanzdev.model.entity.Subscription;
import com.lanzdev.model.entity.Wall;
import com.lanzdev.vk.group.GroupItem;
import com.lanzdev.vk.group.VkGroupGetter;
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
                                sendMessages(wall, item.getChatId(), bot);
                                wallManager.update(wall);
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

    private void sendMessages(Wall wall, Long chatId, MemologyBot bot) {

        VkWallGetter vkWallGetter = new VkWallGetter();
        List<WallItem> wallItems = vkWallGetter.getItems(wall.getWallDomain(), 10, 0);
        List<WallItem> newItems = filterWall(wallItems, wall.getLastPostId());

        if (newItems.size() != 0) {

            GroupItem groupItem = new VkGroupGetter()
                    .getItems(Collections.singletonList(wall)).iterator().next();
            SendMessage sendPublic = new SendMessage();
            sendPublic.setChatId(chatId);
            StringBuilder sendPublicBuilder = new StringBuilder();
            sendPublicBuilder.append("<b>").append(groupItem.getName()).append("</b>");
            sendPublic.enableHtml(true);
            sendPublic.setText(sendPublicBuilder.toString());

            try {
                bot.sendMessage(sendPublic);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }

            newItems.stream()
                    .forEach(item -> {
                        if (item.getText() != null && !item.getText().isEmpty()) {
                            SendMessage sendMessage = new SendMessage();
                            sendMessage.setChatId(chatId);
                            sendMessage.setText(item.getText());
                            try {
                                bot.sendMessage(sendMessage);
                            } catch (TelegramApiException e) {
                                e.printStackTrace();
                            }
                        }

                        item.getPhotos().stream()
                                .forEach(photo -> {
                                    SendPhoto sendPhoto = new SendPhoto();
                                    sendPhoto.setChatId(chatId);
                                    sendPhoto.setPhoto(photo.getSrcBig());
                                    try {
                                        bot.sendPhoto(sendPhoto);
                                    } catch (TelegramApiException e) {
                                        e.printStackTrace();
                                    }
                                });
                    });
            wall.setLastPostId(newItems.get(newItems.size() - 1).getId());
        }
    }

    private List<WallItem> filterWall(List<WallItem> wallItems, Long lastPostId) {

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
            list = wallItems;
        }

        return list;
    }
}
