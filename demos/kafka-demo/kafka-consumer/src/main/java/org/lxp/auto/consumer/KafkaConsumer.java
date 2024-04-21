package org.lxp.auto.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumer.class);

    @KafkaListener(topics = "test-topic")
    public void handle(ConsumerRecords<String, String> records) {
        records.forEach(record -> {
            LOGGER.info("receive message key={} value={} topic={} partition={}, offset={}",
                    record.key(), record.value(), record.topic(), record.partition(), record.offset());
        });
    }
}
