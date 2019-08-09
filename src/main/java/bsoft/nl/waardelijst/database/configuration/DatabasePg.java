package bsoft.nl.waardelijst.database.configuration;

import liquibase.integration.spring.SpringLiquibase;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@Profile("postgres")
@EnableJpaRepositories(
        basePackages = {"bsoft.nl.waardelijst.database.repository"},
        entityManagerFactoryRef = "entityManagerFactoryPg",
        transactionManagerRef = "transactionManagerPg")

public class DatabasePg extends Database {
    private static final Logger logger = LoggerFactory.getLogger(DatabasePg.class);

    @Bean(name = "transactionManagerPg")
    public PlatformTransactionManager transactionManagerPg() {
        return new JpaTransactionManager(entityManagerFactoryPg().getObject());
    }

    @Bean(name = "entityManagerFactoryPg")
    @Primary
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryPg() {

        HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();

        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();

        factoryBean.setDataSource(secondaryDataSource());

        factoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        factoryBean.setJpaProperties(hibernateProperties());

        factoryBean.setPackagesToScan("bsoft.nl.waardelijst.database.model");
        factoryBean.setPersistenceUnitName("pg");

        return factoryBean;
    }

    @Bean(name = "sf", destroyMethod = "")
    public SessionFactory pgSessionFactory() {
        return entityManagerFactoryPg().getObject().unwrap(SessionFactory.class);
    }

    @Bean
    @Primary
    @ConfigurationProperties(prefix = "spring.secondarydatasource")
    public DataSource secondaryDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConfigurationProperties(prefix = "datasource.secondaryliquibase.liquibase")
    public LiquibaseProperties secondaryLiquibaseProperties() {
        return new LiquibaseProperties();
    }

    @Bean("liquibase")
    public SpringLiquibase secondaryLiquibase() {
        return springLiquibase(secondaryDataSource(), secondaryLiquibaseProperties());
    }

    private Properties hibernateProperties() {
        final Properties hibernateProperties = new Properties();
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "none");
        return hibernateProperties;
    }

}
