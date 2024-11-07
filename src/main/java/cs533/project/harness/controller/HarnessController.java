package cs533.project.harness.controller;

import cs533.project.harness.service.TestDataGenerator;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HarnessController
{
    private final TestDataGenerator testDataGenerator;
    private final StringEncryptor stringEncryptor;

    public HarnessController(final TestDataGenerator testDataGenerator,
                             final StringEncryptor stringEncryptor)
    {
        this.testDataGenerator = testDataGenerator;
        this.stringEncryptor = stringEncryptor;
    }

    @GetMapping("/run/iterations/{numberOfIterations}")
    public ResponseEntity<String> runNumberOfIterations(@PathVariable int numberOfIterations)
    {
        testDataGenerator.runNumberOfIterations(numberOfIterations);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/run/iterations/all")
    public ResponseEntity<String> runAllIterations()
    {
        int[] iterationsList = new int[]{1, 100, 1000, 10000, 100000};

        for(int numberOfIterations : iterationsList)
        {
            testDataGenerator.runNumberOfIterations(numberOfIterations);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/run/iterations/distributed")
    public ResponseEntity<String> runDistributedIterations()
    {
        int[] iterationsList = new int[]{1000, 10000};

        for(int numberOfIterations : iterationsList)
        {
            testDataGenerator.runNumberOfIterations(numberOfIterations);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/encrypt/{password}")
    public ResponseEntity<String> encryptPassword(@PathVariable String password)
    {
        return new ResponseEntity<>(stringEncryptor.encrypt(password), HttpStatus.OK);
    }
}
