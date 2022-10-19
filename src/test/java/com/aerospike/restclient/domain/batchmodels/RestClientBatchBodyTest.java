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
package com.aerospike.restclient.domain.batchmodels;

import com.aerospike.client.Key;
import com.aerospike.client.Operation;
import com.aerospike.client.Value;
import com.aerospike.client.policy.CommitLevel;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.restclient.ASTestMapper;
import com.aerospike.restclient.ASTestUtils;
import com.aerospike.restclient.IASTestMapper;
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.config.MsgPackConverter;
import com.aerospike.restclient.domain.RestClientKey;
import com.aerospike.restclient.domain.operationmodels.AddOperation;
import com.aerospike.restclient.domain.operationmodels.OperationTypes;
import com.aerospike.restclient.domain.operationmodels.PutOperation;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import com.aerospike.restclient.util.RestClientErrors;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(Parameterized.class)
public class RestClientBatchBodyTest {

    private final IASTestMapper mapper;
    private final static String ns = "test";
    private final static String set = "set";

    @Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JsonRestClientBatchRecordBodyMapper(), new MsgPackRestClientBatchRecordBodyMapper()
        };
    }

    public RestClientBatchBodyTest(IASTestMapper mapper) {
        this.mapper = mapper;
    }

    /*
     * BatchRead specific tests
     */
    @Test
    public void testReadNoArgConstructor() {
        new BatchRead();
    }

    @Test
    public void testObjectMappedBatchReadConstructionStringKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("key", RecordKeyType.STRING);
        Map<String, Object> batchMap = getBatchReadBase();
        batchMap.put("key", keyMap);

        BatchRead mappedBody = (BatchRead) mapper.bytesToObject(mapper.objectToBytes(batchMap));

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

        BatchRead mappedBody = (BatchRead) mapper.bytesToObject(mapper.objectToBytes(batchMap));

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

        BatchRead mappedBody = (BatchRead) mapper.bytesToObject(mapper.objectToBytes(batchMap));

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

        BatchRead mappedBody = (BatchRead) mapper.bytesToObject(mapper.objectToBytes(batchMap));

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

        BatchRead mappedBody = (BatchRead) mapper.bytesToObject(mapper.objectToBytes(batchMap));

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
        BatchRead rCBRB = new BatchRead();

        rCBRB.binNames = bins;
        rCBRB.key = rcKey;
        rCBRB.readAllBins = false;

        com.aerospike.client.BatchRead convertedBatchRead = rCBRB.toBatchRecord();

        Assert.assertFalse(convertedBatchRead.readAllBins);
        Assert.assertTrue(ASTestUtils.compareKeys(realKey, convertedBatchRead.key));
        Assert.assertArrayEquals(bins, convertedBatchRead.binNames);
    }

    @Test
    public void testConversionToBatchReadWithoutBinNames() {
        Key realKey = new Key(ns, set, "test");
        RestClientKey rcKey = new RestClientKey(realKey);
        String[] bins = {"b1", "b2", "b3"};
        BatchRead rCBRB = new BatchRead();

        rCBRB.binNames = bins;
        rCBRB.key = rcKey;
        rCBRB.readAllBins = true;

        com.aerospike.client.BatchRead convertedBatchRead = rCBRB.toBatchRecord();

        Assert.assertTrue(convertedBatchRead.readAllBins);
        Assert.assertTrue(ASTestUtils.compareKeys(realKey, convertedBatchRead.key));
        Assert.assertNull(convertedBatchRead.binNames);
    }

    @Test
    public void testConversionToBatchReadWithOnlyKeySet() {
        Key realKey = new Key(ns, set, "test");
        RestClientKey rcKey = new RestClientKey(realKey);
        BatchRead rCBRB = new BatchRead();

        rCBRB.key = rcKey;

        com.aerospike.client.BatchRead convertedBatchRead = rCBRB.toBatchRecord();

        Assert.assertFalse(convertedBatchRead.readAllBins);
        Assert.assertTrue(ASTestUtils.compareKeys(realKey, convertedBatchRead.key));
        Assert.assertNull(convertedBatchRead.binNames);
    }

    @Test(expected = RestClientErrors.InvalidKeyError.class)
    public void testConversionWithNullKey() {
        BatchRead rCBRB = new BatchRead();
        rCBRB.key = null;
        rCBRB.toBatchRecord();
    }

    /*
     * BatchWrite specific tests
     */

    @Test
    public void testWriteNoArgConstructor() {
        new BatchWrite();
    }

    @Test
    public void testObjectMappedBatchWriteConstructionStringKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("key", RecordKeyType.STRING);
        Map<String, Object> batchMap = getBatchWriteBase();
        batchMap.put("key", keyMap);

        BatchWrite mappedBody = (BatchWrite) mapper.bytesToObject(mapper.objectToBytes(batchMap));

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
        op1.put("type", OperationTypes.ADD);
        op1.put("binName", "bin1");
        op1.put("incr", 1);

        Map<String, Object> op2 = new HashMap<>();
        op2.put("type", OperationTypes.PUT);
        op2.put("binName", "bin2");
        op2.put("value", "new val");

        expectedOpsListMap.add(op1);
        expectedOpsListMap.add(op2);
        expectedBatchMap.put("key", keyMap);
        expectedBatchMap.put("opsList", expectedOpsListMap);

        BatchWrite actualBody = (BatchWrite) mapper.bytesToObject(mapper.objectToBytes(expectedBatchMap));
        RestClientKey actualKey = actualBody.key;

        Assert.assertEquals(2, actualBody.opsList.size());
        Assert.assertEquals(RecordKeyType.STRING, actualKey.keyType);
    }

    @Test
    public void testObjectMappedBatchWriteConstructionWithPolicy() throws Exception {
        Map<String, Object> keyMap = getKeyMap("key", RecordKeyType.STRING);
        Map<String, Object> batchWritePolicy = new HashMap<>();
        Map<String, Object> expectedBatchMap = getBatchWriteBase();

        batchWritePolicy.put("recordExistsAction", "UPDATE");

        expectedBatchMap.put("key", keyMap);
        expectedBatchMap.put("policy", batchWritePolicy);

        BatchWrite actualBody = (BatchWrite) mapper.bytesToObject(mapper.objectToBytes(expectedBatchMap));
        RestClientKey actualKey = actualBody.key;

        Assert.assertEquals(actualBody.policy.recordExistsAction, RecordExistsAction.UPDATE);
        Assert.assertEquals(0, actualBody.opsList.size());
        Assert.assertEquals(RecordKeyType.STRING, actualKey.keyType);
    }

    @Test
    public void testObjectMappedBatchWriteConstructionIntegerKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap(5, RecordKeyType.INTEGER);
        Map<String, Object> batchMap = getBatchWriteBase();
        batchMap.put("key", keyMap);

        BatchWrite mappedBody = (BatchWrite) mapper.bytesToObject(mapper.objectToBytes(batchMap));

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(mappedBody.opsList, new ArrayList<>());
        Assert.assertEquals(RecordKeyType.INTEGER, rcKey.keyType);
    }

    @Test
    public void testObjectMappedBatchWriteConstructionBytesKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("lookslikebytes", RecordKeyType.BYTES);
        Map<String, Object> batchMap = getBatchWriteBase();
        batchMap.put("key", keyMap);

        BatchWrite mappedBody = (BatchWrite) mapper.bytesToObject(mapper.objectToBytes(batchMap));

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.BYTES, rcKey.keyType);
        Assert.assertEquals(mappedBody.opsList, new ArrayList<>());
    }

    @Test
    public void testObjectMappedBatchWriteConstructionDigestKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("digest", RecordKeyType.DIGEST);
        Map<String, Object> batchMap = getBatchWriteBase();
        batchMap.put("key", keyMap);

        BatchWrite mappedBody = (BatchWrite) mapper.bytesToObject(mapper.objectToBytes(batchMap));

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.DIGEST, rcKey.keyType);
        Assert.assertEquals(mappedBody.opsList, new ArrayList<>());
    }

    @Test
    public void testConversionToBatchWriteWithOps() {
        Key realKey = new Key(ns, set, "test");
        RestClientKey rcKey = new RestClientKey(realKey);
        List<com.aerospike.restclient.domain.operationmodels.Operation> opsList = new ArrayList<>();

        opsList.add(new AddOperation("bin1", 1));
        opsList.add(new PutOperation("bin2", "new val"));

        BatchWrite rCBRB = new BatchWrite();

        rCBRB.key = rcKey;
        rCBRB.opsList = opsList;

        com.aerospike.client.BatchWrite convertedBatchWrite = rCBRB.toBatchRecord();

        Assert.assertTrue(ASTestUtils.compareKeys(realKey, convertedBatchWrite.key));
        Assert.assertNull(convertedBatchWrite.policy);

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
        new BatchUDFPolicy();
    }

    @Test
    public void testObjectMappedBatchUDFConstructionStringKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("key", RecordKeyType.STRING);
        Map<String, Object> batchMap = getBatchUDFBase();
        batchMap.put("key", keyMap);

        BatchUDF mappedBody = (BatchUDF) mapper.bytesToObject(mapper.objectToBytes(batchMap));

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

        BatchUDF mappedBody = (BatchUDF) mapper.bytesToObject(mapper.objectToBytes(batchMap));

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
        Map<String, Object> expectedBatchMap = getBatchUDFBase();

        batchUDFPolicy.put("sendKey", true);
        expectedBatchMap.put("key", keyMap);
        expectedBatchMap.put("policy", batchUDFPolicy);

        BatchUDF actualBody = (BatchUDF) mapper.bytesToObject(mapper.objectToBytes(expectedBatchMap));
        RestClientKey actualKey = actualBody.key;

        Assert.assertTrue(actualBody.policy.sendKey);
        Assert.assertEquals(RecordKeyType.STRING, actualKey.keyType);
    }

    @Test
    public void testObjectMappedBatchUDFConstructionIntegerKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap(5, RecordKeyType.INTEGER);
        Map<String, Object> batchMap = getBatchUDFBase();
        batchMap.put("key", keyMap);

        BatchUDF mappedBody = (BatchUDF) mapper.bytesToObject(mapper.objectToBytes(batchMap));

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.INTEGER, rcKey.keyType);
    }

    @Test
    public void testObjectMappedBatchUDFConstructionBytesKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("lookslikebytes", RecordKeyType.BYTES);
        Map<String, Object> batchMap = getBatchUDFBase();
        batchMap.put("key", keyMap);

        BatchUDF mappedBody = (BatchUDF) mapper.bytesToObject(mapper.objectToBytes(batchMap));

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.BYTES, rcKey.keyType);
    }

    @Test
    public void testObjectMappedBatchUDFConstructionDigestKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("digest", RecordKeyType.DIGEST);
        Map<String, Object> batchMap = getBatchUDFBase();
        batchMap.put("key", keyMap);

        BatchUDF mappedBody = (BatchUDF) mapper.bytesToObject(mapper.objectToBytes(batchMap));

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.DIGEST, rcKey.keyType);
    }

    @Test
    public void testDeleteNoArgConstructor() {
        new BatchDeletePolicy();
    }

    @Test
    public void testObjectMappedBatchDeleteConstructionStringKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("key", RecordKeyType.STRING);
        Map<String, Object> batchMap = getBatchDeleteBase();
        batchMap.put("key", keyMap);

        BatchDelete mappedBody = (BatchDelete) mapper.bytesToObject(mapper.objectToBytes(batchMap));

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.STRING, rcKey.keyType);
        Assert.assertNull(mappedBody.policy);
    }

    @Test
    public void testObjectMappedBatchDeleteConstructionWithFunctionArgs() throws Exception {
        Map<String, Object> keyMap = getKeyMap("key", RecordKeyType.STRING);
        Map<String, Object> batchMap = getBatchDeleteBase();

        batchMap.put("key", keyMap);

        BatchDelete mappedBody = (BatchDelete) mapper.bytesToObject(mapper.objectToBytes(batchMap));

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

        BatchDelete actualBody = (BatchDelete) mapper.bytesToObject(mapper.objectToBytes(expectedBatchMap));
        RestClientKey actualKey = actualBody.key;

        Assert.assertEquals(actualBody.policy.commitLevel, CommitLevel.COMMIT_MASTER);
        Assert.assertEquals(RecordKeyType.STRING, actualKey.keyType);
    }

    @Test
    public void testObjectMappedBatchDeleteConstructionIntegerKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap(5, RecordKeyType.INTEGER);
        Map<String, Object> batchMap = getBatchDeleteBase();
        batchMap.put("key", keyMap);

        BatchDelete mappedBody = (BatchDelete) mapper.bytesToObject(mapper.objectToBytes(batchMap));

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.INTEGER, rcKey.keyType);
    }

    @Test
    public void testObjectMappedBatchDeleteConstructionBytesKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("lookslikebytes", RecordKeyType.BYTES);
        Map<String, Object> batchMap = getBatchDeleteBase();
        batchMap.put("key", keyMap);

        BatchDelete mappedBody = (BatchDelete) mapper.bytesToObject(mapper.objectToBytes(batchMap));

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.BYTES, rcKey.keyType);
    }

    @Test
    public void testObjectMappedBatchDeleteConstructionDigestKey() throws Exception {
        Map<String, Object> keyMap = getKeyMap("digest", RecordKeyType.DIGEST);
        Map<String, Object> batchMap = getBatchDeleteBase();
        batchMap.put("key", keyMap);

        BatchDelete mappedBody = (BatchDelete) mapper.bytesToObject(mapper.objectToBytes(batchMap));

        RestClientKey rcKey = mappedBody.key;

        Assert.assertEquals(RecordKeyType.DIGEST, rcKey.keyType);
    }

    /* HELPERS */
    private Map<String, Object> getBatchReadBase() {
        Map<String, Object> batchMap = new HashMap<>();
        batchMap.put("type", "READ");
        batchMap.put("readAllBins", true);
        batchMap.put("binNames", new String[]{});
        return batchMap;
    }

    private Map<String, Object> getBatchWriteBase() {
        Map<String, Object> batchMap = new HashMap<>();
        batchMap.put("type", "WRITE");
        batchMap.put("opsList", new ArrayList<>());
        return batchMap;
    }

    private Map<String, Object> getBatchUDFBase() {
        Map<String, Object> batchMap = new HashMap<>();
        batchMap.put("type", "UDF");
        batchMap.put("packageName", udfPkg);
        batchMap.put("functionName", udfFunc);
        return batchMap;
    }

    private Map<String, Object> getBatchDeleteBase() {
        Map<String, Object> batchMap = new HashMap<>();
        batchMap.put("type", AerospikeAPIConstants.BATCH_TYPE_DELETE);
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

class JsonRestClientBatchRecordBodyMapper extends ASTestMapper {

    public JsonRestClientBatchRecordBodyMapper() {
        super(JSONMessageConverter.getJSONObjectMapper(), BatchRecord.class);
    }
}

class MsgPackRestClientBatchRecordBodyMapper extends ASTestMapper {

    public MsgPackRestClientBatchRecordBodyMapper() {
        super(MsgPackConverter.getASMsgPackObjectMapper(), BatchRecord.class);
    }
}
