package com.aerospike.restclient.domain;

import com.aerospike.client.exp.Exp;
import com.aerospike.client.exp.Expression;
import com.aerospike.client.policy.BatchDeletePolicy;
import com.aerospike.client.policy.CommitLevel;
import com.aerospike.client.policy.GenerationPolicy;
import com.aerospike.restclient.ASJsonTestMapper;
import com.aerospike.restclient.ASMsgPackTestMapper;
import com.aerospike.restclient.ASTestMapper;
import com.aerospike.restclient.domain.batchmodels.RestClientBatchDeletePolicy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class RestClientBatchDeletePolicyTest {
    private final ASTestMapper mapper;

    @Parameterized.Parameters
    public static Object[] getParams() {
        return new Object[] {
                new JsonRestClientBatchDeletePolicyMapper(new ObjectMapper()),
                new MsgPackRestClientBatchDeletePolicyMapper(new ObjectMapper(new MessagePackFactory()))
        };
    }

    public RestClientBatchDeletePolicyTest(ASTestMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    public void testNoArgConstructor() {
        new RestClientBatchDeletePolicy();
    }

    @Test
    public void testObjectMappedBatchDeleteConstructionStringKey() throws Exception {
        Map<String, Object> policyMap = new HashMap<>();
        policyMap.put("filterExp", "a filter");
        policyMap.put("commitLevel", "COMMIT_MASTER");
        policyMap.put("generationPolicy", "EXPECT_GEN_EQUAL");
        policyMap.put("generation", 101);
        policyMap.put("durableDelete", true);
        policyMap.put("sendKey", true);

        RestClientBatchDeletePolicy mappedBody = (RestClientBatchDeletePolicy) mapper.mapToObject(policyMap);

        Assert.assertEquals("a filter", mappedBody.filterExp);
        Assert.assertEquals(CommitLevel.COMMIT_MASTER, mappedBody.commitLevel);
        Assert.assertEquals(GenerationPolicy.EXPECT_GEN_EQUAL, mappedBody.generationPolicy);
        Assert.assertEquals(101, mappedBody.generation);
        Assert.assertTrue(mappedBody.durableDelete);
        Assert.assertTrue(mappedBody.sendKey);
    }

    @Test
    public void testToBatchDeletePolicy() {
        Expression expectedExp = Exp.build(Exp.ge(Exp.bin("a", Exp.Type.INT), Exp.bin("b", Exp.Type.INT)));
        String expectedExpStr = expectedExp.getBase64();
        RestClientBatchDeletePolicy restPolicy = new RestClientBatchDeletePolicy();
        restPolicy.filterExp = expectedExpStr;
        restPolicy.commitLevel = CommitLevel.COMMIT_MASTER;
        restPolicy.generationPolicy = GenerationPolicy.EXPECT_GEN_EQUAL;
        restPolicy.generation = 101;
        restPolicy.durableDelete = true;
        restPolicy.sendKey = true;

        BatchDeletePolicy actualPolicy = restPolicy.toBatchDeletePolicy();

        Assert.assertEquals(expectedExp, actualPolicy.filterExp);
        Assert.assertEquals(CommitLevel.COMMIT_MASTER, actualPolicy.commitLevel);
        Assert.assertEquals(GenerationPolicy.EXPECT_GEN_EQUAL, actualPolicy.generationPolicy);
        Assert.assertEquals(101, actualPolicy.generation);
        Assert.assertTrue(actualPolicy.durableDelete);
        Assert.assertTrue(actualPolicy.sendKey);
    }
}

class JsonRestClientBatchDeletePolicyMapper extends ASJsonTestMapper {

    public JsonRestClientBatchDeletePolicyMapper(ObjectMapper mapper) {
        super(mapper, RestClientBatchDeletePolicy.class);
    }
}

class MsgPackRestClientBatchDeletePolicyMapper extends ASMsgPackTestMapper {

    public MsgPackRestClientBatchDeletePolicyMapper(ObjectMapper mapper) {
        super(mapper, RestClientBatchDeletePolicy.class);
    }
}