package org.lxp.producer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;

import javax.inject.Inject;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@DirtiesContext
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:19092", "port=19092"})
@Import(KafkaProducerTest.TestConsumer.class)
public class KafkaProducerTest {
    private final TestConsumer consumer;
    private final KafkaProducer producer;

    @Inject
    public KafkaProducerTest(TestConsumer consumer, KafkaProducer producer) {
        this.consumer = consumer;
        this.producer = producer;
    }

    @Test
    public void sendMessage() throws Exception {
        String data = "Sending with our own simple KafkaProducer";

        producer.sendMessage(data);

        boolean messageConsumed = consumer.getLatch().await(10, TimeUnit.SECONDS);
        assertTrue(messageConsumed);
        assertThat(consumer.getPayload(), containsString(data));
    }

    public static class TestConsumer {
        private final CountDownLatch latch = new CountDownLatch(1);
        private String payload;

        @KafkaListener(topics = "${test.topic}")
        public void receive(ConsumerRecord<?, ?> consumerRecord) {
            payload = consumerRecord.toString();
            latch.countDown();
        }

        public CountDownLatch getLatch() {
            return latch;
        }

        public String getPayload() {
            return payload;
        }
    }
}