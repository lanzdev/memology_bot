package com.lanzdev.commands.entity;

import com.lanzdev.commands.AbstractCommand;
import com.lanzdev.commands.Commands;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;

public class DeleteRecommendedCommand extends AbstractCommand {

    public DeleteRecommendedCommand() {
        super(Commands.DELETE_RECOMMENDED, "Delete pre picked publics from the list of pre picked publics");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {


        updateChatLastCommand(chat.getId(), Commands.DELETE_RECOMMENDED);
    }
}
