package com.lanzdev.services.processors.implementations;

import com.lanzdev.services.processors.AbstractProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.AbsSender;

public class AddPrePickProcessor extends AbstractProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddPrePickProcessor.class);

    public AddPrePickProcessor(Message message, AbsSender absSender) {
        super(message, absSender);
    }

    @Override
    public void process( ) {

        LOGGER.debug("Processing add pre pick command.");
        super.process();
    }
}