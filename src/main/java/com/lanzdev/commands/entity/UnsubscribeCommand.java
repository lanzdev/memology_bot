package com.lanzdev.commands.entity;

import com.lanzdev.commands.AbstractCommand;
import com.lanzdev.commands.Commands;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import com.lanzdev.util.Util;
import com.lanzdev.vk.group.PublicItem;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;

import java.util.List;

public class UnsubscribeCommand extends AbstractCommand {

    public UnsubscribeCommand( ) {
        super(Commands.UNSUBSCRIBE, "Unsubscribe form wall distribution");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String msgHeader = "*Unsubscribe*";
        StringBuilder msgBody = new StringBuilder();
        StringBuilder msgPause = new StringBuilder();
        appendSubscribedWalls(msgBody, chat.getId());
        Util.appendPauseChecking(msgPause, chat.getId());

        Sender sender = new MessageSender();
        sender.send(absSender, chat.getId().toString(), msgHeader);
        if (msgPause.length() != 0) {
            sender.send(absSender, chat.getId().toString(), msgPause.toString());
        }
        sender.send(absSender, chat.getId().toString(), msgBody.toString());

        updateChatLastCommand(chat.getId(), Commands.UNSUBSCRIBE);
    }

    private void appendSubscribedWalls(StringBuilder builder, Long chatId) {

        List<PublicItem> publicItems = Util.getSubscribedWalls(chatId);

        publicItems.forEach(group -> {
            String command = String.format("/unsubscribe_%-3d", group.getId());
            builder.append(
                    String.format("[%s -  %s](%s)", command, group.getName(), command))
                    .append("\n");
        });

        if (publicItems.size() != 0) {
            builder.insert(0, "\n")
                    .insert(0, "Or you may print ids of desirable walls separated by commas.")
                    .insert(0, "\n");
            builder.insert(0, "Please click on wall from list if you want to unsubscribe from it.");
        } else {
            builder.append("You have already unsubscribed all available walls.");
        }

    }
}
