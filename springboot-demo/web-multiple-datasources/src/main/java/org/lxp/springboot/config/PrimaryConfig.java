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
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@MapperScan(basePackages = PrimaryConfig.BASE_PACKAGES, sqlSessionTemplateRef = "sqlSessionTemplatePrimary")
public class PrimaryConfig {
    public static final String BASE_PACKAGES = "org.lxp.springboot.dao.primary";
    public static final String MAPPER_XML_PATH = "classpath:org/lxp/springboot/dao/primary/*.xml";
    @Resource
    private DataSource primaryDataSource;

    @Primary
    @Bean(name = "sqlSessionFactoryPrimary")
    public SqlSessionFactory sqlSessionFactory() throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(primaryDataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources(MAPPER_XML_PATH));
        return bean.getObject();
    }

    @Primary
    @Bean(name = "transactionManagerPrimary")
    PlatformTransactionManager transactionManagerSecondary(EntityManagerFactoryBuilder builder) {
        return new DataSourceTransactionManager(primaryDataSource);
    }

    @Primary
    @Bean(name = "sqlSessionTemplatePrimary")
    public SqlSessionTemplate sqlSessionTemplate(
            @Qualifier("sqlSessionFactoryPrimary") SqlSessionFactory sqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}