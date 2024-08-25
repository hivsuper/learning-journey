package org.lxp.jpa.config;

import org.junit.jupiter.api.AfterEach;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Inject;

import static org.lxp.jpa.config.MySQLContainerInitializer.DB_NAME;

public class BaseTest {
    @Inject
    private JdbcTemplate jdbcTemplate;

    @AfterEach
    public void tearDown() {
        // 获取所有表名并执行 TRUNCATE 操作
        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = '" + DB_NAME + "'";
        jdbcTemplate.queryForList(sql).forEach(row -> {
            String tableName = (String) row.get("table_name");
            jdbcTemplate.execute("TRUNCATE TABLE " + tableName);
        });
    }
}
