package com.lanzdev.services.processors;

import com.lanzdev.MemologyBot;
import com.lanzdev.commands.Commands;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.AbsSender;

public abstract class AbstractProcessor implements Processor {

    protected Message message;
    protected AbsSender bot;

    public AbstractProcessor(Message message, AbsSender absSender) {
        this.message = message;
        this.bot = absSender;
    }

    @Override
    public void process( ) {
        MemologyBot.COMMANDS.get(Commands.HELP).getCommand()
                .execute(bot, message.getFrom(), message.getChat(), message.getText().split(" "));
    }
}
