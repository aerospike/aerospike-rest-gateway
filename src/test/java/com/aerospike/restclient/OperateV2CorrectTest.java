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
import com.aerospike.restclient.domain.operationmodels.OperationTypes;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(Parameterized.class)
@SpringBootTest
public class OperateV2CorrectTest {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    String OPERATION_TYPE_KEY = "type";

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private final Key testKey;
    private final Key testKey2;

    private final Key testKey3;
    private final Key intKey;
    private final Key bytesKey;
    private final String testEndpoint;
    private final String batchEndpoint;
    private final OperationV2Performer opPerformer;

    @Parameterized.Parameters
    public static Object[][] getParams() {
        return new Object[][]{
                {new JSONOperationV2Performer(), true},
                {new MsgPackOperationV2Performer(), true},
                {new JSONOperationV2Performer(), false},
                {new MsgPackOperationV2Performer(), false}
        };
    }

    /* Set up the correct msgpack/json performer for this set of runs. Also decided whether to use the endpoint with a set or without */
    public OperateV2CorrectTest(OperationV2Performer performer, boolean useSet) {
        this.opPerformer = performer;
        if (useSet) {
            testKey = new Key("test", "junit", "operate");
            testEndpoint = ASTestUtils.buildEndpointV2("operate", "test", "junit", "operate");
            testKey2 = new Key("test", "junit", "operate2");
            testKey3 = new Key("test", "junit", "operate3");
            intKey = new Key("test", "junit", 1);
            bytesKey = new Key("test", "junit", new byte[]{1, 127, 127, 1});
            batchEndpoint = "/v2/operate/read/test/junit";
        } else {
            testKey = new Key("test", null, "operate");
            testEndpoint = ASTestUtils.buildEndpointV2("operate", "test", "operate");
            testKey2 = new Key("test", null, "operate2");
            testKey3 = new Key("test", null, "operate3");
            intKey = new Key("test", null, 1);
            bytesKey = new Key("test", null, new byte[]{1, 127, 127, 1});
            batchEndpoint = "/v2/operate/read/test";
        }
    }

    @Before
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        Bin strBin = new Bin("str", "bin");
        Bin intBin = new Bin("int", 5);
        Bin doubleBin = new Bin("double", 5.2);
        client.put(null, testKey, strBin, intBin);
        client.put(null, testKey2, strBin, intBin);
        client.put(null, testKey3, doubleBin);
        client.put(null, intKey, strBin, intBin);
        client.put(null, bytesKey, strBin, intBin);
    }

    @After
    public void clean() {
        client.delete(null, testKey);
        client.delete(null, testKey2);
        client.delete(null, testKey3);
        client.delete(null, intKey);
        client.delete(null, bytesKey);
    }

    @Test
    public void testGetHeaderOp() {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.GET_HEADER);

        Map<String, Object> resp = opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);
        Map<String, Object> rcRecord = (Map<String, Object>) resp.get("record");

        Assert.assertNull(rcRecord.get(AerospikeAPIConstants.RECORD_BINS));

        Record realRecord = client.getHeader(null, testKey);

        int generation = (int) rcRecord.get(AerospikeAPIConstants.GENERATION);
        Assert.assertEquals(generation, realRecord.generation);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testGetOp() {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.GET);

        Map<String, Object> resp = opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);
        Map<String, Object> rcRecord = (Map<String, Object>) resp.get("record");
        Map<String, Object> realBins = client.get(null, testKey).bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj((Map<String, Object>) rcRecord.get("bins"), realBins));
    }

    @Test
    public void testAddIntOp() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put("binName", "int");
        opMap.put("incr", 2);
        opMap.put(OPERATION_TYPE_KEY, OperationTypes.ADD);

        opPerformer.performOperationsAndExpect(mockMVC, testEndpoint, opRequest, status().isOk());

        Map<String, Object> expectedBins = new HashMap<>();
        expectedBins.put("str", "bin");
        expectedBins.put("int", 7L);

        Map<String, Object> realBins = client.get(null, testKey).bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj(expectedBins, realBins));
    }

    @Test
    public void testAddDoubleOp() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put("binName", "double");
        opMap.put("incr", 2.2);
        opMap.put(OPERATION_TYPE_KEY, OperationTypes.ADD);

        opPerformer.performOperationsAndExpect(mockMVC, testEndpoint + "3", opRequest, status().isOk());

        Map<String, Object> expectedBins = new HashMap<>();
        expectedBins.put("str", "bin");
        expectedBins.put("double", 7.2);

        Map<String, Object> realBins = client.get(null, testKey).bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj(expectedBins, realBins));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testReadOp() {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.READ);
        opMap.put("binName", "str");

        Map<String, Object> resp = opPerformer.performOperationsAndReturn(mockMVC, testEndpoint, opRequest);
        Map<String, Object> rcRecord = (Map<String, Object>) resp.get("record");
        /* Only read the str bin on the get*/
        Map<String, Object> realBins = client.get(null, testKey, "str").bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj((Map<String, Object>) rcRecord.get("bins"), realBins));
    }

    @Test
    public void testPutOp() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put("binName", "new");
        opMap.put("value", "put");
        opMap.put(OPERATION_TYPE_KEY, OperationTypes.PUT);

        opList.add(opMap);

        opPerformer.performOperationsAndExpect(mockMVC, testEndpoint, opRequest, status().isOk());

        Map<String, Object> expectedBins = new HashMap<>();
        expectedBins.put("str", "bin");
        expectedBins.put("int", 5L);
        expectedBins.put("new", "put");

        Map<String, Object> realBins = client.get(null, testKey).bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj(expectedBins, realBins));
    }

    @Test
    public void testAppendOp() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.APPEND);
        opMap.put("value", "ary");
        opMap.put("binName", "str");

        opPerformer.performOperationsAndExpect(mockMVC, testEndpoint, opRequest, status().isOk());

        /* Only read the str bin on the get*/
        Map<String, Object> expectedBins = new HashMap<>();
        expectedBins.put("str", "binary");
        Map<String, Object> realBins = client.get(null, testKey, "str").bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj(expectedBins, realBins));
    }

    @Test
    public void testPrependOp() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.PREPEND);
        opMap.put("value", "ro");
        opMap.put("binName", "str");

        opPerformer.performOperationsAndExpect(mockMVC, testEndpoint, opRequest, status().isOk());

        /* Only read the str bin on the get*/
        Map<String, Object> expectedBins = new HashMap<>();
        expectedBins.put("str", "robin");
        Map<String, Object> realBins = client.get(null, testKey, "str").bins;

        Assert.assertTrue(ASTestUtils.compareMapStringObj(expectedBins, realBins));
    }

    @Test
    public void testTouchOp() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        Record record = client.get(null, testKey);
        int oldGeneration = record.generation;

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.TOUCH);

        opList.add(opMap);

        opPerformer.performOperationsAndExpect(mockMVC, testEndpoint, opRequest, status().isOk());

        record = client.get(null, testKey);
        Assert.assertEquals(oldGeneration + 1, record.generation);
    }

    @Test
    public void testTouchOpWithIntegerKey() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        String intEndpoint;

        if (bytesKey.setName == null) {
            intEndpoint = ASTestUtils.buildEndpointV2("operate", "test", "1") + "?keytype=INTEGER";
        } else {
            intEndpoint = ASTestUtils.buildEndpointV2("operate", "test", "junit", "1") + "?keytype=INTEGER";
        }

        Record record = client.get(null, intKey);
        int oldGeneration = record.generation;

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.TOUCH);

        opList.add(opMap);

        opPerformer.performOperationsAndExpect(mockMVC, intEndpoint, opRequest, status().isOk());

        record = client.get(null, intKey);
        Assert.assertEquals(oldGeneration + 1, record.generation);
    }

    @Test
    public void testTouchOpWithBytesKey() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        String urlBytes = Base64.getUrlEncoder().encodeToString((byte[]) bytesKey.userKey.getObject());
        String bytesEndpoint;

        if (bytesKey.setName == null) {
            bytesEndpoint = ASTestUtils.buildEndpointV2("operate", "test", urlBytes) + "?keytype=BYTES";
        } else {
            bytesEndpoint = ASTestUtils.buildEndpointV2("operate", "test", "junit", urlBytes) + "?keytype=BYTES";
        }

        Record record = client.get(null, bytesKey);
        int oldGeneration = record.generation;

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.TOUCH);

        opPerformer.performOperationsAndExpect(mockMVC, bytesEndpoint, opRequest, status().isOk());

        record = client.get(null, bytesKey);
        Assert.assertEquals(oldGeneration + 1, record.generation);
    }

    /*
     * Touch a record by providing it's digest
     */
    @Test
    public void testTouchOpWithDigestKey() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        String urlBytes = Base64.getUrlEncoder().encodeToString(testKey.digest);
        String bytesEndpoint;

        if (testKey.setName == null) {
            bytesEndpoint = ASTestUtils.buildEndpointV2("operate", "test", urlBytes) + "?keytype=DIGEST";
        } else {
            bytesEndpoint = ASTestUtils.buildEndpointV2("operate", "test", "junit", urlBytes) + "?keytype=DIGEST";
        }

        Record record = client.get(null, testKey);
        int oldGeneration = record.generation;

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.TOUCH);

        opList.add(opMap);

        opPerformer.performOperationsAndExpect(mockMVC, bytesEndpoint, opRequest, status().isOk());

        record = client.get(null, testKey);
        Assert.assertEquals(oldGeneration + 1, record.generation);
    }

    @Test
    public void testGetOpNonExistentRecord() throws Exception {
        // Key that does not exist
        String fakeEndpoint = ASTestUtils.buildEndpointV2("operate", "test", "junit12345", "operate");
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.GET);

        String jsString = objectMapper.writeValueAsString(opRequest);
        ASTestUtils.performOperationAndExpect(mockMVC, fakeEndpoint, jsString, status().isNotFound());
    }

    @Test
    public void testDeleteOp() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.DELETE);

        opPerformer.performOperationsAndExpect(mockMVC, testEndpoint, opRequest, status().isOk());

        Record record = client.get(null, testKey);
        Assert.assertNull(record);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testBatchGetOp() throws Exception {
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put(OPERATION_TYPE_KEY, OperationTypes.GET);

        opList.add(opMap);
        String jsString = objectMapper.writeValueAsString(opRequest);
        String batchUrl = batchEndpoint + "?key=operate&key=operate2";
        String jsonResult = ASTestUtils.performOperationAndReturn(mockMVC, batchUrl, jsString);

        TypeReference<Map<String, Object>> ref = new TypeReference<>() {
        };
        List<Map<String, Object>> records = (List<Map<String, Object>>) objectMapper.readValue(jsonResult, ref)
                .get("records");
        List<Object> recordBins = Collections.singletonList(records.stream()
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
        Map<String, Object> opRequest = new HashMap<>();
        List<Map<String, Object>> opList = new ArrayList<>();
        Map<String, Object> opMap = new HashMap<>();
        opRequest.put("opsList", opList);
        opList.add(opMap);

        opMap.put("binName", "str");
        opMap.put(OPERATION_TYPE_KEY, OperationTypes.GET);

        opList.add(opMap);
        String jsString = objectMapper.writeValueAsString(opRequest);
        String batchUrl = batchEndpoint + "?key=operate&key=operate2";
        String jsonResult = ASTestUtils.performOperationAndReturn(mockMVC, batchUrl, jsString);

        TypeReference<Map<String, Object>> ref = new TypeReference<>() {
        };
        List<Map<String, Object>> records = (List<Map<String, Object>>) objectMapper.readValue(jsonResult, ref)
                .get("records");
        List<Object> recordBins = Collections.singletonList(records.stream()
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
