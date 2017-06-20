package com.lanzdev.commands.entity;

import com.lanzdev.commands.AbstractCommand;
import com.lanzdev.commands.Commands;
import com.lanzdev.domain.Wall;
import com.lanzdev.managers.entity.WallManager;
import com.lanzdev.managers.mysql.impl.MySqlWallManager;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import com.lanzdev.vk.group.GroupItem;
import com.lanzdev.vk.group.VkGroupGetter;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;

import java.util.List;

public class DeleteRecommendedCommand extends AbstractCommand {

    public DeleteRecommendedCommand() {
        super(Commands.DELETE_RECOMMENDED, "Delete pre picked publics from the list of pre picked publics");
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String msgHeader = "*Delete recommended*";
        StringBuilder msgBody = new StringBuilder();
        appendDeleteRecommended(msgBody);

        Sender sender = new MessageSender();
        sender.send(absSender, chat.getId().toString(), msgHeader);
        sender.send(absSender, chat.getId().toString(), msgBody.toString());

        updateChatLastCommand(chat.getId(), Commands.DELETE_RECOMMENDED);
    }

    private void appendDeleteRecommended(StringBuilder builder) {

        WallManager wallManager = new MySqlWallManager();
        List<Wall> walls = wallManager.getAllApproved();
        VkGroupGetter groupGetter = new VkGroupGetter();

        List<GroupItem> groupItems = groupGetter.getItems(walls);

        groupItems.forEach(group -> {
            String command = String.format("/delete_$-3d", group.getId());
            builder.append(
                    String.format("[%s -  %s](%s)", command, group.getName(), command))
                    .append("\n");
        });

        if (groupItems.size() != 0) {
            builder.insert(0, "\n")
                    .insert(0, "Or you may print ids of publics you want to delete from recommended list separated by commas.")
                    .insert(0, "\n");
            builder.insert(0, "Please click on publics from list if you want to delete them from recommended list.");
        } else {
            builder.append("You have already deleted all available publics.");
        }

    }
}
