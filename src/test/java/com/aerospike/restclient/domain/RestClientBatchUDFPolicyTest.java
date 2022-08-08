package com.aerospike.restclient.domain;

import com.aerospike.client.exp.Exp;
import com.aerospike.client.exp.Expression;
import com.aerospike.client.policy.*;
import com.aerospike.restclient.ASJsonTestMapper;
import com.aerospike.restclient.ASMsgPackTestMapper;
import com.aerospike.restclient.ASTestMapper;
import com.aerospike.restclient.domain.batchmodels.RestClientBatchUDFPolicy;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.msgpack.jackson.dataformat.MessagePackFactory;

import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class RestClientBatchUDFPolicyTest {
    private final ASTestMapper mapper;

    @Parameterized.Parameters
    public static Object[] getParams() {
        return new Object[] {
                new JsonRestClientBatchUDFPolicyMapper(new ObjectMapper()),
                new MsgPackRestClientBatchUDFPolicyMapper(new ObjectMapper(new MessagePackFactory()))
        };
    }

    public RestClientBatchUDFPolicyTest(ASTestMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    public void testNoArgConstructor() {
        new RestClientBatchUDFPolicy();
    }

    @Test
    public void testObjectMappedBatchUDFConstructionStringKey() throws Exception {
        Map<String, Object> policyMap = new HashMap<>();
        policyMap.put("filterExp", "a filter");
        policyMap.put("commitLevel", "COMMIT_MASTER");
        policyMap.put("expiration", 101);
        policyMap.put("durableDelete", true);
        policyMap.put("sendKey", true);

        RestClientBatchUDFPolicy mappedBody = (RestClientBatchUDFPolicy) mapper.mapToObject(policyMap);

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
        RestClientBatchUDFPolicy restPolicy = new RestClientBatchUDFPolicy();
        restPolicy.filterExp = expectedExpStr;
        restPolicy.commitLevel = CommitLevel.COMMIT_MASTER;
        restPolicy.expiration = 101;
        restPolicy.durableDelete = true;
        restPolicy.sendKey = true;

        BatchUDFPolicy actualPolicy = restPolicy.toBatchUDFPolicy();

        Assert.assertEquals(expectedExp, actualPolicy.filterExp);
        Assert.assertEquals(CommitLevel.COMMIT_MASTER, actualPolicy.commitLevel);
        Assert.assertEquals(101, actualPolicy.expiration);
        Assert.assertTrue(actualPolicy.durableDelete);
        Assert.assertTrue(actualPolicy.sendKey);
    }
}

class JsonRestClientBatchUDFPolicyMapper extends ASJsonTestMapper {

    public JsonRestClientBatchUDFPolicyMapper(ObjectMapper mapper) {
        super(mapper, RestClientBatchUDFPolicy.class);
    }
}

class MsgPackRestClientBatchUDFPolicyMapper extends ASMsgPackTestMapper {

    public MsgPackRestClientBatchUDFPolicyMapper(ObjectMapper mapper) {
        super(mapper, RestClientBatchUDFPolicy.class);
    }
}

