package org.lxp.jpa.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public class RedisContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        GenericContainer<?> redis =
                new GenericContainer<>(DockerImageName.parse("redis:7.2.2-alpine")).withExposedPorts(6379);
        redis.start();
    }
}
