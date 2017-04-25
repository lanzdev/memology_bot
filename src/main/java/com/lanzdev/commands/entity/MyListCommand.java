package com.lanzdev.commands.entity;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class MyListCommand extends BotCommand {

    public MyListCommand() {
        super("my_list", "See list of public already subscribed by you");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

    }
}
