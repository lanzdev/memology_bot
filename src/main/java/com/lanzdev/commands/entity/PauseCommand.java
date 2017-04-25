package com.lanzdev.commands.entity;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class PauseCommand extends BotCommand {

    public PauseCommand() {
        super("pause", "Suspend distribution from publics");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

    }
}
