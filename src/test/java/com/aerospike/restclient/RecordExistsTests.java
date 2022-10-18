/*
 * Copyright 2022 Aerospike, Inc.
 *
 * Portions may be licensed to Aerospike, Inc. under one or more contributor
 * license agreements WHICH ARE COMPATIBLE WITH THE APACHE LICENSE, VERSION 2.0.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.aerospike.restclient;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.head;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/*
 * These Tests are simple tests which store a simple value via the Java Client, then retrieve it via REST
 * The expected and returned values are compared.
 */
@RunWith(Parameterized.class)
@SpringBootTest
public class RecordExistsTests {

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    private MockMvc mockMVC;

    private final Key testKey;
    private final Key intKey;
    private final Key bytesKey;

    private List<Key> keysToRemove;

    // Endpoint to receive all requests
    private final String testEndpoint;
    private final String intEndpoint;
    private final String digestEndpoint;
    private final String bytesEndpoint;
    private final String nonExistentEndpoint;

    @Parameters
    public static Object[] getParams() {
        return new Object[]{true, false};
    }

    public RecordExistsTests(boolean useSet) {
        if (useSet) {
            testKey = new Key("test", "junit", "getput");
            intKey = new Key("test", "junit", 1);
            bytesKey = new Key("test", "junit", new byte[]{1, 127, 127, 1});

            testEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "junit", "getput");
            nonExistentEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "junit", "notreal");

            String keyDigest = Base64.getUrlEncoder().encodeToString(testKey.digest);
            digestEndpoint = ASTestUtils.buildEndpointV1("kvs", this.testKey.namespace, this.testKey.setName,
                    keyDigest) + "?keytype=DIGEST";

            intEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "junit", "1") + "?keytype=INTEGER";

            String urlBytes = Base64.getUrlEncoder().encodeToString((byte[]) bytesKey.userKey.getObject());
            bytesEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "junit", urlBytes) + "?keytype=BYTES";
        } else {
            testKey = new Key("test", null, "getput");
            intKey = new Key("test", null, 1);
            bytesKey = new Key("test", null, new byte[]{1, 127, 127, 1});

            testEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "getput");
            nonExistentEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "notreal");

            String keyDigest = Base64.getUrlEncoder().encodeToString(testKey.digest);
            digestEndpoint = ASTestUtils.buildEndpointV1("kvs", this.testKey.namespace, keyDigest) + "?keytype=DIGEST";

            intEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "1") + "?keytype=INTEGER";

            String urlBytes = Base64.getUrlEncoder().encodeToString((byte[]) bytesKey.userKey.getObject());
            bytesEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", urlBytes) + "?keytype=BYTES";
        }
    }

    @Before
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        keysToRemove = new ArrayList<>();
        client.put(null, testKey, new Bin("a", "b"));
    }

    @After
    public void clean() {
        client.delete(null, this.testKey);
        for (Key key : keysToRemove) {
            client.delete(null, key);
        }
    }

    @Test
    public void testStringKeyExists() throws Exception {
        mockMVC.perform(head(testEndpoint)).andExpect(status().isOk());
    }

    @Test
    public void testWithNonExistentRecord() throws Exception {
        mockMVC.perform(head(nonExistentEndpoint)).andExpect(status().isNotFound());
    }

    /*
     * Test to ensure that a user can specify that the userKey is an integer by appending
     * the correct query parameter to a get request.
     */
    @Test
    public void TestIntegerKey() throws Exception {

        keysToRemove.add(intKey);

        Bin idBin = new Bin("id", "integer");
        client.put(null, intKey, idBin);

        mockMVC.perform(head(intEndpoint)).andExpect(status().isOk());
    }

    @Test
    public void ExistsWithDigest() throws Exception {

        Bin intBin = new Bin("integer", 10);
        client.put(null, this.testKey, intBin);

        mockMVC.perform(head(digestEndpoint)).andExpect(status().isOk());
    }

    @Test
    public void TestBytesKey() throws Exception {
        keysToRemove.add(bytesKey);
        Bin idBin = new Bin("id", "bytes");
        client.put(null, bytesKey, idBin);

        mockMVC.perform(head(bytesEndpoint)).andExpect(status().isOk());
    }

}
