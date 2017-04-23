package com.lanzdev;

import com.lanzdev.vk.Item;
import com.lanzdev.vk.VkWallGetter;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.TelegramBotsApi;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendPhoto;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;

import java.util.List;

public class SimpleBot extends TelegramLongPollingBot {

    public static void main(String[] args) {

        ApiContextInitializer.init();
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        try {
            telegramBotsApi.registerBot(new SimpleBot());
        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getBotUsername( ) {
        return "memolog_bot";
    }

    @Override
    public String getBotToken( ) {
        return "274552806:AAEdqbJvmuA0NPlFanC61HCfnNhbVkcL1xk";
    }

    @Override
    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();
        if (message != null && message.hasText()) {
            if (message.getText().equals("/help")) {
                sendMsg(message, "hi, i'm a robot", null);
            } else {
                parseAndSendMessage(message);
            }
        }
    }

    private void parseAndSendMessage(Message message) {

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
                            sendMsg(message, item.getText(), null);
                        }
                        item.getPhotos().stream()
                                .forEach((photo) -> {
                                    sendMsg(message, photo.getText(), photo.getSrcBig());
                                });
                    });
        } catch (Exception e) {
            sendMsg(message,"please enter <domain> <count> <offset>, chat id: "
                    + message.getChatId(), null);
        }

    }

    private void sendMsg(Message message, String text, String photo) {

        Chat chat = new Chat();
        SendMessage sendMessage = null;
        SendPhoto sendPhoto = null;

        if (text != null && !text.equals("")) {
            sendMessage = new SendMessage();
            sendMessage.enableMarkdown(true);
            sendMessage.setChatId(message.getChatId().toString());
//            sendMessage.setReplyToMessageId(message.getMessageId());
            sendMessage.setText(text);
        }
        if (photo != null && !photo.equals("")) {
            sendPhoto = new SendPhoto();
            sendPhoto.setChatId(message.getChatId().toString());
//            sendPhoto.setReplyToMessageId(message.getMessageId());
            sendPhoto.setPhoto(photo);
        }
        try {
            if (text != null && !text.equals("")) {
                sendMessage(sendMessage);
            }
            if (photo != null && !photo.equals("")) {
                sendPhoto(sendPhoto);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
