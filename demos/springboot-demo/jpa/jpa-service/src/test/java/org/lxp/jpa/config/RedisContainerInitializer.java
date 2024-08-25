package org.lxp.jpa.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

public class RedisContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final GenericContainer<?> redisContainer;

    static {
        redisContainer = new GenericContainer<>(DockerImageName.parse("redis:7.2.2-alpine"))
                .withExposedPorts(6379)
                .waitingFor(Wait.forListeningPort());
        redisContainer.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.setProperty("spring.data.redis.host", redisContainer.getHost());
        System.setProperty("spring.data.redis.port", String.valueOf(redisContainer.getMappedPort(6379)));
    }
}
