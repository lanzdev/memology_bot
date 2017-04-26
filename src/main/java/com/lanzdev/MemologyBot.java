package com.lanzdev;

import com.lanzdev.commands.CommandContainer;
import com.lanzdev.commands.Commands;
import com.lanzdev.commands.entity.*;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.implementation.MySqlChatManager;
import com.lanzdev.utils.CommandProcessor;
import com.lanzdev.vk.wall.WallItem;
import com.lanzdev.vk.wall.VkWallGetter;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemologyBot extends TelegramLongPollingCommandBot {


    public static final Map<String, CommandContainer> COMMANDS = new HashMap<>();

    public MemologyBot( ) {

        COMMANDS.put(Commands.ADD_PRE_PICKED, new CommandContainer(new AddPrePickedCommand(), true));
        COMMANDS.put(Commands.ADMIN, new CommandContainer(new AdminCommand(), true));
        COMMANDS.put(Commands.DELETE_PRE_PICKED, new CommandContainer(new DeletePrePickedCommand(), true));
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

        registerDefaultAction(((absSender, message) -> {
            SendMessage commandUnknownMessage = new SendMessage();
            commandUnknownMessage.setChatId(message.getChatId());
            commandUnknownMessage.setText("The command '" + message.getText()
                    + "' is unknown by this bot. Here comes some help");
            try {
                absSender.sendMessage(commandUnknownMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            COMMANDS.get(Commands.HELP)
                    .getCommand().execute(absSender, message.getFrom(), message.getChat(), new String[] {});
        }));
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();

            ChatManager chatManager = new MySqlChatManager();
            com.lanzdev.model.entity.Chat currentChat = chatManager.getById(message.getChatId());
            String lastCommand = currentChat.getLastCommand();

            CommandProcessor processor = new CommandProcessor(message, lastCommand, this);
            processor.process();
//
//            if (message.hasText()) {
//                SendMessage echoMessage = new SendMessage();
//                echoMessage.setChatId(message.getChatId());
//                echoMessage.setText("Hey here's your message:\n" + message.getText());
//
//                try {
//                    sendMessage(echoMessage);
//                } catch (TelegramApiException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    @Override
    public String getBotUsername( ) {
        return BotConfig.MEMOLOG_NAME;
    }

    @Override
    public String getBotToken( ) {
        return BotConfig.MEMOLOG_TOKEN;
    }

    private void parseAndSendMessage(Message message) {

        Chat chat = message.getChat();
        String firstName = chat.getFirstName();
        String lastName = chat.getLastName();
        String userName = chat.getUserName();
        String[] params = message.getText().split(" ");
        try {
            String domain = params[0];
            Integer count = Integer.parseInt(params[1]);
            Integer offset = Integer.parseInt(params[2]);
            VkWallGetter vkWallGetter = new VkWallGetter();
            List<WallItem> wallItems = vkWallGetter.getItems(domain, count, offset);

            wallItems.stream()
                    .forEach((wallItem) -> {
                        if (wallItem.getText() != null && !wallItem.getText().equals("")) {
                            sendText(message, wallItem.getText());
                        }
                        wallItem.getPhotos().stream()
                                .forEach((photo) -> {
                                    sendPhoto(message, photo.getSrcBig());
                                });
                    });
        } catch (Exception e) {
            sendText(message,
                    "please enter <domain> <count> <offset>, chat id: "
                            + message.getChatId() + " firstName: " + firstName
                            + " lastName: " + lastName + " userName: " + userName);
        }

    }

    private void sendPhoto(Message message, String photo) {

        SendPhoto sendPhoto = null;

        if (photo != null && !photo.equals("")) {
            sendPhoto = new SendPhoto();
            sendPhoto.setChatId(message.getChatId().toString());
            sendPhoto.setPhoto(photo);

            try {
                sendPhoto(sendPhoto);
            } catch (TelegramApiException e) {
                System.out.println("can't send a photo: " + sendPhoto.getPhoto());
                System.out.println(e);
            }
        }
    }

    private void sendText(Message message, String text) {

        SendMessage sendMessage = null;

        if (text != null && !text.equals("")) {
            sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(message.getChatId().toString());
            sendMessage.setText(text);

            try {
                sendMessage(sendMessage);
            } catch (TelegramApiException e) {
                System.out.println("can't send a text: " + sendMessage.getText());
                System.out.println(e);
            }
        }
    }
}
