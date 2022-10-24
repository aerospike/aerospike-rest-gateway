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
import com.aerospike.client.Info;
import com.aerospike.client.query.IndexCollectionType;
import com.aerospike.client.query.IndexType;
import com.aerospike.restclient.domain.RestClientIndex;
import com.aerospike.restclient.util.InfoResponseParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Parameterized.class)
@SpringBootTest
public class SindexTestsCorrect {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    private final RestIndexHandler handler;

    @Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JSONIndexHandler(), new MsgPackIndexHandler()
        };
    }

    public SindexTestsCorrect(RestIndexHandler handler) {
        this.handler = handler;
    }

    String endpoint = "/v1/index";
    /* List of index [ns, name] pairs to delete after each test */ List<String[]> createdIndexPairs;

    RestClientIndex createdRCIndex;
    String testNS = "test";
    String testIndexSet = "idxDemo";
    String testIndexName = "junitIndex";
    String testIndexBin = "junitIdxBin";
    IndexType testIndexType = IndexType.NUMERIC;
    IndexCollectionType testIndexCollectionType = IndexCollectionType.DEFAULT;

    public static TypeReference<List<RestClientIndex>> listIndexType = new TypeReference<List<RestClientIndex>>() {
    };
    public static TypeReference<Map<String, String>> mapStringStringType = new TypeReference<Map<String, String>>() {
    };

    private MockMvc mockMVC;

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private AerospikeClient client;

    /* create and load an index*/
    @Before
    public void setup() throws InterruptedException {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        createdIndexPairs = new ArrayList<>();
    }

    @After
    public void tearDown() {
        for (String[] idxPair : createdIndexPairs) {
            ASTestUtils.ensureDeletion(client, idxPair[0], idxPair[1]);
        }
    }

    @Test
    public void createIndex() throws Exception {
        RestClientIndex postTestIdx = new RestClientIndex();
        postTestIdx.setNamespace(testNS);
        postTestIdx.setSet(testIndexSet);
        postTestIdx.setBin("postTestBinC");
        postTestIdx.setName("postTestIdxC");
        postTestIdx.setIndexType(IndexType.GEO2DSPHERE);
        postTestIdx.setCollectionType(IndexCollectionType.MAPKEYS);
        createdIndexPairs.add(new String[]{testNS, "postTestIdxC"});

        handler.createIndex(mockMVC, endpoint, postTestIdx);

        ASTestUtils.indexExists(client, testNS, "postTestIdx");

        String idxInfos = Info.request(null, client.getNodes()[0], "sindex/" + testNS);
        List<Map<String, String>> idxInfoMaps = InfoResponseParser.getIndexInformation(idxInfos);
        List<RestClientIndex> existingIndexes = idxInfoMaps.stream().map(RestClientIndex::new).toList();

        Assert.assertTrue(indexesContain(existingIndexes, postTestIdx));
    }

    @Test
    public void createIndexWithoutCollectionType() throws Exception {
        RestClientIndex postTestIdx = new RestClientIndex();
        postTestIdx.setNamespace(testNS);
        postTestIdx.setSet(testIndexSet);
        postTestIdx.setBin("NoColIdx");
        postTestIdx.setName("NoColIdx");
        postTestIdx.setIndexType(IndexType.NUMERIC);
        createdIndexPairs.add(new String[]{testNS, "NoColIdx"});

        handler.createIndex(mockMVC, endpoint, postTestIdx);

        ASTestUtils.indexExists(client, testNS, "NoColIdx");

        String idxInfos = Info.request(null, client.getNodes()[0], "sindex/" + testNS);
        List<Map<String, String>> idxInfoMaps = InfoResponseParser.getIndexInformation(idxInfos);
        List<RestClientIndex> existingIndexes = idxInfoMaps.stream().map(RestClientIndex::new).toList();

        Assert.assertTrue(indexesContain(existingIndexes, postTestIdx));
    }

    @Test
    public void createIndexWithoutSet() throws Exception {
        RestClientIndex postTestIdx = new RestClientIndex();
        postTestIdx.setNamespace(testNS);
        postTestIdx.setBin("NoSetIdx");
        postTestIdx.setName("NoSetIdx");
        postTestIdx.setIndexType(IndexType.NUMERIC);
        createdIndexPairs.add(new String[]{testNS, "NoSetIdx"});

        handler.createIndex(mockMVC, endpoint, postTestIdx);

        ASTestUtils.indexExists(client, testNS, "NoSetIdx");

        String idxInfos = Info.request(null, client.getNodes()[0], "sindex/" + testNS);
        List<Map<String, String>> idxInfoMaps = InfoResponseParser.getIndexInformation(idxInfos);
        List<RestClientIndex> existingIndexes = idxInfoMaps.stream().map(RestClientIndex::new).toList();

        Assert.assertTrue(indexesContain(existingIndexes, postTestIdx));
    }

    @Test
    public void getIndexes() throws Exception {
        RestIndexHandler handler = new MsgPackIndexHandler();

        // Create an index specifically for this test
        // We then verify that it shows up when we get a list of indices.
        addIndexForTest("getIndexes", "getIndexes");

        List<RestClientIndex> indexList = handler.getIndexes(mockMVC, endpoint);

        Assert.assertTrue(indexesContain(indexList, createdRCIndex));
    }

    @Test
    public void getIndexStats() throws Exception {
        addIndexForTest("getIndexstat", "getIndexstat");

        Map<String, String> restIndexStatMap = handler.getIndexStats(mockMVC,
                endpoint + "/" + testNS + "/" + testIndexName);
        String indexStat = Info.request(null, client.getNodes()[0], "sindex/" + testNS + "/" + testIndexName);
        Map<String, String> indexStatMap = InfoResponseParser.getIndexStatInfo(indexStat);

        Assert.assertTrue(ASTestUtils.compareStringMap(restIndexStatMap, indexStatMap));
    }

    @Test
    public void testIndexStatsInvalidNS() throws Exception {
        addIndexForTest("getIndexstat", "getIndexstat");
        handler.testIndexNotFound(mockMVC, endpoint + "/" + "fakeNS" + "/" + testIndexName);
    }

    /* This is run with each handler, but it is not changed since no body or response is packed/unpacked */
    @Test
    public void deleteIndex() throws Exception {
        addIndexForTest("delIndex", "delIndex");
        mockMVC.perform(delete(endpoint + "/" + testNS + "/" + testIndexName)).andExpect(status().isAccepted());

        /* give some time for the index dropping to occur */
        Thread.sleep(5000);

        Assert.assertFalse(ASTestUtils.indexExists(client, testNS, testIndexName));
    }

    private boolean indexesContain(List<RestClientIndex> rcIndexes, RestClientIndex rcIndex) {
        for (RestClientIndex testIndex : rcIndexes) {
            if (indexMatches(testIndex, rcIndex)) {
                return true;
            }
        }
        return false;
    }

    private boolean indexMatches(RestClientIndex rcIndex1, RestClientIndex rcIndex2) {
        if (!rcIndex1.getName().equals(rcIndex2.getName())) {
            return false;
        }
        if (!rcIndex1.getNamespace().equals(rcIndex2.getNamespace())) {
            return false;
        }
        if (!rcIndex1.getBin().equals(rcIndex2.getBin())) {
            return false;
        }
        if (!rcIndex1.getCollectionType().equals(rcIndex2.getCollectionType())) {
            return false;
        }
        if (!rcIndex1.getIndexType().equals(rcIndex2.getIndexType())) {
            return false;
        }
        if (rcIndex1.getSet() == null) {
            return rcIndex2.getSet() == null;
        } else return rcIndex1.getSet().equals(rcIndex2.getSet());
    }

    /*
     * Create an index for a test. Used to test whether the REST Gateway finds it.
     */
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
        } catch (AerospikeException ignore) {
        }
        // Wait for the index to exist.
        ASTestUtils.indexExists(client, testNS, indexName);
        String[] idxPair = new String[]{testNS, indexName};
        createdIndexPairs.add(idxPair);
    }

}

/*
 *  MsgPack and non JSON handlers for index operations
 */
interface RestIndexHandler {
    Map<String, String> getIndexStats(MockMvc mockMVC, String endpoint) throws Exception;

    void testIndexNotFound(MockMvc mockMVC, String endpoint) throws Exception;

    void createIndex(MockMvc mockMVC, String endpoint, RestClientIndex idx) throws Exception;

    List<RestClientIndex> getIndexes(MockMvc mockMVC, String endpoint) throws Exception;
}

class MsgPackIndexHandler implements RestIndexHandler {

    private final MediaType mediaType = new MediaType("application", "msgpack");

    ObjectMapper mapper;

    public MsgPackIndexHandler() {
        mapper = new ObjectMapper(new MessagePackFactory());
    }

    @Override
    public Map<String, String> getIndexStats(MockMvc mockMVC, String endpoint) throws Exception {
        byte[] resBytes = mockMVC.perform(get(endpoint).accept(mediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        return mapper.readValue(resBytes, SindexTestsCorrect.mapStringStringType);
    }

    @Override
    public void testIndexNotFound(MockMvc mockMVC, String endpoint) throws Exception {
        mockMVC.perform(get(endpoint).accept(mediaType)).andExpect(status().isNotFound());
    }

    @Override
    public void createIndex(MockMvc mockMVC, String endpoint, RestClientIndex idx) throws Exception {
        byte[] indexPayload = mapper.writeValueAsBytes(idx);
        mockMVC.perform(post(endpoint).contentType(mediaType).content(indexPayload)).andExpect(status().isAccepted());
    }

    @Override
    public List<RestClientIndex> getIndexes(MockMvc mockMVC, String endpoint) throws Exception {
        byte[] resBytes = mockMVC.perform(get(endpoint).accept(mediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        return mapper.readValue(resBytes, SindexTestsCorrect.listIndexType);
    }
}

class JSONIndexHandler implements RestIndexHandler {

    ObjectMapper mapper;
    private final MediaType mediaType = MediaType.APPLICATION_JSON;

    public JSONIndexHandler() {
        mapper = new ObjectMapper();
    }

    @Override
    public Map<String, String> getIndexStats(MockMvc mockMVC, String endpoint) throws Exception {
        String resJson = mockMVC.perform(get(endpoint).accept(mediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return mapper.readValue(resJson, SindexTestsCorrect.mapStringStringType);
    }

    @Override
    public void testIndexNotFound(MockMvc mockMVC, String endpoint) throws Exception {
        mockMVC.perform(get(endpoint).accept(mediaType)).andExpect(status().isNotFound());
    }

    @Override
    public void createIndex(MockMvc mockMVC, String endpoint, RestClientIndex idx) throws Exception {
        String indexPayload = mapper.writeValueAsString(idx);
        mockMVC.perform(post(endpoint).contentType(mediaType).content(indexPayload)).andExpect(status().isAccepted());
    }

    @Override
    public List<RestClientIndex> getIndexes(MockMvc mockMVC, String endpoint) throws Exception {
        String resJson = mockMVC.perform(get(endpoint).accept(mediaType))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        return mapper.readValue(resJson, SindexTestsCorrect.listIndexType);
    }

}
