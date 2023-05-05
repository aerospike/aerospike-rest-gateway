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

import com.aerospike.client.Record;
import com.aerospike.client.*;
import com.aerospike.client.Value.BytesValue;
import com.aerospike.client.policy.*;
import com.aerospike.client.task.RegisterTask;
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.config.MsgPackConverter;
import com.aerospike.restclient.domain.operationmodels.ListReturnType;
import com.aerospike.restclient.domain.operationmodels.OperationTypes;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.AerospikeOperation;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SuppressWarnings("unchecked")
@RunWith(Parameterized.class)
@SpringBootTest
public class BatchCorrectTests {

    private final RestBatchComparator batchComparator;
    private final BatchHandler batchHandler;

    /* Needed to run as a Spring Boot test */
    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();

    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private final String ns = "test";
    private final String set = "batch";
    private final Key[] keys = new Key[100];
    private final Key intKey = new Key(ns, set, 1);
    private final Key bytesKey = new Key(ns, set, new byte[]{1, 127, 127, 1});

    private final static String recordUDFPath = "src/test/java/com/aerospike/restclient/udf/record_example.lua";
    private final static String recordUDFPkg = "record_example";

    private final String endpoint = "/v1/batch";

    @Before
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        Map<String, Object> mapCDT = new HashMap<>();
        Map<String, Object> mapCDT2 = new HashMap<>();
        List<Integer> listCDT = new ArrayList<>();
        mapCDT.put("m2", mapCDT2);
        mapCDT2.put("l1", listCDT);
        listCDT.add(1);
        listCDT.add(2);
        listCDT.add(3);

        for (int i = 0; i < keys.length; i++) {
            keys[i] = new Key("test", "junit", Integer.toString(i));
            Bin bin1 = new Bin("bin1", i);
            Bin bin2 = new Bin("bin2", i * 2);
            Bin bin3 = new Bin("bin3", i * 3);
            Bin bin4 = new Bin("bin4", (i * 1.4));
            Bin bin5 = new Bin("bin5", mapCDT);
            client.put(null, keys[i], bin1, bin2, bin3, bin4, bin5);
        }

        client.put(null, intKey, new Bin("keytype", "integer"));
        client.put(null, bytesKey, new Bin("keytype", "bytes"));

        // Slows down tests.
        RegisterTask task = client.register(null, recordUDFPath, recordUDFPkg + ".lua", Language.LUA);
        task.waitTillComplete();
    }

    @After
    public void clean() {
        for (Key key : keys) {
            client.delete(null, key);
        }
        client.delete(null, intKey);
        client.delete(null, bytesKey);
        client.removeUdf(null, recordUDFPkg + ".lua");
    }

    @Parameters
    public static Object[] mappers() {
        return new Object[][]{
                {new RestBatchComparator(), new JSONBatchHandler()},
                {new RestBatchComparator(), new MsgPackBatchHandler()}
        };
    }

    public BatchCorrectTests(RestBatchComparator comparator, BatchHandler handler) {
        this.batchComparator = comparator;
        this.batchHandler = handler;
    }

    @Test
    public void testGetExistingRecords() throws Exception {
        List<Map<String, Object>> batchKeys = new ArrayList<>();
        List<BatchRecord> batchRecs = new ArrayList<>();

        batchKeys.add(keyToBatchReadObject(null, keys[0], null, null));
        batchKeys.add(keyToBatchReadObject(null, keys[1], null, null));
        batchKeys.add(keyToBatchReadObject(null, keys[2], null, null));

        batchRecs.add(new BatchRead(keys[0], true));
        batchRecs.add(new BatchRead(keys[1], true));
        batchRecs.add(new BatchRead(keys[2], true));

        String payLoad = objectMapper.writeValueAsString(batchKeys);
        Map<String, Object> batchResponse = batchHandler.perform(mockMVC, endpoint, payLoad);
        List<Map<String, Object>> returnedRecords = (List<Map<String, Object>>) batchResponse.get("batchRecords");
        Assert.assertEquals(3, returnedRecords.size());

        client.get(null, batchRecs.stream().map(r -> (BatchRead) r).toList());
        Assert.assertTrue(compareRestRecordsToBatchRecords(returnedRecords, batchRecs));
    }

    @Test
    public void testGetExistingRecordsIsBackwardsCompatible() throws Exception {

        List<Map<String, Object>> batchKeys = new ArrayList<>();
        List<BatchRead> batchRecs = new ArrayList<>();

        batchKeys.add(keyToBatchReadObject(null, keys[0], null, null));
        batchKeys.add(keyToBatchReadObject(null, keys[1], null, null));
        batchKeys.add(keyToBatchReadObject(null, keys[2], null, null));

        batchRecs.add(new BatchRead(keys[0], true));
        batchRecs.add(new BatchRead(keys[1], true));
        batchRecs.add(new BatchRead(keys[2], true));

        String payLoad = objectMapper.writeValueAsString(batchKeys);
        Map<String, Object> batchResponse = batchHandler.perform(mockMVC, endpoint, payLoad);
        List<Map<String, Object>> returnedRecords = (List<Map<String, Object>>) batchResponse.get("batchRecords");
        Assert.assertEquals(3, returnedRecords.size());

        client.get(null, batchRecs);
        Assert.assertTrue(compareRestRecordsToBatchReads(returnedRecords, batchRecs));
    }

    @Test
    public void testBatchGetExistingRecordsBinFilter() throws Exception {
        List<Map<String, Object>> batchKeys = new ArrayList<>();
        List<BatchRecord> batchRecs = new ArrayList<>();
        String[] bins = new String[]{"bin1", "bin3"};

        batchKeys.add(keyToBatchReadObject(null, keys[0], null, bins));
        batchKeys.add(keyToBatchReadObject(null, keys[1], null, bins));
        batchKeys.add(keyToBatchReadObject(null, keys[2], null, bins));

        batchRecs.add(new BatchRead(keys[0], bins));
        batchRecs.add(new BatchRead(keys[1], bins));
        batchRecs.add(new BatchRead(keys[2], bins));

        String payLoad = objectMapper.writeValueAsString(batchKeys);
        Map<String, Object> batchResponse = batchHandler.perform(mockMVC, endpoint, payLoad);
        List<Map<String, Object>> returnedRecords = (List<Map<String, Object>>) batchResponse.get("batchRecords");
        Assert.assertEquals(3, returnedRecords.size());

        client.get(null, batchRecs.stream().map(r -> (BatchRead) r).toList());
        Assert.assertTrue(compareRestRecordsToBatchRecords(returnedRecords, batchRecs));
    }

    @Test
    public void testBatchGetWithNonExistentRecord() throws Exception {
        List<Map<String, Object>> batchKeys = new ArrayList<>();
        List<BatchRecord> batchRecs = new ArrayList<>();
        batchKeys.add(keyToBatchReadObject(null, keys[0], null, null));
        batchKeys.add(keyToBatchReadObject(null, keys[1], null, null));
        batchKeys.add(keyToBatchReadObject(null, new Key("test", "demo", "notreal"), null, null));
        batchKeys.add(keyToBatchReadObject(null, keys[2], null, null));

        batchRecs.add(new BatchRead(keys[0], true));
        batchRecs.add(new BatchRead(keys[1], true));
        batchRecs.add(new BatchRead(new Key("test", "demo", "notreal"), true));
        batchRecs.add(new BatchRead(keys[2], true));

        String payLoad = objectMapper.writeValueAsString(batchKeys);
        Map<String, Object> batchResponse = batchHandler.perform(mockMVC, endpoint, payLoad);
        List<Map<String, Object>> returnedRecords = (List<Map<String, Object>>) batchResponse.get("batchRecords");
        Assert.assertEquals(4, returnedRecords.size());

        client.get(null, batchRecs.stream().map(r -> (BatchRead) r).toList());
        Assert.assertTrue(compareRestRecordsToBatchRecords(returnedRecords, batchRecs));
    }

    @Test
    public void testGetExistingRecordsWithIntKey() throws Exception {
        List<Map<String, Object>> batchKeys = new ArrayList<>();
        List<BatchRecord> batchRecs = new ArrayList<>();
        batchKeys.add(keyToBatchReadObject(null, keys[0], null, null));
        batchKeys.add(keyToBatchReadObject(null, keys[1], null, null));
        batchKeys.add(keyToBatchReadObject(null, keys[2], null, null));
        batchKeys.add(keyToBatchReadObject(null, intKey, AerospikeAPIConstants.RecordKeyType.INTEGER, null));

        batchRecs.add(new BatchRead(keys[0], true));
        batchRecs.add(new BatchRead(keys[1], true));
        batchRecs.add(new BatchRead(keys[2], true));
        batchRecs.add(new BatchRead(intKey, true));

        String payLoad = objectMapper.writeValueAsString(batchKeys);
        Map<String, Object> batchResponse = batchHandler.perform(mockMVC, endpoint, payLoad);
        List<Map<String, Object>> returnedRecords = (List<Map<String, Object>>) batchResponse.get("batchRecords");
        Assert.assertEquals(4, returnedRecords.size());

        client.get(null, batchRecs.stream().map(r -> (BatchRead) r).toList());
        Assert.assertTrue(compareRestRecordsToBatchRecords(returnedRecords, batchRecs));
    }

    @Test
    public void testGetExistingRecordsWithBytesKey() throws Exception {
        List<Map<String, Object>> batchKeys = new ArrayList<>();
        List<BatchRecord> batchRecs = new ArrayList<>();
        batchKeys.add(keyToBatchReadObject(null, bytesKey, AerospikeAPIConstants.RecordKeyType.BYTES, null));
        batchRecs.add(new BatchRead(bytesKey, true));

        String payLoad = objectMapper.writeValueAsString(batchKeys);
        Map<String, Object> batchResponse = batchHandler.perform(mockMVC, endpoint, payLoad);
        List<Map<String, Object>> returnedRecords = (List<Map<String, Object>>) batchResponse.get("batchRecords");
        Assert.assertEquals(1, returnedRecords.size());

        client.get(null, batchRecs.stream().map(r -> (BatchRead) r).toList());
        Assert.assertTrue(compareRestRecordsToBatchRecords(returnedRecords, batchRecs));
    }

    @Test
    public void testGetExistingRecordsWithDigestKey() throws Exception {
        List<Map<String, Object>> batchKeys = new ArrayList<>();
        List<BatchRecord> batchRecs = new ArrayList<>();
        batchKeys.add(keyToBatchReadObject(null, keys[0], AerospikeAPIConstants.RecordKeyType.DIGEST, null));
        batchRecs.add(new BatchRead(keys[0], true));

        String payLoad = objectMapper.writeValueAsString(batchKeys);
        Map<String, Object> batchResponse = batchHandler.perform(mockMVC, endpoint, payLoad);
        List<Map<String, Object>> returnedRecords = (List<Map<String, Object>>) batchResponse.get("batchRecords");
        Assert.assertEquals(1, returnedRecords.size());

        client.get(null, batchRecs.stream().map(r -> (BatchRead) r).toList());
        Assert.assertTrue(compareRestRecordsToBatchRecords(returnedRecords, batchRecs));
    }

    @Test
    public void testGetRecordCDT() throws Exception {
        List<Map<String, Object>> batchKeys = new ArrayList<>();
        Map<String, Object> batchRecReq = new HashMap<>();
        List<Map<String, Object>> opsList = new ArrayList<>();
        Map<String, Object> op1 = new HashMap<>();
        opsList.add(op1);
        List<Map<String, Object>> ctxList = new ArrayList<>();
        Map<String, Object> ctx1 = new HashMap<>();
        Map<String, Object> ctx2 = new HashMap<>();
        ctxList.add(ctx1);
        ctxList.add(ctx2);

        op1.put("type", OperationTypes.LIST_GET_BY_INDEX);
        op1.put("binName", "bin5");
        op1.put("index", 1);
        op1.put("listReturnType", ListReturnType.VALUE);
        op1.put("ctx", ctxList);
        ctx1.put("type", AerospikeAPIConstants.CTX.MAP_KEY);
        ctx1.put("key", "m2");
        ctx2.put("type", AerospikeAPIConstants.CTX.MAP_INDEX);
        ctx2.put("index", 0);

        batchRecReq.put("type", "READ");
        batchRecReq.put("key", keyToMap(keys[0], AerospikeAPIConstants.RecordKeyType.STRING));
        batchRecReq.put("opsList", opsList);
        batchKeys.add(batchRecReq);

        String payLoad = objectMapper.writeValueAsString(batchKeys);
        Map<String, Object> batchResponse = batchHandler.perform(mockMVC, endpoint, payLoad);
        List<Map<String, Object>> returnedRecords = (List<Map<String, Object>>) batchResponse.get("batchRecords");
        Assert.assertEquals(1, returnedRecords.size());
        Map<String, Object> batchRecord = returnedRecords.get(0);
        Map<String, Object> record = (Map<String, Object>) batchRecord.get("record");
        Map<String, Object> bins = (Map<String, Object>) record.get("bins");
        Assert.assertEquals(2, bins.get("bin5"));
    }

    @Test
    public void testWriteSingleExistingRecord() throws Exception {
        List<Map<String, Object>> batchKeys = new ArrayList<>();
        List<BatchRecord> batchRecs = new ArrayList<>();
        List<Map<String, Object>> expectedOpsListMap = new ArrayList<>();

        Map<String, Object> op1 = new HashMap<>();
        op1.put("type", AerospikeOperation.ADD);
        op1.put("binName", "bin1");
        op1.put("incr", 1);

        Map<String, Object> op2 = new HashMap<>();
        op2.put("type", AerospikeOperation.PUT);
        op2.put("binName", "bin2");
        op2.put("value", "new val");

        expectedOpsListMap.add(op1);
        expectedOpsListMap.add(op2);

        batchKeys.add(keyToBatchWriteObject(null, keys[3], null, expectedOpsListMap));
        batchRecs.add(new BatchWrite(keys[3],
                new Operation[]{Operation.add(new Bin("bin1", 1)), Operation.put(new Bin("bin2", "new val"))}));

        String payLoad = objectMapper.writeValueAsString(batchKeys);
        Map<String, Object> batchResponse = batchHandler.perform(mockMVC, endpoint, payLoad);
        List<Map<String, Object>> returnedRecords = (List<Map<String, Object>>) batchResponse.get("batchRecords");

        Record actualRecord = client.get(null, keys[3]);
        client.operate(null, batchRecs);
        Assert.assertEquals(1, returnedRecords.size());
        Assert.assertTrue(compareRestRecordsToBatchRecords(returnedRecords, batchRecs));

        Assert.assertEquals(4L, actualRecord.bins.get("bin1"));
        Assert.assertEquals("new val", actualRecord.bins.get("bin2"));
    }

    @Test
    public void testWriteExistingRecordWithUpdateOnly() throws Exception {
        List<Map<String, Object>> batchKeys = new ArrayList<>();
        List<Map<String, Object>> expectedOpsListMap = new ArrayList<>();
        Key key = new Key("test", "testset", "does not exist");

        BatchWritePolicy policy = new BatchWritePolicy();
        policy.recordExistsAction = RecordExistsAction.UPDATE_ONLY;

        Map<String, Object> op1 = new HashMap<>();
        op1.put("type", OperationTypes.ADD);
        op1.put("binName", "bin1");
        op1.put("incr", 1);

        expectedOpsListMap.add(op1);

        batchKeys.add(keyToBatchWriteObject(policy, key, null, expectedOpsListMap));

        String payLoad = objectMapper.writeValueAsString(batchKeys);
        Map<String, Object> batchResponse = batchHandler.perform(mockMVC, endpoint, payLoad);
        List<Map<String, Object>> returnedRecords = (List<Map<String, Object>>) batchResponse.get("batchRecords");

        Assert.assertEquals(1, returnedRecords.size());
        Map<String, Object> returnedRecord = returnedRecords.get(0);

        Assert.assertEquals(2, returnedRecord.get("resultCode"));
        Assert.assertEquals("Key not found", returnedRecord.get("resultCodeString"));
        Assert.assertNull(returnedRecord.get("record"));
        Assert.assertFalse((boolean) returnedRecord.get("inDoubt"));
    }

    @Test
    public void testWriteExistingRecordWithExpectGenEqual() throws Exception {
        List<Map<String, Object>> batchKeys = new ArrayList<>();
        List<Map<String, Object>> expectedOpsListMap = new ArrayList<>();
        Key key = new Key("test", "testset", "does not exist");

        BatchWritePolicy policy = new BatchWritePolicy();
        policy.generationPolicy = GenerationPolicy.EXPECT_GEN_EQUAL;
        policy.generation = 100;

        Map<String, Object> op1 = new HashMap<>();
        op1.put("type", OperationTypes.ADD);
        op1.put("binName", "bin1");
        op1.put("incr", 1);

        expectedOpsListMap.add(op1);

        batchKeys.add(keyToBatchWriteObject(policy, key, null, expectedOpsListMap));

        String payLoad = objectMapper.writeValueAsString(batchKeys);
        Map<String, Object> batchResponse = batchHandler.perform(mockMVC, endpoint, payLoad);
        List<Map<String, Object>> returnedRecords = (List<Map<String, Object>>) batchResponse.get("batchRecords");

        Assert.assertEquals(1, returnedRecords.size());
        Map<String, Object> returnedRecord = returnedRecords.get(0);

        Assert.assertEquals(3, returnedRecord.get("resultCode"));
        Assert.assertEquals("Generation error", returnedRecord.get("resultCodeString"));
        Assert.assertNull(returnedRecord.get("record"));
        Assert.assertFalse((boolean) returnedRecord.get("inDoubt"));
    }

    @Test
    public void testSingleUDFWithCommitMaster() throws Exception {
        List<Map<String, Object>> batchRecReq = new ArrayList<>();
        BatchUDFPolicy policy = new BatchUDFPolicy();
        policy.commitLevel = CommitLevel.COMMIT_MASTER;

        String bin = "bin5";
        Double binVal = 3.14159;

        List<Object> functionArgs = new ArrayList<>();
        functionArgs.add(bin);
        functionArgs.add(binVal);

        Map<String, Object> batchUDFMapReq = keyToBatchUDFObject(policy, keys[0], null, recordUDFPkg, "writeBin",
                functionArgs);
        batchRecReq.add(batchUDFMapReq);

        String payLoad = objectMapper.writeValueAsString(batchRecReq);
        Map<String, Object> batchResponse = batchHandler.perform(mockMVC, endpoint, payLoad);
        List<Map<String, Object>> returnedRecords = (List<Map<String, Object>>) batchResponse.get("batchRecords");

        Record record = client.get(null, keys[0]);
        Assert.assertEquals(1, returnedRecords.size());
        Assert.assertEquals(0, returnedRecords.get(0).get("resultCode"));

        Assert.assertEquals(binVal, record.bins.getOrDefault(bin, "error"));
    }

    @Test
    public void testSingleDeleteExistingRecord() throws Exception {
        List<Map<String, Object>> batchRecReq = new ArrayList<>();
        Map<String, Object> batchUDFMapReq = keyToBatchDeleteObject(null, keys[0], null);

        batchRecReq.add(batchUDFMapReq);

        String payLoad = objectMapper.writeValueAsString(batchRecReq);
        Map<String, Object> batchResponse = batchHandler.perform(mockMVC, endpoint, payLoad);
        List<Map<String, Object>> returnedRecords = (List<Map<String, Object>>) batchResponse.get("batchRecords");

        boolean recordStillExists = client.exists(null, keys[0]);
        Assert.assertEquals(1, returnedRecords.size());
        Assert.assertEquals(0, returnedRecords.get(0).get("resultCode"));
        Assert.assertFalse(recordStillExists);
    }

    @Test
    public void testSingleDeleteNonExistingRecord() throws Exception {
        List<Map<String, Object>> batchRecReq = new ArrayList<>();
        Map<String, Object> batchUDFMapReq = keyToBatchDeleteObject(null, new Key("test", "testset", "does not exist"),
                null);

        batchRecReq.add(batchUDFMapReq);

        String payLoad = objectMapper.writeValueAsString(batchRecReq);
        Map<String, Object> batchResponse = batchHandler.perform(mockMVC, endpoint, payLoad);
        List<Map<String, Object>> returnedRecords = (List<Map<String, Object>>) batchResponse.get("batchRecords");
        Map<String, Object> returnedRecord = returnedRecords.get(0);

        Assert.assertEquals(2, returnedRecord.get("resultCode"));
        Assert.assertEquals("Key not found", returnedRecord.get("resultCodeString"));
        Assert.assertNull(returnedRecord.get("record"));
        Assert.assertFalse((boolean) returnedRecord.get("inDoubt"));
    }

    @Test
    public void testSingleDeleteExpectGenEqual() throws Exception {
        List<Map<String, Object>> batchRecReq = new ArrayList<>();
        BatchDeletePolicy policy = new BatchDeletePolicy();
        policy.generationPolicy = GenerationPolicy.EXPECT_GEN_EQUAL;
        policy.generation = 100;

        Map<String, Object> batchUDFMapReq = keyToBatchDeleteObject(policy, keys[0], null);

        batchRecReq.add(batchUDFMapReq);

        String payLoad = objectMapper.writeValueAsString(batchRecReq);
        Map<String, Object> batchResponse = batchHandler.perform(mockMVC, endpoint, payLoad);
        List<Map<String, Object>> returnedRecords = (List<Map<String, Object>>) batchResponse.get("batchRecords");
        Map<String, Object> returnedRecord = returnedRecords.get(0);

        Assert.assertEquals(3, returnedRecord.get("resultCode"));
        Assert.assertEquals("Generation error", returnedRecord.get("resultCodeString"));
        Assert.assertNull(returnedRecord.get("record"));
        Assert.assertFalse((boolean) returnedRecord.get("inDoubt"));
    }

    @Test
    public void testComplexMultiBatchOperation() throws Exception {
        List<Map<String, Object>> batchRecReq = new ArrayList<>();
        BatchDeletePolicy deletePolicy = new BatchDeletePolicy();
        deletePolicy.durableDelete = true;

        List<Map<String, Object>> writeOps = new ArrayList<>();
        Map<String, Object> op1 = new HashMap<>();

        op1.put("type", OperationTypes.ADD);
        op1.put("binName", "bin1");
        op1.put("incr", 1);

        Map<String, Object> op2 = new HashMap<>();
        op2.put("type", OperationTypes.PUT);
        op2.put("binName", "bin2");
        op2.put("value", "new val");

        writeOps.add(op1);
        writeOps.add(op2);

        List<Object> functionArgs = new ArrayList<>();
        functionArgs.add("bin3");
        functionArgs.add("bin1");
        functionArgs.add("1");

        batchRecReq.add(keyToBatchDeleteObject(deletePolicy, keys[0], null));
        batchRecReq.add(keyToBatchReadObject(null, keys[1], null, null));
        batchRecReq.add(keyToBatchWriteObject(null, keys[2], null, writeOps));
        batchRecReq.add(keyToBatchUDFObject(null, keys[5], null, recordUDFPkg, "processRecord", functionArgs));

        String payLoad = objectMapper.writeValueAsString(batchRecReq);
        Map<String, Object> batchResponse = batchHandler.perform(mockMVC, endpoint, payLoad);
        List<Map<String, Object>> returnedRecords = (List<Map<String, Object>>) batchResponse.get("batchRecords");

        Assert.assertEquals(4, returnedRecords.size());
        for (Map<String, Object> record : returnedRecords) {
            int resultCode = (int) record.get("resultCode");
            if (resultCode != ResultCode.OK && resultCode != ResultCode.ENTERPRISE_ONLY) {
                Assert.fail(String.format("Expected OK or ENTERPRISE_ONLY response code. Received: %d", resultCode));
            }
        }
    }

    private Map<String, Object> keyToBatchReadObject(BatchReadPolicy policy, Key key,
                                                     AerospikeAPIConstants.RecordKeyType keyType, String[] bins) {
        Map<String, Object> batchObj = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        if (policy != null) {
            batchObj.put("policy", mapper.convertValue(policy, Map.class));
        }

        if (bins != null) {
            batchObj.put("binNames", bins);
        } else {
            batchObj.put("readAllBins", true);
        }

        batchObj.put("type", "READ");
        batchObj.put("key", keyToMap(key, keyType));
        return batchObj;
    }

    private Map<String, Object> keyToBatchWriteObject(BatchWritePolicy policy, Key key,
                                                      AerospikeAPIConstants.RecordKeyType keyType,
                                                      List<Map<String, Object>> opsList) {
        Map<String, Object> batchObj = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        if (policy != null) {
            batchObj.put("policy", mapper.convertValue(policy, Map.class));
        }

        batchObj.put("type", "WRITE");
        batchObj.put("key", keyToMap(key, keyType));
        batchObj.put("opsList", opsList);
        return batchObj;
    }

    private Map<String, Object> keyToBatchUDFObject(BatchUDFPolicy policy, Key key,
                                                    AerospikeAPIConstants.RecordKeyType keyType, String packageName,
                                                    String functionName, List<Object> functionArgs) {
        Map<String, Object> batchObj = new HashMap<>();

        if (policy != null) {
            batchObj.put("policy", policy);
        }

        batchObj.put("type", "UDF");
        batchObj.put("key", keyToMap(key, keyType));
        batchObj.put("packageName", packageName);
        batchObj.put("functionName", functionName);
        batchObj.put("functionArgs", functionArgs);
        return batchObj;
    }

    private Map<String, Object> keyToBatchDeleteObject(BatchDeletePolicy policy, Key key,
                                                       AerospikeAPIConstants.RecordKeyType keyType) {
        Map<String, Object> batchObj = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        if (policy != null) {
            batchObj.put("policy", mapper.convertValue(policy, Map.class));
        }

        batchObj.put("key", keyToMap(key, keyType));
        batchObj.put("type", "DELETE");
        return batchObj;
    }

    private Map<String, Object> keyToMap(Key key, AerospikeAPIConstants.RecordKeyType type) {
        Map<String, Object> restKey = new HashMap<>();
        String userKey;
        String keyType = null;
        restKey.put("namespace", key.namespace);
        restKey.put("setName", key.setName);

        if (type == null) {
            type = AerospikeAPIConstants.RecordKeyType.STRING;
        }

        switch (type) {
            case BYTES:
                BytesValue value = ((BytesValue) key.userKey);
                userKey = Base64.getUrlEncoder().encodeToString((byte[]) value.getObject());
                keyType = "BYTES";
                break;
            case DIGEST:
                userKey = Base64.getUrlEncoder().encodeToString(key.digest);
                keyType = "DIGEST";
                break;
            case INTEGER:
                userKey = key.userKey.toString();
                keyType = "INTEGER";
                break;
            default:
                userKey = key.userKey.toString();
                break;
        }

        restKey.put("userKey", userKey);

        if (keyType != null) {
            restKey.put("keytype", keyType);
        }

        return restKey;
    }

    private boolean compareRestRecordsToBatchReads(List<Map<String, Object>> returnedRecords,
                                                   List<BatchRead> batchReads) {
        if (batchReads.size() != returnedRecords.size()) {
            return false;
        }
        for (int i = 0; i < batchReads.size(); i++) {
            if (!batchComparator.compareRestBatchReadToBatchRead(returnedRecords.get(i), batchReads.get(i))) {
                return false;
            }
        }
        return true;
    }

    private boolean compareRestRecordsToBatchRecords(List<Map<String, Object>> returnedRecords,
                                                     List<BatchRecord> batchRecords) {
        if (batchRecords.size() != returnedRecords.size()) {
            return false;
        }
        for (int i = 0; i < batchRecords.size(); i++) {
            if (!batchComparator.compareRestBatchRecordToBatchRecord(returnedRecords.get(i), batchRecords.get(i))) {
                return false;
            }
        }
        return true;
    }
}

@SuppressWarnings("unchecked")
class RestBatchComparator {

    @SuppressWarnings("unchecked")
    public boolean compareRestBatchReadToBatchRead(Map<String, Object> restRecord, BatchRead batchRead) {
        Map<String, Object> restKey = (Map<String, Object>) restRecord.get("key");

        if (restKey.get("userKey") != null) {
            if (!compareKey(batchRead.key, restKey.get("userKey").toString(), (String) restKey.get("keytype"))) {
                return false;
            }
        } else {
            if (!compareDigests((String) restKey.get("digest"), batchRead.key)) {
                return false;
            }
        }

        if (!restKey.get("namespace").equals(batchRead.key.namespace)) {
            return false;
        }
        if (restKey.get("setName") != null) {
            if (!restKey.get("setName").equals(batchRead.key.setName)) {
                return false;
            }
        } else if (batchRead.key.setName != null) {
            return false;
        }
        if (restRecord.get("record") != null) {
            return ASTestUtils.compareRCRecordToASRecord((Map<String, Object>) restRecord.get("record"),
                    batchRead.record);
        } else {
            return batchRead.record == null;
        }
    }

    public boolean compareRestBatchRecordToBatchRecord(Map<String, Object> restRecord, BatchRecord batchRecord) {
        Map<String, Object> restKey = (Map<String, Object>) restRecord.get("key");

        if (restKey.get("userKey") != null) {
            if (!compareKey(batchRecord.key, restKey.get("userKey").toString(), (String) restKey.get("keytype"))) {
                return false;
            }
        } else {
            if (!compareDigests((String) restKey.get("digest"), batchRecord.key)) {
                return false;
            }
        }

        if (!restKey.get("namespace").equals(batchRecord.key.namespace)) {
            return false;
        }

        if (restKey.get("setName") != null) {
            if (!restKey.get("setName").equals(batchRecord.key.setName)) {
                return false;
            }
        } else if (batchRecord.key.setName != null) {
            return false;
        }

        if (!restRecord.get("inDoubt").equals(batchRecord.inDoubt)) {
            return false;
        }

        if (!restRecord.get("resultCode").equals(batchRecord.resultCode)) {
            return false;
        }

        if (!restRecord.get("resultCodeString").equals(ResultCode.getResultString(batchRecord.resultCode))) {
            return false;
        }

        if (restRecord.get("record") != null) {
            return ASTestUtils.compareRCRecordToASRecord((Map<String, Object>) restRecord.get("record"),
                    batchRecord.record);
        } else {
            return batchRecord.record == null;
        }
    }

    private boolean compareKey(Key userKey, String restKey, String keyType) {
        if (keyType.equals("DIGEST")) {
            byte[] restDigest = Base64.getUrlDecoder().decode(restKey);
            return Arrays.equals(restDigest, userKey.digest);
        } else if (keyType.equals("BYTES")) {
            byte[] restBytes = Base64.getUrlDecoder().decode(restKey);
            BytesValue keyBytesVal = (BytesValue) userKey.userKey;
            byte[] recVal = (byte[]) keyBytesVal.getObject();
            return Arrays.equals(restBytes, recVal);
        }
        return restKey.equals(userKey.userKey.toString());
    }

    private boolean compareDigests(String restDigest, Key userKey) {
        byte[] restBytes = Base64.getUrlDecoder().decode(restDigest);
        return Arrays.equals(restBytes, userKey.digest);
    }

}

/*
 * The handler interface performs a batch request via a json string, and returns a List<Map<String, Object>>
 * Implementations are provided for specifying JSON and MsgPack as return formats
 */
interface BatchHandler {
    Map<String, Object> perform(MockMvc mockMVC, String testEndpoint, String payload) throws Exception;
}

class MsgPackBatchHandler implements BatchHandler {

    final ObjectMapper msgPackMapper;

    public MsgPackBatchHandler() {
        msgPackMapper = MsgPackConverter.getASMsgPackObjectMapper();
    }

    private Map<String, Object> getReturnedBatches(MockHttpServletResponse res) {
        byte[] response = res.getContentAsByteArray();
        TypeReference<Map<String, Object>> btype = new TypeReference<>() {
        };
        Map<String, Object> batchResponse = null;
        try {
            batchResponse = msgPackMapper.readValue(response, btype);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return batchResponse;
    }

    @Override
    public Map<String, Object> perform(MockMvc mockMVC, String testEndpoint, String payload) throws Exception {

        MockHttpServletResponse res = mockMVC.perform(post(testEndpoint).contentType(MediaType.APPLICATION_JSON)
                .accept("application/msgpack")
                .content(payload)).andExpect(status().isOk()).andReturn().getResponse();

        return getReturnedBatches(res);
    }

}

class JSONBatchHandler implements BatchHandler {

    final ObjectMapper msgPackMapper;

    public JSONBatchHandler() {
        msgPackMapper = JSONMessageConverter.getJSONObjectMapper();
    }

    private Map<String, Object> getReturnedBatches(MockHttpServletResponse res) {
        String response = null;
        try {
            response = res.getContentAsString();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        TypeReference<Map<String, Object>> btype = new TypeReference<>() {
        };
        Map<String, Object> batchResponse = null;
        try {
            batchResponse = msgPackMapper.readValue(response, btype);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return batchResponse;
    }

    @Override
    public Map<String, Object> perform(MockMvc mockMVC, String testEndpoint, String payload) throws Exception {

        MockHttpServletResponse res = mockMVC.perform(post(testEndpoint).contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(payload)).andReturn().getResponse();

        int status = res.getStatus();
        if (status != 200) {
            Assert.fail(
                    String.format("Status expected:200 but was:%d\n Response: %s", status, res.getContentAsString()));
        }

        return getReturnedBatches(res);
    }
}
