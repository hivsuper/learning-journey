package org.lxp.gradle.config;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

public class PostgreSQLContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static final String DB_NAME = "gradle-demo";

    private static final PostgreSQLContainer<?> mysqlContainer;

    static {
        mysqlContainer = new PostgreSQLContainer<>(DockerImageName.parse("postgres:17-alpine"))
                .withUsername("testuser")
                .withPassword("testpass")
                .withDatabaseName(DB_NAME);
        mysqlContainer.start();
    }

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        System.setProperty("spring.datasource.url", mysqlContainer.getJdbcUrl());
        System.setProperty("spring.datasource.username", mysqlContainer.getUsername());
        System.setProperty("spring.datasource.password", mysqlContainer.getPassword());
    }
}