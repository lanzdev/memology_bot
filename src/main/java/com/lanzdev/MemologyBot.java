package com.lanzdev;

import com.lanzdev.commands.*;
import com.lanzdev.vk.Item;
import com.lanzdev.vk.VkWallGetter;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingCommandBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;

import java.util.List;

public class MemologyBot extends TelegramLongPollingCommandBot {

    public MemologyBot( ) {

        register(new AddPrePickedCommand());
        register(new AdminCommand());
        register(new DeletePrePickedCommand());
        HelpCommand helpCommand = new HelpCommand(this);
        register(helpCommand);
        register(new ListCommand());
        register(new MyListCommand());
        register(new PauseCommand());
        register(new PickCommand());
        register(new ResumeCommand());
        register(new SubscribeCommand());
        register(new UnsubscribeCommand());

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
            helpCommand.execute(absSender, message.getFrom(), message.getChat(), new String[] {});
        }));
    }

    @Override
    public void processNonCommandUpdate(Update update) {

        if (update.hasMessage()) {
            Message message = update.getMessage();

            if (message.hasText()) {
                SendMessage echoMessage = new SendMessage();
                echoMessage.setChatId(message.getChatId());
                echoMessage.setText("Hey here's your message:\n" + message.getText());

                try {
                    sendMessage(echoMessage);
                } catch (TelegramApiException e) {
                    e.printStackTrace();
                }
            }
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

    /*
    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            if (message.getText().equals("/help")) {
                sendText(message, "hi, i'm a robot");
            } else {
                parseAndSendMessage(message);
            }
        }
    }*/

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
            List<Item> items = vkWallGetter.getItems(domain, count, offset);

            items.stream()
                    .forEach((item) -> {
                        if (item.getText() != null && !item.getText().equals("")) {
                            sendText(message, item.getText());
                        }
                        item.getPhotos().stream()
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
