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
import com.aerospike.client.policy.CommitLevel;
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
public class RestClientBatchUDFPolicyTest {
    private final IASTestMapper mapper;

    @Parameterized.Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JsonRestClientBatchUDFPolicyMapper(), new MsgPackRestClientBatchUDFPolicyMapper()
        };
    }

    public RestClientBatchUDFPolicyTest(IASTestMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    public void testNoArgConstructor() {
        new BatchUDFPolicy();
    }

    @Test
    public void testObjectMappedBatchUDFConstructionStringKey() throws Exception {
        Map<String, Object> policyMap = new HashMap<>();
        policyMap.put("filterExp", "a filter");
        policyMap.put("commitLevel", "COMMIT_MASTER");
        policyMap.put("expiration", 101);
        policyMap.put("durableDelete", true);
        policyMap.put("sendKey", true);

        BatchUDFPolicy mappedBody = (BatchUDFPolicy) mapper.bytesToObject(mapper.objectToBytes(policyMap));

        Assert.assertEquals("a filter", mappedBody.filterExp);
        Assert.assertEquals(CommitLevel.COMMIT_MASTER, mappedBody.commitLevel);
        Assert.assertEquals(101, mappedBody.expiration);
        Assert.assertTrue(mappedBody.durableDelete);
        Assert.assertTrue(mappedBody.sendKey);
    }

    @Test
    public void testToBatchUDFPolicy() {
        Expression expectedExp = Exp.build(Exp.ge(Exp.bin("a", Exp.Type.INT), Exp.bin("b", Exp.Type.INT)));
        String expectedExpStr = expectedExp.getBase64();
        BatchUDFPolicy restPolicy = new BatchUDFPolicy();
        restPolicy.filterExp = expectedExpStr;
        restPolicy.commitLevel = CommitLevel.COMMIT_MASTER;
        restPolicy.expiration = 101;
        restPolicy.durableDelete = true;
        restPolicy.sendKey = true;

        com.aerospike.client.policy.BatchUDFPolicy actualPolicy = restPolicy.toBatchUDFPolicy();

        Assert.assertEquals(expectedExp, actualPolicy.filterExp);
        Assert.assertEquals(CommitLevel.COMMIT_MASTER, actualPolicy.commitLevel);
        Assert.assertEquals(101, actualPolicy.expiration);
        Assert.assertTrue(actualPolicy.durableDelete);
        Assert.assertTrue(actualPolicy.sendKey);
    }
}

class JsonRestClientBatchUDFPolicyMapper extends ASTestMapper {

    public JsonRestClientBatchUDFPolicyMapper() {
        super(JSONMessageConverter.getJSONObjectMapper(), BatchUDFPolicy.class);
    }
}

class MsgPackRestClientBatchUDFPolicyMapper extends ASTestMapper {

    public MsgPackRestClientBatchUDFPolicyMapper() {
        super(MsgPackConverter.getASMsgPackObjectMapper(), BatchUDFPolicy.class);
    }
}

