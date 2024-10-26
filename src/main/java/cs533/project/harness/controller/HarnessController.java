package cs533.project.harness.controller;

import cs533.project.harness.data.mongo.MongoCredentialHandler;
import cs533.project.harness.data.sql.SqlCredentialHandler;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HarnessController
{
    private final MongoCredentialHandler mongoCredentialHandler;
    private final SqlCredentialHandler sqlCredentialHandler;
    private final StringEncryptor stringEncryptor;

    public HarnessController(final MongoCredentialHandler mongoCredentialHandler,
                             final SqlCredentialHandler sqlCredentialHandler,
                             final StringEncryptor stringEncryptor)
    {
        this.mongoCredentialHandler = mongoCredentialHandler;
        this.sqlCredentialHandler = sqlCredentialHandler;
        this.stringEncryptor = stringEncryptor;
    }

    @GetMapping("/test/user")
    public ResponseEntity<String> createTestUser()
    {
        String email = "test@gmail.com";
        boolean success = mongoCredentialHandler.saveUserCredentials(email);
        success = sqlCredentialHandler.saveUserCredentials(email);

        if(success)
        {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else
        {
            return new ResponseEntity<>(String.format("Email %s already exists", email), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/encrypt/{password}")
    public ResponseEntity<String> encryptPassword(@PathVariable String password)
    {
        return new ResponseEntity<>(stringEncryptor.encrypt(password), HttpStatus.OK);
    }
}
