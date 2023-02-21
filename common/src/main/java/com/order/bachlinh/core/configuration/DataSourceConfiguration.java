package com.order.bachlinh.core.configuration;

import com.order.bachlinh.core.entities.model.BaseEntity;
import com.order.bachlinh.core.entities.spi.EntityFactory;
import com.order.bachlinh.core.entities.spi.HibernateL2CachingRegionFactory;
import com.order.bachlinh.core.entities.spi.internal.EntityFactoryBuilderProvider;
import com.order.bachlinh.core.exception.SqlDefinitionException;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.MySQLDialect;
import org.hibernate.dialect.SQLServerDialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.text.MessageFormat;
import java.util.Optional;
import java.util.Properties;

/**
 * Internal configuration for data source in this project.
 *
 * @author Hoang Minh Quan
 * */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@EnableTransactionManagement(proxyTargetClass = true, mode = AdviceMode.ASPECTJ)
class DataSourceConfiguration {
    private String databaseAddress;
    private String databaseName;
    private String username;
    private String password;
    private String driverName;
    private String useDatabase;
    private String port;
    private ApplicationContext applicationContext;

    @Bean(name = "entityManagerFactory")
    LocalSessionFactoryBean sessionFactoryBean() {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        factoryBean.setDataSource(dataSource());
        factoryBean.setPackagesToScan(BaseEntity.class.getPackage().getName());
        factoryBean.setHibernateProperties(hibernateProperties());
        factoryBean.setCacheRegionFactory(new HibernateL2CachingRegionFactory(applicationContext));
        return factoryBean;
    }

    @Bean(name = "transactionManager")
    HibernateTransactionManager transactionManager() {
        return new HibernateTransactionManager(applicationContext.getBean(SessionFactory.class));
    }

    @Bean
    EntityFactory entityFactory() {
        return EntityFactoryBuilderProvider.useDefaultEntityFactoryBuilder()
                .applicationContext(applicationContext)
                .build();
    }

    @Bean
    @Primary
    public EntityManager entityManager() {
        EntityManagerFactory entityManagerFactory = applicationContext.getBean(EntityManagerFactory.class);
        return entityManagerFactory.createEntityManager();
    }

    @Bean
    AuditorAware<Object> auditorProvider() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal;
        if (authentication != null) {
            principal = authentication.getPrincipal();
        } else {
            principal = "Application";
        }
        return () -> Optional
                .of((principal));
    }

    private DataSource dataSource() {
        String url = MessageFormat.format(
                "jdbc:{0}://{1}:{2};database={3};trustServerCertificate=true;sendTimeAsDateTime=false;", useDatabase,
                databaseAddress, port, databaseName);
        HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverName);
        config.setJdbcUrl(url);
        config.setUsername(username);
        config.setPassword(password);
        config.setAutoCommit(true);
        config.setMaxLifetime(1000L * 60L * 30L);
        config.setConnectionTimeout(1000L * 60L * 30L);
        config.setMaximumPoolSize(1000);
        config.setMinimumIdle(2);
        return new HikariDataSource(config);
    }

    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty(AvailableSettings.HBM2DDL_AUTO, "update");
        hibernateProperties.setProperty(AvailableSettings.SHOW_SQL, "true");
        hibernateProperties.setProperty(AvailableSettings.FORMAT_SQL, "true");
        hibernateProperties.setProperty(AvailableSettings.CURRENT_SESSION_CONTEXT_CLASS, "thread");
        hibernateProperties.setProperty(AvailableSettings.DIALECT, sqlDialect(useDatabase));
        hibernateProperties.setProperty(AvailableSettings.USE_SECOND_LEVEL_CACHE, "true");
        hibernateProperties.setProperty(AvailableSettings.USE_QUERY_CACHE, "true");
        return hibernateProperties;
    }

    private String sqlDialect(String databaseUse) {
        return switch (databaseUse) {
            case "sqlserver" -> SQLServerDialect.class.getName();
            case "mysql" -> MySQLDialect.class.getName();
            default -> throw new SqlDefinitionException("Application support mysql and sqlserver only");
        };
    }

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Value("${shop.database.port}")
    public void setPort(String port) {
        this.port = port;
    }

    @Value("${shop.database.use}")
    public void setUseDatabase(String useDatabase) {
        this.useDatabase = useDatabase;
    }

    @Value("${shop.database.driver}")
    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    @Value("${shop.database.username}")
    public void setUsername(String username) {
        this.username = username;
    }

    @Value("${shop.database.password}")
    public void setPassword(String password) {
        this.password = password;
    }

    @Value("${shop.database.name}")
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    @Value("${shop.database.address}")
    public void setDatabaseAddress(String databaseAddress) {
        this.databaseAddress = databaseAddress;
    }
}
