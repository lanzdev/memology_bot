package com.lanzdev.commands.entity;

import com.lanzdev.MemologyBot;
import com.lanzdev.commands.Commands;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.implementation.MySqlChatManager;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import com.lanzdev.util.MarkdownParser;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.bots.commands.ICommandRegistry;

public class HelpCommand extends BotCommand {

    private final ICommandRegistry commandRegistry;

    public HelpCommand(ICommandRegistry commandRegistry) {
        super("help", "See allowed commands");
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] arguments) {

        String msgHeader = "*Help*";
        StringBuilder msgBody = new StringBuilder();
        msgBody.append("These are commands for this Bot:\n");
        appendCommands(msgBody);

        String parsedMsgBody = MarkdownParser.parse(msgBody.toString());
        Sender sender = new MessageSender();
        sender.send(absSender, chat.getId().toString(), msgHeader);
        sender.send(absSender, chat.getId().toString(), parsedMsgBody);

        updateChatLastCommand(chat.getId());
    }

    private void appendCommands(StringBuilder builder) {

        MemologyBot.COMMANDS.entrySet().stream()
                .forEach((item) -> {
                    if (item.getValue().isForAdmin()) {
                        builder.append("");
                    } else {
                        builder.append("/")
                                .append(item.getValue().getCommand().getCommandIdentifier())
                                .append(" - ")
                                .append(item.getValue().getCommand().getDescription())
                                .append("\n");
                    }
                });
    }

    private void updateChatLastCommand(Long chatId) {

        ChatManager chatManager = new MySqlChatManager();
        com.lanzdev.model.entity.Chat currentChat = chatManager.getById(chatId);
        currentChat.setLastCommand(Commands.HELP);
        chatManager.update(currentChat);
    }
}
