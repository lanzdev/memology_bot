package com.lanzdev.commands;

import org.telegram.telegrambots.bots.commands.BotCommand;

public class CommandContainer {

    private BotCommand command;
    private Boolean forAdmin;

    public CommandContainer(BotCommand command, Boolean forAdmin) {
        this.command = command;
        this.forAdmin = forAdmin;
    }

    public BotCommand getCommand( ) {
        return command;
    }

    public void setCommand(BotCommand command) {
        this.command = command;
    }

    public Boolean isForAdmin( ) {
        return forAdmin;
    }

    public void setForAdmin(Boolean forAdmin) {
        this.forAdmin = forAdmin;
    }
}
