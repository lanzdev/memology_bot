package com.lanzdev.services.processors.impl;

import com.lanzdev.domain.Chat;
import com.lanzdev.domain.Wall;
import com.lanzdev.managers.entity.ChatManager;
import com.lanzdev.managers.mysql.impl.MySqlChatManager;
import com.lanzdev.services.processors.AbstractProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.AbsSender;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class DeleteRecommendedProcessor extends AbstractProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteRecommendedProcessor.class);

    public DeleteRecommendedProcessor(Message message, AbsSender absSender) {
        super(message, absSender);
    }

    @Override
    public void process( ) {

        String[] params = null;
        if (message.getText().startsWith("/delete")) {
            String id = message.getText().split("_")[1];
            params = new String[]{id};
            LOGGER.debug("Processing delete recommended command for id: {}", id);
        } else {
            params = message.getText().split("[\\s,.:;]+");
            LOGGER.debug("Processing delete recommended command, params: {}", Arrays.toString(params));
        }

        ChatManager chatManager = new MySqlChatManager();
        Chat currentChat = chatManager.getById(message.getChatId());
        List<Wall> deletedWalls = new LinkedList<>();
    }
}
