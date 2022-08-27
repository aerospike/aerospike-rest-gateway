package com.aerospike.restclient.domain.querymodels;

import com.aerospike.client.Value;
import com.aerospike.client.cdt.CTX;
import com.aerospike.client.cdt.ListOrder;
import com.aerospike.client.cdt.MapOrder;
import com.aerospike.restclient.ASTestMapper;
import com.aerospike.restclient.IASTestMapper;
import com.aerospike.restclient.config.JSONMessageConverter;
import com.aerospike.restclient.config.MsgPackConverter;
import com.aerospike.restclient.domain.ctxmodels.*;
import com.aerospike.restclient.util.AerospikeAPIConstants;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class RestClientQueryBodyTest {

    private final IASTestMapper mapper;
    private final static String ns = "test";
    private final static String set = "ctx";

    @Parameterized.Parameters
    public static Object[] getParams() {
        return new Object[]{
                new JsonQueryBodyMapper(),
                new MsgPackQueryBodyMapper(),
        };
    }

    public RestClientQueryBodyTest(IASTestMapper mapper) {
        this.mapper = mapper;
    }

//    @Test
//    public void testEmptyMapDoesNotMapToRestClientQueryBody() {
//        Map<String, Object> ctxMap = new HashMap<>();
//
//        try {
//            mapper.bytesToObject(mapper.objectToBytes(ctxMap));
//            Assert.fail("Should have not mapped to RestClientCTX");
//        } catch (Exception e) {
//            // Success
//        }
//    }
//
//    @Test
//    public void testMapsToRestClientCTXListIndex() {
//        Map<String, Object> ctxMap = new HashMap<>();
//        ctxMap.put("ctxType", AerospikeAPIConstants.LIST_INDEX);
//        ctxMap.put("index", 1);
//
//        try {
//            RestClientCTXListIndex restCTX = (RestClientCTXListIndex) mapper.bytesToObject(mapper.objectToBytes(ctxMap));
//            Assert.assertEquals(restCTX.ctxType, AerospikeAPIConstants.LIST_INDEX);
//            Assert.assertEquals(restCTX.index.intValue(), 1);
//        } catch (Exception e) {
//            Assert.fail(String.format("Should have mapped to RestClientCTXListIndex %s", e));
//        }
//    }
//
//    @Test
//    public void testRestClientCTXListIndexToASCTX() {
//        CTX expected = CTX.listIndex(5);
//
//        RestClientCTXListIndex listIndex = new RestClientCTXListIndex();
//        listIndex.index = 5;
//
////        assertEquals(expected, listIndex.toCTX());
//    }
}

class JsonQueryBodyMapper extends ASTestMapper {

    public JsonQueryBodyMapper() {
        super(JSONMessageConverter.getJSONObjectMapper(), RestClientQueryBody.class);
    }
}

class MsgPackQueryBodyMapper extends ASTestMapper {

    public MsgPackQueryBodyMapper() {
        super(MsgPackConverter.getASMsgPackObjectMapper(), RestClientQueryBody.class);
    }
}
