package cs533.project.harness.configuration;

import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Setter
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("spring.datasource")
public class SqlConfiguration
{
    private String url;
    private String username;
    private String password;
    private String driverClassName;
    @Value("${application.databaseName}")
    private String databaseName;

    @Bean
    public DataSource getDataSource() {
        return DataSourceBuilder.create()
                .url(url + databaseName)
                .username(username)
                .password(password)
                .driverClassName(driverClassName)
                .build();
    }
}
