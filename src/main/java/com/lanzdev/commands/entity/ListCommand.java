package com.lanzdev.commands.entity;

import com.lanzdev.managers.entity.WallManager;
import com.lanzdev.managers.mysql.implementation.MySqlWallManager;
import com.lanzdev.model.entity.Wall;
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

        StringBuilder listMessageBuilder = new StringBuilder();

        WallManager wallManager = new MySqlWallManager();
        List<Wall> walls = wallManager.getAllApproved();

        walls.stream()
                .forEach((wall) -> listMessageBuilder.append(wall.getId()).append(": ")
                        .append(wall.getWallDomain()).append("\n"));

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
