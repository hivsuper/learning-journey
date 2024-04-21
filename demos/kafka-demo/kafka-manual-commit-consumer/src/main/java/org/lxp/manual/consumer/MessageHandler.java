package org.lxp.manual.consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class MessageHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageHandler.class);

    public void handle(String message) {
        LOGGER.info("MessageHandler:{}", message);
    }
}
