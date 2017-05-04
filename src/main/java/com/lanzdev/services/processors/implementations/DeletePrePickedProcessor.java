package com.lanzdev.services.processors.implementations;

import com.lanzdev.services.processors.AbstractProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.AbsSender;

public class DeletePrePickedProcessor extends AbstractProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeletePrePickedProcessor.class);

    public DeletePrePickedProcessor(Message message, AbsSender absSender) {
        super(message, absSender);
    }

    @Override
    public void process( ) {

        LOGGER.debug("Processing delete pre picked command.");
        super.process();
    }
}
