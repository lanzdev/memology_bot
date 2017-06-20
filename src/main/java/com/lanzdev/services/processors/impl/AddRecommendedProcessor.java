package com.lanzdev.services.processors.impl;

import com.lanzdev.domain.Chat;
import com.lanzdev.domain.Wall;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.entity.WallManager;
import com.lanzdev.managers.mysql.impl.MySqlChatManager;
import com.lanzdev.managers.mysql.impl.MySqlWallManager;
import com.lanzdev.services.processors.AbstractProcessor;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import com.lanzdev.util.Parser;
import com.lanzdev.util.Regex;
import com.lanzdev.vk.group.GroupItem;
import com.lanzdev.vk.group.VkGroupChecker;
import com.lanzdev.vk.group.VkGroupGetter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.AbsSender;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class AddRecommendedProcessor extends AbstractProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddRecommendedProcessor.class);

    public AddRecommendedProcessor(Message message, AbsSender absSender) {
        super(message, absSender);
    }

    @Override
    public void process( ) {

        LOGGER.debug("Processing add recommended command.");
        ChatManager chatManager = new MySqlChatManager();
        Chat currentChat = chatManager.getById(message.getChatId());
        String[] params = message.getText().split("\\s+");
        Arrays.stream(params).forEach(item -> item = item.toLowerCase());
        List<Wall> recommendedWalls = new LinkedList<>();

        createRecommended(params, recommendedWalls);
        sendRecommendedWalls(currentChat, recommendedWalls);

        List<String> recommendedDomains = recommendedWalls.stream()
                .map(Wall::getWallDomain)
                .collect(Collectors.toList());
        LOGGER.debug("Added to recommended: {}", recommendedDomains.toString());
    }

    private void createRecommended(String[] params, List<Wall> recommendedWalls) {

        WallManager wallManager = new MySqlWallManager();
        VkGroupChecker vkGroupChecker = new VkGroupChecker();
        Arrays.stream(params)
                .forEach(item -> {
                    String regex = "com\\/(.*)";
                    String domain = Regex.getDomain(regex, item, 1);

                    if (!vkGroupChecker.contains(domain)) {
                        return;
                    }
                    Wall wall = wallManager.getByDomain(domain);
                    if (wall == null) {
                        wall = new Wall();
                        wall.setWallDomain(domain);
                        wall.setApproved(true);
                        wallManager.add(wall);
                        wall = wallManager.getByDomain(wall.getWallDomain());
                    } else {
                        wall.setApproved(true);
                        wallManager.update(wall);
                    }
                    recommendedWalls.add(wall);
                });
    }

    private void sendRecommendedWalls(Chat currentChat, List<Wall> recommendedWalls) {

        VkGroupGetter groupGetter = new VkGroupGetter();
        List<GroupItem> groupItems = groupGetter.getItems(recommendedWalls);
        StringBuilder builder = new StringBuilder();

        if (groupItems.size() != 0) {
            builder.append("Added to recommended:\n");
            groupItems.stream()
                    .forEach(item -> builder
                            .append(String.format("%-5d", item.getId()))
                            .append("-  ").append(item.getName()).append("\n"));
            builder.deleteCharAt(builder.length() - 1);
        } else {
            builder.append("Didn't add to recommended anything.");
        }
        String msgBody = Parser.parseMarkdown(builder.toString());
        Sender sender = new MessageSender();
        sender.send(bot, currentChat.getId().toString(), msgBody);
    }


}
