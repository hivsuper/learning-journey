package org.lxp.manual.consumer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import javax.inject.Inject;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:19092", "port=19092"})
@Import({KafkaConsumerTest.Config.class, KafkaConsumerTest.TestProducer.class})
public class KafkaConsumerTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(KafkaConsumerTest.class);
    private static final CountDownLatch MESSAGE_HANDLER_LATCH = new CountDownLatch(1);
    @Inject
    private MessageHandler messageHandler;
    @Inject
    private TestProducer producer;

    @Test
    public void handle() throws InterruptedException {
        final String messageBody = "test-message";
        producer.sendMessage(messageBody);
        boolean messageSent = producer.getLatch().await(10, TimeUnit.SECONDS);
        boolean messageConsumed = MESSAGE_HANDLER_LATCH.await(10, TimeUnit.SECONDS);
        assertTrue(messageSent);
        assertTrue(messageConsumed);
        verify(messageHandler, times(1)).handle(messageBody);
    }

    public static class Config {
        @Primary
        @Bean
        public MessageHandler messageHandler() {
            return Mockito.spy(new MyMessageHandler());
        }
    }

    public static class MyMessageHandler extends MessageHandler {
        @Override
        public void handle(String message) {
            super.handle(message);
            MESSAGE_HANDLER_LATCH.countDown();
            LOGGER.info("MyMessageHandler count down");
        }
    }

    public static class TestProducer {
        private CountDownLatch latch = new CountDownLatch(1);
        private final KafkaTemplate<String, String> kafkaTemplate;
        private final String topicName;


        @Inject
        public TestProducer(KafkaTemplate<String, String> kafkaTemplate, @Value("${test.topic}") String topicName) {
            this.kafkaTemplate = kafkaTemplate;
            this.topicName = topicName;
        }

        public void sendMessage(String message) {
            try {
                kafkaTemplate.send(topicName, UUID.randomUUID().toString(), message).get();
                latch.countDown();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
        }

        public CountDownLatch getLatch() {
            return latch;
        }
    }
}