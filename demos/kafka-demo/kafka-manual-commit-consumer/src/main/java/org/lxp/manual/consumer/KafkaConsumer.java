package org.lxp.manual.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
public class KafkaConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);
    private final MessageHandler messageHandler;

    @Inject
    public KafkaConsumer(MessageHandler messageHandler) {
        this.messageHandler = messageHandler;
    }

    @KafkaListener(topics = "${test.topic}")
    public void handle(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        LOGGER.info("receive message key={} topic={} partition={}, offset={}",
                record.key(), record.topic(), record.partition(), record.offset());
        messageHandler.handle(record.value());
        acknowledgment.acknowledge();
    }
}
