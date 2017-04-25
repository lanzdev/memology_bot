package com.lanzdev.commands.entity;

import com.lanzdev.managers.entity.SubscriptionManager;
import com.lanzdev.managers.mysql.implementation.MySqlSubscriptionManager;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class PickCommand extends BotCommand {

    public PickCommand() {
        super("pick", "Pick public from pre picked list");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        StringBuilder pickMessageBuilder = new StringBuilder();

        SubscriptionManager subscriptionManager = new MySqlSubscriptionManager();

    }
}
