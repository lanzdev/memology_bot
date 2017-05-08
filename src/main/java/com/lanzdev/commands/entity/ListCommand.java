package com.lanzdev.commands.entity;

import com.lanzdev.commands.Commands;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.entity.WallManager;
import com.lanzdev.managers.mysql.implementation.MySqlChatManager;
import com.lanzdev.managers.mysql.implementation.MySqlWallManager;
import com.lanzdev.model.entity.Wall;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import com.lanzdev.util.MarkdownParser;
import com.lanzdev.vk.group.GroupItem;
import com.lanzdev.vk.group.VkGroupGetter;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import java.util.List;

public class ListCommand extends BotCommand {

    public ListCommand() {
        super("list", "See list of pre picked walls");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String msgHeader = "*List*";
        StringBuilder msgBody = new StringBuilder();
        msgBody.append("List of recommended walls:\n");
        appendRecommendedWalls(msgBody);

        String parsedMsgBody = MarkdownParser.parse(msgBody.toString());
        Sender sender = new MessageSender();
        sender.send(absSender, chat.getId().toString(), msgHeader);
        sender.send(absSender, chat.getId().toString(), parsedMsgBody);

        updateChatLastCommand(chat.getId());
    }

    private void appendRecommendedWalls(StringBuilder builder) {

        WallManager wallManager = new MySqlWallManager();
        List<Wall> walls = wallManager.getAllApproved();

        VkGroupGetter groupGetter = new VkGroupGetter();
        List<GroupItem> groupItems = groupGetter.getItems(walls);

        groupItems.stream()
                .forEach(group -> builder
                        .append(String.format("%-5d", group.getId()))
                        .append("-  ").append(group.getName()).append("\n"));
    }

    private void updateChatLastCommand(Long chatId) {

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.model.entity.Chat currentChat = chatManager.getById(chatId);
        currentChat.setLastCommand(Commands.LIST);
        chatManager.update(currentChat);
    }
}