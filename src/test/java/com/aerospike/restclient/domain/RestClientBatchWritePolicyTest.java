package com.aerospike.restclient.domain;

import com.aerospike.client.exp.Exp;
import com.aerospike.client.exp.Expression;
import com.aerospike.client.policy.BatchWritePolicy;
import com.aerospike.client.policy.CommitLevel;
import com.aerospike.client.policy.GenerationPolicy;
import com.aerospike.client.policy.RecordExistsAction;
import com.aerospike.restclient.ASJsonTestMapper;
import com.aerospike.restclient.ASMsgPackTestMapper;
import com.aerospike.restclient.ASTestMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class RestClientBatchWritePolicyTest {
    private final ASTestMapper mapper;

    @Parameterized.Parameters
    public static Object[] getParams() {
        return new Object[] {
                new JsonRestClientBatchWritePolicyMapper(new ObjectMapper()),
                new MsgPackRestClientBatchWritePolicyMapper(new ObjectMapper(new MessagePackFactory()))
        };
    }

    public RestClientBatchWritePolicyTest(ASTestMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    public void testNoArgConstructor() {
        new RestClientBatchWritePolicy();
    }

    @Test
    public void testObjectMappedBatchWriteConstructionStringKey() throws Exception {
        Map<String, Object>policyMap = new HashMap<>();
        policyMap.put("filterExp", "a filter");
        policyMap.put("recordExistsAction", "CREATE_ONLY");
        policyMap.put("commitLevel", "COMMIT_MASTER");
        policyMap.put("generationPolicy", "EXPECT_GEN_GT");
        policyMap.put("generation", 101);
        policyMap.put("expiration", 102);
        policyMap.put("durableDelete", true);
        policyMap.put("sendKey", true);

        RestClientBatchWritePolicy mappedBody = (RestClientBatchWritePolicy) mapper.mapToObject(policyMap);

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
        RestClientBatchWritePolicy restPolicy = new RestClientBatchWritePolicy();
        restPolicy.filterExp = expectedExpStr;
        restPolicy.recordExistsAction = RecordExistsAction.CREATE_ONLY;
        restPolicy.commitLevel = CommitLevel.COMMIT_MASTER;
        restPolicy.generationPolicy = GenerationPolicy.EXPECT_GEN_EQUAL;
        restPolicy.generation = 99;
        restPolicy.expiration = 100;
        restPolicy.durableDelete = true;
        restPolicy.sendKey = true;

        BatchWritePolicy actualPolicy = restPolicy.toBatchWritePolicy();

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


class JsonRestClientBatchWritePolicyMapper extends ASJsonTestMapper {

    public JsonRestClientBatchWritePolicyMapper(ObjectMapper mapper) {
        super(mapper, RestClientBatchWritePolicy.class);
    }
}

class MsgPackRestClientBatchWritePolicyMapper extends ASMsgPackTestMapper {

    public MsgPackRestClientBatchWritePolicyMapper(ObjectMapper mapper) {
        super(mapper, RestClientBatchWritePolicy.class);
    }
}
