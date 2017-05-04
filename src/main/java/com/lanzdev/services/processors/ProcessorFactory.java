package com.lanzdev.services.processors;

import com.lanzdev.commands.Commands;
import com.lanzdev.services.processors.implementations.*;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.AbsSender;

public class ProcessorFactory {

    public static Processor getProcessor(Message message, AbsSender absSender, String lastCommand) {

        switch (lastCommand) {
            case Commands.ADD_PRE_PICKED:
                return new AddPrePickProcessor(message, absSender);
            case Commands.ADMIN:
                return new AdminProcessor(message, absSender);
            case Commands.DELETE_PRE_PICKED:
                return new DeletePrePickedProcessor(message, absSender);
            case Commands.HELP:
                return new HelpProcessor(message, absSender);
            case Commands.LIST:
                return new ListProcessor(message, absSender);
            case Commands.MY_LIST:
                return new MyListProcessor(message, absSender);
            case Commands.PAUSE:
                return new PauseProcessor(message, absSender);
            case Commands.PICK:
                return new PickProcessor(message, absSender);
            case Commands.RESUME:
                return new ResumeProcessor(message, absSender);
            case Commands.START:
                return new StartProcessor(message, absSender);
            case Commands.SUBSCRIBE:
                return new SubscribeProcessor(message, absSender);
            case Commands.UNSUBSCRIBE:
                return new UnsubscribeProcessor(message, absSender);
            default:
                return new HelpProcessor(message, absSender);

        }
    }
}
