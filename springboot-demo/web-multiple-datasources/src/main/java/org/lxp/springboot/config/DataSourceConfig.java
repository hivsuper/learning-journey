package org.lxp.springboot.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariDataSource;

/**
 * http://www.jianshu.com/p/34730e595a8c
 * https://gitee.com/didispace/SpringBoot-Learning/blob/master/Chapter3-2-4/src/main/java/com/didispace/DataSourceConfig.java
 * http://guobinwu.leanote.com/post/springboot-mybatis-multidatasource
 */
@Configuration
@ConfigurationProperties(prefix = "spring.datasource.hikari")
public class DataSourceConfig {
    private int connectionTimeout;
    private int maximumPoolSize;
    private int minimumIdle;

    @Primary
    @Bean(name = "primaryDataSource")
    @Qualifier("primaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    public DataSource primaryDataSource() {
        HikariDataSource dataSource = (HikariDataSource) DataSourceBuilder.create().type(HikariDataSource.class)
                .build();
        dataSource.setConnectionTimeout(connectionTimeout);
        dataSource.setMaximumPoolSize(maximumPoolSize);
        dataSource.setMinimumIdle(minimumIdle);
        return dataSource;
    }

    @Bean(name = "secondaryDataSource")
    @Qualifier("secondaryDataSource")
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    public DataSource secondaryDataSource() {
        HikariDataSource dataSource = (HikariDataSource) DataSourceBuilder.create().type(HikariDataSource.class)
                .build();
        dataSource.setConnectionTimeout(connectionTimeout);
        dataSource.setMaximumPoolSize(maximumPoolSize);
        dataSource.setMinimumIdle(minimumIdle);
        return dataSource;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setMaximumPoolSize(int maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public void setMinimumIdle(int minimumIdle) {
        this.minimumIdle = minimumIdle;
    }
}
