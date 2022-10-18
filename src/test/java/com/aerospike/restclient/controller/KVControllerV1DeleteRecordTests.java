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
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.client.policy.WritePolicy;
import com.aerospike.restclient.ASTestUtils.WritePolicyMatcher;
import com.aerospike.restclient.ASTestUtils.WritePolicyMatcher.WritePolicyComparator;
import com.aerospike.restclient.controllers.KeyValueController;
import com.aerospike.restclient.service.AerospikeRecordService;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest
public class KVControllerV1DeleteRecordTests {

    @Autowired
    KeyValueController controller;
    @MockBean
    AerospikeRecordService recordService;
    private final AerospikeException expectedException = new AerospikeException("test exception");

    private final WritePolicyComparator existsActionComparator = (p1, p2) -> p1.recordExistsAction == p2.recordExistsAction;

    private final String ns = "test";
    private final String set = "set";
    private final String key = "key";

    private Map<String, String> queryParams;

    @Before
    /* Initialize the query params argument */ public void setup() {
        queryParams = new HashMap<>();
    }

    @Test
    public void tesNSSetKey() {
        controller.deleteRecordNamespaceSetKey(ns, set, key, queryParams, null);
        verify(recordService, Mockito.only()).deleteRecord(isNull(), eq(ns), eq(set), eq(key), isNull(),
                isA(WritePolicy.class));
    }

    @Test
    public void testNSKey() {
        controller.deleteRecordNamespaceKey(ns, key, queryParams, null);
        verify(recordService, Mockito.only()).deleteRecord(isNull(), eq(ns), isNull(), eq(key), isNull(),
                isA(WritePolicy.class));
    }

    @Test
    public void testWritePolicyNSSetKey() {
        WritePolicy expectedPolicy = new WritePolicy();
        expectedPolicy.recordExistsAction = RecordExistsAction.CREATE_ONLY;
        WritePolicyMatcher matcher = new WritePolicyMatcher(expectedPolicy, existsActionComparator);
        queryParams.put(AerospikeAPIConstants.RECORD_EXISTS_ACTION, RecordExistsAction.CREATE_ONLY.toString());
        controller.deleteRecordNamespaceSetKey(ns, set, key, queryParams, null);
        verify(recordService, Mockito.only()).deleteRecord(isNull(), eq(ns), eq(set), eq(key), isNull(),
                argThat(matcher));
    }

    @Test
    public void testWritePolicyNSKey() {
        WritePolicy expectedPolicy = new WritePolicy();
        expectedPolicy.recordExistsAction = RecordExistsAction.CREATE_ONLY;
        WritePolicyMatcher matcher = new WritePolicyMatcher(expectedPolicy, existsActionComparator);
        queryParams.put(AerospikeAPIConstants.RECORD_EXISTS_ACTION, RecordExistsAction.CREATE_ONLY.toString());

        controller.deleteRecordNamespaceKey(ns, key, queryParams, null);
        verify(recordService, Mockito.only()).deleteRecord(isNull(), eq(ns), isNull(), eq(key), isNull(),
                argThat(matcher));
    }

    @Test(expected = AerospikeException.class)
    public void testErrorNSSetKey() {
        Mockito.doThrow(expectedException)
                .when(recordService)
                .deleteRecord(isNull(), any(String.class), any(String.class), any(String.class), any(), any());
        controller.deleteRecordNamespaceSetKey(ns, set, key, queryParams, null);
    }

    @Test(expected = AerospikeException.class)
    public void testErrorNSKey() {
        Mockito.doThrow(expectedException)
                .when(recordService)
                .deleteRecord(isNull(), any(String.class), isNull(), any(String.class), any(), any());
        controller.deleteRecordNamespaceKey(ns, key, queryParams, null);
    }
}
