package org.lxp.springboot.config;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = SecondaryConfig.BASE_PACKAGES, sqlSessionTemplateRef = "sqlSessionTemplateSecondary")
public class SecondaryConfig {
    public static final String BASE_PACKAGES = "org.lxp.springboot.dao.secondary";
    public static final String MAPPER_XML_PATH = "classpath:org/lxp/springboot/dao/secondary/*.xml";
    @Resource
    private DataSource secondaryDataSource;

    @Bean(name = "sqlSessionFactorySecondary")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(secondaryDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_XML_PATH));
        return bean.getObject();
    }

    @Bean(name = "transactionManagerSecondary")
    PlatformTransactionManager transactionManagerSecondary(EntityManagerFactoryBuilder builder) {
        return new DataSourceTransactionManager(secondaryDataSource);
    }

    @Bean(name = "sqlSessionTemplateSecondary")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("sqlSessionFactorySecondary") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}