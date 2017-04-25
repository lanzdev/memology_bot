package com.lanzdev.commands;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class UnsubscribeCommand extends BotCommand{

    public UnsubscribeCommand() {
        super("unsubscribe", "Unsubscribe form public distribution");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

    }
}
