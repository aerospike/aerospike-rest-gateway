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
package com.aerospike.restclient.controller;

import com.aerospike.client.policy.Policy;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.restclient.controllers.KeyValueController;
import com.aerospike.restclient.service.AerospikeRecordService;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.ByteArrayInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@RunWith(Parameterized.class)
@SpringBootTest
public class KVControllerV1KeyTypeTests {

    @ClassRule
    public static final SpringClassRule springClassRule = new SpringClassRule();
    @Rule
    public final SpringMethodRule springMethodRule = new SpringMethodRule();

    @Autowired
    KeyValueController controller;
    @MockBean
    AerospikeRecordService recordService;

    private final String ns = "test";
    private final String set = "set";
    private final String key = "key";

    private Map<String, Object> dummyBins;
    private Map<String, String> queryParams;
    private MultiValueMap<String, String> multiQueryParams;
    private final RecordKeyType expectedKeyType;

    private byte[] msgpackBins;
    private final ObjectMapper mpMapper = new ObjectMapper(new MessagePackFactory());

    @Parameters
    public static Object[] keyType() {
        return new Object[]{
                RecordKeyType.STRING, RecordKeyType.BYTES, RecordKeyType.DIGEST, RecordKeyType.INTEGER, null
        };
    }

    public KVControllerV1KeyTypeTests(RecordKeyType keyType) {
        this.expectedKeyType = keyType;
    }

    @Before
    public void setup() throws JsonProcessingException {
        dummyBins = new HashMap<>();
        dummyBins.put("bin", "a");
        msgpackBins = mpMapper.writeValueAsBytes(dummyBins);
        queryParams = new HashMap<>();
        multiQueryParams = new LinkedMultiValueMap<>();

        if (expectedKeyType != null) {
            queryParams.put(AerospikeAPIConstants.KEY_TYPE, expectedKeyType.toString());
            multiQueryParams.put(AerospikeAPIConstants.KEY_TYPE, Collections.singletonList(expectedKeyType.toString()));
        }
    }

    /* UPDATE */
    @Test
    public void testKeyTypeForUpdateNSSetKey() {
        controller.updateRecordNamespaceSetKey(ns, set, key, dummyBins, queryParams, null);

        verify(recordService, Mockito.only()).storeRecord(isNull(), eq(ns), eq(set), eq(key), eq(dummyBins),
                eq(expectedKeyType), isA(WritePolicy.class));
    }

    @Test
    public void testRecordKeyTypeForUpdateNSKey() {
        controller.updateRecordNamespaceKey(ns, key, dummyBins, queryParams, null);

        verify(recordService, Mockito.only()).storeRecord(isNull(), eq(ns), isNull(), eq((key)), eq(dummyBins),
                eq(expectedKeyType), isA(WritePolicy.class));
    }

    @Test
    public void testRecordKeyTypeForUpdateNSSetKeyMP() {
        controller.updateRecordNamespaceSetKeyMP(ns, set, key, new ByteArrayInputStream(msgpackBins), queryParams,
                null);

        verify(recordService, Mockito.only()).storeRecord(isNull(), eq(ns), eq(set), eq(key), eq(dummyBins),
                eq(expectedKeyType), isA(WritePolicy.class));
    }

    @Test
    public void testRecordKeyTypeForUpdateNSKeyMP() {
        controller.updateRecordNamespaceKeyMP(ns, key, new ByteArrayInputStream(msgpackBins), queryParams, null);

        verify(recordService, Mockito.only()).storeRecord(isNull(), eq(ns), isNull(), eq((key)), eq(dummyBins),
                eq(expectedKeyType), isA(WritePolicy.class));
    }

    /* DELETE */
    @Test
    public void testKeyTypeDeleteNSSetKey() {
        controller.deleteRecordNamespaceSetKey(ns, set, key, queryParams, null);
        verify(recordService, Mockito.only()).deleteRecord(isNull(), any(String.class), any(String.class),
                any(String.class), eq(expectedKeyType), any(WritePolicy.class));
    }

    @Test
    public void testKeyTypeDeleteNSKey() {
        controller.deleteRecordNamespaceKey(ns, key, queryParams, null);
        verify(recordService, Mockito.only()).deleteRecord(isNull(), any(String.class), isNull(), any(String.class),
                eq(expectedKeyType), any(WritePolicy.class));
    }

    /* CREATE */
    @Test
    public void testKeyTypeCreateNSSetKey() {
        controller.createRecordNamespaceSetKey(ns, set, key, dummyBins, queryParams, null);

        verify(recordService, Mockito.only()).storeRecord(isNull(), eq(ns), eq(set), eq(key), eq(dummyBins),
                eq(expectedKeyType), isA(WritePolicy.class));
    }

    @Test
    public void testRecordKeyTypeCreateNSKey() {
        controller.createRecordNamespaceKey(ns, key, dummyBins, queryParams, null);

        verify(recordService, Mockito.only()).storeRecord(isNull(), eq(ns), isNull(), eq((key)), eq(dummyBins),
                eq(expectedKeyType), isA(WritePolicy.class));
    }

    @Test
    public void testRecordKeyTypeCreateNSSetKeyMP() {
        controller.createRecordNamespaceSetKeyMP(ns, set, key, new ByteArrayInputStream(msgpackBins), queryParams,
                null);

        verify(recordService, Mockito.only()).storeRecord(isNull(), eq(ns), eq(set), eq(key), eq(dummyBins),
                eq(expectedKeyType), isA(WritePolicy.class));
    }

    @Test
    public void testRecordKeyTypeCreateNSKeyMP() {
        controller.createRecordNamespaceKeyMP(ns, key, new ByteArrayInputStream(msgpackBins), queryParams, null);

        verify(recordService, Mockito.only()).storeRecord(isNull(), eq(ns), isNull(), eq((key)), eq(dummyBins),
                eq(expectedKeyType), isA(WritePolicy.class));
    }

    /* REPLACE */
    @Test
    public void testKeyTypeReplaceNSSetKey() {
        controller.replaceRecordNamespaceSetKey(ns, set, key, dummyBins, queryParams, null);

        verify(recordService, Mockito.only()).storeRecord(isNull(), eq(ns), eq(set), eq(key), eq(dummyBins),
                eq(expectedKeyType), isA(WritePolicy.class));
    }

    @Test
    public void testRecordKeyTypeReplaceNSKey() {
        controller.replaceRecordNamespaceKey(ns, key, dummyBins, queryParams, null);

        verify(recordService, Mockito.only()).storeRecord(isNull(), eq(ns), isNull(), eq((key)), eq(dummyBins),
                eq(expectedKeyType), isA(WritePolicy.class));
    }

    @Test
    public void testRecordKeyTypeReplaceNSSetKeyMP() {
        controller.replaceRecordNamespaceSetKeyMP(ns, set, key, new ByteArrayInputStream(msgpackBins), queryParams,
                null);

        verify(recordService, Mockito.only()).storeRecord(isNull(), eq(ns), eq(set), eq(key), eq(dummyBins),
                eq(expectedKeyType), isA(WritePolicy.class));
    }

    @Test
    public void testRecordKeyTypeReplaceNSKeyMP() {
        controller.replaceRecordNamespaceKeyMP(ns, key, new ByteArrayInputStream(msgpackBins), queryParams, null);

        verify(recordService, Mockito.only()).storeRecord(isNull(), eq(ns), isNull(), eq((key)), eq(dummyBins),
                eq(expectedKeyType), isA(WritePolicy.class));
    }

    /*GET */
    @Test
    public void testKeyTypeForNSSetKey() {
        controller.getRecordNamespaceSetKey(ns, set, key, multiQueryParams, null);
        verify(recordService, Mockito.only()).fetchRecord(isNull(), any(String.class), any(String.class),
                any(String.class), any(String[].class), eq(expectedKeyType), isA(Policy.class));
    }

    @Test
    public void testKeyTypeForNSKey() {
        controller.getRecordNamespaceKey(ns, key, multiQueryParams, null);
        verify(recordService, Mockito.only()).fetchRecord(isNull(), any(String.class), isNull(), any(String.class),
                any(String[].class), eq(expectedKeyType), isA(Policy.class));
    }
}
