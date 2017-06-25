package com.lanzdev.commands.entity;

import com.lanzdev.commands.AbstractCommand;
import com.lanzdev.commands.Commands;
import com.lanzdev.domain.Wall;
import com.lanzdev.managers.entity.WallManager;
import com.lanzdev.managers.mysql.impl.MySqlWallManager;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import com.lanzdev.util.Parser;
import com.lanzdev.vk.group.PublicItem;
import com.lanzdev.vk.group.VkPublicGetter;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;

import java.util.List;

public class ListCommand extends AbstractCommand {

    public ListCommand() {
        super(Commands.LIST, "See list of pre picked walls");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String msgHeader = "*List*";
        StringBuilder msgBody = new StringBuilder();
        msgBody.append("List of recommended walls:\n");
        appendRecommendedWalls(msgBody);

        String parsedMsgBody = Parser.parseMarkdown(msgBody.toString());
        Sender sender = new MessageSender();
        sender.send(absSender, chat.getId().toString(), msgHeader);
        sender.send(absSender, chat.getId().toString(), parsedMsgBody);

        updateChatLastCommand(chat.getId(), Commands.LIST);
    }

    private void appendRecommendedWalls(StringBuilder builder) {

        WallManager wallManager = new MySqlWallManager();
        List<Wall> walls = wallManager.getAllApproved();

        VkPublicGetter groupGetter = new VkPublicGetter();
        List<PublicItem> publicItems = groupGetter.getItems(walls);

        publicItems.stream()
                .forEach(group -> builder
                        .append(String.format("%-5d", group.getId()))
                        .append("-  ").append(group.getName()).append("\n"));
    }
}