package org.lxp.jpa.config;

import org.flywaydb.core.Flyway;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

public class MySQLContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    public static final String DB_NAME = "springboot-jpa";

    private static final MySQLContainer<?> mysqlContainer;

    static {
        mysqlContainer = new MySQLContainer<>(DockerImageName.parse("mysql:8.1"))
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

        final var flyway = Flyway.configure()
                .dataSource(mysqlContainer.getJdbcUrl(), mysqlContainer.getUsername(), mysqlContainer.getPassword())
                .load();
        flyway.migrate();
    }
}