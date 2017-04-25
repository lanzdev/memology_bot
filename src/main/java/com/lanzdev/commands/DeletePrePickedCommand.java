package com.lanzdev.commands;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class DeletePrePickedCommand extends BotCommand {

    public DeletePrePickedCommand() {
        super("delete_pre_picked", "Delete pre picked publics from the list of pre picked publics");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

    }
}
