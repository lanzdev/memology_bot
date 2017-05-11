package com.lanzdev.services.processors.impl;

import com.lanzdev.services.processors.AbstractProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.AbsSender;

public class ResumeProcessor extends AbstractProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(ResumeProcessor.class);

    public ResumeProcessor(Message message, AbsSender absSender) {
        super(message, absSender);
    }

    @Override
    public void process( ) {

        LOGGER.debug("Processing resume command.");
        super.process();
    }
}
