package com.aerospike.restclient.domain.batchmodels;

import com.aerospike.client.exp.Exp;
import com.aerospike.client.exp.Expression;
import com.aerospike.client.policy.*;
import com.aerospike.restclient.ASTestMapper;
import com.aerospike.restclient.IASTestMapper;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class RestClientBatchReadPolicyTest {
    private final IASTestMapper mapper;

    @Parameterized.Parameters
    public static Object[] getParams() {
        return new Object[] {
                new JsonRestClientBatchReadPolicyMapper(new ObjectMapper(new JsonFactory())),
                new MsgPackRestClientBatchReadPolicyMapper(new ObjectMapper(new MessagePackFactory()))
        };
    }

    public RestClientBatchReadPolicyTest(IASTestMapper mapper) {
        this.mapper = mapper;
    }

    @Test
    public void testNoArgConstructor() {
        new RestClientBatchWritePolicy();
    }

    @Test
    public void testObjectMappedBatchReadConstructionStringKey() throws Exception {
        Map<String, Object> policyMap = new HashMap<>();
        policyMap.put("filterExp", "a filter");
        policyMap.put("readModeAP", "ALL");
        policyMap.put("readModeSC", "ALLOW_REPLICA");

        RestClientBatchReadPolicy mappedBody = (RestClientBatchReadPolicy) mapper.bytesToObject(mapper.objectToBytes(policyMap));

        Assert.assertEquals("a filter", mappedBody.filterExp);
        Assert.assertEquals(ReadModeAP.ALL, mappedBody.readModeAP);
        Assert.assertEquals(ReadModeSC.ALLOW_REPLICA, mappedBody.readModeSC);
    }

    @Test
    public void testToBatchReadPolicy() {
        Expression expectedExp = Exp.build(Exp.ge(Exp.bin("a", Exp.Type.INT), Exp.bin("b", Exp.Type.INT)));
        String expectedExpStr = expectedExp.getBase64();
        RestClientBatchReadPolicy restPolicy = new RestClientBatchReadPolicy();
        restPolicy.filterExp = expectedExpStr;
        restPolicy.readModeAP = ReadModeAP.ALL;
        restPolicy.readModeSC = ReadModeSC.ALLOW_REPLICA;

        BatchReadPolicy actualPolicy = restPolicy.toBatchReadPolicy();

        Assert.assertEquals(expectedExp, actualPolicy.filterExp);
        Assert.assertEquals(ReadModeAP.ALL, actualPolicy.readModeAP);
        Assert.assertEquals(ReadModeSC.ALLOW_REPLICA, actualPolicy.readModeSC);
    }
}

class JsonRestClientBatchReadPolicyMapper extends ASTestMapper {

    public JsonRestClientBatchReadPolicyMapper(ObjectMapper mapper) {
        super(mapper, RestClientBatchReadPolicy.class);
    }
}

class MsgPackRestClientBatchReadPolicyMapper extends ASTestMapper {

    public MsgPackRestClientBatchReadPolicyMapper(ObjectMapper mapper) {
        super(mapper, RestClientBatchReadPolicy.class);
    }
}

