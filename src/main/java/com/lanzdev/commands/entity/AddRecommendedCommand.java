package com.lanzdev.commands.entity;

import com.lanzdev.commands.AbstractCommand;
import com.lanzdev.commands.Commands;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;

public class AddRecommendedCommand extends AbstractCommand {

    public AddRecommendedCommand() {
        super(Commands.ADD_RECOMMENDED, "Add pre picked publics to the list of pre picked publics");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String msgHeader = "*Add recommended*";
        String msgBody = "Please enter walls url:";

        Sender sender = new MessageSender();
        sender.send(absSender, chat.getId().toString(), msgHeader);
        sender.send(absSender, chat.getId().toString(), msgBody);

        updateChatLastCommand(chat.getId(), Commands.ADD_RECOMMENDED);
    }

}
