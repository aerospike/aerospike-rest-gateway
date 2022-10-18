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

import com.aerospike.client.exp.Exp;
import com.aerospike.client.exp.Expression;
import com.aerospike.client.policy.ReadModeAP;
import com.aerospike.client.policy.ReadModeSC;
import com.aerospike.restclient.ASTestMapper;
import com.aerospike.restclient.IASTestMapper;
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.config.MsgPackConverter;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class RestClientBatchReadPolicyTest {
    private final IASTestMapper mapper;

    @Parameterized.Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JsonRestClientBatchReadPolicyMapper(), new MsgPackRestClientBatchReadPolicyMapper()
        };
    }

    public RestClientBatchReadPolicyTest(IASTestMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    public void testNoArgConstructor() {
        new BatchWritePolicy();
    }

    @Test
    public void testObjectMappedBatchReadConstructionStringKey() throws Exception {
        Map<String, Object> policyMap = new HashMap<>();
        policyMap.put("filterExp", "a filter");
        policyMap.put("readModeAP", "ALL");
        policyMap.put("readModeSC", "ALLOW_REPLICA");

        BatchReadPolicy mappedBody = (BatchReadPolicy) mapper.bytesToObject(mapper.objectToBytes(policyMap));

        Assert.assertEquals("a filter", mappedBody.filterExp);
        Assert.assertEquals(ReadModeAP.ALL, mappedBody.readModeAP);
        Assert.assertEquals(ReadModeSC.ALLOW_REPLICA, mappedBody.readModeSC);
    }

    @Test
    public void testToBatchReadPolicy() {
        Expression expectedExp = Exp.build(Exp.ge(Exp.bin("a", Exp.Type.INT), Exp.bin("b", Exp.Type.INT)));
        String expectedExpStr = expectedExp.getBase64();
        BatchReadPolicy restPolicy = new BatchReadPolicy();
        restPolicy.filterExp = expectedExpStr;
        restPolicy.readModeAP = ReadModeAP.ALL;
        restPolicy.readModeSC = ReadModeSC.ALLOW_REPLICA;

        com.aerospike.client.policy.BatchReadPolicy actualPolicy = restPolicy.toBatchReadPolicy();

        Assert.assertEquals(expectedExp, actualPolicy.filterExp);
        Assert.assertEquals(ReadModeAP.ALL, actualPolicy.readModeAP);
        Assert.assertEquals(ReadModeSC.ALLOW_REPLICA, actualPolicy.readModeSC);
    }
}

class JsonRestClientBatchReadPolicyMapper extends ASTestMapper {

    public JsonRestClientBatchReadPolicyMapper() {
        super(JSONMessageConverter.getJSONObjectMapper(), BatchReadPolicy.class);
    }
}

class MsgPackRestClientBatchReadPolicyMapper extends ASTestMapper {

    public MsgPackRestClientBatchReadPolicyMapper() {
        super(MsgPackConverter.getASMsgPackObjectMapper(), BatchReadPolicy.class);
    }
}

