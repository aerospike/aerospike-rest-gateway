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
package com.aerospike.restclient;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.BatchRead;
import com.aerospike.client.Bin;
import com.aerospike.client.Key;
import com.aerospike.client.Value.BytesValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/*
 * A batch Read sent to the server looks like:
 * {
 *  "key": {"set":"..", "nameSpace": "...", "userKey":..., "keytype":..., "digest":..}
 *  "binNames": ["a", "b", "c"],
 *  "readAllBins: boolean
 * }
 */
public class BatchReadCorrectTests {

    private RestBatchReadComparator batchComparator;
    private BatchHandler batchHandler;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMVC;

    @Autowired
    private AerospikeClient client;

    @Autowired
    private WebApplicationContext wac;

    private final Key[] keys = new Key[100];
    private final Key intKey = new Key("test", "junit", 1);
    private final Key bytesKey = new Key("test", "junit", new byte[]{1, 127, 127, 1});

    TypeReference<List<Map<String, Object>>> batchReturnType = new TypeReference<List<Map<String, Object>>>() {
    };
    private final String endpoint = "/v1/batch";

    @BeforeEach
    public void setup() {
        mockMVC = MockMvcBuilders.webAppContextSetup(wac).build();
        for (int i = 0; i < keys.length; i++) {
            keys[i] = new Key("test", "junit", Integer.toString(i));
            Bin bin1 = new Bin("bin1", i);
            Bin bin2 = new Bin("bin2", i * 2);
            Bin bin3 = new Bin("bin3", i * 3);
            client.put(null, keys[i], bin1, bin2, bin3);
        }

        client.put(null, intKey, new Bin("keytype", "integer"));
        client.put(null, bytesKey, new Bin("keytype", "bytes"));
    }

    @AfterEach
    public void clean() {
        for (Key key : keys) {
            client.delete(null, key);
        }
        client.delete(null, intKey);
        client.delete(null, bytesKey);
    }

    private static Stream<Arguments> mappers() {
        return Stream.of(
                Arguments.of(new RestBatchReadComparator(), new JSONBatchHandler()),
                Arguments.of(new RestBatchReadComparator(), new MsgPackBatchHandler())
        );
    }

    @ParameterizedTest
    @MethodSource("mappers")
    void addMappers(RestBatchReadComparator comparator, BatchHandler handler) {
        this.batchComparator = comparator;
        this.batchHandler = handler;
    }

    @Test
    public void testGetExistingRecords() throws Exception {
        List<Map<String, Object>> batchKeys = new ArrayList<>();
        List<BatchRead> batchRecs = new ArrayList<>();

        batchKeys.add(keyToBatchObject(keys[0], null));
        batchKeys.add(keyToBatchObject(keys[1], null));
        batchKeys.add(keyToBatchObject(keys[2], null));

        batchRecs.add(new BatchRead(keys[0], true));
        batchRecs.add(new BatchRead(keys[1], true));
        batchRecs.add(new BatchRead(keys[2], true));

        String payLoad = objectMapper.writeValueAsString(batchKeys);
        List<Map<String, Object>> returnedRecords = batchHandler.perform(mockMVC, endpoint, payLoad);
        assertEquals(3, returnedRecords.size());

        client.get(null, batchRecs);
        assertTrue(compareRestRecordsToBatchReads(returnedRecords, batchRecs));
    }

    @Test
    public void testGetExistingRecordsBinFilter() throws Exception {
        List<Map<String, Object>> batchKeys = new ArrayList<>();
        List<BatchRead> batchRecs = new ArrayList<>();
        String[] bins = new String[]{"bin1", "bin3"};

        batchKeys.add(keyToBatchObject(keys[0], bins));
        batchKeys.add(keyToBatchObject(keys[1], bins));
        batchKeys.add(keyToBatchObject(keys[2], bins));

        batchRecs.add(new BatchRead(keys[0], bins));
        batchRecs.add(new BatchRead(keys[1], bins));
        batchRecs.add(new BatchRead(keys[2], bins));

        String payLoad = objectMapper.writeValueAsString(batchKeys);
        List<Map<String, Object>> returnedRecords = batchHandler.perform(mockMVC, endpoint, payLoad);
        assertEquals(3, returnedRecords.size());

        client.get(null, batchRecs);
        assertTrue(compareRestRecordsToBatchReads(returnedRecords, batchRecs));
    }

    @Test
    public void testBatchGetWithNonExistentRecord() throws Exception {
        List<Map<String, Object>> batchKeys = new ArrayList<>();
        List<BatchRead> batchRecs = new ArrayList<>();
        batchKeys.add(keyToBatchObject(keys[0], null));
        batchKeys.add(keyToBatchObject(keys[1], null));
        batchKeys.add(keyToBatchObject(new Key("test", "demo", "notreal"), null));
        batchKeys.add(keyToBatchObject(keys[2], null));

        batchRecs.add(new BatchRead(keys[0], true));
        batchRecs.add(new BatchRead(keys[1], true));
        batchRecs.add(new BatchRead(new Key("test", "demo", "notreal"), true));
        batchRecs.add(new BatchRead(keys[2], true));

        String payLoad = objectMapper.writeValueAsString(batchKeys);
        List<Map<String, Object>> returnedRecords = batchHandler.perform(mockMVC, endpoint, payLoad);
        assertEquals(4, returnedRecords.size());

        client.get(null, batchRecs);
        assertTrue(compareRestRecordsToBatchReads(returnedRecords, batchRecs));
    }

    @Test
    public void testGetExistingRecordsWithIntKey() throws Exception {
        List<Map<String, Object>> batchKeys = new ArrayList<>();
        List<BatchRead> batchRecs = new ArrayList<>();
        batchKeys.add(keyToBatchObject(keys[0], null));
        batchKeys.add(keyToBatchObject(keys[1], null));
        batchKeys.add(keyToBatchObject(keys[2], null));
        batchKeys.add(intKeyToBatchObject(intKey, null));

        batchRecs.add(new BatchRead(keys[0], true));
        batchRecs.add(new BatchRead(keys[1], true));
        batchRecs.add(new BatchRead(keys[2], true));
        batchRecs.add(new BatchRead(intKey, true));

        String payLoad = objectMapper.writeValueAsString(batchKeys);
        List<Map<String, Object>> returnedRecords = batchHandler.perform(mockMVC, endpoint, payLoad);
        assertEquals(4, returnedRecords.size());

        client.get(null, batchRecs);
        assertTrue(compareRestRecordsToBatchReads(returnedRecords, batchRecs));
    }

    @Test
    public void testGetExistingRecordsWithBytesKey() throws Exception {
        List<Map<String, Object>> batchKeys = new ArrayList<>();
        List<BatchRead> batchRecs = new ArrayList<>();
        batchKeys.add(bytesKeyToBatchObject(bytesKey, null));
        batchRecs.add(new BatchRead(bytesKey, true));

        String payLoad = objectMapper.writeValueAsString(batchKeys);
        List<Map<String, Object>> returnedRecords = batchHandler.perform(mockMVC, endpoint, payLoad);
        assertEquals(1, returnedRecords.size());

        client.get(null, batchRecs);
        assertTrue(compareRestRecordsToBatchReads(returnedRecords, batchRecs));
    }

    @Test
    public void testGetExistingRecordsWithDigestKey() throws Exception {
        List<Map<String, Object>> batchKeys = new ArrayList<>();
        List<BatchRead> batchRecs = new ArrayList<>();
        batchKeys.add(digestKeyToBatchObject(keys[0], null));
        batchRecs.add(new BatchRead(keys[0], true));

        String payLoad = objectMapper.writeValueAsString(batchKeys);
        List<Map<String, Object>> returnedRecords = batchHandler.perform(mockMVC, endpoint, payLoad);
        assertEquals(1, returnedRecords.size());

        client.get(null, batchRecs);
        assertTrue(compareRestRecordsToBatchReads(returnedRecords, batchRecs));
    }

    private Map<String, Object> keyToBatchObject(Key key, String[] bins) {
        Map<String, Object> restKey = new HashMap<>();
        Map<String, Object> batchObj = new HashMap<>();
        restKey.put("namespace", key.namespace);
        restKey.put("setName", key.setName);
        restKey.put("userKey", key.userKey.toString());
        if (bins != null) {
            batchObj.put("binNames", bins);
        } else {
            batchObj.put("readAllBins", true);
        }
        batchObj.put("key", restKey);
        return batchObj;
    }

    private Map<String, Object> intKeyToBatchObject(Key key, String[] bins) {
        Map<String, Object> restKey = new HashMap<>();
        Map<String, Object> batchObj = new HashMap<>();

        restKey.put("namespace", key.namespace);
        restKey.put("setName", key.setName);
        restKey.put("userKey", (key.userKey).toString());
        restKey.put("keytype", "INTEGER");
        if (bins != null) {
            batchObj.put("bins", bins);
        } else {
            batchObj.put("readAllBins", true);
        }
        batchObj.put("key", restKey);
        return batchObj;
    }

    private Map<String, Object> bytesKeyToBatchObject(Key key, String[] bins) {
        Map<String, Object> restKey = new HashMap<>();
        BytesValue value = ((BytesValue) key.userKey);

        String urlBytes = Base64.getUrlEncoder().encodeToString((byte[]) value.getObject());
        Map<String, Object> batchObj = new HashMap<>();

        restKey.put("namespace", key.namespace);
        restKey.put("setName", key.setName);
        restKey.put("userKey", urlBytes);
        restKey.put("keytype", "BYTES");

        if (bins != null) {
            batchObj.put("bins", bins);
        } else {
            batchObj.put("readAllBins", true);
        }
        batchObj.put("key", restKey);
        return batchObj;
    }

    private Map<String, Object> digestKeyToBatchObject(Key key, String[] bins) {
        Map<String, Object> restKey = new HashMap<>();

        String urlDigest = Base64.getUrlEncoder().encodeToString(key.digest);
        Map<String, Object> batchObj = new HashMap<>();

        restKey.put("namespace", key.namespace);
        restKey.put("setName", key.setName);
        restKey.put("userKey", urlDigest);
        restKey.put("keytype", "DIGEST");
        if (bins != null) {
            batchObj.put("bins", bins);
        } else {
            batchObj.put("readAllBins", true);
        }
        batchObj.put("key", restKey);
        return batchObj;
    }

    private boolean compareRestRecordsToBatchReads(List<Map<String, Object>> returnedRecords,
                                                   List<BatchRead> batchReads) {
        if (batchReads.size() != returnedRecords.size()) {
            return false;
        }
        for (int i = 0; i < batchReads.size(); i++) {
            if (!batchComparator.compareRestBatchReadToBatchRead(returnedRecords.get(i),
                    batchReads.get(i))) {
                return false;
            }
        }
        return true;
    }
}

class RestBatchReadComparator {

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
            return ASTestUtils.compareRCRecordToASRecord((Map<String, Object>) restRecord.get("record"), batchRead.record);
        } else {
            return batchRead.record == null;
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
    List<Map<String, Object>> perform(MockMvc mockMVC, String testEndpoint, String payload) throws Exception;
}

class MsgPackBatchHandler implements BatchHandler {

    ObjectMapper msgPackMapper;

    public MsgPackBatchHandler() {
        msgPackMapper = new ObjectMapper(new MessagePackFactory());
    }

    private List<Map<String, Object>> getReturnedBatches(MockHttpServletResponse res) {
        byte[] response = res.getContentAsByteArray();
        TypeReference<List<Map<String, Object>>> bType = new TypeReference<List<Map<String, Object>>>() {
        };
        List<Map<String, Object>> batchResponse = null;
        try {
            batchResponse = msgPackMapper.readValue(response, bType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return batchResponse;
    }

    @Override
    public List<Map<String, Object>> perform(MockMvc mockMVC, String testEndpoint, String payload)
            throws Exception {

        MockHttpServletResponse res = mockMVC.perform(post(testEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/msgpack")
                .content(payload)).andExpect(status().isOk())
                .andReturn().getResponse();

        return getReturnedBatches(res);
    }
}

class JSONBatchHandler implements BatchHandler {

    ObjectMapper msgPackMapper;

    public JSONBatchHandler() {
        msgPackMapper = new ObjectMapper();
    }

    private List<Map<String, Object>> getReturnedBatches(MockHttpServletResponse res) {
        String response = null;
        try {
            response = res.getContentAsString();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }

        TypeReference<List<Map<String, Object>>> bType = new TypeReference<List<Map<String, Object>>>() {
        };
        List<Map<String, Object>> batchResponse = null;
        try {
            batchResponse = msgPackMapper.readValue(response, bType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return batchResponse;
    }

    @Override
    public List<Map<String, Object>> perform(MockMvc mockMVC, String testEndpoint, String payload)
            throws Exception {

        MockHttpServletResponse res = mockMVC.perform(post(testEndpoint)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(payload)).andExpect(status().isOk())
                .andReturn().getResponse();
        return getReturnedBatches(res);
    }
}
