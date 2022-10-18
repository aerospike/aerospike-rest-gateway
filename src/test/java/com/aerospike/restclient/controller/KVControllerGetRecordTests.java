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

import com.aerospike.client.AerospikeException;
import com.aerospike.client.Record;
import com.aerospike.client.policy.Policy;
import com.aerospike.restclient.controllers.KeyValueController;
import com.aerospike.restclient.domain.RestClientRecord;
import com.aerospike.restclient.service.AerospikeRecordService;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.AdditionalMatchers.aryEq;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KVControllerGetRecordTests {

    @Autowired
    KeyValueController controller;
    @MockBean
    AerospikeRecordService recordService;

    private final AerospikeException expectedException = new AerospikeException("test exception");

    private final String ns = "test";
    private final String set = "set";
    private final String key = "key";

    private MultiValueMap<String, String> queryParams;
    private final String[] binAry = {"bin1", "bin2", "bin3"};
    private final List<String> binList = Arrays.asList(binAry);

    private RestClientRecord testRecord;

    @Before
    /* Initialize the query params argument */ public void setup() {
        Map<String, Object> testBins = new HashMap<>();
        testBins.put("bin1", "val1");
        testBins.put("bin2", "val2");
        testRecord = new RestClientRecord(new Record(testBins, 2, 2));

        queryParams = new LinkedMultiValueMap<>();
    }

    @Test
    public void tesNSSetKey() {
        controller.getRecordNamespaceSetKey(ns, set, key, queryParams, null);
        verify(recordService, Mockito.only()).fetchRecord(isNull(), eq(ns), eq(set), eq(key), any(), isNull(),
                isA(Policy.class));
    }

    @Test
    public void testNSKey() {
        controller.getRecordNamespaceKey(ns, key, queryParams, null);
        verify(recordService, Mockito.only()).fetchRecord(isNull(), eq(ns), isNull(), eq(key), any(String[].class),
                isNull(), isA(Policy.class));
    }

    @Test
    public void tesBinsNSSetKey() {
        queryParams.put(AerospikeAPIConstants.RECORD_BINS, binList);
        controller.getRecordNamespaceSetKey(ns, set, key, queryParams, null);
        verify(recordService, Mockito.only()).fetchRecord(isNull(), eq(ns), eq(set), eq(key), aryEq(binAry), isNull(),
                isA(Policy.class));
    }

    @Test
    public void testBinsNSKey() {
        queryParams.put(AerospikeAPIConstants.RECORD_BINS, binList);
        controller.getRecordNamespaceKey(ns, key, queryParams, null);
        verify(recordService, Mockito.only()).fetchRecord(isNull(), eq(ns), isNull(), eq(key), aryEq(binAry), isNull(),
                isA(Policy.class));
    }

    @Test
    public void testNSSetKeyReturn() {
        when(recordService.fetchRecord(isNull(), anyString(), anyString(), anyString(), any(), isNull(),
                any(Policy.class))).thenReturn(testRecord);
        RestClientRecord actualRecord = controller.getRecordNamespaceSetKey(ns, set, key, queryParams, null);
        Assert.assertTrue(recordsEqual(testRecord, actualRecord));
    }

    @Test
    public void testBinsNSKeyReturn() {
        when(recordService.fetchRecord(isNull(), anyString(), isNull(), anyString(), any(), any(),
                any(Policy.class))).thenReturn(testRecord);
        RestClientRecord actualRecord = controller.getRecordNamespaceKey(ns, key, queryParams, null);
        Assert.assertTrue(recordsEqual(testRecord, actualRecord));
    }

    @Test(expected = AerospikeException.class)
    public void testErrorNSSetKey() {
        Mockito.doThrow(expectedException)
                .when(recordService)
                .fetchRecord(isNull(), any(String.class), any(String.class), any(String.class), any(), any(),
                        any(Policy.class));
        controller.getRecordNamespaceSetKey(ns, set, key, queryParams, null);
    }

    @Test(expected = AerospikeException.class)
    public void testErrorNSKey() {
        Mockito.doThrow(expectedException)
                .when(recordService)
                .fetchRecord(isNull(), any(String.class), isNull(), any(String.class), any(), isNull(),
                        any(Policy.class));
        controller.getRecordNamespaceKey(ns, key, queryParams, null);
    }

    private boolean recordsEqual(RestClientRecord expected, RestClientRecord actual) {
        return expected.generation == actual.generation && expected.ttl == actual.ttl && expected.bins.equals(
                actual.bins);
    }
}
