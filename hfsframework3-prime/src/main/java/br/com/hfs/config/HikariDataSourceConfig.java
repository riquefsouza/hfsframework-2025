package br.com.hfs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class HikariDataSourceConfig {
	
    //@Value("classpath:hikari.properties")
    //private Resource hikariResource;

    @Bean
    public HikariDataSource getDataSource(Environment env) {
		//HikariConfig config = new HikariConfig(hikariResource.getFile().getAbsolutePath());
    	
    	HikariConfig config = new HikariConfig();
        
    	/*
    	config.setJdbcUrl(env.getProperty("spring.datasource.url"));
        config.setDriverClassName(env.getProperty("spring.datasource.driverClassName"));
        config.setUsername(env.getProperty("spring.datasource.username"));
        config.setPassword(env.getProperty("spring.datasource.password"));
        
        config.setAutoCommit(Boolean.valueOf(env.getProperty("spring.datasource.hikari.auto-commit")));
        config.setConnectionTimeout(Long.valueOf(env.getProperty("spring.datasource.hikari.connection-timeout")));
        config.setMaxLifetime(Long.valueOf(env.getProperty("spring.datasource.hikari.max-lifetime")));
        config.setMaximumPoolSize(Integer.valueOf(env.getProperty("spring.datasource.hikari.maximum-pool-size")));
        config.setMinimumIdle(Integer.valueOf(env.getProperty("spring.datasource.hikari.minimum-idle")));
        config.setPoolName(env.getProperty("spring.datasource.hikari.pool-name"));
        */        
        
    	config.setJdbcUrl("jdbc:postgresql://localhost:5432/dbhefesto");
        config.setDriverClassName("org.postgresql.Driver");
        config.setUsername("postgres");
        config.setPassword("abcd1234");
    	
        config.setAutoCommit(false);
        config.setConnectionTimeout(250);
        config.setMaxLifetime(600000L);
        config.setMaximumPoolSize(20);
        config.setMinimumIdle(10);
        config.setPoolName("master");
        
        
		HikariDataSource dataSource = new HikariDataSource(config);
    	return dataSource;
    }

}
