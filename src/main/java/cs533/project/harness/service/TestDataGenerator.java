package cs533.project.harness.service;

import cs533.project.harness.data.mongo.MongoAuthenticationHandler;
import cs533.project.harness.data.mongo.MongoCredentialHandler;
import cs533.project.harness.data.mongo.MongoPostsHandler;
import cs533.project.harness.data.sql.SqlAuthenticationHandler;
import cs533.project.harness.data.sql.SqlCredentialHandler;
import cs533.project.harness.data.sql.SqlPostsHandler;
import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class TestDataGenerator
{
    private final MongoCredentialHandler mongoCredentialHandler;
    private final MongoAuthenticationHandler mongoAuthenticationHandler;
    private final MongoPostsHandler mongoPostsHandler;
    private final SqlCredentialHandler sqlCredentialHandler;
    private final SqlAuthenticationHandler sqlAuthenticationHandler;
    private final SqlPostsHandler sqlPostsHandler;
    private final List<String> testEmails = new ArrayList<>();
    private final String testPassword;
    private final String testPassword2;
    private final String testPost = "This is test post number %d";
    private final int testDataOffset;
    private final int testDataAmount;
    public TestDataGenerator(@Value("${data.testEmailsFile}") final String testEmailsFileName,
                             final MongoCredentialHandler mongoCredentialHandler,
                             final MongoAuthenticationHandler mongoAuthenticationHandler,
                             final MongoPostsHandler mongoPostsHandler,
                             final SqlCredentialHandler sqlCredentialHandler,
                             final SqlAuthenticationHandler sqlAuthenticationHandler,
                             final SqlPostsHandler sqlPostsHandler,
                             final StringEncryptor stringEncryptor,
                             @Value("${data.testDataOffset}") final int testDataOffset,
                             @Value("${data.testDataAmount}") final int testDataAmount) throws IOException
    {
        this.mongoCredentialHandler = mongoCredentialHandler;
        this.mongoAuthenticationHandler = mongoAuthenticationHandler;
        this.mongoPostsHandler = mongoPostsHandler;
        this.sqlCredentialHandler = sqlCredentialHandler;
        this.sqlAuthenticationHandler = sqlAuthenticationHandler;
        this.sqlPostsHandler = sqlPostsHandler;
        loadTestEmails(testEmailsFileName, testDataOffset, testDataAmount);
        this.testPassword = stringEncryptor.encrypt("test_password");
        this.testPassword2 = stringEncryptor.encrypt("test_password2");
        this.testDataOffset = testDataOffset;
        this.testDataAmount = testDataAmount;
    }

    //runs tests on a given number of iterations
    public void runNumberOfIterations(int numberOfIterations)
    {
        StringBuilder resultBuilder = new StringBuilder();
        String results = resultBuilder
                .append(generateMongoTestUsers(numberOfIterations))
                .append(generateSqlTestUsers(numberOfIterations))
                .append(generateMongoPosts(numberOfIterations))
                .append(generateSqlPosts(numberOfIterations))
                .append(readMongoDbPosts())
                .append(readSqlDbPosts())
                .append(updateMongoDbPasswords())
                .append(updateSqlDbPasswords())
                .append(deleteMongoDbPosts())
                .append(deleteSqlDbPosts())
                .append(deleteMongoDbTestUsers(numberOfIterations))
                .append(deleteSqlDbTestUsers(numberOfIterations))
                .toString();

        writeResultsFile(results, numberOfIterations);
    }

    private String generateMongoTestUsers(int numberOfIterations)
    {
        long mongoDbStart = System.nanoTime();

        for(int i = 0; i < numberOfIterations; i++)
        {
            //mongodb
            //create credential entry
            String userId = mongoCredentialHandler.saveUserCredentials(testEmails.get(i));
            //create authentication entry
            mongoAuthenticationHandler.saveUserAuthentication(userId, testPassword);
        }

        long mongoDbStop = System.nanoTime();
        long timeElapsed = mongoDbStop - mongoDbStart;
        return "MONGO CREATE USERS: " + TimeUnit.NANOSECONDS.toMillis(timeElapsed) + "\n";
    }

    private String generateSqlTestUsers(int numberOfIterations)
    {
        long sqlDbStart = System.nanoTime();

        for(int i = 0; i < numberOfIterations; i++)
        {
            //sql
            //create credential entry
            String userId = sqlCredentialHandler.saveUserCredentials(testEmails.get(i));
            //create authentication entry
            sqlAuthenticationHandler.saveUserAuthentication(userId, testPassword);
        }

        long sqlDbStop = System.nanoTime();
        long timeElapsed = sqlDbStop - sqlDbStart;
        return "SQL CREATE USERS: " + TimeUnit.NANOSECONDS.toMillis(timeElapsed) + "\n";
    }

    private String generateMongoPosts(int numberOfIterations)
    {
        String uuid = mongoCredentialHandler.retrieveUserId(testEmails.get(0));
        long mongoDbStart = System.nanoTime();

        for(int i = 0; i < numberOfIterations; i++)
        {
            mongoPostsHandler.saveNewPost(uuid, String.format(testPost, i));
        }

        long mongoDbStop = System.nanoTime();
        long timeElapsed = mongoDbStop - mongoDbStart;
        return "MONGO CREATE POSTS: " + TimeUnit.NANOSECONDS.toMillis(timeElapsed) + "\n";
    }

    private String generateSqlPosts(int numberOfIterations)
    {
        String uuid = sqlCredentialHandler.retrieveUserId(testEmails.get(0));
        long sqlDbStart = System.nanoTime();

        for(int i = 0; i < numberOfIterations; i++)
        {
            sqlPostsHandler.saveNewPost(uuid, String.format(testPost, i));
        }

        long sqlDbStop = System.nanoTime();
        long timeElapsed = sqlDbStop - sqlDbStart;
        return "SQL CREATE POSTS: " + TimeUnit.NANOSECONDS.toMillis(timeElapsed) + "\n";
    }

    private String readMongoDbPosts()
    {
        //this will allow the tests to be performed in a distributed manner
        String uuid = mongoCredentialHandler.retrieveUserId(testEmails.get(0));
        long mongoDbStart = System.nanoTime();
        mongoPostsHandler.readPosts(uuid);
        long mongoDbStop = System.nanoTime();
        long timeElapsed = mongoDbStop - mongoDbStart;
        return "MONGO READ POSTS: " + TimeUnit.NANOSECONDS.toMillis(timeElapsed) + "\n";
    }

    private String readSqlDbPosts()
    {
        //this will allow the tests to be performed in a distributed manner
        String uuid = sqlCredentialHandler.retrieveUserId(testEmails.get(0));
        long sqlDbStart = System.nanoTime();
        sqlPostsHandler.readPosts(uuid);
        long sqlDbStop = System.nanoTime();
        long timeElapsed = sqlDbStop - sqlDbStart;
        return "SQL READ POSTS: " + TimeUnit.NANOSECONDS.toMillis(timeElapsed) + "\n";
    }

    private String updateMongoDbPasswords()
    {
        long mongoDbStart = System.nanoTime();
        mongoAuthenticationHandler.updateUpdateUserAuthentication(testPassword2);
        long mongoDbStop = System.nanoTime();
        long timeElapsed = mongoDbStop - mongoDbStart;
        return "MONGO UPDATE AUTHENTICATION: " + TimeUnit.NANOSECONDS.toMillis(timeElapsed) + "\n";
    }

    private String updateSqlDbPasswords()
    {
        long sqlDbStart = System.nanoTime();
        sqlAuthenticationHandler.updateUpdateUserAuthentication(testPassword2);
        long sqlDbStop = System.nanoTime();
        long timeElapsed = sqlDbStop - sqlDbStart;
        return "SQL UPDATE AUTHENTICATION: " + TimeUnit.NANOSECONDS.toMillis(timeElapsed) + "\n";
    }

    private String deleteMongoDbPosts()
    {
        //this will allow the tests to be performed in a distributed manner
        String uuid = mongoCredentialHandler.retrieveUserId(testEmails.get(0));
        long mongoDbStart = System.nanoTime();
        mongoPostsHandler.deletePosts(uuid);
        long mongoDbStop = System.nanoTime();
        long timeElapsed = mongoDbStop - mongoDbStart;
        return "MONGO DELETE POSTS: " + TimeUnit.NANOSECONDS.toMillis(timeElapsed) + "\n";
    }

    private String deleteSqlDbPosts()
    {
        //this will allow the tests to be performed in a distributed manner
        String uuid = sqlCredentialHandler.retrieveUserId(testEmails.get(0));
        long sqlDbStart = System.nanoTime();
        sqlPostsHandler.deletePosts(uuid);
        long sqlDbStop = System.nanoTime();
        long timeElapsed = sqlDbStop - sqlDbStart;
        return "SQL DELETE POSTS: " + TimeUnit.NANOSECONDS.toMillis(timeElapsed) + "\n";
    }

    private String deleteMongoDbTestUsers(int numberOfIterations)
    {
        long mongoDbStart = System.nanoTime();

        for(int i = 0; i < numberOfIterations; i++)
        {
            //mongodb
            //retrieve user id
            String userId = mongoCredentialHandler.retrieveUserId(testEmails.get(i));
            //delete authentication entry
            mongoAuthenticationHandler.deleteUserAuthentication(userId);
            //delete credentials entry
            mongoCredentialHandler.deleteUserCredentials(testEmails.get(i));
        }

        long mongoDbStop = System.nanoTime();
        long timeElapsed = mongoDbStop - mongoDbStart;
        return "MONGO DELETE USERS: " + TimeUnit.NANOSECONDS.toMillis(timeElapsed) + "\n";
    }

    private String deleteSqlDbTestUsers(int numberOfIterations)
    {
        long sqlDbStart = System.nanoTime();

        for(int i = 0; i < numberOfIterations; i++)
        {
            //sql db
            //retrieve user id
            String userId = sqlCredentialHandler.retrieveUserId(testEmails.get(i));
            //delete authentication entry
            sqlAuthenticationHandler.deleteUserAuthentication(userId);
            //delete credentials entry
            sqlCredentialHandler.deleteUserCredentials(testEmails.get(i));
        }

        long sqlDbStop = System.nanoTime();
        long timeElapsed = sqlDbStop - sqlDbStart;
        return "SQL DELETE USERS: " + TimeUnit.NANOSECONDS.toMillis(timeElapsed) + "\n";
    }

    private void loadTestEmails(String testEmailsFileName, int testDataOffset, int testDataAmount) throws IOException
    {
        List<String> testEmailList = new ArrayList<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(new ClassPathResource(testEmailsFileName).getInputStream()));

        // Read from file
        String line = br.readLine();

        while (line != null) {
            line = br.readLine();
            testEmailList.add(line);
        }

        testEmails.addAll(testEmailList.subList(testDataOffset, testDataOffset + testDataAmount));
    }

    //create results file
    private void writeResultsFile(String results, int numberOfIterations)
    {
        try
        {
            String fileName = testDataOffset + "_" + testDataAmount + "_" + numberOfIterations + "_results.txt";
            PrintWriter pw = new PrintWriter(fileName);
            pw.print(results);
            pw.flush();
            pw.close();
        }
        catch (Exception e)
        {
            log.error("Failed to generate results file, ", e);
        }
    }
}
