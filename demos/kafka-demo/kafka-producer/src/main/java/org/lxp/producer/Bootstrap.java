package org.lxp.producer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.inject.Inject;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootApplication
public class Bootstrap implements CommandLineRunner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);
    private KafkaProducer kafkaProducer;

    @Inject
    public Bootstrap(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    public static void main(String[] args) {
        SpringApplication.run(Bootstrap.class, args);
    }

    @Override
    public void run(String... args) {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        // Start a separate thread to read input from System.in
        executor.submit(() -> {
            try {
                while (true) {
                    Thread.sleep(1000); // Sleep for 1 second
                    System.out.println("Enter a message:");
                    Scanner scanner = new Scanner(System.in);
                    String message = scanner.nextLine();
                    kafkaProducer.sendMessage(message);
                }
            } catch (InterruptedException e) {
                LOGGER.error(e.getMessage(), e);
            }
        });
    }
}