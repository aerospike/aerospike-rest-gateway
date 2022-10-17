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
import com.aerospike.restclient.domain.executemodels.RestClientExecuteTask;
import com.aerospike.restclient.domain.executemodels.RestClientExecuteTaskStatus;
import com.aerospike.restclient.util.AerospikeOperation;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.aerospike.restclient.util.AerospikeAPIConstants.OPERATION_FIELD;
import static com.aerospike.restclient.util.AerospikeAPIConstants.OPERATION_VALUES_FIELD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Parameterized.class)
@SpringBootTest
public class ExecuteV1Tests {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private String setName;
    private final String namespace;
    private final String currentMediaType;

    private static final int numberOfRecords = 10;

    private final Key[] testKeys;
    private final String testEndpoint;
    private final String queryStatusEndpoint;

    @Before
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        for (int i = 0; i < testKeys.length; i++) {
            Bin bin = new Bin("binInt", i);
            client.add(null, testKeys[i], bin);
        }
    }

    @After
    public void clean() {
        for (Key testKey : testKeys) {
            client.delete(null, testKey);
        }
    }

    private final ObjectMapper objectMapper;
    private final ResponseDeserializer responseDeserializer;

    @Parameterized.Parameters
    public static Object[][] getParams() {
        return new Object[][]{
                {new ObjectMapper(), new JSONResponseDeserializer(), MediaType.APPLICATION_JSON.toString(), true}, {
                new ObjectMapper(new MessagePackFactory()),
                new MsgPackResponseDeserializer(),
                "application/msgpack",
                true
        }, {new ObjectMapper(), new JSONResponseDeserializer(), MediaType.APPLICATION_JSON.toString(), false}, {
                        new ObjectMapper(new MessagePackFactory()),
                        new MsgPackResponseDeserializer(),
                        "application/msgpack",
                        false
                }
        };
    }

    public ExecuteV1Tests(ObjectMapper mapper, ResponseDeserializer deserializer, String mt, boolean useSet) {
        objectMapper = mapper;
        responseDeserializer = deserializer;
        currentMediaType = mt;
        namespace = "test";
        if (useSet) {
            setName = "executeSet";
            testEndpoint = "/v1/execute/scan/" + namespace + "/" + setName;
        } else {
            testEndpoint = "/v1/execute/scan/" + namespace;
        }
        testKeys = setKeys();
        queryStatusEndpoint = "/v1/execute/scan/status/";
    }

    private Key[] setKeys() {
        Key[] keys = new Key[numberOfRecords];
        for (int i = 0; i < numberOfRecords; i++) {
            keys[i] = new Key(namespace, setName, "exec_" + i);
        }
        return keys;
    }

    @Test
    public void testExecuteScan() throws Exception {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        opValues.put("bin", "binInt");
        opValues.put("incr", 1);
        opMap.put(OPERATION_FIELD, AerospikeOperation.ADD);
        opMap.put(OPERATION_VALUES_FIELD, opValues);
        opList.add(opMap);

        byte[] payload = objectMapper.writeValueAsBytes(opList);
        MockHttpServletResponse response = mockMVC.perform(
                        post(testEndpoint).contentType(currentMediaType).content(payload).accept(currentMediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        RestClientExecuteTask task = responseDeserializer.getResponse(response, RestClientExecuteTask.class);

        Thread.sleep(200);
        String endpoint = queryStatusEndpoint + task.getTaskId();
        MockHttpServletResponse statusResponse = mockMVC.perform(get(endpoint).accept(currentMediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse();

        RestClientExecuteTaskStatus status = responseDeserializer.getResponse(statusResponse,
                RestClientExecuteTaskStatus.class);

        Assert.assertEquals("COMPLETE", status.getStatus());

        for (int i = 0; i < numberOfRecords; i++) {
            long binValue = (long) client.get(null, testKeys[i]).bins.get("binInt");
            Assert.assertEquals(binValue, i + 1);
        }
    }
}

