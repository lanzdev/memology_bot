package com.lanzdev.services.processors.impl;

import com.lanzdev.services.processors.AbstractProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.AbsSender;

public class HelpProcessor extends AbstractProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(HelpProcessor.class);

    public HelpProcessor(Message message, AbsSender absSender) {
        super(message, absSender);
    }

    @Override
    public void process( ) {

        LOGGER.debug("Processing help command.");
        super.process();
    }
}
