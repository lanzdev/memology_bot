package com.lanzdev.utils;

import com.lanzdev.MemologyBot;
import com.lanzdev.commands.Commands;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.entity.SubscriptionManager;
import com.lanzdev.managers.entity.WallManager;
import com.lanzdev.managers.mysql.implementation.MySqlChatManager;
import com.lanzdev.managers.mysql.implementation.MySqlSubscriptionManager;
import com.lanzdev.managers.mysql.implementation.MySqlWallManager;
import com.lanzdev.model.entity.Chat;
import com.lanzdev.model.entity.Subscription;
import com.lanzdev.model.entity.Wall;
import com.lanzdev.vk.group.GroupItem;
import com.lanzdev.vk.group.VkGroupChecker;
import com.lanzdev.vk.group.VkGroupGetter;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CommandProcessor {

    private Message message;
    private String lastCommand;
    private MemologyBot bot;

    public CommandProcessor(Message message, String lastCommand, MemologyBot bot) {
        this.message = message;
        this.lastCommand = lastCommand;
        this.bot = bot;
    }

    public void process( ) {

        switch (lastCommand) {

            case Commands.ADD_PRE_PICKED:
                processAddPrePick();
                break;
            case Commands.ADMIN:
                processAdmin();
                break;
            case Commands.DELETE_PRE_PICKED:
                processDeletePrePicked();
                break;
            case Commands.HELP:
                processHelp();
                break;
            case Commands.LIST:
                processList();
                break;
            case Commands.MY_LIST:
                processMyList();
                break;
            case Commands.PAUSE:
                processPause();
                break;
            case Commands.PICK:
                processPick();
                break;
            case Commands.RESUME:
                processResume();
                break;
            case Commands.START:
                processStart();
                break;
            case Commands.SUBSCRIBE:
                processSubscribe();
                break;
            case Commands.UNSUBSCRIBE:
                processUnsubscribe();
                break;
            default:
                processHelp();
                break;
        }
    }

    private void processAddPrePick( ) {
    }

    private void processAdmin( ) {
    }

    private void processDeletePrePicked( ) {
    }

    private void processHelp( ) {
    }

    private void processList( ) {
    }

    private void processMyList( ) {
    }

    private void processPause( ) {
    }

    private void processPick( ) {

        ChatManager chatManager = new MySqlChatManager();
        Chat currentChat = chatManager.getById(message.getChatId());
        String[] params = message.getText().split(",");
        List<Wall> pickedWalls = new LinkedList<>();

        createSubscriptionsForPick(currentChat, params, pickedWalls);
        sendPickedWalls(currentChat, pickedWalls);

        currentChat.setLastCommand("");
        chatManager.update(currentChat);
    }

    private void createSubscriptionsForPick(Chat currentChat, String[] params, List<Wall> pickedWalls) {

        WallManager wallManager = new MySqlWallManager();
        SubscriptionManager subscriptionManager = new MySqlSubscriptionManager();
        Arrays.stream(params)
                .forEach((item) -> {
                    try {
                        Integer wallId = Integer.parseInt(item);
                        Wall wall = wallManager.getById(wallId);
                        Subscription subscription = new Subscription();
                        subscription.setChatId(currentChat.getId());
                        subscription.setWallDomain(wall.getWallDomain());
                        subscriptionManager.add(subscription);
                        pickedWalls.add(wall);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }

    private void sendPickedWalls(Chat currentChat, List<Wall> pickedWalls) {
        StringBuilder pickedWallsBuilder = new StringBuilder();
        if (pickedWalls.size() != 0) {
            pickedWallsBuilder.append("Added: ");
            pickedWalls.stream()
                    .forEach(wall -> pickedWallsBuilder.append(wall.getId()).append(" : ")
                            .append(wall.getWallDomain()).append("\n"));
            pickedWallsBuilder.deleteCharAt(pickedWallsBuilder.length() - 1);

        } else {
            pickedWallsBuilder.append("Didn't add something");
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(currentChat.getId());
        sendMessage.setText(pickedWallsBuilder.toString());

        try {
            bot.sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void processResume( ) {
    }

    private void processStart( ) {
    }

    private void processSubscribe( ) {

        ChatManager chatManager = new MySqlChatManager();
        Chat currentChat = chatManager.getById(message.getChatId());
        String[] params = message.getText().split(", ");
        Arrays.stream(params).forEach(item -> item = item.toLowerCase());
        List<Wall> subscribedWalls = new LinkedList<>();

        createSubscriptionsForSubscribe(currentChat, params, subscribedWalls);
        sendSubscribedWalls(currentChat, subscribedWalls);

        currentChat.setLastCommand("");
        chatManager.update(currentChat);
    }

    private void createSubscriptionsForSubscribe(Chat currentChat, String[] params, List<Wall> subscribedWalls) {

        WallManager wallManager = new MySqlWallManager();
        SubscriptionManager subscriptionManager = new MySqlSubscriptionManager();
        VkGroupChecker vkGroupChecker = new VkGroupChecker();
        Arrays.stream(params)
                .forEach(item -> {
                    if (vkGroupChecker.contains(item)) {

                        Wall wall = wallManager.getByDomain(item);
                        System.out.println(wall);
                        if (wall == null) {
                            wall = new Wall();
                            wall.setWallDomain(item);
                            wall.setApproved(false);
                            wallManager.add(wall);
                        }

                        Subscription subscription = new Subscription();
                        subscription.setChatId(currentChat.getId());
                        subscription.setWallDomain(item);
                        subscriptionManager.add(subscription);

                        wall = wallManager.getByDomain(item);
                        subscribedWalls.add(wall);
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
                            .append(item.getId())
                            .append(": ").append(item.getScreenName())
                            .append(" - ").append(item.getName()).append("\n"));
            subscribedWallsBuilder.deleteCharAt(subscribedWallsBuilder.length() - 1);
        } else {
            subscribedWallsBuilder.append("Didn't subscribe something");
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(currentChat.getId());
        sendMessage.setText(subscribedWallsBuilder.toString());
        try {
            bot.sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void processUnsubscribe( ) {

        ChatManager chatManager = new MySqlChatManager();
        Chat currentChat = chatManager.getById(message.getChatId());
        String[] params = message.getText().split(",");
        List<Wall> unsubscribedWalls = new LinkedList<>();

        deleteSubscriptionsForUnsubscribe(currentChat, params, unsubscribedWalls);
        sendUnsubscribedWalls(currentChat, unsubscribedWalls);

        currentChat.setLastCommand("");
        chatManager.update(currentChat);
    }

    private void deleteSubscriptionsForUnsubscribe(Chat currentChat, String[] params, List<Wall> unsubscribedWalls) {
        WallManager wallManager = new MySqlWallManager();
        SubscriptionManager subscriptionManager = new MySqlSubscriptionManager();
        List<Subscription> subscriptions = subscriptionManager.getByChat(currentChat.getId());
        Arrays.stream(params)
                .forEach(item -> {
                    item = item.trim();
                    try {
                        Integer wallId = Integer.parseInt(item);
                        Wall wall = wallManager.getById(wallId);
                        Subscription subscription = getByWallDomain(subscriptions, wall.getWallDomain());
                        subscriptionManager.delete(subscription);
                        unsubscribedWalls.add(wall);
                    } catch (Exception e) {
                        e.printStackTrace();
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
                            .append(item.getId())
                            .append(": ").append(item.getScreenName())
                            .append(" - ").append(item.getName()).append("\n"));
            unsubscribedWallsBuilder.deleteCharAt(unsubscribedWallsBuilder.length() - 1);
        } else {
            unsubscribedWallsBuilder.append("Didn't unsubscribe something");
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(currentChat.getId());
        sendMessage.setText(unsubscribedWallsBuilder.toString());
        try {
            bot.sendMessage(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
