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
import com.aerospike.restclient.controllers.KeyValueController;
import com.aerospike.restclient.service.AerospikeRecordService;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import com.aerospike.restclient.util.AerospikeAPIConstants.RecordKeyType;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

@RunWith(Parameterized.class)
@SpringBootTest
public class KVControllerGetRecordKeyTypeTests {

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

    private MultiValueMap<String, String> queryParams;
    private final RecordKeyType expectedKeyType;

    @Parameters
    public static Object[] keyType() {
        return new Object[]{
                RecordKeyType.STRING, RecordKeyType.BYTES, RecordKeyType.DIGEST, RecordKeyType.INTEGER, null
        };
    }

    public KVControllerGetRecordKeyTypeTests(RecordKeyType keyType) {
        this.expectedKeyType = keyType;
    }

    @Before
    public void setup() {
        queryParams = new LinkedMultiValueMap<>();
        if (expectedKeyType != null) {
            queryParams.put(AerospikeAPIConstants.KEY_TYPE, Collections.singletonList(expectedKeyType.toString()));
        }
    }

    @Test
    public void testKeyTypeForNSSetKey() {
        controller.getRecordNamespaceSetKey(ns, set, key, queryParams, null);
        verify(recordService, Mockito.only()).fetchRecord(isNull(), any(String.class), any(String.class),
                any(String.class), any(String[].class), eq(expectedKeyType), isA(Policy.class));
    }

    @Test
    public void testKeyTypeForNSKey() {
        controller.getRecordNamespaceKey(ns, key, queryParams, null);
        verify(recordService, Mockito.only()).fetchRecord(isNull(), any(String.class), isNull(), any(String.class),
                any(String[].class), eq(expectedKeyType), isA(Policy.class));
    }

}
