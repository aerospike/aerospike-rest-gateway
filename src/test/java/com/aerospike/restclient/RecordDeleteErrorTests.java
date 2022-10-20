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
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Parameterized.class)
@SpringBootTest
public class RecordDeleteErrorTests {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMVC;
    @Autowired
    private WebApplicationContext wac;

    @Before
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
    }

    private String nonExistentNSEndpoint = null;
    private String nonExistentRecordEndpoint = null;

    @Parameters
    public static Object[] getParams() {
        return new Object[]{true, false};
    }

    public RecordDeleteErrorTests(boolean useSet) {
        if (useSet) {
            nonExistentNSEndpoint = ASTestUtils.buildEndpointV1("kvs", "fakeNS", "demo", "1");
            nonExistentRecordEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "demo",
                    "thisisnotarealkeyforarecord");
        } else {
            nonExistentNSEndpoint = ASTestUtils.buildEndpointV1("kvs", "fakeNS", "1");
            nonExistentRecordEndpoint = ASTestUtils.buildEndpointV1("kvs", "test", "thisisnotarealkeyforarecord");
        }
    }

    @Test
    public void DeleteFromNonExistentNS() throws Exception {

        MvcResult result = mockMVC.perform(delete(nonExistentNSEndpoint).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        MockHttpServletResponse res = result.getResponse();
        String resJson = res.getContentAsString();
        TypeReference<Map<String, Object>> sOMapType = new TypeReference<Map<String, Object>>() {
        };
        Map<String, Object> resObject = objectMapper.readValue(resJson, sOMapType);

        Assert.assertFalse((boolean) resObject.get("inDoubt"));
    }

    @Test
    public void DeleteNonExistentRecord() throws Exception {

        MvcResult result = mockMVC.perform(delete(nonExistentRecordEndpoint).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();

        MockHttpServletResponse res = result.getResponse();
        String resJson = res.getContentAsString();
        TypeReference<Map<String, Object>> sOMapType = new TypeReference<Map<String, Object>>() {
        };
        Map<String, Object> resObject = objectMapper.readValue(resJson, sOMapType);

        Assert.assertFalse((boolean) resObject.get("inDoubt"));
    }

}