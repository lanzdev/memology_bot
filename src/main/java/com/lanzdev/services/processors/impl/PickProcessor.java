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
import com.lanzdev.vk.group.PublicItem;
import com.lanzdev.vk.group.VkPublicGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.AbsSender;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class PickProcessor extends AbstractProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PickProcessor.class);

    public PickProcessor(Message message, AbsSender absSender) {
        super(message, absSender);
    }

    @Override
    public void process( ) {

        String[] params = null;
        if (message.getText().startsWith("/pick")) {
            String id = message.getText().split("_")[1];
            params = new String[]{id};
            LOGGER.debug("Processing pick sub command for id: {}", id);
        } else {
            params = message.getText().split(",");
            LOGGER.debug("Processing pick command, params: {}", Arrays.toString(params));
        }

        ChatManager chatManager = new MySqlChatManager();
        Chat currentChat = chatManager.getById(message.getChatId());
        List<Wall> pickedWalls = new LinkedList<>();

        createSubscriptions(currentChat, params, pickedWalls);
        sendPickedWalls(currentChat, pickedWalls);

        List<String> pickedDomains = pickedWalls.stream()
                .map(Wall::getWallDomain)
                .collect(Collectors.toList());
        LOGGER.debug("{} {} #{} picked: {}", currentChat.getFirstName(), currentChat.getLastName(),
                currentChat.getId(), pickedDomains.toString());

        SpreadBot.COMMANDS.get(Commands.PICK).getCommand()
                .execute(bot, message.getFrom(), message.getChat(), null);

    }

    private void createSubscriptions(Chat currentChat, String[] params, List<Wall> pickedWalls) {

        WallManager wallManager = new MySqlWallManager();
        SubscriptionManager subscriptionManager = new MySqlSubscriptionManager();
        Arrays.stream(params)
                .forEach((item) -> {
                    Integer wallId = null;
                    Wall wall;
                    Subscription subscription = null;
                    try {
                        wallId = Integer.parseInt(item.trim());
                        wall = wallManager.getById(wallId);
                        subscription = subscriptionManager.getByChatAndWall(
                                currentChat.getId(), wall.getWallDomain());
                        if (subscription != null) {
                            subscription.setActive(true);
                            subscriptionManager.update(subscription);
                            pickedWalls.add(wall);
                        } else {
                            subscription = new Subscription();
                            subscription.setChatId(currentChat.getId());
                            subscription.setWallDomain(wall.getWallDomain());
                            subscriptionManager.add(subscription);
                            pickedWalls.add(wall);
                        }
                    } catch (Exception e) {
                        LOGGER.error("Cannot create subscription for pick. wall_id = {}.", wallId, e);
                    }
                });
    }

    private void sendPickedWalls(Chat currentChat, List<Wall> pickedWalls) {

        VkPublicGetter groupGetter = new VkPublicGetter();
        List<PublicItem> publicItems = groupGetter.getItems(pickedWalls);
        StringBuilder pickedWallsBuilder = new StringBuilder();

        if (publicItems.size() != 0) {
            pickedWallsBuilder.append("Added:\n");
            publicItems.stream()
                    .forEach(item -> pickedWallsBuilder
                            .append(String.format("%-5d", item.getId()))
                            .append("-  ").append(item.getName()).append("\n"));
            pickedWallsBuilder.deleteCharAt(pickedWallsBuilder.length() - 1);
        } else {
            pickedWallsBuilder.append("Didn't add anything");
        }
        String message = Parser.parseMarkdown(pickedWallsBuilder.toString());
        Sender sender = new MessageSender();
        sender.send(bot, currentChat.getId().toString(), message);
    }


}
