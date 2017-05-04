package com.lanzdev.commands.entity;

import com.lanzdev.commands.Commands;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.implementation.MySqlChatManager;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import com.lanzdev.util.Util;
import com.lanzdev.vk.group.GroupItem;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import java.util.List;

public class UnsubscribeCommand extends BotCommand {

    public UnsubscribeCommand( ) {
        super("unsubscribe", "Unsubscribe form public distribution");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        StringBuilder unsubscribeHeaderBuilder = new StringBuilder();
        unsubscribeHeaderBuilder.append("Please click on group from list if you want to unsubscribe from it.").append("\n");
        unsubscribeHeaderBuilder.append("Or you may print ids of desirable groups separated by commas.");

        List<GroupItem> groupItems = Util.getSubscribedWalls(chat.getId());

        StringBuilder unsubscribeMessageBuilder = new StringBuilder();
        groupItems.forEach(group -> {
            String command = String.format("/unsubscribe_%-3d", group.getId());
            unsubscribeMessageBuilder.append(
                    String.format("[%s -  %s](%s)", command, group.getName(), command))
                    .append("\n");
        });

        Sender sender = new MessageSender();
        if (groupItems.size() != 0) {
            sender.send(absSender, chat.getId().toString(), unsubscribeHeaderBuilder.toString());
            sender.send(absSender, chat.getId().toString(), unsubscribeMessageBuilder.toString());
        } else {
            String noWallsForUnsubscribe = "You have already unsubscribed all available walls";
            sender.send(absSender, chat.getId().toString(), noWallsForUnsubscribe);
        }

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.model.entity.Chat currentChat = chatManager.getById(chat.getId());
        currentChat.setLastCommand(Commands.UNSUBSCRIBE);
        chatManager.update(currentChat);
    }
}
