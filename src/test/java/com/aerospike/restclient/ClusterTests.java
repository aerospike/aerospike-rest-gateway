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

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ClusterTests {

    @Autowired
    ObjectMapper mapper;

    public static TypeReference<Map<String, Object>> clusterInfoType = new TypeReference<Map<String, Object>>() {
    };

    private MockMvc mockMVC;

    @Autowired
    private WebApplicationContext wac;

    private final String endpoint = "/v1/cluster";

    @Before
    public void setup() throws InterruptedException {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    @Test
    public void testClusterWithJSON() throws Exception {
        /* Get all users and verify that the one we just created is included*/

        String response = mockMVC.perform(get(endpoint).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        Map<String, Object> clusterInfo = mapper.readValue(response, clusterInfoType);

        Assert.assertTrue(clusterInfo.containsKey("nodes"));
        Assert.assertTrue(clusterInfo.containsKey("namespaces"));

    }

    @Test
    public void testClusterWithMsgPack() throws Exception {
        /* Get all users and verify that the one we just created is included*/

        ObjectMapper msgpackmapper = new ObjectMapper(new MessagePackFactory());
        byte[] response = mockMVC.perform(get(endpoint).accept(new MediaType("application", "msgpack")))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        Map<String, Object> clusterInfo = msgpackmapper.readValue(response, clusterInfoType);

        Assert.assertTrue(clusterInfo.containsKey("nodes"));
        Assert.assertTrue(clusterInfo.containsKey("namespaces"));

    }

}