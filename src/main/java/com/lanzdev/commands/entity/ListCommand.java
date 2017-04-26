package com.lanzdev.commands.entity;

import com.lanzdev.managers.entity.WallManager;
import com.lanzdev.managers.mysql.implementation.MySqlWallManager;
import com.lanzdev.model.entity.Wall;
import com.lanzdev.utils.Counter;
import com.lanzdev.vk.group.GroupItem;
import com.lanzdev.vk.group.VkGroupGetter;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;

public class ListCommand extends BotCommand{

    public ListCommand() {
        super("list", "See list of pre picked publics");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        WallManager wallManager = new MySqlWallManager();
        List<Wall> walls = wallManager.getAllApproved();

        VkGroupGetter groupGetter = new VkGroupGetter();
        List<GroupItem> groupItems = groupGetter.getItems(walls);

        Counter counter = new Counter();

        StringBuilder listMessageBuilder = new StringBuilder();
        groupItems.stream()
                .forEach((group) -> listMessageBuilder
                        .append(group.getId())
                        .append(": ").append(group.getScreenName())
                        .append(" - ").append(group.getName()).append("\n"));

        SendMessage listMessage = new SendMessage();
        listMessage.setChatId(chat.getId().toString());
        listMessage.setText(listMessageBuilder.toString());

        try {
            absSender.sendMessage(listMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
