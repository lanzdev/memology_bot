package com.lanzdev.services.processors.impl;

import com.lanzdev.SpreadBot;
import com.lanzdev.commands.Commands;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.entity.SubscriptionManager;
import com.lanzdev.managers.entity.WallManager;
import com.lanzdev.managers.mysql.impl.MySqlChatManager;
import com.lanzdev.managers.mysql.impl.MySqlSubscriptionManager;
import com.lanzdev.managers.mysql.impl.MySqlWallManager;
import com.lanzdev.domain.Chat;
import com.lanzdev.domain.Subscription;
import com.lanzdev.domain.Wall;
import com.lanzdev.services.processors.AbstractProcessor;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import com.lanzdev.util.Parser;
import com.lanzdev.vk.group.GroupItem;
import com.lanzdev.vk.group.VkGroupGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.AbsSender;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class UnsubscribeProcessor extends AbstractProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnsubscribeProcessor.class);

    public UnsubscribeProcessor(Message message, AbsSender absSender) {
        super(message, absSender);
    }

    @Override
    public void process( ) {

        String[] params = null;
        if (message.getText().startsWith("/unsubscribe")) {
            String id = message.getText().split("_")[1];
            params = new String[]{id};
            LOGGER.debug("Processing unsubscribe sub command for id: {}", id);
        } else {
            params = message.getText().split("[\\s,.:;]+");
            LOGGER.debug("Processing unsubscribe command, params: {}", Arrays.toString(params));
        }

        ChatManager chatManager = new MySqlChatManager();
        Chat currentChat = chatManager.getById(message.getChatId());
        List<Wall> unsubscribedWalls = new LinkedList<>();

        deactivateSubscriptions(currentChat, params, unsubscribedWalls);
        sendUnsubscribedWalls(currentChat, unsubscribedWalls);

        List<String> unsubscribedDomains = unsubscribedWalls.stream()
                .map(Wall::getWallDomain)
                .collect(Collectors.toList());
        LOGGER.debug("{} {} #{} unsubscribed: {}", currentChat.getFirstName(), currentChat.getLastName(),
                currentChat.getId(), unsubscribedDomains.toString());

        SpreadBot.COMMANDS.get(Commands.UNSUBSCRIBE).getCommand()
                .execute(bot, message.getFrom(), message.getChat(), null);
    }

    private void deactivateSubscriptions(Chat currentChat, String[] params, List<Wall> unsubscribedWalls) {

        WallManager wallManager = new MySqlWallManager();
        SubscriptionManager subscriptionManager = new MySqlSubscriptionManager();
        List<Subscription> subscriptions = subscriptionManager.getByChatId(currentChat.getId());
        Arrays.stream(params)
                .forEach(item -> {
                    Integer wallId = null;
                    Wall wall = null;
                    try {
                        wallId = Integer.parseInt(item.trim());
                        wall = wallManager.getById(wallId);
                        Subscription subscription = getByWallDomain(subscriptions, wall.getWallDomain());
                        if (subscription != null) {
                            subscription.setActive(false);
                            subscriptionManager.update(subscription);
                            unsubscribedWalls.add(wall);
                        }
                    } catch (NumberFormatException e) {
                        LOGGER.error("Cannot parseMarkdown {} into Integer", item);
                    } catch (NullPointerException e) {
                        LOGGER.error("Wall with id {} doesn't contain.", wallId);
                    } catch (Exception e) {
                        LOGGER.error("Cannot delete subscription. wall_id = {}", wallId, e);
                    }
                });
    }

    private Subscription getByWallDomain(List<Subscription> subscriptions, String wallDomain) {

        for (Subscription subscription : subscriptions) {
            if (subscription.getWallDomain().equals(wallDomain)) {
                return subscription;
            }
        }
        return null;
    }

    private void sendUnsubscribedWalls(Chat currentChat, List<Wall> unsubscribedWalls) {

        VkGroupGetter groupGetter = new VkGroupGetter();
        List<GroupItem> groupItems = groupGetter.getItems(unsubscribedWalls);
        StringBuilder unsubscribedWallsBuilder = new StringBuilder();

        if (groupItems.size() != 0) {
            unsubscribedWallsBuilder.append("Unsubscribed:\n");
            groupItems.stream()
                    .forEach(item -> unsubscribedWallsBuilder
                            .append(String.format("%-5d", item.getId()))
                            .append("-  ").append(item.getName()).append("\n"));
            unsubscribedWallsBuilder.deleteCharAt(unsubscribedWallsBuilder.length() - 1);
        } else {
            unsubscribedWallsBuilder.append("Didn't unsubscribe from anything");
        }
        String message = Parser.parseMarkdown(unsubscribedWallsBuilder.toString());
        Sender sender = new MessageSender();
        sender.send(bot, currentChat.getId().toString(), message);
    }
}
