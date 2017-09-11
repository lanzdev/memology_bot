package com.lanzdev;

import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.entity.SubscriptionManager;
import com.lanzdev.managers.entity.WallManager;
import com.lanzdev.managers.mysql.impl.MySqlChatManager;
import com.lanzdev.managers.mysql.impl.MySqlSubscriptionManager;
import com.lanzdev.managers.mysql.impl.MySqlWallManager;
import com.lanzdev.domain.Chat;
import com.lanzdev.domain.Subscription;
import com.lanzdev.domain.Wall;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.PhotoSender;
import com.lanzdev.services.senders.Sender;
import com.lanzdev.util.Parser;
import com.lanzdev.vk.group.GroupItem;
import com.lanzdev.vk.group.VkGroupGetter;
import com.lanzdev.vk.wall.Photo;
import com.lanzdev.vk.wall.VkWallGetter;
import com.lanzdev.vk.wall.WallItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

public class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {

        LOGGER.debug("Enter main().");
        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        SpreadBot bot = new SpreadBot();
        try {
            telegramBotsApi.registerBot(bot);
            LOGGER.info("Bot registered successfully!");
        } catch (TelegramApiRequestException e) {
            LOGGER.error("Cannot register bot.", e);
        }

        try {
            Main main = new Main();
            main.distribution(bot);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * method which distributes new posts to every user according to their subscriptions
     *
     * @param absSender
     */
    private void distribution(AbsSender absSender) {

        LOGGER.debug("Enter distribution().");
        SubscriptionManager subscriptionManager = new MySqlSubscriptionManager();
        WallManager wallManager = new MySqlWallManager();

        LOGGER.info("Start to distribute posts.");

        while (true) {
            try {
                List<Subscription> subscriptions = subscriptionManager.getAll();
                subscriptions.stream()
                        .forEach(item -> {
                            Wall wall = wallManager.getByDomain(item.getWallDomain());
                            if (subscriptionManager.getById(item.getId()) != null) {
                                sendMessages(wall, item, absSender);
                                subscriptionManager.update(item);
                            }
                        });
                sleep();
            } catch (Exception e) {
                LOGGER.error("Some issue occurred during distributing", e);
            }
        }
    }

    private void sleep() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            LOGGER.error("Thread was interrupted.", e);
        }
    }

    private void sendMessages(Wall wall, Subscription subscription, AbsSender absSender) {

        VkWallGetter vkWallGetter = new VkWallGetter();
        List<WallItem> wallItems = vkWallGetter.getItems(wall.getWallDomain(), 10, 0);
        List<WallItem> newItems = pickNewest(wallItems, subscription.getLastPostId());
        ChatManager chatManager = new MySqlChatManager();
        Chat currentChat = chatManager.getById(subscription.getChatId());

        if (newItems.size() == 0) {
            return;
        } else {
            subscription.setLastPostId(newItems.get(newItems.size() - 1).getId());
        }

        LOGGER.info("{} new posts found on the wall \"{}\".", newItems.size(), wall.getWallDomain());

        if (currentChat.isSuspended()) {
            LOGGER.info("Distribution for chat {} is suspended.", currentChat.getId());
            return;
        }

        if (!subscription.isActive()) {
            LOGGER.info("Subscription for public {} is not active for chat {}", wall.getWallDomain(), currentChat.getId());
            return;
        }

        sendMessageHeader(wall, subscription, absSender);
        newItems.stream()
                .forEach(item -> {
                    sendPostsText(item, subscription, absSender);
                    item.getPhotos().stream()
                            .forEach(photo -> {
                                sendPostsPhoto(photo, subscription, absSender);
                            });
                });

    }

    /**
     * Sends message header before sending actual messages. Contains wall name in bald style
     *
     * @param wall
     * @param subscription
     * @param absSender
     */
    private void sendMessageHeader(Wall wall, Subscription subscription, AbsSender absSender) {
        List<GroupItem> groupItems = new VkGroupGetter().getItems(Collections.singletonList(wall));
        if (groupItems.isEmpty()) {
            LOGGER.debug("sendMessageHeader -> groupItems list is empty for wall: {}", wall);
            return;
        }
        GroupItem groupItem = groupItems.iterator().next();
        StringBuilder builder = new StringBuilder();
        String groupName = groupItem.getName();
        groupName = Parser.parseHashtag(groupName);
        groupName = Parser.parseMarkdown(groupName);
        builder.append("#").append(groupName);

        LOGGER.info("Send message header \"{}\" to chat {}",
                builder.toString(), subscription.getChatId().toString());

        Sender sender = new MessageSender();
        sender.send(absSender, subscription.getChatId().toString(), builder.toString());
    }

    /**
     * Sends text message to chat
     *
     * @param item
     * @param subscription
     * @param absSender
     */
    private void sendPostsText(WallItem item, Subscription subscription, AbsSender absSender) {

        Sender sender = new MessageSender();
        if (item.getText() != null && !item.getText().isEmpty()) {
            String message = Parser.parseMarkdown(item.getText());
            sender.send(absSender, subscription.getChatId().toString(), message);
        }
    }

    /**
     * Sends photo message to chat
     *
     * @param photo
     * @param subscription
     * @param absSender
     */
    private void sendPostsPhoto(Photo photo, Subscription subscription, AbsSender absSender) {

        Sender sender = new PhotoSender();
        if (photo.getSrcBig() != null && !photo.getSrcBig().isEmpty()) {
            sender.send(absSender, subscription.getChatId().toString(), photo.getSrcBig());
        }
    }

    /**
     * Picks newest wall items from list {@param wallItems}
     *
     * @param wallItems
     * @param lastPostId
     * @return TODO: create better algorithm for picking new posts
     */
    private List<WallItem> pickNewest(List<WallItem> wallItems, Long lastPostId) {

        List<WallItem> list = new ArrayList<>();

        int last = IntStream.range(0, wallItems.size())
                .filter(i -> wallItems.get(i).getId().equals(lastPostId))
                .findFirst().orElse(0);

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
