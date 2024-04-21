package org.lxp.auto.consumer.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Component
public class KafkaProducer {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final String TOPIC_NAME = "test-topic";

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendMessage(String message) {
        String messageId = UUID.randomUUID().toString();
        System.out.println(messageId);
        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(TOPIC_NAME, messageId, message);
        future.whenComplete((result, exception) -> {
            if (Objects.isNull(exception)) {
                LOGGER.info("Message {} has been successfully sent to the topic {}, partition {}, offset {}",
                        message, TOPIC_NAME, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            } else {
                LOGGER.error("Failed to send message {} to the topic {}", message, TOPIC_NAME);
            }
        });
    }
}
