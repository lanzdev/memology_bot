package com.lanzdev.commands.entity;

import com.lanzdev.SpreadBot;
import com.lanzdev.commands.AbstractCommand;
import com.lanzdev.commands.Commands;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.impl.MySqlChatManager;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import com.lanzdev.util.Parser;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.ICommandRegistry;

public class HelpCommand extends AbstractCommand {

    private final ICommandRegistry commandRegistry;

    public HelpCommand(ICommandRegistry commandRegistry) {
        super(Commands.HELP, "See allowed commands");
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String msgHeader = "*Help*";
        StringBuilder msgBody = new StringBuilder();
        msgBody.append("These are commands for this Bot:\n");
        appendCommands(msgBody, chat.getId());

        String parsedMsgBody = Parser.parseMarkdown(msgBody.toString());
        Sender sender = new MessageSender();
        sender.send(absSender, chat.getId().toString(), msgHeader);
        sender.send(absSender, chat.getId().toString(), parsedMsgBody);

        updateChatLastCommand(chat.getId(), Commands.HELP);
    }

    private void appendCommands(StringBuilder builder, Long chatId) {

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.domain.Chat currentChat = chatManager.getById(chatId);

        SpreadBot.COMMANDS.entrySet().stream()
                .forEach((item) -> {
                    if (!item.getValue().isForAdmin() || currentChat.isAdmin()) {
                        builder.append("/")
                                .append(item.getValue().getCommand().getCommandIdentifier())
                                .append(" - ")
                                .append(item.getValue().getCommand().getDescription())
                                .append("\n");
                    } else {
                        builder.append("");
                    }
                });
    }
}
