// package com.talentprogram.LdPlatformUserService.config;

// import org.springframework.core.env.Environment;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.context.annotation.PropertySource;
// import org.apache.commons.dbcp2.BasicDataSource;
// import javax.sql.DataSource;

// @Configuration
// @PropertySource("classpath:application.properties")
// public class DataSourceConfig 
// {
//     @Bean
//     public DataSource dataSource(Environment env) 
//     {
//         BasicDataSource dataSource = new BasicDataSource();
//         dataSource.setDriverClassName(env.getProperty("db.driver"));
//         dataSource.setUrl(env.getProperty("db.url"));
//         dataSource.setUsername(env.getProperty("db.username"));
//         dataSource.setPassword(env.getProperty("db.password"));
//         return dataSource;
//     }

// }
