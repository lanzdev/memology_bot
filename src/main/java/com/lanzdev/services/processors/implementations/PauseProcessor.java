package com.lanzdev.services.processors.implementations;

import com.lanzdev.services.processors.AbstractProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.AbsSender;

public class PauseProcessor extends AbstractProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(PauseProcessor.class);

    public PauseProcessor(Message message, AbsSender absSender) {
        super(message, absSender);
    }

    @Override
    public void process( ) {

        LOGGER.debug("Processing pause command.");
        super.process();
    }
}
