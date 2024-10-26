package cs533.project.harness.configuration;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "cs533.project.harness")
public class MongoConfiguration extends AbstractMongoClientConfiguration
{
    @Value("${application.databaseName}")
    private String databaseName;
    @Value("${mongo.hostName}")
    private String mongoHostName;

    @Override
    protected String getDatabaseName()
    {
        return databaseName;
    }

    @Override
    public MongoClient mongoClient()
    {
        ConnectionString connectionString = new ConnectionString(String.format("mongodb://%s:27017/%s", mongoHostName, databaseName));
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .build();
        return MongoClients.create(mongoClientSettings);
    }
}
