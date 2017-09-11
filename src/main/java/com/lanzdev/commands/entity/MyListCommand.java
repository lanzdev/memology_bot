package com.lanzdev.commands.entity;

import com.lanzdev.commands.AbstractCommand;
import com.lanzdev.commands.Commands;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import com.lanzdev.util.Parser;
import com.lanzdev.util.Util;
import com.lanzdev.vk.group.PublicItem;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;

import java.util.List;

public class MyListCommand extends AbstractCommand {

    public MyListCommand( ) {
        super(Commands.MY_LIST, "See list of walls already subscribed by you");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String msgHeader = "*My List*";
        StringBuilder msgBody = new StringBuilder();
        msgBody.append("List of subscribed walls:\n");
        appendSubscribedWalls(msgBody, chat.getId());

        String parsedMsgBody = Parser.parseMarkdown(msgBody.toString());
        Sender sender = new MessageSender();
        sender.send(absSender, chat.getId().toString(), msgHeader);
        sender.send(absSender, chat.getId().toString(), parsedMsgBody);

        updateChatLastCommand(chat.getId(), Commands.MY_LIST);
    }

    private void appendSubscribedWalls(StringBuilder builder, Long chatId) {

        List<PublicItem> publicItems = Util.getSubscribedWalls(chatId);
        if (publicItems.size() != 0) {
            publicItems.forEach(group -> builder
                    .append(String.format("%-5d", group.getId()))
                    .append("-  ").append(group.getName()).append("\n"));
        } else {
            builder.append("You haven't subscribed any wall. Your list is empty.");
        }
    }
}
