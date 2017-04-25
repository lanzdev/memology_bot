package com.lanzdev.commands.entity;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class ResumeCommand extends BotCommand {

    public ResumeCommand() {
        super("resume", "Proceed distribution from publics");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

    }
}
