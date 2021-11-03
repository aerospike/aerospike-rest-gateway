/*
 * Copyright 2020 Aerospike, Inc.
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
import com.aerospike.restclient.domain.RestClientRecord;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

public class AuthenticationTest {

    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private final String namespace = "test";
    private final String setName = "auth";
    private final String key = "authRecord";

    private final String testEndpoint = "/v1/kvs/" + namespace + "/" + setName + "/" + key;

    private final Key recordKey = new Key(namespace, setName, key);

    private final String invalidAuthHeader = "Basic dXNlcjE6MTIzNA==";
    private final String validAuthHeader = "Basic Og==";

    @BeforeEach
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        Bin bin = new Bin("binAuth", 1);
        client.add(null, recordKey, bin);
    }

    @AfterEach
    public void clean() {
        client.delete(null, recordKey);
    }

    @Test
    public void testDefaultAuthentication() throws Exception {
        mockMVC.perform(get(testEndpoint))
                .andExpect(status().isOk());
    }

    @Test
    public void testValidUserAuthentication() throws Exception {
        assumeFalse(ClusterUtils.isSecurityEnabled(client));

        MockHttpServletResponse response = mockMVC.perform(get(testEndpoint)
                .header("Authorization", validAuthHeader))
                .andExpect(status().isOk()).andReturn().getResponse();

        RestClientRecord record = new JSONResponseDeserializer().getResponse(response, RestClientRecord.class);
        assertEquals((int) record.bins.get("binAuth"), 1);
    }

    @Test
    public void testInvalidUserAuthentication() throws Exception {
        assumeTrue(ClusterUtils.isSecurityEnabled(client));

        mockMVC.perform(get(testEndpoint)
                .header("Authorization", invalidAuthHeader))
                .andExpect(status().isInternalServerError());
    }
}
