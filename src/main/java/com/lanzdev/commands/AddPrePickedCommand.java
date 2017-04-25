package com.lanzdev.commands;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class AddPrePickedCommand extends BotCommand {

    public AddPrePickedCommand() {
        super("add_pre_picked", "Add pre picked publics to the list of pre picked publics");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

    }
}
