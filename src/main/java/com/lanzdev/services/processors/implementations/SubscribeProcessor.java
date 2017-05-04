package com.lanzdev.services.processors.implementations;

import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.entity.SubscriptionManager;
import com.lanzdev.managers.entity.WallManager;
import com.lanzdev.managers.mysql.implementation.MySqlChatManager;
import com.lanzdev.managers.mysql.implementation.MySqlSubscriptionManager;
import com.lanzdev.managers.mysql.implementation.MySqlWallManager;
import com.lanzdev.model.entity.Chat;
import com.lanzdev.model.entity.Subscription;
import com.lanzdev.model.entity.Wall;
import com.lanzdev.services.processors.AbstractProcessor;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import com.lanzdev.util.MarkdownParser;
import com.lanzdev.util.Regex;
import com.lanzdev.vk.group.GroupItem;
import com.lanzdev.vk.group.VkGroupChecker;
import com.lanzdev.vk.group.VkGroupGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.AbsSender;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SubscribeProcessor extends AbstractProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(SubscribeProcessor.class);

    public SubscribeProcessor(Message message, AbsSender absSender) {
        super(message, absSender);
    }

    @Override
    public void process( ) {

        LOGGER.debug("Processing subscribe command.");
        ChatManager chatManager = new MySqlChatManager();
        Chat currentChat = chatManager.getById(message.getChatId());
        String[] params = message.getText().split("\\s+");
        Arrays.stream(params).forEach(item -> item = item.toLowerCase());
        List<Wall> subscribedWalls = new LinkedList<>();

        createSubscriptions(currentChat, params, subscribedWalls);
        sendSubscribedWalls(currentChat, subscribedWalls);

        List<String> subscribedDomains = subscribedWalls.stream()
                .map(wall -> wall.getWallDomain())
                .collect(Collectors.toList());
        LOGGER.debug("Subscribed: {}", subscribedDomains.toString());
    }

    private void createSubscriptions(Chat currentChat, String[] params, List<Wall> subscribedWalls) {

        WallManager wallManager = new MySqlWallManager();
        SubscriptionManager subscriptionManager = new MySqlSubscriptionManager();
        VkGroupChecker vkGroupChecker = new VkGroupChecker();
        Arrays.stream(params)
                .forEach(item -> {
                    String regex = "com\\/(.*)";
                    String domain = Regex.getDomain(regex, item, 1);

                    if (vkGroupChecker.contains(domain)) {

                        Wall wall = wallManager.getByDomain(domain);
                        if (wall == null) {
                            wall = new Wall();
                            wall.setWallDomain(domain);
                            wall.setApproved(false);
                            wallManager.add(wall);
                        }

                        Subscription subscription = subscriptionManager.getByChatAndWall(
                                currentChat.getId(), wall.getWallDomain());
                        if (subscription == null) {
                            subscription = new Subscription();
                            subscription.setChatId(currentChat.getId());
                            subscription.setWallDomain(domain);
                            subscriptionManager.add(subscription);
                            wall = wallManager.getByDomain(domain);
                            subscribedWalls.add(wall);
                        } else {
                            if (subscription.isActive()) {
                                VkGroupGetter groupGetter = new VkGroupGetter();
                                GroupItem groupItem = groupGetter.getItems(Collections.singletonList(wall))
                                        .iterator().next();
                                String alreadySubscribedString =
                                        String.format("You have already subscribed public: %s", groupItem.getName());
                                Sender sender = new MessageSender();
                                sender.send(bot, currentChat.getId().toString(), alreadySubscribedString);
                            } else {
                                subscription.setActive(true);
                                subscriptionManager.update(subscription);
                                wall = wallManager.getByDomain(domain);
                                subscribedWalls.add(wall);
                            }
                        }
                    }
                });
    }

    private void sendSubscribedWalls(Chat currentChat, List<Wall> subscribedWalls) {
        VkGroupGetter groupGetter = new VkGroupGetter();
        List<GroupItem> groupItems = groupGetter.getItems(subscribedWalls);
        StringBuilder subscribedWallsBuilder = new StringBuilder();

        if (groupItems.size() != 0) {
            subscribedWallsBuilder.append("Subscribed:\n");
            groupItems.stream()
                    .forEach(item -> subscribedWallsBuilder
                            .append(String.format("%-5d",item.getId()))
                            .append("-  ").append(item.getName()).append("\n"));
            subscribedWallsBuilder.deleteCharAt(subscribedWallsBuilder.length() - 1);
        } else {
            subscribedWallsBuilder.append("Didn't subscribe on anything");
        }
        String message = MarkdownParser.parse(subscribedWallsBuilder.toString());
        Sender sender = new MessageSender();
        sender.send(bot, currentChat.getId().toString(), message);
    }
}
