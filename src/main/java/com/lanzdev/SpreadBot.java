package com.lanzdev;

import com.lanzdev.commands.CommandContainer;
import com.lanzdev.commands.Commands;
import com.lanzdev.commands.entity.*;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.impl.MySqlChatManager;
import com.lanzdev.domain.Chat;
import com.lanzdev.services.processors.Processor;
import com.lanzdev.services.processors.ProcessorFactory;
import com.lanzdev.services.senders.MessageSender;
import com.lanzdev.services.senders.Sender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;

import java.util.HashMap;
import java.util.Map;

public class SpreadBot extends TelegramLongPollingCommandBot {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpreadBot.class);
    public static final Map<String, CommandContainer> COMMANDS = new HashMap<>();

    public SpreadBot( ) {

        LOGGER.debug("Enter MemologyBot().");
        COMMANDS.put(Commands.ADD_RECOMMENDED, new CommandContainer(new AddRecommendedCommand(), true));
        COMMANDS.put(Commands.CREATE_NOTIFICATION, new CommandContainer(new CreateNotificationCommand(), true));
        COMMANDS.put(Commands.DELETE_RECOMMENDED, new CommandContainer(new DeleteRecommendedCommand(), true));
        COMMANDS.put(Commands.HELP, new CommandContainer(new HelpCommand(this), false));
        COMMANDS.put(Commands.LIST, new CommandContainer(new ListCommand(), false));
        COMMANDS.put(Commands.MY_LIST, new CommandContainer(new MyListCommand(), false));
        COMMANDS.put(Commands.PAUSE, new CommandContainer(new PauseCommand(), false));
        COMMANDS.put(Commands.PICK, new CommandContainer(new PickCommand(), false));
        COMMANDS.put(Commands.RESUME, new CommandContainer(new ResumeCommand(), false));
        COMMANDS.put(Commands.START, new CommandContainer(new StartCommand(), false));
        COMMANDS.put(Commands.SUBSCRIBE, new CommandContainer(new SubscribeCommand(), false));
        COMMANDS.put(Commands.UNSUBSCRIBE, new CommandContainer(new UnsubscribeCommand(), false));
        COMMANDS.entrySet().stream()
                .forEach((item) -> register(item.getValue().getCommand()));
        LOGGER.info("Fill map COMMANDS with {} items", COMMANDS.size());

        registerDefaultAction(((absSender, message) -> {
            if (message.getText() != null
                    && Constants.subCommands.contains(message.getText().split("_")[0])) {
                executeSubCommand(absSender, message);
            } else {
                String commandUnknownMessage = String.format(
                        "The command %s is unknown by this bot. Here comes some help", message.getText());
                Sender sender = new MessageSender();
                sender.send(absSender, message.getChatId().toString(), commandUnknownMessage);
                COMMANDS.get(Commands.HELP)
                        .getCommand().execute(absSender, message.getFrom(), message.getChat(), new String[]{});
            }
        }));
        LOGGER.debug("Leaving MemologyBot()");
    }

    private void executeSubCommand(AbsSender absSender, Message message) {

        ChatManager chatManager = new MySqlChatManager();
        Chat currentChat = chatManager.getById(message.getChatId());
        String lastCommand = currentChat.getLastCommand();
        Processor processor = ProcessorFactory.getProcessor(message, absSender, lastCommand);
        processor.process();
    }


    @Override
    public void processNonCommandUpdate(Update update) {

        LOGGER.debug("Enter processNonCommandUpdate().");
        if (update.hasMessage()) {
            Message message = update.getMessage();
            ChatManager chatManager = new MySqlChatManager();
            Chat currentChat = chatManager.getById(message.getChatId());
            String lastCommand = currentChat.getLastCommand();
            LOGGER.info("update has message, message: \"{}\". Process it.", message.getText());

            Processor processor = ProcessorFactory.getProcessor(message, this, lastCommand);
            processor.process();
        }
        LOGGER.debug("Leaving processNonCommandUpdate().");
    }

    @Override
    public String getBotUsername( ) {
        return BotConfig.MEMOLOG_NAME;
    }

    @Override
    public String getBotToken( ) {
        return BotConfig.MEMOLOG_TOKEN;
    }
}
