package com.aerospike.restclient;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = "aerospike.restclient.requireAuthentication=true")
public class ConfigRequireAuthTest {
    @Test
    public void startUpTest() {
        // Tests that the application will startup even without an default AerospikeClient instantiated.
    }
}
