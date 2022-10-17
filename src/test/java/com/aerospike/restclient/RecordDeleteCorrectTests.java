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
import com.aerospike.client.Record;
import com.aerospike.client.Value.BytesValue;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Base64;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Parameterized.class)
@SpringBootTest
public class RecordDeleteCorrectTests {

    private MockMvc mockMVC;

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();
    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private String intEndpoint = null;
    private String bytesEndpoint = null;
    private String digestEndpoint = null;

    private Key testKey = new Key("test", "junit", "getput");
    private Key intKey = new Key("test", "junit", 1);
    private Key bytesKey = new Key("test", "junit", new byte[]{1, 127, 127, 1});
    private String testEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "junit", "getput");

    @Parameters
    public static Object[] getParams() {
        return new Object[]{true, false};
    }

    public RecordDeleteCorrectTests(boolean useSet) {
        if (useSet) {
            testKey = new Key("test", "junit", "getput");
            intKey = new Key("test", "junit", 1);
            bytesKey = new Key("test", "junit", new byte[]{1, 127, 127, 1});

            testEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "junit", "getput");

            intEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "junit", "1") + "?keytype=INTEGER";

            String urlBytes = Base64.getUrlEncoder()
                    .encodeToString((byte[]) ((BytesValue) bytesKey.userKey).getObject());
            bytesEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "junit", urlBytes) + "?keytype=BYTES";

            String digestBytes = Base64.getUrlEncoder().encodeToString(testKey.digest);
            digestEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "junit", digestBytes) + "?keytype=DIGEST";

        } else {
            testKey = new Key("test", null, "getput");
            intKey = new Key("test", null, 1);
            bytesKey = new Key("test", null, new byte[]{1, 127, 127, 1});

            testEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "getput");

            intEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "1") + "?keytype=INTEGER";

            String urlBytes = Base64.getUrlEncoder()
                    .encodeToString((byte[]) ((BytesValue) bytesKey.userKey).getObject());
            bytesEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", urlBytes) + "?keytype=BYTES";

            String digestBytes = Base64.getUrlEncoder().encodeToString(testKey.digest);
            digestEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", digestBytes) + "?keytype=DIGEST";
        }
    }

    @Before
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        Bin baseBin = new Bin("initial", "bin");
        client.put(null, testKey, baseBin);
        client.put(null, intKey, baseBin);
        client.put(null, bytesKey, baseBin);
    }

    @After
    public void clean() {
        client.delete(null, testKey);
        client.delete(null, intKey);
        client.delete(null, bytesKey);

    }

    @Test
    public void DeleteRecord() throws Exception {
        mockMVC.perform(delete(testEndpoint).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        Record record = client.get(null, this.testKey);
        Assert.assertNull(record);
    }

    @Test
    public void DeleteRecordWithIntKey() throws Exception {
        mockMVC.perform(delete(intEndpoint).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        Record record = client.get(null, this.intKey);
        Assert.assertNull(record);
    }

    @Test
    public void DeleteRecordWithBytesKey() throws Exception {
        mockMVC.perform(delete(bytesEndpoint).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Record record = client.get(null, this.bytesKey);
        Assert.assertNull(record);
    }

    @Test
    public void DeleteRecordWithDigestKey() throws Exception {
        mockMVC.perform(delete(digestEndpoint).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        Record record = client.get(null, this.testKey);
        Assert.assertNull(record);
    }

    @Test
    public void DeleteRecordWithGenerationMismatch() throws Exception {
        String extraParams = "?generation=150&generationPolicy=EXPECT_GEN_EQUAL";
        mockMVC.perform(delete(testEndpoint + extraParams).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());

        Record record = client.get(null, this.testKey);
        Assert.assertNotNull(record);
    }
}
