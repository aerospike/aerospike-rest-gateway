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
import com.aerospike.client.AerospikeException;
import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.client.query.IndexType;
import com.aerospike.restclient.domain.RestClientIndex;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SindexTestsError {

    String endpoint = "/v1/index";
    /* List of index [ns, name] pairs to delete after each test */ List<String[]> createdIndexPairs;

    RestClientIndex createdRCIndex;
    String testNS = "test";
    String testIndexSet = "idxDemo";
    String testIndexName = "junitIndex";
    String testIndexBin = "junitIdxBin";
    IndexType testIndexType = IndexType.NUMERIC;
    IndexCollectionType testIndexCollectionType = IndexCollectionType.DEFAULT;

    TypeReference<List<RestClientIndex>> listIndexType = new TypeReference<List<RestClientIndex>>() {
    };
    TypeReference<Map<String, String>> mapStringStringType = new TypeReference<Map<String, String>>() {
    };

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMVC;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private AerospikeClient client;

    /* create and load an index*/
    @Before
    public void setup() throws InterruptedException {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        createdIndexPairs = new ArrayList<String[]>();
    }

    @After
    public void tearDown() {
        for (String[] idxPair : createdIndexPairs) {
            ASTestUtils.ensureDeletion(client, idxPair[0], idxPair[1]);
        }
    }

    @Test
    public void createIndexThatAlreadyExists() throws Exception {
        addIndexForTest("postTestIdx", "postTestBin");
        RestClientIndex postTestIdx = new RestClientIndex();
        postTestIdx.setNamespace(testNS);
        postTestIdx.setSet(testIndexSet);
        postTestIdx.setBin("postTestBin");
        postTestIdx.setName("postTestIdx");
        postTestIdx.setIndexType(testIndexType);
        postTestIdx.setCollectionType(testIndexCollectionType);

        String indexPayload = objectMapper.writeValueAsString(postTestIdx);
        mockMVC.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(indexPayload))
                .andExpect(status().isAccepted()); // Server changed in 6.1 to return 'ok'
    }

    @Test
    public void createIndexOnNonExistentNamespace() throws Exception {
        RestClientIndex postTestIdx = new RestClientIndex();
        postTestIdx.setNamespace("notrealns");
        postTestIdx.setSet(testIndexSet);
        postTestIdx.setBin("postTestBin");
        postTestIdx.setName("postTestIdx");
        postTestIdx.setIndexType(testIndexType);
        postTestIdx.setCollectionType(testIndexCollectionType);

        String indexPayload = objectMapper.writeValueAsString(postTestIdx);
        mockMVC.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(indexPayload))
                .andExpect(status().isAccepted()); // Server changed in 6.1 to return 'ok'
    }

    @Test
    public void getIndexesForNonExistentNS() throws Exception {
        mockMVC.perform(get(endpoint + "/NotARealNS")).andExpect(status().isNotFound());
    }

    @Test
    public void createIndexInvalidType() throws Exception {
        Map<String, String> idxMap = new HashMap<String, String>();
        idxMap.put("type", "notreal");
        idxMap.put("namespace", testNS);
        idxMap.put("bin", testIndexBin);
        idxMap.put("name", "cool_index");

        String indexPayload = objectMapper.writeValueAsString(idxMap);
        mockMVC.perform(post(endpoint).contentType(MediaType.APPLICATION_JSON).content(indexPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getIndexStatsForNonExistentIndex() throws Exception {
        mockMVC.perform(get(endpoint + "/" + testNS + "/" + "NOTREALIDX")).andExpect(status().isNotFound());
    }

    @Test
    public void deleteIndexThatDoesNotExist() throws Exception {
        mockMVC.perform(delete(endpoint + "/" + testNS + "/" + "NOTREALIDX"))
                .andExpect(status().isAccepted()); // Server changed in 6.1 to return 'ok'
    }

    private void addIndexForTest(String indexName, String binName) {
        testIndexName = indexName;
        createdRCIndex = new RestClientIndex();
        createdRCIndex.setNamespace(testNS);
        createdRCIndex.setSet(testIndexSet);
        createdRCIndex.setBin(binName);
        createdRCIndex.setName(indexName);
        createdRCIndex.setIndexType(testIndexType);
        createdRCIndex.setCollectionType(testIndexCollectionType);

        try {
            client.createIndex(null, testNS, testIndexSet, indexName, binName, testIndexType, testIndexCollectionType)
                    .waitTillComplete(500, 5000);
        } catch (AerospikeException e) {
        }
        // Wait for the index to exist.
        ASTestUtils.indexExists(client, testNS, indexName);

        createdIndexPairs.add(new String[]{testNS, indexName});
    }

}