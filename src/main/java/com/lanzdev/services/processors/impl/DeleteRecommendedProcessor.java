package com.lanzdev.services.processors.impl;

import com.lanzdev.services.processors.AbstractProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.AbsSender;

public class DeleteRecommendedProcessor extends AbstractProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteRecommendedProcessor.class);

    public DeleteRecommendedProcessor(Message message, AbsSender absSender) {
        super(message, absSender);
    }

    @Override
    public void process( ) {

        LOGGER.debug("Processing delete pre picked command.");
        super.process();
    }
}
