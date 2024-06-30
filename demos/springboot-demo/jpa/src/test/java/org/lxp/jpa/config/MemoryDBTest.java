package org.lxp.jpa.config;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;
import org.springframework.test.context.web.WebAppConfiguration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.springframework.test.context.jdbc.SqlMergeMode.MergeMode.MERGE;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@WebAppConfiguration
@SqlMergeMode(MERGE)
@Sql(scripts = "classpath:test-db-schema.sql")
@ActiveProfiles("test")
public @interface MemoryDBTest {
}
