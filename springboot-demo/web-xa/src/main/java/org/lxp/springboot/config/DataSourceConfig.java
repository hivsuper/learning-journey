package org.lxp.springboot.config;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;

/**
 * http://blog.csdn.net/u011493599/article/details/66973138
 */
@Configuration
public class DataSourceConfig {
    private static final String ATOMIKOS_TRANSACTION_MANAGER = "atomikosTransactionManager";
    private static final String USER_TRANSACTION = "userTransaction";
    private static final String SECONDARY_DATA_SOURCE = "secondaryDataSource";
    private static final String PRIMARY_DATA_SOURCE = "primaryDataSource";
    @Resource
    private PrimaryDataSourceConfig primaryDataSourceConfig;
    @Resource
    private SecondaryDataSourceConfig secondaryDataSourceConfig;

    @Primary
    @Bean(name = PRIMARY_DATA_SOURCE)
    @Qualifier(PRIMARY_DATA_SOURCE)
    public DataSource primaryDataSource() {
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(primaryDataSourceConfig.getJdbcUrl());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXaDataSource.setPassword(primaryDataSourceConfig.getPassword());
        mysqlXaDataSource.setUser(primaryDataSourceConfig.getUsername());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName(PRIMARY_DATA_SOURCE);

        xaDataSource.setMaxPoolSize(secondaryDataSourceConfig.getMaximumPoolSize());
        xaDataSource.setBorrowConnectionTimeout(secondaryDataSourceConfig.getConnectionTimeout());
        xaDataSource.setMinPoolSize(secondaryDataSourceConfig.getMinimumIdle());
        return xaDataSource;
    }

    @Bean(name = SECONDARY_DATA_SOURCE)
    @Qualifier(SECONDARY_DATA_SOURCE)
    public DataSource secondaryDataSource() {
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(secondaryDataSourceConfig.getJdbcUrl());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXaDataSource.setPassword(secondaryDataSourceConfig.getPassword());
        mysqlXaDataSource.setUser(secondaryDataSourceConfig.getUsername());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName(SECONDARY_DATA_SOURCE);

        xaDataSource.setMaxPoolSize(secondaryDataSourceConfig.getMaximumPoolSize());
        xaDataSource.setBorrowConnectionTimeout(secondaryDataSourceConfig.getConnectionTimeout());
        xaDataSource.setMinPoolSize(secondaryDataSourceConfig.getMinimumIdle());
        return xaDataSource;
    }

    @Bean(name = USER_TRANSACTION)
    public UserTransaction userTransaction() throws Throwable {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(10000);
        return userTransactionImp;
    }

    @Bean(name = ATOMIKOS_TRANSACTION_MANAGER, initMethod = "init", destroyMethod = "close")
    public TransactionManager atomikosTransactionManager() throws Throwable {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(false);
        return userTransactionManager;
    }

    @Bean(name = "transactionManager")
    @DependsOn({ USER_TRANSACTION, ATOMIKOS_TRANSACTION_MANAGER })
    public PlatformTransactionManager transactionManager() throws Throwable {
        UserTransaction userTransaction = userTransaction();
        JtaTransactionManager manager = new JtaTransactionManager(userTransaction, atomikosTransactionManager());
        return manager;
    }

}
