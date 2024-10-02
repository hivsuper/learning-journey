package org.lxp.gradle;

import org.lxp.gradle.config.MySQLContainerInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlMergeMode;

@ContextConfiguration(initializers = {MySQLContainerInitializer.class})
@SqlMergeMode(SqlMergeMode.MergeMode.MERGE)
@Sql(scripts = "classpath:scripts/DB_CLEAN.sql")
public class BaseTest {
}
