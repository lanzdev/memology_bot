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

    public void process() {

        switch (lastCommand) {

            case Commands.ADD_PRE_PICKED : processAddPrePick(); break;
            case Commands.ADMIN : processAdmin(); break;
            case Commands.DELETE_PRE_PICKED : processDeletePrePicked(); break;
            case Commands.HELP : processHelp(); break;
            case Commands.LIST : processList(); break;
            case Commands.MY_LIST : processMyList(); break;
            case Commands.PAUSE : processPause(); break;
            case Commands.PICK : processPick(); break;
            case Commands.RESUME : processResume(); break;
            case Commands.START : processStart(); break;
            case Commands.SUBSCRIBE : processSubscribe(); break;
            case Commands.UNSUBSCRIBE : processUnsubscribe(); break;
            default: processHelp(); break;
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

        String[] params = message.getText().split(", ");

        WallManager wallManager = new MySqlWallManager();
        SubscriptionManager subscriptionManager = new MySqlSubscriptionManager();

        List<Wall> pickedWalls = new LinkedList<>();

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

        currentChat.setLastCommand("");
        chatManager.update(currentChat);

        StringBuilder pickedWallsBuilder = new StringBuilder();
        if (pickedWalls.size() != 0) {
            pickedWallsBuilder.append("Added: ");
        }
        pickedWalls.stream()
                .forEach(wall -> pickedWallsBuilder.append(wall.getId()).append(" : ")
                        .append(wall.getWallDomain()).append("\n"));
        pickedWallsBuilder.deleteCharAt(pickedWallsBuilder.length() - 1);

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
    }

    private void processUnsubscribe( ) {
    }
}
