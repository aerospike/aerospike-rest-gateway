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
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.AerospikeOperation;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static com.aerospike.restclient.util.AerospikeAPIConstants.OPERATION_FIELD;
import static com.aerospike.restclient.util.AerospikeAPIConstants.OPERATION_VALUES_FIELD;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OperateV1TestCorrect {

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private final Key testKey = new Key("test", "junit", "operate");
    private final Key testKey2 = new Key("test", "junit", "operate2");
    private final Key intKey = new Key("test", "junit", 1);
    private final Key bytesKey = new Key("test", "junit", new byte[]{1, 127, 127, 1});

    private final String testEndpoint = ASTestUtils.buildEndpointV1("operate", "test", "junit", "operate");
    private final String batchEndpoint = "/v1/operate/read/test/junit";
    private final TypeReference<Map<String, Object>> binType = new TypeReference<Map<String, Object>>() {
    };

    @Before
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        Bin strBin = new Bin("str", "bin");
        Bin intBin = new Bin("int", 5);
        client.put(null, testKey, strBin, intBin);
        client.put(null, testKey2, strBin, intBin);
        client.put(null, intKey, strBin, intBin);
        client.put(null, bytesKey, strBin, intBin);
    }

    @After
    public void clean() {
        client.delete(null, testKey);
        client.delete(null, testKey2);
        client.delete(null, intKey);
        client.delete(null, bytesKey);
    }

    @Test
    public void testGetHeaderOp() throws Exception {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();
        opMap.put(OPERATION_FIELD, AerospikeOperation.GET_HEADER);
        opMap.put(OPERATION_VALUES_FIELD, opValues);

        opList.add(opMap);
        String jsString = objectMapper.writeValueAsString(opList);
        String jsonResult = ASTestUtils.performOperationAndReturn(mockMVC, testEndpoint, jsString);

        Map<String, Object> rcRecord = objectMapper.readValue(jsonResult, binType);
        Assert.assertNull(rcRecord.get(AerospikeAPIConstants.RECORD_BINS));

        Record realRecord = client.getHeader(null, testKey);

        int generation = (int) rcRecord.get(AerospikeAPIConstants.GENERATION);
        Assert.assertEquals(generation, realRecord.generation);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetOp() throws Exception {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();
        opMap.put(OPERATION_FIELD, AerospikeOperation.GET);
        opMap.put(OPERATION_VALUES_FIELD, opValues);

        opList.add(opMap);
        String jsString = objectMapper.writeValueAsString(opList);
        String jsonResult = ASTestUtils.performOperationAndReturn(mockMVC, testEndpoint, jsString);

        Map<String, Object> binsObject = objectMapper.readValue(jsonResult, binType);
        Map<String, Object> realBins = client.get(null, testKey).bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj((Map<String, Object>) binsObject.get("bins"), realBins));
    }

    @Test
    public void testAddOp() throws Exception {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        opValues.put("bin", "int");
        opValues.put("incr", 2);
        opMap.put(OPERATION_FIELD, AerospikeOperation.ADD);
        opMap.put(OPERATION_VALUES_FIELD, opValues);
        opList.add(opMap);

        String payload = objectMapper.writeValueAsString(opList);
        ASTestUtils.performOperation(mockMVC, testEndpoint, payload);

        Map<String, Object> expectedBins = new HashMap<>();
        expectedBins.put("str", "bin");
        expectedBins.put("int", 7L);

        Map<String, Object> realBins = client.get(null, testKey).bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj(expectedBins, realBins));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReadOp() throws Exception {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();
        opMap.put(OPERATION_FIELD, AerospikeOperation.READ);
        opValues.put("bin", "str");
        opMap.put(OPERATION_VALUES_FIELD, opValues);

        opList.add(opMap);
        String jsString = objectMapper.writeValueAsString(opList);

        String jsonResult = ASTestUtils.performOperationAndReturn(mockMVC, testEndpoint, jsString);
        Map<String, Object> binsObject = objectMapper.readValue(jsonResult, binType);
        /* Only read the str bin on the get*/
        Map<String, Object> realBins = client.get(null, testKey, "str").bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj((Map<String, Object>) binsObject.get("bins"), realBins));
    }

    @Test
    public void testPutOp() throws Exception {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        opValues.put("bin", "new");
        opValues.put("value", "put");
        opMap.put(OPERATION_FIELD, AerospikeOperation.PUT);
        opMap.put(OPERATION_VALUES_FIELD, opValues);
        opList.add(opMap);

        String payload = objectMapper.writeValueAsString(opList);

        ASTestUtils.performOperation(mockMVC, testEndpoint, payload);

        Map<String, Object> expectedBins = new HashMap<>();
        expectedBins.put("str", "bin");
        expectedBins.put("int", 5L);
        expectedBins.put("new", "put");

        Map<String, Object> realBins = client.get(null, testKey).bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj(expectedBins, realBins));
    }

    @Test
    public void testAppendOp() throws Exception {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();
        opMap.put(OPERATION_FIELD, AerospikeOperation.APPEND);
        opValues.put("value", "ary");
        opValues.put("bin", "str");
        opMap.put(OPERATION_VALUES_FIELD, opValues);

        opList.add(opMap);
        String jsString = objectMapper.writeValueAsString(opList);

        ASTestUtils.performOperation(mockMVC, testEndpoint, jsString);

        /* Only read the str bin on the get*/
        Map<String, Object> expectedBins = new HashMap<>();
        expectedBins.put("str", "binary");
        Map<String, Object> realBins = client.get(null, testKey, "str").bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj(expectedBins, realBins));
    }

    @Test
    public void testPrependOp() throws Exception {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        opMap.put(OPERATION_FIELD, AerospikeOperation.PREPEND);
        opValues.put("value", "ro");
        opValues.put("bin", "str");
        opMap.put(OPERATION_VALUES_FIELD, opValues);
        opList.add(opMap);

        String jsString = objectMapper.writeValueAsString(opList);

        ASTestUtils.performOperation(mockMVC, testEndpoint, jsString);

        /* Only read the str bin on the get*/
        Map<String, Object> expectedBins = new HashMap<>();
        expectedBins.put("str", "robin");
        Map<String, Object> realBins = client.get(null, testKey, "str").bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj(expectedBins, realBins));
    }

    @Test
    public void testTouchOp() throws Exception {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        Record record = client.get(null, testKey);
        int oldGeneration = record.generation;

        opMap.put(OPERATION_FIELD, AerospikeOperation.TOUCH);
        opMap.put(OPERATION_VALUES_FIELD, opValues);
        opList.add(opMap);

        String jsString = objectMapper.writeValueAsString(opList);

        ASTestUtils.performOperation(mockMVC, testEndpoint, jsString);

        record = client.get(null, testKey);
        Assert.assertEquals(oldGeneration + 1, record.generation);
    }

    @Test
    public void testTouchOpWithIntegerKey() throws Exception {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();
        String intEndpoint = ASTestUtils.buildEndpointV1("operate", "test", "junit", "1") + "?keytype=INTEGER";
        Record record = client.get(null, intKey);
        int oldGeneration = record.generation;

        opMap.put(OPERATION_FIELD, AerospikeOperation.TOUCH);
        opMap.put(OPERATION_VALUES_FIELD, opValues);
        opList.add(opMap);

        String jsString = objectMapper.writeValueAsString(opList);

        ASTestUtils.performOperation(mockMVC, intEndpoint, jsString);

        record = client.get(null, intKey);
        Assert.assertEquals(oldGeneration + 1, record.generation);
    }

    @Test
    public void testTouchOpWithBytesKey() throws Exception {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        String urlBytes = Base64.getUrlEncoder().encodeToString((byte[]) bytesKey.userKey.getObject());
        String bytesEndpoint = ASTestUtils.buildEndpointV1("operate", "test", "junit", urlBytes) + "?keytype=BYTES";

        Record record = client.get(null, bytesKey);
        int oldGeneration = record.generation;

        opMap.put(OPERATION_FIELD, AerospikeOperation.TOUCH);
        opMap.put(OPERATION_VALUES_FIELD, opValues);
        opList.add(opMap);

        String jsString = objectMapper.writeValueAsString(opList);

        ASTestUtils.performOperation(mockMVC, bytesEndpoint, jsString);

        record = client.get(null, bytesKey);
        Assert.assertEquals(oldGeneration + 1, record.generation);
    }

    /*
     * Touch a record by providing it's digest
     */
    @Test
    public void testTouchOpWithDigestKey() throws Exception {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        String urlBytes = Base64.getUrlEncoder().encodeToString(testKey.digest);
        String bytesEndpoint = ASTestUtils.buildEndpointV1("operate", "test", "junit", urlBytes) + "?keytype=DIGEST";

        Record record = client.get(null, testKey);
        int oldGeneration = record.generation;

        opMap.put(OPERATION_FIELD, AerospikeOperation.TOUCH);
        opMap.put(OPERATION_VALUES_FIELD, opValues);
        opList.add(opMap);

        String jsString = objectMapper.writeValueAsString(opList);
        ASTestUtils.performOperation(mockMVC, bytesEndpoint, jsString);

        record = client.get(null, testKey);
        Assert.assertEquals(oldGeneration + 1, record.generation);
    }

    @Test
    public void testGetOpNonExistentRecord() throws Exception {
        // Key that does not exist
        String fakeEndpoint = ASTestUtils.buildEndpointV1("operate", "test", "junit12345", "operate");
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();
        opMap.put(OPERATION_FIELD, AerospikeOperation.GET);
        opMap.put(OPERATION_VALUES_FIELD, opValues);

        opList.add(opMap);
        String jsString = objectMapper.writeValueAsString(opList);
        ASTestUtils.performOperationAndExpect(mockMVC, fakeEndpoint, jsString, status().isNotFound());
    }

    @Test
    public void testDeleteOp() throws Exception {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();

        opMap.put(OPERATION_FIELD, AerospikeOperation.DELETE);
        opMap.put(OPERATION_VALUES_FIELD, opValues);
        opList.add(opMap);

        String jsString = objectMapper.writeValueAsString(opList);

        ASTestUtils.performOperation(mockMVC, testEndpoint, jsString);

        Record record = client.get(null, testKey);
        Assert.assertNull(record);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testBatchGetOp() throws Exception {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();
        opMap.put(OPERATION_FIELD, AerospikeOperation.GET);
        opMap.put(OPERATION_VALUES_FIELD, opValues);

        opList.add(opMap);
        String jsString = objectMapper.writeValueAsString(opList);
        String batchUrl = batchEndpoint + "?key=operate&key=operate2";
        String jsonResult = ASTestUtils.performOperationAndReturn(mockMVC, batchUrl, jsString);

        TypeReference<List<Map<String, Object>>> ref = new TypeReference<List<Map<String, Object>>>() {
        };
        List<Object> recordBins = Collections.singletonList(objectMapper.readValue(jsonResult, ref)
                .stream()
                .map(r -> (Map<String, Object>) r.get("bins"))
                .map(Map::keySet)
                .flatMap(Collection::stream)
                .toList());
        List<Object> expected = Collections.singletonList(Arrays.stream(client.get(null, new Key[]{testKey, testKey2}))
                .map(r -> r.bins)
                .map(Map::keySet)
                .flatMap(Collection::stream)
                .toList());

        assertIterableEquals(expected, recordBins);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testBatchGetBinOp() throws Exception {
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        Map<String, Object> opValues = new HashMap<>();
        opValues.put("bin", "str");
        opMap.put(OPERATION_FIELD, AerospikeOperation.GET);
        opMap.put(OPERATION_VALUES_FIELD, opValues);

        opList.add(opMap);
        String jsString = objectMapper.writeValueAsString(opList);
        String batchUrl = batchEndpoint + "?key=operate&key=operate2";
        String jsonResult = ASTestUtils.performOperationAndReturn(mockMVC, batchUrl, jsString);

        TypeReference<List<Map<String, Object>>> ref = new TypeReference<List<Map<String, Object>>>() {
        };
        List<Object> recordBins = Collections.singletonList(objectMapper.readValue(jsonResult, ref)
                .stream()
                .map(r -> (Map<String, Object>) r.get("bins"))
                .map(Map::keySet)
                .flatMap(Collection::stream)
                .toList());
        List<Object> expected = Collections.singletonList(Arrays.stream(client.get(null, new Key[]{testKey, testKey2}))
                .map(r -> r.bins)
                .map(Map::keySet)
                .flatMap(Collection::stream)
                .filter(k -> k.equals("str"))
                .toList());

        assertIterableEquals(expected, recordBins);
    }
}



