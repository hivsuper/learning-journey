package org.lxp.springboot;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.lxp.springboot.config.MySQLContainerInitializer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;

import javax.inject.Inject;

import static org.lxp.springboot.config.MySQLContainerInitializer.DB_NAME;

@ContextConfiguration(initializers = MySQLContainerInitializer.class)
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

    @RegisterExtension
    static GreenMailExtension greenMail = new GreenMailExtension(ServerSetupTest.SMTP)
            .withConfiguration(GreenMailConfiguration.aConfig().withUser("user", "admin"))
            .withPerMethodLifecycle(false);
}
