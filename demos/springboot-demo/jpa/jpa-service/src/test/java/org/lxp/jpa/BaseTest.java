package org.lxp.jpa;

import org.lxp.jpa.config.MySQLContainerInitializer;
import org.lxp.jpa.config.RedisContainerInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;

@ContextConfiguration(initializers = {MySQLContainerInitializer.class, RedisContainerInitializer.class})
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(scripts = "classpath:scripts/DB_CLEAN.sql")
public class BaseTest {
}
