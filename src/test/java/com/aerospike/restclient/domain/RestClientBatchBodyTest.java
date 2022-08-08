/*
 * Copyright 2019 Aerospike, Inc.
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
package com.aerospike.restclient.domain;

import com.aerospike.client.*;
import com.aerospike.client.policy.CommitLevel;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.restclient.ASJsonTestMapper;
import com.aerospike.restclient.ASMsgPackTestMapper;
import com.aerospike.restclient.ASTestMapper;
import com.aerospike.restclient.ASTestUtils;
import com.aerospike.restclient.domain.batchmodels.*;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import com.aerospike.restclient.util.AerospikeOperation;
import com.aerospike.restclient.util.RestClientErrors;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RunWith(Parameterized.class)
public class RestClientBatchBodyTest {

    private final ASTestMapper mapper;
    private final static String ns = "test";
    private final static String set = "set";

    @Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JsonRestClientBatchRecordBodyMapper(new ObjectMapper(new JsonFactory())),
                new MsgPackRestClientBatchRecordBodyMapper(new ObjectMapper(new MessagePackFactory()))
        };
    }

    public RestClientBatchBodyTest(ASTestMapper mapper) {
        this.mapper = mapper;
    }

    /*
     * BatchRead specific tests
     */
    @Test
    public void testReadNoArgConstructor() {
        new RestClientBatchReadBody();
    }

    @Test
    public void testObjectMappedBatchReadConstructionStringKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("key", RecordKeyType.STRING);
        Map<String, Object> batchMap = getBatchReadBase();
        batchMap.put("key", keyMap);

        RestClientBatchReadBody mappedBody = (RestClientBatchReadBody) mapper.mapToObject(batchMap);

        Assert.assertTrue(mappedBody.readAllBins);
        Assert.assertArrayEquals(mappedBody.binNames, new String[]{});
        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.STRING, rcKey.keyType);
    }

    @Test
    public void testObjectMappedBatchReadConstructionWithBins() throws Exception {
        Map<String, Object> keyMap = getKeyMap("key", RecordKeyType.STRING);
        Map<String, Object> batchMap = getBatchReadBase();
        batchMap.put("key", keyMap);
        String[] bins = {"a", "b", "c"};
        batchMap.put("binNames", bins);

        RestClientBatchReadBody mappedBody = (RestClientBatchReadBody) mapper.mapToObject(batchMap);

        Assert.assertTrue(mappedBody.readAllBins);
        Assert.assertArrayEquals(mappedBody.binNames, bins);
        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.STRING, rcKey.keyType);
    }

    @Test
    public void testObjectMappedBatchReadConstructionIntegerKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap(5, RecordKeyType.INTEGER);
        Map<String, Object> batchMap = getBatchReadBase();
        batchMap.put("key", keyMap);

        RestClientBatchReadBody mappedBody = (RestClientBatchReadBody) mapper.mapToObject(batchMap);

        Assert.assertTrue(mappedBody.readAllBins);
        Assert.assertArrayEquals(mappedBody.binNames, new String[]{});
        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.INTEGER, rcKey.keyType);
    }

    @Test
    public void testObjectMappedBatchReadConstructionBytesKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("lookslikebytes", RecordKeyType.BYTES);
        Map<String, Object> batchMap = getBatchReadBase();
        batchMap.put("key", keyMap);

        RestClientBatchReadBody mappedBody = (RestClientBatchReadBody) mapper.mapToObject(batchMap);

        Assert.assertTrue(mappedBody.readAllBins);
        Assert.assertArrayEquals(mappedBody.binNames, new String[]{});
        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.BYTES, rcKey.keyType);
    }

    @Test
    public void testObjectMappedBatchReadConstructionDigestKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("digest", RecordKeyType.DIGEST);
        Map<String, Object> batchMap = getBatchReadBase();
        batchMap.put("key", keyMap);

        RestClientBatchReadBody mappedBody = (RestClientBatchReadBody) mapper.mapToObject(batchMap);

        Assert.assertTrue(mappedBody.readAllBins);
        Assert.assertArrayEquals(mappedBody.binNames, new String[]{});
        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.DIGEST, rcKey.keyType);
    }

    @Test
    public void testConversionToBatchReadWithBinNames() {
        Key realKey = new Key(ns, set, "test");
        RestClientKey rcKey = new RestClientKey(realKey);
        String[] bins = {"b1", "b2", "b3"};
        RestClientBatchReadBody rCBRB = new RestClientBatchReadBody();

        rCBRB.binNames = bins;
        rCBRB.key = rcKey;
        rCBRB.readAllBins = false;

        BatchRead convertedBatchRead = rCBRB.toBatchRecord();

        Assert.assertFalse(convertedBatchRead.readAllBins);
        Assert.assertTrue(ASTestUtils.compareKeys(realKey, convertedBatchRead.key));
        Assert.assertArrayEquals(bins, convertedBatchRead.binNames);
    }

    @Test
    public void testConversionToBatchReadWithoutBinNames() {
        Key realKey = new Key(ns, set, "test");
        RestClientKey rcKey = new RestClientKey(realKey);
        String[] bins = {"b1", "b2", "b3"};
        RestClientBatchReadBody rCBRB = new RestClientBatchReadBody();

        rCBRB.binNames = bins;
        rCBRB.key = rcKey;
        rCBRB.readAllBins = true;

        BatchRead convertedBatchRead = rCBRB.toBatchRecord();

        Assert.assertTrue(convertedBatchRead.readAllBins);
        Assert.assertTrue(ASTestUtils.compareKeys(realKey, convertedBatchRead.key));
        Assert.assertNull(convertedBatchRead.binNames);
    }

    @Test
    public void testConversionToBatchReadWithOnlyKeySet() {
        Key realKey = new Key(ns, set, "test");
        RestClientKey rcKey = new RestClientKey(realKey);
        RestClientBatchReadBody rCBRB = new RestClientBatchReadBody();

        rCBRB.key = rcKey;

        BatchRead convertedBatchRead = rCBRB.toBatchRecord();

        Assert.assertFalse(convertedBatchRead.readAllBins);
        Assert.assertTrue(ASTestUtils.compareKeys(realKey, convertedBatchRead.key));
        Assert.assertNull(convertedBatchRead.binNames);
    }

    @Test(expected = RestClientErrors.InvalidKeyError.class)
    public void testConversionWithNullKey() {
        RestClientBatchReadBody rCBRB = new RestClientBatchReadBody();
        rCBRB.key = null;
        rCBRB.toBatchRecord();
    }

    /*
     * BatchWrite specific tests
     */

    @Test
    public void testWriteNoArgConstructor() {
        new RestClientBatchWriteBody();
    }

    @Test
    public void testObjectMappedBatchWriteConstructionStringKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("key", RecordKeyType.STRING);
        Map<String, Object> batchMap = getBatchWriteBase();
        batchMap.put("key", keyMap);

        RestClientBatchWriteBody mappedBody = (RestClientBatchWriteBody) mapper.mapToObject(batchMap);

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.STRING, rcKey.keyType);
        Assert.assertEquals(mappedBody.opsList, new ArrayList<>());
    }

    @Test
    public void testObjectMappedBatchWriteConstructionWithOps() throws Exception {
        Map<String, Object> keyMap = getKeyMap("key", RecordKeyType.STRING);
        Map<String, Object> expectedBatchMap = getBatchWriteBase();
        List<Map<String, Object>> expectedOpsListMap = new ArrayList<>();

        Map<String, Object> op1 = new HashMap<>();
        Map<String, Object> op1Vals = new HashMap<>();
        op1.put("operation", AerospikeOperation.ADD);
        op1Vals.put("bin", "bin1");
        op1Vals.put("incr", 1);
        op1.put("opValues", op1Vals);

        Map<String, Object> op2 = new HashMap<>();
        Map<String, Object> op2Vals = new HashMap<>();
        op2.put("operation", AerospikeOperation.PUT);
        op2Vals.put("bin", "bin2");
        op2Vals.put("value", "new val");
        op2.put("opValues", op2Vals);

        expectedOpsListMap.add(op1);
        expectedOpsListMap.add(op2);
        expectedBatchMap.put("key", keyMap);
        expectedBatchMap.put("opsList", expectedOpsListMap);

        RestClientBatchWriteBody actualBody = (RestClientBatchWriteBody) mapper.mapToObject(expectedBatchMap);
        List<Map<String, Object>> actualBodyOpsList = actualBody.opsList.stream().map(RestClientOperation::toMap)
                .collect(Collectors.toList());
        RestClientKey actualKey = actualBody.key;


        Assert.assertTrue(ASTestUtils.compareCollection(expectedOpsListMap, actualBodyOpsList));
        Assert.assertEquals(RecordKeyType.STRING, actualKey.keyType);
    }

    @Test
    public void testObjectMappedBatchWriteConstructionWithPolicy() throws Exception {
        Map<String, Object> keyMap = getKeyMap("key", RecordKeyType.STRING);
        Map<String, Object> batchWritePolicy = new HashMap<>();
        List<Map<String, Object>> expectedOpsListMap = new ArrayList<>();
        Map<String, Object> expectedBatchMap = getBatchWriteBase();

        batchWritePolicy.put("recordExistsAction", "UPDATE");

        expectedBatchMap.put("key", keyMap);
        expectedBatchMap.put("policy", batchWritePolicy);

        RestClientBatchWriteBody actualBody = (RestClientBatchWriteBody) mapper.mapToObject(expectedBatchMap);
        List<Map<String, Object>> actualBodyOpsList = actualBody.opsList.stream().map(RestClientOperation::toMap)
                .collect(Collectors.toList());
        RestClientKey actualKey = actualBody.key;

        Assert.assertEquals(actualBody.policy.recordExistsAction, RecordExistsAction.UPDATE);
        Assert.assertTrue(ASTestUtils.compareCollection(expectedOpsListMap, actualBodyOpsList));
        Assert.assertEquals(RecordKeyType.STRING, actualKey.keyType);
    }

    @Test
    public void testObjectMappedBatchWriteConstructionIntegerKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap(5, RecordKeyType.INTEGER);
        Map<String, Object> batchMap = getBatchWriteBase();
        batchMap.put("key", keyMap);

        RestClientBatchWriteBody mappedBody = (RestClientBatchWriteBody) mapper.mapToObject(batchMap);

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(mappedBody.opsList, new ArrayList<>());
        Assert.assertEquals(RecordKeyType.INTEGER, rcKey.keyType);
    }

    @Test
    public void testObjectMappedBatchWriteConstructionBytesKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("lookslikebytes", RecordKeyType.BYTES);
        Map<String, Object> batchMap = getBatchWriteBase();
        batchMap.put("key", keyMap);

        RestClientBatchWriteBody mappedBody = (RestClientBatchWriteBody) mapper.mapToObject(batchMap);

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.BYTES, rcKey.keyType);
        Assert.assertEquals(mappedBody.opsList, new ArrayList<>());
    }

    @Test
    public void testObjectMappedBatchWriteConstructionDigestKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("digest", RecordKeyType.DIGEST);
        Map<String, Object> batchMap = getBatchWriteBase();
        batchMap.put("key", keyMap);

        RestClientBatchWriteBody mappedBody = (RestClientBatchWriteBody) mapper.mapToObject(batchMap);

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.DIGEST, rcKey.keyType);
        Assert.assertEquals(mappedBody.opsList, new ArrayList<>());
    }

    @Test
    public void testConversionToBatchWriteWithOps() {
        Key realKey = new Key(ns, set, "test");
        RestClientKey rcKey = new RestClientKey(realKey);
        List<RestClientOperation> opsList = new ArrayList<>();

        Map<String, Object> op1Vals = new HashMap<>();
        op1Vals.put("bin", "bin1");
        op1Vals.put("incr", 1);

        Map<String, Object> op2Vals = new HashMap<>();
        op2Vals.put("bin", "bin2");
        op2Vals.put("value", "new val");

        opsList.add(new RestClientOperation(AerospikeOperation.ADD, op1Vals));
        opsList.add(new RestClientOperation(AerospikeOperation.PUT, op2Vals));

        RestClientBatchWriteBody rCBRB = new RestClientBatchWriteBody();

        rCBRB.key = rcKey;
        rCBRB.opsList = opsList;

        BatchWrite convertedBatchWrite = rCBRB.toBatchRecord();

        Assert.assertTrue(ASTestUtils.compareKeys(realKey, convertedBatchWrite.key));
        Assert.assertNull(convertedBatchWrite.policy);

        Operation op1 = convertedBatchWrite.ops[0];
        Operation op2 = convertedBatchWrite.ops[1];
        Assert.assertEquals(convertedBatchWrite.ops[0].type, Operation.Type.ADD);
        Assert.assertEquals(convertedBatchWrite.ops[0].binName, "bin1");
        Assert.assertEquals(convertedBatchWrite.ops[0].value, Value.get(1));
        Assert.assertEquals(convertedBatchWrite.ops[1].type, Operation.Type.WRITE);
        Assert.assertEquals(convertedBatchWrite.ops[1].binName, "bin2");
        Assert.assertEquals(convertedBatchWrite.ops[1].value, Value.get("new val"));
    }

    /*
     * BatchUDF specific tests
     */

    private final static String udfPkg = "test-package-name";
    private final static String udfFunc = "test-function-name";

    @Test
    public void testUDFNoArgConstructor() {
        new RestClientBatchUDFPolicy();
    }

    @Test
    public void testObjectMappedBatchUDFConstructionStringKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("key", RecordKeyType.STRING);
        Map<String, Object> batchMap = getBatchUDFBase();
        batchMap.put("key", keyMap);

        RestClientBatchUDFBody mappedBody = (RestClientBatchUDFBody) mapper.mapToObject(batchMap);

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.STRING, rcKey.keyType);
        Assert.assertEquals(udfPkg, mappedBody.packageName);
        Assert.assertEquals(udfFunc, mappedBody.functionName);
        Assert.assertNull(mappedBody.functionArgs);
        Assert.assertNull(mappedBody.policy);
    }

    @Test
    public void testObjectMappedBatchUDFConstructionWithFunctionArgs() throws Exception {
        Map<String, Object> keyMap = getKeyMap("key", RecordKeyType.STRING);
        Map<String, Object> batchMap = getBatchUDFBase();
        List<Object> expectedFunctionArgs = new ArrayList<>();
        expectedFunctionArgs.add("str");
        expectedFunctionArgs.add(1);
        expectedFunctionArgs.add(1.2);
        expectedFunctionArgs.add(true);

        batchMap.put("key", keyMap);
        batchMap.put("functionArgs", expectedFunctionArgs);

        RestClientBatchUDFBody mappedBody = (RestClientBatchUDFBody) mapper.mapToObject(batchMap);

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.STRING, rcKey.keyType);
        Assert.assertEquals(udfPkg, mappedBody.packageName);
        Assert.assertEquals(udfFunc, mappedBody.functionName);
        Assert.assertTrue(ASTestUtils.compareCollection(mappedBody.functionArgs, expectedFunctionArgs));
        Assert.assertNull(mappedBody.policy);
    }

    @Test
    public void testObjectMappedBatchUDFConstructionWithPolicy() throws Exception {
        Map<String, Object> keyMap = getKeyMap("key", RecordKeyType.STRING);
        Map<String, Object> batchUDFPolicy = new HashMap<>();
        List<Map<String, Object>> expectedOpsListMap = new ArrayList<Map<String, Object>>();
        Map<String, Object> expectedBatchMap = getBatchUDFBase();

        batchUDFPolicy.put("sendKey", true);
        expectedBatchMap.put("key", keyMap);
        expectedBatchMap.put("policy", batchUDFPolicy);

        RestClientBatchUDFBody actualBody = (RestClientBatchUDFBody) mapper.mapToObject(expectedBatchMap);
        RestClientKey actualKey = actualBody.key;

        Assert.assertTrue(actualBody.policy.sendKey);
        Assert.assertEquals(RecordKeyType.STRING, actualKey.keyType);
    }

    @Test
    public void testObjectMappedBatchUDFConstructionIntegerKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap(5, RecordKeyType.INTEGER);
        Map<String, Object> batchMap = getBatchUDFBase();
        batchMap.put("key", keyMap);

        RestClientBatchUDFBody mappedBody = (RestClientBatchUDFBody) mapper.mapToObject(batchMap);

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.INTEGER, rcKey.keyType);
    }

    @Test
    public void testObjectMappedBatchUDFConstructionBytesKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("lookslikebytes", RecordKeyType.BYTES);
        Map<String, Object> batchMap = getBatchUDFBase();
        batchMap.put("key", keyMap);

        RestClientBatchUDFBody mappedBody = (RestClientBatchUDFBody) mapper.mapToObject(batchMap);

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.BYTES, rcKey.keyType);
    }

    @Test
    public void testObjectMappedBatchUDFConstructionDigestKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("digest", RecordKeyType.DIGEST);
        Map<String, Object> batchMap = getBatchUDFBase();
        batchMap.put("key", keyMap);

        RestClientBatchUDFBody mappedBody = (RestClientBatchUDFBody) mapper.mapToObject(batchMap);

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.DIGEST, rcKey.keyType);
    }

    @Test
    public void testDeleteNoArgConstructor() {
        new RestClientBatchDeletePolicy();
    }

    @Test
    public void testObjectMappedBatchDeleteConstructionStringKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("key", RecordKeyType.STRING);
        Map<String, Object> batchMap = getBatchDeleteBase();
        batchMap.put("key", keyMap);

        RestClientBatchDeleteBody mappedBody = (RestClientBatchDeleteBody) mapper.mapToObject(batchMap);

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.STRING, rcKey.keyType);
        Assert.assertNull(mappedBody.policy);
    }

    @Test
    public void testObjectMappedBatchDeleteConstructionWithFunctionArgs() throws Exception {
        Map<String, Object> keyMap = getKeyMap("key", RecordKeyType.STRING);
        Map<String, Object> batchMap = getBatchDeleteBase();

        batchMap.put("key", keyMap);

        RestClientBatchDeleteBody mappedBody = (RestClientBatchDeleteBody) mapper.mapToObject(batchMap);

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.STRING, rcKey.keyType);
        Assert.assertNull(mappedBody.policy);
    }

    @Test
    public void testObjectMappedBatchDeleteConstructionWithPolicy() throws Exception {
        Map<String, Object> keyMap = getKeyMap("key", RecordKeyType.STRING);
        Map<String, Object> batchDeletePolicy = new HashMap<>();
        Map<String, Object> expectedBatchMap = getBatchDeleteBase();

        batchDeletePolicy.put("commitLevel", "COMMIT_MASTER");
        expectedBatchMap.put("key", keyMap);
        expectedBatchMap.put("policy", batchDeletePolicy);

        RestClientBatchDeleteBody actualBody = (RestClientBatchDeleteBody) mapper.mapToObject(expectedBatchMap);
        RestClientKey actualKey = actualBody.key;

        Assert.assertEquals(actualBody.policy.commitLevel, CommitLevel.COMMIT_MASTER);
        Assert.assertEquals(RecordKeyType.STRING, actualKey.keyType);
    }

    @Test
    public void testObjectMappedBatchDeleteConstructionIntegerKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap(5, RecordKeyType.INTEGER);
        Map<String, Object> batchMap = getBatchDeleteBase();
        batchMap.put("key", keyMap);

        RestClientBatchDeleteBody mappedBody = (RestClientBatchDeleteBody) mapper.mapToObject(batchMap);

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.INTEGER, rcKey.keyType);
    }

    @Test
    public void testObjectMappedBatchDeleteConstructionBytesKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("lookslikebytes", RecordKeyType.BYTES);
        Map<String, Object> batchMap = getBatchDeleteBase();
        batchMap.put("key", keyMap);

        RestClientBatchDeleteBody mappedBody = (RestClientBatchDeleteBody) mapper.mapToObject(batchMap);

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.BYTES, rcKey.keyType);
    }

    @Test
    public void testObjectMappedBatchDeleteConstructionDigestKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("digest", RecordKeyType.DIGEST);
        Map<String, Object> batchMap = getBatchDeleteBase();
        batchMap.put("key", keyMap);

        RestClientBatchDeleteBody mappedBody = (RestClientBatchDeleteBody) mapper.mapToObject(batchMap);

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.DIGEST, rcKey.keyType);
    }

    /* HELPERS */
    private Map<String, Object> getBatchReadBase() {
        Map<String, Object> batchMap = new HashMap<>();
        batchMap.put("batchType", "READ");
        batchMap.put("readAllBins", true);
        batchMap.put("binNames", new String[]{});
        return batchMap;
    }


    private Map<String, Object> getBatchWriteBase() {
        Map<String, Object> batchMap = new HashMap<>();
        batchMap.put("batchType", "WRITE");
        batchMap.put("opsList", new ArrayList<>());
        return batchMap;
    }

    private Map<String, Object> getBatchUDFBase() {
        Map<String, Object> batchMap = new HashMap<>();
        batchMap.put("batchType", "UDF");
        batchMap.put("packageName", udfPkg);
        batchMap.put("functionName", udfFunc);
        return batchMap;
    }

    private Map<String, Object> getBatchDeleteBase() {
        Map<String, Object> batchMap = new HashMap<>();
        batchMap.put("batchType", "DELETE");
        return batchMap;
    }

    private Map<String, Object> getKeyMap(Object userKey, RecordKeyType keytype) {
        Map<String, Object> keyMap = new HashMap<>();
        keyMap.put(AerospikeAPIConstants.NAMESPACE, ns);
        keyMap.put(AerospikeAPIConstants.SETNAME, set);
        keyMap.put(AerospikeAPIConstants.USER_KEY, userKey);

        if (keytype != null) {
            keyMap.put(AerospikeAPIConstants.KEY_TYPE, keytype.toString());
        }

        return keyMap;
    }
}

class JsonRestClientBatchRecordBodyMapper extends ASJsonTestMapper {

    public JsonRestClientBatchRecordBodyMapper(ObjectMapper mapper) {
        super(mapper, RestClientBatchRecordBody.class);
    }
}

class MsgPackRestClientBatchRecordBodyMapper extends ASMsgPackTestMapper {

    public MsgPackRestClientBatchRecordBodyMapper(ObjectMapper mapper) {
        super(mapper, RestClientBatchRecordBody.class);
    }
}
