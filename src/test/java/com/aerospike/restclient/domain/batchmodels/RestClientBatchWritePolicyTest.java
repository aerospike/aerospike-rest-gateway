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
import com.aerospike.client.policy.GenerationPolicy;
import com.aerospike.client.policy.RecordExistsAction;
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
public class RestClientBatchWritePolicyTest {
    private final IASTestMapper mapper;

    @Parameterized.Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JsonRestClientBatchWritePolicyMapper(), new MsgPackRestClientBatchWritePolicyMapper()
        };
    }

    public RestClientBatchWritePolicyTest(IASTestMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    public void testNoArgConstructor() {
        new BatchWritePolicy();
    }

    @Test
    public void testObjectMappedBatchWriteConstructionStringKey() throws Exception {
        Map<String, Object> policyMap = new HashMap<>();
        policyMap.put("filterExp", "a filter");
        policyMap.put("recordExistsAction", "CREATE_ONLY");
        policyMap.put("commitLevel", "COMMIT_MASTER");
        policyMap.put("generationPolicy", "EXPECT_GEN_GT");
        policyMap.put("generation", 101);
        policyMap.put("expiration", 102);
        policyMap.put("durableDelete", true);
        policyMap.put("sendKey", true);

        BatchWritePolicy mappedBody = (BatchWritePolicy) mapper.bytesToObject(mapper.objectToBytes(policyMap));

        Assert.assertEquals("a filter", mappedBody.filterExp);
        Assert.assertEquals(RecordExistsAction.CREATE_ONLY, mappedBody.recordExistsAction);
        Assert.assertEquals(CommitLevel.COMMIT_MASTER, mappedBody.commitLevel);
        Assert.assertEquals(GenerationPolicy.EXPECT_GEN_GT, mappedBody.generationPolicy);
        Assert.assertEquals(101, mappedBody.generation);
        Assert.assertEquals(102, mappedBody.expiration);
        Assert.assertTrue(mappedBody.durableDelete);
        Assert.assertTrue(mappedBody.sendKey);
    }

    @Test
    public void testToBatchWritePolicy() {
        Expression expectedExp = Exp.build(Exp.ge(Exp.bin("a", Exp.Type.INT), Exp.bin("b", Exp.Type.INT)));
        String expectedExpStr = expectedExp.getBase64();
        BatchWritePolicy restPolicy = new BatchWritePolicy();
        restPolicy.filterExp = expectedExpStr;
        restPolicy.recordExistsAction = RecordExistsAction.CREATE_ONLY;
        restPolicy.commitLevel = CommitLevel.COMMIT_MASTER;
        restPolicy.generationPolicy = GenerationPolicy.EXPECT_GEN_EQUAL;
        restPolicy.generation = 99;
        restPolicy.expiration = 100;
        restPolicy.durableDelete = true;
        restPolicy.sendKey = true;

        com.aerospike.client.policy.BatchWritePolicy actualPolicy = restPolicy.toBatchWritePolicy();

        Assert.assertEquals(expectedExp, actualPolicy.filterExp);
        Assert.assertEquals(RecordExistsAction.CREATE_ONLY, actualPolicy.recordExistsAction);
        Assert.assertEquals(CommitLevel.COMMIT_MASTER, actualPolicy.commitLevel);
        Assert.assertEquals(GenerationPolicy.EXPECT_GEN_EQUAL, actualPolicy.generationPolicy);
        Assert.assertEquals(99, actualPolicy.generation);
        Assert.assertEquals(100, actualPolicy.expiration);
        Assert.assertTrue(actualPolicy.durableDelete);
        Assert.assertTrue(actualPolicy.sendKey);
    }
}

class JsonRestClientBatchWritePolicyMapper extends ASTestMapper {

    public JsonRestClientBatchWritePolicyMapper() {
        super(JSONMessageConverter.getJSONObjectMapper(), BatchWritePolicy.class);
    }
}

class MsgPackRestClientBatchWritePolicyMapper extends ASTestMapper {

    public MsgPackRestClientBatchWritePolicyMapper() {
        super(MsgPackConverter.getASMsgPackObjectMapper(), BatchWritePolicy.class);
    }
}
