package com.software.aws_spring_boot_native_dsql.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dsql.DsqlUtilities;

import javax.sql.DataSource;

@Profile("!local")
@Configuration
public class DataSourceConfiguration {

    @Value("${spring.datasource.cluster-endpoint}")
    private String clusterEndpoint;

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.username}")
    private String dbUsername;

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        String password = generateToken(clusterEndpoint);

        if (password == null || password.isEmpty()) {
            throw new IllegalStateException("Failed to generate database token");
        }

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driverClassName);
        dataSource.setUrl(dbUrl);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(password);

        return dataSource;
    }

    private String generateToken(String clusterEndpoint) {
        DsqlUtilities utilities = DsqlUtilities.builder()
                .region(Region.US_EAST_1)
                .credentialsProvider(DefaultCredentialsProvider.create())
                .build();

        return utilities.generateDbConnectAdminAuthToken(
                builder -> builder.hostname(clusterEndpoint).region(Region.US_EAST_1));
    }
}
