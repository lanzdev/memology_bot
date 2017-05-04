package com.lanzdev.commands.entity;

import com.lanzdev.MemologyBot;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
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

        StringBuilder helpMessageBuilder = new StringBuilder("*Help*\n");
        helpMessageBuilder.append("These are commands for this Bot:\n\n");

        MemologyBot.COMMANDS.entrySet().stream()
                .forEach((item) -> {
                    String commandDescription = item.getValue().getCommand().getDescription();
                    String command = BotCommand.COMMAND_INIT_CHARACTER + item.getValue().getCommand().getCommandIdentifier();
                    helpMessageBuilder.append(
                            item.getValue().isForAdmin() ? ""
                                    : String.format("[%1$s](%1$s) - %2$s\n", command, commandDescription));
                });


        Sender sender = new MessageSender();
        sender.send(absSender, chat.getId().toString(), helpMessageBuilder.toString());
    }
}
