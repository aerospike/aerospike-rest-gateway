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
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.restclient.domain.RestClientKeyRecord;
import com.aerospike.restclient.domain.scanmodels.RestClientScanResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Parameterized.class)
@SpringBootTest
public class ScanTest {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private static final int numberOfRecords = 101;
    private final Key[] testKeys;
    private String testEndpoint;

    private final String setName;
    private final String namespace;
    private final String currentMediaType;

    @Before
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        WritePolicy writePolicy = new WritePolicy();
        writePolicy.sendKey = true;
        for (int i = 0; i < numberOfRecords; i++) {
            Bin bin = new Bin("binInt", i);
            client.add(writePolicy, testKeys[i], bin);
        }
    }

    @After
    public void clean() {
        for (int i = 0; i < numberOfRecords; i++) {
            client.delete(null, testKeys[i]);
        }
    }

    private final ResponseDeserializer responseDeserializer;

    @Parameterized.Parameters
    public static Object[][] getParams() {
        return new Object[][]{
                {new JSONResponseDeserializer(), MediaType.APPLICATION_JSON.toString()},
                {new MsgPackResponseDeserializer(), "application/msgpack"}
        };
    }

    public ScanTest(ResponseDeserializer deserializer, String mt) {
        this.responseDeserializer = deserializer;
        this.namespace = "test";
        this.currentMediaType = mt;
        this.testKeys = new Key[numberOfRecords];
        this.setName = "scanSet";
        testEndpoint = "/v1/scan/" + namespace + "/" + setName;

        for (int i = 0; i < numberOfRecords; i++) {
            testKeys[i] = new Key(namespace, setName, "key_" + i);
        }
    }

    @Test
    public void testScanAll() throws Exception {
        MockHttpServletResponse response = mockMVC.perform(get(testEndpoint).accept(currentMediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        RestClientScanResponse res = responseDeserializer.getResponse(response, RestClientScanResponse.class);
        Assert.assertEquals(res.getPagination().getTotalRecords(), numberOfRecords);
    }

    @Test
    public void testScanPaginated() throws Exception {
        int pageSize = 10;
        int scanRequests = 0;
        int total = 0;
        RestClientScanResponse res = null;
        Set<Integer> binValues = new HashSet<>();
        testEndpoint = testEndpoint + "?maxRecords=" + pageSize;
        String endpoint = testEndpoint;
        while (total < numberOfRecords) {
            MockHttpServletResponse response = mockMVC.perform(get(endpoint).accept(currentMediaType))
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse();

            res = responseDeserializer.getResponse(response, RestClientScanResponse.class);
            for (RestClientKeyRecord r : res.getRecords()) {
                binValues.add((int) r.bins.get("binInt"));
            }
            total += res.getPagination().getTotalRecords();
            scanRequests++;
            System.out.println(res.getPagination().getNextToken());
            endpoint = testEndpoint + "&from=" + res.getPagination().getNextToken();
        }

        Assert.assertEquals(total, numberOfRecords);
        Assert.assertEquals(binValues.size(), numberOfRecords);
        Assert.assertEquals(scanRequests, numberOfRecords / pageSize + 1);
        Assert.assertNull(res.getPagination().getNextToken());
    }
}

interface ResponseDeserializer {
    <T> T getResponse(MockHttpServletResponse res, Class<T> clazz);
}

class MsgPackResponseDeserializer implements ResponseDeserializer {

    ObjectMapper msgPackMapper;

    public MsgPackResponseDeserializer() {
        msgPackMapper = new ObjectMapper(new MessagePackFactory());
    }

    @Override
    public <T> T getResponse(MockHttpServletResponse res, Class<T> clazz) {
        T response = null;
        try {
            byte[] responseAsByteArray = res.getContentAsByteArray();
            response = msgPackMapper.readValue(responseAsByteArray, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}

class JSONResponseDeserializer implements ResponseDeserializer {

    ObjectMapper mapper;

    public JSONResponseDeserializer() {
        this.mapper = new ObjectMapper();
    }

    @Override
    public <T> T getResponse(MockHttpServletResponse res, Class<T> clazz) {
        T response = null;
        try {
            String responseAsString = res.getContentAsString();
            response = mapper.readValue(responseAsString, clazz);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
}
