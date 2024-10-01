package org.lxp.gradle;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DBMigration {
    public static void main(String[] args) {
        SpringApplication.run(DBMigration.class, args);
    }
}