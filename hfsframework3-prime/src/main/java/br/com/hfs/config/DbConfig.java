package br.com.hfs.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@PropertySource({ "classpath:database.properties" })
@EnableJpaRepositories(basePackages = {"br.com.hfs.admin.repository", "br.com.hfs.hfsfullstack.repository"})
//@EnableJpaRepositories(basePackages = "br.com.hfs")
@EnableTransactionManagement
public class DbConfig {

	@Autowired
	private Environment env;
	
	@Autowired
	private HikariDataSourceConfig hikariDS;
	
    //@Value("classpath:schema.sql")
    //private Resource schemaScript;

    //@Value("classpath:data.sql")
    //private Resource dataScript;

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean emf = new LocalContainerEntityManagerFactoryBean();
		//emf.setDataSource(dataSource());
		emf.setDataSource(hikariDS.getDataSource(env));
		emf.setPackagesToScan("br.com.hfs.admin.model", "br.com.hfs.hfsfullstack.model");
		//emf.setPackagesToScan("br.com.hfs");

		JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		emf.setJpaVendorAdapter(vendorAdapter);

		Properties additionalProperties = additionalProperties();
		
//		additionalProperties.put("hibernate.integrator_provider",
//               (IntegratorProvider) () -> Collections.singletonList(MetadataExtractorIntegrator.INSTANCE));
		
		emf.setJpaProperties(additionalProperties);
		
		return emf;
	}

    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource dataSource) {
        final DataSourceInitializer initializer = new DataSourceInitializer();
        initializer.setDataSource(dataSource);
        //initializer.setDatabasePopulator(databasePopulator());
        return initializer;
    }
/*
    private DatabasePopulator databasePopulator() {
        final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
        //populator.addScript(schemaScript);
        populator.addScript(dataScript);
        return populator;
    }

	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setUrl(env.getProperty("spring.datasource.url"));
		dataSource.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));		
		dataSource.setUsername(env.getProperty("spring.datasource.username"));
		dataSource.setPassword(env.getProperty("spring.datasource.password"));

		
		//dataSource.setUrl("jdbc:postgresql://localhost:5432/dbhefesto");
		//dataSource.setDriverClassName("org.postgresql.Driver");		
		//dataSource.setUsername("postgres");
		//dataSource.setPassword("abcd1234");		
			
		return dataSource;
	}
*/	
	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

		return transactionManager;
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	Properties additionalProperties() {
		return new Properties() {
			private static final long serialVersionUID = 1L;
			{
				
				setProperty("open-in-view", "false");
				setProperty("show-sql", "true");
				setProperty("hibernate.ddl-auto", "none");

				//setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
				setProperty("hibernate.generate_statistics", "false");
				setProperty("hibernate.show-sql", "true");
				setProperty("hibernate.format_sql", "true");
				setProperty("hibernate.connection.provider_disables_autocommit", "true");
				
				/*
				setProperty("open-in-view", env.getProperty("spring.jpa.open-in-view"));
				setProperty("show-sql", env.getProperty("spring.jpa.show-sql"));
				setProperty("hibernate.ddl-auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
				
				setProperty("hibernate.dialect", env.getProperty("spring.jpa.properties.hibernate.dialect"));
				setProperty("hibernate.generate_statistics", env.getProperty("spring.jpa.properties.hibernate.generate_statistics"));
				setProperty("hibernate.show-sql", env.getProperty("spring.jpa.properties.hibernate.show-sql"));
				setProperty("hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql"));
				setProperty("hibernate.connection.provider_disables_autocommit", env.getProperty("spring.jpa.properties.hibernate.connection.provider_disables_autocommit"));
				*/		
			}
		};
	}
}